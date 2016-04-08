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
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("Text")
@javax.xml.bind.annotation.XmlRootElement(name="text")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@XmlDiscriminatorValue("text")
@SuppressWarnings({ "all", "unchecked" })
public class Text extends br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Field implements Serializable {
	public Text() {
	}
	
	@Column(name="AUTO_CAPITALIZE", nullable=true, length=1)	
	@XmlAttribute
	protected boolean autoCapitalize = false;
	
	@Column(name="AUTO_COMPLETE", nullable=true, length=1)
	@XmlAttribute
	protected boolean autoComplete = false;
	
	@Column(name="AUTO_CORRECT", nullable=true, length=1)
	@XmlAttribute
	protected boolean autoCorrect = false;
	
	@Column(name="MAX_LENGTH", nullable=true, length=11)
	@XmlAttribute
	protected Integer maxLength = new Integer(0);
	
	@Column(name="PLACE_HOLDER", nullable=true, length=80)
	@XmlAttribute
	protected String placeHolder = "";
	
	@Column(name="READ_ONLY", nullable=true, length=1)
	@XmlAttribute
	protected boolean readOnly = false;
		
	@Column(name="TEXT_AREA", nullable=true, length=1)	
	@XmlAttribute
	private Boolean textArea = false;
	
	@Column(name="TAMAX_ROWS", nullable=true, length=11)
	@XmlAttribute	
	private Integer maxRows = new Integer(4);
	
	@Column(name="EMAIL_FIELD", nullable=true, length=1)	
	@XmlAttribute
	private Boolean emailField = false;
	
	public void setAutoCapitalize(boolean value) {
		this.autoCapitalize = value;
	}
	
	public boolean getAutoCapitalize() {
		return autoCapitalize;
	}
	
	public void setAutoComplete(boolean value) {
		this.autoComplete = value;
	}
	
	public boolean getAutoComplete() {
		return autoComplete;
	}
	
	public void setAutoCorrect(boolean value) {
		this.autoCorrect = value;
	}
	
	public boolean getAutoCorrect() {
		return autoCorrect;
	}
	
	public void setMaxLength(int value) {
		setMaxLength(new Integer(value));
	}
	
	public void setMaxLength(Integer value) {
		this.maxLength = value;
	}
	
	public Integer getMaxLength() {
		return maxLength;
	}
	
	public void setPlaceHolder(String value) {
		this.placeHolder = value;
	}
	
	public String getPlaceHolder() {
		return placeHolder;
	}
	
	public void setReadOnly(boolean value) {
		this.readOnly = value;
	}
	
	public boolean getReadOnly() {
		return readOnly;
	}
	
	public void setTextArea(boolean value) {
		this.textArea = value;
	}
	
	public void setTextArea(Boolean value) {
		this.textArea = value;
	}
	
	public Boolean getTextArea() {
		return textArea;
	}
	
	public void setMaxRows(int value) {
		setMaxRows(new Integer(value));
	}
	
	public void setMaxRows(Integer value) {
		this.maxRows = value;
	}
	
	public Integer getMaxRows() {
		return maxRows;
	}
	
	public void setEmailField(boolean value) {
		this.emailField = value;
	}
	
	public void setEmailField(Boolean value) {
		this.emailField = value;
	}
	
	public Boolean getEmailField() {
		return emailField;
	}
	
	public String getXType() {
		return "textfield";
	}
	
	public String toString() {
		return super.toString();
	}
	
	public void clone(Text origem, Text destino) {
		destino.setAutoCapitalize(origem.getAutoCapitalize());
		destino.setAutoComplete(origem.getAutoComplete());
		destino.setAutoCorrect(origem.getAutoCorrect());
		destino.setMaxLength(origem.getMaxLength());
		destino.setPlaceHolder(new String(origem.getPlaceHolder()));
		destino.setReadOnly(origem.getReadOnly());
		destino.setMaxRows(origem.getMaxRows() != null ? origem.getMaxRows().intValue() : 4);
		destino.setTextArea(origem.getTextArea() != null ? origem.getTextArea() : false);
		destino.setEmailField(origem.getEmailField() != null ? origem.getEmailField() : false);
		super.clone(origem, destino);
	}
	
	public Text clone() {
		Text destino = new Text();
		this.clone(this, destino);
		return destino;
	}
}
