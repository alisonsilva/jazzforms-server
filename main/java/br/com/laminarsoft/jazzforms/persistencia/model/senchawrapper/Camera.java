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
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("Camera")
@XmlRootElement(name="camera")
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({ "all", "unchecked" })
public class Camera extends br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Component implements Serializable {
	public Camera() {
	}
	
	@Column(name="CAMERA_LABEL", nullable=true, length=60)
	@XmlAttribute
	private String label = "";
	
	@Column(name="PICT_QUANTITY", nullable=true, length=2)
	@XmlAttribute
	private Integer quantity = new Integer(1);
	
	@Column(name="PICT_DESTINATION", nullable=true, length=30)
	@XmlAttribute
	private String destination = "data";
	
	@Column(name="PICT_ENCODING", nullable=true, length=30)
	@XmlAttribute
	private String encoding = "png";
	
	@Column(name="PICT_WIDTH", nullable=true, length=4)
	@XmlAttribute
	private Integer pictWidth = new Integer(200);
	
	@Column(name="PICT_HEIGHT", nullable=true, length=4)
	@XmlAttribute
	private Integer pictHeight = new Integer(200);
	
	@Column(name="PICT_SOURCE", nullable=true, length=30)
	@XmlAttribute
	private String source = "camera";
	
	public void setLabel(String value) {
		this.label = value;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setQuantity(int value) {
		setQuantity(new Integer(value));
	}
	
	public void setQuantity(Integer value) {
		this.quantity = value;
	}
	
	public Integer getQuantity() {
		return quantity;
	}
	
	/**
	 * Podendo ser data (default) ou file
	 */
	public void setDestination(String value) {
		this.destination = value;
	}
	
	/**
	 * Podendo ser data (default) ou file
	 */
	public String getDestination() {
		return destination;
	}
	
	/**
	 * Podendo ser png (default) ou jpb
	 */
	public void setEncoding(String value) {
		this.encoding = value;
	}
	
	/**
	 * Podendo ser png (default) ou jpb
	 */
	public String getEncoding() {
		return encoding;
	}
	
	public void setPictWidth(int value) {
		setPictWidth(new Integer(value));
	}
	
	public void setPictWidth(Integer value) {
		this.pictWidth = value;
	}
	
	public Integer getPictWidth() {
		return pictWidth;
	}
	
	public void setPictHeight(int value) {
		setPictHeight(new Integer(value));
	}
	
	public void setPictHeight(Integer value) {
		this.pictHeight = value;
	}
	
	public Integer getPictHeight() {
		return pictHeight;
	}
	
	/**
	 * podendo ser:
	 * 
	 * album - prompts the user to choose an image from an album
	 * camera - prompts the user to take a new photo (Default)
	 * library - prompts the user to choose an image from the library
	 *  
	 */
	public void setSource(String value) {
		this.source = value;
	}
	
	/**
	 * podendo ser:
	 * 
	 * album - prompts the user to choose an image from an album
	 * camera - prompts the user to take a new photo (Default)
	 * library - prompts the user to choose an image from the library
	 *  
	 */
	public String getSource() {
		return source;
	}
	

	
	public String toString() {
		return super.toString();
	}
	
	public String getXType() {
		return "camera";
	}	
	
	public Camera clone() {
		Camera newCamera = new Camera();
		super.clone(this, newCamera);

		newCamera.label = new String(this.label);
		newCamera.quantity = this.quantity.intValue();
		newCamera.destination = new String(this.destination);
		newCamera.encoding = new String(this.encoding);
		newCamera.pictWidth = this.pictWidth.intValue();
		newCamera.pictHeight = this.pictHeight.intValue();
		newCamera.source = new String(this.source);
		return newCamera;
	}
}
