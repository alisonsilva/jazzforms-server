package br.com.laminarsoft.jazzforms.persistencia.model.senchatransform;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class SenchaImplArraySerializer implements JsonSerializer<SenchaImplementacao.Impl> {

	@Override
	public JsonElement serialize(SenchaImplementacao.Impl impl, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject jsonObj = new JsonObject();
	
		if (impl != null && impl.element != null && impl.element.trim().length() > 0 &&
				impl.event != null && impl.event.trim().length() > 0) {
			jsonObj.add("element", new JsonPrimitive(impl.element));
			jsonObj.add("event", new JsonPrimitive(impl.event));
		}
		jsonObj.add((impl.nomeMetodo == null ? "" : impl.nomeMetodo), new JsonPrimitive(impl.implementacao));			
		
		return jsonObj;
	}
}
