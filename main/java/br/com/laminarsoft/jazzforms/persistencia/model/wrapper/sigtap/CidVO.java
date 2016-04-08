package br.com.laminarsoft.jazzforms.persistencia.model.wrapper.sigtap;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="cid")
@XmlAccessorType(XmlAccessType.FIELD)
public class CidVO {
	@XmlAttribute public String coCid;
	@XmlAttribute public String noCid;
	@XmlAttribute public String tpAgravo;
	@XmlAttribute public String tpSexo;
	@XmlAttribute public Date dhUltimaAtualizacao;
}
