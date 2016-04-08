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
package br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("GPSField")
@XmlRootElement(name="gps")
@XmlDiscriminatorValue("gps")
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({ "all", "unchecked" })
public class GPSField extends br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Hidden implements Serializable {
	public GPSField() {
	}
	
	@Column(name="LATITUDE", nullable=true, length=255)	
	@XmlAttribute
	private String latitude;
	
	@Column(name="LONGITUDE", nullable=true, length=255)
	@XmlAttribute
	private String longitude;
	
	public void setLatitude(String value) {
		this.latitude = value;
	}
	
	public String getLatitude() {
		return latitude;
	}
	
	public void setLongitude(String value) {
		this.longitude = value;
	}
	
	public String getLongitude() {
		return longitude;
	}
	
	public String getXType() {
		return "gps";
	}
	
	public String toString() {
		return super.toString();
	}
	
	public GPSField clone() {
		GPSField ret = new GPSField();
		ret.setLatitude(this.getLatitude() == null ? null : new String(this.getLatitude()));
		ret.setLongitude(this.getLongitude() == null ? null : new String(this.getLongitude()));
		super.clone(this, ret);
		return ret;
	}
	
}
