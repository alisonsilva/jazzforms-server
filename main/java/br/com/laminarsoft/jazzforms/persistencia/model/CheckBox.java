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
package br.com.laminarsoft.jazzforms.persistencia.model;

import java.io.Serializable;
import javax.persistence.*;
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("CheckBox")
@javax.xml.bind.annotation.XmlRootElement(name="check_box")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@SuppressWarnings({ "all", "unchecked" })
public class CheckBox extends br.com.laminarsoft.jazzforms.persistencia.model.Componente implements Serializable {
	public CheckBox() {
	}
	
	public String getTipoComponente() {
		return "CheckBox";
	}
	
	public String toString() {
		return super.toString();
	}

	public CheckBox clone() {
		CheckBox cb = new CheckBox();
		super.clone(this, cb);
		return cb;
	}
}
