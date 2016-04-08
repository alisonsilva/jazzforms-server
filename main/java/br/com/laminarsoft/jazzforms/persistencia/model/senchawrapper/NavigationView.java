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
 * NavigationView is basically a Ext.Container with a card layout, so only one view can be visible at a time. However, NavigationView also adds extra functionality on top of this to allow you to push and pop views at any time. When you do this, your NavigationView will automatically animate between your current active view, and the new view you want to push, or the previous view you want to pop.
 */
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("NavigationView")
@javax.xml.bind.annotation.XmlRootElement(name="navigationview")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@XmlDiscriminatorValue("navigationview")
@SuppressWarnings({ "all", "unchecked" })
public class NavigationView extends br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Container implements Serializable {
	public NavigationView() {
	}
	
	@Column(name="DEFAULT_BACK_BUTTON_TEXT", nullable=true, length=50)
	@XmlAttribute
	private String defaultBackButtonText = "";
	
	@Column(name="NAVIGATION_BAR", nullable=true, length=255)	
	private String navigationBar = "";
	
	@Column(name="USE_TITLE_FOR_BACK_BUTTON_TEXT", nullable=true, length=1)
	@XmlAttribute
	private boolean useTitleForBackButtonText = false;
	
	/**
	 * The text to be displayed on the back button if:
	 * The previous view does not have a title.
	 * The useTitleForBackButtonText configuration is true.
	 * Defaults to: 'Back'
	 */
	public void setDefaultBackButtonText(String value) {
		this.defaultBackButtonText = value;
	}
	
	/**
	 * The text to be displayed on the back button if:
	 * The previous view does not have a title.
	 * The useTitleForBackButtonText configuration is true.
	 * Defaults to: 'Back'
	 */
	public String getDefaultBackButtonText() {
		return defaultBackButtonText;
	}
	
	/**
	 * The NavigationBar used in this navigation view. It defaults to be docked to the top.
	 * You can just pass in a normal object if you want to customize the NavigationBar. For example:
	 * navigationBar: {    ui: 'dark',    docked: 'bottom'}
	 * You cannot specify a title property in this configuration. The title of the navigationBar is taken from the configuration of this views children:
	 * view.push({    title: 'This views title which will be shown in the navigation bar',    html: 'Some HTML'});
	 * Defaults to: {docked: 'top'}
	 */
	public void setNavigationBar(String value) {
		this.navigationBar = value;
	}
	
	/**
	 * The NavigationBar used in this navigation view. It defaults to be docked to the top.
	 * You can just pass in a normal object if you want to customize the NavigationBar. For example:
	 * navigationBar: {    ui: 'dark',    docked: 'bottom'}
	 * You cannot specify a title property in this configuration. The title of the navigationBar is taken from the configuration of this views children:
	 * view.push({    title: 'This views title which will be shown in the navigation bar',    html: 'Some HTML'});
	 * Defaults to: {docked: 'top'}
	 */
	public String getNavigationBar() {
		return navigationBar;
	}
	
	public void setUseTitleForBackButtonText(boolean value) {
		this.useTitleForBackButtonText = value;
	}
	
	public boolean getUseTitleForBackButtonText() {
		return useTitleForBackButtonText;
	}
	
	public String getXType() {
		return "navigationview";
	}
	
	public String toString() {
		return super.toString();
	}
	
	public void clone(NavigationView origem, NavigationView destino) {
		destino.setDefaultBackButtonText(new String(origem.getDefaultBackButtonText()));
		destino.setNavigationBar(new String(origem.getNavigationBar()));
		destino.setUseTitleForBackButtonText(origem.getUseTitleForBackButtonText());
		super.clone(origem, destino);
	}
	
	public NavigationView clone() {
		NavigationView destino = new NavigationView();
		this.clone(this, destino);
		return destino;
	}
	
}
