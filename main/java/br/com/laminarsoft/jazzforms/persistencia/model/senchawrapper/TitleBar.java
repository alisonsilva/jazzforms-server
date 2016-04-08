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
@DiscriminatorValue("TitleBar")
@javax.xml.bind.annotation.XmlRootElement(name="titlebar")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@XmlDiscriminatorValue("titlebar")
@SuppressWarnings({ "all", "unchecked" })
public class TitleBar extends br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Container implements Serializable {
	public TitleBar() {
	}
	
	@Column(name="TITLEBAR_TITLE", nullable=true, length=120)	
	private String title = "";
	
	public void setTitle(String value) {
		this.title = value;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getXType() {
		return "titlebar";
	}
	
	public String toString() {
		return super.toString();
	}
	
	public void clone(TitleBar origem, TitleBar destino) {
		destino.setTitle(new String(origem.getTitle()));
		super.clone(origem, destino);
	}
	
	public TitleBar clone() {
		TitleBar destino = new TitleBar();
		this.clone(this, destino);
		return destino;
	}
}
