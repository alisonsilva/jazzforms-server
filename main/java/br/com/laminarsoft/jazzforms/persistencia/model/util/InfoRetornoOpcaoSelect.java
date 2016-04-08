package br.com.laminarsoft.jazzforms.persistencia.model.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Option;

@SuppressWarnings("all")
@XmlRootElement(name="info_opcaoselect")
@XmlAccessorType(XmlAccessType.FIELD)
public class InfoRetornoOpcaoSelect extends InfoRetornoBase {
	public List<Option> opcoes = new ArrayList<Option>();
}
