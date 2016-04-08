package br.com.laminarsoft.jazzforms.negocio.controller.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MantemUsuariosGruposLocaisTask {
	@Autowired @Qualifier("mantemUsuariosGruposLocaisWorker") private Runnable cacheWorker;
	
	@Scheduled(fixedDelay=5000)
	public void atualizaCache() {
		cacheWorker.run();
	}

}
