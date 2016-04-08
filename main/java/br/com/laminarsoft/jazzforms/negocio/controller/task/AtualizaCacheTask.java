package br.com.laminarsoft.jazzforms.negocio.controller.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AtualizaCacheTask {
	@Autowired @Qualifier("cacheWorker") private Runnable cacheWorker;
	
	@Scheduled(fixedDelay=3600000)
	public void atualizaCache() {
		try {
			Thread.currentThread().sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		cacheWorker.run();
	}

}
