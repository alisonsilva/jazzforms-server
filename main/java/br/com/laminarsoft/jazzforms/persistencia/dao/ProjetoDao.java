package br.com.laminarsoft.jazzforms.persistencia.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Ehcache;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.laminarsoft.jazzforms.persistencia.model.Deployment;
import br.com.laminarsoft.jazzforms.persistencia.model.Grupo;
import br.com.laminarsoft.jazzforms.persistencia.model.Historico;
import br.com.laminarsoft.jazzforms.persistencia.model.ImplementacaoEvento;
import br.com.laminarsoft.jazzforms.persistencia.model.Pagina;
import br.com.laminarsoft.jazzforms.persistencia.model.Projeto;
import br.com.laminarsoft.jazzforms.persistencia.model.Usuario;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.IMensagensErro;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.ParametroException;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.ParametroProjetoException;
import br.com.laminarsoft.jazzforms.persistencia.model.senchatransform.SenchaImplArraySerializer;
import br.com.laminarsoft.jazzforms.persistencia.model.senchatransform.SenchaImplSerializer;
import br.com.laminarsoft.jazzforms.persistencia.model.senchatransform.SenchaImplementacao;
import br.com.laminarsoft.jazzforms.persistencia.model.senchatransform.SenchaItem;
import br.com.laminarsoft.jazzforms.persistencia.model.senchatransform.SenchaOption;
import br.com.laminarsoft.jazzforms.persistencia.model.senchatransform.SenchaPage;
import br.com.laminarsoft.jazzforms.persistencia.model.senchatransform.SenchaPicker;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Button;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Camera;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Carousel;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Chart;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.CheckBox;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Component;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Container;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.DataView;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.DatePicker;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Email;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Field;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.FieldSet;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.FormPanel;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.GPSField;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Hidden;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Option;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Panel;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Password;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Select;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Sheet;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Slider;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Spinner;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Tab;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.TabPanel;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Text;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.TextArea;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.TitleBar;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Toggle;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.ToolBar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


@Transactional(propagation=Propagation.SUPPORTS, 
		isolation=Isolation.READ_UNCOMMITTED, 
		timeout=60)
@Repository("ProjetoDao")
@SuppressWarnings("all")
public class ProjetoDao extends BaseDao<Projeto> {
	private static final long serialVersionUID = 1L;
	@Autowired private EhCacheCacheManager cacheManager;

	
	@Autowired
	private PaginaDao paginaDao;	
	
	public class WorkUpdateDeploymentProject implements Work {
		Projeto projeto;
		
		@Override
		public void execute(Connection connection) throws SQLException {
			String updt = "update projeto set projetoid = " + projeto.getProjetoBase().getId() + " where id = " + projeto.getId();
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(updt);
			
			updt = "update deployment set projeto_id = " + projeto.getId() + " where id = " + projeto.getDeployment().getId();
			stmt.executeUpdate(updt);
			stmt.close();
			
			PreparedStatement pstmt = connection.prepareStatement("update pagina set pagina_json = ? where id = ?");
			for (Pagina pagina : projeto.getPaginas()) {
				pstmt.setBytes(1, pagina.getPaginaJson());
				pstmt.setBigDecimal(2, new BigDecimal(pagina.getId()));
				pstmt.executeUpdate();
				pstmt.clearParameters();
			}
			pstmt.close();
		}
		
	}
	
	public class WorkValidacaoProjeto implements Work {
		Projeto projeto;
		ValidaPreenchimentoProjeto validacao = new ValidaPreenchimentoProjeto();
        Map<String, String> nomesCampos = new HashMap<String, String>();

		@Override
        public void execute(Connection connection) throws SQLException {
	        Long idProjeto = projeto.getId();
	        String nomeProjeto = projeto.getNome();
	        validacao.valido = true;
	        String qNomeProjeto = "select count(id) from projeto where nome = ? and id <> ? and publicado = 0 and deployment_id is null and projetoid is null";
	        
	        PreparedStatement pstmt = connection.prepareStatement(qNomeProjeto);
	        pstmt.setString(1, nomeProjeto);
	        pstmt.setLong(2, (idProjeto == null ? 0l : idProjeto));
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	        	int qtd = rs.getInt(1);
	        	if (qtd > 0) {
	        		validacao.valido = false;
	        		CampoValidado cval = new CampoValidado();
	        		cval.nomeCampo = nomeProjeto;
	        		cval.mensagem = "Nome de projeto duplicado";
	        		validacao.naoValidos.add(cval);
	        	}
	        }
	        if (validacao.valido) {
	        	for(int idxPag = 0; idxPag < projeto.getPaginas().size(); idxPag++) {
	        		if(nomesCampos.containsKey(projeto.getPaginas().get(idxPag).getNome())) {
	        			validacao.valido = false;
		        		CampoValidado cval = new CampoValidado();
		        		cval.nomeCampo = nomeProjeto;
		        		cval.mensagem = "Nome da página repetido";
	        			validacao.naoValidos.add(cval);
	        			break;
	        		} else {
	        			nomesCampos.put(projeto.getPaginas().get(idxPag).getNome(), projeto.getPaginas().get(idxPag).getNome());
	        			validaNomesComponentes(projeto.getPaginas().get(idxPag).getContainer());
	        		}
	        	}
	        }
        }
		
		private void validaNomesComponentes(Component cmp) {
			if (nomesCampos.containsKey(cmp.getFieldId())) {
        		validacao.valido = false;
        		CampoValidado cval = new CampoValidado();
        		cval.nomeCampo = cmp.getFieldId();
        		cval.mensagem = "Nome do campo duplicado";
        		validacao.naoValidos.add(cval);
			} else {
				if(cmp instanceof Carousel) {
					Carousel car = (Carousel)cmp;
					for(int idxPag = 0; idxPag < car.getPaginas().size(); idxPag++) {
						Tab pagina = car.getPaginas().get(idxPag);
						for(int idxCmp = 0; idxCmp < pagina.getItems().size(); idxCmp++) {
							validaNomesComponentes(pagina.getItems().get(idxCmp));
						}
					}
				} else if (cmp instanceof TabPanel) {
					TabPanel tabp = (TabPanel)cmp;
					for(int idxTab = 0; idxTab < tabp.getTabs().size(); idxTab++) {
						Tab tab = tabp.getTabs().get(idxTab);
						for(int idxCmp = 0; idxCmp < tab.getItems().size(); idxCmp++) {
							validaNomesComponentes(tab.getItems().get(idxCmp));
						}
					}
				} else if (cmp instanceof Container) {
					Container cnt = ((Container)cmp);
					for (int idxCnt = 0; idxCnt < cnt.getItems().size(); idxCnt++) {
						validaNomesComponentes(cnt.getItems().get(idxCnt));						
					}
				}
			}
		}
	}
	
	public class WorkDeactivateDeployedProject implements Work {
		Long deployedProjetoId;
		Boolean flagActivate;
		String msgRetorno = "";
		String usuario = "";
		
		@Override
		public void execute(Connection connection) throws SQLException {
			String updt = "update deployment set ativo = " + flagActivate + " where projeto_id = " + deployedProjetoId;
			//TODO concluir a alteração do historico
			String insHistorico = "insert into historico (instancia_id, usuario_id, projeto_id, dh_alteracao, descricao, idx, codigo) values (null, (select id from usuario where upper(login) = ?), ?, ?, ?, 1, ?)";
			
			// encontra a quantidade de deployments ativos para o projeto principal
			String findAtivosDiferentesReferencia = "select count(dep.id) from deployment dep "
							+"where dep.projeto_id in ( "
									+"select p1.id from projeto p1 "
									+"where p1.projetoid = (select p2.projetoid from projeto p2 where p2.id = " + deployedProjetoId + ")) "
							+"and dep.ativo = true "
							+"and dep.projeto_id <> " + deployedProjetoId;
			
			if(flagActivate) {// caso seja ativação, verificar se já não há deployments ativos
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(findAtivosDiferentesReferencia);
				if(rs.next()) {
					int qtd = rs.getInt(1);
					if (qtd > 0) {
						msgRetorno = "Existem implantações ativas para o projeto referido";
						return;
					} else {
						Statement stmtUpdt = connection.createStatement();
						stmtUpdt.executeUpdate(updt);
						stmtUpdt.close();
					}
				}
			} else {
				Statement stmtUpdt = connection.createStatement();
				stmtUpdt.executeUpdate(updt);
				stmtUpdt.close();
			}
			PreparedStatement pstmt = connection.prepareStatement(insHistorico);
			pstmt.setString(1, usuario.toUpperCase());
			pstmt.setLong(2, deployedProjetoId);
			pstmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
			pstmt.setString(4, "Ativação/Desativação do deployment");
			pstmt.setInt(5, Historico.CODIGO_ATIVADESATIVA_DEPLOYMENT);
			pstmt.executeUpdate();
			pstmt.close();
		}
		
	}	
	
	public class WorkInsertDeployment implements Work {

		Deployment deploy;
		@Override
		public void execute(Connection connection) throws SQLException {
			String insertDeploy = "insert into deployment (projeto_id, dh_publicacao, nome_processo_negocio, nome_atividade_negocio) values (?,?,?,?)";
			PreparedStatement pstmt = connection.prepareStatement(insertDeploy, Statement.RETURN_GENERATED_KEYS);
			int posstmt = 1;
			pstmt.setLong(posstmt++, deploy.getProjeto().getId());
			pstmt.setTimestamp(posstmt++, new Timestamp(deploy.getDhPublicacao().getTime()));
			pstmt.setString(posstmt++, deploy.getNomeProcessoNegocio());
			pstmt.setString(posstmt++, deploy.getNomeAtividadeNegocio());
			pstmt.executeUpdate();
			
			ResultSet rs = pstmt.getGeneratedKeys();
			if(rs.next()) {
				deploy.setId(rs.getLong(1));
			}
			rs.close();
			pstmt.close();
			
			for(Usuario usr : deploy.getUsuariosPossiveis()) {
				long usrId = manterUsuario(usr, connection);
				Statement insertDepUsr = connection.createStatement();
				insertDepUsr.executeUpdate("insert into deployment_usuario (deploymentid, usuarioid, idx) values (" + deploy.getId() +"," + usrId + ",1)");
				insertDepUsr.close();
				usr.getDeployment().add(deploy);
			}
			
			for(Grupo grp : deploy.getGruposPossiveis()) {
				long grpId = manterGrupo(grp, connection);
				
				Statement insertDepGrp = connection.createStatement();
				insertDepGrp.executeUpdate("insert into deployment_grupo (deploymentid, grupoid, idx) values ("+ deploy.getId() + ", " + grpId + ", 1)");
				insertDepGrp.close();
				grp.getDeployment().add(deploy);
			}
			
			
			pstmt.close();
		}
		
		public long manterGrupo(Grupo grupo, Connection connection) throws SQLException {
			long idGrupo = 0;
			String qgrupo = "select * from grupo where nome = '" + grupo.getNome() + "'";
			ResultSet rs = connection.createStatement().executeQuery(qgrupo);
			if (rs.next()) {
				idGrupo = rs.getLong("id");
			} else {
				rs.close();
				PreparedStatement pstmt = connection.prepareStatement("insert into grupo (nome, descricao) values (?,?)", Statement.RETURN_GENERATED_KEYS);
				pstmt.setString(1, grupo.getNome());
				pstmt.setString(2, grupo.getDescricao());
				pstmt.executeUpdate();
				
				rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					idGrupo = rs.getLong(1);
				}
				pstmt.close();
				rs.close();
			}
			return idGrupo;
		}
		
		public long manterUsuario(Usuario usuario, Connection connection) throws SQLException {
			long idUsuario = 0;
			String selectUsuario = "select id from usuario where upper(login) = '" + usuario.getLogin().trim().toUpperCase() + "'";
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(selectUsuario);
			if(rs.next()) { // se o usuário existir, recupera seu id
				idUsuario = rs.getLong("id");
				usuario.setId(idUsuario);
			} else { // se não existir, deverá ser inserido
				String insUsuario = "insert into usuario (nome, login) values (?,?)";
				PreparedStatement pstmtUsuario = connection.prepareStatement(insUsuario, Statement.RETURN_GENERATED_KEYS);
				pstmtUsuario.setString(1, usuario.getNome());
				pstmtUsuario.setString(2, usuario.getLogin());
				pstmtUsuario.executeUpdate();
				
				rs = pstmtUsuario.getGeneratedKeys();
				if(rs.next()) {
					usuario.setId(rs.getLong(1));
					idUsuario = usuario.getId();
				}
			}
			rs.close();
			String selUsuarioGrupo = "select * from usuario_grupo where usuarioid = ? and grupoid = ?";
			String insUsuarioGrupo = "insert into usuario_grupo (usuarioid, grupoid, idx) values (?,?,?)";
			String insGrupo = "insert into grupo (nome, descricao) values (?,?)";
			PreparedStatement pstmtSelUsrGrp = connection.prepareStatement(selUsuarioGrupo);
			PreparedStatement pstmtInsUsrGrp = connection.prepareStatement(insUsuarioGrupo);
			PreparedStatement pstmtGrupo = connection.prepareStatement(insGrupo, Statement.RETURN_GENERATED_KEYS); 
			for(Grupo grupo : usuario.getGrupos()) {// insere grupos
				String selectGrupo = "select id from grupo where nome = '" + grupo.getNome() + "'";
				stmt = connection.createStatement();
				rs = stmt.executeQuery(selectGrupo);
				if (!rs.next()) {
					pstmtGrupo.setString(1, grupo.getNome());
					pstmtGrupo.setString(2, grupo.getDescricao());
					pstmtGrupo.executeUpdate();
					rs = pstmtGrupo.getGeneratedKeys();
					if(rs.next()) {
						grupo.setId(rs.getLong(1));
					}
				} else {
					grupo.setId(rs.getLong(1));
				}
				pstmtGrupo.clearParameters();
				rs.close();
				
				pstmtSelUsrGrp.setLong(1, usuario.getId());
				pstmtSelUsrGrp.setLong(2, grupo.getId());
				rs = pstmtSelUsrGrp.executeQuery();
				if (!rs.next()) {// povoa tabela relacionamento, caso não haja um relacionamento entre o usuário e o grupo em questão
					pstmtInsUsrGrp.setLong(1, usuario.getId());
					pstmtInsUsrGrp.setLong(2, grupo.getId());
					pstmtInsUsrGrp.setInt(3, 1);
					pstmtInsUsrGrp.executeUpdate();
				}
				pstmtSelUsrGrp.clearParameters();
				pstmtInsUsrGrp.clearParameters();
				rs.close();
			}
			pstmtSelUsrGrp.close();
			pstmtInsUsrGrp.close();
			pstmtGrupo.close();
			
			return idUsuario;
		}		
	}
	
	public class WorkInsertHistoricoProjeto implements Work {
		
		Projeto projeto;
		List<Historico> historicos;

		@Override
		public void execute(Connection connection) throws SQLException {
			List<Historico> lstHist = historicos;
			if(lstHist == null) {
				lstHist = projeto.getHistoricos();
			}
			for (Historico historico : lstHist) {
				if (historico.getId() == 0) {
					int intIdxHistoricoProjeto = 1;
					long idHistorico;
					String queryIdxHistorico = "select max(idx) from historico where projeto_id = "
							+ projeto.getId();
					String insertHistorico = "insert into historico (usuario_id, projeto_id, dh_alteracao, descricao, idx) values "
							+ "((select u.id from usuario u where upper(login) = ?),?,?,?,?)";
					Statement stmt = connection.createStatement();
					ResultSet rs = stmt.executeQuery(queryIdxHistorico);
					if (rs.next()) {
						intIdxHistoricoProjeto = rs.getInt(1) + 1;
					}
					rs.close();
					//verifica existencia usuario
					mantemUsuario(historico.getUsuario(), connection);
					PreparedStatement pstmt = connection.prepareStatement(
							insertHistorico, Statement.RETURN_GENERATED_KEYS);
					int posstmt = 1;
					pstmt.setString(posstmt++, historico.getUsuario().getLogin().toUpperCase());
					pstmt.setLong(posstmt++, projeto.getId());
					pstmt.setTimestamp(posstmt++, new Timestamp(historico
							.getDhAlteracao().getTime()));
					pstmt.setString(posstmt++, historico.getDescricao());
					pstmt.setInt(posstmt++, intIdxHistoricoProjeto);
					pstmt.executeUpdate();
					rs = pstmt.getGeneratedKeys();
					if (rs.next()) {
						historico.setId(rs.getLong(1));
					}
					rs.close();
					pstmt.close();
				}
			}
		}
		
		public long mantemUsuario(Usuario usuario, Connection connection) throws SQLException {
			long idUsuario = 0;
			String selectUsuario = "select id from usuario where upper(login) = '" + usuario.getLogin().trim().toUpperCase() + "'";
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(selectUsuario);
			if(rs.next()) { // se o usuário existir, recupera seu id
				idUsuario = rs.getLong("id");
				usuario.setId(idUsuario);
			} else { // se não existir, deverá ser inserido
				String insUsuario = "insert into usuario (nome, login) values (?,?)";
				PreparedStatement pstmtUsuario = connection.prepareStatement(insUsuario, Statement.RETURN_GENERATED_KEYS);
				pstmtUsuario.setString(1, usuario.getNome());
				pstmtUsuario.setString(2, usuario.getLogin());
				pstmtUsuario.executeUpdate();
				
				rs = pstmtUsuario.getGeneratedKeys();
				if(rs.next()) {
					usuario.setId(rs.getLong(1));
					idUsuario = usuario.getId();
				}
			}
			rs.close();
			String selUsuarioGrupo = "select * from usuario_grupo where usuarioid = ? and grupoid = ?";
			String insUsuarioGrupo = "insert into usuario_grupo (usuarioid, grupoid, idx) values (?,?,?)";
			String insGrupo = "insert into grupo (nome, descricao) values (?,?)";
			PreparedStatement pstmtSelUsrGrp = connection.prepareStatement(selUsuarioGrupo);
			PreparedStatement pstmtInsUsrGrp = connection.prepareStatement(insUsuarioGrupo);
			PreparedStatement pstmtGrupo = connection.prepareStatement(insGrupo, Statement.RETURN_GENERATED_KEYS); 
			for(Grupo grupo : usuario.getGrupos()) {// insere grupos
				String selectGrupo = "select id from grupo where nome = '" + grupo.getNome() + "'";
				stmt = connection.createStatement();
				rs = stmt.executeQuery(selectGrupo);
				if (!rs.next()) {
					pstmtGrupo.setString(1, grupo.getNome());
					pstmtGrupo.setString(2, grupo.getDescricao());
					pstmtGrupo.executeUpdate();
					rs = pstmtGrupo.getGeneratedKeys();
					if(rs.next()) {
						grupo.setId(rs.getLong(1));
					}
				} else {
					grupo.setId(rs.getLong(1));
				}
				pstmtGrupo.clearParameters();
				rs.close();
				
				pstmtSelUsrGrp.setLong(1, usuario.getId());
				pstmtSelUsrGrp.setLong(2, grupo.getId());
				rs = pstmtSelUsrGrp.executeQuery();
				if (!rs.next()) {// povoa tabela relacionamento, caso não haja um relacionamento entre o usuário e o grupo em questão
					pstmtInsUsrGrp.setLong(1, usuario.getId());
					pstmtInsUsrGrp.setLong(2, grupo.getId());
					pstmtInsUsrGrp.setInt(3, 1);
					pstmtInsUsrGrp.executeUpdate();
				}
				pstmtSelUsrGrp.clearParameters();
				pstmtInsUsrGrp.clearParameters();
				rs.close();
			}
			pstmtSelUsrGrp.close();
			pstmtInsUsrGrp.close();
			pstmtGrupo.close();
			
			return idUsuario;
		}
		
	}
	
	
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=30)	
	@Override
	public Long persist(Projeto projeto) throws ParametroException {
		Long ret = new Long(0);
		if (projeto == null) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
		}
		
		
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		WorkValidacaoProjeto workVal = new WorkValidacaoProjeto();
		workVal.projeto = projeto;
		session.doWork(workVal);
		
		if (!workVal.validacao.valido) {
			throw new ParametroProjetoException(workVal.validacao);
		}
				
		List<Historico> historicos = projeto.getHistoricos();
		projeto.setHistoricos(new ArrayList<Historico>());
		ret = super.persist(projeto);
		projeto.setHistoricos(historicos);
		projeto.setId(ret);
		if (projeto.getHistoricos().size() > 0) {
			session = hibernateTemplate.getSessionFactory()
					.getCurrentSession();
			WorkInsertHistoricoProjeto work = new WorkInsertHistoricoProjeto();
			work.projeto = projeto;
			session.doWork(work);
		}		
		Cache cache = cacheManager.getCache("projeto");
		cache.put(ret, projeto);
		return ret;
	}
	
	
	
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=50)	
	public Projeto refreshProjetoDeployment(final Long deploymentId, final String loginUsuario) {
		if (deploymentId == null || deploymentId <= 0 || loginUsuario == null || loginUsuario.isEmpty()) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_ID_INVALIDO);
		}
		Cache cache = cacheManager.getCache("deployment");
		Cache cachePrj = cacheManager.getCache("projeto");
		
		Deployment deployment = null;
		if (cache.get(deploymentId) == null) {
			deployment = hibernateTemplate.get(Deployment.class, deploymentId);
		} else {
			deployment = (Deployment)(cache.get(deploymentId).get());
		}
		final Historico hist = new Historico();
		hist.setDhAlteracao(new Date());
		hist.setCodigo(Historico.CODIGO_REFRESH_DEPLOYMENT);
		hist.setDescricao("Refresh do projeto principal do deployment");

//		Usuario usr = (Usuario)hibernateTemplate.find("from Usuario usr where usr.login = ?", loginUsuario).get(0);
		
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				PreparedStatement pstmt = connection.prepareStatement("select id from usuario where upper(login) = ?");
				pstmt.setString(1, loginUsuario.toUpperCase());
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()) {
					Usuario usr = new Usuario();
					usr.setId(rs.getLong("id"));
					usr.setLogin(loginUsuario);
					hist.setUsuario(usr);
				}
			}
		});
		Projeto projetoBase = deployment.getProjeto().getProjetoBase();
		projetoBase = (Projeto)(cachePrj.get(projetoBase.getId()).get());
		
		final Projeto prj = projetoBase.clone();
		prj.setId(0);
		for(Pagina pg : prj.getPaginas()) {
			pg.setId(null);
		}
		prj.setDhCriacao(new Date());
		final Projeto projetoAnterior = deployment.getProjeto();
		prj.setPublicado(true);
		
		projetoAnterior.setDeployment(null);
		deployment.setProjeto(null);
		projetoAnterior.setPublicado(false);
		projetoAnterior.setDeploymentId(deploymentId);
		
		session.doWork(new Work() {// altera configurações do projeto anterior (projeto anteriormente como sendo o projeto referenciado pelo deploy)
			
			@Override
			public void execute(Connection connection) throws SQLException {
				PreparedStatement pstmt = connection.prepareStatement("update projeto set publicado = ?, deployment_id = ? where id = ?");
				pstmt.setBoolean(1, false);
				pstmt.setLong(2, deploymentId);
				pstmt.setLong(3, projetoAnterior.getId());
				pstmt.executeUpdate();
			}
		});
		
		//super.persist(projetoAnterior);
		
		prj.setProjetoBase(projetoBase);

		for (Pagina pag : prj.getPaginas()) {
			String json = recuperaJsonPagina(pag);
			pag.setPaginaJson(json.getBytes());
		}		
		
//		hibernateTemplate.persist(hist);
		
		prj.setDeployment(null);
		long ret = super.persist(prj);
		prj.setId(ret);
		deployment.setProjeto(prj);
		prj.setDeployment(deployment);
		deployment.descricaoProjeto = prj.getDescricao();
		deployment.idProjeto = prj.getId();
		deployment.nomeProjeto = prj.getNome();
		deployment.projetoAplicacao = prj.getAplicacao() ? 1 : 0;
		List<Historico> historicos = new ArrayList<Historico>();
		historicos.add(hist);		
		
		if(historicos.size() > 0) {
			WorkInsertHistoricoProjeto work = new WorkInsertHistoricoProjeto();
			work.projeto = prj;
			work.historicos = historicos;
			session.doWork(work);
		}
		
		
		WorkUpdateDeploymentProject work = new WorkUpdateDeploymentProject();
		work.projeto = prj;
		session.doWork(work);
		
		deployment.dhUltimaAtualizacao = getUltimaDataAutalizacaoDeployment(deployment.getId()).getTime();
		
		for(Pagina pag : prj.getPaginas()) {
			Container cnt = pag.getContainer();
			for(Component cmp : cnt.getItems()) {
				updateComponentIdItem(cnt.getId(), cmp);
			}
		}
		
		cache.put(deployment.getId(), deployment);
		cachePrj.put(prj.getId(), prj);
		
		return prj;		
	}	
	
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=50)	
	private void updateComponentIdItem(final Long componentId, final Component item) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String updtComponent = "update sncha_component set component_id = ? where id = ?";
				PreparedStatement pstmt = connection.prepareStatement(updtComponent);
				pstmt.setLong(1, componentId);
				pstmt.setLong(2, item.getId());
				pstmt.executeUpdate();
			}
		});
		
		if(item instanceof Container) {
			if(item instanceof Carousel) {
				Carousel car = (Carousel)item;
				for(Tab tab : car.getPaginas()) {
					for(Component comp : tab.getItems()) {
						updateComponentIdItem(car.getId(), comp);
					}
				}
			} else if(item instanceof TabPanel) {
				TabPanel panl = (TabPanel)item;
				for(Tab tab : panl.getTabs()) {
					for(Component comp : tab.getItems()) {
						updateComponentIdItem(panl.getId(), comp);
					}
				}
			} else {
				Container cont = (Container)item;
				for(Component comp : cont.getItems()) {
					updateComponentIdItem(cont.getId(), comp);
				}
			}
		}
	}
	
	public Date getUltimaDataAutalizacaoDeployment(final Long depId) {
	    DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
	    final Date dt = new Date();
		try {
			dt.setTime(fmt.parse("01/01/1900").getTime());
		} catch (Exception e) {
		}
	    Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
	    session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
			    PreparedStatement pstmt = connection.prepareStatement("select max(dh_alteracao) from historico where projeto_id = (select projeto_id from deployment where id = ?)");
			    pstmt.setLong(1, depId);
			    ResultSet rs = pstmt.executeQuery();			    
			    if(rs.next()) {
			    	if (rs.getTimestamp(1) != null) {
						dt.setTime(rs.getTimestamp(1).getTime());
					}
			    }				
			}
		});
	    return dt;
	}
	
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=30)	
	public Projeto deploy(Projeto projeto) throws ParametroException {
		if (projeto == null) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
		} else if(projeto.getId() == 0) {
			throw new ParametroException(IMensagensErro.IDENTIFICADOR_VALIDO, IMensagensErro.PARAMETRO_ID_INVALIDO);
		}
		Deployment deployment = projeto.getDeployment();
		List<Historico> historicos = projeto.getHistoricos();
		Projeto prj = projeto.clone();
		prj.setDeployment(null);
		prj.setPublicado(true);
		projeto.setHistoricos(new ArrayList<Historico>());
		try {
			long ret = super.persist(prj);
			prj.setId(ret);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		prj.setHistoricos(historicos);
		if (prj.getHistoricos().size() > 0) {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			WorkInsertHistoricoProjeto work = new WorkInsertHistoricoProjeto();
			work.projeto = prj;
			session.doWork(work);
		}		
		if(deployment != null) {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			WorkInsertDeployment work = new WorkInsertDeployment();
			deployment.setProjeto(prj);
			deployment.setDhPublicacao(new Date(System.currentTimeMillis()));
			deployment.dhUltimaAtualizacao = deployment.getDhPublicacao().getTime();
			work.deploy = deployment;
			session.doWork(work);
			
			List<Usuario> usuarios = new ArrayList<Usuario>();
			for(Usuario usr : deployment.getUsuariosPossiveis()) {
				Usuario usrRecup = (Usuario)hibernateTemplate.find("from Usuario usr where upper(usr.login) = ?", usr.getLogin().toUpperCase()).get(0);
				usuarios.add(usrRecup);
			}
			deployment.getUsuariosPossiveis().clear();
			deployment.setUsuariosPossiveis(usuarios);
			
			List<Grupo> grupos = new ArrayList<Grupo>();
			for(Grupo grp : deployment.getGruposPossiveis()) {
				Grupo grpRecup = (Grupo)hibernateTemplate.find("from Grupo grp where grp.nome = ?", grp.getNome()).get(0);
				grupos.add(grpRecup);
			}
			deployment.getGruposPossiveis().clear();
			deployment.setGruposPossiveis(grupos);
		}
		prj.setProjetoBase(new Projeto());
		prj.getProjetoBase().setId(projeto.getId());
		prj.setDeployment(deployment);
		
		for (Pagina pag : prj.getPaginas()) { 
			String json = recuperaJsonPagina(pag);
			pag.setPaginaJson(json.getBytes());
		}
		
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		WorkUpdateDeploymentProject work = new WorkUpdateDeploymentProject();
		work.projeto = prj;
		session.doWork(work);
		
		prj.setProjetoBase(null);

		Cache cache = cacheManager.getCache("deployment");
		cache.put(deployment.getId(), deployment);		
		return prj;
	}
	
	
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=120)	
	public Long update(Projeto projeto) throws ParametroException {
		Long ret = new Long(0);
		if (projeto == null || projeto.getId() <= 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
		}
		
		
		
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		WorkValidacaoProjeto workVal = new WorkValidacaoProjeto();
		workVal.projeto = projeto;
		session.doWork(workVal);
		
		if (!workVal.validacao.valido) {
			throw new ParametroProjetoException(workVal.validacao);
		}
		
		Cache cache = cacheManager.getCache("projeto");
		Cache cacheDep = cacheManager.getCache("deployment");
		
		Projeto prjLocal = null;
		
		if (cache.get(projeto.getId()) == null) {
			prjLocal = super.findById(projeto.getId());
		} else {
			prjLocal = (Projeto)(cache.get(projeto.getId()).get());
		}
		if(prjLocal != null) {
			prjLocal.setDescricao(projeto.getDescricao());
			prjLocal.setNome(projeto.getNome());
			prjLocal.setAplicacao(projeto.getAplicacao());
			for (Pagina pag : prjLocal.getPaginas()) {
				paginaDao.remove(pag);
			}
			prjLocal.getPaginas().clear();
			for(Pagina pg : projeto.getPaginas()) {
				Pagina pgcl = pg.clone();
				pgcl.setId(null);
				prjLocal.getPaginas().add(pgcl);
			}
			hibernateTemplate.update(prjLocal);
			
			ret = prjLocal.getId();
		}
		if (projeto.getHistoricos().size() > 0) {
			session = hibernateTemplate.getSessionFactory().getCurrentSession();
			WorkInsertHistoricoProjeto work = new WorkInsertHistoricoProjeto();
			work.projeto = projeto;
			session.doWork(work);
		}
		
		cache.put(ret, prjLocal);
		
		Ehcache ehcache = (Ehcache) cacheDep.getNativeCache();
		List chaves = ehcache.getKeys();
		for(int i = 0; i < chaves.size(); i++) {
			Object ch = chaves.get(i);
			Deployment dp = (Deployment)(cacheDep.get(ch).get());
			Projeto prjBase = dp.getProjeto().getProjetoBase();
			if(prjBase.getId() == ret) {
				dp.getProjeto().setProjetoBase(prjLocal);
			}
		}
		return ret;
	}
	
	
	@Override
	public Projeto findById(Long id) throws ParametroException {
		if (id == null || id <= 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
		}
		
		Projeto projeto = null;
		Cache cache = cacheManager.getCache("projeto");
		projeto = cache.get(id) == null ? null : (Projeto)(cache.get(id).get());
		
		if (projeto == null) {
			projeto = super.findById(id);
			for (Pagina pagina : projeto.getPaginas()) {
				pagina.getId();
				pagina.getDescricao();
				pagina.getNome();
				List<ImplementacaoEvento> metodos = pagina.getMetodos();
				for (ImplementacaoEvento mtdo : metodos) {
					mtdo.getNome();
					mtdo.getDescricao();
					mtdo.getTipoEvento().getNome();
				}
				if (pagina.getContainer() != null) {
					pagina.getContainer().getId();
					pagina.getContainer().getActiveItem();
				}
			}
			cache.put(id, projeto);
		}
		return projeto;
	}
	
	
	public String deactivatePublishedProject(final Long id, final Boolean flagActivate, final String usuario) throws ParametroException {
		String mensagem = "";
		if (id == null || id <= 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
		}
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		try {
	        WorkDeactivateDeployedProject work = new WorkDeactivateDeployedProject();
	        work.deployedProjetoId = id;
	        work.flagActivate = flagActivate;
	        work.usuario = usuario;
	        session.doWork(work);
	        mensagem = work.msgRetorno;
	        
	        session.doWork(new Work() {
				
				@Override
				public void execute(Connection connection) throws SQLException {
					PreparedStatement pstmt = connection.prepareStatement("select id from deployment where projeto_id = ?");
					pstmt.setLong(1, id);
					ResultSet rs = pstmt.executeQuery();
					if(rs.next()) {
						long deployId = rs.getLong("id");
						Cache cache = cacheManager.getCache("deployment");
						Deployment dep = cache.get(deployId) == null ? null : (Deployment)(cache.get(deployId).get());
						if (dep != null) {
							dep.setAtivo(flagActivate);
							dep.dhUltimaAtualizacao = getUltimaDataAutalizacaoDeployment(dep.getId()).getTime();
							cache.put(id, dep);
						}
					}
				}
			});
        } catch (HibernateException e) {
	        e.printStackTrace();
        }		
		return mensagem;
	}	
	
	@Override
	public List<Projeto> findAllNoLimit() {		
		final List<Projeto> projetos = new ArrayList<Projeto>();
		final List<Long> prjNotFound = new ArrayList<Long>();
		final Cache cache = cacheManager.getCache("projeto");
		
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				ResultSet rs = connection.createStatement().executeQuery("select p.id "
						+ "from projeto p "
						+ "where p.publicado = false and p.deployment_id is null");
				while(rs.next()) {
					long idPrj = rs.getLong("id");
					if(cache.get(idPrj) == null) {
						prjNotFound.add(idPrj);
					} else {
						projetos.add((Projeto)(cache.get(idPrj).get()));
					}
				}
			}
		});
		
		for(Long prjId : prjNotFound) {
			Projeto p = hibernateTemplate.get(Projeto.class, prjId);
			for(Pagina pag : p.getPaginas()) {
				pag.getDescricao();
				pag.getNome();
			}
			projetos.add(p);
			cache.put(prjId, p);
		}		
		
		return projetos;
	}	
	
	public void restoreCacheProjeto() {
		final List<Long> projetos = new ArrayList<Long>();
		final Cache cache = cacheManager.getCache("projeto");
		
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				ResultSet rs = connection.createStatement().executeQuery("select p.id "
						+ "from projeto p "
						+ "where p.publicado = false and p.deployment_id is null");
				while(rs.next()) {
					long idPrj = rs.getLong("id");
					projetos.add(idPrj);
				}
			}
		});
		
		for(Long prjId : projetos) {
			Projeto p = hibernateTemplate.get(Projeto.class, prjId);
			for(Pagina pag : p.getPaginas()) {
				pag.getDescricao();
				pag.getNome();
			}
			cache.put(prjId, p);
		}		
		
		findAllPublished();
	}
	
	public List<Projeto> findAllPublished() {
		final List<Projeto> projetos = new ArrayList<Projeto>();
		final List<Long> prjNotFound = new ArrayList<Long>();
		final Cache cache = cacheManager.getCache("projeto");
		final Cache cacheDep = cacheManager.getCache("deployment");
		
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				ResultSet rs = connection.createStatement().executeQuery("select p.id, d.removido from projeto p "
						+ "left join deployment d on d.projeto_id = p.id "
						+ "where p.publicado = true and d.removido is not null and d.removido = false");
				while(rs.next()) {
					long idPrj = rs.getLong("id");
					if(cache.get(idPrj) == null) {
						prjNotFound.add(idPrj);
					} else {
						projetos.add((Projeto)(cache.get(idPrj).get()));
					}
				}
			}
		});
		
		for(Long idPrj : prjNotFound) {
			Projeto p = hibernateTemplate.get(Projeto.class, idPrj);
			Deployment d = p.getDeployment();
			d.getAtivo();
			d.getDhPublicacao();
			d.idProjeto = idPrj;
			getUltimaDataAtualizacao(d);
			projetos.add(p);
			cache.put(idPrj, p);
			if (d != null) {
				cacheDep.put(d.getId(), d);
			}
		}
		
		return projetos;
	}
	
	
	private void getUltimaDataAtualizacao(final Deployment dep) {
	    DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
	    final Date dt = new Date();
		try {
			dt.setTime(fmt.parse("01/01/1900").getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    
	    
	    Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
	    session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
			    PreparedStatement pstmt = connection.prepareStatement("select max(dh_alteracao) from historico where projeto_id = ?");
			    pstmt.setLong(1, dep.idProjeto);
			    ResultSet rs = pstmt.executeQuery();
			    
			    if(rs.next()) {
			    	Timestamp tm = new Timestamp(System.currentTimeMillis());
			    	if(rs.getTimestamp(1) != null) {
			    		tm = rs.getTimestamp(1);
			    	}
			    	dt.setTime(tm.getTime());
			    }				
			}
		});
	    
	    
	    dep.dhUltimaAtualizacao = dt.getTime();
    }	
	
	public String recuperaJsonPagina(Pagina pagina) {
		String json = null;
		
		SenchaPage page = new SenchaPage();
		page.xtype = pagina.getXtype();
		
		page.fullScreen = true;
		page.itemId = pagina.getNome();
		page.layout = "fit";

		for(Component cmp : pagina.getContainer().getItems()) {
			page.items.add(recuperaJsonItem(cmp));
		}
		
		SenchaImplementacao impl = new SenchaImplementacao();
		SenchaImplementacao.Impl[] ret = null;
		for(ImplementacaoEvento imp : pagina.getMetodos()) {
			String metodo = imp.getTipoEvento().getNome();
			String codImpl = new String(imp.getImplementacao());
			impl.addImplementacao(metodo, codImpl);
		}
		page.listeners = impl;
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(SenchaImplementacao.class, new SenchaImplSerializer());
		gsonBuilder.registerTypeAdapter(SenchaImplementacao.Impl.class, new SenchaImplArraySerializer());
		gsonBuilder.disableHtmlEscaping();
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		json = gson.toJson(page);
		
		return formatarFuncoes(json);
	}
	
	private String formatarFuncoes(String json) {
		String jsonFormatado = "";
		
		jsonFormatado = json.replace("\"function", "function");
		jsonFormatado = jsonFormatado.replace("listenersarray", "listeners");
		jsonFormatado = jsonFormatado.replace("}\"", "}");
		jsonFormatado = jsonFormatado.replace("\"{", "{");
		jsonFormatado = jsonFormatado.replace("]\"", "]");
		jsonFormatado = jsonFormatado.replace(")\"", ")");
		jsonFormatado = jsonFormatado.replace("\\n", "\n");
		jsonFormatado = jsonFormatado.replace("\\t", "\t");
		jsonFormatado = jsonFormatado.replace("\"store\": \"{", "\"store\": {");
		jsonFormatado = jsonFormatado.replace("\\\"", "\"");
		jsonFormatado = jsonFormatado.replace("\\\\", "\\");
		jsonFormatado = jsonFormatado.replace("\"store\": \"", "\"store\": "); 
		jsonFormatado = jsonFormatado.replace("\"series\": \"[", "\"series\": [");
		jsonFormatado = jsonFormatado.replace("\"colors\": \"[", "\"colors\": [");
		jsonFormatado = jsonFormatado.replace("\"axes\": \"[", "\"axes\": [");
		jsonFormatado = jsonFormatado.replace("\"legend\": \"{", "\"legend\": {");
		jsonFormatado = jsonFormatado.replace("\"interactions\": \"[", "\"interactions\": [");
		jsonFormatado = jsonFormatado.replace("\"implementacaoCustomizada\": \"", "");
		
		jsonFormatado = jsonFormatado.replace("}\n\"", "}");
		jsonFormatado = jsonFormatado.replace(")\n\"", ")");
		jsonFormatado = jsonFormatado.replace("]\n\"", "]");
		return jsonFormatado;
	}
	
	private SenchaImplementacao getImplementacao(List<ImplementacaoEvento> eventos) {
		SenchaImplementacao impl = new SenchaImplementacao();
		for (ImplementacaoEvento evento : eventos) {
			String implevento = new String(evento.getImplementacao());
	        impl.addImplementacao(evento.getTipoEvento().getNome(), implevento);
        }
		return impl;
	}
	
	private void preencheValoresField(SenchaItem item, Field field) {
		item.label = field.getLabel().isEmpty() ? null : field.getLabel();
		item.labelAlign = field.getLabelAlign().isEmpty() ? null : field.getLabelAlign();
		item.name = field.getName().isEmpty() ? null : field.getName();
		item.value = field.getValue().isEmpty() ? null : field.getName();
		item.labelWrap = field.getLabelWrap() ? true : null;
		item.required = field.getRequired() ? true : null;		
		item.labelWidth = field.getLabelWidth() != null && field.getLabelWidth() > 0 ? field.getLabelWidth() + "%" : null;
	}
	
	private void preencheValoresText(SenchaItem item, Text text) {
		preencheValoresField(item, text);
		item.placeHolder = text.getPlaceHolder().isEmpty() ? null : text.getPlaceHolder();
		item.maxLength = text.getMaxLength() == 0 ? null : text.getMaxLength();
		item.autoCapitalize = text.getAutoCapitalize() ? true : null;
		item.autoComplete = text.getAutoComplete() ? true : null;
		item.autoCorrect = text.getAutoCorrect() ? true : null;
		item.readOnly = text.getReadOnly() ? true : null;			
	}
	
	private void preencheValoresContainer(SenchaItem item, Container cont) {
		item.itemTpl = (cont.getItemTpl().isEmpty() ? null : cont.getItemTpl()) ;
		if (StringUtils.isNotEmpty(item.itemTpl)) {
			item.itemTpl = item.itemTpl.replace("\n", " ");
			item.itemTpl = item.itemTpl.replace("\\n", " ");
		}
		item.store = cont.getStore().isEmpty() ? null : cont.getStore();
		item.modal = cont.getModal() ? true : null;
		item.vscrollable = cont.getVscrollable() ? true : null;
		item.hscrollable = cont.getHscrollable() ? true : null;
		if (cont.getDocked() != null && !cont.getDocked().equalsIgnoreCase("center")) {
	        item.docked = cont.getDocked();
        } else {
        	item.docked = null;
        }
		
		item.maxHeight = cont.getMaxHeight() != null && cont.getMaxHeight() > 0 ? cont.getMaxHeight() + "px": null;
		item.maxWidth = cont.getMaxWidth() != null && cont.getMaxWidth() > 0 ? cont.getMaxWidth() + "px": null;

	}
	
	private void preencheValoresComponent(SenchaItem item, Component comp) {
		if (!StringUtils.isEmpty(comp.getDocked()) && !comp.getDocked().equalsIgnoreCase("center")) {
	        item.docked = comp.getDocked();
        } else {
        	item.docked = null;
        }
	}
	
	private ArrayList<SenchaItem> getItems(Container cmp) {
		ArrayList<SenchaItem> items = new ArrayList<SenchaItem>();
		for(Component cmpi : cmp.getItems()) {
			items.add(recuperaJsonItem(cmpi));
		}
		return (items.size() > 0 ? items : null);
	}
	
	private SenchaItem recuperaJsonItem(Component cmp) {
		SenchaItem json = new SenchaItem();
		json.xtype = cmp.getXType();
		json.itemId = cmp.getFieldId();
		json.cls = (cmp.getCls() != null && cmp.getCls().trim().length() > 0 ? cmp.getCls() : null);
		if (cmp instanceof FieldSet) {
			FieldSet fs = (FieldSet)cmp;
			preencheValoresContainer(json, fs);
			json.instructions = fs.getInstructions();
			json.title = fs.getTitle();
			json.listeners = getImplementacao(fs.getImplementacoes());
			json.items = getItems(fs);
		} else if(cmp instanceof ToolBar){
			ToolBar tb = (ToolBar)cmp;
			preencheValoresContainer(json, tb);
			json.title = tb.getTitle();
			json.listeners = getImplementacao(tb.getImplementacoes());
			json.items = getItems(tb);
		} else if(cmp instanceof TitleBar){
			TitleBar tb = (TitleBar)cmp;
			preencheValoresContainer(json, tb);
			json.title = tb.getTitle();
			json.listeners = getImplementacao(tb.getImplementacoes());
			json.items = getItems(tb);
		} else if(cmp instanceof FormPanel){
			FormPanel tb = (FormPanel)cmp;
			preencheValoresContainer(json, tb);
			json.method = tb.getMethod();
			json.record = tb.getRecord();
			json.maxHeight = tb.getMaxHeight() != null && tb.getMaxHeight() > 0 ? tb.getMaxHeight() + "px": null;
			json.maxWidth = tb.getMaxWidth() != null && tb.getMaxWidth() > 0 ? tb.getMaxWidth() + "px": null;
			json.url = tb.getUrl();
			json.scrollable = tb.getScrollable();
			json.submitOnAction = tb.getSubmitOnAction();
			json.listeners = getImplementacao(tb.getImplementacoes());
			json.items = getItems(tb);
		} else if(cmp instanceof Sheet){
			Sheet tb = (Sheet)cmp;
			preencheValoresContainer(json, tb);
			json.enter = tb.getEnter();
			json.exit = tb.getExit();
			json.hideAnimation = tb.getHideAnimation();
			json.showAnimation = tb.getShowAnimation();
			json.listeners = getImplementacao(tb.getImplementacoes());
			json.items = getItems(tb);
		} else if(cmp instanceof Panel){
			Panel tb = (Panel)cmp;
			preencheValoresContainer(json, tb);
			json.listeners = getImplementacao(tb.getImplementacoes());
			json.items = getItems(tb);
		} else if(cmp instanceof DataView){
			DataView tb = (DataView)cmp;
			preencheValoresContainer(json, tb);
			json.emptyText = tb.getEmptyText();
			json.loadingText = tb.getDataViewLoadingText();
			json.mode = tb.getDtViewMode();
			json.scrollable = tb.getDataViewScrollable().isEmpty() ? false : true;
			json.store = (tb.getDataViewStore() != null ? (new String(tb.getDataViewStore())).trim() : null);
			json.triggerEvent = tb.getTriggerEvent();
			json.maxItemCache = tb.getMaxItemCache();
			json.disableSelection = tb.getDisableSelection();
			json.selectedCls = tb.getDataViewSelectedCls();
			json.pressedCls = tb.getDataViewPressedCls();
			if(tb.getUseComponents()) {
				json.useComponents = true;
				json.defaultType = tb.getDefaultType();
			} else {
				json.useComponents = false;
				json.defaultType = null;
			}
//			json.implementacaoCustomizada = (tb.getPacoteCodigoCustomizado() != null ? new String(tb.getPacoteCodigoCustomizado()) : null);
			json.listeners = getImplementacao(tb.getImplementacoes());
			json.items = getItems(tb);
		} else if(cmp instanceof Carousel){
			Carousel tb = (Carousel)cmp;
			preencheValoresContainer(json, tb);
			json.direction = tb.getDirection();
			json.indicator = tb.getIndicator();
			json.listeners = getImplementacao(tb.getImplementacoes());
			for(Tab tab : tb.getPaginas()){
				SenchaItem containerTab = new SenchaItem();
				containerTab.xtype = "container";
				containerTab.layout = "fit";
				containerTab.fullscreen = true;
				json.items.add(containerTab);
				for(Component item : tab.getItems()) {
					containerTab.items.add(recuperaJsonItem(item));
				}
			}
		} else if(cmp instanceof TabPanel){
			TabPanel tb = (TabPanel)cmp;
			preencheValoresContainer(json, tb);
			json.tabBarPosition = tb.getTabBarPosition();
			json.listeners = getImplementacao(tb.getImplementacoes());
			for(Tab tab : tb.getTabs()){
				SenchaItem itJson = recuperaJsonItem(tab.getItems().get(0));
				itJson.iconCls = tab.getImagem();
				itJson.title = tab.getText();
				json.items.add(itJson);
			}
		} else if (cmp instanceof Chart) {
			Chart chart = (Chart)cmp;
			preencheValoresContainer(json, chart);
			json.store = StringUtils.isEmpty(chart.getChartStore()) ? null : chart.getChartStore();
			json.axes = StringUtils.isEmpty(chart.getAxes()) ? null : chart.getAxes();
			json.series = StringUtils.isEmpty(chart.getSeries()) ? null : chart.getSeries();
			json.legend = StringUtils.isEmpty(chart.getLegend()) ? null : chart.getLegend();
			json.interactions = StringUtils.isEmpty(chart.getInteractions()) ? null : chart.getInteractions();
			json.listeners = getImplementacao(chart.getImplementacoes());
			json.theme = StringUtils.isEmpty(chart.getTheme()) ? null : chart.getTheme();
			json.shadow = chart.getShadow() == null ? null : chart.getShadow();
			json.flipXY = chart.getFlipXY() == null ? false : chart.getFlipXY();
			json.animate = chart.getAnimate() != null && chart.getAnimate() ? true : false;
			json.insetPadding = chart.getInsetPadding() != null && chart.getInsetPadding() > 0 ? chart.getInsetPadding() : null;
			json.xtype = chart.getPolarChart() ? "polar" : json.xtype;
			json.colors = StringUtils.isEmpty(chart.getColors()) ? null : chart.getColors();
			json.scrollable = chart.getChartScrollable() != null && chart.getChartScrollable() ? true : null;
		} else if(cmp instanceof Container){
			Container tb = (Container)cmp;
			preencheValoresContainer(json, tb);
			json.listeners = getImplementacao(tb.getImplementacoes());
			json.items = getItems(tb);
		}
		
		json = montaJsonComponent(cmp, json);
		return json;
	}

	private SenchaItem montaJsonComponent(Component cmp, SenchaItem json) {
	    if (cmp instanceof Button) {
			Button btn = (Button)cmp;
			if (cmp.getDocked() != null && !cmp.getDocked().endsWith("center")) {
		        json.align = cmp.getDocked();
		        json.docked = cmp.getDocked();
	        } else {
	        	json.align = null;
	        	json.docked = null;
	        }			
//			preencheValoresComponent(json, cmp);
			json.badgeText = btn.getBadgeText();
			json.iconCls = btn.getIconCls();
			json.ui = btn.getUi();
			json.text = btn.getButtonText();
			
			json.listeners = getImplementacao(btn.getImplementacoes());
		} else if(cmp instanceof CheckBox) {
			CheckBox ckb = (CheckBox)cmp;
			preencheValoresComponent(json, cmp);
			preencheValoresField(json, ckb);
			json.checked = ckb.getChecked();
			
			json.listeners = getImplementacao(ckb.getImplementacoes());
		} else if(cmp instanceof Slider || 
				cmp instanceof Toggle) {
			Slider sli = (Slider)cmp;
			preencheValoresComponent(json, cmp);
			preencheValoresField(json, sli);
			json.increment = sli.getIncrement();
			
			json.listeners = getImplementacao(sli.getImplementacoes());
		}  else if(cmp instanceof TextArea) {
			TextArea txtArea = (TextArea)cmp;
			preencheValoresComponent(json, cmp);
			preencheValoresText(json, txtArea);
			json.maxRows = txtArea.getMaxRows();
			
			json.listeners = getImplementacao(txtArea.getImplementacoes());
		} else if(cmp instanceof Spinner) {
			Spinner spinner = (Spinner)cmp;
			preencheValoresText(json, spinner);
			preencheValoresComponent(json, cmp);
			json.maxValue = spinner.getMaxValue();
			json.stepValue = spinner.getStepValue();
			json.accelerateOnTapHold = spinner.getAccelerateOnTapHold();
			json.cycle = spinner.getCycle();
			json.defaultValue = spinner.getDefaultValue();
			
			json.listeners = getImplementacao(spinner.getImplementacoes());
		} else if (cmp instanceof GPSField){
			GPSField gps = (GPSField)cmp;
			preencheValoresText(json, gps);
			preencheValoresComponent(json, gps);
			json.xtype = "hiddenfield";
		} else if(cmp instanceof Hidden || 
				cmp instanceof Password ||
				cmp instanceof Email ) {
			Text item = (Text)cmp;
			preencheValoresText(json, item);	
			preencheValoresComponent(json, cmp);
			
			json.listeners = getImplementacao(item.getImplementacoes());
		} else if (cmp instanceof GPSField){
			GPSField gps = (GPSField)cmp;
			preencheValoresText(json, gps);
			preencheValoresComponent(json, gps);
			json.xtype = "hiddenfield";
		} else if(cmp instanceof DatePicker) {
			DatePicker dp = (DatePicker)cmp;
			preencheValoresText(json, dp);
			preencheValoresComponent(json, cmp);
			json.yearFrom = dp.getYearFrom();
			json.yearTo = dp.getYearTo();
			
			SenchaPicker picker = new SenchaPicker();
			picker.cancelButton = "Cancela";
			picker.doneButton = "Confirma";
			picker.addSlotOrder("day").addSlotOrder("month").addSlotOrder("year");
			json.picker = dp.getPicker();
			json.listeners = getImplementacao(dp.getImplementacoes());
		} else if (cmp instanceof Camera) {
			Camera cam = (Camera)cmp;
			preencheValoresComponent(json, cam);
			json.xtype = "toolbar";
			json.docked = "bottom";
			json.margin = "5 10 5 10";
			json.cls = "jazzit-camera-toolbar";
			
			SenchaItem label = new SenchaItem();			
			label.xtype = "container";
			label.html = "0 de " + cam.getQuantity();
			label.itemId = json.itemId + "_label";
			label.margin = "5 0 0 20";
			label.style = "font-family: daxlinepro; font-size: 20px; color: #330066";
			label.html = "<div><div style=\'font-family: daxlineprobold; font-size: 16px; color: #666666; text-align: center\'>Quantidade de fotos</div>"
					+ "<div style=\' font-family: daxlinepro; font-size: 20px; color: #330066; text-align:center\'>0 de " + cam.getQuantity()  + "</div></div>";
			//leftLabel.cls = cam.getCls();
			SenchaImplementacao impl = new SenchaImplementacao();
			impl.addImplementacao("element", "tap", "fn", "function(){JazzIT.util.DeviceUtil.manterFotosSalvas('"+ json.itemId  +"');}");
			label.listenersarray = impl.implementacoes;
			
			SenchaItem leftPicture = new SenchaItem();
			leftPicture.xtype = "container";
//			rightButton.iconCls = "jazz-camera";
			leftPicture.docked = "left";
			leftPicture.align = "left";
			leftPicture.margin = "10 10 0 10";
			leftPicture.html = "<div><img widht='30' height='30' style='padding: 10 15 0 0' src='./resources/icons/camera.png' alt='Retirar foto'/></div>";
			impl = new SenchaImplementacao();
			impl.addImplementacao("element", "tap", "fn", "function(){JazzIT.util.DeviceUtil.capture('"+ json.itemId  +"');}");
			leftPicture.listenersarray = impl.implementacoes;
			
			SenchaItem rightPicture = new SenchaItem();
			rightPicture.xtype = "container";
//			rightButton.iconCls = "jazz-camera";
			rightPicture.docked = "right";
			rightPicture.align = "right";
			rightPicture.margin = "20 10 0 10";
			rightPicture.html = "<div><img widht='20' height='20' style='padding: 20 10 0 10' src='./resources/icons/right.png' alt='visualizar fotos'/></div>";
			impl = new SenchaImplementacao();
			impl.addImplementacao("element", "tap", "fn", "function(){JazzIT.util.DeviceUtil.manterFotosSalvas('"+ json.itemId  +"');}");
			rightPicture.listenersarray = impl.implementacoes;			
			
			json.items.add(label);
			json.items.add(leftPicture);
			json.items.add(rightPicture);
			
		} else if(cmp instanceof Select) {
			Select sel = (Select)cmp;
			preencheValoresText(json, sel);
			preencheValoresComponent(json, sel);
			json.displayField = sel.getDisplayField();
			json.hiddenName = sel.getHiddenName();
			json.defaultPhonePickerConfig = new SenchaPicker("Confirma", "Cancela");
			json.defaultTabletPickerConfig = new SenchaPicker("Confirma", "Cancela");
			json.store = (StringUtils.isEmpty(sel.getStore()) ? null : sel.getStore());
			json.options = new ArrayList<SenchaOption>();
			for(Option opt : sel.getOptions()) {
				json.options.add(new SenchaOption(opt.getText(), opt.getValue()));
			}
			json.listeners = getImplementacao(sel.getImplementacoes());
		} else if(cmp instanceof br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Number) {
			br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Number nmb = (br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Number)cmp;
			preencheValoresText(json, nmb);
			json.maxValue = (nmb.getMaxValue() == null || nmb.getMaxValue() == 0 ? null : nmb.getMaxValue());
			json.stepValue = (nmb.getStepValue() == null || nmb.getStepValue() == 0 ? null : nmb.getStepValue());
			if(!StringUtils.isEmpty(nmb.getDocked()) && !nmb.getDocked().equalsIgnoreCase("center")) {
				json.docked = nmb.getDocked();
				json.align = nmb.getDocked();
				json.labelAlign = nmb.getLabelAlign();
			}
			
			json.listeners = getImplementacao(nmb.getImplementacoes());
		} else if(cmp instanceof Text) {
			Text txt = (Text)cmp;
			preencheValoresText(json, txt);
			if(!StringUtils.isEmpty(txt.getDocked()) && !txt.getDocked().equalsIgnoreCase("center")) {
				json.docked = txt.getDocked();
				json.align = txt.getDocked();
				json.labelAlign = txt.getLabelAlign();
			}
			if(txt.getTextArea()) {
				json.xtype = "textareafield";
				json.maxRows = txt.getMaxRows();
			}
			if(txt.getEmailField()) {
				json.xtype = "emailfield";
			}
			json.listeners = getImplementacao(txt.getImplementacoes());
		}
	    return json;
    }
	
	
	
	public class ValidaPreenchimentoProjeto {
		public boolean valido = false;
		public List<CampoValidado> naoValidos = new ArrayList<CampoValidado>();
	}

	public class CampoValidado {
		public String nomeCampo;
		public String mensagem;
	}	
}


