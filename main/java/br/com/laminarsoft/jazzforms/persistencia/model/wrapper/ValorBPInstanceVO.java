package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("all")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="valor_bpinstance")
public class ValorBPInstanceVO implements Serializable{
	@XmlAttribute public String idCampo;
	@XmlAttribute public String valorCampo;
	@XmlAttribute public Date dhAlteracao;
}
