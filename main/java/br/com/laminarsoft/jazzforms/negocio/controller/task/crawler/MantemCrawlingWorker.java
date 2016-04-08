package br.com.laminarsoft.jazzforms.negocio.controller.task.crawler;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.laminarsoft.jazzforms.jazzcrawling.IJazzCrawling;
import br.com.laminarsoft.jazzforms.persistencia.dao.LandDao;
import br.com.laminarsoft.jazzforms.persistencia.model.Land;

@Component("mantemCrawlingWorker")
@SuppressWarnings("all")
public class MantemCrawlingWorker implements Runnable {
	
	@Autowired private LandDao landDao;
	
	@Override
	public void run() {
		List<Land> lands = landDao.getLandsToCrawl();
		for(Land land : lands) {
			if(StringUtils.isNotEmpty(land.getClasseManipulacao())) {
				try {
					Class classe = Class.forName(land.getClasseManipulacao());
					IJazzCrawling crawling = (IJazzCrawling)(classe.newInstance());
					crawling.setTextMaxLength(7000);// cumprimento máximo do texto dos artigos
					Land newLand = crawling.getEntries(land);
					landDao.gravaLandEntries(newLand);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
