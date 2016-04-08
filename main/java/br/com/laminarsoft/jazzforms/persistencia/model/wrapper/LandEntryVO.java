package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="land_entry")
public class LandEntryVO implements Serializable {
	public static final long serialVersionUID = 21l;
	
	@XmlAttribute public long id;
	@XmlAttribute public String titulo;
	public String texto;
	@XmlAttribute public String url;
	@XmlAttribute public long dhInclusao;
	@XmlAttribute public boolean abrirUrlDiretamente = false;
	@XmlAttribute public String categoria;
	@XmlAttribute public String iconeUrl;
	@XmlAttribute public String iconeTipo;
	@XmlAttribute public String nomeFonte;
	public byte[] icone;
	
}
