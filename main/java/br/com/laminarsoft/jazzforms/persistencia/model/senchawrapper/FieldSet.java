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
 * A FieldSet is a great way to visually separate elements of a form. It's normally used when you have a form with fields that can be divided into groups - for example a customer's billing details in one fieldset and their shipping address in another. A fieldset can be used inside a form or on its own elsewhere in your app. Fieldsets can optionally have a title at the top and instructions at the bottom. Here's how we might create a FieldSet inside a form:
 */
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("FieldSet")
@javax.xml.bind.annotation.XmlRootElement(name="fieldset")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@XmlDiscriminatorValue("fieldset")
@SuppressWarnings({ "all", "unchecked" })
public class FieldSet extends br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Container implements Serializable {
	public FieldSet() {
	}
	
	@Column(name="INSTRUCTIONS", nullable=true, length=255)	
	private String instructions = "";
	
	@Column(name="FIELDSET_TITLE", nullable=true, length=120)	
	private String title = "";
	
	public void setInstructions(String value) {
		this.instructions = value;
	}
	
	public String getInstructions() {
		return instructions;
	}
	
	public void setTitle(String value) {
		this.title = value;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getXType() {
		return "fieldset";
	}
	
	public String toString() {
		return super.toString();
	}
	
	public void clone(FieldSet origem, FieldSet destino) {
		destino.setInstructions(new String(origem.getInstructions()));
		destino.setTitle(new String(origem.getTitle()));
		super.clone((Container)origem, (Container)destino);
	}
	
	public FieldSet clone() {
		FieldSet destino = new FieldSet();
		this.clone(this, destino);
		return destino;
	}
}
