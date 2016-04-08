package br.com.laminarsoft.jazzforms.persistencia.model.wrapper.sigtap;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="procedimento")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProcedimentoVO {
	@XmlAttribute public String coProcedimento;
	@XmlAttribute public String noProcedimento;
	@XmlAttribute public Date dhUltimaAtualizacao;
}
