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
@DiscriminatorValue("TextArea")
@javax.xml.bind.annotation.XmlRootElement(name="textarea")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@XmlDiscriminatorValue("textarea")
@SuppressWarnings({ "all", "unchecked" })
public class TextArea extends br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Text implements Serializable {
	public TextArea() {
	}
	
	@Column(name="MAX_ROWS", nullable=true, length=11)	
	@XmlAttribute
	private Integer maxRows = 4;
	
	public void setMaxRows(int value) {
		setMaxRows(new Integer(value));
	}
	
	public void setMaxRows(Integer value) {
		this.maxRows = value;
	}
	
	public Integer getMaxRows() {
		return maxRows;
	}
	
	public String getXType() {
		return "textareafield";
	}
	
	public String toString() {
		return super.toString();
	}

	public void clone(TextArea origem, TextArea destino) {
		destino.setMaxRows(origem.getMaxRows());
		super.clone(origem, destino);
	}
	
	public TextArea clone() {
		TextArea destino = new TextArea();
		this.clone(this, destino);
		return destino;
	}
}
