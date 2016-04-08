package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="tipo_sugestao")
public class TipoSugestaoVO implements Serializable {
	public static final long serialVersionUID = 21l;
	
	@XmlAttribute public int coTipo;
	@XmlAttribute public String descricaoTipo;
}
