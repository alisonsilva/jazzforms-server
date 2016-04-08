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
@DiscriminatorValue("Password")
@javax.xml.bind.annotation.XmlRootElement(name="password")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@XmlDiscriminatorValue("password")
@SuppressWarnings({ "all", "unchecked" })
public class Password extends br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Text implements Serializable {
	public Password() {
	}
	
	public String getXType() {
		return "passwordfield";
	}
	
	public String toString() {
		return super.toString();
	}
	
	public Password clone() {
		Password destino = new Password();
		super.clone(this, destino);
		return destino;
	}
	
}
