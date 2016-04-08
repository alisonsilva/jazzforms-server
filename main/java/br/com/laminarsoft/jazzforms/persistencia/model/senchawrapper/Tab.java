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
@Table(name="TAB")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@javax.xml.bind.annotation.XmlRootElement(name="tab")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@SuppressWarnings({ "all", "unchecked" })
public class Tab implements Serializable {
	public Tab() {
	}
	
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue(generator="VC0A83801140D4273B9D0521B")	
	@org.hibernate.annotations.GenericGenerator(name="VC0A83801140D4273B9D0521B", strategy="native")	
	@XmlAttribute
	private long id;
	
	@Column(name="TEXTO", nullable=true, length=30)
	@XmlAttribute
	private String text = "";
	
	@Column(name="IMAGEM", nullable=true, length=30)
	@XmlAttribute
	private String imagem = "";
	
	@OneToMany(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Component.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})	
	@JoinColumn(name="TAB_ID", nullable=true)	
	@org.hibernate.annotations.IndexColumn(name="IDX")	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.FALSE)	
	private java.util.List<Component> items = new java.util.ArrayList<Component>();
	
	public void setId(long value) {
		this.id = value;
	}
	
	public long getId() {
		return id;
	}
	
	public long getORMID() {
		return getId();
	}
	
	public void setText(String value) {
		this.text = value;
	}
	
	public String getText() {
		return text;
	}
	
	public void setImagem(String value) {
		this.imagem = value;
	}
	
	public String getImagem() {
		return imagem;
	}
	
	public void setItems(java.util.List<Component> value) {
		this.items = value;
	}
	
	public java.util.List<Component> getItems() {
		return items;
	}
	
	
	@Transient	
	public long hsId = 0;
	
	public String toString() {
		if (hsId > 0) {
			return String.valueOf(hsId);
		}
		return String.valueOf(getId());
	}
	
	
	public Tab clone() {
		Tab destino = new Tab();
		destino.setImagem(new String(this.getImagem()));
		destino.setText(new String(this.getText().toString()));
		destino.hsId = this.hsId;
		for(Component comp : this.getItems()) {
			destino.getItems().add(comp.clone());
		}
		return destino;
	}
}
