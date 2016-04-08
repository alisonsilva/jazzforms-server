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
@Table(name="SUGESTAO")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="sugestao")
@SuppressWarnings({ "all", "unchecked" })
public class Sugestao implements Serializable, Cloneable {
	public Sugestao() {
	}
	
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue(generator="VAC1E023E14DB495FBF90E98F")	
	@org.hibernate.annotations.GenericGenerator(name="VAC1E023E14DB495FBF90E98F", strategy="native")	
	@XmlAttribute
	private long id;
	
	@ManyToOne(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.TipoSugestao.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})	
	@JoinColumns({ @JoinColumn(name="TIPO_SUGESTAOID", referencedColumnName="ID") })	
	private br.com.laminarsoft.jazzforms.persistencia.model.TipoSugestao tipo;
	
	@Column(name="DETALHE", nullable=true, length=500)	
	private String detalhe;
	
	@Column(name="DH_ENVIO", nullable=true)
	@XmlAttribute
	private java.util.Date dhEnvio;
	
	@Column(name="LIDA", nullable=false, length=1)	
	@XmlAttribute
	private boolean lida = false;
	
	public void setId(long value) {
		this.id = value;
	}
	
	public long getId() {
		return id;
	}
	
	public long getORMID() {
		return getId();
	}
	
	public void setDetalhe(String value) {
		this.detalhe = value;
	}
	
	public String getDetalhe() {
		return detalhe;
	}
	
	public void setDhEnvio(java.util.Date value) {
		this.dhEnvio = value;
	}
	
	public java.util.Date getDhEnvio() {
		return dhEnvio;
	}
	
	public void setLida(boolean value) {
		this.lida = value;
	}
	
	public boolean getLida() {
		return lida;
	}
	
	public void setTipo(br.com.laminarsoft.jazzforms.persistencia.model.TipoSugestao value) {
		this.tipo = value;
	}
	
	public br.com.laminarsoft.jazzforms.persistencia.model.TipoSugestao getTipo() {
		return tipo;
	}
	
	public String toString() {
		return String.valueOf(getId());
	}
	
	public Sugestao clone() {
		Sugestao sug = new Sugestao();
		sug.setDetalhe(this.getDetalhe());
		sug.setTipo(this.getTipo().clone());
		sug.setDhEnvio(this.getDhEnvio());
		sug.setLida(this.getLida());
		return sug;
	}
}
