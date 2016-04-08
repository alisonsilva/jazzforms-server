package br.com.laminarsoft.jazzforms.negocio.controller.task.backup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MantemBackupTask {
	@Autowired @Qualifier("mantemBackupWorker") private Runnable backupWorker;
	
	// cron: segundo(0-59) minuto(0-59) hora(0-23) dia_do_mes(1-31) mês(1-12 ou Jan-Dec) dia_da_semana(0-6 ou Sun-Sat)
	// roda às 6 e 15 horas de todos os dias 
	@Scheduled(cron="0 0 6,15 * * ?")
	public void atualizaCache() {
		backupWorker.run();
	}

}
