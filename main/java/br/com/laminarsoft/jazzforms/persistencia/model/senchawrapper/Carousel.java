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
@DiscriminatorValue("Carousel")
@javax.xml.bind.annotation.XmlRootElement(name="carousel")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@XmlDiscriminatorValue("carousel")
@SuppressWarnings({ "all", "unchecked" })
public class Carousel extends br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Container implements Serializable {
	public Carousel() {
	}
	
	@Column(name="DIRECTION", nullable=true, length=20)	
	@XmlAttribute
	private String direction = "horizontal";
	
	@Column(name="INDICATOR", nullable=true, length=1)
	@XmlAttribute
	private boolean indicator = true;
	
	@OneToMany(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Tab.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})	
	@JoinColumn(name="SNCHA_CAROUSEL_ID", nullable=true)	
	@org.hibernate.annotations.IndexColumn(name="IDX")	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.FALSE)	
	private java.util.List<Tab> paginas = new java.util.ArrayList<Tab>();
	
	/**
	 * The direction of the Carousel, either 'horizontal' or 'vertical'. Defaults to: 'horizontal'
	 */
	public void setDirection(String value) {
		this.direction = value;
	}
	
	/**
	 * The direction of the Carousel, either 'horizontal' or 'vertical'. Defaults to: 'horizontal'
	 */
	public String getDirection() {
		return direction;
	}
	
	/**
	 * Provides an indicator while toggling between child items to let the user know where they are in the card stack. Defaults to: true
	 */
	public void setIndicator(boolean value) {
		this.indicator = value;
	}
	
	/**
	 * Provides an indicator while toggling between child items to let the user know where they are in the card stack. Defaults to: true
	 */
	public boolean getIndicator() {
		return indicator;
	}
	
	public void setPaginas(java.util.List<Tab> value) {
		this.paginas = value;
	}
	
	public java.util.List<Tab> getPaginas() {
		return paginas;
	}
	
	
	public void removeAllContent() {
		this.getItems().clear();
		for(Tab tab : this.getPaginas()) {
			tab.getItems().clear();
		}
	}
	
	public void removeComponent(br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Component component) {
		this.getItems().remove(component);
		boolean boolRemoved = false;
		for(Tab tab : this.getPaginas()) {
			if (tab.getItems().contains(component)) {
				tab.getItems().remove(component);
				boolRemoved = true;
				break;
			}
		}
		if (!boolRemoved) {
			Tab removeTab = null;
			for(Tab tab : this.getPaginas()) {
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
	
	public String getXType() {
		return "carousel";
	}

	public String toString() {
		return super.toString();
	}

	@Override
    public Carousel clone() {
		Carousel destino = new Carousel();
		super.clone(this, destino);
		destino.setDirection(new String(this.getDirection().toString()));
		destino.setIndicator(this.getIndicator());
		for(Tab tab : this.getPaginas()) {
			destino.getPaginas().add(tab.clone());
		}
		return destino;
    }
	
	
	
}
