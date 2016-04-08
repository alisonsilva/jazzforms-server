package br.com.laminarsoft.jazzforms.negocio.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tmatesoft.svn.core.SVNException;

import br.com.laminarsoft.jazzforms.logging.types.MessageData;
import br.com.laminarsoft.jazzforms.negocio.controller.util.IProjetoController;
import br.com.laminarsoft.jazzforms.negocio.controller.util.PropertiesServiceController;
import br.com.laminarsoft.jazzforms.negocio.controller.util.SVNController;
import br.com.laminarsoft.jazzforms.persistencia.dao.AlertaDao;
import br.com.laminarsoft.jazzforms.persistencia.dao.DeploymentDao;
import br.com.laminarsoft.jazzforms.persistencia.dao.PaginaDao;
import br.com.laminarsoft.jazzforms.persistencia.dao.ProjetoDao;
import br.com.laminarsoft.jazzforms.persistencia.model.Deployment;
import br.com.laminarsoft.jazzforms.persistencia.model.Projeto;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.ParametroException;

@Service(value="projetoService")
@SuppressWarnings("all")
@Transactional(propagation=Propagation.SUPPORTS, 
		isolation=Isolation.READ_UNCOMMITTED, 
		timeout=30)
public class ManterProjetoService {

	@Autowired private ProjetoDao projetoDao;	
	@Autowired private PaginaDao paginaDao;	
	@Autowired private AlertaDao alertaDao;
	@Autowired private DeploymentDao deploymentDao;
	@Autowired private PropertiesServiceController propertiesServiceController;
	@Autowired private SVNController svnController;
	
	
	private JAXBContext jaxbContext;
	
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=30,
			readOnly=false)
	public Projeto persist(Projeto projeto) throws ParametroException {
		if (projeto.getId() > 0) {
			projetoDao.update(projeto);
		} else {
			projeto.setDhCriacao(new Date());
			projeto.setId(projetoDao.persist(projeto));
		}
		if(projeto.getVersionControl() ) {
			try {
				if (propertiesServiceController == null) {
					propertiesServiceController = PropertiesServiceController.getInstance();
				}

				StringWriter writer = new StringWriter();
				if (jaxbContext == null) {
					jaxbContext = JAXBContext.newInstance(Projeto.class);
				}
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
				jaxbMarshaller.marshal(projeto, writer);
				
				String fileName = propertiesServiceController.getProperty(IProjetoController.SVN_LOCAL_REPO) + "/" + projeto.getNome() + ".xml";
				File nf = new File(fileName);
				if(!nf.exists()) {
					nf.createNewFile();
				}
				PrintWriter out = new PrintWriter(nf);
				out.println(writer.getBuffer().toString());
				out.flush();
				out.close();
				
				try {
					if (!svnController.isVersioned(nf)) {
						svnController.addEntry(nf);
					}
					svnController.commit(nf, false, projeto.getVersionControllDesc());
				} catch (SVNException e) {
					if(e.getErrorMessage().getErrorCode().getCode() == 155007) {
						try {
							svnController.addEntry(nf);
							svnController.commit(nf, false, projeto.getVersionControllDesc());
						} catch (SVNException e1) {
							e1.printStackTrace();
						}
					}
					e.printStackTrace();
				}
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (JAXBException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return projeto;
	}
	
	
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=30,
			readOnly=false)
	public Projeto deploy(Projeto projeto) throws ParametroException {
		Projeto prjdep = projetoDao.deploy(projeto);		
		alertaDao.enviaAlertaDeployment(projeto.getDeployment().getId(), "Serviço novo", "Há atualizações disponíveis para os serviços", MessageData.ATUALIZACAO_APLICACAO);
		return prjdep;
	}
	
	public boolean existeDeploymentAtivo(Long projectId) {
		return deploymentDao.existeDeploymentAtivo(projectId);
	}
	
	@Secured({"ROLE_ADMINISTRADORES", "ROLE_LAMINARSOFTADMIN"})
	public Projeto findById(Long id) throws ParametroException {
		return projetoDao.findById(id);
	}
	
	public String deactivatePublishedProject(Long id, Boolean flagActivate, String usuario) throws ParametroException {
		return projetoDao.deactivatePublishedProject(id, flagActivate, usuario);
	}
	
	public List<Projeto> findAllNoLimit() {
		return projetoDao.findAllNoLimit();
	}
	
	public void removeDeployment(Long deploymentId, String login) {
		deploymentDao.removeDeployment(deploymentId, login);
		alertaDao.enviaAlertaDeployment(deploymentId, "Serviço removido", "Há atualizações disponíveis para os serviços", MessageData.ATUALIZACAO_APLICACAO);
	}
	
	public List<Projeto> findAllPublished() {
		return projetoDao.findAllPublished();
	}	
	
	
	public Projeto refreshProjetoDeployment(Long deploymentId, String loginUsuario) {
		Projeto prj = null;
		try {
			prj = projetoDao.refreshProjetoDeployment(deploymentId, loginUsuario);
		} catch (Exception e) {
			e.printStackTrace();
		}
		alertaDao.enviaAlertaDeployment(deploymentId, "Serviço atualizado", "Há atualizações disponíveis para os serviços", MessageData.ATUALIZACAO_APLICACAO);
		return prj;
	}
	
	public Deployment getDeployById(Long deployId) {
		return deploymentDao.getDeploymentById(deployId);
	}
	
	public void alteraDestinatariosDeployment(Projeto projeto) {
		deploymentDao.alterarDestinatarios(projeto);
	}
}
