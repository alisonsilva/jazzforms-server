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
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("Hidden")
@javax.xml.bind.annotation.XmlRootElement(name="hidden")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@XmlDiscriminatorValue("hidden")
@SuppressWarnings({ "all", "unchecked" })
public class Hidden extends br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Text implements Serializable {
	public Hidden() {
	}
	
	public String getXType() {
		return "hiddenfield";
	}
	
	public String toString() {
		return super.toString();
	}
	
	public Hidden clone() {
		Hidden destino = new Hidden();
		super.clone(this, destino);
		return destino;
	}
}
