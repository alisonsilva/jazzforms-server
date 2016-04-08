package br.com.laminarsoft.jazzforms.persistencia.test.dao;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Icon;
import br.com.laminarsoft.jazzforms.persistencia.test.TesteBase;

public class IconesDaoTest extends TesteBase {

	@Test
	public void getAllWithNames() {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> teste todos icones com nomes <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		List<Icon> icones = iconDao.findAllWithNames();
		Assert.assertTrue("Forma recuperados todos os icones", icones.size() < 300);
	}
}
