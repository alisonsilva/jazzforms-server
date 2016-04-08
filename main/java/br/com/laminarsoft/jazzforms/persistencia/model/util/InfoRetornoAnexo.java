package br.com.laminarsoft.jazzforms.persistencia.model.util;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.AnexoVO;

@SuppressWarnings("all")
@XmlRootElement(name="info_anexo")
@XmlAccessorType(XmlAccessType.FIELD)
public class InfoRetornoAnexo extends InfoRetornoBase {

	@XmlAttribute public Long id;
	@XmlAttribute public Long dhInclusao;
	@XmlAttribute public String nomeArquivo;
	@XmlAttribute public String urlSite;
	@XmlAttribute public String type;
	
	public String arqAnexo;
}
