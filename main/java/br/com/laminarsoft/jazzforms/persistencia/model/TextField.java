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
@DiscriminatorValue("TextField")
@javax.xml.bind.annotation.XmlRootElement(name="text_field")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@SuppressWarnings({ "all", "unchecked" })
public class TextField extends br.com.laminarsoft.jazzforms.persistencia.model.Componente implements Serializable {
	public TextField() {
	}
	
	@Column(name="PASSWORD", nullable=true, length=1)	
	private boolean password;
	
	@Column(name="MASCARA", nullable=true, length=40)	
	private String mascara;
	
	@Column(name="QTD_MAXIMA", nullable=true, length=3)	
	private int qtdMaxima;
	
	@Column(name="QTD_MINIMA", nullable=true, length=3)	
	private int qtdMinima;
	
	@Column(name="EXPRESSAO_REGULAR", nullable=true, length=50)	
	private String expressaoRegular;
	
	@Column(name="DATE_PICKER", nullable=true, length=1)	
	private boolean datePicker;
	
	@Column(name="QTD_LINHA_APRESENTACAO", nullable=true, length=3)	
	private int qtdLinhaApresentacao;
	
	public void setPassword(boolean value) {
		this.password = value;
	}
	
	public boolean getPassword() {
		return password;
	}
	
	public void setMascara(String value) {
		this.mascara = value;
	}
	
	public String getMascara() {
		return mascara;
	}
	
	public void setQtdMaxima(int value) {
		this.qtdMaxima = value;
	}
	
	public int getQtdMaxima() {
		return qtdMaxima;
	}
	
	public void setQtdMinima(int value) {
		this.qtdMinima = value;
	}
	
	public int getQtdMinima() {
		return qtdMinima;
	}
	
	public void setExpressaoRegular(String value) {
		this.expressaoRegular = value;
	}
	
	public String getExpressaoRegular() {
		return expressaoRegular;
	}
	
	public void setDatePicker(boolean value) {
		this.datePicker = value;
	}
	
	public boolean getDatePicker() {
		return datePicker;
	}
	
	public void setQtdLinhaApresentacao(int value) {
		this.qtdLinhaApresentacao = value;
	}
	
	public int getQtdLinhaApresentacao() {
		return qtdLinhaApresentacao;
	}
	
	public String getTipoComponente() {
		return "TextField";
	}
	
	public String toString() {
		return super.toString();
	}
	
	
	public TextField clone() {
		TextField tf = new TextField();
		super.clone(this, tf);
		tf.password = this.password;
		tf.mascara = this.mascara;
		tf.qtdMaxima = this.qtdMaxima;
		tf.qtdMinima = this.qtdMinima;
		tf.expressaoRegular = this.expressaoRegular;
		tf.datePicker = this.datePicker;
		tf.qtdLinhaApresentacao = this.qtdLinhaApresentacao;
		return tf;
	}
}
