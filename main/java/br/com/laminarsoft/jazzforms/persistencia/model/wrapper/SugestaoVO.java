package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="sugestao")
public class SugestaoVO implements Serializable {
	public static final long serialVersionUID = 23l;
	
	@XmlAttribute public String token;
	@XmlAttribute public String loginUsuario;
	@XmlAttribute public String detalhe;
	@XmlAttribute public long tipoSugestao;
}
