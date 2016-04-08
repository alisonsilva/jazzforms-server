package br.com.laminarsoft.jazzforms.persistencia.model.util;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.laminarsoft.jazzforms.persistencia.model.ProcessModel;

@SuppressWarnings("all")
@XmlRootElement(name="info_process_model")
@XmlAccessorType(XmlAccessType.FIELD)
public class InfoRetornoProcessModel extends InfoRetornoBase {
	public ProcessModel processModel;
	
	public List<ProcessModel> processModels;
}
