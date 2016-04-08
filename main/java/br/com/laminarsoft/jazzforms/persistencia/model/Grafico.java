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
@DiscriminatorValue("Grafico")
@javax.xml.bind.annotation.XmlRootElement(name="grafico")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@SuppressWarnings({ "all", "unchecked" })
public class Grafico extends br.com.laminarsoft.jazzforms.persistencia.model.Componente implements Serializable {
	public Grafico() {
	}
	
	@Column(name="TIPO_GRAFICO", nullable=true, length=1)	
	private int tipoGrafico;
	
	public void setTipoGrafico(int value) {
		this.tipoGrafico = value;
	}
	
	public int getTipoGrafico() {
		return tipoGrafico;
	}
	
	public String getTipoComponente() {
		return "Grafico";
	}
	
	public String toString() {
		return super.toString();
	}
	
	public Grafico clone() {
		Grafico g = new Grafico();
		super.clone(this, g);
		g.tipoGrafico = this.tipoGrafico;
		return g;
	}
}
