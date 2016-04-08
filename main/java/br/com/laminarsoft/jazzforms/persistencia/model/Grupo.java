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

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Table(name="GRUPO")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@javax.xml.bind.annotation.XmlRootElement(name="grupo")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@SuppressWarnings({ "all", "unchecked" })
public class Grupo implements Serializable, Cloneable {
	public Grupo() {
	}
	
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue(generator="VC0A8380113F84F318D30AE8D")	
	@org.hibernate.annotations.GenericGenerator(name="VC0A8380113F84F318D30AE8D", strategy="native")
	@XmlAttribute
	private long id;
	
	@Column(name="NOME", nullable=false, length=50)	
	@XmlAttribute
	private String nome;
	
	@Column(name="DESCRICAO", nullable=true, length=255)	
	private String descricao;
	
	@Column(name="Dn", nullable=true, length=100)	
	private String dn;
	
	@ManyToMany(mappedBy="grupos", targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Usuario.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.LOCK})	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.FALSE)	
	@XmlTransient
	private java.util.List<Usuario> usuarios = new java.util.ArrayList<Usuario>();
	
	@ManyToMany(mappedBy="gruposPossiveis", targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Deployment.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.LOCK})	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)	
	@XmlTransient
	private java.util.List<Deployment> deployment = new java.util.ArrayList<Deployment>();

	@OneToMany(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Mensagem.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.LOCK})	
	@JoinColumn(name="GRUPO_ID", nullable=true)	
	@org.hibernate.annotations.IndexColumn(name="IDX")	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)
	@XmlTransient	
	private java.util.List<Mensagem> mensagens = new java.util.ArrayList<Mensagem>();
	
	@ManyToMany(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Land.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})	
	@JoinTable(name="GRUPO_LAND", joinColumns={ @JoinColumn(name="GRUPO_ID") }, inverseJoinColumns={ @JoinColumn(name="LAND_ID") })	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.FALSE)
	@XmlTransient
	private java.util.List<Land> lands = new java.util.ArrayList<Land>();
	
	public void setId(long value) {
		this.id = value;
	}
	
	public long getId() {
		return id;
	}
	
	public long getORMID() {
		return getId();
	}
	
	public void setNome(String value) {
		this.nome = value;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setDescricao(String value) {
		this.descricao = value;
	}
	
	public String getDescricao() {
		return descricao;
	}

	public void setDn(String value) {
		this.dn = value;
	}
	
	public String getDn() {
		return dn;
	}
	
	public void setUsuarios(java.util.List<Usuario> value) {
		this.usuarios = value;
	}
	
	public java.util.List<Usuario> getUsuarios() {
		return usuarios;
	}
	
	
	public void setDeployment(java.util.List<Deployment> value) {
		this.deployment = value;
	}
	
	public java.util.List<Deployment> getDeployment() {
		return deployment;
	}
	
	
	public void setMensagens(java.util.List<Mensagem> value) {
		this.mensagens = value;
	}
	
	public java.util.List<Mensagem> getMensagens() {
		return mensagens;
	}
	
	public void setLands(java.util.List<Land> value) {
		this.lands = value;
	}
	
	public java.util.List<Land> getLands() {
		return lands;
	}
	
	
	@Transient	
	private String cn;
	
	public String getCn() {
		return cn;
	}
	
	public void setCn(String aCn) {
		cn = aCn;
	}
	
	public String toString() {
		return String.valueOf(getId());
	}

	@Override
	public Grupo clone() {
		Grupo ngrupo = new Grupo();
		ngrupo.setCn(this.getCn());
		ngrupo.setDescricao(this.getDescricao());
		ngrupo.setDn(this.getDn());
		ngrupo.setId(this.getId());
		ngrupo.setNome(this.getNome());
		return ngrupo;
	}
	
	
	
}
