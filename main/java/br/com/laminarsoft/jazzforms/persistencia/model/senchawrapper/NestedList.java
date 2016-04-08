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
 * NestedList provides a miller column interface to navigate between nested sets and provide a clean interface with limited screen real-estate.
 */
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("NestedList")
@javax.xml.bind.annotation.XmlRootElement(name="nestedlist")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@XmlDiscriminatorValue("nestedlist")
@SuppressWarnings({ "all", "unchecked" })
public class NestedList extends br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Container implements Serializable {
	public NestedList() {
	}
	
	@Column(name="ALLOW_DESELECT", nullable=true, length=1)	
	@XmlAttribute
	private boolean allowDeselect = false;
	
	@Column(name="BACK_BUTTON", nullable=true, length=30)
	@XmlAttribute
	private String backButton = "";
	
	@Column(name="BACK_TEXT", nullable=true, length=50)
	@XmlAttribute
	private String backText = "";
	
	@Column(name="NESTED_LIST_DISPLAY_FIELD", nullable=true, length=30)
	@XmlAttribute
	private String nestedListDisplayField = "";
	
	@Column(name="NESTED_LIST_EMPTY_TEXT", nullable=true, length=50)
	@XmlAttribute
	private String nestedListEmptyText = "";
	
	@Column(name="NESTED_LIST_EMPTY_HEIGHT", nullable=true, length=11)
	@XmlAttribute
	private Integer nestedListItemHeight = new Integer(47);
	
	@Column(name="NESTED_LIST_LOADING_TEXT", nullable=true, length=50)
	@XmlAttribute
	private String nestedListLoadingText = "Carregando...";
	
	@Column(name="NESTED_LIST_STORE", nullable=true, length=255)	
	private String nestedListStore = "";
	
	@Column(name="NESTED_LIST_TITLE", nullable=true, length=50)	
	@XmlAttribute
	private String nestedListTitle = "";
	
	@Column(name="NESTED_LIST_TOOLBAR", nullable=true, length=50)
	@XmlAttribute
	private String nestedListToolBar = "";
	
	@Column(name="DETAIL_CARD", nullable=true, length=255)	
	private String detailCard = "";
	
	@Column(name="DETAIL_CONTAINER", nullable=true, length=255)	
	private String detailContainer = "";
	
	/**
	 * Set to true to allow the user to deselect leaf items via interaction.
	 * Defaults to: false
	 */
	public void setAllowDeselect(boolean value) {
		this.allowDeselect = value;
	}
	
	/**
	 * Set to true to allow the user to deselect leaf items via interaction.
	 * Defaults to: false
	 */
	public boolean getAllowDeselect() {
		return allowDeselect;
	}
	
	/**
	 * The configuration for the back button used in the nested list.
	 * Defaults to: {ui: 'back', hidden: true}
	 */
	public void setBackButton(String value) {
		this.backButton = value;
	}
	
	/**
	 * The configuration for the back button used in the nested list.
	 * Defaults to: {ui: 'back', hidden: true}
	 */
	public String getBackButton() {
		return backButton;
	}
	
	/**
	 * The label to display for the back button.
	 * Defaults to: 'Back'
	 */
	public void setBackText(String value) {
		this.backText = value;
	}
	
	/**
	 * The label to display for the back button.
	 * Defaults to: 'Back'
	 */
	public String getBackText() {
		return backText;
	}
	
	/**
	 * provides the information for a leaf in a Miller column list. In a Miller column, users follow a hierarchial tree structure to a leaf, which provides information about the item in the list. The detailCard lists the information at the leaf.
	 * See http://docs.sencha.com/touch/2-2/#!/guide/nested_list-section-3 and http://en.wikipedia.org/wiki/Miller_columns
	 */
	public void setDetailCard(String value) {
		this.detailCard = value;
	}
	
	/**
	 * provides the information for a leaf in a Miller column list. In a Miller column, users follow a hierarchial tree structure to a leaf, which provides information about the item in the list. The detailCard lists the information at the leaf.
	 * See http://docs.sencha.com/touch/2-2/#!/guide/nested_list-section-3 and http://en.wikipedia.org/wiki/Miller_columns
	 */
	public String getDetailCard() {
		return detailCard;
	}
	
	/**
	 * The container of the detailCard. A detailContainer is a reference to the container where a detail card displays.
	 * See http://docs.sencha.com/touch/2-2/#!/guide/nested_list-section-4 and http://en.wikipedia.org/wiki/Miller_columns
	 * The two possible values for a detailContainer are undefined (default), which indicates that a detailCard appear in the same container, or you can specify a new container location. The default condition uses the current List container.
	 * The following example shows creating a location for a detailContainer:
	 * var detailContainer = Ext.create('Ext.Container', {
	 * layout: 'card'
	 * });
	 * var nestedList = Ext.create('Ext.NestedList', {
	 * store: treeStore,detailCard: true,detailContainer: detailContainer
	 * });
	 * The default value is typically used for phone devices in portrait mode where the small screen size dictates that the detailCard replace the current container.
	 */
	public void setDetailContainer(String value) {
		this.detailContainer = value;
	}
	
	/**
	 * The container of the detailCard. A detailContainer is a reference to the container where a detail card displays.
	 * See http://docs.sencha.com/touch/2-2/#!/guide/nested_list-section-4 and http://en.wikipedia.org/wiki/Miller_columns
	 * The two possible values for a detailContainer are undefined (default), which indicates that a detailCard appear in the same container, or you can specify a new container location. The default condition uses the current List container.
	 * The following example shows creating a location for a detailContainer:
	 * var detailContainer = Ext.create('Ext.Container', {
	 * layout: 'card'
	 * });
	 * var nestedList = Ext.create('Ext.NestedList', {
	 * store: treeStore,detailCard: true,detailContainer: detailContainer
	 * });
	 * The default value is typically used for phone devices in portrait mode where the small screen size dictates that the detailCard replace the current container.
	 */
	public String getDetailContainer() {
		return detailContainer;
	}
	
	/**
	 * Display field to use when setting item text and title. This configuration is ignored when overriding getItemTextTpl or getTitleTextTpl for the item text or title.
	 * Defaults to: 'text'
	 */
	public void setNestedListDisplayField(String value) {
		this.nestedListDisplayField = value;
	}
	
	/**
	 * Display field to use when setting item text and title. This configuration is ignored when overriding getItemTextTpl or getTitleTextTpl for the item text or title.
	 * Defaults to: 'text'
	 */
	public String getNestedListDisplayField() {
		return nestedListDisplayField;
	}
	
	/**
	 * Empty text to display when a subtree is empty.
	 * Defaults to: 'No items available.'
	 */
	public void setNestedListEmptyText(String value) {
		this.nestedListEmptyText = value;
	}
	
	/**
	 * Empty text to display when a subtree is empty.
	 * Defaults to: 'No items available.'
	 */
	public String getNestedListEmptyText() {
		return nestedListEmptyText;
	}
	
	/**
	 * This allows you to set the default item height and is used to roughly calculate the amount of items needed to fill the list. By default items are around 50px high. If you set this configuration in combination with setting the variableHeights to false you can improve the scrolling speed
	 * Defaults to: 47
	 */
	public void setNestedListItemHeight(int value) {
		setNestedListItemHeight(new Integer(value));
	}
	
	/**
	 * This allows you to set the default item height and is used to roughly calculate the amount of items needed to fill the list. By default items are around 50px high. If you set this configuration in combination with setting the variableHeights to false you can improve the scrolling speed
	 * Defaults to: 47
	 */
	public void setNestedListItemHeight(Integer value) {
		this.nestedListItemHeight = value;
	}
	
	/**
	 * This allows you to set the default item height and is used to roughly calculate the amount of items needed to fill the list. By default items are around 50px high. If you set this configuration in combination with setting the variableHeights to false you can improve the scrolling speed
	 * Defaults to: 47
	 */
	public Integer getNestedListItemHeight() {
		return nestedListItemHeight;
	}
	
	/**
	 * Loading text to display when a subtree is loading.
	 * Defaults to: 'Loading...'
	 */
	public void setNestedListLoadingText(String value) {
		this.nestedListLoadingText = value;
	}
	
	/**
	 * Loading text to display when a subtree is loading.
	 * Defaults to: 'Loading...'
	 */
	public String getNestedListLoadingText() {
		return nestedListLoadingText;
	}
	
	/**
	 * The tree store to be used for this nested list.
	 */
	public void setNestedListStore(String value) {
		this.nestedListStore = value;
	}
	
	/**
	 * The tree store to be used for this nested list.
	 */
	public String getNestedListStore() {
		return nestedListStore;
	}
	
	/**
	 * The title of the toolbar
	 * Defaults to: ''
	 */
	public void setNestedListTitle(String value) {
		this.nestedListTitle = value;
	}
	
	/**
	 * The title of the toolbar
	 * Defaults to: ''
	 */
	public String getNestedListTitle() {
		return nestedListTitle;
	}
	
	/**
	 * The configuration to be used for the toolbar displayed in this nested list.
	 * Defaults to: {docked: 'top', xtype: 'titlebar', ui: 'light', inline: true}
	 */
	public void setNestedListToolBar(String value) {
		this.nestedListToolBar = value;
	}
	
	/**
	 * The configuration to be used for the toolbar displayed in this nested list.
	 * Defaults to: {docked: 'top', xtype: 'titlebar', ui: 'light', inline: true}
	 */
	public String getNestedListToolBar() {
		return nestedListToolBar;
	}
	
	public String getXType() {
		return "nestedlist";
	}
	
	public String toString() {
		return super.toString();
	}
	
	public void clone(NestedList origem, NestedList destino) {
		destino.setAllowDeselect(origem.getAllowDeselect());
		destino.setBackButton(new String(origem.getBackButton()));
		destino.setBackText(new String(origem.getBackText()));
		destino.setDetailCard(new String(origem.getDetailCard()));
		destino.setDetailContainer(new String(origem.getDetailContainer()));
		destino.setNestedListDisplayField(new String(origem.getNestedListDisplayField()));
		destino.setNestedListEmptyText(new String(origem.getNestedListEmptyText()));
		destino.setNestedListItemHeight(origem.getNestedListItemHeight().intValue());
		destino.setNestedListLoadingText(new String(origem.getNestedListLoadingText()));
		destino.setNestedListStore(new String(origem.getNestedListStore()));
		destino.setNestedListTitle(new String(origem.getNestedListTitle()));
		destino.setNestedListToolBar(new String(origem.getNestedListToolBar()));
		super.clone(origem, destino);
	}
	
	public NestedList clone() {
		NestedList destino = new NestedList();
		this.clone(this, destino);
		return destino;
	}
}
