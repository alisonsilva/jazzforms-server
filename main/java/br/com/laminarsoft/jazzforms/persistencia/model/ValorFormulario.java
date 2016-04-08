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
@Table(name="VALOR_FORMULARIO")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@javax.xml.bind.annotation.XmlRootElement(name="valorformulario")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@SuppressWarnings({ "all", "unchecked" })
public class ValorFormulario implements Serializable {
	public ValorFormulario() {
	}
	
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue(generator="VC0A8010714336EE92E00E3C9")	
	@org.hibernate.annotations.GenericGenerator(name="VC0A8010714336EE92E00E3C9", strategy="native")
	@XmlAttribute
	private Long id;
	
	@ManyToOne(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Component.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})	
	@JoinColumns({ @JoinColumn(name="SNCHA_COMPONENTID", referencedColumnName="ID") })	
	@Basic(fetch=FetchType.LAZY)	
	private br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Component componente;
	
	@Column(name="VALOR", nullable=true, length=255)	
	@XmlAttribute
	private String valor;
	
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
	
	public void setValor(String value) {
		this.valor = value;
	}
	
	public String getValor() {
		return valor;
	}
	
	public void setComponente(br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Component value) {
		this.componente = value;
	}
	
	public br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Component getComponente() {
		return componente;
	}
	
	@Transient	
	private Integer formfieldId;
	
	@Transient	
	private Long dhPreenchimento;
	
	public Long getDhPreenchimento() {
		return dhPreenchimento;
	}
	
	public void setDhPreenchimento(Long aDhPreenchimento) {
		dhPreenchimento = aDhPreenchimento;
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
	private String fieldId;
	
	public String getFieldId() {
		return fieldId;
	}
	
	public void setFieldId(String aFieldId) {
		fieldId = aFieldId;
	}
	
	@Transient	
	private Long idOriginal;
	
	public Long getIdOriginal() {
		return idOriginal;
	}
	
	public void setIdOriginal(Long aIdOriginal) {
		idOriginal = aIdOriginal;
	}
	
	public String toString() {
		return String.valueOf(getId());
	}
	
}
