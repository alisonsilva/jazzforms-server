package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="land")
@SuppressWarnings("all")
public class LandVO implements Serializable {
	@XmlAttribute public Long id;	
	@XmlAttribute public String iconCls;
	@XmlAttribute public String titulo;
	@XmlAttribute public String texto;
	@XmlAttribute public String url;
	@XmlAttribute public Long dhInclusao;
	@XmlAttribute public String categoria;
	@XmlAttribute public String nomeFonte;
	
	public List<GrupoVO> grupos = new ArrayList<GrupoVO>();
	public List<LandEntryVO> entries;
	
	public LandVO() {
		
	}
	
	public LandVO(Long id, String iconCls, String titulo, String texto, String url, Long dhInclusao, String categoria) {
		this.id = id;
		this.iconCls = iconCls;
		this.titulo = titulo;
		this.texto = texto;
		this.url = url;
		this.dhInclusao = dhInclusao;
		this.categoria = categoria;
	}
}
