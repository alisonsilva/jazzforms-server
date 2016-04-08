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
@DiscriminatorValue("Picture")
@javax.xml.bind.annotation.XmlRootElement(name="picture")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@SuppressWarnings({ "all", "unchecked" })
public class Picture extends br.com.laminarsoft.jazzforms.persistencia.model.Componente implements Serializable {
	public Picture() {
	}
	
	@Column(name="IMAGEM", nullable=true)	
	private byte[] imagem;
	
	public void setImagem(byte[] value) {
		this.imagem = value;
	}
	
	public byte[] getImagem() {
		return imagem;
	}
	
	public String getTipoComponente() {
		return "Picture";
	}
	
	public String toString() {
		return super.toString();
	}

	public Picture clone() {
		Picture p = new Picture();
		super.clone(this, p);
		p.imagem = this.imagem;		
		return p;
	}
}
