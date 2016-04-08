package br.com.laminarsoft.jazzforms.persistencia.dao;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.laminarsoft.jazzforms.persistencia.model.Deployment;
import br.com.laminarsoft.jazzforms.persistencia.model.Foto;
import br.com.laminarsoft.jazzforms.persistencia.model.Grupo;
import br.com.laminarsoft.jazzforms.persistencia.model.Historico;
import br.com.laminarsoft.jazzforms.persistencia.model.Instancia;
import br.com.laminarsoft.jazzforms.persistencia.model.Projeto;
import br.com.laminarsoft.jazzforms.persistencia.model.Usuario;
import br.com.laminarsoft.jazzforms.persistencia.model.ValorDataview;
import br.com.laminarsoft.jazzforms.persistencia.model.ValorFormulario;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.IMensagensErro;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.ParametroException;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Component;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.CampoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.InstanciaVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LinhaVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.ValorDataviewVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.ValorFormularioVO;

@Transactional(propagation=Propagation.SUPPORTS, 
	isolation=Isolation.READ_UNCOMMITTED, timeout=30)
@Repository("InstanciaDao")
@SuppressWarnings("all")
public class InstanciaDao extends BaseDao<Instancia> {
	
	
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=30)	
	public void persistirInstancias(Instancia[] instancias) {
		if (instancias == null || instancias.length == 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_ID_INVALIDO);
		}
		for(int idx = 0; idx < instancias.length; idx++) {
			if (instancias[idx].getIdOriginal() != null) {
				Instancia instTmp = instancias[idx];
				
				Instancia instOriginal = hibernateTemplate.get(Instancia.class, instancias[idx].getIdOriginal());
				instOriginal.setDhAlteracaoP(instTmp.getDhAlteracaoP());
				instOriginal.setRetornada(false);
				Projeto prj = instOriginal.getProjeto();
				
				Usuario usr = (Usuario)hibernateTemplate.find("from Usuario u where upper(u.login) = ?", instTmp.getUserLogin().toUpperCase()).get(0);
				instOriginal.setUsuario(usr);
				instOriginal.setUsuarioRetorno(null);
				
				hibernateTemplate.deleteAll(instOriginal.getValoresDataview());
				hibernateTemplate.deleteAll(instOriginal.getValoresFormulario());
				
				instOriginal.setValoresDataview(new ArrayList<ValorDataview>());
				instOriginal.setValoresFormulario(new ArrayList<ValorFormulario>());
				
				Historico hist = new Historico();
				hist.setCodigo(502);
				hist.setDescricao("Instancia alterada");
				hist.setDhAlteracao(new Date());
				hist.setUsuario(usr);
				usr.getHistoricos().add(hist);				
				instOriginal.getHistoricos().add(hist);
				
				for(ValorDataview vlr : instTmp.getValoresDataview()) {
					Long idCmp = vlr.getIdOriginal();
					Component cmp = hibernateTemplate.get(Component.class, idCmp);
					vlr.setDataView(cmp);
					instOriginal.getValoresDataview().add(vlr);
				}
				for(ValorFormulario vlr : instTmp.getValoresFormulario()) {
					Long idCmp = vlr.getIdOriginal();
					Component cmp = hibernateTemplate.get(Component.class, idCmp);
					vlr.setComponente(cmp);
					instOriginal.getValoresFormulario().add(vlr);
				}
				hibernateTemplate.saveOrUpdate(instOriginal);
			} else {
				Projeto prj = hibernateTemplate.get(Projeto.class, instancias[idx].getProjetoId());
				Usuario usr = (Usuario)hibernateTemplate.find("from Usuario usr where upper(usr.login) = ?", instancias[idx].getUserLogin().toUpperCase()).get(0);
				instancias[idx].setProjeto(prj);
				instancias[idx].setUsuario(usr);
				for(int idxVlr = 0; idxVlr < instancias[idx].getValoresFormulario().size(); idxVlr++) {
					Component cmp = hibernateTemplate.get(Component.class, instancias[idx].getValoresFormulario().get(idxVlr).getIdOriginal());
					instancias[idx].getValoresFormulario().get(idxVlr).setComponente(cmp);
				}
				for(int idxDt = 0; idxDt < instancias[idx].getValoresDataview().size(); idxDt++) {
					Component cmp = hibernateTemplate.get(Component.class, instancias[idx].getValoresDataview().get(idxDt).getIdOriginal());
					instancias[idx].getValoresDataview().get(idxDt).setDataView(cmp);
				}
				for(int idxFoto = 0; idxFoto < instancias[idx].getFotos().size(); idxFoto++) {
					Foto foto = instancias[idx].getFotos().get(idxFoto);
					Component cmp = hibernateTemplate.get(Component.class, foto.getIdCameraOriginal());
					foto.setCamera(cmp);
					foto.setDhPicture(new Date(foto.getDhPictureLng()));
					foto.setPicture(foto.pictStr.getBytes());
				}
				
				Historico hist = new Historico();
				hist.setCodigo(501);
				hist.setDescricao("Instancia criada");
				hist.setDhAlteracao(new Date());
				hist.setUsuario(usr);
				usr.getHistoricos().add(hist);				
				instancias[idx].getHistoricos().add(hist);
				
				hibernateTemplate.save(instancias[idx]);
			}
		}
	}
	
	public void reenviarInstanciaUsuario(Long instanciaId, String loginUsuario, String loginUsuarioAlteracao) {
		if(instanciaId == null || instanciaId <= 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO + ": o identificador da instancia deve ser válido", IMensagensErro.PARAMETRO_ID_INVALIDO);
		}
		if (loginUsuario == null || loginUsuario.trim().length() == 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO + ": o login do usuário deve ser válido", IMensagensErro.PARAMETRO_ID_INVALIDO);
		}
		Instancia instancia = hibernateTemplate.get(Instancia.class, instanciaId);
		instancia.setRetornada(true);
		
		List usuarios = hibernateTemplate.find("from Usuario u where upper(u.login) = ?", loginUsuario.toUpperCase());
		if (usuarios == null || usuarios.size() == 0) {
			throw new ParametroException("O usuário especificado não foi encontrado");
		}
		Usuario usuario = (Usuario)usuarios.get(0);
		instancia.setUsuarioRetorno(usuario);

		Usuario usuarioAlteracao = (Usuario)hibernateTemplate.find("from Usuario u where upper(u.login) = ?", loginUsuarioAlteracao.toUpperCase()).get(0);
		
		Historico hist = new Historico();
		hist.setCodigo(503);
		hist.setDescricao("Instancia reenviada");
		hist.setDhAlteracao(new Date());
		hist.setUsuario(usuarioAlteracao);
		usuarioAlteracao.getHistoricos().add(hist);
		instancia.setUsuario(usuarioAlteracao);
		instancia.getHistoricos().add(hist);		
		
		hibernateTemplate.saveOrUpdate(instancia);
	}
	
	public void desfazerReenvioInstancia(Long instanciaId, String loginUsuarioAlteracao) {
		if(instanciaId == null || instanciaId <= 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO + ": o identificador da instancia deve ser válido", IMensagensErro.PARAMETRO_ID_INVALIDO);
		}
		Instancia instancia = hibernateTemplate.get(Instancia.class, instanciaId);
		instancia.setRetornada(false);
		instancia.setUsuarioRetorno(null);

		Usuario usuarioAlteracao = (Usuario)hibernateTemplate.find("from Usuario u where upper(u.login) = ?", loginUsuarioAlteracao.toUpperCase()).get(0);
		Historico hist = new Historico();
		hist.setCodigo(504);
		hist.setDescricao("Desfeito reenvio");
		hist.setDhAlteracao(new Date());
		hist.setUsuario(usuarioAlteracao);
		usuarioAlteracao.getHistoricos().add(hist);			
		instancia.setUsuario(usuarioAlteracao);
		instancia.getHistoricos().add(hist);		
		
		hibernateTemplate.saveOrUpdate(instancia);
	}
	
	
	public List<Usuario> usuariosPossiveisReenvioInstancia(Long instanciaId) {
		if(instanciaId == null || instanciaId <= 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO + ": o identificador da instancia deve ser válido", IMensagensErro.PARAMETRO_ID_INVALIDO);
		}

		Set<Usuario> usuarios = new HashSet<Usuario>();
		Instancia instancia = hibernateTemplate.get(Instancia.class, instanciaId);
		Projeto projeto = instancia.getProjeto();
		Deployment deployment = projeto.getDeployment();
		if (deployment == null) {
			Long depId = projeto.getDeploymentId();
			deployment = hibernateTemplate.get(Deployment.class, depId);
		}
		List<Usuario> usuariosDeployment = deployment.getUsuariosPossiveis();
		List<Grupo> gruposDeployment = deployment.getGruposPossiveis();
		usuarios.addAll(usuariosDeployment);

		for (Grupo grpDep : gruposDeployment) {
			usuarios.addAll(grpDep.getUsuarios());
		}	
		
		return Arrays.asList(usuarios.toArray(new Usuario[usuarios.size()]));
	}
	
	public void reportProjetoInexistente(Long idInstancia, String loginUsuario) {
		if(idInstancia == null || idInstancia == 0 || loginUsuario == null || loginUsuario.trim().length() == 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO + ": id instancia e login usuario devem ser validos", IMensagensErro.PARAMETRO_ID_INVALIDO);
		}
		Instancia instancia = hibernateTemplate.get(Instancia.class, idInstancia);
		instancia.setRetornada(false);
		
		Usuario usuario = (Usuario)hibernateTemplate.find("from Usuario u where upper(u.login) = ?", loginUsuario.toUpperCase()).get(0);
		Historico hist = new Historico();
		hist.setCodigo(505);
		hist.setDescricao("O projeto para a instancia retornada não está disponível para o usuário");
		hist.setDhAlteracao(new Date());
		hist.setUsuario(usuario);
		usuario.getHistoricos().add(hist);
		instancia.setUsuario(usuario);
		instancia.getHistoricos().add(hist);
	}
	
	public List<Foto> getFotosPorInstancia(final long instanciaId) {
		final List<Foto> fotos = new  ArrayList<Foto>();
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				Foto foto = new Foto();
				String slctFotos = "select picture, dh_picture, camera_id from foto where instancia_id = ?";
				PreparedStatement pstmt = connection.prepareStatement(slctFotos);
				pstmt.setLong(1, instanciaId);
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()) {
					foto.setIdCameraOriginal(rs.getLong("camera_id"));
					foto.setDhPictureLng(rs.getDate("dh_picture").getTime());
					Blob blobPict = rs.getBlob("picture");
					foto.setPictStr(new String(blobPict.getBytes(1l, (int)blobPict.length())));
					fotos.add(foto);
				}
			}
		});
		return fotos;
	}
	
	public List<InstanciaVO> getInstanciasReenviadas(final String loginUsuario) {
		if(loginUsuario == null || loginUsuario.trim().length() == 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO + ": o login do usuário deve ser válido", IMensagensErro.PARAMETRO_ID_INVALIDO);
		}
		final List<InstanciaVO> instancias = new ArrayList<InstanciaVO>();
		
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				InstanciaVO instVo = new InstanciaVO();
				String slctInstancia = "select id, usuario_retorno_id, usuario_id, projeto_id, dh_criacao, dh_alteracao, retornada "
						+ "from instancia "
						+ "where retornada = 1 and usuario_retorno_id = (select id from usuario where upper(login) = '" + loginUsuario.toUpperCase() + "')";
				ResultSet rs = connection.createStatement().executeQuery(slctInstancia);
				if(rs.next()) {
					instVo.setId(rs.getLong("id"));
					instVo.setIdUsuarioReenvio(rs.getLong("usuario_retorno_id"));
					instVo.setUserId(rs.getLong("usuario_id"));
					instVo.setProjetoId(rs.getLong("projeto_id"));
					instVo.setDhCriacao(rs.getTimestamp("dh_criacao"));
					instVo.setDhAlteracao(rs.getTimestamp("dh_alteracao"));
					instVo.setReenviado(rs.getBoolean("retornada"));
					
					String slctValorForm = "select valor.id, valor.sncha_componentid, valor.instanciaid, valor.valor, comp.discriminator "
							+ "from valor_formulario valor "
							+ "inner join sncha_component comp on comp.id = valor.sncha_componentid where instanciaid = " + instVo.getId();
					ResultSet rsValForm = connection.createStatement().executeQuery(slctValorForm);
					while(rsValForm.next()) {
						ValorFormularioVO valVo = new ValorFormularioVO();
						valVo.setId(rsValForm.getLong("id"));
						valVo.setComponentId(rsValForm.getLong("sncha_componentid"));
						valVo.setValor(rsValForm.getString("valor"));
						valVo.setFieldType(rsValForm.getString("discriminator"));
						instVo.getValoresFormulario().add(valVo);
					}
					
					String slctValorDtv = "select id, instancia_id, sncha_componentid from valor_dataview where instancia_id = " + instVo.getId();
					
					String slctLinha = "select linha.id as linha_id, valor_dataviewid, rownum, campo.id as campo_id, campo.nome_campo, campo.valor_campo "
							+ "from linha inner join campo on campo.linha_id = linha.id where linha.valor_dataviewid = ?";
					PreparedStatement pstmt = connection.prepareStatement(slctLinha); 
					ResultSet rsValDtv = connection.createStatement().executeQuery(slctValorDtv);
					while(rsValDtv.next()) {
						ValorDataviewVO valVo = new ValorDataviewVO();
						valVo.setId(rsValDtv.getLong("id"));
						valVo.setComponentId(rsValDtv.getLong("sncha_componentid"));
						valVo.setInstanciaId(rsValDtv.getLong("instancia_id"));	
						
						pstmt.clearParameters();
						pstmt.setLong(1, valVo.getId());
						Long linhaAtual = 0l;
						Long linhaTmp = 0l;
						ResultSet rsRows = pstmt.executeQuery();
						LinhaVO linha = null;
						while(rsRows.next()) {
							linhaAtual = rsRows.getLong("linha_id");
							if(linhaAtual.longValue() != linhaTmp.longValue()) {
								linha = new LinhaVO();
								valVo.getLinhas().add(linha);
								linhaTmp = linhaAtual;
							}
							linha.setNumero(rsRows.getInt("rownum"));
							CampoVO campo = new CampoVO();
							campo.setId(rsRows.getLong("campo_id"));
							campo.setNomeCampo(rsRows.getString("nome_campo"));
							campo.setValorCampo(rsRows.getString("valor_campo"));
							linha.getCampos().add(campo);
						}
						instVo.getValoresDataview().add(valVo);
					}
					instancias.add(instVo);
				}
			}
		});
		
		return instancias;
	}
}
