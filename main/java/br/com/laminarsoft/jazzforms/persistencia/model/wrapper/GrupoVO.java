package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("all")
@XmlRootElement(name="grupo")
@XmlAccessorType(XmlAccessType.FIELD)
public class GrupoVO {
	@XmlAttribute public long id;
	@XmlAttribute public String nome;
	@XmlAttribute public String dn;
	@XmlAttribute public String cn;
	
	public String descricao;
	
	public List<Long> deployments;
}
