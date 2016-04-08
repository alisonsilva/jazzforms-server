package br.com.laminarsoft.jazzforms.persistencia.model.wrapper.sigtap;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="nacionalidade")
@XmlAccessorType(XmlAccessType.FIELD)
public class NacionalidadeVO {
	@XmlAttribute public Long coNacionalidade = 0l;
	@XmlAttribute public String dsNacionalidade;
	@XmlAttribute public Date dhUltimaAtualizacao;
}
