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
@Table(name="CAMPO")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@javax.xml.bind.annotation.XmlRootElement(name="campo")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@SuppressWarnings({ "all", "unchecked" })
public class Campo implements Serializable, Comparable<Campo>, Cloneable {
	public Campo() {
	}
	
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue(generator="VC0A801071433A34910D02CFE")	
	@org.hibernate.annotations.GenericGenerator(name="VC0A801071433A34910D02CFE", strategy="native")	
	@XmlAttribute
	private Long id;
	
	@Column(name="NOME_CAMPO", nullable=true, length=100)
	@XmlAttribute
	private String nomeCampo;
	
	@Column(name="VALOR_CAMPO", nullable=true, length=255)
	@XmlAttribute
	private String valorCampo;
	
	@Override
    public int compareTo(Campo o) {
	    return this.getId().compareTo(o.getId());
    }

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
	
	public void setNomeCampo(String value) {
		this.nomeCampo = value;
	}
	
	public String getNomeCampo() {
		return nomeCampo;
	}
	
	public void setValorCampo(String value) {
		this.valorCampo = value;
	}
	
	public String getValorCampo() {
		return valorCampo;
	}
	
	public String toString() {
		return valorCampo;
	}

	@Override
    public Campo clone() {
	    Campo newcampo = new Campo();
	    newcampo.setNomeCampo(this.getNomeCampo());
	    newcampo.setValorCampo(this.getValorCampo());
	    return newcampo;
    }
	
	
	
}
