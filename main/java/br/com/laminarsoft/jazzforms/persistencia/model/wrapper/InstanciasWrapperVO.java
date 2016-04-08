package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import br.com.laminarsoft.jazzforms.persistencia.model.Instancia;

@javax.xml.bind.annotation.XmlRootElement(name="instancias")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@SuppressWarnings("all")
public class InstanciasWrapperVO implements Serializable {
	@XmlAttribute public String loginUsuarioAlteracao; 
	@XmlAttribute public String token;
	
	@XmlElement(name="instancia")
	public Instancia[] instancias = new Instancia[0];

}
