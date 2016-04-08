package br.com.laminarsoft.jazzforms.persistencia.test.dao;

import java.util.List;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import br.com.laminarsoft.jazzforms.persistencia.model.Pagina;
import br.com.laminarsoft.jazzforms.persistencia.test.TesteBase;

@Ignore
public class PaginaDaoTest extends TesteBase {

	@Test
	public void getByIdTest() {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> teste recupera p�gina por id <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		Pagina pagina = paginaDao.findById(1l);
		Assert.assertTrue("A p�gina desejada n�o foi encontrada", pagina != null && pagina.getId() == 1l);
	}
	
	@Test
	public void getByProjetoIdTest() {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> teste recupera p�gina por projeto id <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		List<Pagina> paginas = paginaDao.findByProjetoId(1l);
		Assert.assertTrue("A p�gina desejada n�o foi encontrada", paginas != null && paginas.size() > 0);
	}
}
