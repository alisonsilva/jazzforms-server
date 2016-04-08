package br.com.laminarsoft.jazzforms.persistencia.test.util.gson;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DataSerializer implements JsonSerializer<Data> {


	@Override
	public JsonElement serialize(Data src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject jsonObj = new JsonObject();
		
		for (Campo cmp : src.campos) {
	        jsonObj.add(cmp.valor1, new JsonPrimitive(cmp.valor2));
        }
		return jsonObj;
	}

}
