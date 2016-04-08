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
@DiscriminatorValue("Spinner")
@javax.xml.bind.annotation.XmlRootElement(name="spinner")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@XmlDiscriminatorValue("spinner")
@SuppressWarnings({ "all", "unchecked" })
public class Spinner extends br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Number implements Serializable {
	public Spinner() {
	}
	
	@Column(name="ACCELERATE_ON_TAP_HOLD", nullable=true, length=1)
	@XmlAttribute
	private boolean accelerateOnTapHold = true;
	
	@Column(name="CYDE", nullable=true, length=1)
	@XmlAttribute
	private boolean cycle = false;
	
	@Column(name="DEFAULT_VALUE", nullable=true)
	@XmlAttribute
	private Double defaultValue = new Double(0);
	
	@Column(name="GROUP_BUTTONS", nullable=true, length=1)
	@XmlAttribute
	private boolean groupButtons = true;
	
	/**
	 * True if autorepeating should start slowly and accelerate.
	 * Defaults to: true
	 */
	public void setAccelerateOnTapHold(boolean value) {
		this.accelerateOnTapHold = value;
	}
	
	/**
	 * True if autorepeating should start slowly and accelerate.
	 * Defaults to: true
	 */
	public boolean getAccelerateOnTapHold() {
		return accelerateOnTapHold;
	}
	
	/**
	 * When set to true, it will loop the values of a minimum or maximum is reached. If the maximum value is reached, the value will be set to the minimum.
	 * Defaults to: false
	 */
	public void setCycle(boolean value) {
		this.cycle = value;
	}
	
	/**
	 * When set to true, it will loop the values of a minimum or maximum is reached. If the maximum value is reached, the value will be set to the minimum.
	 * Defaults to: false
	 */
	public boolean getCycle() {
		return cycle;
	}
	
	/**
	 * The default value for this field when no value has been set. It is also used when the value is set to NaN.
	 * Defaults to: 0
	 */
	public void setDefaultValue(double value) {
		setDefaultValue(new Double(value));
	}
	
	/**
	 * The default value for this field when no value has been set. It is also used when the value is set to NaN.
	 * Defaults to: 0
	 */
	public void setDefaultValue(Double value) {
		this.defaultValue = value;
	}
	
	/**
	 * The default value for this field when no value has been set. It is also used when the value is set to NaN.
	 * Defaults to: 0
	 */
	public Double getDefaultValue() {
		return defaultValue;
	}
	
	/**
	 * The default value for this field when no value has been set. It is also used when the value is set to NaN.
	 * Defaults to: 0
	 */
	public void setGroupButtons(boolean value) {
		this.groupButtons = value;
	}
	
	/**
	 * The default value for this field when no value has been set. It is also used when the value is set to NaN.
	 * Defaults to: 0
	 */
	public boolean getGroupButtons() {
		return groupButtons;
	}
	
	public String getXType() {
		return "spinnerfield";
	}
	
	public String toString() {
		return super.toString();
	}
	
	public void clone(Spinner origem, Spinner destino) {
		destino.setAccelerateOnTapHold(origem.getAccelerateOnTapHold());
		destino.setCycle(origem.getCycle());
		destino.setDefaultValue(origem.getDefaultValue().doubleValue());
		destino.setGroupButtons(origem.getGroupButtons());
		super.clone(origem, destino);
	}
	
	public Spinner clone() {
		Spinner destino = new Spinner();
		this.clone(this, destino);
		return destino;
	}
}
