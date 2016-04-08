package br.com.laminarsoft.jazzforms.persistencia.model.util;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.laminarsoft.jazzforms.persistencia.model.ComponentType;
import br.com.laminarsoft.jazzforms.persistencia.model.TipoEvento;

@SuppressWarnings("all")
@XmlRootElement(name="info_evento")
@XmlAccessorType(XmlAccessType.FIELD)
public class InfoRetornoEvento extends InfoRetornoBase {
	
	public ComponentType ctype;
	
	public List<ComponentType> ctypes;
	
	public List<TipoEvento> tiposEvento;
}
