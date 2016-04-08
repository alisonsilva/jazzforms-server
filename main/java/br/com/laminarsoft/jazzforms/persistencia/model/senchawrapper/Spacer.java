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
 * The Ext.Spacer component is generally used to put space between items in Ext.Toolbar components.
 */
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("Spacer")
@javax.xml.bind.annotation.XmlRootElement(name="spacer")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@XmlDiscriminatorValue("spacer")
@SuppressWarnings({ "all", "unchecked" })
public class Spacer extends br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Component implements Serializable {
	public Spacer() {
	}
	
	public String getXType() {
		return "spacer";
	}
	
	public String toString() {
		return super.toString();
	}
	
	public void clone(Spacer origem, Spacer destino) {
		super.clone(origem, destino);
	}
	
	public Spacer clone() {
		Spacer destino = new Spacer();
		this.clone(this, destino);
		return destino;
	}
	
}
