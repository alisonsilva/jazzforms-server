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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Table(name="AGRUPAMENTO_MENSAGEM")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@SuppressWarnings({ "all", "unchecked" })
@XmlRootElement(name="agrupamento_mensagem")
@XmlAccessorType(XmlAccessType.FIELD)
public class AgrupamentoMensagem implements Serializable {
	public AgrupamentoMensagem() {
	}
	
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue(generator="VC0A801031475E28EB240E585")	
	@org.hibernate.annotations.GenericGenerator(name="VC0A801031475E28EB240E585", strategy="native")
	@XmlAttribute
	private Long id;
	
	@OneToOne(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Usuario.class)	
	@org.hibernate.annotations.Cascade({})	
	@JoinColumns({ @JoinColumn(name="USUARIO_TAREFA_ID") })	
	@Basic(fetch=FetchType.EAGER)	
	private br.com.laminarsoft.jazzforms.persistencia.model.Usuario usuarioTarefa;


	@Column(name="RESOLVIDO", nullable=true, length=1)
	@XmlAttribute
	private Boolean resolvido = false;
	
	@Column(name="DH_RESOLVIDO", nullable=true)
	@XmlAttribute
	private Date dh_resolvido;
	
	@OneToMany(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Mensagem.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})	
	@JoinColumn(name="AGRUPAMENTO_MENSAGEM_ID", nullable=true)	
	@org.hibernate.annotations.IndexColumn(name="IDX")	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.FALSE)	
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
	
	public void setResolvido(boolean value) {
		setResolvido(new Boolean(value));
	}
	
	public void setResolvido(Boolean value) {
		this.resolvido = value;
	}
	
	public Boolean getResolvido() {
		return resolvido;
	}
	
	public void setDh_resolvido(Date value) {
		this.dh_resolvido = value;
	}
	
	public Date getDh_resolvido() {
		return dh_resolvido;
	}
	
	public void setMensagens(java.util.List<Mensagem> value) {
		this.mensagens = value;
	}
	
	public java.util.List<Mensagem> getMensagens() {
		return mensagens;
	}
	
	
	public void setUsuarioTarefa(br.com.laminarsoft.jazzforms.persistencia.model.Usuario value) {
		this.usuarioTarefa = value;
	}
	
	public br.com.laminarsoft.jazzforms.persistencia.model.Usuario getUsuarioTarefa() {
		return usuarioTarefa;
	}
	
	public String toString() {
		return String.valueOf(getId());
	}
	
}
