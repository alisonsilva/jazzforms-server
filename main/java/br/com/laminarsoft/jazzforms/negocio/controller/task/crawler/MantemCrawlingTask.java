package br.com.laminarsoft.jazzforms.negocio.controller.task.crawler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MantemCrawlingTask {
	@Autowired @Qualifier("mantemCrawlingWorker") private Runnable crawlingWorker;
	
	// a cada 24 horas
	//@Scheduled(fixedDelay=10800000)
	
	// cron: minuto(0-59) hora(0-23) dia_do_mes(1-31) mês(1-12 ou Jan-Dec) dia_da_semana(0-6 ou Sun-Sat)
	// roda às 10, 12, 14, 16 horas de todos os dias ("0 0 8,10,12,14,16,18 * * ?")
	//4*60*60*1000
	@Scheduled(fixedDelay=14400000)	
	public void atualizaCache() {
		crawlingWorker.run();
	}

}
