package br.com.laminarsoft.jazzforms.persistencia.dao;

import java.sql.Blob;
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
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.laminarsoft.jazzforms.negocio.controller.util.LDAPServiceController;
import br.com.laminarsoft.jazzforms.negocio.controller.util.WebServiceController;
import br.com.laminarsoft.jazzforms.persistencia.model.Campo;
import br.com.laminarsoft.jazzforms.persistencia.model.Deployment;
import br.com.laminarsoft.jazzforms.persistencia.model.Grupo;
import br.com.laminarsoft.jazzforms.persistencia.model.Historico;
import br.com.laminarsoft.jazzforms.persistencia.model.ImplementacaoEvento;
import br.com.laminarsoft.jazzforms.persistencia.model.Instancia;
import br.com.laminarsoft.jazzforms.persistencia.model.Linha;
import br.com.laminarsoft.jazzforms.persistencia.model.Pagina;
import br.com.laminarsoft.jazzforms.persistencia.model.Projeto;
import br.com.laminarsoft.jazzforms.persistencia.model.Usuario;
import br.com.laminarsoft.jazzforms.persistencia.model.ValorDataview;
import br.com.laminarsoft.jazzforms.persistencia.model.ValorFormulario;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.IMensagensErro;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.ParametroException;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Component;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.DataView;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Option;
import br.com.laminarsoft.jazzforms.persistencia.model.util.InfoRetornoOpcaoSelect;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.DataViewStructure;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.FotoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.InstanciaVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.InstanciasVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LdapGrupoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LdapUsuarioVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.ProjetoImplantadoHistoricoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.ProjetoImplantadoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.ValorDataviewVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.ValorFormularioVO;

import com.unboundid.ldap.sdk.LDAPSearchException;

@Transactional(propagation=Propagation.SUPPORTS, 
	isolation=Isolation.READ_UNCOMMITTED, 
	timeout=30)
@Repository("DeploymentDao")
@SuppressWarnings("all")
public class DeploymentDao extends BaseDao<Deployment> {

	@Autowired private EhCacheCacheManager cacheManager;
	@Autowired private LDAPServiceController ldapServiceController;
	public static final long serialVersionUID = 12l;
	
	private static final int SITUACAO_APAGADO = 20;
	private static final int SITUACAO_DESATIVADO = 10;
	private static final int SITUACAO_ATIVO = 1;
	
	public class WorkRemoveDeployment implements Work {

		Long projetoId;
		
		@Override
        public void execute(Connection connection) throws SQLException {
			ResultSet deployId = connection.createStatement().executeQuery("select id from deployment where projeto_id = " + projetoId);
			
			//Remove o deployment, relacionamento com usuários e grupos
			Statement stmt = connection.createStatement();
			if (deployId.next()) {
				Long deploymentId = deployId.getLong("id");
	            String rmvRelUsuarios = "delete from deployment_usuario where deploymentid = " + deploymentId.toString();
	            String rmvRelGrupos = "delete from deployment_grupo where deploymentid = " + deploymentId.toString();
	            String rmvMensagens = "delete from mensagem where deployment_id = " + deploymentId;
	            String rmvBPM = "delete from bp_instance where deployment_id = " + deploymentId;
	            String rmvDeployment = "delete from deployment where id = " + deploymentId; 
	            stmt = connection.createStatement();
	            stmt.executeUpdate(rmvRelUsuarios);
	            stmt.executeUpdate(rmvRelGrupos);
	            stmt.executeUpdate(rmvMensagens);
	            stmt.executeUpdate(rmvBPM);
	            stmt.executeUpdate(rmvDeployment);
            }
			//remove historicos
			String rmvHistoricos = "delete from historico where projeto_id = " + projetoId; 
			stmt.executeUpdate(rmvHistoricos);
			
			//remove valores de formulário e de dataview
			removeInstancias(connection);
			
			//remove as paginas
			removePaginas(connection);
			
			connection.createStatement().executeUpdate("delete from projeto where id = " + projetoId);
			
        }
		
		private void removePaginas(Connection connection) throws SQLException {
			String slctPaginas = "select id, sncha_componentid as componente_id from pagina where projeto_id = " + projetoId;
			Statement stmt = connection.createStatement();
			
			List<Long> containeres = new ArrayList<Long>();
			List<Long> paginas = new ArrayList<Long>();
			ResultSet rsPaginas = stmt.executeQuery(slctPaginas);
			while(rsPaginas.next()) {
				Long paginaId = rsPaginas.getLong("id");
				String rmvImplEventoPagina = "delete from implementacao_evento where pagina_id = " + paginaId;
				connection.createStatement().executeUpdate(rmvImplEventoPagina);
				
				Long containerId = rsPaginas.getLong("componente_id");
				containeres.add(containerId);
				paginas.add(rsPaginas.getLong("id"));
			}

			for(Long paginaId : paginas) {
				connection.createStatement().executeUpdate("delete from pagina where id = " + paginaId);
			}
			
			for(Long containerId : containeres) {
				removeComponente(connection, containerId);
			}
			
			stmt.close();
		}

		private void removeComponente(Connection connection, Long componenteId) throws SQLException {
			ResultSet rsComponentesFilhos = connection.createStatement().executeQuery("select id from sncha_component where component_id = " + componenteId);
			
			while(rsComponentesFilhos.next()) {
				removeComponente(connection, rsComponentesFilhos.getLong("id"));
			}
			
			String slctComponente = "select id, "
					+ "case when geolocation_id is null then 0 else geolocation_id end as geoid from sncha_component where id = " + componenteId;
			Statement stmt = connection.createStatement();
			ResultSet rs1 = stmt.executeQuery(slctComponente);
			
			if(rs1.next()) {
				Long geolocationId = rs1.getLong("geoid");
				
				if (geolocationId > 0) {
	                String rmvGeolocation = "delete from sncha_geolocation where id = " + geolocationId;
	                connection.createStatement().executeUpdate(rmvGeolocation);
                }
			}
			String rmvOption = "delete from sncha_option where sncha_component_id = " + componenteId;
			connection.createStatement().executeUpdate(rmvOption);
			
			String rmvImplementacaoEvento = "delete from implementacao_evento where sncha_component_id = " + componenteId;
			connection.createStatement().executeUpdate(rmvImplementacaoEvento);

			removeTabs(connection, componenteId);

			String rmvComponente = "delete from sncha_component where id = " + componenteId;
			connection.createStatement().executeUpdate(rmvComponente);
		}
		
		/**
		 * Remove as tabs/paginas contidas no componente em questão 
		 * @param connection
		 * @param componentId Id do componente que contém a página/tab
		 * @throws SQLException
		 */
		private void removeTabs(Connection connection, Long componentId) throws SQLException {
			String slctTabsComp = "select id from tab where sncha_tabpanel_id = " + componentId;
			String slctPagsComp = "select id from tab where sncha_carousel_id = " + componentId;
			
			List<Long> idtabs = new ArrayList<Long>();
			
			ResultSet rsTabsCmp = connection.createStatement().executeQuery(slctTabsComp);
			while(rsTabsCmp.next()) {
				Long tabId = rsTabsCmp.getLong("id");
				idtabs.add(tabId);
				ResultSet rsComponentes = connection.createStatement().executeQuery("select id from sncha_component where tab_id = " + tabId);
				while(rsComponentes.next()) {
					removeComponente(connection, rsComponentes.getLong("id"));
				}
			}
			for(Long tbid : idtabs) {
				connection.createStatement().executeUpdate("delete from tab where id = " + tbid);
			}
			idtabs.clear();
			
			ResultSet rsPagsCmp = connection.createStatement().executeQuery(slctPagsComp);
			while(rsPagsCmp.next()) {
				Long pagId = rsPagsCmp.getLong("id");
				idtabs.add(pagId);
				ResultSet rsComponents = connection.createStatement().executeQuery("select id from sncha_component where tab_id = " + pagId);
				while(rsComponents.next()) {
					removeComponente(connection, rsComponents.getLong("id"));
				}
			}
			for(Long tbid : idtabs) {
				connection.createStatement().executeUpdate("delete from tab where id = " + tbid);
			}
			rsTabsCmp.close();
			rsPagsCmp.close();
		}
		
		private void removeInstancias(Connection connection) throws SQLException {
			String rmvInstancias = "delete from instancia where projeto_id = " + projetoId;
			String slctInstancias = "select id from instancia where projeto_id = " + projetoId;
	        Statement stmtSlctInstancias = connection.createStatement();
			ResultSet rsInstancias = stmtSlctInstancias.executeQuery(slctInstancias);			
			while(rsInstancias.next()) {
				Long instanciaId = rsInstancias.getLong("id");
				String rmvValorFormulario = "delete from valor_formulario where instanciaid = " + instanciaId;
				String rmvValorDataview = "delete from valor_dataview where instancia_id = " + instanciaId;
				String rmvLinha = "delete from linha where valor_dataviewid = ?";
				String rmvCampo = "delete from campo where linha_id = ?";
				
				String slctDataview = "select id from valor_dataview where instancia_id = " + instanciaId;
				String slctLinha = "select id from linha where valor_dataviewid = ?";
				
				
				Statement dstmt = connection.createStatement();
				dstmt.executeUpdate(rmvValorFormulario);

				
				PreparedStatement pstmtRmvLinha = connection.prepareStatement(rmvLinha);
				PreparedStatement pstmtRmvCampo = connection.prepareStatement(rmvCampo);
				PreparedStatement pstmtSlctLinha = connection.prepareStatement(slctLinha);
				
				Statement sstmt = connection.createStatement();
				ResultSet dtvrs = sstmt.executeQuery(slctDataview);
				
				while(dtvrs.next()) {
					Long idDataview = dtvrs.getLong("id");
					pstmtSlctLinha.clearParameters();
					pstmtSlctLinha.setLong(1, idDataview);
					ResultSet rsLinha = pstmtSlctLinha.executeQuery();
					while(rsLinha.next()) {
						pstmtRmvCampo.clearParameters();
						pstmtRmvCampo.setLong(1, rsLinha.getLong("id"));
						pstmtRmvCampo.executeUpdate();
					}
					rsLinha.close();
					
					pstmtRmvLinha.clearParameters();
					pstmtRmvLinha.setLong(1, idDataview);
					pstmtRmvLinha.executeUpdate();
				}
				
				dstmt = connection.createStatement();
				dstmt.executeUpdate(rmvValorDataview);
			}
			//remove as instancias
			Statement stmtRmvInstancia = connection.createStatement();
			stmtRmvInstancia.executeUpdate(rmvInstancias);
        }
	}
	
	public class WorkInstancias implements Work {
		
		Long projetoId;
		List<InstanciaVO> instancias = new ArrayList<InstanciaVO>();

		@Override
        public void execute(Connection connection) throws SQLException {
	        PreparedStatement pstmtInst = connection.prepareStatement("select instancia.id as instancia_id, instancia.USUARIO_ID, instancia.PROJETO_ID, " 
	        		+"instancia.DH_CRIACAO, instancia.DH_ALTERACAO, instancia.retornada as retornada, usuario.NOME, usuario.LOGIN, usrret.login as login_usuario_ret, usrret.nome as nome_usuario_ret "
	        		+"from instancia "
	        		+"inner join usuario on instancia.USUARIO_ID = usuario.ID "
	        		+"left join usuario usrret on instancia.usuario_retorno_id = usrret.id "
	        		+"where instancia.PROJETO_ID = ?");
	        
	        PreparedStatement pstmtValorForm = connection.prepareStatement("select valor_formulario.ID as valor_id, valor_formulario.VALOR, "
	        		+ "valor_formulario.SNCHA_COMPONENTID as componente_id, "
	        		+"sncha_component.FIELD_ID, sncha_component.DISCRIMINATOR as field_type, "
	        		+"sncha_component.LABEL as label, sncha_component.url_atualizacao as url " 
	        		+"from valor_formulario "
	        		+"inner join sncha_component on valor_formulario.SNCHA_COMPONENTID = sncha_component.ID "
	        		+"where valor_formulario.INSTANCIAID = ?");
	        
	        PreparedStatement pstmtOptions = connection.prepareStatement("select text, value from sncha_option where sncha_component_id = ?");
	        
	        PreparedStatement pstmtFotos = connection.prepareStatement("select cam.id, cam.picture, cam.dh_picture, cam.camera_id, "
	        		+"sncha_component.FIELD_ID, sncha_component.DISCRIMINATOR as field_type, "
	        		+"sncha_component.LABEL as label "
	        		+"from foto cam "
	        		+"inner join sncha_component on cam.camera_id = sncha_component.id "
	        		+"where cam.instancia_id = ?");
	        
	        PreparedStatement pstmtValorDTV = connection.prepareStatement("select valor_dataview.id as dataview_id, "
	        		+ "sncha_component.FIELD_ID as field_id, "
	        		+ "sncha_component.DISCRIMINATOR as field_type, "
	        		+ "sncha_component.id as componente_id, "
	        		+"sncha_component.LABEL as label, "
	        		+"case when count(linha.id) = 0 then null else count(linha.id) end as qtd_linhas "
	        		+"from valor_dataview "
	        		+"left join sncha_component on valor_dataview.SNCHA_COMPONENTID = sncha_component.ID "
	        		+"left join linha on valor_dataview.id = linha.valor_dataviewid "
	        		+"where valor_dataview.INSTANCIA_ID = ?");
	        
	        pstmtInst.setLong(1, projetoId);
	        ResultSet rsInst = pstmtInst.executeQuery();
	        
	        InstanciaVO novaInstnciaVazia = null;
	        while(rsInst.next()) {
	        	InstanciaVO instVo = new InstanciaVO();
	        	instVo.setId(rsInst.getLong("instancia_id"));
	        	instVo.setProjetoId(rsInst.getLong("projeto_id"));
	        	instVo.setUserLogin(rsInst.getString("login"));
	        	instVo.setUserName(rsInst.getString("nome"));
	        	instVo.setDhAlteracao(rsInst.getDate("dh_alteracao"));
	        	instVo.setDhCriacao(rsInst.getDate("dh_criacao"));
	        	Boolean retornada = rsInst.getBoolean("retornada");	        	
	        	instVo.setReenviado(retornada);
	        	
	        	if(retornada) {
	        		instVo.setLoginUsuarioReenvio(rsInst.getString("login_usuario_ret"));
	        		instVo.setNomeUsuarioReenvio(rsInst.getString("nome_usuario_ret"));
	        	}
	        	
	        	pstmtValorDTV.clearParameters();
	        	pstmtValorForm.clearParameters();
	        	pstmtFotos.clearParameters();
	        	
	        	pstmtValorForm.setLong(1, instVo.getId());
	        	ResultSet rsValorForm = pstmtValorForm.executeQuery();
	        	
	        	while(rsValorForm.next()) {
	        		ValorFormularioVO formVo = new ValorFormularioVO();
	        		formVo.setId(rsValorForm.getLong("valor_id"));
	        		formVo.setComponentId(rsValorForm.getLong("componente_id"));
	        		formVo.setFieldId(rsValorForm.getString("field_id"));
	        		formVo.setFieldType(rsValorForm.getString("field_type"));
	        		formVo.setValor(rsValorForm.getString("valor"));
	        		formVo.setFieldLabel(rsValorForm.getString("label"));
	        		if(formVo.getFieldType().equalsIgnoreCase("Select")) {
	        			//recuperar valores da combo pelo webservice, caso exista
	        			String urlOptions = rsValorForm.getString("url");	        			
	        			
	        			if (urlOptions == null || StringUtils.isEmpty(urlOptions)) {
							pstmtOptions.clearParameters();
							pstmtOptions.setLong(1, formVo.getComponentId());
							ResultSet rsOptions = pstmtOptions.executeQuery();
							while (rsOptions.next()) {
								Option opt = new Option();
								opt.setText(rsOptions.getString("text"));
								opt.setValue(rsOptions.getString("value"));
								formVo.getOptions().add(opt);
							}
						} else {
							WebServiceController<InfoRetornoOpcaoSelect> opcoes = new WebServiceController<InfoRetornoOpcaoSelect>();
							InfoRetornoOpcaoSelect infoOpcoes = opcoes.executaChamadaGet(urlOptions, InfoRetornoOpcaoSelect.class);
							for(Option opt :infoOpcoes.opcoes) {
								formVo.getOptions().add(opt);
							}
						}
	        		}
	        		instVo.getValoresFormulario().add(formVo);
	        	}
	        	
	        	pstmtFotos.setLong(1, instVo.getId());
	        	ResultSet rsFotos = pstmtFotos.executeQuery();
	        	while(rsFotos.next()) {
	        		FotoVO fotoVo = new FotoVO();
	        		fotoVo.setDhPicture(rsFotos.getDate("dh_picture").getTime());
	        		fotoVo.setId(rsFotos.getLong("id"));
	        		fotoVo.setComponentId(rsFotos.getLong("camera_id"));
	        		fotoVo.setFieldId(rsFotos.getString("FIELD_ID"));
	        		fotoVo.setFieldLabel(rsFotos.getString("label"));
	        		fotoVo.setFieldType(rsFotos.getString("field_type"));
					Blob blobPict = rsFotos.getBlob("picture");
					
					fotoVo.setFoto(new Base64().decode(blobPict.getBytes(1l, (int)blobPict.length())));
					fotoVo.setValor("1");
					instVo.getValoresFoto().add(fotoVo);
	        	}
	        	
	        	pstmtValorDTV.setLong(1, instVo.getId());
	        	ResultSet rsValorDTV = pstmtValorDTV.executeQuery();
	        	while(rsValorDTV.next()) {
	        		if (rsValorDTV.getString("field_id") == null) {
	        			continue;
	        		}
	        		ValorDataviewVO dtvVo = new ValorDataviewVO();
	        		dtvVo.setComponentId(rsValorDTV.getLong("componente_id"));
	        		dtvVo.setFieldType(rsValorDTV.getString("field_type"));
	        		dtvVo.setFieldId(rsValorDTV.getString("field_id"));
	        		dtvVo.setId(rsValorDTV.getLong("dataview_id"));
	        		dtvVo.setFieldLabel(rsValorDTV.getString("label"));
	        		dtvVo.setQtdLinhas(rsValorDTV.getInt("qtd_linhas"));
	        		instVo.getValoresDataview().add(dtvVo);
	        	}
	        	
	        	instancias.add(instVo);
	        }
	        
        }
	}
	
	public class WorkProjetosImplantados implements Work {
		List<ProjetoImplantadoVO> listaPrjImplantados = new ArrayList<ProjetoImplantadoVO>();

		@Override
        public void execute(Connection connection) throws SQLException {
			Statement stmtNaoAtivos = connection.createStatement();
			PreparedStatement pstmt_1 = connection.prepareStatement("select usrDep.login as dep_login, " 
						   +"usrDep.nome as dep_user, " 
					       +"hist.dep_descricao, " 
						   +"hist.dep_dh_alteracao, "
					       +"usrBase.login as base_login, " 
					       +"usrBase.nome as base_user, " 
						   +"hist.base_descricao, "
						   +"hist.base_dh_alteracao "
					+"from usuario usrDep, usuario usrBase, "
					+"( "
						+"select max(histDep.id) as max_dep_id, " 
							   +"histDep.descricao as dep_descricao, "
							   +"histDep.usuario_id as dep_user_id, "
							   +"histDep.dh_alteracao as dep_dh_alteracao, "
							   +"max(histBase.id) as max_base_id, "
							   +"histBase.descricao as base_descricao, "
							   +"histBase.usuario_id as base_user_id, "
							   +"histBase.DH_ALTERACAO as base_dh_alteracao "
						+"from historico histDep, historico histBase, "
							+"(select projDep.id as dep_id, projBase.id as base_id from projeto projDep " 
							+"left join projeto projBase on projDep.projetoid = projBase.ID "
							+"where projDep.id = ? "
							+") proj "
						+"where histDep.projeto_id = proj.dep_id and histBase.projeto_id = proj.base_id "
					+") hist "
					+"where usrDep.id = hist.dep_user_id "
					+"and usrBase.id = hist.base_user_id");
			
			ResultSet rsNaoAtivos = stmtNaoAtivos.executeQuery("select dep.id as deploy_id, dep.dh_publicacao, dep.ativo, dep.removido, "
				+"proj2.id as projeto_id, proj2.nome, proj2.descricao, proj2.dh_criacao, proj2.publicado, proj2.inst_qtd, "
				+"(select dh_alteracao from historico where id = proj2.hist_id) as ultima_alteracao, " 
				+"(select descricao from historico where id = proj2.hist_id) as desc_ultima_alteracao, " 
				+"proj2.hist_id as historico_id "
				+"from deployment dep " 
				+"inner join ( "
				+"	select prj1.*, inst_qtd, (select max(hist.id) from historico hist where hist.PROJETO_ID = prj1.id) as hist_id "
				+"    from projeto prj1 "
				+"	inner join ( "
				+"		select prj.id as projeto_id, count(inst.id) as inst_qtd "
				+"		from projeto prj "
				+"		left join instancia inst on prj.id = inst.PROJETO_ID "
				+"		where prj.DEPLOYMENT_ID is not null "
				+"		group by prj.id "
				+"    ) retprj on prj1.id = retprj.projeto_id "
				+") proj2 on dep.id = proj2.deployment_id");
			
			ResultSet rsAtivos = connection.createStatement().executeQuery("select dep.id as deploy_id, dep.dh_publicacao, dep.ativo, dep.removido, "
				+"proj2.id as projeto_id, proj2.nome, proj2.descricao, proj2.dh_criacao, proj2.publicado, " 
				+"(select count(*) from instancia where instancia.projeto_id = proj2.id) as inst_qtd, "
				+"(select dh_alteracao from historico where id = hist.id) as ultima_alteracao, " 
				+"(select descricao from historico where id = hist.id) as desc_ultima_alteracao, "
				+"hist.id as historico_id "
				+"from deployment dep "
				+"inner join projeto proj2 on dep.projeto_id = proj2.id "
				+"inner join (select max(id) as id, projeto_id from historico group by projeto_id) hist on dep.projeto_id = hist.projeto_id");
			
			while(rsNaoAtivos.next()) {
				pstmt_1.clearParameters();
				ProjetoImplantadoVO vo = new ProjetoImplantadoVO();
				vo.setDeployId(rsNaoAtivos.getLong("deploy_id"));
				vo.setDhPublicacao(rsNaoAtivos.getDate("dh_publicacao"));
				vo.setAtivo(rsNaoAtivos.getBoolean("ativo"));
				vo.setRemovido(rsNaoAtivos.getBoolean("removido"));
				vo.setProjetoId(rsNaoAtivos.getLong("projeto_id"));
				vo.setNomeProjeto(rsNaoAtivos.getString("nome"));
				vo.setDescProjeto(rsNaoAtivos.getString("descricao"));
				vo.setDhCriacao(rsNaoAtivos.getDate("dh_criacao"));
				vo.setPublicado(rsNaoAtivos.getBoolean("publicado"));
				vo.setQtdInstancias(rsNaoAtivos.getLong("inst_qtd"));
				vo.setUltimaAlteracao(rsNaoAtivos.getDate("ultima_alteracao"));
				vo.setDescUltimaAlteracao(rsNaoAtivos.getString("desc_ultima_alteracao"));
				vo.setHistoricoId(rsNaoAtivos.getLong("historico_id"));
				vo.setSubstituido(true);
				
				pstmt_1.setLong(1, vo.getProjetoId());
				ResultSet rsUsuario = pstmt_1.executeQuery();
				if (rsUsuario.next()) {
					ProjetoImplantadoHistoricoVO hvo = new ProjetoImplantadoHistoricoVO();
					hvo.setLoginUsuarioDeployment(rsUsuario.getString("dep_login"));
					hvo.setNomeUsuarioDeployment(rsUsuario.getString("dep_user"));
					hvo.setDescricaoAcaoUsuarioDeployment(rsUsuario.getString("dep_descricao"));
					hvo.setLoginUsuarioProjeto(rsUsuario.getString("base_login"));
					hvo.setNomeUsuarioProjeto(rsUsuario.getString("base_user"));
					hvo.setDescricaoAcaoUsuarioProjeto(rsUsuario.getString("base_descricao"));
					hvo.setDhAlteracaoDeployment(rsUsuario.getDate("dep_dh_alteracao"));
					hvo.setDhAlteracaoProjeto(rsUsuario.getDate("base_dh_alteracao"));
					vo.setDadosUsuario(hvo);
				}
				
				listaPrjImplantados.add(vo);
			}
			
			while(rsAtivos.next()) {
				pstmt_1.clearParameters();
				ProjetoImplantadoVO vo = new ProjetoImplantadoVO();
				vo.setDeployId(rsAtivos.getLong("deploy_id"));
				vo.setDhPublicacao(rsAtivos.getDate("dh_publicacao"));
				vo.setAtivo(rsAtivos.getBoolean("ativo"));
				vo.setRemovido(rsAtivos.getBoolean("removido"));
				vo.setProjetoId(rsAtivos.getLong("projeto_id"));
				vo.setNomeProjeto(rsAtivos.getString("nome"));
				vo.setDescProjeto(rsAtivos.getString("descricao"));
				vo.setDhCriacao(rsAtivos.getDate("dh_criacao"));
				vo.setPublicado(rsAtivos.getBoolean("publicado"));
				vo.setQtdInstancias(rsAtivos.getLong("inst_qtd"));
				vo.setUltimaAlteracao(rsAtivos.getDate("ultima_alteracao"));
				vo.setDescUltimaAlteracao(rsAtivos.getString("desc_ultima_alteracao"));
				vo.setHistoricoId(rsAtivos.getLong("historico_id"));
				vo.setSubstituido(false);

				pstmt_1.setLong(1, vo.getProjetoId());
				ResultSet rsUsuario = pstmt_1.executeQuery();
				if (rsUsuario.next()) {
					ProjetoImplantadoHistoricoVO hvo = new ProjetoImplantadoHistoricoVO();
					hvo.setLoginUsuarioDeployment(rsUsuario.getString("dep_login"));
					hvo.setNomeUsuarioDeployment(rsUsuario.getString("dep_user"));
					hvo.setDescricaoAcaoUsuarioDeployment(rsUsuario.getString("dep_descricao"));
					hvo.setLoginUsuarioProjeto(rsUsuario.getString("base_login"));
					hvo.setNomeUsuarioProjeto(rsUsuario.getString("base_user"));
					hvo.setDescricaoAcaoUsuarioProjeto(rsUsuario.getString("base_descricao"));
					hvo.setDhAlteracaoDeployment(rsUsuario.getDate("dep_dh_alteracao"));
					hvo.setDhAlteracaoProjeto(rsUsuario.getDate("base_dh_alteracao"));
					vo.setDadosUsuario(hvo);
				}				
				
				listaPrjImplantados.add(vo);
			}
			
			pstmt_1.close();
			rsNaoAtivos.close();
			rsAtivos.close();
        }
	}
	
	public class WorkUpdateDestinatariosDeployment implements Work {
		Projeto projeto;
		String msgErro = null;
		
		@Override
		public void execute(Connection connection) throws SQLException {
			Deployment deploy = projeto.getDeployment();
			Long idProjeto = projeto.getId();
			Historico hist = (projeto.getHistoricos() != null && projeto.getHistoricos().size() > 0 ? projeto.getHistoricos().get(0) : null);
			
			PreparedStatement pstmt_0 = connection.prepareStatement("(select case when max(idx) is null then 1 else max(idx) + 1 end from historico where usuario_id = (select id from usuario where upper(login) = ?) and projeto_id = ?)");
			pstmt_0.setString(1, hist.getUsuario().getLogin().toUpperCase());
			pstmt_0.setLong(2, idProjeto);
			ResultSet rs_0 = pstmt_0.executeQuery();
			
			int idx = 1;
			if (rs_0.next()) {
				idx = rs_0.getInt(1);
			}
			rs_0.close();
			pstmt_0.close();
			
			String insertIntoHistorico = "insert into historico (usuario_id, projeto_id, dh_alteracao, descricao, idx) "
					+ "values ((select id from usuario where upper(login) = ?), ?, ?, ?, ?)";
			PreparedStatement pstmt_1 = connection.prepareStatement(insertIntoHistorico);
			pstmt_1.setString(1, hist.getUsuario().getLogin().toUpperCase());
			pstmt_1.setLong(2, idProjeto);
			pstmt_1.setDate(3, new java.sql.Date(hist.getDhAlteracao().getTime()));
			pstmt_1.setString(4, hist.getDescricao());
			pstmt_1.setInt(5, idx);
			
			pstmt_1.executeUpdate();
			pstmt_1.close();
			
			String deleteDepUsr = "delete from deployment_usuario where deploymentid = ?";
			pstmt_1 = connection.prepareStatement(deleteDepUsr);
			pstmt_1.setLong(1, deploy.getId());
			pstmt_1.executeUpdate();
			pstmt_1.close();
			
			String deleteDepGrp = "delete from deployment_grupo where deploymentid = ?";
			pstmt_1 = connection.prepareStatement(deleteDepGrp);
			pstmt_1.setLong(1, deploy.getId());
			pstmt_1.executeUpdate();
			pstmt_1.close();
			
			
			List<Usuario> usuarios = deploy.getUsuariosPossiveis();
			List<Grupo> grupos = deploy.getGruposPossiveis();
			
			if (usuarios != null && usuarios.size() > 0) {
				PreparedStatement stmt_2 = connection.prepareStatement("select id from usuario where upper(login) = ?");
				PreparedStatement stmt_21 = connection.prepareStatement("select id from grupo where nome = ?");
				PreparedStatement stmt_3 = connection.prepareStatement("insert into deployment_usuario (usuarioid, deploymentid) values (?,?)");
                PreparedStatement stmt_4 = connection.prepareStatement("insert into usuario (nome, login) values (?,?)", Statement.RETURN_GENERATED_KEYS);
                PreparedStatement stmt_5 = connection.prepareStatement("insert into grupo (nome, descricao) values (?,?)", Statement.RETURN_GENERATED_KEYS);
                PreparedStatement stmt_6 = connection.prepareStatement("insert into usuario_grupo (usuarioid, grupoid) values (?,?) ");
                PreparedStatement stmt_7 = connection.prepareStatement("delete from usuario_grupo where usuarioid = ?");
                try{
					for (Usuario usr : usuarios) {
						stmt_2.clearParameters();
						stmt_21.clearParameters();
						stmt_3.clearParameters();
						stmt_4.clearParameters();
						stmt_5.clearParameters();
						stmt_6.clearParameters();
						stmt_7.clearParameters();
						
	                    Usuario usuarioLdap = getLdapUserInfo(usr.getLogin());
	
	                    stmt_2.setString(1, usr.getLogin().toUpperCase());
						ResultSet rs = stmt_2.executeQuery();
						if (rs.next()) {// usuário já cadastrado localmente
							Long idUsuario = rs.getLong(1);
							stmt_3.setLong(1, idUsuario);
							stmt_3.setLong(2, deploy.getId());
							stmt_3.executeUpdate();
							
							// refazer relacionamentos entre usuários e grupos
							stmt_7.setLong(1, idUsuario);
							stmt_7.executeUpdate();
							
							atualizarGruposUsuario(stmt_21, stmt_5, stmt_6, usuarioLdap, idUsuario);
							
						} else {// cadastrar usuário bem como seus grupos, caso não estejam cadastrados, localmente
	                        stmt_4.setString(1, usuarioLdap.getNome());
	                        stmt_4.setString(2, usuarioLdap.getLogin());
	                        stmt_4.executeUpdate();
	                        
	                        ResultSet rsusr = stmt_4.getGeneratedKeys();
	                        if (rsusr.next()) {
	                        	Long usrId = rsusr.getLong(1);	                        	
	                        	atualizarGruposUsuario(stmt_21, stmt_5, stmt_6, usuarioLdap, usrId);
	                        	
	                        	stmt_3.setLong(1, usrId);
								stmt_3.setLong(2, deploy.getId());
								stmt_3.executeUpdate();
	                        }
						}
					}
                } catch (LDAPSearchException e) {
                	msgErro = "Erro ao recuperar usuario LDAP: " + e.getMessage();
                	return;
                } 
				stmt_2.close();
				stmt_3.close();
			}
			if (grupos != null && grupos.size() > 0) {
				PreparedStatement stmt_2 = connection.prepareStatement("select id from grupo where nome = ?");
				PreparedStatement stmt_3 = connection.prepareStatement("insert into deployment_grupo (deploymentid, grupoid) values (?,?)");
				PreparedStatement stmt_4 = connection.prepareStatement("delete from usuario_grupo where grupoid = ?");
				PreparedStatement stmt_5 = connection.prepareStatement("select id from usuario where upper(login) = ?");
				PreparedStatement stmt_6 = connection.prepareStatement("insert into grupo (nome, descricao) values (?,?)", Statement.RETURN_GENERATED_KEYS);
				PreparedStatement stmt_7 = connection.prepareStatement("insert into usuario (nome, login, ativo) values (?,?, 1)",  Statement.RETURN_GENERATED_KEYS);
				PreparedStatement stmt_8 = connection.prepareStatement("insert into usuario_grupo (usuarioid, grupoid) values (?,?)");
				
				try {
	                for(Grupo grp : grupos) {
	                	stmt_2.clearParameters();
	                	stmt_3.clearParameters();
	                	stmt_4.clearParameters();
	                	stmt_5.clearParameters();
	                	
	                	stmt_2.setString(1, grp.getNome());
	                	ResultSet rs = stmt_2.executeQuery();
	                	Grupo ldapgrupo = getLdapGroupInfo(grp.getNome());
	                	if (rs.next()) {// grupo já cadastrado
	                		Long grupoId = rs.getLong(1);// insere o relacionamento grupo-deployment
	                		stmt_3.setLong(1, deploy.getId());
	                		stmt_3.setLong(2, grupoId);
	                		stmt_3.executeUpdate();
	                		
	                		//stmt_4.setLong(1, grupoId);// limpa tabela relacionamento grupo-usuario
	                		//stmt_4.executeUpdate();
	                		
	                		//atualizarUsuariosGrupo(stmt_5, stmt_7, stmt_8, ldapgrupo, grupoId);
	                		
	                	} else {// grupo ainda não cadastrado. recuperar suas informações e cadastrá-lo
	                		stmt_6.clearParameters();
	                		stmt_6.setString(1, grp.getNome());
	                		stmt_6.setString(2, ldapgrupo.getDescricao());
	                		stmt_6.executeUpdate();
	                		
	                		ResultSet rsgrp = stmt_6.getGeneratedKeys();
	                		if (rsgrp.next()) {
	                			Long grupoId = rsgrp.getLong(1);
	                			atualizarUsuariosGrupo(stmt_5, stmt_7, stmt_8, ldapgrupo, grupoId);
	                			
	                			stmt_3.setLong(1, deploy.getId());
		                		stmt_3.setLong(2, grupoId);
		                		stmt_3.executeUpdate();
	                		}
	                	}
	                }
                } catch (LDAPSearchException e) {
                	msgErro = "Erro ao recuperar grupo LDAP: " + e.getMessage();
                	return;
                }
				stmt_2.close();
				stmt_3.close();
			}
		}

		private void atualizarUsuariosGrupo(PreparedStatement stmt_5, PreparedStatement stmt_7, PreparedStatement stmt_8, Grupo ldapgrupo, Long grupoId)
                throws SQLException {
	        for(Usuario ldapusr : ldapgrupo.getUsuarios()) {
	        	stmt_5.clearParameters();
	        	stmt_7.clearParameters();
	        	stmt_8.clearParameters();

	        	stmt_5.setString(1, ldapusr.getLogin().toUpperCase());
	        	ResultSet rsusr = stmt_5.executeQuery();
	        	if(rsusr.next()) {// usuário cadastrado, recupera id do usuário e insere o relacionamento usuario-grupo
	        		Long usrId = rsusr.getLong(1);
	        		stmt_8.setLong(1, usrId);
	        		stmt_8.setLong(2, grupoId);
	        		stmt_8.executeUpdate();
	        	} else { // insere o usuário e insere o relacionamento usuário-grupo
	        		stmt_7.setString(1, ldapusr.getNome());
	        		stmt_7.setString(2, ldapusr.getLogin());
	        		stmt_7.executeUpdate();

	        		ResultSet rsinsusr = stmt_7.getGeneratedKeys();
	        		if (rsinsusr.next()) {
	        			Long usrId = rsinsusr.getLong(1);
	        			stmt_8.setLong(1, usrId);
	        			stmt_8.setLong(2, grupoId);
	        			stmt_8.executeUpdate();
	        		}
	        	}
	        }
        }

		private void atualizarGruposUsuario(PreparedStatement stmt_21, PreparedStatement stmt_5, PreparedStatement stmt_6, Usuario usuarioLdap, Long usrId)
                throws SQLException {
	        for(Grupo grp : usuarioLdap.getGrupos()) {
	        	stmt_21.clearParameters();
	        	stmt_6.clearParameters();
	        	stmt_21.setString(1, grp.getNome());
	        	ResultSet rsIdGrp = stmt_21.executeQuery();
	        	if(rsIdGrp.next()) {// grupo já existe localmente
	        		Long idGrupo = rsIdGrp.getLong(1);
	        		stmt_6.setLong(1, usrId);
	        		stmt_6.setLong(2, idGrupo);
	        		stmt_6.executeUpdate();
	        	} else { //grupo ainda não cadastrado localmente
	        		stmt_5.clearParameters();
	        		stmt_5.setString(1, grp.getNome());
	        		stmt_5.setString(2, grp.getDescricao());
	        		stmt_5.executeUpdate();
	        		
	        		rsIdGrp = stmt_5.getGeneratedKeys();
	        		if (rsIdGrp.next()) {
	        			Long idGrupo = rsIdGrp.getLong(1);
	        			stmt_6.setLong(1, usrId);
	        			stmt_6.setLong(2, idGrupo);
	        			stmt_6.executeUpdate();
	        		}
	        	}
	        }
        }
		
		private Usuario getLdapUserInfo(String loginUsr) throws LDAPSearchException {
			
			LdapUsuarioVO ldapusr= ldapServiceController.getUsuario(loginUsr);
			Usuario usr = new Usuario();
			usr.setLogin(loginUsr);
			usr.setNome(ldapusr.getCn());
			List<LdapGrupoVO> grupos = ldapServiceController.getGruposUsuario(loginUsr);
			for (LdapGrupoVO grupo : grupos) { 
                Grupo grp = new Grupo();
                grp.setNome(grupo.getCn());
                grp.setDescricao(grupo.getDescription());
                usr.getGrupos().add(grp);
			}			
			return usr;
		}
		
		private Grupo getLdapGroupInfo(String nomeGrupo) throws LDAPSearchException {
			LdapGrupoVO ldapgrp= ldapServiceController.getGrupo(nomeGrupo);
			Grupo grp = new Grupo();
			grp.setNome(ldapgrp.getNome());
			grp.setDescricao(ldapgrp.getDescription());
			for(LdapUsuarioVO ldapusr : ldapgrp.getUsuarios()) {
				Usuario usr = new Usuario();
				usr.setLogin(ldapusr.getUid());
				usr.setNome(ldapusr.getNome());
				grp.getUsuarios().add(usr);
			}
			
			return grp;			
		}
		
	}
	
	/**
	 * Verifica se há algum deployment ativo para o projeto identificado pelo id passado
	 * @param projectId Identificador do projeto a ser verificado se há deployment ativo para ele
	 * @return True, caso haja deployment ativo para o projeto; false, caso contrário
	 */
	@SuppressWarnings("all")
	public boolean existeDeploymentAtivo(Long projectId) {
		boolean ativo = false;
		if (projectId == null || projectId == 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_ID_INVALIDO);
		}
		List<Deployment> deployments = hibernateTemplate.find("from Deployment d where d.projeto.projetoBase.id = ? and d.ativo = true and d.removido = false", projectId);
		ativo = deployments != null && deployments.size() > 0;
		return ativo;
	}
	
	public Deployment getDeploymentById(final long id) {
		Cache cache = cacheManager.getCache("deployment");
		Deployment dep = cache.get(id) == null ? null : (Deployment)(cache.get(id).get());
		if (dep == null) {
			dep = findById(id);
			if (dep != null && dep.getProjeto() != null) {
				try {
					Long prjId = dep.getProjeto().getId();
					getUltimaDataAtualizacao(dep);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				for (Pagina pag : dep.getProjeto().getPaginas()) {
					for (ImplementacaoEvento imp : pag.getMetodos()) {
						imp.getNome();
					}
				}
			}
			cache.put(dep.getId(), dep);
		} 
		return dep;
	}
	
	
	public void restoreCacheDeployment() {
		Cache cache = cacheManager.getCache("deployment");
		final List<Long> deployIds = new ArrayList<Long>();
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("select * from deployment");
				while(rs.next()) {
					deployIds.add(rs.getLong("id"));
				}
			}
		});
		Deployment dep = null;
		for (Long id : deployIds) {
			dep = findById(id);
			if (dep != null && dep.getProjeto() != null) {
				try {
					Long prjId = dep.getProjeto().getId();
					getUltimaDataAtualizacao(dep);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				for (Pagina pag : dep.getProjeto().getPaginas()) {
					for (ImplementacaoEvento imp : pag.getMetodos()) {
						imp.getNome();
					}
				}
			}
			cache.put(dep.getId(), dep);
		}
	}
		
	public void alterarDestinatarios(Projeto projeto) {
		if (projeto == null || projeto.getDeployment() == null || projeto.getDeployment().getId() == 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_ID_INVALIDO);
		}
		
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		WorkUpdateDestinatariosDeployment work = new WorkUpdateDestinatariosDeployment();
		work.projeto = projeto;
        session.doWork(work);		
	}
	
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=50)	
	public int getSituacaoDeployment(Long id) {
		if (id <= 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_ID_INVALIDO);
		}
		int situacao = 0;
		Cache cache = cacheManager.getCache("deployment");
		Deployment dep = null;
		if(cache.get(id) == null) {
			dep = hibernateTemplate.get(Deployment.class, id);
			Projeto p = dep.getProjeto().clone();
			cache.put(id, p.getDeployment());
		} else {
			dep = (Deployment)(cache.get(id).get());
		}
		if (dep == null) {
			situacao = SITUACAO_APAGADO;
		} else if(dep.getAtivo()) {
			situacao = SITUACAO_ATIVO;
		} else {
			situacao = SITUACAO_DESATIVADO;
		}
		
		return situacao;
	}

	/**
	 * Remoção lógica de um deployment
	 * @param deploymentId
	 * @param login Usuáiro que está realizando a remoção lógica
	 */
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=30)	
	public void removeDeployment(final Long deploymentId, final String login) {
		if (deploymentId == null || deploymentId <= 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_ID_INVALIDO);
		}
		Cache cache = cacheManager.getCache("deployment");
		Deployment dep = cache.get(deploymentId) == null ? null : (Deployment)(cache.get(deploymentId).get());
		if (dep == null) {
			dep = hibernateTemplate.get(Deployment.class, deploymentId);
		}
		
		if (dep != null) {// TODO Realizar lógica de deleção quando houverem formulários preenchidos para esse deployment
			
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			session.doWork(new Work() {
				
				@Override
				public void execute(Connection connection) throws SQLException {
					String insHist = "insert into historico (usuario_id, dh_alteracao, descricao, idx, projeto_id) values ((select id from usuario where upper(login) = ?),"
							+ "?, ?, 1,"
							+ "(select projeto_id from deployment where id = ?))";
					
					String updtDeployment = "update deployment set removido = 1, ativo = 0 where id = ?";
					PreparedStatement pstmt = connection.prepareStatement(insHist);
					pstmt.setString(1, login.toUpperCase());
					pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
					pstmt.setString(3, "Deployment do projeto removido");
					pstmt.setLong(4, deploymentId);
					pstmt.executeUpdate();
					
					pstmt.close();
					pstmt = connection.prepareStatement(updtDeployment);
					pstmt.setLong(1, deploymentId);
					pstmt.executeUpdate();
				}
			});
			
			cache.evict(deploymentId);
		}
	}
	
	
	/**
	 * Apaga um deployment permanentemente
	 * @param projetoId id do deployment a ser apagado
	 */
	public void removeDeploymentFisico(final Long projetoId) {
		if (projetoId == null || projetoId <= 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_ID_INVALIDO);
		}
		WorkRemoveDeployment work = new WorkRemoveDeployment();
		work.projetoId = projetoId;
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();

		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				PreparedStatement pstmt = connection.prepareStatement("select id from deployment where projeto_id = ?");
				pstmt.setLong(1, projetoId);
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()) {
					Long id = rs.getLong("id");
					
					Cache cache = cacheManager.getCache("deployment");
					cache.evict(id);					
				}
			}
		});
		
		session.doWork(work);
	}
	
	public List<Deployment> findDeploymentsByLogin(final String login) {
		if (login == null || login.isEmpty()) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_ID_INVALIDO);
		}
		
		final List<Deployment> lstDeployments = new ArrayList<Deployment>();
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		
		// recupera deployments por usuario
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				PreparedStatement pstmtDepUsr = connection.prepareStatement("select d.id as dep_id, p.id as proj_id, d.dh_publicacao, d.nome_processo_negocio, d.nome_atividade_negocio, d.ativo, d.removido, d.dep_mensagem,"
						+ "p.nome as nome_projeto, p.descricao as descricao_projeto, p.aplicacao as aplicacao_projeto "
						+ "from deployment d "
						+ "inner join projeto p on d.projeto_id = p.id "
						+ "inner join deployment_usuario du on d.id = du.deploymentid "
						+ "where du.usuarioid = (select id from usuario where upper(login) = ?)");
				pstmtDepUsr.setString(1, login.toUpperCase());
				ResultSet rs = pstmtDepUsr.executeQuery();
				while(rs.next()) {
					Deployment dep = new Deployment();
					dep.descricaoProjeto = rs.getString("descricao_projeto");
					dep.idProjeto = rs.getLong("proj_id");
					dep.nomeProjeto = rs.getString("nome_projeto");
					dep.setDhPublicacao(rs.getDate("dh_publicacao"));
					dep.projetoAplicacao = rs.getInt("aplicacao_projeto");
					dep.setId(rs.getLong("dep_id"));
					dep.setNomeProcessoNegocio(rs.getString("nome_processo_negocio"));
					dep.setNomeAtividadeNegocio(rs.getString("nome_atividade_negocio"));
					dep.setAtivo(rs.getBoolean("ativo"));
					dep.setRemovido(rs.getBoolean("removido"));
					dep.setProjetoMensagem(rs.getBoolean("dep_mensagem"));
        			try {
						getUltimaDataAtualizacao(dep, connection);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					lstDeployments.add(dep);
				}
			}
		});
		
		
		// recupera deployments por grupo
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				PreparedStatement pstmtDepUsr = connection.prepareStatement("select d.id as dep_id, p.id as proj_id, d.nome_processo_negocio, d.nome_atividade_negocio, d.ativo, d.removido, d.dep_mensagem,"
						+ "p.nome as nome_projeto, p.descricao as descricao_projeto, p.aplicacao as aplicacao_projeto "
						+ "from deployment d "
						+ "inner join projeto p on d.projeto_id = p.id "
						+ "inner join deployment_grupo dg on d.id = dg.deploymentid "
						+ "where dg.grupoid in (select grupoid from usuario_grupo where usuarioid = (select id from usuario where upper(login) = ?))");
				pstmtDepUsr.setString(1, login.toUpperCase());
				ResultSet rs = pstmtDepUsr.executeQuery();
				while(rs.next()) {
					Deployment dep = new Deployment();
					dep.descricaoProjeto = rs.getString("descricao_projeto");
					dep.idProjeto = rs.getLong("proj_id");
					dep.nomeProjeto = rs.getString("nome_projeto");
					dep.projetoAplicacao = rs.getInt("aplicacao_projeto");
					dep.setId(rs.getLong("dep_id"));
					dep.setNomeProcessoNegocio(rs.getString("nome_processo_negocio"));
					dep.setNomeAtividadeNegocio(rs.getString("nome_atividade_negocio"));
					dep.setAtivo(rs.getBoolean("ativo"));
					dep.setRemovido(rs.getBoolean("removido"));
					dep.setProjetoMensagem(rs.getBoolean("dep_mensagem"));
        			try {
						getUltimaDataAtualizacao(dep, connection);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					lstDeployments.add(dep);
				}
			}
		});		

		// recupera deployments por projeto BPM
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				PreparedStatement pstmt = connection.prepareStatement("select d.id as dep_id, p.id as proj_id, d.nome_processo_negocio, d.nome_atividade_negocio, d.ativo, d.removido, d.dep_mensagem,"
						+ "p.nome as nome_projeto, p.descricao as descricao_projeto, p.aplicacao as aplicacao_projeto from deployment d inner join projeto p on d.projeto_id = p.id and nome_processo_negocio is not null");
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()) {
					Deployment dep = new Deployment();
					dep.descricaoProjeto = rs.getString("descricao_projeto");
					dep.idProjeto = rs.getLong("proj_id");
					dep.nomeProjeto = rs.getString("nome_projeto");
					dep.projetoAplicacao = rs.getInt("aplicacao_projeto");
					dep.setId(rs.getLong("dep_id"));
					dep.setNomeProcessoNegocio(rs.getString("nome_processo_negocio"));
					dep.setNomeAtividadeNegocio(rs.getString("nome_atividade_negocio"));
					dep.setAtivo(rs.getBoolean("ativo"));
					dep.setRemovido(rs.getBoolean("removido"));
					dep.setProjetoMensagem(rs.getBoolean("dep_mensagem"));
        			try {
						getUltimaDataAtualizacao(dep, connection);
					} catch (ParseException e) {
						e.printStackTrace();
					}
        			lstDeployments.add(dep);
				}
			}
		});
		
		// recupera informações sobre os grupos e sobre os usuários habilitados ao deployment
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				PreparedStatement stmtGrupo = connection.prepareStatement("select g.id, g.nome, g.descricao, g.dn from deployment_grupo dg inner join grupo g on g.id = dg.grupoid where dg.deploymentid = ?");
				PreparedStatement stmtUsuario = connection.prepareStatement("select u.id, u.nome, u.login, u.ativo, u.dn, u.uid from deployment_usuario du inner join usuario u on du.usuarioid = u.id where du.deploymentid = ?");
				PreparedStatement stmtUsuarioGrupo = connection.prepareStatement("select u.id, u.nome, u.login, u.ativo, u.dn, u.uid from usuario_grupo ug inner join usuario u on u.id = ug.usuarioid where ug.grupoid = ?");
				for(Deployment dep : lstDeployments) {
					stmtGrupo.clearParameters();
					stmtGrupo.setLong(1, dep.getId());
					ResultSet rs = stmtGrupo.executeQuery();
					while(rs.next()) {
						Grupo grp = new Grupo();
						grp.setDn(rs.getString("dn"));
						grp.setDescricao(rs.getString("descricao"));
						grp.setId(rs.getLong("id"));
						grp.setNome(rs.getString("nome"));
						
						stmtUsuarioGrupo.clearParameters();
						stmtUsuarioGrupo.setLong(1, grp.getId());
						ResultSet rsUsr = stmtUsuarioGrupo.executeQuery();
						while(rsUsr.next()) {
							Usuario usr = new Usuario();
							usr.setAtivo(rsUsr.getBoolean("ativo"));
							usr.setDn(rsUsr.getString("dn"));
							usr.setId(rsUsr.getLong("id"));
							usr.setLogin(rsUsr.getString("login"));
							usr.setNome(rsUsr.getString("nome"));
							usr.setUid(rsUsr.getString("uid"));
							grp.getUsuarios().add(usr);
						}
						dep.getGruposPossiveis().add(grp);
					}
					rs.close();
					
					stmtUsuario.clearParameters();
					stmtUsuario.setLong(1, dep.getId());
					rs = stmtUsuario.executeQuery();
					while(rs.next()) {
						Usuario usr = new Usuario();
						usr.setAtivo(rs.getBoolean("ativo"));
						usr.setDn(rs.getString("dn"));
						usr.setId(rs.getLong("id"));
						usr.setLogin(rs.getString("login"));
						usr.setNome(rs.getString("nome"));
						usr.setUid(rs.getString("uid"));
						dep.getUsuariosPossiveis().add(usr);
					}
				}
			}
		});
		
		return lstDeployments;
	}
	
	public List<Long> getDeploymentIdsByGroupName(final String groupName) {
		if(StringUtils.isEmpty(groupName)) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_ID_INVALIDO);
		}
		final List<Long> deps = new ArrayList<Long>();
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String qDeps = "select dg.* from deployment_grupo dg where dg.grupoid = (select g.id from grupo g where g.nome = ?)";
				PreparedStatement pstmt = connection.prepareStatement(qDeps);
				pstmt.setString(1, groupName);
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()) {
					deps.add(rs.getLong("deploymentid"));
				}
			}
		});
		return deps;
	}
	
	public List<Deployment> findDeploymentByBPInstance(Long bpId) {
		if (bpId == null || bpId <= 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_ID_INVALIDO);
		}
		
		List<Deployment> lstDeployments = new ArrayList<Deployment>();
		
		try {
			List deployments = hibernateTemplate.find("from Deployment u where u.instancias.id = ? ", bpId);
			lstDeployments.addAll(deployments);
        } catch (Exception e) {
	        e.printStackTrace();
	        throw new ParametroException("Erro ao recuperar deployments para o processo de negócio passado", e);
        }
		
		return lstDeployments;
	}	
	
	public List<ProjetoImplantadoVO> findInfoDeploymentsProjet() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		WorkProjetosImplantados work = new WorkProjetosImplantados();
        session.doWork(work);
        
        return work.listaPrjImplantados;
		
	}
	
	public List<InstanciaVO> findInstanciasPorProjeto(Long projetoId) {
		if (projetoId == null || projetoId <= 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_ID_INVALIDO);
		}

		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		WorkInstancias work = new WorkInstancias();
		work.projetoId = projetoId;
        session.doWork(work);
		
		return work.instancias;
	}
	
	public ValorDataview findValoresDataviewPorId(Long idDataview) {
		if (idDataview == null || idDataview <= 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_ID_INVALIDO);
		}
		ValorDataview valor = hibernateTemplate.get(ValorDataview.class, idDataview);
		valor.getRows().removeAll(Collections.singleton(null));
		return valor;
	}
	
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=30)	
	public void removeLinhaInstanciaDataview(Long linhaId) {
		if (linhaId == null || linhaId <= 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_ID_INVALIDO);
		}
		
		Linha linha = hibernateTemplate.get(Linha.class, linhaId);
		hibernateTemplate.delete(linha);
	}
	
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=30)	
	public void removeInstancia(Long instanciaId) {
		if (instanciaId == null || instanciaId <= 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_ID_INVALIDO);
		}
		Instancia instancia = hibernateTemplate.get(Instancia.class, instanciaId);
		hibernateTemplate.delete(instancia);		
	}
	
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=30)	
	public void alteraValoresDataview(ValorDataview valorDataview) {
		if (valorDataview == null || valorDataview.getId() == null || valorDataview.getId() <= 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_ID_INVALIDO);
		}		
		ValorDataview rec = hibernateTemplate.get(ValorDataview.class, valorDataview.getId());
		if (rec == null) {
			throw new ParametroException("Não foi possível recuperar campo de dados", 1);
		}
		rec.getRows().clear();
		hibernateTemplate.persist(rec);

		int idxNumLinha = 1;
		for(Linha linha : valorDataview.getRows()) {
			linha.setNumero(idxNumLinha++);			
		}		
		
		for (Linha linha : valorDataview.getRows()) {
			linha.setId(null);
			for(Campo campo : linha.getCampos()) {
				campo.setId(null);
			}
			rec.getRows().add(linha);
		}
		
		hibernateTemplate.save(rec);
	}
	
	public void alteraValoresInstancias(InstanciasVO instancias) {
		if (instancias == null || instancias.getInstancias().size() == 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_ID_INVALIDO);
		}		
		for(InstanciaVO instVo : instancias.getInstancias()) {
			Instancia instTmp = new Instancia(); 
			if (instVo.getId() != null) {
	            instTmp = hibernateTemplate.get(Instancia.class, instVo.getId());
	            if (instTmp == null) {
	            	continue;
	            }
				instTmp.setDhAlteracaoP(new Date());
            } else {
            	instTmp.setDhCriacaoP(instVo.getDhCriacao());
            	instTmp.setProjeto(hibernateTemplate.get(Projeto.class, instVo.getProjetoId()));
            	Usuario usuario = (Usuario)hibernateTemplate.find("from Usuario u where upper(u.login) = ?", instVo.getUserLogin().toUpperCase()).get(0);
            	instTmp.setUsuario(usuario);
            }
			instTmp.getValoresFormulario().clear();
			int idx = 1;
			for(ValorFormularioVO vlrFrm : instVo.getValoresFormulario()) {
				ValorFormulario vlr = new ValorFormulario();
				vlr.setValor(vlrFrm.getValor());
				Component component = (Component)hibernateTemplate.find("from Component c where c.id = ?", vlrFrm.getComponentId()).get(0); 
				vlr.setComponente(component);
				instTmp.getValoresFormulario().add(vlr);
			}
			
			Usuario usr = (Usuario)hibernateTemplate.find("from Usuario u where upper(u.login) = ?", instancias.getLoginUsuarioAlteracao().toUpperCase()).get(0);
			Historico hist = new Historico();
			hist.setCodigo(502);
			hist.setDescricao("Alteracao instancia");
			hist.setDhAlteracao(new Date());
			hist.setUsuario(usr);
			usr.getHistoricos().add(hist);				
			instTmp.getHistoricos().add(hist);				
			
			hibernateTemplate.saveOrUpdate(instTmp);
		}
	}
	
	
	public InstanciaVO novaInstanciaParaProjeto(Long projetoId, String userLogin) {
		if (projetoId == null || projetoId <= 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_ID_INVALIDO);
		}
		if (userLogin == null || userLogin.trim().length() == 0) {
			throw new ParametroException("Login de usuário deve ser válido", IMensagensErro.PARAMETRO_ID_INVALIDO);
		}
		InstanciaVO inst = new InstanciaVO();
		Projeto prj = hibernateTemplate.get(Projeto.class, projetoId);
		Instancia instOriginal = prj.criaNovaInstanciaVazia();
		Usuario usr = (Usuario)hibernateTemplate.find("from Usuario u where upper(u.login) = ?", userLogin.toUpperCase()).get(0);
		instOriginal.setUsuario(usr);
		
		Historico hist = new Historico();
		hist.setCodigo(505);
		hist.setDescricao("Instancia criada");
		hist.setDhAlteracao(new Date());
		hist.setUsuario(usr);
		usr.getHistoricos().add(hist);				
		instOriginal.getHistoricos().add(hist);	
		
		hibernateTemplate.saveOrUpdate(instOriginal);
		inst = montaInstancia(instOriginal);
		
		return inst;
	}
	
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=30)		
	public ValorDataview novaInstanciaLinhaDataview(Long dataviewId) {
		if (dataviewId == null || dataviewId <= 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_ID_INVALIDO);
		}
		ValorDataviewVO vo = new ValorDataviewVO();
		
		ValorDataview vlrDtv = hibernateTemplate.get(ValorDataview.class, dataviewId);
		DataView dtv = (DataView)vlrDtv.getDataView();
		if (dtv.getDataViewStore() == null ) {
			throw new ParametroException("O campo dataview store deve ser preenchido");
		}
		List<String> camposDataview;
        try {
	        camposDataview = DataViewStructure.getFieldsDataview(new String(dtv.getDataViewStore()));
        } catch (Exception e) {
        	throw new ParametroException("Erro ao realizar parse na estrutura do dataview store");
        }
		
		Linha linha = new Linha();
		int pos = 0;
		for(Linha ln : vlrDtv.getRows()) {
			if(ln.getNumero() > pos) {
				pos = ln.getNumero().intValue();
			}
		}
		linha.setNumero(pos+1);
		
		for(String cmp : camposDataview) {
			Campo novoCmp = new Campo();
			novoCmp.setNomeCampo(cmp);
			linha.getCampos().add(novoCmp);
		}
		vlrDtv.getRows().add(linha);
		
		hibernateTemplate.saveOrUpdate(vlrDtv);
		
		return vlrDtv;
	}

//	private LinhaVO montaLinhaDataview(Linha linha) {
//		LinhaVO novaLinha = new LinhaVO();
//		novaLinha.setNumero(linha.getNumero().intValue());
//		for(Campo cmp : linha.getCampos()) {
//			CampoVO novoCampo = new CampoVO();
//			novoCampo.setId(cmp.getId());
//			novoCampo.setNomeCampo(cmp.getNomeCampo());
//			novoCampo.setValorCampo(cmp.getValorCampo());
//			novaLinha.getCampos().add(novoCampo);
//		}
//		return novaLinha;
//	}
	
	private InstanciaVO montaInstanciaExemplo(Long projetoId, String userLogin) {
		if (projetoId == null || projetoId <= 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_ID_INVALIDO);
		}
		if (userLogin == null || userLogin.trim().length() == 0) {
			throw new ParametroException("Login de usuário deve ser válido", IMensagensErro.PARAMETRO_ID_INVALIDO);
		}
		InstanciaVO inst = new InstanciaVO();
		Projeto prj = hibernateTemplate.get(Projeto.class, projetoId);
		Instancia instOriginal = prj.criaNovaInstanciaVazia();
		Usuario usr = (Usuario)hibernateTemplate.find("from Usuario u where upper(u.login) = ?", userLogin.toUpperCase()).get(0);
		instOriginal.setUsuario(usr);
		
		inst = montaInstancia(instOriginal);
		
		return inst;
		
	}
	
	private InstanciaVO montaInstancia(Instancia inst) {
		InstanciaVO vo = new InstanciaVO();
		vo.setDhCriacao(new Date());
		vo.setId(inst.getId());
		vo.setProjetoId(inst.getProjeto().getId());
		vo.setUserLogin(inst.getUsuario().getLogin());
		vo.setUserName(inst.getUsuario().getNome());

		for(ValorFormulario vlr : inst.getValoresFormulario()) {
			 ValorFormularioVO frmVo = new ValorFormularioVO();
			 frmVo.setComponentId(vlr.getComponente().getId());
			 frmVo.setFieldId(vlr.getFieldId());
			 frmVo.setFieldType(vlr.getComponente().getFieldType());
			 frmVo.setValor(vlr.getValor());
			 frmVo.setId(vlr.getId());
			 vo.getValoresFormulario().add(frmVo);
		}
		for(ValorDataview vlr : inst.getValoresDataview()) {
			ValorDataviewVO dtvVo = new ValorDataviewVO();
			dtvVo.setComponentId(vlr.getDataView().getId());
			dtvVo.setFieldId(vlr.getFieldId());
			dtvVo.setFieldType(vlr.getDataView().getFieldType());
			dtvVo.setFieldLabel(vlr.getFieldId());
			dtvVo.setId(vlr.getId());
			dtvVo.setQtdLinhas(0);
			vo.getValoresDataview().add(dtvVo);
		}
		
		return vo;
	}
	
	private void getUltimaDataAtualizacao(Deployment dep, Connection conn) throws ParseException, SQLException {
	    DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
	    Date dt = fmt.parse("01/01/1900");
	    
	    PreparedStatement pstmt = conn.prepareStatement("select max(dh_alteracao) from historico where projeto_id = ?");
	    if (dep.getProjeto() != null) {
			pstmt.setLong(1, dep.getProjeto().getId());
		} else {
			pstmt.setLong(1, dep.idProjeto);
		}
		ResultSet rs = pstmt.executeQuery();
	    
	    if(rs.next()) {
	    	dt = rs.getTimestamp(1);
	    }
	    
	    dep.dhUltimaAtualizacao = dt.getTime();
    }
	
	private void getUltimaDataAtualizacao(final Deployment dep) throws ParseException {
	    DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
	    final Date dt = fmt.parse("01/01/1900");
	    
	    
	    Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
	    session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
			    PreparedStatement pstmt = connection.prepareStatement("select max(dh_alteracao) from historico where projeto_id = ?");
			    pstmt.setLong(1, dep.getProjeto().getId());
			    ResultSet rs = pstmt.executeQuery();
			    
			    if(rs.next()) {
			    	dt.setTime(rs.getTimestamp(1).getTime());
			    }				
			}
		});
	    
	    
	    dep.dhUltimaAtualizacao = dt.getTime();
    }	
	

}
