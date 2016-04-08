package br.com.laminarsoft.jazzforms.persistencia.model.util;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.laminarsoft.jazzforms.persistencia.model.Pagina;

@SuppressWarnings("all")
@XmlRootElement(name="info_pagina")
@XmlAccessorType(XmlAccessType.FIELD)
public class InfoRetornoPagina extends InfoRetornoBase {
	public Pagina pagina;
	
	public List<Pagina> paginas;
}
