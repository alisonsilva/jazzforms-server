package br.com.laminarsoft.jazzforms.persistencia.model.util;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.laminarsoft.jazzforms.persistencia.model.Projeto;


@SuppressWarnings("all")
@XmlRootElement(name="info_projeto")
@XmlAccessorType(XmlAccessType.FIELD)
public class InfoRetornoProjeto extends InfoRetornoBase {
	public Projeto projeto;
	
	public List<Projeto> projetos;
}
