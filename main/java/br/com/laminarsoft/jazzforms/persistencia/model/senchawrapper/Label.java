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

import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;
/**
 * A simple label component which allows you to insert content using html configuration.
 */
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("Label")
@javax.xml.bind.annotation.XmlRootElement(name="label")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@XmlDiscriminatorValue("label")
@SuppressWarnings({ "all", "unchecked" })
public class Label extends br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Component implements Serializable {
	public Label() {
	}
	
	public String getXType() {
		return "label";
	}
	
	public String toString() {
		return super.toString();
	}
	
	public void clone(Label origem, Label destino) {
		super.clone(origem, destino);
	}
	
	public Label clone() {
		Label destino = new Label();
		this.clone(this, destino);
		return destino;
	}
}
