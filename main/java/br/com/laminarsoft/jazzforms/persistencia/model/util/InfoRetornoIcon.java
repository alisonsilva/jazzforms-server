package br.com.laminarsoft.jazzforms.persistencia.model.util;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Icon;

@SuppressWarnings("all")
@XmlRootElement(name="info_icon")
@XmlAccessorType(XmlAccessType.FIELD)
public class InfoRetornoIcon extends InfoRetornoBase {
	
	public Icon icon;
	
	public List<Icon> icons;
}
