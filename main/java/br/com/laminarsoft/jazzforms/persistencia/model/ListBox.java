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
@DiscriminatorValue("ListBox")
@javax.xml.bind.annotation.XmlRootElement(name="list_box")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@SuppressWarnings({ "all", "unchecked" })
public class ListBox extends br.com.laminarsoft.jazzforms.persistencia.model.Componente implements Serializable {
	public ListBox() {
	}
	
	@Column(name="MULTIPLA_SELECAO", nullable=true, length=1)	
	private boolean multiplaSelecao;
	
	@Column(name="COMBO", nullable=true, length=1)	
	private boolean combo;
	
	public void setMultiplaSelecao(boolean value) {
		this.multiplaSelecao = value;
	}
	
	public boolean getMultiplaSelecao() {
		return multiplaSelecao;
	}
	
	public void setCombo(boolean value) {
		this.combo = value;
	}
	
	public boolean getCombo() {
		return combo;
	}
	
	public String getTipoComponente() {
		return "ListBox";
	}
	
	public String toString() {
		return super.toString();
	}
	
	@Override
	public ListBox clone() {
		ListBox lb = new ListBox();
		super.clone(this, lb);
		lb.multiplaSelecao = this.multiplaSelecao;
		lb.combo = this.combo;
		
		return lb;
	}
}
