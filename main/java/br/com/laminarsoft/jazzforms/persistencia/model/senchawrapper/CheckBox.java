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
@DiscriminatorValue("CheckBox")
@javax.xml.bind.annotation.XmlRootElement(name="checkbox")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@XmlDiscriminatorValue("checkbox")
@SuppressWarnings({ "all", "unchecked" })
public class CheckBox extends br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Field implements Serializable {
	public CheckBox() {
	}
	
	@Column(name="CHECKED", nullable=true, length=1)
	@XmlAttribute
	protected boolean checked = false;
	
	/**
	 * true if the checkbox should render initially checked.
	 */
	public void setChecked(boolean value) {
		this.checked = value;
	}
	
	/**
	 * true if the checkbox should render initially checked.
	 */
	public boolean getChecked() {
		return checked;
	}
	
	public String getXType() {
		return "checkboxfield";
	}
	
	public String toString() {
		return super.toString();
	}
	
	public void clone(CheckBox origem, CheckBox destino) {
		destino.setChecked(origem.getChecked());
		super.clone(origem, destino);
	}
	
	public CheckBox clone() {
		CheckBox destino = new CheckBox();
		this.clone(this, destino);
		return destino;
	}
}
