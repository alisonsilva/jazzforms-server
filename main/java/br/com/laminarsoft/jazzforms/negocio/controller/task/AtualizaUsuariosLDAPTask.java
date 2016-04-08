package br.com.laminarsoft.jazzforms.negocio.controller.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AtualizaUsuariosLDAPTask {
	@Autowired @Qualifier("conexaoManterUsuariosLdapWorker") private Runnable atualizaUsuariosWorker;
	
	//@Scheduled(cron="0 0 4,16 * * ?")
	@Scheduled(fixedDelay=43200000)//1000*60*60*12 - de 12 em 12 horas
	public void atualizaCache() {
		try {
			Thread.currentThread().sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		atualizaUsuariosWorker.run();
	}

}
