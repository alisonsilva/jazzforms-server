package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("all")
@XmlRootElement(name="anexo")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnexoVO {
	@XmlAttribute public Long id;
	@XmlAttribute public Date dhInclusao;
	@XmlAttribute public String nomeArquivo;
	@XmlAttribute public String urlSite;
	@XmlAttribute public String type;
	@XmlAttribute public Integer contemArquivo = 0;
	
	public byte[] arqAnexo;
	
}
