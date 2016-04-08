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
 * SegmentedButton is a container for a group of Ext.Buttons. Generally a SegmentedButton would be a child of a Ext.Toolbar and would be used to switch between different views.
 */
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("SegmentedButton")
@javax.xml.bind.annotation.XmlRootElement(name="segmentedbutton")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@XmlDiscriminatorValue("segmentedbutton")
@SuppressWarnings({ "all", "unchecked" })
public class SegmentedButton extends br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Container implements Serializable {
	public SegmentedButton() {
	}
	
	@Column(name="ALLOW_DEPRESS", nullable=true, length=1)	
	@XmlAttribute
	private Boolean allowDepress = false;
	
	@Column(name="ALLOW_MULTIPLE", nullable=true, length=1)
	@XmlAttribute
	private Boolean allowMultiple = false;
	
	@Column(name="ALLOW_TOGGLE", nullable=true, length=1)
	@XmlAttribute
	private Boolean allowToggle = false;
	
	@Column(name="PRESSED_BUTTONS", nullable=true, length=255)	
	private String pressedButtons = "";
	
	@Column(name="SEGMENTED_BUTTON_PRESSED_CLS", nullable=true, length=80)
	@XmlAttribute
	private String segmentedButtonPressedCls = "";
	
	/**
	 * Allow toggling the pressed state of each button. Defaults to true when allowMultiple is true.
	 * Defaults to: false
	 */
	public void setAllowDepress(boolean value) {
		setAllowDepress(new Boolean(value));
	}
	
	/**
	 * Allow toggling the pressed state of each button. Defaults to true when allowMultiple is true.
	 * Defaults to: false
	 */
	public void setAllowDepress(Boolean value) {
		this.allowDepress = value;
	}
	
	/**
	 * Allow toggling the pressed state of each button. Defaults to true when allowMultiple is true.
	 * Defaults to: false
	 */
	public Boolean getAllowDepress() {
		return allowDepress;
	}
	
	/**
	 * Allow multiple pressed buttons.
	 * Defaults to: false
	 */
	public void setAllowMultiple(boolean value) {
		setAllowMultiple(new Boolean(value));
	}
	
	/**
	 * Allow multiple pressed buttons.
	 * Defaults to: false
	 */
	public void setAllowMultiple(Boolean value) {
		this.allowMultiple = value;
	}
	
	/**
	 * Allow multiple pressed buttons.
	 * Defaults to: false
	 */
	public Boolean getAllowMultiple() {
		return allowMultiple;
	}
	
	/**
	 * Allow child buttons to be pressed when tapped on. Set to false to allow tapping but not toggling of the buttons.
	 * Defaults to: true
	 */
	public void setAllowToggle(boolean value) {
		setAllowToggle(new Boolean(value));
	}
	
	/**
	 * Allow child buttons to be pressed when tapped on. Set to false to allow tapping but not toggling of the buttons.
	 * Defaults to: true
	 */
	public void setAllowToggle(Boolean value) {
		this.allowToggle = value;
	}
	
	/**
	 * Allow child buttons to be pressed when tapped on. Set to false to allow tapping but not toggling of the buttons.
	 * Defaults to: true
	 */
	public Boolean getAllowToggle() {
		return allowToggle;
	}
	
	/**
	 * The pressed buttons for this segmented button.
	 * You can remove all pressed buttons by calling setPressedButtons with an empty array.
	 * Defaults to: []
	 */
	public void setPressedButtons(String value) {
		this.pressedButtons = value;
	}
	
	/**
	 * The pressed buttons for this segmented button.
	 * You can remove all pressed buttons by calling setPressedButtons with an empty array.
	 * Defaults to: []
	 */
	public String getPressedButtons() {
		return pressedButtons;
	}
	
	/**
	 * CSS class when a button is in pressed state.
	 * Defaults to: Ext.baseCSSPrefix + 'button-pressed'
	 */
	public void setSegmentedButtonPressedCls(String value) {
		this.segmentedButtonPressedCls = value;
	}
	
	/**
	 * CSS class when a button is in pressed state.
	 * Defaults to: Ext.baseCSSPrefix + 'button-pressed'
	 */
	public String getSegmentedButtonPressedCls() {
		return segmentedButtonPressedCls;
	}
	
	public String getXType() {
		return "segmentedbutton";
	}
	
	public String toString() {
		return super.toString();
	}
	
	public void clone(SegmentedButton origem, SegmentedButton destino) {
		destino.setAllowDepress(origem.getAllowDepress().booleanValue());
		destino.setAllowMultiple(origem.getAllowMultiple().booleanValue());
		destino.setAllowToggle(origem.getAllowToggle().booleanValue());
		destino.setPressedButtons(new String(origem.getPressedButtons()));
		destino.setSegmentedButtonPressedCls(new String(origem.getSegmentedButtonPressedCls()));
		super.clone(origem, destino);
	}
	
	public SegmentedButton clone() {
		SegmentedButton destino = new SegmentedButton();
		this.clone(this, destino);
		return destino;
	}
}
