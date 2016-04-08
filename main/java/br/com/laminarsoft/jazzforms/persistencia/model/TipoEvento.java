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

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Table(name="TIPO_EVENTO")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@javax.xml.bind.annotation.XmlRootElement(name="tipo_evento")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@SuppressWarnings({ "all", "unchecked" })
public class TipoEvento implements Serializable {
	public TipoEvento() {
	}
	
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue(generator="VC0A8380113E3C7E330D06E2B")	
	@org.hibernate.annotations.GenericGenerator(name="VC0A8380113E3C7E330D06E2B", strategy="native")	
	@XmlAttribute
	private long id;
	
	@ManyToOne(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.ComponentType.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})	
	@JoinColumns({ @JoinColumn(name="COMPONENT_TYPE_ID", referencedColumnName="ID") })	
	@XmlTransient
	private br.com.laminarsoft.jazzforms.persistencia.model.ComponentType component;
	
	@Column(name="NOME", nullable=false, length=100)
	@XmlAttribute
	private String nome;
	
	@Column(name="DESCRICAO", nullable=true, length=512)	
	private String descricao;
	
	@Column(name="ASSINATURA", nullable=true, length=512)	
	private String assinatura;
	
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
	
	public void setAssinatura(String value) {
		this.assinatura = value;
	}
	
	public String getAssinatura() {
		return assinatura;
	}
	
	public void setComponent(br.com.laminarsoft.jazzforms.persistencia.model.ComponentType value) {
		this.component = value;
	}
	
	public br.com.laminarsoft.jazzforms.persistencia.model.ComponentType getComponent() {
		return component;
	}
	
	public String toString() {
		return String.valueOf(getId());
	}
	
}
