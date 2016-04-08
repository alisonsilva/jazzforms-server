package br.com.laminarsoft.jazzforms.negocio.controller.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.laminarsoft.jazzforms.negocio.controller.util.LogController;
import br.com.laminarsoft.jazzforms.persistencia.dao.DeploymentDao;
import br.com.laminarsoft.jazzforms.persistencia.dao.ProjetoDao;

@Component("cacheWorker")
public class CacheManagerWorker implements Runnable {
	@Autowired private LogController logController;
	@Autowired private ProjetoDao projetoDao;
	@Autowired private DeploymentDao deploymentDao;

	@Override
	public void run() {
		projetoDao.restoreCacheProjeto();
//		deploymentDao.restoreCacheDeployment();
	}

}
