/**
 * "Visual Paradigm: DO NOT MODIFY THIS FILE!"
 * 
 * This is an automatic generated file. It will be regenerated every time 
 * you generate persistence class.
 * 
 * Modifying its content may cause the program not work, or your work may lost.
 */

/**
 * Licensee: DuKe TeAm
 * License Type: Purchased
 */
package br.com.laminarsoft.jazzforms.persistencia.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Table(name="LAND_ENTRY")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@SuppressWarnings({ "all", "unchecked" })
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="land_entry")
public class LandEntry implements Serializable, Cloneable {
	public LandEntry() {
	}
	
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue(generator="VC0A8010614D6216BD770C847")	
	@org.hibernate.annotations.GenericGenerator(name="VC0A8010614D6216BD770C847", strategy="native")
	@XmlAttribute
	private long id;
	
	@ManyToOne(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Land.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.DELETE, org.hibernate.annotations.CascadeType.SAVE_UPDATE})	
	@JoinColumns({ @JoinColumn(name="LAND_ID", referencedColumnName="ID", insertable=false, updatable=false) })	
	@Basic(fetch=FetchType.EAGER)	
	private br.com.laminarsoft.jazzforms.persistencia.model.Land land;
	
	@Column(name="TITULO", nullable=true, length=200)	
	@XmlAttribute
	private String titulo;
	
	@Column(name="TEXTO", nullable=true, length=3000)
	private String texto;
	
	@Column(name="URL", nullable=true, length=200)
	@XmlAttribute
	private String url;
	
	@Column(name="DH_INCLUSAO", nullable=true)
	@XmlAttribute
	private java.util.Date dhInclusao;
	
	@Column(name="ICONE", nullable=true)	
	private byte[] icone;
	
	@Column(name="ICONE_URL", nullable=true, length=200)
	@XmlAttribute
	private String iconeUrl;
	
	@Column(name="ICONE_TIPO", nullable=true, length=15)	
	@XmlAttribute
	private String iconeTipo;
	
	@Column(name="ABRIR_URL", nullable=false, length=1)	
	@XmlAttribute
	private boolean abrirUrlDiretamente = false;
	
	@Column(name="DH_NOTICIA", nullable=true)
	@XmlAttribute	
	private java.util.Date dhNoticia;
	
	public void setId(long value) {
		this.id = value;
	}
	
	public long getId() {
		return id;
	}
	
	public long getORMID() {
		return getId();
	}
	
	public void setTitulo(String value) {
		this.titulo = value;
	}
	
	public String getTitulo() {
		return titulo;
	}
	
	public void setTexto(String value) {
		this.texto = value;
	}
	
	public String getTexto() {
		return texto;
	}
	
	public void setUrl(String value) {
		this.url = value;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setDhInclusao(java.util.Date value) {
		this.dhInclusao = value;
	}
	
	public java.util.Date getDhInclusao() {
		return dhInclusao;
	}
	
	public void setIcone(byte[] value) {
		this.icone = value;
	}
	
	public byte[] getIcone() {
		return icone;
	}
	
	public void setAbrirUrlDiretamente(boolean value) {
		this.abrirUrlDiretamente = value;
	}
	
	public boolean getAbrirUrlDiretamente() {
		return abrirUrlDiretamente;
	}
	
	public void setIconeUrl(String value) {
		this.iconeUrl = value;
	}
	
	public String getIconeUrl() {
		return iconeUrl;
	}
	
	public void setIconeTipo(String value) {
		this.iconeTipo = value;
	}
	
	public String getIconeTipo() {
		return iconeTipo;
	}
	
	public void setDhNoticia(Date value) {
		this.dhNoticia = value;
	}
	
	public Date getDhNoticia() {
		return dhNoticia;
	}
	
	public void setLand(br.com.laminarsoft.jazzforms.persistencia.model.Land value) {
		this.land = value;
	}
	
	public br.com.laminarsoft.jazzforms.persistencia.model.Land getLand() {
		return land;
	}
	
	public LandEntry clone() {
		LandEntry newEntry = new LandEntry();
		newEntry.setDhInclusao(new Date(this.getDhInclusao().getTime()));
		newEntry.setTexto(this.getTexto());
		newEntry.setTitulo(this.getTitulo());
		newEntry.setUrl(this.getUrl());
		newEntry.setAbrirUrlDiretamente(this.getAbrirUrlDiretamente());
		newEntry.setIconeUrl(this.getIconeUrl());
		newEntry.setIconeTipo(this.getIconeTipo());
		newEntry.setDhNoticia(this.getDhNoticia() != null ? new Date(getDhNoticia().getTime()) : null);
		
		if(this.getIcone() != null && this.getIcone().length > 0) {
			byte[] iconC = new byte[this.getIcone().length];
			for(int idx = 0; idx < this.getIcone().length; idx++) {
				iconC[idx] = this.getIcone()[idx];
			}
			newEntry.setIcone(iconC);
		}
		return newEntry;
	}
	
	public String toString() {
		return String.valueOf(getId());
	}
	
}
