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

import org.eclipse.persistence.oxm.annotations.XmlTransformation;
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Table(name="VALOR_DATAVIEW")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@javax.xml.bind.annotation.XmlRootElement(name="valordataview")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@SuppressWarnings({ "all", "unchecked" })
public class ValorDataview implements Serializable {
	public ValorDataview() {
	}
	
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue(generator="VC0A8010714336EE92EF0E3CA")	
	@org.hibernate.annotations.GenericGenerator(name="VC0A8010714336EE92EF0E3CA", strategy="native")
	@XmlAttribute
	private Long id;
	
	@OneToOne(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Component.class)	
	@org.hibernate.annotations.Cascade({})	
	@JoinColumns({ @JoinColumn(name="SNCHA_COMPONENTID") })	
	@Basic(fetch=FetchType.LAZY)	
	@XmlTransient
	private br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Component dataView;
	
	@OneToMany(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Linha.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})	
	@JoinColumn(name="VALOR_DATAVIEWID", nullable=true)	
	@org.hibernate.annotations.IndexColumn(name="IDX")	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.FALSE)	
	private java.util.List<Linha> rows = new java.util.ArrayList<Linha>();
	
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
	
	public void setDataView(br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Component value) {
		this.dataView = value;
	}
	
	public br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Component getDataView() {
		return dataView;
	}
	
	public void setRows(java.util.List<Linha> value) {
		this.rows = value;
	}
	
	public java.util.List<Linha> getRows() {
		return rows;
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
