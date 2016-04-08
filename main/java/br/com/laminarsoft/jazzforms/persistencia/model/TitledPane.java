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
@DiscriminatorValue("TitledPane")
@javax.xml.bind.annotation.XmlRootElement(name="titled_pane")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@SuppressWarnings({ "all", "unchecked" })
public class TitledPane extends br.com.laminarsoft.jazzforms.persistencia.model.Componente implements Serializable {
	public TitledPane() {
	}
	
	@OneToMany(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Componente.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})	
	@JoinColumn(name="TITLED_PANE_ID", nullable=true)	
	@org.hibernate.annotations.IndexColumn(name="IDX")	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.FALSE)	
	private java.util.List<Componente> conteudo = new java.util.ArrayList<Componente>();
	
	public void setConteudo(java.util.List<Componente> value) {
		this.conteudo = value;
	}
	
	public java.util.List<Componente> getConteudo() {
		return conteudo;
	}
	
	
	public String getTipoComponente() {
		return "TitledPane";
	}
	
	public String toString() {
		return super.toString();
	}
	
	public TitledPane clone() {
		TitledPane tp = new TitledPane();
		super.clone(this, tp);
		for(Componente c : this.conteudo) {
			tp.conteudo.add(c);
		}
		return tp;
	}
}
