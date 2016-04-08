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
/**
 * Utility class for generating different styles of message boxes. The framework provides a global singleton Ext.Msg for common usage which you should use in most cases.
 * If you want to use Ext.MessageBox directly, just think of it as a modal Ext.Container.
 * Note that the MessageBox is asynchronous. Unlike a regular JavaScript alert (which will halt browser execution), showing a MessageBox will not cause the code to stop. For this reason, if you have code that should only run after some user feedback from the MessageBox, you must use a callback function (see the fn configuration option parameter for the show method for more details).
 */
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("MessageBox")
@javax.xml.bind.annotation.XmlRootElement(name="messagebox")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@SuppressWarnings({ "all", "unchecked" })
public class MessageBox extends br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Sheet implements Serializable {
	public MessageBox() {
	}
	
	@Column(name="BUTTONS", nullable=true, length=255)	
	private String buttons = "";
	
	@Column(name="DEFAULT_TEXT_HEIGHT", nullable=true)	
	private Double defaultTextHeight = new Double(75);
	
	@Column(name="MESSAGE", nullable=true, length=255)	
	private String message = "";
	
	@Column(name="MESSAGEBOX_TITLE", nullable=true, length=120)	
	private String title = "";
	
	/**
	 * An array of buttons, or an object of a button to be displayed in the toolbar of this Ext.MessageBox.
	 */
	public void setButtons(String value) {
		this.buttons = value;
	}
	
	/**
	 * An array of buttons, or an object of a button to be displayed in the toolbar of this Ext.MessageBox.
	 */
	public String getButtons() {
		return buttons;
	}
	
	/**
	 * The default height in pixels of the message box's multiline textarea if displayed.
	 * Defaults to: 75
	 */
	public void setDefaultTextHeight(double value) {
		setDefaultTextHeight(new Double(value));
	}
	
	/**
	 * The default height in pixels of the message box's multiline textarea if displayed.
	 * Defaults to: 75
	 */
	public void setDefaultTextHeight(Double value) {
		this.defaultTextHeight = value;
	}
	
	/**
	 * The default height in pixels of the message box's multiline textarea if displayed.
	 * Defaults to: 75
	 */
	public Double getDefaultTextHeight() {
		return defaultTextHeight;
	}
	
	public void setMessage(String value) {
		this.message = value;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setTitle(String value) {
		this.title = value;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getXType() {
		return "messagebox";
	}
	
	public String toString() {
		return super.toString();
	}
	
	public void clone(MessageBox origem, MessageBox destino) {
		destino.setButtons(new String(origem.getButtons()));
		destino.setDefaultTextHeight(origem.getDefaultTextHeight().doubleValue());
		destino.setMessage(new String(origem.getMessage()));
		destino.setTitle(new String(origem.getTitle()));
		super.clone(origem, destino);
	}
	
	public MessageBox clone() {
		MessageBox destino = new MessageBox();
		this.clone((MessageBox)this, (MessageBox)destino);
		return destino;
	}
}
