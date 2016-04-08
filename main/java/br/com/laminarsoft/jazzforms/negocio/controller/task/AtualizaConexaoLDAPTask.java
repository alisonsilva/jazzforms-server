package br.com.laminarsoft.jazzforms.negocio.controller.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AtualizaConexaoLDAPTask {
	@Autowired @Qualifier("conexaoLdapWorker") private Runnable conexaoLdapWorker;
	
	@Scheduled(fixedDelay=3600000)//1000*60*60*1 - de hora em hora
	public void atualizaCache() {
		try {
			Thread.currentThread().sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		conexaoLdapWorker.run();
	}

}
