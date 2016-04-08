package br.com.laminarsoft.jazzforms.persistencia.dao;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.internal.ApnsConnection;
import com.notnoop.exceptions.InvalidSSLConfig;
import com.notnoop.exceptions.NetworkIOException;
import com.notnoop.exceptions.RuntimeIOException;

import br.com.laminarsoft.jazzforms.logging.types.MessageData;
import br.com.laminarsoft.jazzforms.persistencia.model.Usuario;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.IMensagensErro;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.ParametroException;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.AlertaVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.UsuarioAlertaVO;
import br.com.laminarsoft.jazzforms.negocio.controller.util.PropertiesServiceController;

@Transactional(propagation=Propagation.REQUIRED, 
	isolation=Isolation.READ_COMMITTED, 
	timeout=60)
@Repository("AlertaDao")
@SuppressWarnings("all")
public class AlertaDao extends BaseDao<Usuario> {
	private static final String SEMAFORO = "semaforo";
	
	@Autowired private EhCacheCacheManager cacheManager;
	@Autowired private PropertiesServiceController properties;

	public AlertaVO addAlerta(final AlertaVO alerta) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String query = "insert into alerta (login_usuario, mensagem, dh_envio, enviado) values (?,?,?,?)";
				PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				pstmt.setString(1, alerta.loginUsuario);
				pstmt.setString(2, alerta.mensagem);
				pstmt.setTimestamp(3, new Timestamp(alerta.dhEnvio.getTime()));
				pstmt.setBoolean(4, alerta.enviado);
				pstmt.executeUpdate();
				
				ResultSet rs = pstmt.getGeneratedKeys();
				if(rs.next()) {
					alerta.id = rs.getLong(1);
				}
				rs.close();
				pstmt.close();
			}
		});
		return alerta;
	}
	
	public List<AlertaVO> getAlertasUsuarioNaoEnviados(final String loginUsuario) {
		final List<AlertaVO> alertas = new ArrayList<AlertaVO>();
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String qAlertas = "select * from alerta where login_usuario = ? and enviado = false";
				PreparedStatement pstmt = connection.prepareStatement(qAlertas);
				pstmt.setString(1, loginUsuario);
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()) {
					Long id = rs.getLong("id");
					String mensagem = rs.getString("mensagem");
					Date dhEnvio = rs.getDate("dh_envio");
					AlertaVO vo = new AlertaVO();
					vo.dhEnvio = dhEnvio;
					vo.mensagem = mensagem;
					vo.id = id;
					vo.loginUsuario = loginUsuario;
					alertas.add(vo);
				}
			}
		});
		
		return alertas;
	}
	
	public void setAlertasEnviados(final List<AlertaVO> alertas) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				PreparedStatement pstmt = connection.prepareStatement("update alerta set enviado = true where id = ?");
				for(AlertaVO alerta : alertas) {
					pstmt.clearParameters();
					pstmt.setLong(1, alerta.id);
					pstmt.executeUpdate();
				}
			}
		});
	}	
	
	public void removeAlerta(final Long id) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				PreparedStatement pstmt = connection.prepareStatement("delete from alerta where id = ?");
				pstmt.setLong(1, id);
				pstmt.executeUpdate();
			}
		});
	}
	
	/**
	 * Envia alertas disponíveis para o usuário em questão.
	 * @param login Usuário destinatário da mensagem
	 * @param titulo
	 * @param msg A mensagem a ser enviada ao usuário. Formato: tipo_mensagem:<tipo_mensagem>,detalhes: <detalhes_mensagem>, id_mensagem:  
	 * 			  onde tipo_mensagem pode ser: 1 - Nova mensagem de texto, 2 - Atualização de aplicação, 3 - nova aplicação, 4 - Remoção de aplicação 
	 */
	public void enviaAlertaUsuario(final String login, String titulo, String msg, String tipoMensagem) {
		try {

	        List<UsuarioAlertaVO> lstUsrAlerta = getUsuarioAlerta(login);
	        MessageData msgData = new MessageData();
	        msgData.createData(titulo, msg);
	        
	        Gson gson = new Gson();
	        String jsonMensagem = gson.toJson(msgData);
	        
        	AlertaVO alert = new AlertaVO();
        	alert.dhEnvio = new Date();
        	alert.enviado = false;
        	alert.loginUsuario = login;
        	alert.mensagem = jsonMensagem;
        	alert = addAlerta(alert);

	        if(lstUsrAlerta.size() > 0) {
	        	List<UsuarioAlertaVO> usuariosAndroid = new ArrayList<UsuarioAlertaVO>();
	        	List<UsuarioAlertaVO> usuariosIOS = new ArrayList<UsuarioAlertaVO>();
	        	for(UsuarioAlertaVO usrAlrt : lstUsrAlerta) {
	        		if(usrAlrt.tipoAparelho.equalsIgnoreCase("ANDROID")){
	        			usuariosAndroid.add(usrAlrt);
	        		} else if(usrAlrt.tipoAparelho.equalsIgnoreCase("IOS")) {
	        			usuariosIOS.add(usrAlrt);
	        		}
	        	}
	        	if (usuariosAndroid.size() > 0) {
					enviaAlertaAndroid(titulo, usuariosAndroid, msgData, alert, tipoMensagem);
				}
	        	if(usuariosIOS.size() > 0) {
	        		enviaAlertaIOS(titulo, usuariosIOS, msgData, alert, tipoMensagem);
	        	}
	        }
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void enviaAlertaGrupo(final String grupo, String titulo, String msg, String tipoMensagem) {
		try {
	        List<UsuarioAlertaVO> lstUsrAlerta = getUsuariosGrupo(grupo);
	        MessageData msgData = new MessageData();
	        msgData.createData(titulo, msg);
	        
	        Gson gson = new Gson();
	        String jsonMensagem = gson.toJson(msgData);
	        
        	AlertaVO alert = new AlertaVO();
        	alert.dhEnvio = new Date();
        	alert.enviado = false;
        	alert.loginUsuario = "";
        	alert.mensagem = jsonMensagem;
        	alert = addAlerta(alert);

	        if(lstUsrAlerta.size() > 0) {
	        	List<UsuarioAlertaVO> usuariosAndroid = new ArrayList<UsuarioAlertaVO>();
	        	List<UsuarioAlertaVO> usuariosIOS = new ArrayList<UsuarioAlertaVO>();
	        	for(UsuarioAlertaVO usrAlrt : lstUsrAlerta) {
	        		if(usrAlrt.tipoAparelho.equalsIgnoreCase("ANDROID")){
	        			usuariosAndroid.add(usrAlrt);
	        		} else if(usrAlrt.tipoAparelho.equalsIgnoreCase("IOS")) {
	        			usuariosIOS.add(usrAlrt);
	        		}
	        	}
	        	if (usuariosAndroid.size() > 0) {
					enviaAlertaAndroid(titulo, usuariosAndroid, msgData, alert, tipoMensagem);
				}
	        	if(usuariosIOS.size() > 0) {
	        		enviaAlertaIOS(titulo, usuariosIOS, msgData, alert, tipoMensagem);
	        	}
	        }
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void enviaAlertaEquipamento(final Long equipamentoId, String titulo, String msg, String tipoMensagem) {
		try {

	        UsuarioAlertaVO usrAlrt = getUsuarioEquipamento(equipamentoId);
	        MessageData msgData = new MessageData();
	        msgData.createData(titulo, msg);
	        
	        Gson gson = new Gson();
	        String jsonMensagem = gson.toJson(msgData);
	        
        	AlertaVO alert = new AlertaVO();
        	alert.dhEnvio = new Date();
        	alert.enviado = false;
        	alert.loginUsuario = "";
        	alert.mensagem = jsonMensagem;
        	alert = addAlerta(alert);

        	List<UsuarioAlertaVO> usuariosAndroid = new ArrayList<UsuarioAlertaVO>();
        	List<UsuarioAlertaVO> usuariosIOS = new ArrayList<UsuarioAlertaVO>();
    		if(usrAlrt.tipoAparelho.equalsIgnoreCase("ANDROID")){
    			usuariosAndroid.add(usrAlrt);
    		} else if(usrAlrt.tipoAparelho.equalsIgnoreCase("IOS")) {
    			usuariosIOS.add(usrAlrt);
    		}
        	if (usuariosAndroid.size() > 0) {
				enviaAlertaAndroid(titulo, usuariosAndroid, msgData, alert, tipoMensagem);
			}
        	if(usuariosIOS.size() > 0) {
        		enviaAlertaIOS(titulo, usuariosIOS, msgData, alert, tipoMensagem);
        	}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	public void enviaAlertaDeployment(final Long idDeployment, String titulo, String msg, String tipoMensagem) {
		try {
	        List<UsuarioAlertaVO> lstUsrAlerta = getUsuariosDeployment(idDeployment);
	        MessageData msgData = new MessageData();
	        msgData.createData(titulo, msg);
	        
	        Gson gson = new Gson();
	        String jsonMensagem = gson.toJson(msgData);
	        
        	AlertaVO alert = new AlertaVO();
        	alert.dhEnvio = new Date();
        	alert.enviado = false;
        	alert.loginUsuario = "";
        	alert.mensagem = jsonMensagem;
        	alert = addAlerta(alert);

	        if(lstUsrAlerta.size() > 0) {
	        	List<UsuarioAlertaVO> usuariosAndroid = new ArrayList<UsuarioAlertaVO>();
	        	List<UsuarioAlertaVO> usuariosIOS = new ArrayList<UsuarioAlertaVO>();
	        	for(UsuarioAlertaVO usrAlrt : lstUsrAlerta) {
	        		if(usrAlrt.tipoAparelho.equalsIgnoreCase("ANDROID")){
	        			usuariosAndroid.add(usrAlrt);
	        		} else if(usrAlrt.tipoAparelho.equalsIgnoreCase("IOS")) {
	        			usuariosIOS.add(usrAlrt);
	        		}
	        	}
	        	if (usuariosAndroid.size() > 0) {
					enviaAlertaAndroid(titulo, usuariosAndroid, msgData, alert, tipoMensagem);
				}
	        	if(usuariosIOS.size() > 0) {
	        		enviaAlertaIOS(titulo, usuariosIOS, msgData, alert, tipoMensagem);
	        	}
	        }
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void enviaAlertaAndroid(String titulo, List<UsuarioAlertaVO> lstUsrAlerta, MessageData msgData,
			AlertaVO alert, String tipoMensagem) throws MalformedURLException, IOException,
			ProtocolException, JsonGenerationException, JsonMappingException {
		for(UsuarioAlertaVO usrVO : lstUsrAlerta) {
			msgData.addRegId(usrVO.idMsgUsuario);
		}
		msgData.putAttribute("atributos", "{tipo_mensagem:" + tipoMensagem + ", id_mensagem: " + alert.id + "}");
		ObjectMapper mapper = new ObjectMapper();
		
		
		URL url = new URL(properties.getProperty("google.gsm.url"));
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Authorization", "key=" + properties.getProperty("google.api.key"));        
		conn.setDoOutput(true);

		try {
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			mapper.writeValue(wr, msgData);
			wr.flush();
			wr.close();
			int responseCode = conn.getResponseCode();
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
//		List<AlertaVO> alertas = new ArrayList<AlertaVO>();
//		alertas.add(alert);
//		setAlertasEnviados(alertas);
	}
	
	private void enviaAlertaIOS(String titulo, List<UsuarioAlertaVO> lstUsrAlerta, MessageData msgData,
			AlertaVO alert, String tipoMensagem) {
		String fileName = properties.getProperty("ios.key.file");
		String filePasswd = properties.getProperty("ios.key.file.passwd");
		
		try {
			String producao = properties.getProperty("ios.push.production");
			boolean bprod = producao.equalsIgnoreCase("T");
			ApnsService service = APNS.newService().withCert(this.getClass().getResourceAsStream(fileName), filePasswd).withAppleDestination(bprod).build();
			
			String conteudo = msgData.getMensagem();
			String title = titulo;
			
			String payload = APNS.newPayload()
					.alertTitle(title)
					.alertBody(conteudo)
					.badge(1)
					.sound("default")
					.customField("atributos", "{tipo_mensagem:" + tipoMensagem + ", id_mensagem: " + alert.id + "}")
					.build();
			
			for(UsuarioAlertaVO usrVO : lstUsrAlerta) {
				String token = usrVO.idMsgUsuario;
				try {
					service.push(token, payload);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}		
			
//			List<AlertaVO> alertas = new ArrayList<AlertaVO>();
//			alertas.add(alert);
//			setAlertasEnviados(alertas);
		} catch (RuntimeIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidSSLConfig e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NetworkIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	public void addUsuarioAlerta(final UsuarioAlertaVO usrAlerta) {
	
		if(StringUtils.isEmpty(usrAlerta.loginUsuario) || StringUtils.isEmpty(usrAlerta.idMsgUsuario) || StringUtils.isEmpty(usrAlerta.serialAparelho)) {
			throw new ParametroException("Ausência de parâmetros obrigatórios", IMensagensErro.PARAMETRO_NULO_CODE);
		}
		
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {

			@Override
			public void execute(Connection connection) throws SQLException {
				String qSel = "select * from usuario_alerta where login_usuario = ? and serial_aparelho = ?";
				String qUpdt = "update usuario_alerta set id_msg_usuario=?, dh_cadastro = ?, tipo_aparelho = ? where login_usuario = ? and serial_aparelho = ?";
				String qInst = "insert into usuario_alerta (login_usuario, id_msg_usuario, dh_cadastro, tipo_aparelho, serial_aparelho) values (?,?,?,?,?)";

				PreparedStatement pstmt = connection.prepareStatement(qSel);
				pstmt.setString(1, usrAlerta.loginUsuario);
				pstmt.setString(2, usrAlerta.serialAparelho);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {// o registro já existe: alterá-lo
					PreparedStatement pstmt1 = connection.prepareStatement(qUpdt);
					pstmt1.setString(1, usrAlerta.idMsgUsuario);
					pstmt1.setTimestamp(2, new Timestamp(usrAlerta.dhCadastro.getTime()));
					pstmt1.setString(3, usrAlerta.tipoAparelho);
					pstmt1.setString(4, usrAlerta.loginUsuario);
					pstmt1.setString(5, usrAlerta.serialAparelho);
					pstmt1.executeUpdate();
				} else {// registro não existe: inclui o registro
					PreparedStatement pstmt2 = connection.prepareStatement(qInst);
					pstmt2.setString(1, usrAlerta.loginUsuario);
					pstmt2.setString(2, usrAlerta.idMsgUsuario);
					pstmt2.setTimestamp(3, new Timestamp(usrAlerta.dhCadastro.getTime()));
					pstmt2.setString(4, usrAlerta.tipoAparelho);
					pstmt2.setString(5, usrAlerta.serialAparelho);
					pstmt2.executeUpdate();
				}
			}
		});
	}
	
	public List<UsuarioAlertaVO> getTodosUsuariosAlerta() {
		final List<UsuarioAlertaVO> usuarios = new ArrayList<UsuarioAlertaVO>();
		
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String qSelect = "select * from usuario_alerta";
				ResultSet rs = connection.createStatement().executeQuery(qSelect);
				while(rs.next()) {
					UsuarioAlertaVO vo = new UsuarioAlertaVO();
					vo.dhCadastro = rs.getDate("dh_cadastro");
					vo.idMsgUsuario = rs.getString("id_msg_usuario");
					vo.loginUsuario = rs.getString("login_usuario");
					vo.tipoAparelho = rs.getString("tipo_aparelho");
					vo.serialAparelho = rs.getString("serial_aparelho");
					usuarios.add(vo);
				}
			}
		});
		return usuarios;
	}
	

	public List<UsuarioAlertaVO> getUsuarioAlerta(final String login) {
		final List<UsuarioAlertaVO> usuarios = new ArrayList<UsuarioAlertaVO>();
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				try {
					String qUsusariosLogin = "select * from usuario_alerta where login_usuario = ?";
					PreparedStatement pstmt = connection.prepareStatement(qUsusariosLogin);
					pstmt.setString(1, login);
					ResultSet rs = pstmt.executeQuery();
					while(rs.next()) {
						UsuarioAlertaVO usr = new UsuarioAlertaVO();
						usr.loginUsuario = login;
						usr.dhCadastro = rs.getDate("dh_cadastro");
						usr.idMsgUsuario = rs.getString("id_msg_usuario");
						usr.tipoAparelho = rs.getString("tipo_aparelho");
						usr.serialAparelho = rs.getString("serial_aparelho");
						usuarios.add(usr);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		return usuarios;
	}
	
	public List<UsuarioAlertaVO> getUsuariosGrupo(final String nomeGrupo) {
		final List<UsuarioAlertaVO> usuarios = new ArrayList<UsuarioAlertaVO>();
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				try {
					String qUsuariosGrupo = "select ua.* from usuario_alerta ua where ua.login_usuario in ("
							+ "select u.login from usuario u where u.id in ("
							+ "select ug.usuarioid from usuario_grupo ug where ug.grupoid = ("
							+ "select g.id from grupo g where g.nome = ?"
							+ ")"
							+ ")"
							+ ")";
					PreparedStatement pstmt = connection.prepareStatement(qUsuariosGrupo);
					pstmt.setString(1, nomeGrupo);
					ResultSet rs = pstmt.executeQuery();
					while(rs.next()) {
						UsuarioAlertaVO usr = new UsuarioAlertaVO();
						
						usr.loginUsuario = rs.getString("login_usuario");
						usr.dhCadastro = rs.getDate("dh_cadastro");
						usr.idMsgUsuario = rs.getString("id_msg_usuario");
						usr.tipoAparelho = rs.getString("tipo_aparelho");
						usr.serialAparelho = rs.getString("serial_aparelho");
						usuarios.add(usr);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		return usuarios;
	}
	
	public List<UsuarioAlertaVO> getUsuariosDeployment(final Long deploymentId) {
		final List<UsuarioAlertaVO> usuarios = new ArrayList<UsuarioAlertaVO>();
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				try {
					String qUsuariosGrupo = "select ua.* from usuario_alerta ua where ua.login_usuario in ("
							+ "select u.login from usuario u where u.id in ("
							+ "select ug.usuarioid from usuario_grupo ug where ug.grupoid in ("
							+ "select dg.grupoid from deployment_grupo dg where dg.deploymentid = ?"
							+ ")"
							+ ")"
							+ ")";
					String qUsuariosDep = "select ua.* from usuario_alerta ua where ua.login_usuario in ("
							+ "select u.login from usuario u where u.id in ("
							+ "select du.usuarioid from deployment_usuario du where du.deploymentid = ?"
							+ ")"
							+ ")"; 
					
					PreparedStatement pstmt = connection.prepareStatement(qUsuariosGrupo);
					pstmt.setLong(1, deploymentId);
					ResultSet rs = pstmt.executeQuery();
					while(rs.next()) {
						UsuarioAlertaVO usr = new UsuarioAlertaVO();
						
						usr.loginUsuario = rs.getString("login_usuario");
						usr.dhCadastro = rs.getDate("dh_cadastro");
						usr.idMsgUsuario = rs.getString("id_msg_usuario");
						usr.tipoAparelho = rs.getString("tipo_aparelho");
						usr.serialAparelho = rs.getString("serial_aparelho");
						usuarios.add(usr);
					}
					pstmt.close();
					rs.close();
					pstmt = connection.prepareStatement(qUsuariosDep);
					pstmt.setLong(1, deploymentId);
					rs = pstmt.executeQuery();
					while(rs.next()) {
						UsuarioAlertaVO usr = new UsuarioAlertaVO();
						
						usr.loginUsuario = rs.getString("login_usuario");
						usr.dhCadastro = rs.getDate("dh_cadastro");
						usr.idMsgUsuario = rs.getString("id_msg_usuario");
						usr.tipoAparelho = rs.getString("tipo_aparelho");
						usr.serialAparelho = rs.getString("serial_aparelho");
						usuarios.add(usr);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		return usuarios;
	}
	
	public UsuarioAlertaVO getUsuarioEquipamento(final Long equipamentoId) {
		final UsuarioAlertaVO usuario = new UsuarioAlertaVO();
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				try {
					String qUsuariosGrupo = "select ua.* from usuario_alerta ua where ua.login_usuario = ("
							+ "select u.login from usuario u where u.id = ("
							+ "select e.usuario_id from equipamento e where e.id = ? "
							+ ")"
							+ ")";
					PreparedStatement pstmt = connection.prepareStatement(qUsuariosGrupo);
					pstmt.setLong(1, equipamentoId);
					ResultSet rs = pstmt.executeQuery();
					while(rs.next()) {
						UsuarioAlertaVO usr = new UsuarioAlertaVO();
						
						usuario.loginUsuario = rs.getString("login_usuario");
						usuario.dhCadastro = rs.getDate("dh_cadastro");
						usuario.idMsgUsuario = rs.getString("id_msg_usuario");
						usuario.tipoAparelho = rs.getString("tipo_aparelho");
						usuario.serialAparelho = rs.getString("serial_aparelho");						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		return usuario;
	}
	
	public void removeUsuariosAlertaAntigos() {
		
	}
}

