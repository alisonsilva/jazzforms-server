package br.com.laminarsoft.jazzforms.persistencia.test.util.gson;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class TesteGson {

	@Test
	public void teste_1_FromObjectToString() {
		Rest rst = new Rest();

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.disableHtmlEscaping();
		gsonBuilder.registerTypeAdapter(Data.class, new DataSerializer());
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		String json = gson.toJson(rst);
		System.out.println(json);
		Assert.assertTrue(true);
	}
	
	@Test
	public void teste_2_FromStringToObject() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.disableHtmlEscaping();
		gsonBuilder.registerTypeAdapter(Data.class, new DataViewStructureSerializer());
		gsonBuilder.registerTypeAdapter(Data.class, new DataViewStructureDeserializer());
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		
		String strRest = "{	fields: ['id','name','age'], data: [{id: '1',name: 'Jamie',age: 100},{id: '2',name: 'Rob',age: 21},{id: '3',name: 'Tommy',age: 24},{id: '4',name: 'Jacky',age: 24},{id: '5',name: 'Ed',age: 26}]}";
		
		Rest rst = gson.fromJson(strRest, Rest.class);
		Assert.assertNotNull(rst);
	}
	
}

class DataViewStructureSerializer implements JsonSerializer<Data> {


	@Override
	@SuppressWarnings("all")
	public JsonElement serialize(Data src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject jsonObj = new JsonObject();
		
		for (Campo cmp : src.campos) {
	        jsonObj.add(cmp.valor1, new JsonPrimitive(cmp.valor2));
        }
		return jsonObj;
	}

}

class DataViewStructureDeserializer implements JsonDeserializer<Data> {

	@Override
	@SuppressWarnings("all")
    public Data deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		Data data = new Data();
		JsonObject obj = json.getAsJsonObject();
		Set<Entry<String, JsonElement>> entradasSet = obj.entrySet();
		Entry<String, JsonElement>[] entradas = obj.entrySet().toArray(new Entry[entradasSet.size()]);
		for (Entry<String, JsonElement> entrada : entradas) {
			String chave = entrada.getKey();
			JsonElement elemento = entrada.getValue();
			Campo campo = new Campo(chave, elemento.getAsString());
			data.campos.add(campo);
		}
		return data;
    }
	
}


class Rest implements Serializable{
	String[] fields = new String[]{"campo1", "campo2", "campo3"};
	List<Data> data = new ArrayList<Data>();
	
	public Rest() {
		Data dt = new Data();
		dt.campos.add(new Campo("id", "1"));
		dt.campos.add(new Campo("name", "Jame"));
		dt.campos.add(new Campo("age", "100"));
		data.add(dt);
		
		dt = new Data();
		dt.campos.add(new Campo("id", "2"));
		dt.campos.add(new Campo("name", "Rob"));
		dt.campos.add(new Campo("age", "21"));
		data.add(dt);
	}
}

class Data implements Serializable{
	public List<Campo> campos = new ArrayList<Campo>();
	
}
class Campo implements Serializable{
	public String valor1;
	public String valor2;

	public Campo(String valor1, String valor2) {
		this.valor1 = valor1;
		this.valor2 = valor2;
	}
}