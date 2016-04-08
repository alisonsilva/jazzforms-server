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
@Table(name="ICON")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@javax.xml.bind.annotation.XmlRootElement(name="icon")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@XmlDiscriminatorValue("icon")
@SuppressWarnings({ "all", "unchecked" })
public class Icon implements Serializable {
	public Icon() {
	}
	
	@Column(name="Id", nullable=false)	
	@Id	
	@GeneratedValue(generator="VAC1154891405ABAE64C0CB0F")	
	@org.hibernate.annotations.GenericGenerator(name="VAC1154891405ABAE64C0CB0F", strategy="native")
	@XmlAttribute
	private long id;
	
	@Column(name="NOME", nullable=true, length=60)	
	private String nome = "";
	
	@Column(name="ICON24", nullable=true)	
	private byte[] icon24;
	
	@Column(name="ICON16", nullable=true)	
	private byte[] icon16;
	
	public void setId(long value) {
		this.id = value;
	}
	
	public long getId() {
		return id;
	}
	
	public long getORMID() {
		return getId();
	}
	
	public void setNome(String value) {
		this.nome = value;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setIcon24(byte[] value) {
		this.icon24 = value;
	}
	
	public byte[] getIcon24() {
		return icon24;
	}
	
	public void setIcon16(byte[] value) {
		this.icon16 = value;
	}
	
	public byte[] getIcon16() {
		return icon16;
	}
	
	public String toString() {
		return String.valueOf(getId());
	}
	
}
