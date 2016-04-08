package br.com.laminarsoft.jazzforms.persistencia.model.wrapper.sigtap;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="tribo_indigena")
@XmlAccessorType(XmlAccessType.FIELD)
public class TriboIndigenaVO {
	@XmlAttribute public Long coTriboIndigena = 0l;
	@XmlAttribute public String noTriboIndigena;
	@XmlAttribute public Date dhUltimaAtualizacao;
}
