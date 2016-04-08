package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

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

@SuppressWarnings("all")
public class DataViewStructure {

	private Rest rstEstrutura;
	
	public void montaEstruturaDataview(String estrutura) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.disableHtmlEscaping();
		gsonBuilder.registerTypeAdapter(Data.class, new DataViewStructureSerializer());
		gsonBuilder.registerTypeAdapter(Data.class, new DataViewStructureDeserializer());
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		
		this.rstEstrutura = gson.fromJson(estrutura, Rest.class);		
	}
	
	public List<String> getFields() {
		return rstEstrutura.fields;
	}
	
	public static List<String> getFieldsDataview(String especificacaoDataview) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.disableHtmlEscaping();
		gsonBuilder.registerTypeAdapter(Data.class, new DataViewStructureSerializer());
		gsonBuilder.registerTypeAdapter(Data.class, new DataViewStructureDeserializer());
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		
		Rest estrutura = gson.fromJson(especificacaoDataview, Rest.class);		
		return estrutura.fields;
	}

}

class DataViewStructureSerializer implements JsonSerializer<Data> {


	@Override
	@SuppressWarnings("all")
	public JsonElement serialize(Data src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject jsonObj = new JsonObject();
		
		for (Campo cmp : src.campos) {
	        jsonObj.add(cmp.nome, new JsonPrimitive(cmp.valor));
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

@SuppressWarnings("all")
class Rest implements Serializable{
	List<String> fields = new ArrayList<String>();
	List<Data> data = new ArrayList<Data>();
	
	public Rest() {
	}
}

@SuppressWarnings("all")
class Data implements Serializable{
	public List<Campo> campos = new ArrayList<Campo>();
	
}
@SuppressWarnings("all")
class Campo implements Serializable{
	public String nome;
	public String valor;

	public Campo(String valor1, String valor2) {
		this.nome = valor1;
		this.valor = valor2;
	}
}
