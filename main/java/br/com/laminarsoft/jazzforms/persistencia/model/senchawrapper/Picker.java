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
 * A general picker class. Ext.picker.Slots are used to organize multiple scrollable slots into a single picker. slots is the only necessary configuration.
 * The slots configuration with a few key values:
 * name: The name of the slot (will be the key when using getValues in this Ext.picker.Picker).
 * title: The title of this slot (if useTitles is set to true).
 * data/store: The data or store to use for this slot.
 * Remember, Ext.picker.Slot class extends from Ext.dataview.DataView.
 *  
 */
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("Picker")
@javax.xml.bind.annotation.XmlRootElement(name="picker")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@XmlDiscriminatorValue("picker")
@SuppressWarnings({ "all", "unchecked" })
public class Picker extends br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Sheet implements Serializable {
	public Picker() {
	}
	
	@Column(name="CANCEL_BUTTON", nullable=true, length=255)	
	private String cancelButton = "cancela";
	
	@Column(name="DONE_BUTTON", nullable=true, length=255)	
	private String doneButton = "ok";
	
	@Column(name="SLOTS", nullable=true, length=255)	
	private String slots = "";
	
	@Column(name="TOOL_BAR", nullable=true, length=255)	
	private String toolBar = "";
	
	@Column(name="USE_TITLES", nullable=true, length=1)	
	@XmlAttribute
	private boolean useTitles = false;
	
	@Column(name="PICKER_VALUE", nullable=true, length=255)	
	private String pipckerValue = "";
	
	/**
	 * Can be either:
	 * A {String} text to be used on the Cancel button.
	 * An {Object} as config for Ext.Button.
	 * false or null to hide it.
	 * Defaults to: true
	 *  
	 */
	public void setCancelButton(String value) {
		this.cancelButton = value;
	}
	
	/**
	 * Can be either:
	 * A {String} text to be used on the Cancel button.
	 * An {Object} as config for Ext.Button.
	 * false or null to hide it.
	 * Defaults to: true
	 *  
	 */
	public String getCancelButton() {
		return cancelButton;
	}
	
	/**
	 * Can be either:
	 * A {String} text to be used on the Done button.
	 * An {Object} as config for Ext.Button.
	 * false or null to hide it.
	 * Defaults to: true
	 *  
	 */
	public void setDoneButton(String value) {
		this.doneButton = value;
	}
	
	/**
	 * Can be either:
	 * A {String} text to be used on the Done button.
	 * An {Object} as config for Ext.Button.
	 * false or null to hide it.
	 * Defaults to: true
	 *  
	 */
	public String getDoneButton() {
		return doneButton;
	}
	
	/**
	 * 
	 * An array of slot configurations.
	 * name {String} - Name of the slot
	 * data {Array} - An array of text/value pairs in the format {text: 'myKey', value: 'myValue'}
	 * title {String} - Title of the slot. This is used in conjunction with useTitles: true.
	 *  
	 */
	public void setSlots(String value) {
		this.slots = value;
	}
	
	/**
	 * 
	 * An array of slot configurations.
	 * name {String} - Name of the slot
	 * data {Array} - An array of text/value pairs in the format {text: 'myKey', value: 'myValue'}
	 * title {String} - Title of the slot. This is used in conjunction with useTitles: true.
	 *  
	 */
	public String getSlots() {
		return slots;
	}
	
	/**
	 * The toolbar which contains the doneButton and cancelButton buttons. You can override this if you wish, and add your own configurations. Just ensure that you take into account the doneButton and cancelButton configurations.
	 * The default xtype is a Ext.TitleBar:
	 * toolbar: {    items: [        {            xtype: 'button',            text: 'Left',            align: 'left'        },        {            xtype: 'button',            text: 'Right',            align: 'left'        }    ]}
	 * Or to use a instead:
	 * toolbar: {    xtype: 'toolbar',    items: [        {            xtype: 'button',            text: 'Left'        },        {            xtype: 'button',            text: 'Left Two'        }    ]}
	 * Defaults to: {xtype: 'titlebar'}
	 */
	public void setToolBar(String value) {
		this.toolBar = value;
	}
	
	/**
	 * The toolbar which contains the doneButton and cancelButton buttons. You can override this if you wish, and add your own configurations. Just ensure that you take into account the doneButton and cancelButton configurations.
	 * The default xtype is a Ext.TitleBar:
	 * toolbar: {    items: [        {            xtype: 'button',            text: 'Left',            align: 'left'        },        {            xtype: 'button',            text: 'Right',            align: 'left'        }    ]}
	 * Or to use a instead:
	 * toolbar: {    xtype: 'toolbar',    items: [        {            xtype: 'button',            text: 'Left'        },        {            xtype: 'button',            text: 'Left Two'        }    ]}
	 * Defaults to: {xtype: 'titlebar'}
	 */
	public String getToolBar() {
		return toolBar;
	}
	
	/**
	 * Generate a title header for each individual slot and use the title configuration of the slot.
	 * Defaults to: false
	 */
	public void setUseTitles(boolean value) {
		this.useTitles = value;
	}
	
	/**
	 * Generate a title header for each individual slot and use the title configuration of the slot.
	 * Defaults to: false
	 */
	public boolean getUseTitles() {
		return useTitles;
	}
	
	/**
	 * The value to initialize the picker with.
	 */
	public void setPipckerValue(String value) {
		this.pipckerValue = value;
	}
	
	/**
	 * The value to initialize the picker with.
	 */
	public String getPipckerValue() {
		return pipckerValue;
	}
	
	public String getXType() {
		return "picker";
	}
	
	public String toString() {
		return super.toString();
	}
	
	public void clone(Picker origem, Picker destino) {
		destino.setCancelButton(new String(origem.getCancelButton()));
		destino.setDoneButton(new String(origem.getDoneButton()));
		destino.setSlots(new String(origem.getSlots()));
		destino.setToolBar(new String(origem.getToolBar()));
		destino.setUseTitles(origem.getUseTitles());
		destino.setPipckerValue(new String(origem.getPipckerValue()));
		super.clone(origem, destino);
	}
	
	public Picker clone() {
		Picker destino = new Picker();
		this.clone((Picker)this, (Picker)destino);
		return destino;
	}
}
