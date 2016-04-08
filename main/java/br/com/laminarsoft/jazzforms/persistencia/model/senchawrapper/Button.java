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
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAttribute;

import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

import br.com.laminarsoft.jazzforms.persistencia.model.Picture;
/**
 * A simple class to display a button in Sencha Touch.
 * There are various different styles of Button you can create by using the icon, iconCls, iconAlign, ui, and text configurations.
 */
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("Button")
@javax.xml.bind.annotation.XmlRootElement(name="button")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@XmlDiscriminatorValue("button")
@SuppressWarnings({ "all", "unchecked" })
public class Button extends br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Component implements Serializable {
	public Button() {
	}
	
	@ManyToOne(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Icon.class)	
	@org.hibernate.annotations.Cascade({})	
	@JoinColumns({ @JoinColumn(name="ICON_ID", referencedColumnName="Id") })	
	private br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Icon icon;
	
	@Column(name="BADGE_CLS", nullable=true, length=80)
	@XmlAttribute
	private String badgeCls = "";
	
	@Column(name="BADGE_TEXT", nullable=true, length=60)
	@XmlAttribute
	private String badgeText = "";
	
	@Column(name="HANDLER", nullable=true, length=512)	
	private String handler = "";
	
	@Column(name="ICON", nullable=true, length=50)
	@XmlAttribute
	private String iconName = "";
	
	@Column(name="ICON_ALIGN", nullable=true, length=50)
	@XmlAttribute
	private String iconAlign = "";
	
	@Column(name="ICON_CLS", nullable=true, length=80)
	@XmlAttribute
	private String iconCls = "";
	
	@Column(name="BUTTON_PRESSED_CLS", nullable=true, length=80)
	@XmlAttribute
	private String buttonPressedCls = "";
	
	@Column(name="PRESSED_DELAY", nullable=true, length=11)
	@XmlAttribute
	private Integer pressedDelay = new Integer(0);
	
	@Column(name="TEXT", nullable=true, length=255)	
	private String buttonText;
	
	/**
	 * The CSS class to add to the Button's badge, if it has one.
	 * Defaults to: Ext.baseCSSPrefix + 'badge'
	 */
	public void setBadgeCls(String value) {
		this.badgeCls = value;
	}
	
	/**
	 * The CSS class to add to the Button's badge, if it has one.
	 * Defaults to: Ext.baseCSSPrefix + 'badge'
	 */
	public String getBadgeCls() {
		return badgeCls;
	}
	
	public void setBadgeText(String value) {
		this.badgeText = value;
	}
	
	public String getBadgeText() {
		return badgeText;
	}
	
	public void setHandler(String value) {
		this.handler = value;
	}
	
	public String getHandler() {
		return handler;
	}
	
	/**
	 * Url to the icon image to use if you want an icon to appear on your button.
	 * Defaults to: null
	 */
	public void setIconName(String value) {
		this.iconName = value;
	}
	
	/**
	 * Url to the icon image to use if you want an icon to appear on your button.
	 * Defaults to: null
	 */
	public String getIconName() {
		return iconName;
	}
	
	/**
	 * The position within the Button to render the icon Options are: top, right, bottom, left and center (when you have no text set).
	 * Defaults to: 'left'
	 */
	public void setIconAlign(String value) {
		this.iconAlign = value;
	}
	
	/**
	 * The position within the Button to render the icon Options are: top, right, bottom, left and center (when you have no text set).
	 * Defaults to: 'left'
	 */
	public String getIconAlign() {
		return iconAlign;
	}
	
	public void setIconCls(String value) {
		this.iconCls = value;
	}
	
	public String getIconCls() {
		return iconCls;
	}
	
	public void setButtonPressedCls(String value) {
		this.buttonPressedCls = value;
	}
	
	public String getButtonPressedCls() {
		return buttonPressedCls;
	}
	
	/**
	 * The amount of delay between the tapstart and the moment we add the pressedCls (in milliseconds). Settings this to true defaults to 100ms.
	 * Defaults to: 0
	 */
	public void setPressedDelay(int value) {
		setPressedDelay(new Integer(value));
	}
	
	/**
	 * The amount of delay between the tapstart and the moment we add the pressedCls (in milliseconds). Settings this to true defaults to 100ms.
	 * Defaults to: 0
	 */
	public void setPressedDelay(Integer value) {
		this.pressedDelay = value;
	}
	
	/**
	 * The amount of delay between the tapstart and the moment we add the pressedCls (in milliseconds). Settings this to true defaults to 100ms.
	 * Defaults to: 0
	 */
	public Integer getPressedDelay() {
		return pressedDelay;
	}
	
	public void setButtonText(String value) {
		this.buttonText = value;
	}
	
	public String getButtonText() {
		return buttonText;
	}
	
	public void setIcon(br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Icon value) {
		this.icon = value;
	}
	
	public br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Icon getIcon() {
		return icon;
	}
	
	public String getXType() {
		return "button";
	}
	
	public String toString() {
		return super.toString();
	}
	
	public void clone(Button origem, Button destino) {
		destino.setBadgeCls(new String(origem.getBadgeCls()));
		destino.setBadgeText(new String(origem.getBadgeText()));
		destino.setButtonPressedCls(new String(origem.getButtonPressedCls()));
		destino.setButtonText(new String(origem.getButtonText()));
		destino.setHandler(new String(origem.getHandler()));
		destino.setIconName(new String(origem.getIconName()));
		destino.setIcon(origem.getIcon());
		destino.setIconAlign(new String(origem.getIconAlign()));
		destino.setIconCls(new String(origem.getIconCls()));
		destino.setPressedDelay(origem.getPressedDelay());
		super.clone(origem, destino);
	}
	
	public Button clone() {
		Button b = new Button();
		this.clone(this, b);
		return b;
	}	
}
