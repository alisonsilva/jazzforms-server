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
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("Slider")
@javax.xml.bind.annotation.XmlRootElement(name="slider")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@XmlDiscriminatorValue("slider")
@SuppressWarnings({ "all", "unchecked" })
public class Slider extends br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Field implements Serializable {
	public Slider() {
	}
	
	@Column(name="INCREMENT", nullable=true, length=11)	
	@XmlAttribute
	private Integer increment = new Integer(1);
	
	/**
	 * The increment by which to snap each thumb when its value changes. Any thumb movement will be snapped to the nearest value that is a multiple of the increment (e.g. if increment is 10 and the user tries to move the thumb to 67, it will be snapped to 70 instead)
	 * Defaults to: 1
	 */
	public void setIncrement(int value) {
		setIncrement(new Integer(value));
	}
	
	/**
	 * The increment by which to snap each thumb when its value changes. Any thumb movement will be snapped to the nearest value that is a multiple of the increment (e.g. if increment is 10 and the user tries to move the thumb to 67, it will be snapped to 70 instead)
	 * Defaults to: 1
	 */
	public void setIncrement(Integer value) {
		this.increment = value;
	}
	
	/**
	 * The increment by which to snap each thumb when its value changes. Any thumb movement will be snapped to the nearest value that is a multiple of the increment (e.g. if increment is 10 and the user tries to move the thumb to 67, it will be snapped to 70 instead)
	 * Defaults to: 1
	 */
	public Integer getIncrement() {
		return increment;
	}
	
	public String getXType() {
		return "sliderfield";
	}
	
	public String toString() {
		return super.toString();
	}
	
	public void clone(Slider origem, Slider destino) {
		destino.setIncrement(origem.getIncrement());
		super.clone(origem, destino);
	}
	
	public Slider clone() {
		Slider destino = new Slider();
		this.clone(this, destino);
		return destino;
	}
	
}
