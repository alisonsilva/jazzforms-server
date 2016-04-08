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
 * A general sheet class. This renderable container provides base support for orientation-aware transitions for popup or side-anchored sliding Panels.
 * In most cases, you should use Ext.ActionSheet, Ext.MessageBox, Ext.picker.Picker, or Ext.picker.Date.
 */
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("Sheet")
@javax.xml.bind.annotation.XmlRootElement(name="sheet")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@XmlDiscriminatorValue("sheet")
@SuppressWarnings({ "all", "unchecked" })
public class Sheet extends br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Panel implements Serializable {
	public Sheet() {
	}
	
	@Column(name="ENTER_EVT", nullable=true, length=255)	
	protected String enter = "bottom";
	
	@Column(name="EXIT_EVT", nullable=true, length=255)	
	protected String exit = "bottom";
	
	@Column(name="HIDE_ANIMATION", nullable=true, length=255)	
	protected String hideAnimation = "";
	
	@Column(name="SHOW_ANIMATION", nullable=true, length=255)	
	protected String showAnimation = "";
	
	@Column(name="STRETCH_X", nullable=true, length=1)
	@XmlAttribute
	protected boolean stretchX = false;
	
	@Column(name="STRETCH_Y", nullable=true, length=1)
	@XmlAttribute
	protected boolean stretchY = false;
	
	/**
	 * The viewport side used as the enter point when shown. Valid values are 'top', 'bottom', 'left', and 'right'. Applies to sliding animation effects only.
	 * Defaults to: 'bottom'
	 */
	public void setEnter(String value) {
		this.enter = value;
	}
	
	/**
	 * The viewport side used as the enter point when shown. Valid values are 'top', 'bottom', 'left', and 'right'. Applies to sliding animation effects only.
	 * Defaults to: 'bottom'
	 */
	public String getEnter() {
		return enter;
	}
	
	/**
	 * The viewport side used as the exit point when hidden. Valid values are 'top', 'bottom', 'left', and 'right'. Applies to sliding animation effects only.
	 * Defaults to: 'bottom'
	 */
	public void setExit(String value) {
		this.exit = value;
	}
	
	/**
	 * The viewport side used as the exit point when hidden. Valid values are 'top', 'bottom', 'left', and 'right'. Applies to sliding animation effects only.
	 * Defaults to: 'bottom'
	 */
	public String getExit() {
		return exit;
	}
	
	/**
	 * Animation effect to apply when the Component is being hidden. Typically you want to use an outbound animation type such as 'fadeOut' or 'slideOut'.
	 */
	public void setHideAnimation(String value) {
		this.hideAnimation = value;
	}
	
	/**
	 * Animation effect to apply when the Component is being hidden. Typically you want to use an outbound animation type such as 'fadeOut' or 'slideOut'.
	 */
	public String getHideAnimation() {
		return hideAnimation;
	}
	
	public void setShowAnimation(String value) {
		this.showAnimation = value;
	}
	
	public String getShowAnimation() {
		return showAnimation;
	}
	
	public void setStretchX(boolean value) {
		this.stretchX = value;
	}
	
	public boolean getStretchX() {
		return stretchX;
	}
	
	public void setStretchY(boolean value) {
		this.stretchY = value;
	}
	
	public boolean getStretchY() {
		return stretchY;
	}
	
	public String getXType() {
		return "sheet";
	}
	
	public String toString() {
		return super.toString();
	}
	
	public void clone(Sheet origem, Sheet destino) {
		super.clone((Panel)origem, (Panel)destino);
		destino.setEnter(new String(origem.getEnter()));
		destino.setExit(new String(origem.getExit()));
		destino.setHideAnimation(new String(origem.getHideAnimation()));
		destino.setShowAnimation(new String(origem.getShowAnimation()));
		destino.setStretchX(origem.getStretchX());
		destino.setStretchY(origem.getStretchY());
	}
	
	public Sheet clone() {
		Sheet destino = new Sheet();
		this.clone(this, destino);
		return destino;
	}
}
