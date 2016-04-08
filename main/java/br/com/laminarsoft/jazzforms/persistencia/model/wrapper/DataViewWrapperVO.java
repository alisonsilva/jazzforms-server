package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.laminarsoft.jazzforms.persistencia.model.Projeto;
import br.com.laminarsoft.jazzforms.persistencia.model.ValorDataview;

@SuppressWarnings("all")
@XmlRootElement(name="valor_dataview_wrapper")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataViewWrapperVO implements Serializable {
	@XmlAttribute public String token;
	
	public ValorDataview valorDataview;
}
