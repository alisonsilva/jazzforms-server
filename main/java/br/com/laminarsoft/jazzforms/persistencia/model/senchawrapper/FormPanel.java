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
/**
 * The Form panel presents a set of form fields and provides convenient ways to load and save data. Usually a form panel just contains the set of fields you want to display
 */
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("FormPanel")
@javax.xml.bind.annotation.XmlRootElement(name="formpanel")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@XmlDiscriminatorValue("formpanel")
@SuppressWarnings({ "all", "unchecked" })
public class FormPanel extends br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Panel implements Serializable {
	public FormPanel() {
	}
	
	@Column(name="METHOD", nullable=true, length=80)	
	private String method = "post";
	
	@Column(name="RECORD", nullable=true, length=80)	
	private String record = "";
	
	@Column(name="SCROLLABLE", nullable=true, length=1)
	@XmlAttribute
	private boolean scrollable = true;
	
	@Column(name="SUBMIT_ON_ACTION", nullable=true, length=1)
	@XmlAttribute
	private boolean submitOnAction = false;
	
	@Column(name="TRACK_RESET_ON_LOAD", nullable=true, length=1)
	@XmlAttribute
	private boolean trackResetOnLoad = false;
	
	@Column(name="URL", nullable=true, length=255)	
	private String url = "";
	
	/**
	 * The method which this form will be submitted. post or get.
	 * Defaults to: 'post'
	 */
	public void setMethod(String value) {
		this.method = value;
	}
	
	/**
	 * The method which this form will be submitted. post or get.
	 * Defaults to: 'post'
	 */
	public String getMethod() {
		return method;
	}
	
	/**
	 * The model instance of this form. Can by dynamically set at any time.
	 */
	public void setRecord(String value) {
		this.record = value;
	}
	
	/**
	 * The model instance of this form. Can by dynamically set at any time.
	 */
	public String getRecord() {
		return record;
	}
	
	public void setScrollable(boolean value) {
		this.scrollable = value;
	}
	
	public boolean getScrollable() {
		return scrollable;
	}
	
	/**
	 * When this is set to true, the form will automatically submit itself whenever the action event fires on a field in this form. The action event usually fires whenever you press go or enter inside a textfield.
	 * Defaults to: false
	 */
	public void setSubmitOnAction(boolean value) {
		this.submitOnAction = value;
	}
	
	/**
	 * When this is set to true, the form will automatically submit itself whenever the action event fires on a field in this form. The action event usually fires whenever you press go or enter inside a textfield.
	 * Defaults to: false
	 */
	public boolean getSubmitOnAction() {
		return submitOnAction;
	}
	
	/**
	 * If set to true, reset() resets to the last loaded or setValues() data instead of when the form was first created.
	 * Defaults to: false
	 */
	public void setTrackResetOnLoad(boolean value) {
		this.trackResetOnLoad = value;
	}
	
	/**
	 * If set to true, reset() resets to the last loaded or setValues() data instead of when the form was first created.
	 * Defaults to: false
	 */
	public boolean getTrackResetOnLoad() {
		return trackResetOnLoad;
	}
	
	/**
	 * The default url for submit actions.
	 */
	public void setUrl(String value) {
		this.url = value;
	}
	
	/**
	 * The default url for submit actions.
	 */
	public String getUrl() {
		return url;
	}
	
	public String getXType() {
		return "formpanel";
	}
	
	public String toString() {
		return super.toString();
	}
	
	public void clone(FormPanel origem, FormPanel destino) {
		super.clone((Panel)origem, (Panel)destino);
		destino.setMethod(new String(origem.getMethod()));
		destino.setRecord(new String(origem.getRecord()));
		destino.setScrollable(origem.getScrollable());
		destino.setSubmitOnAction(origem.getSubmitOnAction());
		destino.setTrackResetOnLoad(origem.getTrackResetOnLoad());
		destino.setUrl(new String(origem.getUrl()));
		destino.setMaxWidth(origem.getMaxWidth());
		destino.setMaxHeight(origem.getMaxHeight());
	}
	
	public FormPanel clone() {
		FormPanel destino = new FormPanel();
		this.clone(this, destino);
		return destino;
	}
}
