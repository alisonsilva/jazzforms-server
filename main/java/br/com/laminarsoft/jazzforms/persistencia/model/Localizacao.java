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
@Table(name="LOCALIZACAO")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@XmlRootElement(name="localizacao")
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({ "all", "unchecked" })
public class Localizacao implements Serializable, Comparable<Localizacao> {
	public Localizacao() {
	}
	
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue(generator="VC0A82B9C143EE7433AA07810")	
	@org.hibernate.annotations.GenericGenerator(name="VC0A82B9C143EE7433AA07810", strategy="native")
	@XmlAttribute
	private Long id;
	
	@Column(name="LATITUDE", nullable=true)
	@XmlAttribute
	private Double latitude;
	
	@Column(name="LONGITUDE", nullable=true)
	@XmlAttribute
	private Double longitude;
	
	@Column(name="DATA", nullable=true)
	@XmlAttribute
	private java.util.Date data;
	
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
	
	public void setLatitude(double value) {
		setLatitude(new Double(value));
	}
	
	public void setLatitude(Double value) {
		this.latitude = value;
	}
	
	public Double getLatitude() {
		return latitude;
	}
	
	public void setLongitude(double value) {
		setLongitude(new Double(value));
	}
	
	public void setLongitude(Double value) {
		this.longitude = value;
	}
	
	public Double getLongitude() {
		return longitude;
	}
	
	public void setData(java.util.Date value) {
		this.data = value;
	}
	
	public java.util.Date getData() {
		return data;
	}
	
	public String toString() {
		return String.valueOf(getId());
	}

	@Override
    public int compareTo(Localizacao o) {
		if (this.getData() == null) {
			return -1;
		} else if(o == null || o.getData() == null) {
			return 1;
		}
		return this.getData().compareTo(o.getData());
    }
	
}
