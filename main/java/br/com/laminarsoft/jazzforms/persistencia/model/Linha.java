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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Table(name="LINHA")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@javax.xml.bind.annotation.XmlRootElement(name="linha")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@SuppressWarnings({ "all", "unchecked" })
public class Linha implements Serializable, Cloneable {
	public Linha() {
	}
	
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue(generator="VC0A8010714336EE92F90E3CB")	
	@org.hibernate.annotations.GenericGenerator(name="VC0A8010714336EE92F90E3CB", strategy="native")	
	@XmlAttribute
	private Long id;
	
	@Column(name="ROWNUM", nullable=true, length=3)	
	private Long numero;
	
	@OneToMany(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Campo.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})	
	@JoinColumn(name="LINHA_ID", nullable=true)	
	@org.hibernate.annotations.IndexColumn(name="IDX")	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.FALSE)	
	private java.util.List<Campo> campos = new java.util.ArrayList<Campo>();
	
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
	
	public void setNumero(long value) {
		setNumero(new Long(value));
	}
	
	public void setNumero(Long value) {
		this.numero = value;
	}
	
	public Long getNumero() {
		return numero;
	}
	
	public void setCampos(java.util.List<Campo> value) {
		this.campos = value;
	}
	
	public java.util.List<Campo> getCampos() {
		return campos;
	}
	
	
	public String toString() {
		return String.valueOf(getId());
	}
	

	@Override
    public Linha clone() {
		Linha newLinha = new Linha();
		newLinha.setId(null);
		newLinha.setNumero(null);
		for(Campo campo : this.getCampos()) {
			newLinha.getCampos().add(campo.clone());
		}
		return newLinha;
		
    }
	
	
	
}
