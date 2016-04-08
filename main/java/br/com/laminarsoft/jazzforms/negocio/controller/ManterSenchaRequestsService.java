package br.com.laminarsoft.jazzforms.negocio.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.laminarsoft.jazzforms.persistencia.dao.AlertaDao;
import br.com.laminarsoft.jazzforms.persistencia.dao.DeploymentDao;
import br.com.laminarsoft.jazzforms.persistencia.dao.EquipamentoDao;
import br.com.laminarsoft.jazzforms.persistencia.dao.InstanciaDao;
import br.com.laminarsoft.jazzforms.persistencia.dao.LandDao;
import br.com.laminarsoft.jazzforms.persistencia.dao.MensagemDao;
import br.com.laminarsoft.jazzforms.persistencia.dao.ProcessModelDao;
import br.com.laminarsoft.jazzforms.persistencia.dao.UsuarioDao;
import br.com.laminarsoft.jazzforms.persistencia.model.Deployment;
import br.com.laminarsoft.jazzforms.persistencia.model.Grupo;
import br.com.laminarsoft.jazzforms.persistencia.model.Instancia;
import br.com.laminarsoft.jazzforms.persistencia.model.Land;
import br.com.laminarsoft.jazzforms.persistencia.model.LandEntry;
import br.com.laminarsoft.jazzforms.persistencia.model.Usuario;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.ParametroException;
import br.com.laminarsoft.jazzforms.persistencia.model.util.InfoRetornoMensagem;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.AlertaVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.EquipamentoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.GrupoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.InstanciaVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LandEntryVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LandVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LdapUsuarioVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LocalizacaoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.MensagemVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.RequisicaoAtualizacaoLocalizacaoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.RequisicaoAtualizacoesVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.SugestaoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.TipoSugestaoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.UsuarioAlertaVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.UsuarioVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.ValorBPInstanceVO;


@Service(value="senchaService")
@SuppressWarnings("all")
@Transactional(propagation=Propagation.SUPPORTS, 
		isolation=Isolation.READ_UNCOMMITTED, 
		timeout=30)
public class ManterSenchaRequestsService {
	private static final String OBJ_SYNC = "objeto de sincronização";
	private static final String NOME_CACHE_USRUPDT = "usuarios_para_update";
	
	@Autowired private CacheManager cacheManager;
	
	@Autowired private UsuarioDao usuarioDao;	
	@Autowired private DeploymentDao deploymentDao;	
	@Autowired private InstanciaDao instanciaDao;
	@Autowired private LandDao landDao;
	@Autowired private MensagemDao mensagemDao;
	@Autowired private ProcessModelDao processmodelDao;
	@Autowired private EquipamentoDao equipamentoDao;
	@Autowired private AlertaDao alertaDao;
	
	public Usuario findByLogin(String login) throws ParametroException {
		List<Usuario> usuarios = usuarioDao.findByLogin(login);
		Usuario ret = null;
		if (usuarios.size() > 0) {
			ret = usuarios.get(0);
		}
		return ret;
	}
	
	public List<Deployment> findDeploymentsByLogin(String login) {
		List<Deployment> lstDeployments = deploymentDao.findDeploymentsByLogin(login);
		return lstDeployments;
	}
	
	public List<Deployment> findDeploymentByBPInstance(Long bpid) {
		List<Deployment> lstDeployments = deploymentDao.findDeploymentByBPInstance(bpid);
		return lstDeployments;
	}
	
	public UsuarioVO getPermissoesByLogin(String login) {
		return usuarioDao.getPermissoesUsuarioPorLogin(login);
	}
	
	public Deployment getDeploymentById(long id) {
		return deploymentDao.getDeploymentById(id);
	}
	
	public Usuario getUsuarioPorLogin(String login) {
		return usuarioDao.getInfoUsuarioComGrupos(login);
	}
	
	public LdapUsuarioVO getInfoUsuario(String login) {
		return usuarioDao.getInfoUsuario(login);
	}
	
	public int getSituacaoDeployment(long deploymentId) {
		return deploymentDao.getSituacaoDeployment(deploymentId);
	}
	
	public void persistInstancias(Instancia[] instancias) {
		instanciaDao.persistirInstancias(instancias);
	}
	
	public Long getIdUsuario(Usuario usuario) {
		return  usuarioDao.getIdUsuario(usuario).getId();
	}
	
	public void atualizarRelacionamentosUsuario(LdapUsuarioVO usuario) {
		Cache cache = cacheManager.getCache(NOME_CACHE_USRUPDT);
		cache.put(usuario.getLogin(), usuario);
	}
	
	public void mantemGruposUsuario(Usuario usuario) {
		usuarioDao.mantemGruposUsuario(usuario);
	}
	
	public List<MensagemVO> getMensagensPorUsuario(String login, String equipamentoUUID, Long dataUltimaMensagem) {
		return mensagemDao.getMensagensPorUsuario(login, equipamentoUUID, dataUltimaMensagem);
	}
	
	public List<ValorBPInstanceVO> getValoresBPInstance(Long bpInstanceId){
		return processmodelDao.getValoresBPInstance(bpInstanceId);
	}
	
	public InfoRetornoMensagem removeMensagemUsuario(Long idMensagem, String loginUsuario) {
		return mensagemDao.removeMensagemUsuario(idMensagem, loginUsuario);
	}
	
	public void atribuiTarefaMensagem(Long mensagemId, String loginUsuario) {
		mensagemDao.atribuiTarefaMensagem(mensagemId, loginUsuario);
	}
	
	public String isTarefaAtribuida(Long mensagemId) {
		return mensagemDao.isTarefaAtribuida(mensagemId);
	}
	
	public void persistInfoEquipamento(EquipamentoVO equipamento) {
		equipamentoDao.persistInfoEquipamento(equipamento);
	}
	
	public Integer getAtualizacoesUsuario(RequisicaoAtualizacoesVO vo) {
		Integer ret = 0;
		if(vo.deployments == null || vo.deployments.size() == 0) {
			ret = usuarioDao.getQtdMensagensNovasUsuario(vo);
		} else {
			equipamentoDao.persistInfoEquipamento(vo.aparelho);
			ret = usuarioDao.getQtdDeploymentsAtualizadosUsuario(vo);
		}
		return ret;
	}
	
	public void novaSugestao(SugestaoVO sugestao) {
		usuarioDao.novaSugestao(sugestao);
	}
	
	public List<TipoSugestaoVO> getTiposSugestao() {
		return usuarioDao.getTiposSugestao();
	}
	
	public void atualizaLocalizacaoAparelho(RequisicaoAtualizacaoLocalizacaoVO vo) {
		for(LocalizacaoVO localizacao : vo.localizacoes) {
			EquipamentoVO equip = new EquipamentoVO();
			equip.setDeviceName(vo.deviceName);
			equip.setDevicePlatform(vo.devicePlatform);
			equip.setDeviceUUID(vo.deviceUUID);
			equip.setDhEvento(localizacao.dhEvento);
			equip.setLatitude(localizacao.latitude);
			equip.setLongitude(localizacao.longitude);
			equip.setLoginUsuario(vo.login);
			equipamentoDao.persistInfoEquipamento(equip);
		}
	}
	
	public List<InstanciaVO> getInstanciasReenviadas(String loginUsuario) {
		return instanciaDao.getInstanciasReenviadas(loginUsuario);
	}
	
	public void reportProjetoInexistente(Long idInstancia, String loginUsuario) {
		instanciaDao.reportProjetoInexistente(idInstancia, loginUsuario);
	}
	
	public boolean isUsuarioAtivo(String loginUsuario) {
		return usuarioDao.isUsuarioAtivo(loginUsuario);
	}
	
	public AlertaVO addAlerta(AlertaVO alerta) {
		return alertaDao.addAlerta(alerta);
	}
	
	public List<AlertaVO> getAlertasUsuarioNaoEnviados(String loginUsuario) {
		return alertaDao.getAlertasUsuarioNaoEnviados(loginUsuario);
	}
	
	public void removeAlerta(Long alertaId) {
		alertaDao.removeAlerta(alertaId);
	}
	
	public void addUsuarioAlerta(UsuarioAlertaVO usrAlerta) {
		alertaDao.addUsuarioAlerta(usrAlerta);
	}
	
	public List<UsuarioAlertaVO> getTodosUsuariosAlerta() {
		return alertaDao.getTodosUsuariosAlerta();
	}
	
	public List<UsuarioAlertaVO> getUsuarioAlerta(String login) {
		return alertaDao.getUsuarioAlerta(login);
	}
	
	public List<LandVO> getLandsPorUsuario(String login) {
		List<Land> lands = landDao.getLandEntriesPorUsuario(login);
		List<LandVO> landVos = new ArrayList<LandVO>();
		
		for(Land land : lands) {
			LandVO landVo = new LandVO();
			landVo.categoria = land.getCategoria();
			landVo.dhInclusao = land.getDhInclusao().getTime();
			landVo.iconCls = land.getIconCls();
			landVo.id = land.getId();
			for(Grupo grupo : land.getGrupos()) {
				GrupoVO grpVo = new GrupoVO();
				grpVo.id = grupo.getId();
				grpVo.nome = grupo.getNome();
				grpVo.descricao = grupo.getDescricao();
				landVo.grupos.add(grpVo);
			}
			
			landVo.url = land.getUrl();
			landVo.nomeFonte = land.getNomeFonte();
			if(land.getEntries().size() > 0) {
				landVo.entries = new ArrayList<LandEntryVO>();
			}
			for(LandEntry entry : land.getEntries()) {
				LandEntryVO entryVo = new LandEntryVO();
				entryVo.titulo = entry.getTitulo();
				entryVo.texto = entry.getTexto();
				entryVo.url = entry.getUrl();
				entryVo.dhInclusao = entry.getDhInclusao().getTime();
				entryVo.abrirUrlDiretamente = entry.getAbrirUrlDiretamente();
				entryVo.icone = null;
				entryVo.id = entry.getId();
				entryVo.iconeTipo = entry.getIconeTipo();
				entryVo.iconeUrl = entry.getIconeUrl();
				entryVo.categoria = land.getCategoria();
				entryVo.nomeFonte = land.getNomeFonte();
				landVo.entries.add(entryVo);
			}
			landVos.add(landVo);
		}
		
		return landVos;
	}
	

}
