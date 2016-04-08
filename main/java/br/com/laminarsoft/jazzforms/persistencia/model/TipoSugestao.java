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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Table(name="TIPO_SUGESTAO")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="tipo_sugestao")
@SuppressWarnings({ "all", "unchecked" })
public class TipoSugestao implements Serializable, Cloneable {
	public TipoSugestao() {
	}
	
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue(generator="VAC1E023E14DB495FC180E990")	
	@org.hibernate.annotations.GenericGenerator(name="VAC1E023E14DB495FC180E990", strategy="native")
	@XmlAttribute
	private long id;
	
	@Column(name="CO_TIPO", nullable=false, length=3)
	@XmlAttribute
	private int codTipo;
	
	@Column(name="DESCRICAO_TIPO", nullable=true, length=120)	
	private String descricaoTipo;
	
	public void setId(long value) {
		this.id = value;
	}
	
	public long getId() {
		return id;
	}
	
	public long getORMID() {
		return getId();
	}
	
	public void setCodTipo(int value) {
		this.codTipo = value;
	}
	
	public int getCodTipo() {
		return codTipo;
	}
	
	public void setDescricaoTipo(String value) {
		this.descricaoTipo = value;
	}
	
	public String getDescricaoTipo() {
		return descricaoTipo;
	}
	
	public String toString() {
		return String.valueOf(getId());
	}

	@Override
	public TipoSugestao clone() {
		TipoSugestao novoTipo = new TipoSugestao();
		novoTipo.setCodTipo(this.getCodTipo());
		novoTipo.setDescricaoTipo(this.getDescricaoTipo());
		return novoTipo;
	}
	
	
	
}
