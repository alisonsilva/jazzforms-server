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
@DiscriminatorValue("Label")
@javax.xml.bind.annotation.XmlRootElement(name="label")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@SuppressWarnings({ "all", "unchecked" })
public class Label extends br.com.laminarsoft.jazzforms.persistencia.model.Componente implements Serializable {
	public Label() {
	}
	
	public String getTipoComponente() {
		return "Label";
	}
	
	public String toString() {
		return super.toString();
	}

	
	public Label clone() {
		Label l = new Label();
		super.clone(this, l);
		return l;
	}
}
