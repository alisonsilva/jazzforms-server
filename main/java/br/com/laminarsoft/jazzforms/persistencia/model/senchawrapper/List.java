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
 * List is a custom styled DataView which allows Grouping, Indexing, Icons, and a Disclosure. See the Guide and Video for more.
 */
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("List")
@javax.xml.bind.annotation.XmlRootElement(name="list")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@XmlDiscriminatorValue("list")
@SuppressWarnings({ "all", "unchecked" })
public class List extends br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.DataView implements Serializable {
	public List() {
	}
	
	@Column(name="DISCLOSURE_PROPERTY", nullable=true, length=50)	
	@XmlAttribute
	private String disclosureProperty = "";
	
	@Column(name="GROUPED", nullable=true, length=1)
	@XmlAttribute
	private boolean grouped = false;
	
	@Column(name="INDEX_BAR", nullable=true, length=1)
	@XmlAttribute
	private boolean indexBar = false;
	
	@Column(name="INFINITE", nullable=true, length=1)
	@XmlAttribute
	private boolean infinite = false;
	
	@Column(name="LIST_ITEM_HEIGHT", nullable=true, length=11)
	@XmlAttribute
	private Integer listItemHeight = new Integer(47);
	
	@Column(name="PIN_HEADERS", nullable=true, length=1)
	@XmlAttribute
	private boolean pinHeaders = true;
	
	@Column(name="REFRESH_HEIGHT_ON_UPDATE", nullable=true, length=1)
	@XmlAttribute
	private boolean refreshHeightOnUpdate = true;
	
	@Column(name="STRIPED", nullable=true, length=1)
	@XmlAttribute
	private boolean striped = false;
	
	@Column(name="USE_SIMPLE_ITEMS", nullable=true, length=1)
	@XmlAttribute
	private boolean useSimpleItems = true;
	
	@Column(name="VARIABLE_HEIGHTS", nullable=true, length=1)
	@XmlAttribute
	private boolean variableHeights = false;
	
	/**
	 * A property to check on each record to display the disclosure on a per record basis. This property must be false to prevent the disclosure from being displayed on the item.
	 * Defaults to: 'disclosure'
	 */
	public void setDisclosureProperty(String value) {
		this.disclosureProperty = value;
	}
	
	/**
	 * A property to check on each record to display the disclosure on a per record basis. This property must be false to prevent the disclosure from being displayed on the item.
	 * Defaults to: 'disclosure'
	 */
	public String getDisclosureProperty() {
		return disclosureProperty;
	}
	
	/**
	 * Whether or not to group items in the provided Store with a header for each item.
	 * Defaults to: false
	 */
	public void setGrouped(boolean value) {
		this.grouped = value;
	}
	
	/**
	 * Whether or not to group items in the provided Store with a header for each item.
	 * Defaults to: false
	 */
	public boolean getGrouped() {
		return grouped;
	}
	
	/**
	 * true to render an alphabet IndexBar docked on the right. This can also be a config object that will be passed to Ext.IndexBar.
	 * Defaults to: false
	 */
	public void setIndexBar(boolean value) {
		this.indexBar = value;
	}
	
	/**
	 * true to render an alphabet IndexBar docked on the right. This can also be a config object that will be passed to Ext.IndexBar.
	 * Defaults to: false
	 */
	public boolean getIndexBar() {
		return indexBar;
	}
	
	/**
	 * Set this to false to render all items in this list, and render them relatively. Note that this configuration can not be dynamically changed after the list has instantiated.
	 * Defaults to: false
	 */
	public void setInfinite(boolean value) {
		this.infinite = value;
	}
	
	/**
	 * Set this to false to render all items in this list, and render them relatively. Note that this configuration can not be dynamically changed after the list has instantiated.
	 * Defaults to: false
	 */
	public boolean getInfinite() {
		return infinite;
	}
	
	/**
	 * This allows you to set the default item height and is used to roughly calculate the amount of items needed to fill the list. By default items are around 50px high.
	 * Defaults to: 47
	 */
	public void setListItemHeight(int value) {
		setListItemHeight(new Integer(value));
	}
	
	/**
	 * This allows you to set the default item height and is used to roughly calculate the amount of items needed to fill the list. By default items are around 50px high.
	 * Defaults to: 47
	 */
	public void setListItemHeight(Integer value) {
		this.listItemHeight = value;
	}
	
	/**
	 * This allows you to set the default item height and is used to roughly calculate the amount of items needed to fill the list. By default items are around 50px high.
	 * Defaults to: 47
	 */
	public Integer getListItemHeight() {
		return listItemHeight;
	}
	
	/**
	 * Whether or not to pin headers on top of item groups while scrolling for an iPhone native list experience.
	 * Defaults to: true
	 */
	public void setPinHeaders(boolean value) {
		this.pinHeaders = value;
	}
	
	/**
	 * Whether or not to pin headers on top of item groups while scrolling for an iPhone native list experience.
	 * Defaults to: true
	 */
	public boolean getPinHeaders() {
		return pinHeaders;
	}
	
	/**
	 * Set this to false if you make many updates to your list (like in an interval), but updates won't affect the item's height. Doing this will increase the performance of these updates.
	 * Defaults to: true
	 */
	public void setRefreshHeightOnUpdate(boolean value) {
		this.refreshHeightOnUpdate = value;
	}
	
	/**
	 * Set this to false if you make many updates to your list (like in an interval), but updates won't affect the item's height. Doing this will increase the performance of these updates.
	 * Defaults to: true
	 */
	public boolean getRefreshHeightOnUpdate() {
		return refreshHeightOnUpdate;
	}
	
	/**
	 * Set this to true if you want the items in the list to be zebra striped, alternating their background color.
	 * Defaults to: false
	 */
	public void setStriped(boolean value) {
		this.striped = value;
	}
	
	/**
	 * Set this to true if you want the items in the list to be zebra striped, alternating their background color.
	 * Defaults to: false
	 */
	public boolean getStriped() {
		return striped;
	}
	
	/**
	 * Set this to true if you just want to have the list create simple items that use the itemTpl. These simple items still support headers, grouping and disclosure functionality but avoid container layouts and deeply nested markup. For many Lists using this configuration will drastically increase the scrolling and render performance.
	 * Defaults to: true
	 */
	public void setUseSimpleItems(boolean value) {
		this.useSimpleItems = value;
	}
	
	/**
	 * Set this to true if you just want to have the list create simple items that use the itemTpl. These simple items still support headers, grouping and disclosure functionality but avoid container layouts and deeply nested markup. For many Lists using this configuration will drastically increase the scrolling and render performance.
	 * Defaults to: true
	 */
	public boolean getUseSimpleItems() {
		return useSimpleItems;
	}
	
	/**
	 * This configuration allows you optimize the list by not having it read the DOM heights of list items. Instead it will assume (and set) the height to be theitemHeight.
	 * Defaults to: false
	 */
	public void setVariableHeights(boolean value) {
		this.variableHeights = value;
	}
	
	/**
	 * This configuration allows you optimize the list by not having it read the DOM heights of list items. Instead it will assume (and set) the height to be theitemHeight.
	 * Defaults to: false
	 */
	public boolean getVariableHeights() {
		return variableHeights;
	}
	
	public String getXType() {
		return "list";
	}
	
	public String toString() {
		return super.toString();
	}
	
	public void clone(List origem, List destino) {
		destino.setDisclosureProperty(new String(origem.getDisclosureProperty()));
		destino.setGrouped(origem.getGrouped());
		destino.setIndexBar(origem.getIndexBar());
		destino.setInfinite(origem.getInfinite());
		destino.setListItemHeight(origem.getListItemHeight().intValue());
		destino.setPinHeaders(origem.getPinHeaders());
		destino.setRefreshHeightOnUpdate(origem.getRefreshHeightOnUpdate());
		destino.setStriped(origem.getStriped());
		destino.setUseSimpleItems(origem.getUseSimpleItems());
		destino.setVariableHeights(origem.getVariableHeights());
		super.clone(origem, destino);
	}
	
	public List clone() {
		List destino = new List();
		this.clone(this, destino);
		return destino;
	}
}
