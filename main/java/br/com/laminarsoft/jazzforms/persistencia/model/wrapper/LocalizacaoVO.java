package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("all")
@XmlRootElement(name="localizacao")
@XmlAccessorType(XmlAccessType.FIELD)
public class LocalizacaoVO implements Serializable, Comparable<LocalizacaoVO> {
	public String conteudo;
	public String titulo;
	@XmlAttribute public String latitude;
	@XmlAttribute public String longitude;	
	@XmlAttribute public Date data;
	@XmlAttribute public Long dhEvento;
	@XmlAttribute public Long id;	
	
	@Override
    public int compareTo(LocalizacaoVO o) {
		LocalizacaoVO comp = (LocalizacaoVO)o;
		return this.data.compareTo(comp.data);
    }
	
	@Override
    public boolean equals(Object obj) {
		if(!(obj instanceof LocalizacaoVO)) {
			return false;
		}
		LocalizacaoVO loc = (LocalizacaoVO)obj;
		if(this.getId() == loc.getId()) {
			return true;
		}
		return false;
    }



	public String toString() {
		return String.valueOf(id);
	}
	
	public int hashCode() {
		return id.hashCode();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getConteudo() {
		return conteudo;
	}
	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String logitude) {
		this.longitude = logitude;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
}
