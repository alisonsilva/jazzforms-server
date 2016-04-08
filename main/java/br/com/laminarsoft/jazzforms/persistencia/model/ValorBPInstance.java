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
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Table(name="VALOR_BP_INSTANCE")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@SuppressWarnings({ "all", "unchecked" })
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="valor_bp_instance")
public class ValorBPInstance implements Serializable, Cloneable {
	public ValorBPInstance() {
	}
	
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue(generator="VC0A8010214611E90E740144E")	
	@org.hibernate.annotations.GenericGenerator(name="VC0A8010214611E90E740144E", strategy="native")	
	@XmlAttribute
	private long id;
	
	@Column(name="ID_CAMPO", nullable=true, length=80)	
	@XmlAttribute
	private String identificadorCampo;
	
	@Column(name="VALOR_CAMPO", nullable=true, length=255)	
	private String valorCampo;
	
	@Column(name="DH_ALTERACAO", nullable=true)
	@XmlAttribute
	private java.util.Date dhAlteracao;
	
	public void setId(long value) {
		this.id = value;
	}
	
	public long getId() {
		return id;
	}
	
	public long getORMID() {
		return getId();
	}
	
	public void setIdentificadorCampo(String value) {
		this.identificadorCampo = value;
	}
	
	public String getIdentificadorCampo() {
		return identificadorCampo;
	}
	
	public void setValorCampo(String value) {
		this.valorCampo = value;
	}
	
	public String getValorCampo() {
		return valorCampo;
	}
	
	public void setDhAlteracao(java.util.Date value) {
		this.dhAlteracao = value;
	}
	
	public java.util.Date getDhAlteracao() {
		return dhAlteracao;
	}
	
	public String toString() {
		return String.valueOf(getId());
	}

	@Override
	public ValorBPInstance clone() {
		ValorBPInstance vlr = new ValorBPInstance();
		vlr.setId(this.getId());
		vlr.setDhAlteracao(new java.util.Date(this.getDhAlteracao().getTime()));
		vlr.setIdentificadorCampo(this.getIdentificadorCampo().toString());
		vlr.setValorCampo(this.getValorCampo());
		return vlr;
	}
	
	
	
}
