package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="paramtro")
@XmlAccessorType(XmlAccessType.FIELD)
public class ParametroVO {
	@XmlAttribute public String nome = "";
	@XmlAttribute public String valor = "";
}
