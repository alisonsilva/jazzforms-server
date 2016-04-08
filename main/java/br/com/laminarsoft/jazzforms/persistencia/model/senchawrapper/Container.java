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
import java.util.Collections;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("Container")
@javax.xml.bind.annotation.XmlRootElement(name="container")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@SuppressWarnings({ "all", "unchecked" })
public class Container extends br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Component implements Serializable {
	public Container() {
	}
	
	@OneToOne(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Layout.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})
	@JoinColumns({ @JoinColumn(name="LAYOUT_ID") })	
	private br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Layout layout;
	
	@Column(name="ACTIVE_ITEM", nullable=true, length=80)	
	protected String activeItem = "";
	
	@Column(name="AUTO_DESTROY", nullable=true, length=1)	
	@XmlAttribute
	protected boolean autoDestroy = true;
	
	@Column(name="HIDE_ON_MASK_TAP", nullable=true, length=1)	
	@XmlAttribute
	protected boolean hideOnMaskTap = true;
	
	@Column(name="MASKED", nullable=true, length=1)	
	@XmlAttribute
	protected boolean masked = false;
	
	@Column(name="MODAL", nullable=true, length=1)	
	@XmlAttribute
	protected boolean modal = false;
	
	@Column(name="VSCROLLABLE", nullable=true, length=1)	
	@XmlAttribute
	protected boolean vscrollable = false;
	
	@Column(name="HSCROLLABLE", nullable=true, length=1)	
	@XmlAttribute
	protected boolean hscrollable = false;
	
	@Column(name="ITEM_TPL", nullable=true, length=255)	
	protected String itemTpl = "";
	
	@Column(name="STORE", nullable=true, length=255)	
	protected String store = "";
	
	@OneToMany(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Component.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE, org.hibernate.annotations.CascadeType.PERSIST})	
	@JoinColumn(name="COMPONENT_ID", nullable=true)	
	@org.hibernate.annotations.IndexColumn(name="IDX")	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.FALSE)	
	@XmlElements({
		@XmlElement(name="buttom", type=Button.class),
		@XmlElement(name="toolbar", type=ToolBar.class),
		@XmlElement(name="carousel", type=Carousel.class),
		@XmlElement(name="checkbox", type=CheckBox.class),
		@XmlElement(name="dataview", type=DataView.class),
		@XmlElement(name="datepicker", type=DatePicker.class),
		@XmlElement(name="email", type=Email.class),
		@XmlElement(name="field", type=Field.class),
		@XmlElement(name="fieldset", type=FieldSet.class),
		@XmlElement(name="formpanel", type=FormPanel.class),
		@XmlElement(name="geolocation", type=Geolocation.class),
		@XmlElement(name="hidden", type=Hidden.class),
		@XmlElement(name="icon", type=Icon.class),
		@XmlElement(name="image", type=Image.class),
		@XmlElement(name="label", type=Label.class),
		@XmlElement(name="list", type=List.class),
		@XmlElement(name="map", type=Map.class),
		@XmlElement(name="navigationview", type=NavigationView.class),
		@XmlElement(name="nestedlist", type=NestedList.class),
		@XmlElement(name="number", type=Number.class),
		@XmlElement(name="option", type=Option.class),
		@XmlElement(name="panel", type=Panel.class),
		@XmlElement(name="password", type=Password.class),
		@XmlElement(name="picker", type=Picker.class),
		@XmlElement(name="radio", type=Radio.class),
		@XmlElement(name="segmentedbutton", type=SegmentedButton.class),
		@XmlElement(name="select", type=Select.class),
		@XmlElement(name="sheet", type=Sheet.class),
		@XmlElement(name="spinner", type=Spinner.class),
		@XmlElement(name="spacer", type=Spacer.class),
		@XmlElement(name="slider", type=Slider.class),
		@XmlElement(name="tabpanel", type=TabPanel.class),
		@XmlElement(name="text", type=Text.class),
		@XmlElement(name="textarea", type=TextArea.class),
		@XmlElement(name="titlebar", type=TitleBar.class),
		@XmlElement(name="toggle", type=Toggle.class),
		@XmlElement(name="camera", type=Camera.class),
		@XmlElement(name="gps", type=GPSField.class),
		@XmlElement(name="chart", type=Chart.class)
	})
	private java.util.List<Component> items = new java.util.ArrayList<Component>();
	
	/**
	 * The item from the�items�collection that will be active first. This is usually only meaningful in a card layout, where only one item can be active at a time. If passes a string, it will be assumed to be a Ext.ComponentQuery selector.
	 */
	public void setActiveItem(String value) {
		this.activeItem = value;
	}
	
	/**
	 * The item from the�items�collection that will be active first. This is usually only meaningful in a card layout, where only one item can be active at a time. If passes a string, it will be assumed to be a Ext.ComponentQuery selector.
	 */
	public String getActiveItem() {
		return activeItem;
	}
	
	/**
	 * If true, child items will be destroyed as soon as they are removed from this container.
	 */
	public void setAutoDestroy(boolean value) {
		this.autoDestroy = value;
	}
	
	/**
	 * If true, child items will be destroyed as soon as they are removed from this container.
	 */
	public boolean getAutoDestroy() {
		return autoDestroy;
	}
	
	/**
	 * When using a modal Component, setting this to true will hide the modal mask and the Container when the mask is tapped on.
	 */
	public void setHideOnMaskTap(boolean value) {
		this.hideOnMaskTap = value;
	}
	
	/**
	 * When using a modal Component, setting this to true will hide the modal mask and the Container when the mask is tapped on.
	 */
	public boolean getHideOnMaskTap() {
		return hideOnMaskTap;
	}
	
	/**
	 * 
	 * A configuration to allow you to mask this container. You can optionally pass an object block with and xtype of loadmask, and an optional message value to display a loading mask. Please refer to the Ext.LoadMask component to see other configurations.
	 * masked: {    xtype: 'loadmask',    message: 'My message'}
	 * Alternatively, you can just call the setter at any time with true/false to show/hide the mask:
	 * setMasked(true); //show the masksetMasked(false); //hides the mask
	 * There are also two convenient methods, mask and unmask, to allow you to mask and unmask this container at any time.
	 * Remember, the Ext.Viewport is always a container, so if you want to mask your whole application at anytime, can call:
	 * Ext.Viewport.setMasked({    xtype: 'loadmask',    message: 'Hello'});
	 */
	public void setMasked(boolean value) {
		this.masked = value;
	}
	
	/**
	 * 
	 * A configuration to allow you to mask this container. You can optionally pass an object block with and xtype of loadmask, and an optional message value to display a loading mask. Please refer to the Ext.LoadMask component to see other configurations.
	 * masked: {    xtype: 'loadmask',    message: 'My message'}
	 * Alternatively, you can just call the setter at any time with true/false to show/hide the mask:
	 * setMasked(true); //show the masksetMasked(false); //hides the mask
	 * There are also two convenient methods, mask and unmask, to allow you to mask and unmask this container at any time.
	 * Remember, the Ext.Viewport is always a container, so if you want to mask your whole application at anytime, can call:
	 * Ext.Viewport.setMasked({    xtype: 'loadmask',    message: 'Hello'});
	 */
	public boolean getMasked() {
		return masked;
	}
	
	/**
	 * true to make this Container modal. This will create a mask underneath the Container that covers its parent and does not allow the user to interact with any other Components until this Container is dismissed.
	 */
	public void setModal(boolean value) {
		this.modal = value;
	}
	
	/**
	 * true to make this Container modal. This will create a mask underneath the Container that covers its parent and does not allow the user to interact with any other Components until this Container is dismissed.
	 */
	public boolean getModal() {
		return modal;
	}
	
	public void setVscrollable(boolean value) {
		this.vscrollable = value;
	}
	
	public boolean getVscrollable() {
		return vscrollable;
	}
	
	public void setHscrollable(boolean value) {
		this.hscrollable = value;
	}
	
	public boolean getHscrollable() {
		return hscrollable;
	}
	
	/**
	 * The tpl to use for each of the items displayed in this DataView.
	 * Defaults to: '<div>{text}</div>'
	 */
	public void setItemTpl(String value) {
		this.itemTpl = value;
	}
	
	/**
	 * The tpl to use for each of the items displayed in this DataView.
	 * Defaults to: '<div>{text}</div>'
	 */
	public String getItemTpl() {
		return itemTpl;
	}

	/**
	 * The tree store to be used for this nested list.
	 */
	public void setStore(String value) {
		this.store = value;
	}
	
	/**
	 * The tree store to be used for this nested list.
	 */
	public String getStore() {
		return store;
	}
	
	public void setItems(java.util.List<Component> value) {
		this.items = value;
	}
	
	public java.util.List<Component> getItems() {
		return items;
	}
	
	
	public void setLayout(br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Layout value) {
		this.layout = value;
	}
	
	public br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Layout getLayout() {
		return layout;
	}
	
	public String getXType() {
		return "";
	}
	
	public void removeAllContent() {
		this.getItems().clear();
	}
	
	public void removeComponent(br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Component component) {
		this.getItems().remove(component);
	}
	
	public String toString() {
		return super.toString();
	}
	
	
	public void clone(Container origem, Container destino) {
		super.clone(origem, destino);
		destino.setActiveItem(new String(origem.getActiveItem()));
		destino.setAutoDestroy(origem.getAutoDestroy());
		destino.setHideOnMaskTap(origem.getHideOnMaskTap());
		destino.setMasked(origem.getMasked());
		destino.setStore(new String(origem.getStore()));
		destino.setModal(origem.getModal());
		destino.setVscrollable(origem.getVscrollable());
		destino.setHscrollable(origem.getHscrollable());
		destino.setItemTpl(new String(origem.getItemTpl()));
		origem.items.removeAll(Collections.singleton(null));
		for(Component compOrigem : origem.items) {
			Component compDest = compOrigem.clone();
			destino.getItems().add(compDest);
		}
		if (origem.getLayout() != null) {
			destino.setLayout(origem.getLayout().clone());
		}
	}
	
	public Container clone() {
		Container destino = new Container();
		this.clone(this, destino);
		return destino;
	}
}
