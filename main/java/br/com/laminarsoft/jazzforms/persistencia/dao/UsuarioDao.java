package br.com.laminarsoft.jazzforms.persistencia.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.EmailValidator;
import org.hibernate.Session;
import org.hibernate.cfg.search.HibernateSearchEventListenerRegister;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.laminarsoft.jazzforms.logging.types.InfoEnvioMailMSG;
import br.com.laminarsoft.jazzforms.negocio.controller.security.AuthenticationService;
import br.com.laminarsoft.jazzforms.negocio.controller.util.IProjetoController;
import br.com.laminarsoft.jazzforms.negocio.controller.util.LDAPServiceController;
import br.com.laminarsoft.jazzforms.negocio.controller.util.PropertiesServiceController;
import br.com.laminarsoft.jazzforms.persistencia.model.Grupo;
import br.com.laminarsoft.jazzforms.persistencia.model.Historico;
import br.com.laminarsoft.jazzforms.persistencia.model.Usuario;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.IMensagensErro;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.ParametroException;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.DeploymentVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.GrupoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LdapGrupoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LdapUsuarioVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LocalizacaoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.RequisicaoAtualizacoesVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.SugestaoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.TipoSugestaoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.UsuarioVO;
import br.com.laminarsoft.jazzforms.persistencia.util.LoginUtil;
import br.com.laminarsoft.jazzforms.persistencia.util.ValidacaoUtil;

import com.unboundid.ldap.sdk.LDAPSearchException;

@Transactional(propagation=Propagation.SUPPORTS, 
	isolation=Isolation.READ_COMMITTED, 
	timeout=30)
@Repository("UsuarioDao")
@SuppressWarnings("all")
public class UsuarioDao extends BaseDao<Usuario> {

	public static final long serialVersionUID = 10l;

	@Autowired private LDAPServiceController ldapServiceController;
	@Autowired private PropertiesServiceController propertiesServiceController;
	@Autowired private AuthenticationService authenticationService;
	@Autowired private LoginUtil loginUtil;
	@Resource(mappedName="java:/queue/mailQ") private Queue reportQueue;	
	@Resource(mappedName="java:/ConnectionFactory") private ConnectionFactory connectionFactory;

	public class WorkRemoveUsuario implements Work {
		String uid;

		@Override
        public void execute(Connection connection) throws SQLException {
			PreparedStatement pstmt = connection.prepareStatement("select id from usuario where upper(uid) = ?");
			pstmt.setString(1, uid.toUpperCase());
			ResultSet rsUsr = pstmt.executeQuery();
			if(rsUsr.next()) {
				Long userId = rsUsr.getLong(1);
				connection.createStatement().executeUpdate("delete from deployment_usuario where usuarioid = " + userId);
				connection.createStatement().executeUpdate("delete from usuario_grupo where usuarioid = " + userId);
				connection.createStatement().executeUpdate("update usuario set ativo = false where id = " + userId);
			}
			rsUsr.close();
			pstmt.close();
        }
	}
	
	public class WorkRemoveUsuarioGrupo implements Work {
		String uid;
		String nomeGrupo;
		
		@Override
        public void execute(Connection connection) throws SQLException {
			PreparedStatement pstmtU = connection.prepareStatement("select id from usuario where upper(login) = ?");
			pstmtU.setString(1, uid.toUpperCase());
			ResultSet rsUsuario = pstmtU.executeQuery();
			
			PreparedStatement pstmtG = connection.prepareStatement("select id from grupo where nome = ?");
			pstmtG.setString(1, nomeGrupo);
			ResultSet rsGrupo = pstmtG.executeQuery();
			if(rsUsuario.next() && rsGrupo.next()) {
				Long userId = rsUsuario.getLong(1);
				Long grupoId = rsGrupo.getLong(1);
				connection.createStatement().executeUpdate("delete from usuario_grupo where usuarioid = " + userId + " and grupoid = " + grupoId);
			}
			rsUsuario.close();
			rsGrupo.close();
        }
	}
	
	public class WorkAdicionaUsuarioGrupo implements Work {

		LdapUsuarioVO usuariovo;
		LdapGrupoVO grupovo;
		
		@Override
        public void execute(Connection connection) throws SQLException {
			Long grupoId = null;
			Long usuarioId = null;
			PreparedStatement pstmtU = connection.prepareStatement("select id from usuario where upper(uid) = ?");
			pstmtU.setString(1, usuariovo.getUid().toUpperCase());
			ResultSet rsUsuario = pstmtU.executeQuery();
			
			PreparedStatement pstmtG = connection.prepareStatement("select id from grupo where nome = ?");
			pstmtG.setString(1, grupovo.getNome());
			ResultSet rsGrupo = pstmtG.executeQuery();
			if (!rsGrupo.next()) {
				PreparedStatement pstmt = connection.prepareStatement("insert into grupo (nome, descricao, dn) values (?,?,?)", Statement.RETURN_GENERATED_KEYS);
				pstmt.setString(1, grupovo.getNome());
				pstmt.setString(2, grupovo.getDescription());
				pstmt.setString(3, grupovo.getDn());
				pstmt.executeUpdate();
				ResultSet rsKey = pstmt.getGeneratedKeys();
				if(rsKey.next()) {
					grupoId = rsKey.getLong(1);
				}
			} else {
				grupoId = rsGrupo.getLong(1);
			}
			if (!rsUsuario.next()) {
				PreparedStatement pstmt = connection.prepareStatement("insert into usuario (nome, login, ativo, dn, uid) values (?, ?, 1, ?, ?)", Statement.RETURN_GENERATED_KEYS);
				pstmt.setString(1, usuariovo.getNome());
				pstmt.setString(2, usuariovo.getLogin().toUpperCase());
				pstmt.setString(2, usuariovo.getDn());
				pstmt.setString(4, usuariovo.getUid());
				pstmt.executeUpdate();
				ResultSet rsKey = pstmt.getGeneratedKeys();
				if(rsKey.next()) {
					usuarioId = rsKey.getLong(1);
				}				
			} else {
				usuarioId = rsUsuario.getLong(1);
			}

			ResultSet rsRel = connection.createStatement().executeQuery("select * from usuario_grupo where usuarioid = " + usuarioId + " and grupoid = " + grupoId);
			if(!rsRel.next()) {
				connection.createStatement().executeUpdate("insert into usuario_grupo (usuarioid, grupoid) values (" + usuarioId + ", " + grupoId +")");
			}
			rsRel.close();
			rsUsuario.close();
			rsGrupo.close();
			boolean usrEncontrado = false;
			for(LdapUsuarioVO usr : grupovo.getUsuarios()) {
				if(usr.getUid().equalsIgnoreCase(usuariovo.getUid())) {
					usrEncontrado = true;
					break;
				}
			}
			if(!usrEncontrado) {
				grupovo.getUsuarios().add(usuariovo);
			}
        }
	}
	
	public class WorkRemoveGrupo implements Work {
		Grupo grupo = null;
		
		@Override
        public void execute(Connection connection) throws SQLException {
			String slctGrupo = "select id from grupo where nome = '" + grupo.getNome() + "'"; 
			ResultSet rs = connection.createStatement().executeQuery(slctGrupo);
			
			if (rs.next()) {
				Long grupoId = rs.getLong(1);
				String rmv = "delete from usuario_grupo where grupoid = " + grupoId;
				connection.createStatement().executeUpdate(rmv);
				
				rmv = "delete from deployment_grupo where grupoid = " + grupoId;
				connection.createStatement().executeUpdate(rmv);
				
				rmv = "delete from grupo where id = " + grupoId;
				connection.createStatement().executeUpdate(rmv);
			}
        }
	}
	
	public class WorkCheckGrupoDeployment implements Work {
		String nomeGrupo = "";
		Boolean existe = false;

		@Override
        public void execute(Connection connection) throws SQLException {
			String slct = "select * from deployment_grupo where grupoid = (select id from grupo where nome = '"+ nomeGrupo + "')";
			ResultSet rs = connection.createStatement().executeQuery(slct);
			if (rs.next()) {
				existe = true;
			}
			rs.close();
        }
		
	}
	
	public class WorkMantemGruposUsuario implements Work {

		Usuario usuario;
		
		@Override
        public void execute(Connection connection) throws SQLException {
			String qExistenciaUsr = "select * from usuario where upper(login) = ?";
			String qExistenciaGrupo = "select * from grupo where nome = ?";
			String qExistenciaRelUsrGrupo = "selet * from usuario_grupo where usuarioid = ? and grupoid = ?";
			
			//String uUsuario = "update usuario set nome = ? where upper(login) = ?";
			String uGrupo = "update grupo set descricao = ? where nome = ?";
			
			String dRelUsrGrp = "delete from usuario_grupo where usuarioid = (select id from usuario where upper(login) = ?)";
			
			String iRelUsrGrp = "insert into usuario_grupo (usuarioid, grupoid, idx) values (?, ?, ?)";
			String iGrupo = "insert into grupo (nome, descricao) values (?, ?)";
			String iUsuario = "insert into usuario (nome, login) values (?,?)";
			
			try {
	            PreparedStatement pstmtQUsr = connection.prepareStatement(qExistenciaUsr);
	            PreparedStatement pstmtDRelUsrGrp = connection.prepareStatement(dRelUsrGrp);
	            PreparedStatement pstmtQGrp = connection.prepareStatement(qExistenciaGrupo);
	            PreparedStatement pstmtUGrp = connection.prepareStatement(uGrupo);
	            PreparedStatement pstmtIRelUsrGrp = connection.prepareStatement(iRelUsrGrp);
	            PreparedStatement pstmtIGrp = connection.prepareStatement(iGrupo, Statement.RETURN_GENERATED_KEYS);
	            PreparedStatement pstmtIUsr = connection.prepareStatement(iUsuario, Statement.RETURN_GENERATED_KEYS);
	            pstmtQUsr.setString(1, usuario.getLogin().toUpperCase());
	            ResultSet rs = pstmtQUsr.executeQuery();
	            if(rs.next()) {// o usuário está cadastrado localmente
	            	Long idUsr = rs.getLong("id");

	            	manutencaoGrupos(pstmtDRelUsrGrp, pstmtQGrp, pstmtUGrp, pstmtIRelUsrGrp, pstmtIGrp, idUsr);	            	
	            } else {// o usuário não está cadastrado localmente
	            	Long idUsr = null;
	            	pstmtIUsr.setString(1, usuario.getNome());
	            	pstmtIUsr.setString(2, usuario.getLogin());
	            	pstmtIUsr.executeUpdate();
	            	
	            	ResultSet rsUsrKey = pstmtIUsr.getGeneratedKeys();
	            	if (rsUsrKey.next()) {
	            		idUsr = rsUsrKey.getLong(1);
	            	}
	            	manutencaoGrupos(pstmtDRelUsrGrp, pstmtQGrp, pstmtUGrp, pstmtIRelUsrGrp, pstmtIGrp, idUsr);
	            }
	            pstmtQUsr.close();
	            pstmtDRelUsrGrp.close();
	            pstmtQGrp.close();
	            pstmtUGrp.close();
	            pstmtIRelUsrGrp.close();
	            pstmtIGrp.close();
	            pstmtIUsr.close();
            } catch (Exception e) {
	            e.printStackTrace();
            }
			
        }

		private void manutencaoGrupos(PreparedStatement pstmtDRelUsrGrp, PreparedStatement pstmtQGrp, PreparedStatement pstmtUGrp,
                PreparedStatement pstmtIRelUsrGrp, PreparedStatement pstmtIGrp, Long idUsr) throws SQLException {
	        pstmtDRelUsrGrp.clearParameters();
	        pstmtDRelUsrGrp.setString(1, usuario.getLogin().toUpperCase());
	        pstmtDRelUsrGrp.executeUpdate(); // remove todos os relacionamentos com grupos
	        
	        for (int idxGrp = 0; idxGrp < usuario.getGrupos().size(); idxGrp++) {
	        	Long idGrp = null;
	        	pstmtQGrp.clearParameters();
	        	pstmtIRelUsrGrp.clearParameters();
	        	pstmtIGrp.clearParameters();
	        	pstmtUGrp.clearParameters();
	        	Grupo grp = usuario.getGrupos().get(idxGrp);
	        	pstmtQGrp.setString(1, grp.getNome());
	        	ResultSet rsGrp = pstmtQGrp.executeQuery();
	        	if (rsGrp.next()) {// o grupo já existe
	        		pstmtUGrp.setString(1, grp.getDescricao());
	        		pstmtUGrp.setString(2, grp.getNome());
	        		pstmtUGrp.executeUpdate();
	        		idGrp = rsGrp.getLong("id");
	        	} else { // o grupo ainda não existe
	        		pstmtIGrp.setString(1, grp.getNome());
	        		pstmtIGrp.setString(2, grp.getDescricao());
	        		pstmtIGrp.executeUpdate();
	        		
	        		ResultSet rsGrpKey = pstmtIGrp.getGeneratedKeys();
	        		if(rsGrpKey.next()) {
	        			idGrp = rsGrpKey.getLong(1);
	        		}
	        	}
	        	pstmtIRelUsrGrp.setLong(1, idUsr);
	        	pstmtIRelUsrGrp.setLong(2, idGrp);
	        	pstmtIRelUsrGrp.setNull(3, Types.INTEGER);
	        	pstmtIRelUsrGrp.executeUpdate();
	        }
        }		
	}
	
	public List<Usuario> findByLogin(String login) {
		loginUtil.validaLogin(login);
		List<Usuario> cmps = hibernateTemplate.find("from Usuario u where upper(u.login) = ?", login.toUpperCase());
		if(cmps == null || cmps.size() == 0) {
			try {
				LdapUsuarioVO usuarioVo = ldapServiceController.getUsuario(login);
				if(usuarioVo != null) {
					usuarioVo.setGrupos(ldapServiceController.getGruposUsuario(login));
					adicionarUsuario(usuarioVo);
				}
				cmps = hibernateTemplate.find("from Usuario u where upper(u.login) = ?", login.toUpperCase());
			} catch (LDAPSearchException e) {
				e.printStackTrace();
			}
		}
		return cmps;
	}	
	
	public List<LocalizacaoVO> getLocalizacoesUsuario(final String usuario) {
		loginUtil.validaLogin(usuario);
		final List<LocalizacaoVO> localizacoes = new ArrayList<LocalizacaoVO>();
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String qLoc = "select * from localizacao where equipamento_id in (select id from equipamento where usuario_id = (select id from usuario where upper(login) = ?)) order by data";
				PreparedStatement pstmt = connection.prepareStatement(qLoc);
				pstmt.setString(1, usuario.toUpperCase());
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()) {
					LocalizacaoVO loc = new LocalizacaoVO();
					loc.data = rs.getDate("data");
					Double latitude = rs.getDouble("latitude");
					Double longitude = rs.getDouble("longitude");
					loc.setLatitude(latitude == null ? null : String.valueOf(latitude));
					loc.setLongitude(longitude == null ? null : String.valueOf(longitude));
					localizacoes.add(loc);
				}
			}
		});
		
		return localizacoes;
	}
	
	public List<Grupo> findGroupByName(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
		}
		List<Grupo> grps = hibernateTemplate.find("from Grupo g where g.nome = ?", name);
		if(grps == null || grps.size() == 0) {
			try {
				LdapGrupoVO grupoVo = ldapServiceController.getGrupo(name);
				if(grupoVo != null) {
					adicionaGrupo(grupoVo);
				}
				grps = hibernateTemplate.find("from Grupo g where g.nome = ?", name);
			} catch (LDAPSearchException e) {
				e.printStackTrace();
			}
		}
		return grps;
	}
	
	public void mantemGruposUsuario(Usuario usr) {
		if (usr == null) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
		}
		
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		WorkMantemGruposUsuario work = new WorkMantemGruposUsuario();
		work.usuario = usr;
		session.doWork(work);		
	}
	
	public Grupo adicionaGrupo(LdapGrupoVO grupoVo) {
		List encontrados = hibernateTemplate.find("from Grupo where nome = ?", grupoVo.getNome());
		if (encontrados.size() > 0) {
			return (Grupo)encontrados.get(0);
		}
		Grupo grp = new Grupo();
		grp.setNome(grupoVo.getNome());
		grp.setDescricao(grupoVo.getDescription());
		hibernateTemplate.persist(grp);
		return grp;
	}
	
	public void atualizaRelacionamentoUsuario(final LdapUsuarioVO usuario) {
		if (usuario != null) {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			session.doWork(new Work() {
				
				@Override
				public void execute(Connection connection) throws SQLException {
					String qUsuario = "select * from usuario where upper(login) = ?";
					String qGrupo = "select * from grupo where nome = ?";
					
					String rUsuarioGrupo = "delete from usuario_grupo where usuarioid = (select id from usuario where upper(login) = ?)";
					String iUsuarioGrupo = "insert into usuario_grupo (usuarioid, grupoid, idx) values ((select id from usuario where upper(login) = ?), (select id from grupo where nome = ?), 1)";
					String qUsuarioGrupo = "select * from usuario_grupo where usuarioid = (select u.id from usuario u where upper(u.login) = ?) and grupoid = (select g.id from grupo g where g.nome = ?)";
					String uGrupo = "update grupo set descricao = ?, Dn = ? where nome = ?";
					String uUsuario = "update usuario set nome = ?, ativo = ?, dn = ?, uid = ? where upper(login) = ?";
					String iGrupo = "insert into grupo (nome, descricao, dn) values (?,?,?)";
					String iUsuario = "insert into usuario (nome, login, ativo, dn, uid) values (?,?,?,?,?)";
					
					PreparedStatement pstmt = connection.prepareStatement(rUsuarioGrupo);
					pstmt.setString(1, usuario.getLogin().toUpperCase());
					pstmt.executeUpdate();
					pstmt.close();
					
					pstmt = connection.prepareStatement(qUsuario);
					pstmt.setString(1, usuario.getLogin().toUpperCase());
					ResultSet rs = pstmt.executeQuery();
					if(rs.next()) {
						PreparedStatement pstmt2 = connection.prepareStatement(uUsuario);
						pstmt2.setString(1, usuario.getNome());
						pstmt2.setBoolean(2, usuario.isAtivo());
						pstmt2.setString(3, usuario.getDn());
						pstmt2.setString(4, usuario.getUid());
						pstmt2.setString(5, usuario.getLogin().toUpperCase());
						pstmt2.executeUpdate();
						pstmt2.close();
					} else {
						PreparedStatement pstmt2 = connection.prepareStatement(iUsuario);
						pstmt2.setString(1, usuario.getNome());
						pstmt2.setString(2, usuario.getLogin().toUpperCase());
						pstmt2.setBoolean(3, usuario.isAtivo());
						pstmt2.setString(4, usuario.getDn());
						pstmt2.setString(5, usuario.getUid());
						pstmt2.executeUpdate();
						pstmt2.close();
					}
					
					rs.close();
					pstmt.close();
					
					pstmt = connection.prepareStatement(qGrupo);
					PreparedStatement pstmtUG = connection.prepareStatement(iUsuarioGrupo);
					PreparedStatement pstmtQUG = connection.prepareStatement(qUsuarioGrupo);
					for(LdapGrupoVO grupo : usuario.getGrupos()) {
						pstmt.clearParameters();
						pstmt.setString(1, grupo.getNome());
						rs = pstmt.executeQuery();
						if(rs.next()) {
							PreparedStatement pstmt2 = connection.prepareStatement(uGrupo);
							pstmt2.setString(1, grupo.getDescription());
							pstmt2.setString(2, grupo.getDn());
							pstmt2.setString(3, grupo.getNome());
							pstmt2.executeUpdate();
							pstmt2.close();
						} else {
							PreparedStatement pstmt2 = connection.prepareStatement(iGrupo);
							pstmt2.setString(1, grupo.getNome());
							pstmt2.setString(2, grupo.getDescription());
							pstmt2.setString(3, grupo.getDn());
							pstmt2.executeUpdate();
							pstmt2.close();
						}						
						rs.close();
						pstmtQUG.clearParameters();
						pstmtQUG.setString(1, usuario.getLogin().toUpperCase());
						pstmtQUG.setString(2, grupo.getNome());
						ResultSet rsQUG = pstmtQUG.executeQuery();
						
						if (!rsQUG.next()) {
							pstmtUG.clearParameters();
							pstmtUG.setString(1, usuario.getLogin().toUpperCase());
							pstmtUG.setString(2, grupo.getNome());
							pstmtUG.executeUpdate();
						}
						rsQUG.close();
					}
					pstmt.close();
					pstmtQUG.close();
					pstmtUG.close();
				}
			});
		}
	}
	
	public Usuario getUsuarioPorLogin(String login) {
		List usuarios = hibernateTemplate.find("from Usuario where upper(login) = ?", login.toUpperCase());
		Usuario ret = null;
		if(usuarios != null && usuarios.size() > 0) {
			ret = (Usuario) usuarios.get(0);
		}
		return ret;
	}
	
	public LdapUsuarioVO getInfoUsuarioPorLogin(final String login) {
		if(StringUtils.isEmpty(login)) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
		}
		
		final LdapUsuarioVO usr = new LdapUsuarioVO();
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String qUsr = "select * from usuario where upper(login) = ?";
				PreparedStatement pstmt = connection.prepareStatement(qUsr);
				pstmt.setString(1, login.toUpperCase());
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()) {
					usr.setNome(rs.getString("nome"));
					usr.setAdvogado(rs.getBoolean("advogado"));
					Date dt = rs.getDate("data_nascimento");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
					usr.setDataNascimento(sdf.format(dt));
					usr.setTelefone(rs.getString("telefone"));
					usr.setCpf(rs.getString("cpf"));
					usr.setMail(rs.getString("email"));
					usr.setAtivo(rs.getBoolean("ativo"));		
					usr.setLogin(rs.getString("login"));
					usr.setUid(rs.getString("uid"));
				}
				rs.close();
				pstmt.close();
			}
		});
		
		return usr;
	}	
	
	public LdapUsuarioVO getInfoUsuario(final String login) {
		loginUtil.validaLogin(login);
		
		final LdapUsuarioVO usr = new LdapUsuarioVO();
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String qUsr = "select * from usuario where upper(login) = ?";
				PreparedStatement pstmt = connection.prepareStatement(qUsr);
				pstmt.setString(1, login.toUpperCase());
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()) {
					usr.setNome(rs.getString("nome"));
					usr.setLogin(rs.getString("login"));
					usr.setMatricula(rs.getString("matricula"));
					usr.setUid(rs.getString("uid"));
					usr.setAdvogado(rs.getBoolean("advogado"));
					Date dt = rs.getDate("data_nascimento");
					if (dt != null) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
						usr.setDataNascimento(sdf.format(dt));
					}
					usr.setTelefone(rs.getString("telefone"));
					usr.setCpf(rs.getString("cpf"));
					usr.setMail(rs.getString("email"));
					usr.setAtivo(rs.getBoolean("ativo"));	
					usr.setUsuarioExterno(rs.getBoolean("usuario_externo"));					
				} 
				rs.close();
				pstmt.close();
			}
		});
		
		return usr;
	}
	
	public Usuario getInfoUsuarioComGrupos(final String login) {
		loginUtil.validaLogin(login);
		
		final Usuario usr = new Usuario();
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String qUsr = "select * from usuario where upper(login) = ?";
				String qGrupos = "select g.* from grupo g "
						+ "inner join usuario_grupo ug on ug.grupoid = g.id "
						+ "where ug.usuarioid = ?";
				PreparedStatement pstmt = connection.prepareStatement(qUsr);
				PreparedStatement pstmtGrupos = connection.prepareStatement(qGrupos);
				pstmt.setString(1, login.toUpperCase());
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()) {
					usr.setNome(rs.getString("nome"));
					usr.setLogin(login);
					usr.setMatricula(rs.getString("matricula"));
					usr.setUid(rs.getString("uid"));
					usr.setAdvogado(rs.getBoolean("advogado"));					
					usr.setTelefone(rs.getString("telefone"));
					usr.setCpf(rs.getString("cpf"));
					usr.setEmail(rs.getString("email"));
					usr.setAtivo(rs.getBoolean("ativo"));	
					usr.setUsuarioExterno(rs.getBoolean("usuario_externo"));
					usr.setId(rs.getLong("id"));
					
					pstmtGrupos.clearParameters();
					pstmtGrupos.setLong(1, rs.getLong("id"));
					ResultSet rsGrupos = pstmtGrupos.executeQuery();
					while(rsGrupos.next()) {
						Grupo grp = new Grupo();
						grp.setId(rsGrupos.getLong("id"));
						grp.setNome(rsGrupos.getString("nome"));
						grp.setDescricao(rsGrupos.getString("descricao"));
						usr.getGrupos().add(grp);
					}
				}
				rs.close();
				pstmt.close();
			}
		});
		
		return usr;
	}
	

	public Usuario getInfoUsuarioComGruposPorUID(final String UID) {
		loginUtil.validaLogin(UID);
		
		final Usuario usr = new Usuario();
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String qUsr = "select * from usuario where upper(uid) = ?";
				String qGrupos = "select g.* from grupo g "
						+ "inner join usuario_grupo ug on ug.grupoid = g.id "
						+ "where ug.usuarioid = ?";
				PreparedStatement pstmt = connection.prepareStatement(qUsr);
				PreparedStatement pstmtGrupos = connection.prepareStatement(qGrupos);
				pstmt.setString(1, UID.toUpperCase());
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()) {
					usr.setNome(rs.getString("nome"));
					usr.setLogin(rs.getString("login"));
					usr.setMatricula(rs.getString("matricula"));
					usr.setUid(rs.getString("uid"));
					usr.setAdvogado(rs.getBoolean("advogado"));					
					usr.setTelefone(rs.getString("telefone"));
					usr.setCpf(rs.getString("cpf"));
					usr.setEmail(rs.getString("email"));
					usr.setAtivo(rs.getBoolean("ativo"));	
					usr.setUsuarioExterno(rs.getBoolean("usuario_externo"));
					usr.setId(rs.getLong("id"));
					
					pstmtGrupos.clearParameters();
					pstmtGrupos.setLong(1, rs.getLong("id"));
					ResultSet rsGrupos = pstmtGrupos.executeQuery();
					while(rsGrupos.next()) {
						Grupo grp = new Grupo();
						grp.setId(rsGrupos.getLong("id"));
						grp.setNome(rsGrupos.getString("nome"));
						grp.setDescricao(rsGrupos.getString("descricao"));
						usr.getGrupos().add(grp);
					}
				}
				rs.close();
				pstmt.close();
			}
		});
		
		return usr;
	}
	
	public List<LdapUsuarioVO> getTodosUsuarios() {
		final List<LdapUsuarioVO> usuarios = new ArrayList<LdapUsuarioVO>();
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String qUsr = "select * from usuario";
				ResultSet rs = connection.createStatement().executeQuery(qUsr);
				if(rs.next()) {
					LdapUsuarioVO usr = new LdapUsuarioVO();
					usr.setNome(rs.getString("nome"));
					usr.setLogin(rs.getString("login"));
					usr.setCn(rs.getString("nome"));
					usr.setMatricula(rs.getString("matricula"));
					usr.setUid(rs.getString("uid"));
					usr.setAdvogado(rs.getBoolean("advogado"));
					Date dt = rs.getDate("data_nascimento");
					if (dt != null) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
						usr.setDataNascimento(sdf.format(dt));
					}
					usr.setTelefone(rs.getString("telefone"));
					usr.setCpf(rs.getString("cpf"));
					usr.setMail(rs.getString("email"));
					usr.setUid(rs.getString("uid"));
					usr.setAtivo(rs.getBoolean("ativo"));	
					usr.setUsuarioExterno(rs.getBoolean("usuario_externo"));
					
				}
				rs.close();				
			}
		});
		return usuarios;
	}
	
	public synchronized Usuario adicionarUsuario(final LdapUsuarioVO usuarioVo) {
		loginUtil.validaLogin(usuarioVo.getLogin());
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String cUsuario = "select * from usuario where upper(login) = ?";
				String rUsuGrp = "delete from usuario_grupo where usuarioid = ?";
				String uUsuario = "update usuario set nome = ?, matricula = ?, ativo = ?, uid = ?, cpf = ?, telefone = ?, data_nascimento = ?, email = ?, usuario_externo = ?, advogado = ?, senha = ? where upper(login) = ?";
				String iUsuario = "insert into usuario (nome, login, matricula, ativo, uid, cpf, telefone, data_nascimento, email, usuario_externo, advogado, senha) values (?,?,?,?,?,?,?,?,?,?,?,?)";
				String iUsuGrp = "insert into usuario_grupo (usuarioid, grupoid) values (?,?)";
				String iUsuGrpPNome = "insert into usuario_grupo (usuarioid, grupoid) values (?, (select g.id from grupo g where g.nome = ?))";
				String qGrupo = "select * from grupo where nome = ?";
				String iGrupo = "insert into grupo (nome, descricao, dn) values (?,?,?)";
				
				PreparedStatement pstmtConsultaUsuario = connection.prepareStatement(cUsuario);
				pstmtConsultaUsuario.setString(1, usuarioVo.getLogin().toUpperCase());
				ResultSet rsConsultaUsuario = pstmtConsultaUsuario.executeQuery();
				if(rsConsultaUsuario.next()) {//usuário já existe: altera o usuário e adiciona o mesmo aos grupos
					PreparedStatement pstmtUUsuario = connection.prepareStatement(uUsuario);
					int idx = 1;
					pstmtUUsuario.setString(idx++, usuarioVo.getNome().toUpperCase());
					pstmtUUsuario.setString(idx++, usuarioVo.getMatricula());
					pstmtUUsuario.setBoolean(idx++, usuarioVo.isAtivo());
					pstmtUUsuario.setString(idx++, usuarioVo.getUid());
					pstmtUUsuario.setString(idx++, usuarioVo.getCpf());
					pstmtUUsuario.setString(idx++, usuarioVo.getTelefone());
					if (StringUtils.isNotEmpty(usuarioVo.getDataNascimento())) {
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						try {
							pstmtUUsuario.setDate(idx++, new java.sql.Date(sdf.parse(usuarioVo.getDataNascimento()).getTime()));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					} else {
						pstmtUUsuario.setNull(idx++, java.sql.Types.DATE);
					}
					if(usuarioVo.getLogin().equalsIgnoreCase("T318203")) {
						System.out.print("Alison");
					}
					pstmtUUsuario.setString(idx++, usuarioVo.getMail());
					pstmtUUsuario.setBoolean(idx++, usuarioVo.isUsuarioExterno());
					pstmtUUsuario.setBoolean(idx++, usuarioVo.isAdvogado());
					if(StringUtils.isNotEmpty(usuarioVo.getSenha())) {
						pstmtUUsuario.setString(idx++, authenticationService.encrypt(usuarioVo.getSenha()));
					} else {
						pstmtUUsuario.setNull(idx++, java.sql.Types.VARCHAR);
					}
					pstmtUUsuario.setString(idx++, usuarioVo.getLogin().toUpperCase());

					pstmtUUsuario.executeUpdate();
					pstmtUUsuario.close();
					
					Long usuarioId = rsConsultaUsuario.getLong("id");

					PreparedStatement pstmtRU = connection.prepareStatement(rUsuGrp);
					pstmtRU.setLong(1, usuarioId);
					pstmtRU.executeUpdate();
					pstmtRU.close();
					
					PreparedStatement pstmtIUG = connection.prepareStatement(iUsuGrpPNome);
					for(LdapGrupoVO grp : usuarioVo.getGrupos()) {
						pstmtIUG.clearParameters();
						pstmtIUG.setLong(1, usuarioId);
						pstmtIUG.setString(2, grp.getNome());
						pstmtIUG.executeUpdate();
					}
					pstmtIUG.close();
				} else {// usuário não existe, cria o usuário e adiciona o mesmo aos grupos
					PreparedStatement pstmtIUsuario = connection.prepareStatement(iUsuario, Statement.RETURN_GENERATED_KEYS);
					int idx = 1;
					pstmtIUsuario.setString(idx++, usuarioVo.getNome().toUpperCase());
					pstmtIUsuario.setString(idx++, usuarioVo.getLogin().toUpperCase());
					pstmtIUsuario.setString(idx++, usuarioVo.getMatricula());
					pstmtIUsuario.setBoolean(idx++, usuarioVo.isAtivo());
					pstmtIUsuario.setString(idx++, usuarioVo.getUid());
					pstmtIUsuario.setString(idx++, usuarioVo.getCpf());
					pstmtIUsuario.setString(idx++, usuarioVo.getTelefone());
					if (StringUtils.isNotEmpty(usuarioVo.getDataNascimento())) {
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						try {
							pstmtIUsuario.setDate(idx++, new java.sql.Date(sdf.parse(usuarioVo.getDataNascimento()).getTime()));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					} else {
						pstmtIUsuario.setNull(idx++, java.sql.Types.DATE);
					}
					pstmtIUsuario.setString(idx++, usuarioVo.getMail());
					pstmtIUsuario.setBoolean(idx++, usuarioVo.isUsuarioExterno());
					pstmtIUsuario.setBoolean(idx++, usuarioVo.isAdvogado());
					if(StringUtils.isNotEmpty(usuarioVo.getSenha())) {
						pstmtIUsuario.setString(idx++, authenticationService.encrypt(usuarioVo.getSenha()));
					} else {
						pstmtIUsuario.setNull(idx++, java.sql.Types.VARCHAR);
					}
					pstmtIUsuario.executeUpdate();
					ResultSet rsChaveUsuario = pstmtIUsuario.getGeneratedKeys();
					Long idUsuario = 0l;
					if(rsChaveUsuario.next()) {
						idUsuario = rsChaveUsuario.getLong(1);
					}
					
					PreparedStatement pstmtQGrupo = connection.prepareStatement(qGrupo);
					PreparedStatement pstmtIUsuGrp = connection.prepareStatement(iUsuGrp);
					for(LdapGrupoVO grupo : usuarioVo.getGrupos()) {
						pstmtQGrupo.clearParameters();
						pstmtIUsuGrp.clearParameters();
						pstmtQGrupo.setString(1, grupo.getNome());
						ResultSet rsGrp = pstmtQGrupo.executeQuery();
						if(rsGrp.next()) {
							Long idGrupo = rsGrp.getLong("id");
							pstmtIUsuGrp.setLong(1, idUsuario);
							pstmtIUsuGrp.setLong(2, idGrupo);
							pstmtIUsuGrp.executeUpdate();
						} else {
							PreparedStatement pstmtIGrupo = connection.prepareStatement(iGrupo, Statement.RETURN_GENERATED_KEYS);
							pstmtIGrupo.setString(1, grupo.getNome());
							pstmtIGrupo.setString(2, grupo.getDescription());
							pstmtIGrupo.setString(3, grupo.getDn());
							pstmtIGrupo.executeUpdate();
							ResultSet rsIGrupo = pstmtIGrupo.getGeneratedKeys();
							Long idGrupo = 0l;
							if(rsIGrupo.next()) {
								idGrupo = rsIGrupo.getLong("id");
								pstmtIUsuGrp.setLong(1, idUsuario);
								pstmtIUsuGrp.setLong(2, idGrupo);
								pstmtIUsuGrp.executeUpdate();
							}
							pstmtIGrupo.close();
							rsIGrupo.close();
						}
					}
					pstmtQGrupo.close();
					pstmtIUsuGrp.close();
				}
			}
		});
		
		Usuario usuario = new Usuario();
		usuario.setNome(usuarioVo.getNome());
		usuario.setUid(usuarioVo.getUid());
		usuario.setLogin(usuarioVo.getLogin());
//		List encontrados = hibernateTemplate.find("from Usuario where upper(login) = ?", usuarioVo.getLogin().toUpperCase());
//		if(encontrados.size() > 0) {
//			usuario = (Usuario)encontrados.get(0);
//		}
		return usuario;
	}
	
	public LdapUsuarioVO alteraUsuarioPublico(final LdapUsuarioVO usuarioVo) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();

		final String basePessoalDn = propertiesServiceController.getProperty(IProjetoController.LDAP_PESSOAS) + "," +
				propertiesServiceController.getProperty(IProjetoController.LDAP_ROOT_DN);
		final String usuarioUid = "uid=" + usuarioVo.getMail() + "," + basePessoalDn;
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Date nascimento = null;
				try {
					if (StringUtils.isNotEmpty(usuarioVo.getDataNascimento())) {
						nascimento = sdf.parse(usuarioVo.getDataNascimento());
					}
				} catch (ParseException e) {
				}				
				String updtUsuario = "update usuario set "
						+ "nome = ?, "
						+ "login = ?, "
						+ "ativo = ?, "
						+ "dn = ?, "
						+ "uid = ?, "
						+ "telefone = ?, "
						+ "data_nascimento = ?, "
						+ "email = ?, "
						+ "usuario_externo = 1, "
						+ "senha = ?, "
						+ "advogado = ?, "
						+ "data_cadastro = ? "
						+ "where upper(login) = ?";
				PreparedStatement pstmt = connection.prepareStatement(updtUsuario);
				int idx = 1;
				if (StringUtils.isNotEmpty(usuarioVo.getNome())) {
					pstmt.setString(idx++, usuarioVo.getNome());
				} else {
					pstmt.setNull(idx++, Types.VARCHAR);
				}
				if (StringUtils.isNotEmpty(usuarioVo.getMail())) {
					pstmt.setString(idx++, usuarioVo.getMail().toUpperCase());
				} else {
					pstmt.setString(idx++, usuarioVo.getLogin());
				} 
				if (StringUtils.isNotEmpty(usuarioVo.getMail())) {
					pstmt.setBoolean(idx++, usuarioVo.getMail().equalsIgnoreCase(usuarioVo.getLogin()));
				} else {
					pstmt.setBoolean(idx++, usuarioVo.isAtivo());
				}
				pstmt.setString(idx++, usuarioUid);
				if (StringUtils.isNotEmpty(usuarioVo.getMail())) {
					pstmt.setString(idx++, usuarioVo.getMail());
				} else {
					pstmt.setNull(idx++, Types.VARCHAR);
				}
				if (StringUtils.isNotEmpty(usuarioVo.getTelefone())) {
					pstmt.setString(idx++, usuarioVo.getTelefone());
				} else {
					pstmt.setNull(idx++, Types.VARCHAR);
				}
				if (nascimento != null) {
					pstmt.setDate(idx++, new java.sql.Date(nascimento.getTime()));
				} else {
					pstmt.setNull(idx++, Types.DATE);
				}
				if (StringUtils.isNotEmpty(usuarioVo.getMail())) {
					pstmt.setString(idx++, usuarioVo.getMail());
				} else {
					pstmt.setString(idx++, usuarioVo.getLogin());
				}
				pstmt.setString(idx++, authenticationService.encrypt(usuarioVo.getPasswd()));
				pstmt.setBoolean(idx++, usuarioVo.isAdvogado());
				pstmt.setDate(idx++, new java.sql.Date(System.currentTimeMillis()));
				pstmt.setString(idx++, usuarioVo.getLogin().toUpperCase());
				pstmt.executeUpdate();
			}
		});
		
		return usuarioVo;
	}
	
	public LdapUsuarioVO adicionarUsuarioPublico(final LdapUsuarioVO usuarioVo) {
		validarEntradaUsuarioPublico(usuarioVo);
		
		final String nomeGrupo = "UsuariosComuns";
		final String nomeGrupoDn = "cn=" + nomeGrupo + "," + propertiesServiceController.getProperty(IProjetoController.LDAP_GRUPOS) + "," + 
				propertiesServiceController.getProperty(IProjetoController.LDAP_ROOT_DN);
		final String basePessoalDn = propertiesServiceController.getProperty(IProjetoController.LDAP_PESSOAS) + "," +
				propertiesServiceController.getProperty(IProjetoController.LDAP_ROOT_DN);
		final String usuarioUid = "uid=" + usuarioVo.getMail() + "," + basePessoalDn;
		
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String consultaUsuario = "select * from usuario where upper(login) = ?";
				String insUsuario = "insert into usuario (nome, login, ativo, dn, uid, cpf, telefone, data_nascimento, email, usuario_externo, data_cadastro, senha, advogado) "
						+ "values (?,?,0,?,?,?,?,?,?,1,?,?,?)";
				String consGrupo = "select * from grupo where nome = ?";
				String insGrupo = "insert into grupo (nome, descricao, dn) values (?,?,?)";
				String insUsrGrp = "insert into usuario_grupo (usuarioid, grupoid, idx) values "
						+ "(?,?,(select case when max(ug2.idx) is null or max(ug2.idx) = 0 then 1 else max(ug2.idx) + 1 end from usuario_grupo ug2))";
				
				PreparedStatement pstmt = connection.prepareStatement(consGrupo);
				pstmt.setString(1, nomeGrupo);
				ResultSet rs = pstmt.executeQuery();
				Long grpId = 0l;
				if(rs.next()) {
					grpId = rs.getLong("id");
				} else {
					PreparedStatement pstmt2 = connection.prepareStatement(insGrupo, Statement.RETURN_GENERATED_KEYS);
					pstmt2.setString(1, nomeGrupo);
					pstmt2.setString(2, "Grupo geral para público externo");
					pstmt2.setString(3, nomeGrupoDn);
					pstmt2.executeUpdate();
					ResultSet rsId = pstmt2.getGeneratedKeys();
					if(rsId.next()) {
						grpId = rsId.getLong(1);
					}
					pstmt2.close();
				}
				rs.close();
				pstmt.close();
				pstmt = connection.prepareStatement(consultaUsuario);
				pstmt.setString(1, usuarioVo.getLogin().toUpperCase());
				rs = pstmt.executeQuery();
				if(rs.next()) {
					throw new ParametroException(IMensagensErro.USUARIO_EXISTENTE_MSG, IMensagensErro.USUARIO_EXISTENTE_CODE);
				} else {
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					Date nascimento = null;
					try {
						if(usuarioVo.getDataNascimento() != null) {
							nascimento = sdf.parse(usuarioVo.getDataNascimento());
						}
					} catch (ParseException e) {
					}
					PreparedStatement pstmt2 = connection.prepareStatement(insUsuario, Statement.RETURN_GENERATED_KEYS);
					pstmt2.setString(1, (StringUtils.isEmpty(usuarioVo.getNome()) ? usuarioVo.getLogin() : usuarioVo.getNome()));
					pstmt2.setString(2, usuarioVo.getLogin());
					pstmt2.setString(3, usuarioUid);
					pstmt2.setString(4, usuarioVo.getMail());
					if (StringUtils.isNotEmpty(usuarioVo.getCpf())) {
						pstmt2.setString(5, usuarioVo.getCpf());
					} else {
						pstmt2.setNull(5, Types.VARCHAR);
					}
					if (StringUtils.isNotEmpty(usuarioVo.getTelefone())) {
						pstmt2.setString(6, usuarioVo.getTelefone());
					} else {
						pstmt2.setNull(6, Types.VARCHAR);
					}
					if (nascimento != null) {
						pstmt2.setDate(7, new java.sql.Date(nascimento.getTime()));
					} else {
						pstmt2.setNull(7, Types.DATE);
					}
					if (StringUtils.isNotEmpty(usuarioVo.getMail())) {
						pstmt2.setString(8, usuarioVo.getMail());
					} else {
						pstmt2.setNull(8, Types.VARCHAR);
					}
					pstmt2.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
					pstmt2.setString(10, authenticationService.encrypt(usuarioVo.getPasswd()));
					pstmt2.setBoolean(11, usuarioVo.isAdvogado());
					pstmt2.executeUpdate();
					
					ResultSet rsId = pstmt2.getGeneratedKeys();
					Long usrId = 0l;
					if(rsId.next()){
						usrId = rsId.getLong(1);
						rsId.close();
						pstmt2.close();
						
						pstmt2 = connection.prepareStatement(insUsrGrp);
						pstmt2.setLong(1, usrId);
						pstmt2.setLong(2, grpId);
						pstmt2.executeUpdate();
						
						usuarioVo.setId(usrId);
					}					
				}
			}
		});
		
		return usuarioVo;
	}

	public void validarEntradaUsuarioPublico(final LdapUsuarioVO usuarioVo) {
		EmailValidator emailValidator = EmailValidator.getInstance();
		if(!emailValidator.isValid(usuarioVo.getMail())) {
			String mensagem = MessageFormat.format(IMensagensErro.FORMATACAO_INVALIDA_MSG, "email");
			throw new ParametroException(mensagem, IMensagensErro.FORMATACAO_INVALIDA_CODE); 
		}		
		if(StringUtils.isNotEmpty(usuarioVo.getCpf()) && !ValidacaoUtil.validaCPF(usuarioVo.getCpf())) {
			String mensagem = MessageFormat.format(IMensagensErro.FORMATACAO_INVALIDA_MSG, "cpf");
			throw new ParametroException(mensagem, IMensagensErro.FORMATACAO_INVALIDA_CODE); 
		}
		if(StringUtils.isNotEmpty(usuarioVo.getTelefone()) && !ValidacaoUtil.validaTelefone(usuarioVo.getTelefone())) {
			String mensagem = MessageFormat.format(IMensagensErro.FORMATACAO_INVALIDA_MSG, "telefone");
			throw new ParametroException(mensagem, IMensagensErro.FORMATACAO_INVALIDA_CODE); 
		}
		if(StringUtils.isNotEmpty(usuarioVo.getDataNascimento()) && !ValidacaoUtil.validaDataNascimento(usuarioVo.getDataNascimento())) {
			String mensagem = MessageFormat.format(IMensagensErro.FORMATACAO_INVALIDA_MSG, "data nascimento");
			throw new ParametroException(mensagem, IMensagensErro.FORMATACAO_INVALIDA_CODE); 
		}
		if(usuarioVo.getMail().contains("@tjdft") || usuarioVo.getMail().contains("@tjdf")) {
			throw new ParametroException(IMensagensErro.USUARIO_CORPORATIVO_MSG, IMensagensErro.USUARIO_CORPORATIVO_CODE);
		}
		Usuario usr = this.getInfoUsuarioComGrupos(usuarioVo.getMail());
		if(usr.getGrupos() != null && usr.getGrupos().size() > 0) {
			List<Grupo> grupos = usr.getGrupos();
			String grupoCorporativo = PropertiesServiceController.getInstance().getProperty("ldap.grupos.usuarios.corporativos");
			for(Grupo grupo : grupos) {
				if(grupo.getNome().equalsIgnoreCase(grupoCorporativo)) {
					throw new ParametroException(IMensagensErro.USUARIO_CORPORATIVO_MSG, IMensagensErro.USUARIO_CORPORATIVO_CODE);
				}
			}
		}
		
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String qUsuario = "select * from usuario where (upper(login) = ? or cpf = ?)";
				PreparedStatement pstmt = connection.prepareStatement(qUsuario);
				pstmt.setString(1, usuarioVo.getMail().toUpperCase());
				pstmt.setString(2, usuarioVo.getCpf());
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()) {
					pstmt.close();
					throw new ParametroException(IMensagensErro.USUARIO_EXISTENTE_MSG, IMensagensErro.USUARIO_EXISTENTE_CODE);
				}
			}
		});
	}
	
	public void validarEntradaAlteracaoUsuarioPublico(final LdapUsuarioVO usuarioVo) {
		EmailValidator emailValidator = EmailValidator.getInstance();
		if(StringUtils.isNotEmpty(usuarioVo.getMail()) && !emailValidator.isValid(usuarioVo.getMail())) {
			String mensagem = MessageFormat.format(IMensagensErro.FORMATACAO_INVALIDA_MSG, "email");
			throw new ParametroException(mensagem, IMensagensErro.FORMATACAO_INVALIDA_CODE); 
		}		
		if(StringUtils.isNotEmpty(usuarioVo.getCpf()) && !ValidacaoUtil.validaCPF(usuarioVo.getCpf())) {
			String mensagem = MessageFormat.format(IMensagensErro.FORMATACAO_INVALIDA_MSG, "cpf");
			throw new ParametroException(mensagem, IMensagensErro.FORMATACAO_INVALIDA_CODE); 
		}
		if(StringUtils.isNotEmpty(usuarioVo.getTelefone()) && !ValidacaoUtil.validaTelefone(usuarioVo.getTelefone())) {
			String mensagem = MessageFormat.format(IMensagensErro.FORMATACAO_INVALIDA_MSG, "telefone");
			throw new ParametroException(mensagem, IMensagensErro.FORMATACAO_INVALIDA_CODE); 
		}
		if(StringUtils.isNotEmpty(usuarioVo.getDataNascimento()) && !ValidacaoUtil.validaDataNascimento(usuarioVo.getDataNascimento())) {
			String mensagem = MessageFormat.format(IMensagensErro.FORMATACAO_INVALIDA_MSG, "data nascimento");
			throw new ParametroException(mensagem, IMensagensErro.FORMATACAO_INVALIDA_CODE); 
		}
		if(StringUtils.isNotEmpty(usuarioVo.getMail()) && (usuarioVo.getMail().contains("@tjdft") || usuarioVo.getMail().contains("@tjdf"))) {
			throw new ParametroException(IMensagensErro.USUARIO_CORPORATIVO_MSG, IMensagensErro.USUARIO_CORPORATIVO_CODE);
		}
		Usuario usr = this.getInfoUsuarioComGrupos(StringUtils.isNotEmpty(usuarioVo.getMail()) ? usuarioVo.getMail() : usuarioVo.getLogin());
		if(usr.getGrupos() != null && usr.getGrupos().size() > 0) {
			List<Grupo> grupos = usr.getGrupos();
			String grupoCorporativo = PropertiesServiceController.getInstance().getProperty("ldap.grupos.usuarios.corporativos");
			for(Grupo grupo : grupos) {
				if(grupo.getNome().equalsIgnoreCase(grupoCorporativo)) {
					throw new ParametroException(IMensagensErro.USUARIO_CORPORATIVO_MSG, IMensagensErro.USUARIO_CORPORATIVO_CODE);
				}
			}
		}
		
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String qUsuario = "select * from usuario where upper(login) = ?";
				PreparedStatement pstmt = connection.prepareStatement(qUsuario);
				pstmt.setString(1, usuarioVo.getLogin());
				ResultSet rs = pstmt.executeQuery();
				if(!rs.next()) {
					pstmt.close();
					throw new ParametroException(IMensagensErro.USUARIO_INEXISTENTE_MSG, IMensagensErro.USUARIO_INEXISTENTE_CODE);
				}
				usuarioVo.setId(rs.getLong("id"));
				if(StringUtils.isEmpty(usuarioVo.getPasswd())) {
					String senha = authenticationService.decrypt(rs.getString("senha"));
					usuarioVo.setPasswd(senha.trim());
					usuarioVo.setAtivo(rs.getBoolean("ativo"));
				}
			}
		});
	}	
	
	public void validarLembreteUsuarioPublico (final LdapUsuarioVO usuarioVo) {
		if(!ValidacaoUtil.validaEmail(usuarioVo.getMail())) {
			String mensagem = MessageFormat.format(IMensagensErro.FORMATACAO_INVALIDA_MSG, "email");
			throw new ParametroException(mensagem, IMensagensErro.FORMATACAO_INVALIDA_CODE); 
		}
	}
	
	public LdapUsuarioVO ativaUsuario(final Long id) {
		if(id == null || id <= 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
		}	
		final LdapUsuarioVO usr = new LdapUsuarioVO(); 
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String consultaUsuario = "select * from usuario where id = ? and usuario_externo = true";
				String ativaUsuario = "update usuario set ativo = true where id = ?";
				PreparedStatement pstmt = connection.prepareStatement(consultaUsuario);
				pstmt.setLong(1, id);
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()) {
					usr.setLogin(rs.getString("login"));
					boolean ativo = rs.getBoolean("ativo");
					if(ativo) {
						throw new ParametroException(IMensagensErro.USUARIO_ATIVO_MSG, IMensagensErro.USUARIO_ATIVO_CODE);
					} else {
						pstmt.close();
						pstmt = connection.prepareStatement(ativaUsuario);
						pstmt.setLong(1, id);
						pstmt.executeUpdate();
					}
				} else {
					throw new ParametroException(IMensagensErro.USUARIO_INEXISTENTE_MSG, IMensagensErro.USUARIO_INEXISTENTE_CODE);
				}
			}
		});
		return usr;
	}
	
	public boolean isUsuarioExterno(final String login) {
		final IntegerWrapper iw = new IntegerWrapper();
		iw.valor = 1;
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String qUsuario = "select * from usuario where upper(login) = ?";
				PreparedStatement pstmt = connection.prepareStatement(qUsuario);
				pstmt.setString(1, login.toUpperCase());
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()) {
					int usuarioExterno = rs.getInt("usuario_externo");
					if(usuarioExterno == 1) {
						iw.valor = 1;
					} else {
						iw.valor = 0;
					}
				} else {
					throw new ParametroException(IMensagensErro.USUARIO_INEXISTENTE_MSG, IMensagensErro.USUARIO_INEXISTENTE_CODE);
				}
			}
		});
		
		return iw.valor == 1;
	}
	
	public boolean isUsuarioAtivoMail(final String mail) {
		final IntegerWrapper iw = new IntegerWrapper();
		iw.valor = 1;
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String qUsuario = "select * from usuario where upper(login) = ?";
				PreparedStatement pstmt = connection.prepareStatement(qUsuario);
				pstmt.setString(1, mail.toUpperCase());
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()) {
					int ativo = rs.getInt("ativo");
					if(ativo == 1) {
						iw.valor = 1;
					} else {
						iw.valor = 0;
					}
				} else {
					throw new ParametroException(IMensagensErro.USUARIO_INEXISTENTE_MSG, IMensagensErro.USUARIO_INEXISTENTE_CODE);
				}
			}
		});
		
		return iw.valor == 1;
	}
	
	public boolean isUsuarioAtivo(final String loginUsuario) {
		final IntegerWrapper iw = new IntegerWrapper();
		iw.valor = 1;
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String qUsuario = "select * from usuario where upper(login) = ?";
				PreparedStatement pstmt = connection.prepareStatement(qUsuario);
				pstmt.setString(1, loginUsuario.toUpperCase());
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()) {
					int ativo = rs.getInt("ativo");
					if(ativo == 1) {
						iw.valor = 1;
					} else {
						iw.valor = 0;
					}
				} else {
					throw new ParametroException(IMensagensErro.USUARIO_EXISTENTE_MSG, IMensagensErro.USUARIO_INEXISTENTE_CODE);
				}
			}
		});
		
		return iw.valor == 1;
	}
	
	public List<TipoSugestaoVO> getTiposSugestao() {
		final List<TipoSugestaoVO> tipos = new ArrayList<TipoSugestaoVO>();
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String qTipos = "select * from tipo_sugestao";
				ResultSet rs = connection.createStatement().executeQuery(qTipos);
				while(rs.next()) {
					TipoSugestaoVO tipo = new TipoSugestaoVO();
					tipo.coTipo = rs.getInt("co_tipo");
					tipo.descricaoTipo = rs.getString("descricao_tipo");
					tipos.add(tipo);
				}
			}
		});
		return tipos;
	}
	
	public void novaSugestao(final SugestaoVO sugestao) {
		if(StringUtils.isEmpty(sugestao.detalhe) || StringUtils.isEmpty(sugestao.loginUsuario) || sugestao.tipoSugestao <= 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
		}
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String iSugestao = "insert into sugestao (usuario_id, tipo_sugestao, detalhe, dh_envio, lida) values ((select u.id from usuario u where upper(u.login) = ?),?,?,?,0)";
				PreparedStatement pstmt = connection.prepareStatement(iSugestao);
				pstmt.setString(1, sugestao.loginUsuario.toUpperCase());
				pstmt.setLong(2, sugestao.tipoSugestao);
				pstmt.setString(3, sugestao.detalhe);
				pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
				pstmt.executeUpdate();
			}
		});
	}
	
	public void enviaMailUsuario(LdapUsuarioVO vo, String assunto, String corpo) {
	    javax.jms.Connection connection = null;
        try {
			connection = connectionFactory.createConnection();
	        javax.jms.Session session = connection.createSession(false,  javax.jms.Session.AUTO_ACKNOWLEDGE);
	        MessageProducer producer = session.createProducer(reportQueue);
            connection.start();

            String token = authenticationService.encrypt(System.currentTimeMillis() + "#" + vo.getId());
            
            InfoEnvioMailMSG info = new InfoEnvioMailMSG();
            info.assunto = assunto;
            String corpoMensagem = corpo;
            corpoMensagem = MessageFormat.format(corpoMensagem, vo.getNome().toUpperCase(), propertiesServiceController.getProperty("raiz.link.ativacao_conta") + "/" + token);
            corpoMensagem.replace("&ocb;", "{");
            corpoMensagem.replace("&ccb;", "}");
            info.conteudo = corpoMensagem;
            System.out.println(info.conteudo);
            info.destinatario = vo.getMail();
            
            ObjectMessage msg = session.createObjectMessage(info);
            producer.send(msg);
            session.close();            
		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}		
	}
	
	public LdapUsuarioVO autenticaUsuarioDB(final String login, final String passwd) {
		if(StringUtils.isEmpty(login) || StringUtils.isEmpty(passwd)) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
		}
		final List<LdapUsuarioVO> usuarios = new ArrayList<LdapUsuarioVO>();
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String qUsuario = "select * from usuario where upper(login) = ?";
				PreparedStatement pstmt = connection.prepareStatement(qUsuario);
				pstmt.setString(1, login.toUpperCase());
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()) {
					LdapUsuarioVO usuario = new LdapUsuarioVO();
					usuario.setLogin(login.toUpperCase());
					usuario.setAdvogado(rs.getBoolean("advogado"));
					usuario.setNome(rs.getString("nome"));
					usuario.setMatricula(rs.getString("matricula"));
					usuario.setAtivo(rs.getBoolean("ativo"));
					usuario.setUid(rs.getString("uid"));
					usuario.setCpf(rs.getString("cpf"));
					usuario.setMail(rs.getString("email"));
					usuario.setUsuarioExterno(rs.getBoolean("usuario_externo"));
					String senha = rs.getString("senha");
					if(StringUtils.isEmpty(senha)) {
						throw new ParametroException(IMensagensErro.USUARIO_CORPORATIVO_MSG, IMensagensErro.USUARIO_CORPORATIVO_CODE);
					}
					senha = authenticationService.decrypt(senha);
					if(passwd.equals(senha.trim())) {
						usuarios.add(usuario);
					}
				} else {
					throw new ParametroException(IMensagensErro.USUARIO_INEXISTENTE_MSG, IMensagensErro.USUARIO_INEXISTENTE_CODE);
				}
			}
		});
		return usuarios.size() > 0 ? usuarios.get(0) : null;
	}
	
	public LdapUsuarioVO enviaLembreteMailUsuario(final LdapUsuarioVO vo) {
		
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection sqlConn) throws SQLException {
				String mailAddress = null;
				String nomeUsr = null;
				String senha = null;
				String qUsuario = "select * from usuario where upper(login) = ? and usuario_externo = true";
				PreparedStatement pstmt = sqlConn.prepareStatement(qUsuario);
				pstmt.setString(1, vo.getMail().toUpperCase());
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()) {
					mailAddress = rs.getString("email");
					nomeUsr = rs.getString("nome");
					senha = rs.getString("senha");
					senha = authenticationService.decrypt(senha);
					vo.setMail(mailAddress);					
				} else {
					throw new ParametroException(IMensagensErro.USUARIO_INEXISTENTE_MSG, IMensagensErro.USUARIO_INEXISTENTE_CODE);
				}				
				
			    javax.jms.Connection connection = null;
		        try {
					connection = connectionFactory.createConnection();
			        javax.jms.Session session = connection.createSession(false,  javax.jms.Session.AUTO_ACKNOWLEDGE);
			        MessageProducer producer = session.createProducer(reportQueue);
		            connection.start();

		            InfoEnvioMailMSG info = new InfoEnvioMailMSG();
		            info.assunto = "Lembrete";
		            
		            String corpoMensagem = propertiesServiceController.getProperty("mail.lembrete.senha");
		            corpoMensagem = MessageFormat.format(corpoMensagem, nomeUsr, senha);		            
		            corpoMensagem.replace("&ocb;", "{");
		            corpoMensagem.replace("&ccb;", "}");
		            info.conteudo = corpoMensagem;
		            info.destinatario = vo.getMail();
		            
		            ObjectMessage msg = session.createObjectMessage(info);
		            producer.send(msg);
		            session.close();            
				} catch (JMSException e) {
					e.printStackTrace();
				} finally {
					try {
						connection.close();
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}		
			}
		});
		return vo;
	}	
	
	public Usuario getIdUsuario(final Usuario usuario) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				ResultSet rs = connection.createStatement().executeQuery("select id from usuario where upper(login) = '" + usuario.getLogin().toUpperCase() + "'");
				Long id = 0l;
				while(rs.next()) {
					id = rs.getLong("id");
				}
				if (id > 0) {
					usuario.setId(id);
				}
			}
		});
		return usuario;
	}
	
	public UsuarioVO getPermissoesUsuarioPorLogin(final String login) {
		final UsuarioVO vo = new UsuarioVO();
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String qDepUsuario = "select du.deploymentid, u.id, u.nome, u.ativo, u.dn, u.uid from deployment_usuario du inner join usuario u on u.id = du.usuarioid where du.usuarioid = (select u.id from usuario u where upper(u.login) = ?)";
				String qGruposUsuario = "select g.id, g.nome, g.descricao, g.dn from grupo g where g.id in (select ug.grupoid from usuario_grupo ug where ug.usuarioid = (select u.id from usuario u where upper(u.login) = ?))";
				String qDepGrupo = "select dg.deploymentid from deployment_grupo dg where dg.grupoid = ?";
				
				PreparedStatement pstmtDepUsuario = connection.prepareStatement(qDepUsuario);
				pstmtDepUsuario.setString(1, login.toUpperCase());
				ResultSet rsDepUsuario = pstmtDepUsuario.executeQuery();
				vo.deployments = new ArrayList<Long>();
				while(rsDepUsuario.next()) {
					vo.id = rsDepUsuario.getLong("id");
					vo.ativo = rsDepUsuario.getBoolean("ativo");
					vo.showName = rsDepUsuario.getString("nome");
					vo.distinguishedName = rsDepUsuario.getString("dn");
					vo.deployments.add(rsDepUsuario.getLong("deploymentid"));
				}
				rsDepUsuario.close();
				pstmtDepUsuario.close();
				
				PreparedStatement pstmtGpUsuario = connection.prepareStatement(qGruposUsuario);
				PreparedStatement pstmtDepGrp = connection.prepareStatement(qDepGrupo);
				pstmtGpUsuario.setString(1, login.toUpperCase());
				ResultSet rsGrps = pstmtGpUsuario.executeQuery();
				vo.grupos = new ArrayList<GrupoVO>();
				while(rsGrps.next()) {
					GrupoVO grp = new GrupoVO();
					grp.nome = rsGrps.getString("nome");
					grp.id = rsGrps.getLong("id");
					grp.dn = rsGrps.getString("dn");
					pstmtDepGrp.clearParameters();
					pstmtDepGrp.setLong(1, grp.id);
					ResultSet rsDepGrupo = pstmtDepGrp.executeQuery();
					grp.deployments = new ArrayList<Long>();
					while(rsDepGrupo.next()) {
						grp.deployments.add(rsDepGrupo.getLong("deploymentid"));
					}
					rsDepGrupo.close();
					vo.grupos.add(grp);
				}
			}
		});
		return vo;
	}
	
	public void removeGrupo(String nomeGrupo) {
		WorkRemoveGrupo workRemGrupo = new WorkRemoveGrupo();
		Grupo grupo = new Grupo();
		grupo.setNome(nomeGrupo);
		workRemGrupo.grupo = grupo;
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(workRemGrupo);
	}
	
	public void removeUsuario(String uid) {
		WorkRemoveUsuario work = new WorkRemoveUsuario();
		work.uid = uid;
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(work);
	}
	
	public void removeUsuarioPorMailPermanentemente(final String mail) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String d1 = "delete from deployment_usuario where usuarioid = (select u.id from usuario u where upper(u.login) = ?)";
				String d2 = "delete from usuario_grupo where usuarioid = (select u.id from usuario u where upper(u.login) = ?)";
				String d3 = "delete from mensagem_usuario where usuario_id = (select u.id from usuario u where upper(u.login) = ?)";
				String d5 = "delete from historico where usuario_id = (select u.id from usuario u where upper(u.login) = ?)";
				String d6 = "delete from instancia where usuario_retorno_id = (select u.id from usuario u where upper(u.login) = ?) or usuario_id = (select u.id from usuario u where upper(u.login) = ?)";
				String d7 = "delete from usuario where upper(u.login) = ?";
				
				PreparedStatement pstmt = connection.prepareStatement(d1);
				pstmt.setString(1, mail.toUpperCase());
				pstmt.executeUpdate();
				pstmt.close();
				
				pstmt = connection.prepareStatement(d2);
				pstmt.setString(1, mail.toUpperCase());
				pstmt.executeUpdate();
				pstmt.close();

				pstmt = connection.prepareStatement(d3);
				pstmt.setString(1, mail.toUpperCase());
				pstmt.executeUpdate();
				pstmt.close();

				pstmt = connection.prepareStatement(d5);
				pstmt.setString(1, mail.toUpperCase());
				pstmt.executeUpdate();
				pstmt.close();

				pstmt = connection.prepareStatement(d6);
				pstmt.setString(1, mail.toUpperCase());
				pstmt.setString(2, mail.toUpperCase());
				pstmt.executeUpdate();
				pstmt.close();

				pstmt = connection.prepareStatement(d7);
				pstmt.setString(1, mail.toUpperCase());
				pstmt.executeUpdate();
				pstmt.close();
			}
		});
	}
	
	public Boolean grupoTemDeployment(String nomeGrupo) {
		WorkCheckGrupoDeployment work = new WorkCheckGrupoDeployment();
		work.nomeGrupo = nomeGrupo;
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(work);
		return work.existe;
	}
	
	public void addUserToGroup(LdapUsuarioVO usuariovo, LdapGrupoVO grupovo) {
		WorkAdicionaUsuarioGrupo work = new WorkAdicionaUsuarioGrupo();
		work.grupovo = grupovo;
		work.usuariovo = usuariovo;
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(work);
		
	}
	
	public void removeUserFromGroup(String uid, String nomeGrupo) {
		WorkRemoveUsuarioGrupo work = new WorkRemoveUsuarioGrupo();
		work.nomeGrupo = nomeGrupo;
		work.uid = uid;
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(work);
	}
	
	public List<LdapUsuarioVO> getUsuariosPorGrupo (final String nomeGrupo) {
		final List<LdapUsuarioVO> usuarios = new ArrayList<LdapUsuarioVO>();
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String queryUsuarios = "select u.* from usuario u where u.id in (select ug.usuarioid from usuario_grupo ug where ug.grupoid = (select g.id from grupo g where g.nome = ?)) ";
				PreparedStatement pstmt = connection.prepareStatement(queryUsuarios);
				pstmt.setString(1, nomeGrupo);
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()){
					LdapUsuarioVO usuario = new LdapUsuarioVO();
					usuario.setNome(rs.getString("nome"));
					usuario.setAdvogado(rs.getBoolean("advogado"));
					usuario.setAtivo(rs.getBoolean("ativo"));
					usuario.setCn(rs.getString("nome"));
					Date dtNascimento = rs.getDate("data_nascimento");
					if(dtNascimento != null) {
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						usuario.setDataNascimento(sdf.format(dtNascimento));
					}
					usuario.setCpf(rs.getString("cpf"));
					usuario.setUid(rs.getString("uid"));
					usuario.setTelefone(rs.getString("telefone"));
					usuario.setMail(rs.getString("email"));
					usuario.setLogin(rs.getString("login"));
					usuario.setUsuarioExterno(rs.getBoolean("usuario_externo"));
					//usuario.setSenha(rs.getString("senha"));
					usuarios.add(usuario);
				}
			}
		});
		return usuarios;
	}
	
	public Integer getQtdDeploymentsAtualizadosUsuario(final RequisicaoAtualizacoesVO vo) {
		final IntegerWrapper iw = new IntegerWrapper();
		iw.setValor(0);
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String strQuery = "select h2.id, h2.dh_alteracao from historico h2 where h2.id = (select max(h1.id) from historico h1 where h1.projeto_id = (select projeto_id from deployment where id = ?)"
						+ " and h1.codigo in ("+ Historico.CODIGO_DEPLOY + ", " + Historico.CODIGO_REFRESH_DEPLOYMENT + ", " + Historico.CODIGO_ALTERACAO_DEPLOYMENT +"))";
				PreparedStatement pstmt = connection.prepareStatement(strQuery);
				HashMap<Long, Date> deps = new HashMap<Long, Date>();
				for(DeploymentVO depVo : vo.deployments) {
					pstmt.clearParameters();
					pstmt.setLong(1, depVo.id);
					deps.put(depVo.id, new Date(depVo.dhAlteracao));
					ResultSet rs = pstmt.executeQuery();
					if(rs.next()) {
						Long hid = rs.getLong(1);
						Timestamp tm = rs.getTimestamp(2);
						if (tm.after(new Date(depVo.dhAlteracao))) {
							iw.setValor(iw.getValor() + 1);
						}
					}					
				}
				pstmt.close();
				
				strQuery = "select du.deploymentid as depid_u, dg.DEPLOYMENTID as depid_g "
					+"from usuario u " 
					+"left join deployment_usuario du on du.usuarioid = u.id "
					+"left join usuario_grupo ug on ug.USUARIOID = u.ID "
					+"left join deployment_grupo dg on dg.GRUPOID = ug.GRUPOID "
					+"where upper(u.LOGIN) = ? "
					+"and (du.DEPLOYMENTID is not null or dg.DEPLOYMENTID is not null) "
					+"group by du.deploymentid, dg.DEPLOYMENTID";
				pstmt = connection.prepareStatement(strQuery);
				pstmt.setString(1, vo.login.toUpperCase());
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()) {
					Long idU = rs.getLong(1);
					Long idG = rs.getLong(2);
					if(idU != null && idU > 0 && !deps.containsKey(idU)) {
						iw.setValor(iw.getValor() + 1);
					}
					if(idG != null && idG > 0 && idG != idU && !deps.containsKey(idG)) {
						iw.setValor(iw.getValor() + 1);
					}
				}
			}
		});
		return iw.getValor();
	}
	
	public Integer getQtdMensagensNovasUsuario(final RequisicaoAtualizacoesVO vo) {
		final IntegerWrapper iw = new IntegerWrapper();
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				PreparedStatement pstmtUG = connection.prepareStatement("select count(*) from mensagem m "
						+ "where (m.usuario_mensagem_id = (select u.id from usuario u where upper(u.login) = ?) "
						+ "or m.grupo_id in (select ug.grupoid from usuario_grupo ug where ug.usuarioid = (select u.id from usuario u where upper(u.login) = ?)) "
						+ "or m.equipamento_id = (select e.id from equipamento e where e.serial = ?) "
						+ "or m.grupo_equipamento_id in (select ge.grupo_equipamentoid from grp_equipamento_equipamento ge where ge.equipamentoid = (select e.id from equipamento e where e.serial = ?))) "
						+ "and m.id > ?");
				pstmtUG.setString(1, vo.login.toUpperCase());
				pstmtUG.setString(2, vo.login.toUpperCase());
				pstmtUG.setString(3, vo.aparelho.getDeviceUUID());
				pstmtUG.setString(4, vo.aparelho.getDeviceUUID());
				pstmtUG.setLong(5, vo.ultimaMensagemId);
				ResultSet rs = pstmtUG.executeQuery();
				if(rs.next()) {
					iw.setValor(rs.getInt(1));
				} else {
					iw.setValor(0);
				}
			}
		});
		return iw.getValor();
	}
	
	private class IntegerWrapper {
		public Integer valor;
		public void setValor(Integer vlr) {
			this.valor = vlr;
		}
		
		public Integer getValor() {
			return this.valor;
		}
	}
}
