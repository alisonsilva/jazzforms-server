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
@Table(name="SNCHA_OPTION")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@javax.xml.bind.annotation.XmlRootElement(name="option")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@XmlDiscriminatorValue("option")
@SuppressWarnings({ "all", "unchecked" })
public class Option implements Serializable {
	public Option() {
	}
	
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue(generator="VC0A8380113FF6C299620C1F6")	
	@org.hibernate.annotations.GenericGenerator(name="VC0A8380113FF6C299620C1F6", strategy="native")
	@XmlAttribute
	private Long id;
	
	@Column(name="TEXT", nullable=true, length=60)	
	@XmlAttribute
	private String text = "";
	
	@Column(name="VALUE", nullable=true, length=60)
	@XmlAttribute
	private String value = "";
	
	public void setId(long value) {
		setId(new Long(value));
	}
	
	public void setId(Long value) {
		this.id = value;
	}
	
	public Long getId() {
		return id;
	}
	
	public Long getORMID() {
		return getId();
	}
	
	public void setText(String value) {
		this.text = value;
	}
	
	public String getText() {
		return text;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public String toString() {
		return String.valueOf(text);
	}
	
	public void clone(Option origem, Option destino) {
		destino.setText(new String(origem.getText()));
		destino.setValue(new String(origem.getValue()));
	}
	
	public Option clone() {
		Option destino = new Option();
		this.clone(this, destino);
		return destino;
	}
}
