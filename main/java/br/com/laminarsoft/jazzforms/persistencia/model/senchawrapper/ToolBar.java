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
 * Ext.Toolbars are most commonly used as docked items as within a Ext.Container. They can be docked either top or bottom using the dockedconfiguration.
 * They allow you to insert items (normally buttons) and also add a title.
 * The defaultType of Ext.Toolbar is Ext.Button.
 * You can alternatively use Ext.TitleBar if you want the title to automatically adjust the size of its items.
 */
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("ToolBar")
@javax.xml.bind.annotation.XmlRootElement(name="toolbar")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@XmlDiscriminatorValue("toolbar")
@SuppressWarnings({ "all", "unchecked" })
public class ToolBar extends br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Container implements Serializable {
	public ToolBar() {
	}
	
	@Column(name="TOOLBAR_TITLE", nullable=true, length=120)	
	private String title = "";
	
	/**
	 * The title of the toolbar.
	 */
	public void setTitle(String value) {
		this.title = value;
	}
	
	/**
	 * The title of the toolbar.
	 */
	public String getTitle() {
		return title;
	}
	
	public String getXType() {
		return "toolbar";
	}
	
	public String toString() {
		return super.toString();
	}
	
	public void clone(ToolBar origem, ToolBar destino) {
		destino.setTitle(new String(origem.getTitle()));
		super.clone(origem, destino);
	}
	
	public ToolBar clone() {
		ToolBar destino = new ToolBar();
		this.clone(this, destino);
		return destino;
	}
}
