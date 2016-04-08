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
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Table(name="INSTANCIA")
@javax.xml.bind.annotation.XmlRootElement(name="instancia")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@SuppressWarnings({ "all", "unchecked" })
public class Instancia implements Serializable {
	public Instancia() {
	}
	
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue(generator="VC0A8010714336DEFFC10A121")	
	@org.hibernate.annotations.GenericGenerator(name="VC0A8010714336DEFFC10A121", strategy="native")
	@XmlAttribute
	private Long id;
	
	@ManyToOne(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Usuario.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})	
	@JoinColumns({ @JoinColumn(name="USUARIO_RETORNO_ID", referencedColumnName="ID") })	
	@Basic(fetch=FetchType.LAZY)	
	private br.com.laminarsoft.jazzforms.persistencia.model.Usuario usuarioRetorno;


	@ManyToOne(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Usuario.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})	
	@JoinColumns({ @JoinColumn(name="USUARIO_ID", referencedColumnName="ID") })	
	@Basic(fetch=FetchType.LAZY)	
	private br.com.laminarsoft.jazzforms.persistencia.model.Usuario usuario;
	
	@ManyToOne(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Projeto.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})	
	@JoinColumns({ @JoinColumn(name="PROJETO_ID", referencedColumnName="ID") })	
	@Basic(fetch=FetchType.LAZY)	
	private br.com.laminarsoft.jazzforms.persistencia.model.Projeto projeto;
	
	@Column(name="DH_CRIACAO", nullable=true)	
	private java.util.Date dhCriacaoP;
	
	@Column(name="DH_ALTERACAO", nullable=true)	
	private java.util.Date dhAlteracaoP;
	
	@Column(name="RETORNADA", nullable=true, length=1)
	@XmlAttribute	
	private Boolean retornada = false;
	
	@OneToMany(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.ValorFormulario.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})	
	@JoinColumn(name="INSTANCIAID", nullable=true)	
	@org.hibernate.annotations.IndexColumn(name="IDX")	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.FALSE)	
	private java.util.List<ValorFormulario> valoresFormulario = new java.util.ArrayList<ValorFormulario>();
	
	@OneToMany(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.ValorDataview.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})	
	@JoinColumn(name="INSTANCIA_ID", nullable=true)	
	@org.hibernate.annotations.IndexColumn(name="IDX")	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.FALSE)	
	private java.util.List<ValorDataview> valoresDataview = new java.util.ArrayList<ValorDataview>();

	@OneToMany(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Foto.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})	
	@JoinColumn(name="INSTANCIA_ID", nullable=true)	
	@org.hibernate.annotations.IndexColumn(name="IDX")	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.FALSE)	
	private java.util.List<Foto> fotos = new java.util.ArrayList<Foto>();

	@OneToMany(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Historico.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.LOCK})	
	@JoinColumn(name="INSTANCIA_ID", nullable=true)	
	@org.hibernate.annotations.IndexColumn(name="IDX")	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)	
	private java.util.List<Historico> historicos = new java.util.ArrayList<Historico>();
	
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
	
	public void setDhCriacaoP(java.util.Date value) {
		this.dhCriacaoP = value;
	}
	
	public java.util.Date getDhCriacaoP() {
		return dhCriacaoP;
	}
	
	public void setDhAlteracaoP(java.util.Date value) {
		this.dhAlteracaoP = value;
	}
	
	public java.util.Date getDhAlteracaoP() {
		return dhAlteracaoP;
	}


	public void setRetornada(boolean value) {
		setRetornada(new Boolean(value));
	}
	
	public void setRetornada(Boolean value) {
		this.retornada = value;
	}
	
	public Boolean getRetornada() {
		return retornada;
	}
	
	public void setProjeto(br.com.laminarsoft.jazzforms.persistencia.model.Projeto value) {
		this.projeto = value;
	}
	
	public br.com.laminarsoft.jazzforms.persistencia.model.Projeto getProjeto() {
		return projeto;
	}
	
	public void setUsuario(br.com.laminarsoft.jazzforms.persistencia.model.Usuario value) {
		this.usuario = value;
	}
	
	public br.com.laminarsoft.jazzforms.persistencia.model.Usuario getUsuario() {
		return usuario;
	}
	
	public void setValoresFormulario(java.util.List<ValorFormulario> value) {
		this.valoresFormulario = value;
	}
	
	public java.util.List<ValorFormulario> getValoresFormulario() {
		return valoresFormulario;
	}
	
	
	public void setValoresDataview(java.util.List<ValorDataview> value) {
		this.valoresDataview = value;
	}
	
	public java.util.List<ValorDataview> getValoresDataview() {
		return valoresDataview;
	}

	public void setUsuarioRetorno(br.com.laminarsoft.jazzforms.persistencia.model.Usuario value) {
		this.usuarioRetorno = value;
	}
	
	public br.com.laminarsoft.jazzforms.persistencia.model.Usuario getUsuarioRetorno() {
		return usuarioRetorno;
	}

	public void setFotos(java.util.List<Foto> value) {
		this.fotos = value;
	}
	
	public java.util.List<Foto> getFotos() {
		return fotos;
	}	


	public void setHistoricos(java.util.List value) {
		this.historicos = value;
	}
	
	public java.util.List getHistoricos() {
		return historicos;
	}
	
	@Transient	
	private String userLogin;
	
	public String getUserLogin() {
		return userLogin;
	}
	
	public void setUserLogin(String aUserLogin) {
		userLogin = aUserLogin;
	}
	
	@Transient	
	private Long projetoId;
	
	public Long getProjetoId() {
		return projetoId;
	}
	
	public void setProjetoId(Long aProjetoId) {
		projetoId = aProjetoId;
	}
	
	@Transient	
	private Long dhAlteracao;
	
	public Long getDhAlteracao() {
		return dhAlteracao;
	}
	
	public void setDhAlteracao(Long aDhAlteracao) {
		dhAlteracao = aDhAlteracao;
	}
	
	@Transient	
	private Long dhCriacao;
	
	public Long getDhCriacao() {
		return dhCriacao;
	}
	
	public void setDhCriacao(Long aDhCriacao) {
		dhCriacao = aDhCriacao;
	}
	
	@Transient private Integer flReenviado;
	@Transient private Long idOriginal = null;
	
	
	
	public Integer getFlReenviado() {
		return flReenviado;
	}

	public void setFlReenviado(Integer flReenviado) {
		this.flReenviado = flReenviado;
	}

	public Long getIdOriginal() {
		return idOriginal;
	}

	public void setIdOriginal(Long idOriginal) {
		this.idOriginal = idOriginal;
	}

	public String toString() {
		return String.valueOf(getId());
	}
	
}
