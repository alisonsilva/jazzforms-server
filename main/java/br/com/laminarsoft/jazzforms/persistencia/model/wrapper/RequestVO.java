package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("all")
@XmlRootElement(name="campo")
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestVO implements Serializable {
	public static final int CODIGO_TIMESTAMP_EXPIRADO = 98;
	public static final int CODIGO_DECRIPT_TOKEN_ERROR = 99;
	
	public String[] parametros;
	
	public String token;
	
	public RequestVO(String[] parms, String token) {
		this.parametros = parms;
		this.token = token;
	}
	
	public RequestVO() {
		
	}
}
