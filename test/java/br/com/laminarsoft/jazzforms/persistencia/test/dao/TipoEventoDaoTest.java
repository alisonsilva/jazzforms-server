package br.com.laminarsoft.jazzforms.persistencia.test.dao;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import br.com.laminarsoft.jazzforms.persistencia.model.TipoEvento;
import br.com.laminarsoft.jazzforms.persistencia.test.TesteBase;

@SuppressWarnings("all")
@Ignore
public class TipoEventoDaoTest extends TesteBase{

	@Test
	public void persistTest() {
		TipoEvento tpEvento = new TipoEvento();
		tpEvento.setNome("TextFieldEvent");
		tpEvento.setDescricao("Tipo de eventos disparados para campos do tipo TextField");
		

		Long id = tipoEventoDao.persist(tpEvento);
		Assert.assertTrue("O id do registro inserido deve ser maior que zero", id > 0);
	}
}
