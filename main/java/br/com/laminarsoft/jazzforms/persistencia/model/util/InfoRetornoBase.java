package br.com.laminarsoft.jazzforms.persistencia.model.util;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("all")
@XmlRootElement(name="info_base")
@XmlAccessorType(XmlAccessType.FIELD)
public class InfoRetornoBase implements Serializable {
	@XmlAttribute public Integer codigo;
	@XmlAttribute public String mensagem;
	@XmlAttribute public String token;
}
