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
 * 
 * DataView makes it easy to create lots of components dynamically, usually based off a Store. It's great for rendering lots of data from your server backend or any other data source and is what powers components like Ext.List.
 * Use DataView whenever you want to show sets of the same component many times, for examples in apps like these:
 * List of messages in an email app
 * Showing latest news/tweets
 * Tiled set of albums in an HTML5 music player
 *  
 */
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("DataView")
@SuppressWarnings({ "all", "unchecked" })
@javax.xml.bind.annotation.XmlRootElement(name="dataview")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@XmlDiscriminatorValue("dataview")
public class DataView extends br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Container implements Serializable {
	public DataView() {
	}
	
	@Column(name="OBJECT", nullable=true, length=255)	
	private String object = "";
	
	@Column(name="DEFER_EMPTY_TEXT", nullable=true, length=1)
	@XmlAttribute
	private boolean deferEmptyText = true;
	
	@Column(name="DESELECT_ON_CONTAINER_CLICK", nullable=true, length=1)
	@XmlAttribute
	private boolean deselectOnContainerClick = true;
	
	@Column(name="DISABLE_SELECTION", nullable=true, length=1)
	@XmlAttribute
	private boolean disableSelection = false;
	
	@Column(name="EMPTY_TEXT", nullable=true, length=80)
	@XmlAttribute
	private String emptyText = "";
	
	@Column(name="INLINE", nullable=true, length=1)
	@XmlAttribute
	private boolean inline = false;
	
	@Column(name="ITEM_CLS", nullable=true, length=80)
	@XmlAttribute
	private String itemCls = "";
	
	@Column(name="ITEM_CONFIG", nullable=true, length=80)
	@XmlAttribute
	private String itemConfig = "";
	
	@Column(name="DATA_VIEW_LOADING_TEXT", nullable=true, length=50)
	@XmlAttribute
	private String dataViewLoadingText = "Carregando...";
	
	@Column(name="MAX_ITEM_CACHE", nullable=true, length=11)
	@XmlAttribute
	private Integer maxItemCache = new Integer(20);
	
	@Column(name="DTVIEW_MODE", nullable=true, length=20)
	@XmlAttribute
	private String dtViewMode = "SINGLE";
	
	@Column(name="DATA_VIEW_PRESSED_CLS", nullable=true, length=80)	
	@XmlAttribute
	private String dataViewPressedCls = "";
	
	@Column(name="DATA_VIEW_PRESSED_DELAY", nullable=true, length=11)	
	@XmlAttribute
	private Integer dataViewPressedDelay = new Integer(100);
	
	@Column(name="SCROLL_TO_TOP_ON_REFRESH", nullable=true, length=1)
	@XmlAttribute
	private boolean scrollToTopOnRefresh = true;
	
	@Column(name="DATA_VIEW_SCROLLABLE", nullable=true, length=20)	
	@XmlAttribute
	private String dataViewScrollable = "vertical";
	
	@Column(name="DATA_VIEW_SELECTED_CLS", nullable=true, length=80)	
	@XmlAttribute
	private String dataViewSelectedCls = "";
	
	@Column(name="DATA_VIEW_STORE", nullable=true)	
	private byte[] dataViewStore;
	
	@Column(name="TPL_WRITE_MODE", nullable=true, length=50)	
	@XmlAttribute
	private String tplWriteMode = "overwrite";
	
	@Column(name="TRIGGER_CT_EVENT", nullable=true, length=255)	
	private String triggerCtEvent = "tap";
	
	@Column(name="TRIGGER_EVENT", nullable=true, length=255)	
	private String triggerEvent = "itemtap";
	
	@Column(name="USE_COMPONENTS", nullable=true, length=1)	
	@XmlAttribute
	private boolean useComponents = false;
	
	@Column(name="URL_ATUALIZACAO_DATAVIEW", nullable=true, length=255)	
	protected String urlAtualizacao;
	
	@Column(name="DEFAULT_TYPE", nullable=true, length=120)
	@XmlAttribute	
	private String defaultType;
	
	/**
	 * The initial set of data to apply to the tpl to update the content area of the Component.
	 */
	public void setObject(String value) {
		this.object = value;
	}
	
	/**
	 * The initial set of data to apply to the tpl to update the content area of the Component.
	 */
	public String getObject() {
		return object;
	}
	
	/**
	 * true to defer emptyText being applied until the store's first load.
	 * Defaults to: true
	 */
	public void setDeferEmptyText(boolean value) {
		this.deferEmptyText = value;
	}
	
	/**
	 * true to defer emptyText being applied until the store's first load.
	 * Defaults to: true
	 */
	public boolean getDeferEmptyText() {
		return deferEmptyText;
	}
	
	/**
	 * When set to true, tapping on the DataView's background (i.e. not on an item in the DataView) will deselect any currently selected items.
	 * Defaults to: true
	 */
	public void setDeselectOnContainerClick(boolean value) {
		this.deselectOnContainerClick = value;
	}
	
	/**
	 * When set to true, tapping on the DataView's background (i.e. not on an item in the DataView) will deselect any currently selected items.
	 * Defaults to: true
	 */
	public boolean getDeselectOnContainerClick() {
		return deselectOnContainerClick;
	}
	
	/**
	 * true to disable selection. This configuration will lock the selection model that the DataView uses.
	 */
	public void setDisableSelection(boolean value) {
		this.disableSelection = value;
	}
	
	/**
	 * true to disable selection. This configuration will lock the selection model that the DataView uses.
	 */
	public boolean getDisableSelection() {
		return disableSelection;
	}
	
	/**
	 * The text to display in the view when there is no data to display
	 */
	public void setEmptyText(String value) {
		this.emptyText = value;
	}
	
	/**
	 * The text to display in the view when there is no data to display
	 */
	public String getEmptyText() {
		return emptyText;
	}
	
	/**
	 * When set to true the items within the DataView will have their display set to inline-block and be arranged horizontally. By default the items will wrap to the width of the DataView. Passing an object with { wrap: false } will turn off this wrapping behavior and overflowed items will need to be scrolled to horizontally.
	 */
	public void setInline(boolean value) {
		this.inline = value;
	}
	
	/**
	 * When set to true the items within the DataView will have their display set to inline-block and be arranged horizontally. By default the items will wrap to the width of the DataView. Passing an object with { wrap: false } will turn off this wrapping behavior and overflowed items will need to be scrolled to horizontally.
	 */
	public boolean getInline() {
		return inline;
	}
	
	/**
	 * An additional CSS class to apply to items within the DataView.
	 */
	public void setItemCls(String value) {
		this.itemCls = value;
	}
	
	/**
	 * An additional CSS class to apply to items within the DataView.
	 */
	public String getItemCls() {
		return itemCls;
	}
	
	/**
	 * A configuration object that is passed to every item created by a component based DataView. Because each item that a DataView renders is a Component, we can pass configuration options to each component to easily customize how each child component behaves.
	 * Note: this is only used when useComponents is true.
	 * Defaults to: {}
	 */
	public void setItemConfig(String value) {
		this.itemConfig = value;
	}
	
	/**
	 * A configuration object that is passed to every item created by a component based DataView. Because each item that a DataView renders is a Component, we can pass configuration options to each component to easily customize how each child component behaves.
	 * Note: this is only used when useComponents is true.
	 * Defaults to: {}
	 */
	public String getItemConfig() {
		return itemConfig;
	}
	
	/**
	 * A string to display during data load operations. If specified, this text will be displayed in a loading div and the view's contents will be cleared while loading, otherwise the view's contents will continue to display normally until the new data is loaded and the contents are replaced.
	 * Defaults to: 'Loading...'
	 */
	public void setDataViewLoadingText(String value) {
		this.dataViewLoadingText = value;
	}
	
	/**
	 * A string to display during data load operations. If specified, this text will be displayed in a loading div and the view's contents will be cleared while loading, otherwise the view's contents will continue to display normally until the new data is loaded and the contents are replaced.
	 * Defaults to: 'Loading...'
	 */
	public String getDataViewLoadingText() {
		return dataViewLoadingText;
	}
	
	/**
	 * Maintains a cache of reusable components when using a component based DataView. Improving performance at the cost of memory.
	 * Note: this is currently only used when useComponents is true.
	 * Defaults to: 20
	 */
	public void setMaxItemCache(int value) {
		setMaxItemCache(new Integer(value));
	}
	
	/**
	 * Maintains a cache of reusable components when using a component based DataView. Improving performance at the cost of memory.
	 * Note: this is currently only used when useComponents is true.
	 * Defaults to: 20
	 */
	public void setMaxItemCache(Integer value) {
		this.maxItemCache = value;
	}
	
	/**
	 * Maintains a cache of reusable components when using a component based DataView. Improving performance at the cost of memory.
	 * Note: this is currently only used when useComponents is true.
	 * Defaults to: 20
	 */
	public Integer getMaxItemCache() {
		return maxItemCache;
	}
	
	/**
	 * Modes of selection. Valid values are 'SINGLE', 'SIMPLE', and 'MULTI'.
	 * Defaults to: 'SINGLE'
	 */
	public void setDtViewMode(String value) {
		this.dtViewMode = value;
	}
	
	/**
	 * Modes of selection. Valid values are 'SINGLE', 'SIMPLE', and 'MULTI'.
	 * Defaults to: 'SINGLE'
	 */
	public String getDtViewMode() {
		return dtViewMode;
	}
	
	/**
	 * The CSS class to apply to an item on the view while it is being pressed.
	 * Defaults to: 'x-item-pressed'
	 */
	public void setDataViewPressedCls(String value) {
		this.dataViewPressedCls = value;
	}
	
	/**
	 * The CSS class to apply to an item on the view while it is being pressed.
	 * Defaults to: 'x-item-pressed'
	 */
	public String getDataViewPressedCls() {
		return dataViewPressedCls;
	}
	
	/**
	 * The amount of delay between the tapstart and the moment we add the pressedCls.
	 * Settings this to true defaults to 100ms.
	 * Defaults to: 100
	 */
	public void setDataViewPressedDelay(int value) {
		setDataViewPressedDelay(new Integer(value));
	}
	
	/**
	 * The amount of delay between the tapstart and the moment we add the pressedCls.
	 * Settings this to true defaults to 100ms.
	 * Defaults to: 100
	 */
	public void setDataViewPressedDelay(Integer value) {
		this.dataViewPressedDelay = value;
	}
	
	/**
	 * The amount of delay between the tapstart and the moment we add the pressedCls.
	 * Settings this to true defaults to 100ms.
	 * Defaults to: 100
	 */
	public Integer getDataViewPressedDelay() {
		return dataViewPressedDelay;
	}
	
	/**
	 * Scroll the DataView to the top when the DataView is refreshed.
	 * Defaults to: true
	 */
	public void setScrollToTopOnRefresh(boolean value) {
		this.scrollToTopOnRefresh = value;
	}
	
	/**
	 * Scroll the DataView to the top when the DataView is refreshed.
	 * Defaults to: true
	 */
	public boolean getScrollToTopOnRefresh() {
		return scrollToTopOnRefresh;
	}
	
	/**
	 * Configuration options to make this Container scrollable. Acceptable values are:
	 * 'horizontal', 'vertical', 'both' to enabling scrolling for that direction.
	 * true/false to explicitly enable/disable scrolling.
	 * Alternatively, you can give it an object which is then passed to the scroller instance:
	 * scrollable: {    direction: 'vertical',    directionLock: true}
	 * Please look at the Ext.scroll.Scroller documentation for more example on how to use this.
	 * Defaults to: true
	 *  
	 */
	public void setDataViewScrollable(String value) {
		this.dataViewScrollable = value;
	}
	
	/**
	 * Configuration options to make this Container scrollable. Acceptable values are:
	 * 'horizontal', 'vertical', 'both' to enabling scrolling for that direction.
	 * true/false to explicitly enable/disable scrolling.
	 * Alternatively, you can give it an object which is then passed to the scroller instance:
	 * scrollable: {    direction: 'vertical',    directionLock: true}
	 * Please look at the Ext.scroll.Scroller documentation for more example on how to use this.
	 * Defaults to: true
	 *  
	 */
	public String getDataViewScrollable() {
		return dataViewScrollable;
	}
	
	/**
	 * The CSS class to apply to an item on the view while it is selected.
	 * Defaults to: 'x-item-selected'
	 */
	public void setDataViewSelectedCls(String value) {
		this.dataViewSelectedCls = value;
	}
	
	/**
	 * The CSS class to apply to an item on the view while it is selected.
	 * Defaults to: 'x-item-selected'
	 */
	public String getDataViewSelectedCls() {
		return dataViewSelectedCls;
	}
	
	/**
	 * Can be either a Store instance or a configuration object that will be turned into a Store. The Store is used to populate the set of items that will be rendered in the DataView. See the DataView intro documentation for more information about the relationship between Store and DataView.
	 */
	public void setDataViewStore(byte[] value) {
		this.dataViewStore = value;
		this.dataViewStoreConv = new String(value);
	}
	
	/**
	 * Can be either a Store instance or a configuration object that will be turned into a Store. The Store is used to populate the set of items that will be rendered in the DataView. See the DataView intro documentation for more information about the relationship between Store and DataView.
	 */
	public byte[] getDataViewStore() {
		if (dataViewStore == null) {
			dataViewStore = new byte[0];
		}
		this.dataViewStoreConv = new String(dataViewStore);
		return dataViewStore;
	}
	
	/**
	 * The Ext.(X)Template method to use when updating the content area of the Component. Valid modes are:
	 * append
	 * insertAfter
	 * insertBefore
	 * insertFirst
	 * overwrite
	 * Defaults to: 'overwrite'
	 *  
	 */
	public void setTplWriteMode(String value) {
		this.tplWriteMode = value;
	}
	
	/**
	 * The Ext.(X)Template method to use when updating the content area of the Component. Valid modes are:
	 * append
	 * insertAfter
	 * insertBefore
	 * insertFirst
	 * overwrite
	 * Defaults to: 'overwrite'
	 *  
	 */
	public String getTplWriteMode() {
		return tplWriteMode;
	}
	
	/**
	 * Determines what type of touch event is recognized as a touch on the container. Valid options are 'tap' and 'singletap'.
	 * Defaults to: 'tap'
	 */
	public void setTriggerCtEvent(String value) {
		this.triggerCtEvent = value;
	}
	
	/**
	 * Determines what type of touch event is recognized as a touch on the container. Valid options are 'tap' and 'singletap'.
	 * Defaults to: 'tap'
	 */
	public String getTriggerCtEvent() {
		return triggerCtEvent;
	}
	
	/**
	 * Determines what type of touch event causes an item to be selected. Valid options are: 'itemtap', 'itemsingletap', 'itemdoubletap', 'itemswipe', 'itemtaphold'.
	 * Defaults to: 'itemtap'
	 */
	public void setTriggerEvent(String value) {
		this.triggerEvent = value;
	}
	
	/**
	 * Determines what type of touch event causes an item to be selected. Valid options are: 'itemtap', 'itemsingletap', 'itemdoubletap', 'itemswipe', 'itemtaphold'.
	 * Defaults to: 'itemtap'
	 */
	public String getTriggerEvent() {
		return triggerEvent;
	}
	
	/**
	 * Flag the use a component based DataView implementation. This allows the full use of components in the DataView at the cost of some performance.
	 * Checkout the DataView Guide for more information on using this configuration.
	 */
	public void setUseComponents(boolean value) {
		this.useComponents = value;
	}
	
	/**
	 * Flag the use a component based DataView implementation. This allows the full use of components in the DataView at the cost of some performance.
	 * Checkout the DataView Guide for more information on using this configuration.
	 */
	public boolean getUseComponents() {
		return useComponents;
	}
	
	public void setUrlAtualizacao(String value) {
		this.urlAtualizacao = value;
	}
	
	public String getUrlAtualizacao() {
		return urlAtualizacao;
	}
	public void setDefaultType(String value) {
		this.defaultType = value;
	}
	
	public String getDefaultType() {
		return defaultType;
	}
	
	public String getXType() {
		return "dataview";
	}
	
	public String toString() {
		return super.toString();
	}
	
	@Transient
	public String dataViewStoreConv;
	
	public void clone(DataView origem, DataView destino) {
		destino.setObject(new String(origem.getObject()));
		destino.setDeferEmptyText(origem.getDeferEmptyText());
		destino.setDeselectOnContainerClick(origem.getDeselectOnContainerClick());
		destino.setDisableSelection(origem.getDisableSelection());
		destino.setEmptyText(new String(origem.getEmptyText()));
		destino.setInline(origem.getInline());
		destino.setItemCls(new String(origem.getItemCls()));
		destino.setItemConfig(new String(origem.getItemConfig()));
		destino.setDataViewLoadingText(new String(origem.getDataViewLoadingText()));
		destino.setMaxItemCache(origem.getMaxItemCache());
		destino.setDtViewMode(new String(origem.getDtViewMode()));
		destino.setDataViewPressedCls(new String(origem.getDataViewPressedCls()));
		destino.setDataViewPressedDelay(origem.getDataViewPressedDelay());
		destino.setScrollToTopOnRefresh(origem.getScrollToTopOnRefresh());
		destino.setDataViewScrollable(new String(origem.getDataViewScrollable()));
		destino.setDataViewSelectedCls(new String(origem.getDataViewSelectedCls()));
		destino.setDataViewStore(origem.getDataViewStore());
		destino.setTplWriteMode(new String(origem.getTplWriteMode()));
		destino.setTriggerCtEvent(new String(origem.getTriggerCtEvent()));
		destino.setTriggerEvent(new String(origem.getTriggerEvent()));
		destino.setUseComponents(origem.getUseComponents());
		destino.setUrlAtualizacao(origem.getUrlAtualizacao());
		destino.setDefaultType(origem.getDefaultType());
		super.clone(origem, destino);
	}
	
	public DataView clone() {
		DataView destino = new DataView();
		this.clone(this, destino);
		return destino;
	}
}
