package br.com.laminarsoft.jazzforms.negocio.controller.util;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.springframework.security.core.userdetails.User;

import com.sun.jersey.api.client.Client;


public class WebServiceController<T> {
	private static Client client;

	
	
	public WebServiceController() {
		if (client == null) {
			client = Client.create();
		}
	}

	public T executaChamadaGet(String url, Class<T> classe) {
		T info = null;
		try {
//			WebResource webResource = client.resource(url);
//			webResource.accept(MediaType.APPLICATION_XML);
//			info = webResource.get(classe);
			
			URL turl = new URL(url);
			
			HttpURLConnection conn = (HttpURLConnection) turl.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/xml");
 
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }
 
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String apiOutput = br.readLine();
            conn.disconnect();
 
            JAXBContext jaxbContext = JAXBContext.newInstance(classe);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            info = (T) jaxbUnmarshaller.unmarshal(new StringReader(apiOutput));
            
            
		} catch (Exception e) {
			throw new RuntimeException(e);
		}		
		return info;
	}	
	
	public interface IInfoRetornoGenerico {
		public List<Object> objetos = new ArrayList<Object>();
	}
}
