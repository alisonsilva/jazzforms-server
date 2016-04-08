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
 * ActionSheets are used to display a list of buttons in a popup dialog.
 * The key difference between ActionSheet and Ext.Sheet is that ActionSheets are docked at the bottom of the screen, and thedefaultType is set to button.
 */
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("ActionSheet")
@SuppressWarnings({ "all", "unchecked" })
public class ActionSheet extends br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Sheet implements Serializable {
	public ActionSheet() {
	}
	
	public String getXType() {
		return "actionsheet";
	}
	
	public String toString() {
		return super.toString();
	}
	
	public ActionSheet clone() {
		ActionSheet destino = new ActionSheet();
		super.clone(this, destino);
		return destino;
	}
}
