package br.com.laminarsoft.jazzforms.persistencia.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.laminarsoft.jazzforms.logging.types.MessageData;
import br.com.laminarsoft.jazzforms.persistencia.model.Deployment;
import br.com.laminarsoft.jazzforms.persistencia.model.Grupo;
import br.com.laminarsoft.jazzforms.persistencia.model.Historico;
import br.com.laminarsoft.jazzforms.persistencia.model.Mensagem;
import br.com.laminarsoft.jazzforms.persistencia.model.Projeto;
import br.com.laminarsoft.jazzforms.persistencia.model.Usuario;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.IMensagensErro;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.ParametroException;
import br.com.laminarsoft.jazzforms.persistencia.model.util.InfoRetornoMensagem;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.AnexoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.DeploymentVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.MensagemVO;

@Transactional(propagation=Propagation.SUPPORTS, 
	isolation=Isolation.READ_COMMITTED, 
	timeout=30)
@Repository("MensagemDao")
@SuppressWarnings("all")
public class MensagemDao extends BaseDao<Mensagem> {

	@Autowired private ProjetoDao projetoDao;
	@Autowired private UsuarioDao usuarioDao;
	@Autowired private AlertaDao alertaDao;
	
	public static final long serialVersionUID = 10l;
	
	private class WorkMensagensPorUsuario implements Work {
		String loginUsuario;
		Long dataUltimaMensagem;
		String serialEquipamento;
		
		List<MensagemVO> mensagens = new ArrayList<MensagemVO>();
		List<MensagemVO> mensagensRemovidas = new ArrayList<MensagemVO>();
		
		
		@Override
        public void execute(Connection connection) throws SQLException {
			String slctMsgsUsuario = "select m.id, m.conteudo, m.titulo, m.data, m.deployment_id, m.bp_instance_id " 
+" from mensagem m "
+" left join agrupamento_mensagem am on am.id = m.AGRUPAMENTO_MENSAGEM_ID "
+" where m.usuario_mensagem_id = (select id from usuario where upper(login) = '"+loginUsuario.toUpperCase()+"') "
+" and (am.RESOLVIDO = false or am.RESOLVIDO is null) "
					+ "and unix_timestamp(data) > " + dataUltimaMensagem;
			ResultSet rsUsuario = connection.createStatement().executeQuery(slctMsgsUsuario);
			montaMensagem(rsUsuario, connection);
			String slctMsgsGrupo = "select m.id, m.conteudo, m.titulo, m.data, m.deployment_id, m.bp_instance_id "
+"from mensagem m " 
+"left join agrupamento_mensagem am on am.id = m.AGRUPAMENTO_MENSAGEM_ID "
+"where m.grupo_id in (select grupoid from usuario_grupo where usuarioid = (select id from usuario where upper(login) = '"+loginUsuario.toUpperCase()+"')) "
+"and (am.RESOLVIDO = false or am.RESOLVIDO is null) "
							+ "and unix_timestamp(data) > " + dataUltimaMensagem;
			rsUsuario = connection.createStatement().executeQuery(slctMsgsGrupo);
			montaMensagem(rsUsuario, connection);
			if (serialEquipamento != null) {
				String slctMsgsEquipamento = "select id, conteudo, titulo, data, deployment_id, bp_instance_id from mensagem "
						+" where equipamento_id = (select id from equipamento where serial = '" + serialEquipamento + "') and unix_timestamp(data) > " + dataUltimaMensagem;
				rsUsuario = connection.createStatement().executeQuery(slctMsgsEquipamento);
				montaMensagem(rsUsuario, connection);
				
				String slctMsgsGrupoEquipamento = "select id, conteudo, titulo, data, deployment_id, bp_instance_id from mensagem " 
						+" where grupo_equipamento_id in (select grupo_equipamentoid from grp_equipamento_equipamento where equipamentoid = (select id from equipamento where serial = '" + serialEquipamento + "')) "
								+ "and unix_timestamp(data) > " + dataUltimaMensagem;

				rsUsuario = connection.createStatement().executeQuery(slctMsgsGrupoEquipamento);
				montaMensagem(rsUsuario, connection);
			}
			
			mensagensRemovidas.clear();
			PreparedStatement pstmtMsgRemovida = connection.prepareStatement("select mensagem_id, usuario_id, mensagem_removida from mensagem_usuario "
					+ "where mensagem_id = ? and usuario_id = (select id from usuario where upper(login) = ?) and mensagem_removida = 1");
			for(MensagemVO vo : mensagens) {
				pstmtMsgRemovida.clearParameters();
				pstmtMsgRemovida.setLong(1, vo.getId());
				pstmtMsgRemovida.setString(2, loginUsuario.toUpperCase());
				ResultSet rsMsgRemovida = pstmtMsgRemovida.executeQuery();
				if(rsMsgRemovida.next()) {
					mensagensRemovidas.add(vo);
				}
			}
			mensagens.removeAll(mensagensRemovidas);
			mensagensRemovidas.clear();
        }


		private void montaMensagem(ResultSet rsUsuario, Connection connection) throws SQLException {
			PreparedStatement pstmt = connection.prepareStatement("select id, dh_inclusao, arq_anexo, nome_arquivo, url_site, type from anexo  where mensagem_id = ?");
			PreparedStatement pstmtDeployment = connection.prepareStatement("select id, dh_publicacao, ativo, removido from deployment where id = ?");
			PreparedStatement pstmtBPInstance = connection.prepareStatement("select id from bp_instance where deployment_id = ?");
			
	        while(rsUsuario.next()) {
	        	pstmt.clearParameters();
	        	pstmtDeployment.clearParameters();
	        	
	        	MensagemVO vo = new MensagemVO();
	        	vo.setId(rsUsuario.getLong("id")); 
	        	vo.setConteudo(rsUsuario.getString("conteudo"));
	        	vo.setTitulo(rsUsuario.getString("titulo"));
	        	vo.setData(rsUsuario.getTimestamp("data"));
	        	pstmt.setLong(1, vo.getId());
	        	
	        	ResultSet rsAnexos = pstmt.executeQuery();
	        	while(rsAnexos.next()) {
	        		AnexoVO anexo = new AnexoVO();
	        		anexo.id = rsAnexos.getLong("id");
	        		anexo.nomeArquivo = rsAnexos.getString("nome_arquivo");
	        		
	        		byte[] arquivo = rsAnexos.getBytes("arq_anexo");
	        		if(arquivo != null && arquivo.length > 0) {
		        		anexo.contemArquivo = 1;
	        		} else {
	        			anexo.contemArquivo = 0;
	        		}
	        		
					anexo.urlSite = rsAnexos.getString("url_site");
	        		anexo.type = rsAnexos.getString("type");
	        		vo.getAnexos().add(anexo);
	        	}
	        	rsAnexos.close();
	        	
	        	Long dpId = rsUsuario.getLong("deployment_id");
	        	if(dpId != null && dpId > 0) {
	        		pstmtDeployment.setLong(1, dpId);
	        		ResultSet rsDeployment = pstmtDeployment.executeQuery();
	        		if(rsDeployment.next()) {
		        		DeploymentVO depVo = new DeploymentVO();
		        		depVo.id = rsDeployment.getLong("id");
		        		depVo.dhPublicacao = rsDeployment.getTimestamp("dh_publicacao") != null ? rsDeployment.getTimestamp("dh_publicacao").getTime() : null;
		        		depVo.ativo = rsDeployment.getBoolean("ativo");
		        		depVo.removido = rsDeployment.getBoolean("removido");
		        		
		        		vo.deployment = depVo;

		        		vo.bpInstanceId = rsUsuario.getLong("bp_instance_id");
	        		}
	        	}
	        	
	        	mensagens.add(vo);
	        }
	        rsUsuario.close();
        }
	}
	
	public AnexoVO getAnexoPorMensagemUsuario(final Long mensagemId, final String loginUsuario) {
		final AnexoVO vo = new AnexoVO();
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				PreparedStatement pstmt = connection.prepareStatement("select id, arq_anexo, dh_inclusao, nome_arquivo, type from anexo where mensagem_id = ?");
				pstmt.setLong(1, mensagemId);
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()) {
					vo.arqAnexo = rs.getBytes("arq_anexo");
					 
					vo.dhInclusao = rs.getTimestamp("dh_inclusao");
					vo.nomeArquivo = rs.getString("nome_arquivo");
					vo.type = rs.getString("type");
					vo.id = rs.getLong("id");
				}
			}
		});
		return vo;
	}
	
	public List<MensagemVO> getMensagensPorUsuario(String login, String equipamentoUUID, Long dataUltimaMensagem) {
		if (login == null || login.trim().length() == 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
		}
		WorkMensagensPorUsuario work = new WorkMensagensPorUsuario();
		work.loginUsuario = login;
		work.dataUltimaMensagem = dataUltimaMensagem;
		work.serialEquipamento = equipamentoUUID;
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(work);
		return work.mensagens;
	}
	
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=50)
	public void novaMensagemUsuario(final MensagemVO mensagem) {
		if (mensagem == null || mensagem.getDestinatarioUID() == null || mensagem.getRemetenteUID() == null ||
				mensagem.getDestinatarioUID().trim().length() == 0 || mensagem.getRemetenteUID().trim().length() == 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
		}
		
		
		final List<Long> prjidNovosDeployments = new ArrayList<Long>();
		final List<Long> idMensagemCriada = new ArrayList<Long>();
		
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work(){

			@Override
            public void execute(Connection connection) throws SQLException {
				Long idFDeployment = null;
				if(mensagem.idProjeto != null && mensagem.idProjeto > 0) {
					ResultSet rsPrj = connection.createStatement().executeQuery("select id, aplicacao from projeto where id = " + mensagem.idProjeto);
					if(rsPrj.next()) {
						if(!rsPrj.getBoolean("aplicacao")) {
							throw new ParametroException("O projeto não é do tipo aplicação", IMensagensErro.PARAMETRO_ID_INVALIDO);
						}
					}
					
					PreparedStatement pstmtDep = connection.prepareStatement("select d.id, d.ativo, d.removido, d.dep_mensagem from deployment d where d.projeto_id in (select id from projeto where projetoid = ? and  publicado = 1)");
					pstmtDep.setLong(1, mensagem.idProjeto);
					ResultSet rsDep = pstmtDep.executeQuery();
					boolean depFound = false;
					while (rsDep.next()) {
						boolean depMensagem = rsDep.getBoolean("dep_mensagem");	
						boolean ativo = rsDep.getBoolean("ativo");
						boolean removido = rsDep.getBoolean("removido");
						if (!ativo) {
							throw new ParametroException("Existe um deployment não ativo para esse projeto", IMensagensErro.PARAMETRO_ID_INVALIDO);
						} else if(removido) {
							throw new ParametroException("Existe um deployment removido lógicamente para o projeto", IMensagensErro.PARAMETRO_ID_INVALIDO);
						} else if(!depMensagem) {
							throw new ParametroException("Existe um deployment ativo para o projeto não destinado a mensagem", IMensagensErro.PARAMETRO_ID_INVALIDO);
						}
						depFound = true;
						idFDeployment = rsDep.getLong("id");
					} 
					if(!depFound) {
						prjidNovosDeployments.add(new Long(mensagem.idProjeto));
					} 
				}
				
				String insrtCommand = "insert into mensagem (usuario_remetente_id, usuario_mensagem_id, conteudo, titulo, data, idx, deployment_id) values ("
						+ "(select id from usuario where upper(uid) = ?),"
						+ "(select id from usuario where upper(uid) = ?),?,?,?, "
						+ "(select case when max(m.idx) is null then 1 else max(m.idx) + 1 end from mensagem m where m.usuario_mensagem_id = (select id from usuario where upper(uid) = ?)), ?)";
				String insrtAnexoCmd = "insert into anexo (dh_inclusao, arq_anexo, nome_arquivo, url_site, mensagem_id, idx, type) values (?, ?, ?, ?, ?, ?, ?)";
				
				PreparedStatement pstmt = connection.prepareStatement(insrtCommand, Statement.RETURN_GENERATED_KEYS);
				pstmt.setString(1, mensagem.getRemetenteUID().toUpperCase());
				pstmt.setString(2, mensagem.getDestinatarioUID().toUpperCase());
				pstmt.setString(3, mensagem.getConteudo());
				pstmt.setString(4, mensagem.getTitulo());
				pstmt.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));
				pstmt.setString(6, mensagem.getDestinatarioUID().toUpperCase());
				if (idFDeployment == null) {
					pstmt.setNull(7, java.sql.Types.INTEGER);
				} else {
					pstmt.setLong(7, idFDeployment);
				}
				pstmt.executeUpdate();
				
				Long mensagemId = null;
				ResultSet keys = pstmt.getGeneratedKeys();
				if(keys.next()) {
					mensagemId = keys.getLong(1);
					idMensagemCriada.add(mensagemId);
				}
				pstmt.close();
				
				pstmt = connection.prepareStatement(insrtAnexoCmd);
				
				for(AnexoVO anexo : mensagem.getAnexos()) {
					pstmt.clearParameters();
					pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));

					pstmt.setBytes(2, anexo.arqAnexo);
					
					
					pstmt.setString(3, anexo.nomeArquivo);
					pstmt.setString(4, anexo.urlSite);
					pstmt.setLong(5, mensagemId);
					pstmt.setLong(6, 1);
					pstmt.setString(7, anexo.type);
					pstmt.executeUpdate();
				}
				alertaDao.enviaAlertaUsuario(mensagem.getDestinatarioUID(), "Novo alerta", mensagem.titulo, MessageData.NOVA_MENSAGEM);
				
            }			
		});
		
		Usuario usrRemetente = null;
		List usrs = hibernateTemplate.find("from Usuario where upper(uid) = ?", mensagem.getRemetenteUID().toUpperCase());
		if(usrs.size() > 0) {
			usrRemetente = (Usuario)usrs.get(0);
		}
		
		if(prjidNovosDeployments.size() > 0) {
			final Projeto prj = hibernateTemplate.get(Projeto.class, prjidNovosDeployments.get(0));
			
			Deployment dep = new Deployment();
			dep.setProjeto(prj);
			prj.setDeployment(dep);
			dep.setAtivo(true);
			dep.setDhPublicacao(new java.util.Date());
			dep.setProjetoMensagem(true);
			dep.setRemovido(false);

			Historico hist = new Historico();
			hist.setDescricao("Projeto publicado para mensagem");
			hist.setDhAlteracao(new java.util.Date());
			if (usrRemetente != null) {
				hist.setUsuario(usrRemetente);
				prj.getHistoricos().add(hist);
			}
			
			Projeto implantado = projetoDao.deploy(prj);
			final Long idProjetoImplantado = new Long(implantado.getDeployment().getId());
			session.doWork(new Work() {
				
				@Override
				public void execute(Connection connection) throws SQLException {
					String updtMensagem = "update mensagem set deployment_id = ? where id = ?";
					
					PreparedStatement pstmt = connection.prepareStatement(updtMensagem);
					pstmt.setLong(1, idProjetoImplantado);
					pstmt.setLong(2, idMensagemCriada.get(0));
					pstmt.executeUpdate();
				}
			});
		}
	}
	
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=30)
	public void novaMensagemGrupo(final MensagemVO mensagem) {
		if (mensagem == null || mensagem.getGrupos().size() == 0 || mensagem.getRemetenteUID() == null ||
				mensagem.getRemetenteUID().trim().length() == 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
		}
		final List<Long> prjidNovosDeployments = new ArrayList<Long>();
		final List<Long> idMensagemCriada = new ArrayList<Long>();
		
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work(){

			@Override
            public void execute(Connection connection) throws SQLException {
				Long idFDeployment = null;
				if(mensagem.idProjeto != null && mensagem.idProjeto > 0) {
					ResultSet rsPrj = connection.createStatement().executeQuery("select id, aplicacao from projeto where id = " + mensagem.idProjeto);
					if(rsPrj.next()) {
						if(!rsPrj.getBoolean("aplicacao")) {
							throw new ParametroException("O projeto não é do tipo aplicação", IMensagensErro.PARAMETRO_ID_INVALIDO);
						}
					}
					
					PreparedStatement pstmtDep = connection.prepareStatement("select d.id, d.ativo, d.removido, d.dep_mensagem "
							+ "from deployment d where d.projeto_id in (select id from projeto where projetoid = ? and  publicado = 1)");
					pstmtDep.setLong(1, mensagem.idProjeto);
					ResultSet rsDep = pstmtDep.executeQuery();
					boolean depFound = false;
					while (rsDep.next()) {
						boolean depMensagem = rsDep.getBoolean("dep_mensagem");	
						boolean ativo = rsDep.getBoolean("ativo");
						boolean removido = rsDep.getBoolean("removido");
						if (!ativo) {
							throw new ParametroException("Existe um deployment não ativo para esse projeto", IMensagensErro.PARAMETRO_ID_INVALIDO);
						} else if(removido) {
							throw new ParametroException("Existe um deployment removido lógicamente para o projeto", IMensagensErro.PARAMETRO_ID_INVALIDO);
						} else if(!depMensagem) {
							throw new ParametroException("Existe um deployment ativo para o projeto não destinado a mensagem", IMensagensErro.PARAMETRO_ID_INVALIDO);
						}
						depFound = true;
						idFDeployment = rsDep.getLong("id");
					} 
					if(!depFound) {
						prjidNovosDeployments.add(new Long(mensagem.idProjeto));
					} 
				}
				
				String insrtCommand = "insert into mensagem (usuario_remetente_id, grupo_id, conteudo, titulo, data, idx, deployment_id) values ("
						+ "(select id from usuario where upper(uid) = ?),"
						+ "(select id from grupo where nome = ?),?,?,?, "
						+ "(select case when max(m.idx) is null then 1 else max(m.idx) + 1 end from mensagem m where m.grupo_id = (select id from grupo where nome = ?)), ?)";
				
				String insrtAnexoCmd = "insert into anexo (dh_inclusao, arq_anexo, nome_arquivo, url_site, mensagem_id, idx, type) values (?, ?, ?, ?, ?, ?, ?)";
				
				PreparedStatement pstmt = connection.prepareStatement(insrtCommand, Statement.RETURN_GENERATED_KEYS);
				PreparedStatement pstmtAnexo = connection.prepareStatement(insrtAnexoCmd);

				for (String nomeGrupo : mensagem.getGrupos()) {
					pstmt.clearParameters();
					pstmtAnexo.clearParameters();
					
	                pstmt.setString(1, mensagem.getRemetenteUID().toUpperCase());
	                pstmt.setString(2, nomeGrupo);
	                pstmt.setString(3, mensagem.getConteudo());
	                pstmt.setString(4, mensagem.getTitulo());
	                pstmt.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));
	                pstmt.setString(6, nomeGrupo);
					if (idFDeployment == null) {
						pstmt.setNull(7, java.sql.Types.INTEGER);
					} else {
						pstmt.setLong(7, idFDeployment);
					}
					pstmt.executeUpdate();

					Long mensagemId = null;
					ResultSet keys = pstmt.getGeneratedKeys();
					if(keys.next()) {
						mensagemId = keys.getLong(1);
						idMensagemCriada.add(mensagemId);
					}					
					
					for(AnexoVO anexo : mensagem.getAnexos()) {
						pstmtAnexo.clearParameters();
						pstmtAnexo.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
						pstmt.setBytes(2, anexo.arqAnexo);

						pstmtAnexo.setString(3, anexo.nomeArquivo);
						pstmtAnexo.setString(4, anexo.urlSite);
						pstmtAnexo.setLong(5, mensagemId);
						pstmtAnexo.setLong(6, 1);
						pstmtAnexo.setString(7, anexo.type);
						pstmtAnexo.executeUpdate();
					}
					alertaDao.enviaAlertaGrupo(nomeGrupo, "Novo alerta", mensagem.titulo, MessageData.NOVA_MENSAGEM);
				}				
            }			
		});
		
		Usuario usrRemetente = null;
		List usrs = hibernateTemplate.find("from Usuario where upper(uid) = ?", mensagem.getRemetenteUID().toUpperCase());
		if(usrs.size() > 0) {
			usrRemetente = (Usuario)usrs.get(0);
		}
		
		Collection<ArrayList<String>> uids = mensagem.uidsGrupos.values();
		Map<String, Boolean> jaEnviado = new HashMap<String, Boolean>();
		for(List<String> lstUid : uids) {
			for(String uid : lstUid) {
				if(jaEnviado.get(uid) == null || !jaEnviado.get(uid)) {
					jaEnviado.put(uid, true);
					alertaDao.enviaAlertaUsuario(uid, "Novo alerta", mensagem.titulo, MessageData.NOVA_MENSAGEM);
				}				
			}
		}
		
		
		if(prjidNovosDeployments.size() > 0) {
			final Projeto prj = hibernateTemplate.get(Projeto.class, prjidNovosDeployments.get(0));
			
			Deployment dep = new Deployment();
			dep.setProjeto(prj);
			prj.setDeployment(dep);
			dep.setAtivo(true);
			dep.setDhPublicacao(new java.util.Date());
			dep.setProjetoMensagem(true);
			dep.setRemovido(false);

			Historico hist = new Historico();
			hist.setDescricao("Projeto publicado para mensagem");
			hist.setDhAlteracao(new java.util.Date());
			if (usrRemetente != null) {
				hist.setUsuario(usrRemetente);
				prj.getHistoricos().add(hist);
			}
			
			Projeto implantado = projetoDao.deploy(prj);
			final Long idProjetoImplantado = new Long(implantado.getDeployment().getId());
			session.doWork(new Work() {
				
				@Override
				public void execute(Connection connection) throws SQLException {
					String updtMensagem = "update mensagem set deployment_id = ? where id = ?";
					
					PreparedStatement pstmt = connection.prepareStatement(updtMensagem);
					pstmt.setLong(1, idProjetoImplantado);
					pstmt.setLong(2, idMensagemCriada.get(0));
					pstmt.executeUpdate();
				}
			});
		}	
		
		
	}	
	
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=30)
	public void novaMensagemEquipamento(final MensagemVO mensagem) {
		if (mensagem == null || mensagem.getEquipamentos().size() == 0 || mensagem.getRemetenteUID() == null ||
				mensagem.getRemetenteUID().trim().length() == 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
		}
		
		final List<Long> prjidNovosDeployments = new ArrayList<Long>();
		final List<Long> idMensagemCriada = new ArrayList<Long>();
		
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work(){

			@Override
            public void execute(Connection connection) throws SQLException {
				Long idFDeployment = null;
				if(mensagem.idProjeto != null && mensagem.idProjeto > 0) {
					ResultSet rsPrj = connection.createStatement().executeQuery("select id, aplicacao from projeto where id = " + mensagem.idProjeto);
					if(rsPrj.next()) {
						if(!rsPrj.getBoolean("aplicacao")) {
							throw new ParametroException("O projeto não é do tipo aplicação", IMensagensErro.PARAMETRO_ID_INVALIDO);
						}
					}
					
					PreparedStatement pstmtDep = connection.prepareStatement("select d.id, d.ativo, d.removido, d.dep_mensagem from deployment d where d.projeto_id in (select id from projeto where projetoid = ? and  publicado = 1)");
					pstmtDep.setLong(1, mensagem.idProjeto);
					ResultSet rsDep = pstmtDep.executeQuery();
					boolean depFound = false;
					while (rsDep.next()) {
						boolean depMensagem = rsDep.getBoolean("dep_mensagem");	
						boolean ativo = rsDep.getBoolean("ativo");
						boolean removido = rsDep.getBoolean("removido");
						if (!ativo) {
							throw new ParametroException("Existe um deployment não ativo para esse projeto", IMensagensErro.PARAMETRO_ID_INVALIDO);
						} else if(removido) {
							throw new ParametroException("Existe um deployment removido lógicamente para o projeto", IMensagensErro.PARAMETRO_ID_INVALIDO);
						} else if(!depMensagem) {
							throw new ParametroException("Existe um deployment ativo para o projeto não destinado a mensagem", IMensagensErro.PARAMETRO_ID_INVALIDO);
						}
						depFound = true;
						idFDeployment = rsDep.getLong("id");
					} 
					if(!depFound) {
						prjidNovosDeployments.add(new Long(mensagem.idProjeto));
					} 
				}

				String insrtCommand = "insert into mensagem (usuario_remetente_id, equipamento_id, conteudo, titulo, data, idx, deployment_id) values ("
						+ "(select id from usuario where upper(uid) = ?),"
						+ "?,?,?,?, "
						+ "(select case when max(m.idx) is null then 1 else max(m.idx) + 1 end from mensagem m where m.equipamento_id = ?), ?)";
				String insrtAnexoCmd = "insert into anexo (dh_inclusao, arq_anexo, nome_arquivo, url_site, mensagem_id, idx, type) values (?, ?, ?, ?, ?, ?, ?)";
				
				PreparedStatement pstmt = connection.prepareStatement(insrtCommand, Statement.RETURN_GENERATED_KEYS);
				PreparedStatement pstmtAnexo = connection.prepareStatement(insrtAnexoCmd);

				for (Long idEquipamento : mensagem.getEquipamentos()) {
					pstmt.clearParameters();
					pstmtAnexo.clearParameters();
					
	                pstmt.setString(1, mensagem.getRemetenteUID().toUpperCase());
	                pstmt.setLong(2, idEquipamento);
	                pstmt.setString(3, mensagem.getConteudo());
	                pstmt.setString(4, mensagem.getTitulo());
	                pstmt.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));
	                pstmt.setLong(6, idEquipamento);
					if (idFDeployment == null) {
						pstmt.setNull(7, java.sql.Types.INTEGER);
					} else {
						pstmt.setLong(7, idFDeployment);
					}	                
	                pstmt.executeUpdate();

					Long mensagemId = null;
					ResultSet keys = pstmt.getGeneratedKeys();
					if(keys.next()) {
						mensagemId = keys.getLong(1);
						idMensagemCriada.add(mensagemId);
					}					
					
					for(AnexoVO anexo : mensagem.getAnexos()) {
						pstmtAnexo.clearParameters();
						pstmtAnexo.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
						pstmt.setBytes(2, anexo.arqAnexo);
						pstmtAnexo.setString(3, anexo.nomeArquivo);
						pstmtAnexo.setString(4, anexo.urlSite);
						pstmtAnexo.setLong(5, mensagemId);
						pstmtAnexo.setLong(6, 1);
						pstmtAnexo.setString(7, anexo.type);
						pstmtAnexo.executeUpdate();
					}
					alertaDao.enviaAlertaEquipamento(idEquipamento, "Novo alerta", mensagem.titulo, MessageData.NOVA_MENSAGEM);
				}
            }			
		});
		
		Usuario usrRemetente = null;
		List usrs = hibernateTemplate.find("from Usuario where upper(uid) = ?", mensagem.getRemetenteUID().toUpperCase());
		if(usrs.size() > 0) {
			usrRemetente = (Usuario)usrs.get(0);
		}
		
		if(prjidNovosDeployments.size() > 0) {
			final Projeto prj = hibernateTemplate.get(Projeto.class, prjidNovosDeployments.get(0));
			
			Deployment dep = new Deployment();
			dep.setProjeto(prj);
			prj.setDeployment(dep);
			dep.setAtivo(true);
			dep.setDhPublicacao(new java.util.Date());
			dep.setProjetoMensagem(true);
			dep.setRemovido(false);

			Historico hist = new Historico();
			hist.setDescricao("Projeto publicado para mensagem");
			hist.setDhAlteracao(new java.util.Date());
			if (usrRemetente != null) {
				hist.setUsuario(usrRemetente);
				prj.getHistoricos().add(hist);
			}
			
			Projeto implantado = projetoDao.deploy(prj);
			final Long idProjetoImplantado = new Long(implantado.getDeployment().getId());
			session.doWork(new Work() {
				
				@Override
				public void execute(Connection connection) throws SQLException {
					String updtMensagem = "update mensagem set deployment_id = ? where id = ?";
					
					PreparedStatement pstmt = connection.prepareStatement(updtMensagem);
					pstmt.setLong(1, idProjetoImplantado);
					pstmt.setLong(2, idMensagemCriada.get(0));
					pstmt.executeUpdate();
				}
			});
		}		
	}	
	
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=30)
	public void novaMensagemGrupoEquipamentos(final MensagemVO mensagem) {
		if (mensagem == null || mensagem.getGrupoEquipamentos().size() == 0 || mensagem.getRemetenteUID() == null ||
				mensagem.getRemetenteUID().trim().length() == 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
		}
		
		final List<Long> prjidNovosDeployments = new ArrayList<Long>();
		final List<Long> idMensagemCriada = new ArrayList<Long>();
		
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work(){

			@Override
            public void execute(Connection connection) throws SQLException {
				Long idFDeployment = null;
				if(mensagem.idProjeto != null && mensagem.idProjeto > 0) {
					ResultSet rsPrj = connection.createStatement().executeQuery("select id, aplicacao from projeto where id = " + mensagem.idProjeto);
					if(rsPrj.next()) {
						if(!rsPrj.getBoolean("aplicacao")) {
							throw new ParametroException("O projeto não é do tipo aplicação", IMensagensErro.PARAMETRO_ID_INVALIDO);
						}
					}
					
					PreparedStatement pstmtDep = connection.prepareStatement("select d.id, d.ativo, d.removido, d.dep_mensagem from deployment d where d.projeto_id in (select id from projeto where projetoid = ? and  publicado = 1)");
					pstmtDep.setLong(1, mensagem.idProjeto);
					ResultSet rsDep = pstmtDep.executeQuery();
					boolean depFound = false;
					while (rsDep.next()) {
						boolean depMensagem = rsDep.getBoolean("dep_mensagem");	
						boolean ativo = rsDep.getBoolean("ativo");
						boolean removido = rsDep.getBoolean("removido");
						if (!ativo) {
							throw new ParametroException("Existe um deployment não ativo para esse projeto", IMensagensErro.PARAMETRO_ID_INVALIDO);
						} else if(removido) {
							throw new ParametroException("Existe um deployment removido lógicamente para o projeto", IMensagensErro.PARAMETRO_ID_INVALIDO);
						} else if(!depMensagem) {
							throw new ParametroException("Existe um deployment ativo para o projeto não destinado a mensagem", IMensagensErro.PARAMETRO_ID_INVALIDO);
						}
						depFound = true;
						idFDeployment = rsDep.getLong("id");
					} 
					if(!depFound) {
						prjidNovosDeployments.add(new Long(mensagem.idProjeto));
					} 
				}
				
				String insrtCommand = "insert into mensagem (usuario_remetente_id, GRUPO_EQUIPAMENTO_ID, conteudo, titulo, data, idx, deployment_id) values ("
						+ "(select id from usuario where upper(uid) = ?),"
						+ "?,?,?,?, "
						+ "(select case when max(m.idx) is null then 1 else max(m.idx) + 1 end from mensagem m where m.GRUPO_EQUIPAMENTO_ID = ?), ?)";
				String insrtAnexoCmd = "insert into anexo (dh_inclusao, arq_anexo, nome_arquivo, url_site, mensagem_id, idx, type) values (?, ?, ?, ?, ?, ?, ?)";
				
				PreparedStatement pstmt = connection.prepareStatement(insrtCommand, Statement.RETURN_GENERATED_KEYS);
				PreparedStatement pstmtAnexo = connection.prepareStatement(insrtAnexoCmd);

				for (Long idGrupoEquipamentos : mensagem.getGrupoEquipamentos()) {
					pstmt.clearParameters();
	                pstmt.setString(1, mensagem.getRemetenteUID().toUpperCase());
	                pstmt.setLong(2, idGrupoEquipamentos);
	                pstmt.setString(3, mensagem.getConteudo());
	                pstmt.setString(4, mensagem.getTitulo());
	                pstmt.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));
	                pstmt.setLong(6, idGrupoEquipamentos);
					if (idFDeployment == null) {
						pstmt.setNull(7, java.sql.Types.INTEGER);
					} else {
						pstmt.setLong(7, idFDeployment);
					}	                
	                pstmt.executeUpdate();

	                Long mensagemId = null;
					ResultSet keys = pstmt.getGeneratedKeys();
					if(keys.next()) {
						mensagemId = keys.getLong(1);
						idMensagemCriada.add(mensagemId);
					}
					
					for(AnexoVO anexo : mensagem.getAnexos()) {
						pstmtAnexo.clearParameters();
						pstmtAnexo.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
						pstmt.setBytes(2, anexo.arqAnexo);
						pstmtAnexo.setString(3, anexo.nomeArquivo);
						pstmtAnexo.setString(4, anexo.urlSite);
						pstmtAnexo.setLong(5, mensagemId);
						pstmtAnexo.setLong(6, 1);
						pstmtAnexo.setString(7, anexo.type);
						pstmtAnexo.executeUpdate();
					}				
                }
            }			
		});
		
		Usuario usrRemetente = null;
		List usrs = hibernateTemplate.find("from Usuario where upper(uid) = ?", mensagem.getRemetenteUID().toUpperCase());
		if(usrs.size() > 0) {
			usrRemetente = (Usuario)usrs.get(0);
		}
		
		if(prjidNovosDeployments.size() > 0) {
			final Projeto prj = hibernateTemplate.get(Projeto.class, prjidNovosDeployments.get(0));
			
			Deployment dep = new Deployment();
			dep.setProjeto(prj);
			prj.setDeployment(dep);
			dep.setAtivo(true);
			dep.setDhPublicacao(new java.util.Date());
			dep.setProjetoMensagem(true);
			dep.setRemovido(false);

			Historico hist = new Historico();
			hist.setDescricao("Projeto publicado para mensagem");
			hist.setDhAlteracao(new java.util.Date());
			if (usrRemetente != null) {
				hist.setUsuario(usrRemetente);
				prj.getHistoricos().add(hist);
			}
			
			Projeto implantado = projetoDao.deploy(prj);
			final Long idProjetoImplantado = new Long(implantado.getDeployment().getId());
			session.doWork(new Work() {
				
				@Override
				public void execute(Connection connection) throws SQLException {
					String updtMensagem = "update mensagem set deployment_id = ? where id = ?";
					
					PreparedStatement pstmt = connection.prepareStatement(updtMensagem);
					pstmt.setLong(1, idProjetoImplantado);
					pstmt.setLong(2, idMensagemCriada.get(0));
					pstmt.executeUpdate();
				}
			});
		}	
	}	
	
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=50)
	public void novaMensagemBPM(final MensagemVO mensagem) {
		if (mensagem == null || (mensagem.destinatariosUids.size() == 0 && mensagem.grupos.size() == 0) || mensagem.getRemetenteUID() == null ||
				mensagem.getRemetenteUID().trim().length() == 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
		}		
		
		final List<Long> prjidNovosDeployments = new ArrayList<Long>();
		final List<Long> idMensagemCriada = new ArrayList<Long>();
		
		List<Usuario> usrs = usuarioDao.findByLogin(mensagem.remetenteUID);
		final Usuario usuarioRemetente = usrs != null && usrs.size() > 0 ? usrs.get(0) : null;
		
		final List<Usuario> usuarios = new ArrayList<Usuario>();
		final List<Grupo> grupos = new ArrayList<Grupo>();
		for(String uid : mensagem.destinatariosUids) {
			usrs = usuarioDao.findByLogin(uid);
			if(usrs != null && usrs.size() > 0) {
				usuarios.add(usrs.get(0));
			}
		}

		// mensagems para grupos usuários
		for(String cn : mensagem.grupos) {
			List<Grupo> grps = usuarioDao.findGroupByName(cn);
			if(grps != null && grps.size() > 0) {
				grupos.add(grps.get(0));
			}
		}
		 
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		
		session.doWork(new Work(){

			@Override
            public void execute(Connection connection) throws SQLException {
				PreparedStatement pstmtAgrupamento = connection.prepareStatement("insert into AGRUPAMENTO_MENSAGEM (resolvido) values (?)", Statement.RETURN_GENERATED_KEYS);
				
				PreparedStatement pstmtUsr = connection.prepareStatement("insert into MENSAGEM (bp_instance_id, deployment_id, usuario_remetente_id, "
						+ "usuario_mensagem_id, grupo_id, conteudo, titulo, data, agrupamento_mensagem_id, idx) values (?, ?, ?, ?, ?, ?, ?, ?, ?, 1)");

				pstmtAgrupamento.setBoolean(1, false);
				pstmtAgrupamento.executeUpdate();
				ResultSet agrp = pstmtAgrupamento.getGeneratedKeys();
				Long agrupamentoKey = null;
				if(agrp.next()) {
					agrupamentoKey = agrp.getLong(1);
				}
				
				// mensagems para usuários
				for(Usuario usr : usuarios) {
					pstmtUsr.clearParameters();
					pstmtUsr.setLong(1, mensagem.bpInstance.id);
					pstmtUsr.setLong(2, mensagem.deployment.id);
					pstmtUsr.setLong(3, usuarioRemetente.getId());
					pstmtUsr.setLong(4, usr.getId());
					pstmtUsr.setNull(5, Types.INTEGER);
					pstmtUsr.setString(6, mensagem.conteudo);
					pstmtUsr.setString(7, mensagem.titulo);
					pstmtUsr.setTimestamp(8, new java.sql.Timestamp(mensagem.data.getTime()));
					pstmtUsr.setLong(9, agrupamentoKey);
					
					pstmtUsr.executeUpdate();
				}
				
				// mensagems para grupos usuários
				for(Grupo grupo : grupos) {
					pstmtUsr.clearParameters();
					pstmtUsr.setLong(1, mensagem.bpInstance.id);
					pstmtUsr.setLong(2, mensagem.deployment.id);
					pstmtUsr.setLong(3, usuarioRemetente.getId());
					pstmtUsr.setNull(4, Types.INTEGER);
					pstmtUsr.setLong(5, grupo.getId());
					pstmtUsr.setString(6, mensagem.conteudo);
					pstmtUsr.setString(7, mensagem.titulo);
					pstmtUsr.setTimestamp(8, new java.sql.Timestamp(mensagem.data.getTime()));
					pstmtUsr.setLong(9, agrupamentoKey);
					pstmtUsr.executeUpdate();
				}
				
            }			
		});
		
		
	}	
	
	
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=50)
	public void novaMensagem(final MensagemVO mensagem) {
		if (mensagem == null || (mensagem.destinatariosUids.size() == 0 && mensagem.grupos.size() == 0) || mensagem.getRemetenteUID() == null ||
				mensagem.getRemetenteUID().trim().length() == 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
		}		
		
		final List<Long> prjidNovosDeployments = new ArrayList<Long>();
		final List<Long> idMensagemCriada = new ArrayList<Long>();
		
		List<Usuario> usrs = usuarioDao.findByLogin(mensagem.remetenteUID);
		final Usuario usuarioRemetente = usrs != null && usrs.size() > 0 ? usrs.get(0) : null;
		
		final List<Usuario> usuarios = new ArrayList<Usuario>();
		final List<Grupo> grupos = new ArrayList<Grupo>();
		for(String uid : mensagem.destinatariosUids) {
			usrs = usuarioDao.findByLogin(uid);
			if(usrs != null && usrs.size() > 0) {
				usuarios.add(usrs.get(0));
			}
		}

		// mensagems para grupos usuários
		for(String cn : mensagem.grupos) {
			List<Grupo> grps = usuarioDao.findGroupByName(cn);
			if(grps != null && grps.size() > 0) {
				grupos.add(grps.get(0));
			}
		}
		 
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		
		session.doWork(new Work(){

			@Override
            public void execute(Connection connection) throws SQLException {
				
				PreparedStatement pstmtUsr = connection.prepareStatement("insert into MENSAGEM (usuario_remetente_id, "
						+ "usuario_mensagem_id, grupo_id, conteudo, titulo, data, idx) values (?, ?, ?, ?, ?, ?, 1)");

				// mensagems para usuários
				for(Usuario usr : usuarios) {
					pstmtUsr.clearParameters();
					pstmtUsr.setLong(1, usuarioRemetente.getId());
					pstmtUsr.setLong(2, usr.getId());
					pstmtUsr.setNull(3, Types.INTEGER);
					pstmtUsr.setString(4, mensagem.conteudo);
					pstmtUsr.setString(5, mensagem.titulo);
					pstmtUsr.setTimestamp(6, new java.sql.Timestamp(mensagem.data.getTime()));
					
					pstmtUsr.executeUpdate();
				}
				
				// mensagems para grupos usuários
				for(Grupo grupo : grupos) {
					pstmtUsr.clearParameters();
					pstmtUsr.setLong(1, usuarioRemetente.getId());
					pstmtUsr.setNull(2, Types.INTEGER);
					pstmtUsr.setLong(3, grupo.getId());
					pstmtUsr.setString(4, mensagem.conteudo);
					pstmtUsr.setString(5, mensagem.titulo);
					pstmtUsr.setTimestamp(6, new java.sql.Timestamp(mensagem.data.getTime()));
					pstmtUsr.executeUpdate();
				}
				
            }			
		});
		
		
	}		
	
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=50)
	public void atribuiTarefaMensagem(final Long mensagemId, final String loginUsuario) {
		if(mensagemId == null || mensagemId <= 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
		}
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String consultaMensgemResolvida = "select ag.resolvido, ag.id from agrupamento_mensagem ag where ag.id = (select m.agrupamento_mensagem_id from mensagem m where m.id = ?)";
				PreparedStatement pstmt = connection.prepareStatement(consultaMensgemResolvida);
				pstmt.setLong(1, mensagemId);
				
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()) {
					boolean resolvido = rs.getBoolean("resolvido");
					long agrupamentoId = rs.getLong("id");
					if(!resolvido) {
						Statement stmt = connection.createStatement();
						stmt.executeUpdate("update agrupamento_mensagem set resolvido = 1, usuario_tarefa_id = (select id from usuario where upper(uid) = '"+loginUsuario.toUpperCase()+"') where id = " + agrupamentoId);
					} else {
						throw new ParametroException(IMensagensErro.TAREFA_JA_ATRIBUIDA_MSG, IMensagensErro.TAREFA_JA_ATRIBUIDA_CODE);
					}
				} else {
					throw new ParametroException(IMensagensErro.TAREFA_NAO_PODESER_ATRIBUIDA_MSG, IMensagensErro.TAREFA_NAO_PODESER_ATRIBUIDA_CODE);
				}
			}
		});
	}
	
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=30)
	public InfoRetornoMensagem removeMensagemUsuario(final Long idMensagem, final String loginUsuario) {
		InfoRetornoMensagem info = new InfoRetornoMensagem();
		if(idMensagem == null || idMensagem <= 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
		} else if(StringUtils.isEmpty(loginUsuario)) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
		}
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				PreparedStatement pstmtC = connection.prepareStatement("select mensagem_id, usuario_id, mensagem_removida from mensagem_usuario "
						+ "where mensagem_id = ? and usuario_id = (select id from usuario where upper(login) = ?) and mensagem_removida = 1");
				pstmtC.setLong(1, idMensagem);
				pstmtC.setString(2, loginUsuario.toUpperCase());
				ResultSet rs = pstmtC.executeQuery();
				if (!rs.next()) {
					PreparedStatement pstmt = connection.prepareStatement("insert into mensagem_usuario (mensagem_id, usuario_id, mensagem_removida) "
							+ "values (?, (select id from usuario where upper(login) = ?), 1)");
					pstmt.setLong(1, idMensagem);
					pstmt.setString(2, loginUsuario.toUpperCase());
					pstmt.executeUpdate();
					
					pstmt.close();
				}
				rs.close();
				pstmtC.close();
				
			}
		});
		return info;
	}
	
	
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=50)
	public String isTarefaAtribuida(final Long mensagemId) {
		final List<String> lstRet = new ArrayList<String>();
		lstRet.add("");
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				PreparedStatement pstmt = connection.prepareStatement("select am.resolvido, u.login "
						+ "from mensagem m "
						+ "inner join agrupamento_mensagem am on m.agrupamento_mensagem_id = am.id "
						+ "left join usuario u on u.id = am.usuario_tarefa_id "
						+ "where m.id = ?");
				pstmt.setLong(1, mensagemId);
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()) {
					Boolean b = rs.getBoolean("resolvido");
					String usuario = rs.getString("login");
					lstRet.clear();
					String msgAtribuicao = "{tarefaAtribuida: " + String.valueOf(b)  + ", usuarioAtribuido: '" + usuario + "' }";
					lstRet.add(msgAtribuicao);
				} else {
					lstRet.clear();
					String msgAtribuicao = "{tarefaAtribuida: false, usuarioAtribuido: '' }";
					lstRet.add(msgAtribuicao);
				}
			}
		});
		
		return lstRet.get(0);
	}
	

	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=50)
	public void atribuiProjetoMensagem(final Long idProjeto, final Long idMensagem) {
		if(idProjeto == null || idProjeto <= 0 ||
				idMensagem == null || idMensagem <= 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
		}
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work(){

			@Override
			public void execute(Connection connection) throws SQLException {
				ResultSet rs = connection.createStatement().executeQuery("select * from mensagem where id = " + idMensagem);
				if(rs.next()) {
					MensagemVO msgvo = new MensagemVO();
					
					ResultSet rsDeploy = connection.createStatement().executeQuery("select id from deployment where projeto_id = " + idProjeto);
					if(rsDeploy.next()) {
						PreparedStatement pstmt = connection.prepareStatement("update mensagem set deployment_id = ? where id = ?");
						pstmt.setLong(1, rsDeploy.getLong("id"));
						pstmt.setLong(2, idMensagem);
						
						pstmt.executeUpdate();
					} else {
						throw new ParametroException("Não existe deployment para o projeto especificado", IMensagensErro.PARAMETRO_ID_INVALIDO);
					}
				} else {
					throw new ParametroException("Não existe mensagem com o código especificado", IMensagensErro.PARAMETRO_ID_INVALIDO);
				}
			}
		});
	}
	
}
