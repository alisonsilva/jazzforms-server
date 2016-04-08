package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.laminarsoft.jazzforms.persistencia.model.Projeto;

@SuppressWarnings("all")
@XmlRootElement(name="projeto_wrapper")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProjetoWrapperVO implements Serializable {
	@XmlAttribute public String token;
	
	public Projeto projeto;
}
