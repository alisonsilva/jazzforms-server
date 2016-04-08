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
@Table(name="MENSAGEM")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@XmlRootElement(name="mensagem")
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({ "all", "unchecked" })
public class Mensagem implements Serializable {
	public Mensagem() {
	}
	
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue(generator="VC0A82B9C143EE7433BD07811")	
	@org.hibernate.annotations.GenericGenerator(name="VC0A82B9C143EE7433BD07811", strategy="native")
	@XmlAttribute
	private Long id;
	
	@ManyToOne(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.AgrupamentoMensagem.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})	
	@JoinColumns({ @JoinColumn(name="AGRUPAMENTO_MENSAGEM_ID", referencedColumnName="ID", insertable=false, updatable=false) })	
	private br.com.laminarsoft.jazzforms.persistencia.model.AgrupamentoMensagem agrupamento;
	
	@OneToOne(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.BPInstance.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})	
	@JoinColumns({ @JoinColumn(name="BP_INSTANCE_ID") })	
	private br.com.laminarsoft.jazzforms.persistencia.model.BPInstance bpInstance;
	
	@OneToOne(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Deployment.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.LOCK})	
	@JoinColumns({ @JoinColumn(name="DEPLOYMENT_ID") })	
	@Basic(fetch=FetchType.EAGER)	
	private br.com.laminarsoft.jazzforms.persistencia.model.Deployment deployment;
	
	@ManyToOne(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Usuario.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})	
	@JoinColumns({ @JoinColumn(name="USUARIO_REMETENTE_ID", referencedColumnName="ID") })	
	@Basic(fetch=FetchType.LAZY)	
	@XmlTransient
	private br.com.laminarsoft.jazzforms.persistencia.model.Usuario remetente;
	
	@Column(name="CONTEUDO", nullable=true, length=512)	
	private String conteudo;
	
	@Column(name="TITULO", nullable=true, length=80)	
	private String titulo;
	
	@Column(name="DATA", nullable=true)	
	@XmlAttribute
	private java.util.Date data;
	
	@OneToMany(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Anexo.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})	
	@JoinColumn(name="MENSAGEM_ID", nullable=true)	
	@org.hibernate.annotations.IndexColumn(name="IDX")	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.FALSE)	
	private java.util.List<Anexo> anexos = new java.util.ArrayList<Anexo>();
	
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
	
	public void setConteudo(String value) {
		this.conteudo = value;
	}
	
	public String getConteudo() {
		return conteudo;
	}
	
	public void setTitulo(String value) {
		this.titulo = value;
	}
	
	public String getTitulo() {
		return titulo;
	}
	
	public void setData(java.util.Date value) {
		this.data = value;
	}
	
	public java.util.Date getData() {
		return data;
	}
	
	public void setRemetente(br.com.laminarsoft.jazzforms.persistencia.model.Usuario value) {
		this.remetente = value;
	}
	
	public br.com.laminarsoft.jazzforms.persistencia.model.Usuario getRemetente() {
		return remetente;
	}
	
	public void setAnexos(java.util.List<Anexo> value) {
		this.anexos = value;
	}
	
	public java.util.List<Anexo> getAnexos() {
		return anexos;
	}
	
	
	public void setDeployment(br.com.laminarsoft.jazzforms.persistencia.model.Deployment value) {
		this.deployment = value;
	}
	
	public br.com.laminarsoft.jazzforms.persistencia.model.Deployment getDeployment() {
		return deployment;
	}
	
	public void setBpInstance(br.com.laminarsoft.jazzforms.persistencia.model.BPInstance value) {
		this.bpInstance = value;
	}
	
	public br.com.laminarsoft.jazzforms.persistencia.model.BPInstance getBpInstance() {
		return bpInstance;
	}
	
	public void setAgrupamento(br.com.laminarsoft.jazzforms.persistencia.model.AgrupamentoMensagem value) {
		this.agrupamento = value;
	}
	
	public br.com.laminarsoft.jazzforms.persistencia.model.AgrupamentoMensagem getAgrupamento() {
		return agrupamento;
	}
	
	public String toString() {
		return String.valueOf(getId());
	}
	
}
