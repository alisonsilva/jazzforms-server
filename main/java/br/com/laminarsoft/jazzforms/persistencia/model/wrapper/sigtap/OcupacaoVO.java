package br.com.laminarsoft.jazzforms.persistencia.model.wrapper.sigtap;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ocupacao")
@XmlAccessorType(XmlAccessType.FIELD)
public class OcupacaoVO {
	@XmlAttribute public String coOcupacao;
	@XmlAttribute public String noOcupacao;
	@XmlAttribute public Date dhUltimaAtualizacao;
}
