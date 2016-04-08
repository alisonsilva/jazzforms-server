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
import javax.xml.bind.annotation.XmlAttribute;

import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;
/**
 * This is a simple way to add an image of any size to your application and have it participate in the layout system like any other component. This component typically takes between 1 and 3 configurations - a src, and optionally a height and a width:
 */
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("Image")
@javax.xml.bind.annotation.XmlRootElement(name="image")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@XmlDiscriminatorValue("image")
@SuppressWarnings({ "all", "unchecked" })
public class Image extends br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Component implements Serializable {
	public Image() {
	}
	
	@Column(name="BACKGROUN_CLS", nullable=true, length=80)
	@XmlAttribute
	private String backgroundCls = "";
	
	@Column(name="IMG_CLS", nullable=true, length=80)
	@XmlAttribute
	private String imgCls = "";
	
	@Column(name="IMAGE_MODE", nullable=true, length=80)
	@XmlAttribute
	private String imageMode = "";
	
	@Column(name="SRC", nullable=true, length=255)	
	private String src = "";
	
	/**
	 * The CSS class to be used when mode is set to 'background'
	 * Defaults to: Ext.baseCSSPrefix + 'img-background'
	 */
	public void setBackgroundCls(String value) {
		this.backgroundCls = value;
	}
	
	/**
	 * The CSS class to be used when mode is set to 'background'
	 * Defaults to: Ext.baseCSSPrefix + 'img-background'
	 */
	public String getBackgroundCls() {
		return backgroundCls;
	}
	
	/**
	 * The CSS class to be used when mode is not set to 'background'
	 * Defaults to: Ext.baseCSSPrefix + 'img-image'
	 */
	public void setImgCls(String value) {
		this.imgCls = value;
	}
	
	/**
	 * The CSS class to be used when mode is not set to 'background'
	 * Defaults to: Ext.baseCSSPrefix + 'img-image'
	 */
	public String getImgCls() {
		return imgCls;
	}
	
	/**
	 * If set to 'background', uses a background-image CSS property instead of an <img> tag to display the image.
	 * Defaults to: 'background'
	 */
	public void setImageMode(String value) {
		this.imageMode = value;
	}
	
	/**
	 * If set to 'background', uses a background-image CSS property instead of an <img> tag to display the image.
	 * Defaults to: 'background'
	 */
	public String getImageMode() {
		return imageMode;
	}
	
	/**
	 * The source of this image
	 */
	public void setSrc(String value) {
		this.src = value;
	}
	
	/**
	 * The source of this image
	 */
	public String getSrc() {
		return src;
	}
	
	public String getXType() {
		return "image";
	}
	
	public String toString() {
		return super.toString();
	}
	
	public void clone(Image origem, Image destino) {
		destino.setBackgroundCls(new String(origem.getBackgroundCls()));
		destino.setImgCls(new String(origem.getImgCls()));
		destino.setImageMode(new String(origem.getImageMode()));
		destino.setSrc(new String(origem.getSrc()));
		super.clone(origem, destino);
	}
	
	public Image clone() {
		Image destino = new Image();
		this.clone(this, destino);
		return destino;
	}
}
