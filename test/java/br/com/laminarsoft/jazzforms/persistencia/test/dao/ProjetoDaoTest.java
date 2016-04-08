package br.com.laminarsoft.jazzforms.persistencia.test.dao;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

import br.com.laminarsoft.jazzforms.persistencia.model.Pagina;
import br.com.laminarsoft.jazzforms.persistencia.model.Projeto;
import br.com.laminarsoft.jazzforms.persistencia.test.TesteBase;


@SuppressWarnings("all")
public class ProjetoDaoTest extends TesteBase {

	@Test
	public void insereProjeto() {
		Projeto projeto = new Projeto();
		projeto.setNome("projeto teste 1");
		projeto.setDescricao("Esse é um projeto de teste para recuperação");
		projeto.setDhCriacao(new Date());
		
		Pagina pg = new Pagina();
		pg.setNome("Nome pagina 1");
		pg.setDescricao("Descrição da página 1");
		pg.setOrdem(1);
		pg.setProjeto(projeto);
		
		projeto.getPaginas().add(pg);
		
		pg = new Pagina();
		pg.setNome("Nome pagina 2");
		pg.setDescricao("Descrição da página 2");
		pg.setOrdem(2);
		pg.setProjeto(projeto);
		
		projeto.getPaginas().add(pg);
		
		Long idProjeto = projetoDao.persist(projeto);
		Assert.assertTrue("Projeto não inserido", idProjeto != null && idProjeto > 0);

		Projeto projeto2 = projetoDao.findById(1l);
		Assert.assertTrue("Projeto não recuperado", projeto2.getId() > 0);
		
		Assert.assertTrue("Páginas não recuperadas", projeto2.getPaginas().size() > 0);
		
		Assert.assertTrue("A descrição não foi recuperada", ((Pagina)projeto2.getPaginas().get(0)).getDescricao() != null && 
				((Pagina)projeto2.getPaginas().get(0)).getDescricao().trim().length() != 0);	
	}
	
	
}
