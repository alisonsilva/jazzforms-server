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
@Table(name="FOTO")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@XmlRootElement(name="foto")
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({ "all", "unchecked" })
public class Foto implements Serializable {
	public Foto() {
	}
	
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue(generator="VC0A80103144BBBF724502AA4")	
	@org.hibernate.annotations.GenericGenerator(name="VC0A80103144BBBF724502AA4", strategy="native")
	@XmlAttribute
	private Long id;
	
	@OneToOne(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Component.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.LOCK})	
	@JoinColumns({ @JoinColumn(name="CAMERA_ID") })	
	@Basic(fetch=FetchType.LAZY)	
	private br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Component camera;
	
	@Column(name="PICTURE", nullable=false)	
	private byte[] picture;
	
	@Column(name="DH_PICTURE", nullable=true)
	@XmlAttribute
	private java.util.Date dhPicture;
	
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
	
	public void setPicture(byte[] value) {
		this.picture = value;
	}
	
	public byte[] getPicture() {
		return picture;
	}
	
	public void setDhPicture(java.util.Date value) {
		this.dhPicture = value;
	}
	
	public java.util.Date getDhPicture() {
		return dhPicture;
	}
	
	public void setCamera(br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Component value) {
		this.camera = value;
	}
	
	public br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Component getCamera() {
		return camera;
	}
	
	
	
	public Long getDhPictureLng() {
		return dhPictureLng;
	}

	public void setDhPictureLng(Long dhPictureLng) {
		this.dhPictureLng = dhPictureLng;
	}

	@Transient public Long idCameraOriginal;
	@Transient public Long dhPictureLng;
	@Transient public String pictStr;	
	
	public String getPictStr() {
		return pictStr;
	}

	public void setPictStr(String pictStr) {
		this.pictStr = pictStr;
	}

	public Long getIdCameraOriginal() {
		return idCameraOriginal;
	}

	public void setIdCameraOriginal(Long idCameraOriginal) {
		this.idCameraOriginal = idCameraOriginal;
	}

	public String toString() {
		return String.valueOf(getId());
	}
	
}
