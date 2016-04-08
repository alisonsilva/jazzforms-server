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
@DiscriminatorValue("Number")
@javax.xml.bind.annotation.XmlRootElement(name="number")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@XmlDiscriminatorValue("number")
@SuppressWarnings({ "all", "unchecked" })
public class Number extends br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Text implements Serializable {
	public Number() {
	}
	
	@Column(name="MAX_VALUE", nullable=true)	
	@XmlAttribute
	protected Double maxValue;
	
	@Column(name="MIN_VALUE", nullable=true)
	@XmlAttribute
	protected Double minValue;
	
	@Column(name="STEP_VALUE", nullable=true)
	@XmlAttribute
	protected Double stepValue;
	
	public void setMaxValue(double value) {
		setMaxValue(new Double(value));
	}
	
	public void setMaxValue(Double value) {
		this.maxValue = value;
	}
	
	public Double getMaxValue() {
		return maxValue;
	}
	
	public void setMinValue(double value) {
		setMinValue(new Double(value));
	}
	
	public void setMinValue(Double value) {
		this.minValue = value;
	}
	
	public Double getMinValue() {
		return minValue;
	}
	
	public void setStepValue(double value) {
		setStepValue(new Double(value));
	}
	
	public void setStepValue(Double value) {
		this.stepValue = value;
	}
	
	public Double getStepValue() {
		return stepValue;
	}
	
	public String getXType() {
		return "numberfield";
	}
	
	public String toString() {
		return super.toString();
	}
	
	public void clone(Number origem, Number destino) {
		destino.setMaxValue(origem.getMaxValue());
		destino.setMinValue(origem.getMinValue());
		destino.setStepValue(origem.getStepValue());
		super.clone(origem, destino);
	}
	
	public Number clone() {
		Number destino = new Number();
		this.clone(this, destino);
		return destino;
	}
}
