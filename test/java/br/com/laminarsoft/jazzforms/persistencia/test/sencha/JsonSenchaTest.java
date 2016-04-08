package br.com.laminarsoft.jazzforms.persistencia.test.sencha;

import junit.framework.Assert;

import org.junit.Test;

import br.com.laminarsoft.jazzforms.persistencia.model.senchatransform.SenchaImplArraySerializer;
import br.com.laminarsoft.jazzforms.persistencia.model.senchatransform.SenchaImplSerializer;
import br.com.laminarsoft.jazzforms.persistencia.model.senchatransform.SenchaImplementacao;
import br.com.laminarsoft.jazzforms.persistencia.model.senchatransform.SenchaItem;
import br.com.laminarsoft.jazzforms.persistencia.model.senchatransform.SenchaPage;
import br.com.laminarsoft.jazzforms.persistencia.test.TesteBase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonSenchaTest extends TesteBase {

	@Test
	public void executaTesteFormacaoJson() {
		SenchaPage page = new SenchaPage();
		page.xtype = "mypanel";
		page.extend = "Ext.panel";
		
		page.fullScreen = true;
		page.itemId = "mypanel";
		page.layout = "fit";

		
		SenchaItem button = new SenchaItem();
		button.text = "meu botão";
		button.badgeText = "2";
		button.docked = "bottom";
		button.ui = "confirm";
		button.itemId = "mybutton";
		button.xtype = "button";
		page.items.add(button);
		
		SenchaImplementacao impl = new SenchaImplementacao();
		impl.addImplementacao("tap", "function(button, e, eOpts) {var me = this; "
				+ "me.fireEvent('AnotherButtonClicked');}");
		impl.addImplementacao("element", "tap", "fn", "function(){var me = this;}");
		
		button.listenersarray = impl.implementacoes;
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(SenchaImplementacao.class, new SenchaImplSerializer());
		gsonBuilder.registerTypeAdapter(SenchaImplementacao.Impl.class, new SenchaImplArraySerializer());
		gsonBuilder.disableHtmlEscaping();
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		
		String json = gson.toJson(page);
		
		System.out.println(json);
		
		Assert.assertTrue("Não foi gerado json", json.length() > 0);
	}
}
