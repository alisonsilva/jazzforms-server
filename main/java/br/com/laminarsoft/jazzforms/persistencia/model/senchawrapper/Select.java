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
@DiscriminatorValue("Select")
@javax.xml.bind.annotation.XmlRootElement(name="select")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@XmlDiscriminatorValue("select")
@SuppressWarnings({ "all", "unchecked" })
public class Select extends br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Text implements Serializable {
	public Select() {
	}
	
	@Column(name="AUTO_SELECT", nullable=true, length=1)
	@XmlAttribute
	private boolean autoSelect = true;
	
	@Column(name="DISPLAY_FIELD", nullable=true, length=80)
	@XmlAttribute
	private String displayField = "text";
	
	@Column(name="HIDDEN_NAME", nullable=true, length=80)
	@XmlAttribute
	private String hiddenName = "";
	
	@Column(name="USE_PICKER", nullable=true, length=1)
	@XmlAttribute
	private boolean usePicker = true;
	
	@Column(name="SELECT_STORE", nullable=true, length=50)	
	@XmlAttribute
	private String store = "";
	
	@Column(name="URL_ATUALIZACAO", nullable=true, length=255)
    @XmlAttribute	
	private String urlAtualizacao;
	
	@OneToMany(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Option.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.ALL})	
	@JoinColumn(name="SNCHA_COMPONENT_ID", nullable=true)	
	@org.hibernate.annotations.IndexColumn(name="COMPONENT_INDEX")	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.FALSE)	
	private java.util.List<Option> options = new java.util.ArrayList<Option>();
	
	/**
	 * true to auto select the first value in the store or options when they are changed. Only happens when the value is set to null.
	 * Defaults to: true
	 */
	public void setAutoSelect(boolean value) {
		this.autoSelect = value;
	}
	
	/**
	 * true to auto select the first value in the store or options when they are changed. Only happens when the value is set to null.
	 * Defaults to: true
	 */
	public boolean getAutoSelect() {
		return autoSelect;
	}
	
	/**
	 * The underlying data value name (or numeric Array index) to bind to this Select control. This resolved value is the visibly rendered value of the available selection options.
	 * Defaults to: 'text'
	 */
	public void setDisplayField(String value) {
		this.displayField = value;
	}
	
	/**
	 * The underlying data value name (or numeric Array index) to bind to this Select control. This resolved value is the visibly rendered value of the available selection options.
	 * Defaults to: 'text'
	 */
	public String getDisplayField() {
		return displayField;
	}
	
	/**
	 * Specify a hiddenName if you're using the standardSubmit option. This name will be used to post the underlying value of the select to the server.
	 */
	public void setHiddenName(String value) {
		this.hiddenName = value;
	}
	
	/**
	 * Specify a hiddenName if you're using the standardSubmit option. This name will be used to post the underlying value of the select to the server.
	 */
	public String getHiddenName() {
		return hiddenName;
	}
	
	public void setUsePicker(boolean value) {
		this.usePicker = value;
	}
	
	public boolean getUsePicker() {
		return usePicker;
	}


	public void setUrlAtualizacao(String value) {
		this.urlAtualizacao = value;
	}
	
	public String getUrlAtualizacao() {
		return urlAtualizacao;
	}
	
 	public void setStore(String value) {
		this.store = value;
	}
	
	public String getStore() {
		return store;
	}

	public void setOptions(java.util.List<Option> value) {
		this.options = value;
	}
	
	public java.util.List<Option> getOptions() {
		return options;
	}
	
	
	public String getXType() {
		return "selectfield";
	}
	
	public String toString() {
		return super.toString();
	}
	
	public void clone(Select origem, Select destino) {
		destino.setAutoSelect(origem.getAutoSelect());
		destino.setDisplayField(new String(origem.getDisplayField()));
		destino.setHiddenName(new String(origem.getHiddenName()));
		destino.setUsePicker(origem.getUsePicker());
		destino.setStore(new String(origem.getStore()));
		destino.setUrlAtualizacao(new String((origem.getUrlAtualizacao() == null ? "" : origem.getUrlAtualizacao())));
		super.clone(origem, destino);
		for(Option option : origem.getOptions()) {
			destino.getOptions().add(option.clone());
		}
	}
	
	public Select clone() {
		Select destino = new Select();
		this.clone(this, destino);
		return destino;
	}
}
