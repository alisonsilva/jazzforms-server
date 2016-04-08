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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Table(name="LAND")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@SuppressWarnings({ "all", "unchecked" })
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="land")
public class Land implements Serializable, Cloneable {
	public Land() {
	}
	
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue(generator="VC0A8010614D6216BD550C846")	
	@org.hibernate.annotations.GenericGenerator(name="VC0A8010614D6216BD550C846", strategy="native")
	@XmlAttribute
	private long id;
	
	@Column(name="ICON_CLS", nullable=true, length=70)
	@XmlAttribute
	private String iconCls;
	
	@Column(name="URL", nullable=true, length=200)
	@XmlAttribute
	private String url;
	
	@Column(name="DH_INCLUSAO", nullable=true)
	@XmlAttribute
	private java.util.Date dhInclusao;
	
	@Column(name="CATEGORIA", nullable=true, length=255)
	@XmlAttribute
	private String categoria;
	
	@Column(name="QTD_NIVEIS", nullable=true, length=2)
	@XmlAttribute
	private int qtdNiveis;
	
	@Column(name="PADRAO_EXCLUSAO", nullable=true, length=200)
	@XmlAttribute
	private String padraoExclusao;
	
	@Column(name="CLASSE_MANIPULACAO", nullable=true, length=200)
	@XmlAttribute
	private String classeManipulacao;
	
	@Column(name="NOME_FONTE", nullable=true, length=255)	
	@XmlAttribute
	private String nomeFonte;
	
	@ManyToMany(mappedBy="lands", targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Grupo.class)	
	@org.hibernate.annotations.Cascade({})	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.FALSE)	
	private java.util.List<Grupo> grupos = new java.util.ArrayList<Grupo>();
	
	@OneToMany(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.LandEntry.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})	
	@JoinColumn(name="LAND_ID", nullable=false)	
	@org.hibernate.annotations.IndexColumn(name="IDX")	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.FALSE)	
	private java.util.List<LandEntry> entries = new java.util.ArrayList<LandEntry>();
	
	public void setId(long value) {
		this.id = value;
	}
	
	public long getId() {
		return id;
	}
	
	public long getORMID() {
		return getId();
	}
	
	public void setIconCls(String value) {
		this.iconCls = value;
	}
	
	public String getIconCls() {
		return iconCls;
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
	
	public void setCategoria(String value) {
		this.categoria = value;
	}
	
	public String getCategoria() {
		return categoria;
	}
	
	public void setNomeFonte(String value) {
		this.nomeFonte = value;
	}
	
	public String getNomeFonte() {
		return nomeFonte;
	}
	
	public void setQtdNiveis(int value) {
		this.qtdNiveis = value;
	}
	
	public int getQtdNiveis() {
		return qtdNiveis;
	}
	
	public void setPadraoExclusao(String value) {
		this.padraoExclusao = value;
	}
	
	public String getPadraoExclusao() {
		return padraoExclusao;
	}
	
	public void setClasseManipulacao(String value) {
		this.classeManipulacao = value;
	}
	
	public String getClasseManipulacao() {
		return classeManipulacao;
	}

	public void setGrupos(java.util.List<Grupo> value) {
		this.grupos = value;
	}
	
	public java.util.List<Grupo> getGrupos() {
		return grupos;
	}
	
	public void setEntries(java.util.List<LandEntry> value) {
		this.entries = value;
	}
	
	public java.util.List<LandEntry> getEntries() {
		return entries;
	}
	
	public Land clone() {
		Land newLand = new Land();
		newLand.setCategoria(this.getCategoria());
		newLand.setDhInclusao(new Date(this.getDhInclusao().getTime()));
		for(Grupo grp : this.getGrupos()) {
			newLand.getGrupos().add(grp.clone());
		}
		newLand.setIconCls(this.getIconCls());
		newLand.setPadraoExclusao(this.getPadraoExclusao());
		newLand.setQtdNiveis(this.getQtdNiveis());
		newLand.setUrl(this.getUrl());
		newLand.setNomeFonte(this.getNomeFonte());
		newLand.setClasseManipulacao(this.getClasseManipulacao());
		for(LandEntry entry : this.getEntries()) {
			LandEntry cloned = entry.clone();
			cloned.setLand(newLand);
			newLand.getEntries().add(cloned);
			
		}
		return newLand;
	}
	
	public String toString() {
		return String.valueOf(getId());
	}
	
}
