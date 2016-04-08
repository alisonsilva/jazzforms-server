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
 * Tab Panels are a great way to allow the user to switch between several pages that are all full screen. Each Component in the Tab Panel gets its own Tab, which shows the Component when tapped on. Tabs can be positioned at the top or the bottom of the Tab Panel, and can optionally accept title and icon configurations.
 * Here's how we can set up a simple Tab Panel with tabs at the bottom. Use the controls at the top left of the example to toggle between code mode and live preview mode (you can also edit the code and see your changes in the live preview):
 */
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("TabPanel")
@javax.xml.bind.annotation.XmlRootElement(name="tabpanel")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@XmlDiscriminatorValue("tabpanel")
@SuppressWarnings({ "all", "unchecked" })
public class TabPanel extends br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Container implements Serializable {
	public TabPanel() {
	}
	
	@Column(name="TAB_BAR", nullable=true, length=255)	
	private String tabBar = "true";
	
	@Column(name="TAB_BAR_POSITION", nullable=true, length=30)
	@XmlAttribute
	private String tabBarPosition = "top";
	
	@OneToMany(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Tab.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})	
	@JoinColumn(name="SNCHA_TABPANEL_ID", nullable=true)	
	@org.hibernate.annotations.IndexColumn(name="IDX")	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.FALSE)	
	private java.util.List<Tab> tabs = new java.util.ArrayList<Tab>();
	
	public void setTabBar(String value) {
		this.tabBar = value;
	}
	
	public String getTabBar() {
		return tabBar;
	}
	
	/**
	 * The docked position for the tabBar instance. Possible values are 'top' and 'bottom'.
	 * Defaults to: 'top'
	 */
	public void setTabBarPosition(String value) {
		this.tabBarPosition = value;
	}
	
	/**
	 * The docked position for the tabBar instance. Possible values are 'top' and 'bottom'.
	 * Defaults to: 'top'
	 */
	public String getTabBarPosition() {
		return tabBarPosition;
	}
	
	public void setTabs(java.util.List<Tab> value) {
		this.tabs = value;
	}
	
	public java.util.List<Tab> getTabs() {
		return tabs;
	}
	
	
	public String getXType() {
		return "tabpanel";
	}
	   
	public void removeAllContent() {
		this.getItems().clear();
		for(Tab tab : this.getTabs()) {
			tab.getItems().clear();
		}
	}
	
	public void removeComponent(br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Component component) {
		this.getItems().remove(component);
		boolean boolRemoved = false;
		for(Tab tab : this.getTabs()) {
			if (tab.getItems().contains(component)) {
				tab.getItems().remove(component);
				boolRemoved = true;
				break;
			}
		}
		if (!boolRemoved) {
			Tab removeTab = null;
			for(Tab tab : this.getTabs()) {
				if (tab.getId() == component.getId() || tab.getId() == component.hashCode()) {
					removeTab = tab;
					break;
				}
			}
			if (removeTab != null) {
				for(Component comp : removeTab.getItems()) {
					if (comp instanceof Container) {
						Container containerInt = (Container) comp;
						containerInt.removeAllContent();
					}
				}
				removeTab.getItems().clear();
			}
		}
	}

	public String toString() {
		return super.toString();
	}
	
	public void clone(TabPanel origem, TabPanel destino) {
		destino.setTabBar(new String(origem.getTabBar()));
		destino.setTabBarPosition(new String(origem.getTabBarPosition()));
		for(Tab tab : origem.getTabs()) {
			destino.getTabs().add(tab.clone());
		}
		super.clone(origem, destino);
	}
	
	public TabPanel clone() {
		TabPanel destino = new TabPanel();
		this.clone(this, destino);
		return destino;
	}
}
