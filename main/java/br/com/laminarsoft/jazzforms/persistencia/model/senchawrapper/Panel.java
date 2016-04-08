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

import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;
/**
 * Panels are most useful as Overlays - containers that float over your application. They contain extra styling such that when you showByanother component, the container will appear in a rounded black box with a 'tip' pointing to a reference component. 
 * If you don't need this extra functionality, you should use Ext.Container instead.
 */
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("Panel")
@javax.xml.bind.annotation.XmlRootElement(name="panel")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@XmlDiscriminatorValue("panel")
@SuppressWarnings({ "all", "unchecked" })
public class Panel extends br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Container implements Serializable {
	public Panel() {
	}
	
	public String getXType() {
		return "panel";
	}
	
	public String toString() {
		return super.toString();
	}
	
	public void clone(Panel origem, Panel destino) {
		super.clone((Container)origem, (Container)destino);
	}
	
	public Panel clone() {
		Panel destino = new Panel();
		this.clone((Panel)this, (Panel)destino);
		return destino;
	}
}
