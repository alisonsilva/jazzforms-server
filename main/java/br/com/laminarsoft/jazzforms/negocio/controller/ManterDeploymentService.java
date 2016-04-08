package br.com.laminarsoft.jazzforms.negocio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.laminarsoft.jazzforms.persistencia.dao.DeploymentDao;
import br.com.laminarsoft.jazzforms.persistencia.dao.PaginaDao;
import br.com.laminarsoft.jazzforms.persistencia.dao.ProjetoDao;
import br.com.laminarsoft.jazzforms.persistencia.model.ValorDataview;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.InstanciaVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.InstanciasVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.ProjetoImplantadoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.ValorDataviewVO;

@Service(value="deploymentService")
@SuppressWarnings("all")
@Transactional(propagation=Propagation.SUPPORTS, 
		isolation=Isolation.READ_UNCOMMITTED, 
		timeout=30)
public class ManterDeploymentService {

	@Autowired private ProjetoDao projetoDao;
	
	@Autowired private PaginaDao paginaDao;
	
	@Autowired private DeploymentDao deploymentDao;
	
	public void removeDeploymentFisico(Long projetoId) {
		deploymentDao.removeDeploymentFisico(projetoId);
	}
	
	public List<ProjetoImplantadoVO> findInfoDeploymentsProjet() {
		return deploymentDao.findInfoDeploymentsProjet();
	}
	
	public List<InstanciaVO> findInstaniasPorProjeto(Long projetoId) {
		return deploymentDao.findInstanciasPorProjeto(projetoId);
	}
	
	public ValorDataview findValoresDataviewPorId(Long idDataview) {
		return deploymentDao.findValoresDataviewPorId(idDataview);
	}
	
	public void removeLinhaInstanciaDataview(Long linhaId) {
		deploymentDao.removeLinhaInstanciaDataview(linhaId);
	}
	
	public void removeInstancia(Long instanciaId) {
		deploymentDao.removeInstancia(instanciaId);
	}
	
	public void alteraValoresDataview(ValorDataview valorDataview) {
		deploymentDao.alteraValoresDataview(valorDataview);
	}
	
	public void alteraValoresInstancias(InstanciasVO instancias) {
		deploymentDao.alteraValoresInstancias(instancias);
	}
	
	public InstanciaVO novaInstanciaParaProjeto(Long projetoId, String loginUsuario) {
		return deploymentDao.novaInstanciaParaProjeto(projetoId, loginUsuario);
	}
	
	public ValorDataview novaInstanciaLinhaDataview(Long dataviewId) {
		return deploymentDao.novaInstanciaLinhaDataview(dataviewId);
	}
}
