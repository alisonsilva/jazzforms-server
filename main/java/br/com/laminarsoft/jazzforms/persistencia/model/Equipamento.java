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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Table(name="EQUIPAMENTO")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@XmlRootElement(name="equipamento")
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({ "all", "unchecked" })
public class Equipamento implements Serializable {
	public Equipamento() {
	}
	
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue(generator="VC0A82B9C143EE7433990780E")	
	@org.hibernate.annotations.GenericGenerator(name="VC0A82B9C143EE7433990780E", strategy="native")
	@XmlAttribute
	private Long id;
	
	@ManyToOne(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Usuario.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})	
	@JoinColumns({ @JoinColumn(name="USUARIO_ID", referencedColumnName="ID") })	
	@Basic(fetch=FetchType.LAZY)	
	private br.com.laminarsoft.jazzforms.persistencia.model.Usuario ultimoUsuarioLogado;
	
	@Column(name="MARCA", nullable=true, length=80)	
	private String marca;
	
	@Column(name="MODELO", nullable=true, length=80)	
	private String modelo;
	
	@Column(name="DESCRICAO", nullable=true, length=255)	
	private String descricao;
	
	@Column(name="SERIAL", nullable=true, length=150)
    @XmlAttribute	
	private String serial;
	
	@Column(name="DATA_INCLUSAO", nullable=true)
	@XmlAttribute	
	private java.util.Date dataInclusao;
	
	@ManyToMany(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.GrupoEquipamento.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE, org.hibernate.annotations.CascadeType.LOCK})	
	@JoinTable(name="GRP_EQUIPAMENTO_EQUIPAMENTO", joinColumns={ @JoinColumn(name="EQUIPAMENTOID") }, inverseJoinColumns={ @JoinColumn(name="GRUPO_EQUIPAMENTOID") })	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)	
	@XmlTransient
	private java.util.List<GrupoEquipamento> grupos = new java.util.ArrayList<GrupoEquipamento>();
	
	@OneToMany(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Localizacao.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE, org.hibernate.annotations.CascadeType.LOCK})	
	@JoinColumn(name="EQUIPAMENTO_ID", nullable=true)	
	@org.hibernate.annotations.IndexColumn(name="IDX")	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)	
	private java.util.List<Localizacao> localizacoes = new java.util.ArrayList<Localizacao>();
	
	@OneToMany(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Mensagem.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE, org.hibernate.annotations.CascadeType.LOCK})	
	@JoinColumn(name="EQUIPAMENTO_ID", nullable=true)	
	@org.hibernate.annotations.IndexColumn(name="IDX")	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)	
	private java.util.List<Mensagem> mensagens = new java.util.ArrayList<Mensagem>();
	
	public void setId(long value) {
		setId(new Long(value));
	}
	
	public void setId(Long value) {
		this.id = value;
	}
	
	public Long getId() {
		return id;
	}
	
	public Long getORMID() {
		return getId();
	}
	
	public void setMarca(String value) {
		this.marca = value;
	}
	
	public String getMarca() {
		return marca;
	}
	
	public void setModelo(String value) {
		this.modelo = value;
	}
	
	public String getModelo() {
		return modelo;
	}
	
	public void setDescricao(String value) {
		this.descricao = value;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setSerial(String value) {
		this.serial = value;
	}
	
	public String getSerial() {
		return serial;
	}

	public void setDataInclusao(java.util.Date value) {
		this.dataInclusao = value;
	}
	
	public java.util.Date getDataInclusao() {
		return dataInclusao;
	}


	public void setGrupos(java.util.List<GrupoEquipamento> value) {
		this.grupos = value;
	}
	
	public java.util.List<GrupoEquipamento> getGrupos() {
		return grupos;
	}
	
	
	public void setUltimoUsuarioLogado(br.com.laminarsoft.jazzforms.persistencia.model.Usuario value) {
		this.ultimoUsuarioLogado = value;
	}
	
	public br.com.laminarsoft.jazzforms.persistencia.model.Usuario getUltimoUsuarioLogado() {
		return ultimoUsuarioLogado;
	}
	
	public void setLocalizacoes(java.util.List<Localizacao> value) {
		this.localizacoes = value;
	}
	
	public java.util.List<Localizacao> getLocalizacoes() {
		return localizacoes;
	}
	
	
	public void setMensagens(java.util.List<Mensagem> value) {
		this.mensagens = value;
	}
	
	public java.util.List<Mensagem> getMensagens() {
		return mensagens;
	}
	
	
	public String toString() {
		return String.valueOf(getId());
	}
	
}
