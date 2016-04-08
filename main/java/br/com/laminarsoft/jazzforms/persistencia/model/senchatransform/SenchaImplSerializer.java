package br.com.laminarsoft.jazzforms.persistencia.model.senchatransform;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class SenchaImplSerializer implements JsonSerializer<SenchaImplementacao> {

	@Override
	public JsonElement serialize(SenchaImplementacao impl, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject jsonObj = new JsonObject();
		
		if (impl != null && impl.implementacoes != null) {
			for (SenchaImplementacao.Impl src : impl.implementacoes) {
				if (src != null && src.element != null
						&& src.element.trim().length() > 0 && src.event != null
						&& src.event.trim().length() > 0) {
					jsonObj.add("element", new JsonPrimitive(src.element));
					jsonObj.add("event", new JsonPrimitive(src.event));
				}
				jsonObj.add((src.nomeMetodo == null ? "" : src.nomeMetodo),
						new JsonPrimitive(src.implementacao));
			}
		}
		return jsonObj;
	}
}
