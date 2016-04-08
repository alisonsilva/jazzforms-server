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
import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;

import org.apache.commons.lang.StringUtils;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

import br.com.laminarsoft.jazzforms.persistencia.model.ImplementacaoEvento;
import br.com.laminarsoft.jazzforms.persistencia.model.util.InfoPreenchimentoCampos;
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Table(name="SNCHA_COMPONENT")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="DISCRIMINATOR", discriminatorType=DiscriminatorType.STRING)
@DiscriminatorValue("Component")
@javax.xml.bind.annotation.XmlRootElement(name="component")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@XmlDiscriminatorValue("component")
@SuppressWarnings({ "all", "unchecked" })
public class Component implements Serializable {
	public Component() {
	}
	
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue(generator="VC0A8380113FF6C299460C1F5")	
	@org.hibernate.annotations.GenericGenerator(name="VC0A8380113FF6C299460C1F5", strategy="native")	
	@XmlAttribute
	private long id;
	
	@Column(name="BASE_CLS", nullable=true, length=80)	
	protected String baseCls = "";
	
	@Column(name="BOTTOM", nullable=true)	
	protected Double bottom = new Double(0);
	
	@Column(name="CENTERED", nullable=false, length=1)	
	protected boolean centered = true;
	
	@Column(name="CLS", nullable=true, length=80)	
	protected String cls = "";
	
	@Column(name="DISABLED", nullable=false, length=1)	
	protected boolean disabled = false;
	
	@Column(name="DISABLED_CLS", nullable=true, length=80)	
	protected String disabledCls = "";
	
	@Column(name="DOCKED", nullable=true, length=25)	
	protected String docked = "";
	
	@Column(name="FLEX", nullable=true, length=11)	
	protected Integer flex = new Integer(1);
	
	@Column(name="FULLSCREEN", nullable=false, length=1)	
	protected boolean fullscreen = true;
	
	@Column(name="HEIGHT", nullable=true)	
	protected Double height;
	
	@Column(name="HIDDEN", nullable=false, length=1)	
	protected boolean hidden = false;
	
	@Column(name="HTML", nullable=true, length=255)	
	protected String html = "";
	
	@Column(name="`LEFT`", nullable=true)	
	protected Double left = new Double(0);
	
	@Column(name="MARGIN", nullable=true)	
	protected Double margin;
	
	@Column(name="MAX_HEIGHT", nullable=true)	
	protected Double maxHeight;
	
	@Column(name="MAX_WIDTH", nullable=true)	
	protected Double maxWidth;
	
	@Column(name="MIN_HEIGHT", nullable=true)	
	protected Double minHeight;
	
	@Column(name="MIN_WIDTH", nullable=true)	
	protected Double minWidth;
	
	@Column(name="`RIGHT`", nullable=true)	
	protected Double right = new Double(0);
	
	@Column(name="STYLE", nullable=true)	
	protected byte[] style;
	
	@Column(name="TOP", nullable=true)	
	protected Double top = new Double(0);
	
	@Column(name="UI", nullable=true, length=255)	
	protected String ui = "normal";
	
	@Column(name="WIDTH", nullable=true)	
	protected Double width;
	
	@Column(name="PACOTE_CODIGO_CUSTOMIZADO", nullable=true)	
	protected byte[] pacoteCodigoCustomizado;
	
	@Column(name="FIELD_ID", nullable=true, length=70)	
	@XmlAttribute
	protected String fieldId = "";
	
	@Column(name="LABEL_CLS", nullable=true, length=80)	
	protected String labelCls = "";
	
	@Column(name="DESCRICAO", nullable=true, length=255)	
	protected String descricao = "";
	
	@Column(name="JFX_PREF_WIDTH", nullable=true)
	@XmlAttribute	
	private Double jfxPreferedWidth;
	
	@Column(name="JFX_PREF_HEIGHT", nullable=true)
	@XmlAttribute
	private Double jfxPreferedHeight;
	
	@OneToMany(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.ImplementacaoEvento.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})	
	@JoinColumn(name="SNCHA_COMPONENT_ID", nullable=true)	
	@org.hibernate.annotations.IndexColumn(name="IDX")	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.FALSE)	
	private java.util.List<ImplementacaoEvento> implementacoes = new java.util.ArrayList<ImplementacaoEvento>();
	
	public void setId(long value) {
		this.id = value;
	}
	
	public long getId() {
		return id;
	}
	
	public long getORMID() {
		return getId();
	}
	
	/**
	 * The base CSS class to apply to this component's element. This will also be prepended to other elements within this component. To add specific styling for sub-classes, use the cls config.
	 * Defaults to: Ext.baseCSSPrefix + 'field'
	 */
	public void setBaseCls(String value) {
		this.baseCls = value;
	}
	
	/**
	 * The base CSS class to apply to this component's element. This will also be prepended to other elements within this component. To add specific styling for sub-classes, use the cls config.
	 * Defaults to: Ext.baseCSSPrefix + 'field'
	 */
	public String getBaseCls() {
		return baseCls;
	}
	
	public void setBottom(double value) {
		setBottom(new Double(value));
	}
	
	public void setBottom(Double value) {
		this.bottom = value;
	}
	
	public Double getBottom() {
		return bottom;
	}
	
	public void setCentered(boolean value) {
		this.centered = value;
	}
	
	public boolean getCentered() {
		return centered;
	}
	
	/**
	 * 
	 * The CSS class to add to this component's element, in addition to the baseCls.
	 * They are separated by ";"
	 */
	public void setCls(String value) {
		this.cls = value;
	}
	
	/**
	 * 
	 * The CSS class to add to this component's element, in addition to the baseCls.
	 * They are separated by ";"
	 */
	public String getCls() {
		return cls;
	}
	
	public void setDescricao(String value) {
		this.descricao = value;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDisabled(boolean value) {
		this.disabled = value;
	}
	
	public boolean getDisabled() {
		return disabled;
	}
	
	/**
	 * The CSS class to add to the component when it is disabled
	 * Defaults to: "x-item-disabled"
	 */
	public void setDisabledCls(String value) {
		this.disabledCls = value;
	}
	
	/**
	 * The CSS class to add to the component when it is disabled
	 * Defaults to: "x-item-disabled"
	 */
	public String getDisabledCls() {
		return disabledCls;
	}
	
	/**
	 * 
	 * The dock position of this component in its container. Can be left, top, right or bottom.
	 * Notes
	 * You must use a HTML5 doctype for docked bottom to work. To do this, simply add the following code to the HTML file:
	 * <!doctype html>
	 * So your index.html file should look a little like this:
	 * <!doctype html><html>    <head>        <title>MY application title</title>        ...
	 */
	public void setDocked(String value) {
		this.docked = value;
	}
	
	/**
	 * 
	 * The dock position of this component in its container. Can be left, top, right or bottom.
	 * Notes
	 * You must use a HTML5 doctype for docked bottom to work. To do this, simply add the following code to the HTML file:
	 * <!doctype html>
	 * So your index.html file should look a little like this:
	 * <!doctype html><html>    <head>        <title>MY application title</title>        ...
	 */
	public String getDocked() {
		return docked;
	}
	
	/**
	 * The unique id of this component instance.
	 * It should not be necessary to use this configuration except for singleton objects in your application. Components created with an id may be accessed globally using Ext.getCmp.
	 * Instead of using assigned ids, use the itemId config, and ComponentQuery which provides selector-based searching for Sencha Components analogous to DOM querying. The Ext.Container class contains shortcut methods to query its descendant Components by selector.
	 * Note that this id will also be used as the element id for the containing HTML element that is rendered to the page for this component. This allows you to write id-based CSS rules to style the specific instance of this component uniquely, and also to select sub-elements using this component's id as the parent.
	 * Note: to avoid complications imposed by a unique id also see itemId.
	 * Defaults to an auto-assigned id.
	 */
	public void setFieldId(String value) {
		this.fieldId = value;
	}
	
	/**
	 * The unique id of this component instance.
	 * It should not be necessary to use this configuration except for singleton objects in your application. Components created with an id may be accessed globally using Ext.getCmp.
	 * Instead of using assigned ids, use the itemId config, and ComponentQuery which provides selector-based searching for Sencha Components analogous to DOM querying. The Ext.Container class contains shortcut methods to query its descendant Components by selector.
	 * Note that this id will also be used as the element id for the containing HTML element that is rendered to the page for this component. This allows you to write id-based CSS rules to style the specific instance of this component uniquely, and also to select sub-elements using this component's id as the parent.
	 * Note: to avoid complications imposed by a unique id also see itemId.
	 * Defaults to an auto-assigned id.
	 */
	public String getFieldId() {
		return fieldId;
	}
	
	/**
	 * The flex of this item if this item item is inside a Ext.layout.HBox or Ext.layout.VBox layout.
	 */
	public void setFlex(int value) {
		setFlex(new Integer(value));
	}
	
	/**
	 * The flex of this item if this item item is inside a Ext.layout.HBox or Ext.layout.VBox layout.
	 */
	public void setFlex(Integer value) {
		this.flex = value;
	}
	
	/**
	 * The flex of this item if this item item is inside a Ext.layout.HBox or Ext.layout.VBox layout.
	 */
	public Integer getFlex() {
		return flex;
	}
	
	public void setFullscreen(boolean value) {
		this.fullscreen = value;
	}
	
	public boolean getFullscreen() {
		return fullscreen;
	}
	
	/**
	 * The height of this Component; must be a valid CSS length value, e.g: 300, 100px, 30%, etc. By default, if this is not explicitly set, this Component's element will simply have its own natural size.
	 */
	public void setHeight(double value) {
		setHeight(new Double(value));
	}
	
	/**
	 * The height of this Component; must be a valid CSS length value, e.g: 300, 100px, 30%, etc. By default, if this is not explicitly set, this Component's element will simply have its own natural size.
	 */
	public void setHeight(Double value) {
		this.height = value;
	}
	
	/**
	 * The height of this Component; must be a valid CSS length value, e.g: 300, 100px, 30%, etc. By default, if this is not explicitly set, this Component's element will simply have its own natural size.
	 */
	public Double getHeight() {
		return height;
	}
	
	public void setHidden(boolean value) {
		this.hidden = value;
	}
	
	public boolean getHidden() {
		return hidden;
	}
	
	public void setHtml(String value) {
		this.html = value;
	}
	
	public String getHtml() {
		return html;
	}
	
	public void setLabelCls(String value) {
		this.labelCls = value;
	}
	
	public String getLabelCls() {
		return labelCls;
	}
	
	public void setLeft(double value) {
		setLeft(new Double(value));
	}
	
	public void setLeft(Double value) {
		this.left = value;
	}
	
	public Double getLeft() {
		return left;
	}
	
	/**
	 * The margin to use on this Component. Can be specified as a number (in which case all edges get the same margin) or a CSS string like '5 10 10 10'
	 */
	public void setMargin(double value) {
		setMargin(new Double(value));
	}
	
	/**
	 * The margin to use on this Component. Can be specified as a number (in which case all edges get the same margin) or a CSS string like '5 10 10 10'
	 */
	public void setMargin(Double value) {
		this.margin = value;
	}
	
	/**
	 * The margin to use on this Component. Can be specified as a number (in which case all edges get the same margin) or a CSS string like '5 10 10 10'
	 */
	public Double getMargin() {
		return margin;
	}
	
	/**
	 * The maximum height of this Component; must be a valid CSS length value, e.g: 300, 100px, 30%, etc. Note that this config will not apply if the Component is 'floating' (absolutely positioned or centered)
	 */
	public void setMaxHeight(double value) {
		setMaxHeight(new Double(value));
	}
	
	/**
	 * The maximum height of this Component; must be a valid CSS length value, e.g: 300, 100px, 30%, etc. Note that this config will not apply if the Component is 'floating' (absolutely positioned or centered)
	 */
	public void setMaxHeight(Double value) {
		this.maxHeight = value;
	}
	
	/**
	 * The maximum height of this Component; must be a valid CSS length value, e.g: 300, 100px, 30%, etc. Note that this config will not apply if the Component is 'floating' (absolutely positioned or centered)
	 */
	public Double getMaxHeight() {
		return maxHeight;
	}
	
	/**
	 * The maximum width of this Component; must be a valid CSS length value, e.g: 300, 100px, 30%, etc. Note that this config will not apply if the Component is 'floating' (absolutely positioned or centered)
	 */
	public void setMaxWidth(double value) {
		setMaxWidth(new Double(value));
	}
	
	/**
	 * The maximum width of this Component; must be a valid CSS length value, e.g: 300, 100px, 30%, etc. Note that this config will not apply if the Component is 'floating' (absolutely positioned or centered)
	 */
	public void setMaxWidth(Double value) {
		this.maxWidth = value;
	}
	
	/**
	 * The maximum width of this Component; must be a valid CSS length value, e.g: 300, 100px, 30%, etc. Note that this config will not apply if the Component is 'floating' (absolutely positioned or centered)
	 */
	public Double getMaxWidth() {
		return maxWidth;
	}
	
	/**
	 * The minimum height of this Component; must be a valid CSS length value, e.g: 300, 100px, 30%, etc.
	 */
	public void setMinHeight(double value) {
		setMinHeight(new Double(value));
	}
	
	/**
	 * The minimum height of this Component; must be a valid CSS length value, e.g: 300, 100px, 30%, etc.
	 */
	public void setMinHeight(Double value) {
		this.minHeight = value;
	}
	
	/**
	 * The minimum height of this Component; must be a valid CSS length value, e.g: 300, 100px, 30%, etc.
	 */
	public Double getMinHeight() {
		return minHeight;
	}
	
	/**
	 * The minimum width of this Component; must be a valid CSS length value, e.g: 300, 100px, 30%, etc.
	 */
	public void setMinWidth(double value) {
		setMinWidth(new Double(value));
	}
	
	/**
	 * The minimum width of this Component; must be a valid CSS length value, e.g: 300, 100px, 30%, etc.
	 */
	public void setMinWidth(Double value) {
		this.minWidth = value;
	}
	
	/**
	 * The minimum width of this Component; must be a valid CSS length value, e.g: 300, 100px, 30%, etc.
	 */
	public Double getMinWidth() {
		return minWidth;
	}
	
	public void setRight(double value) {
		setRight(new Double(value));
	}
	
	public void setRight(Double value) {
		this.right = value;
	}
	
	public Double getRight() {
		return right;
	}
	
	/**
	 * Optional CSS styles that will be rendered into an inline style attribute when the Component is rendered.  You can pass either a string syntax:  style: 'background:red' Or by using an object:  style: { background: 'red' } When using the object syntax, you can define CSS Properties by using a string:  style: { 'border-left': '1px solid red' } Although the object syntax is much easier to read, we suggest you to use the string syntax for better performance. 
	 */
	public void setStyle(byte[] value) {
		this.style = value;
	}
	
	/**
	 * Optional CSS styles that will be rendered into an inline style attribute when the Component is rendered.  You can pass either a string syntax:  style: 'background:red' Or by using an object:  style: { background: 'red' } When using the object syntax, you can define CSS Properties by using a string:  style: { 'border-left': '1px solid red' } Although the object syntax is much easier to read, we suggest you to use the string syntax for better performance. 
	 */
	public byte[] getStyle() {
		return style;
	}
	
	public void setTop(double value) {
		setTop(new Double(value));
	}
	
	public void setTop(Double value) {
		this.top = value;
	}
	
	public Double getTop() {
		return top;
	}
	
	/**
	 * The ui style to render this button with. The valid default options are:
	 * 'normal' - a basic gray button (default).
	 * 'back' - a back button.
	 * 'forward' - a forward button.
	 * 'round' - a round button.
	 * 'plain'
	 * 'action' - shaded using the $active-color (dark blue by default).
	 * 'decline' - shaded using the $alert-color (red by default).
	 * 'confirm' - shaded using the $confirm-color (green by default).
	 * You can also append -round to each of the last three UI's to give it a round shape:
	 * action-round
	 * decline-round
	 * confirm-round
	 * Defaults to: 'normal'
	 *  
	 */
	public void setUi(String value) {
		this.ui = value;
	}
	
	/**
	 * The ui style to render this button with. The valid default options are:
	 * 'normal' - a basic gray button (default).
	 * 'back' - a back button.
	 * 'forward' - a forward button.
	 * 'round' - a round button.
	 * 'plain'
	 * 'action' - shaded using the $active-color (dark blue by default).
	 * 'decline' - shaded using the $alert-color (red by default).
	 * 'confirm' - shaded using the $confirm-color (green by default).
	 * You can also append -round to each of the last three UI's to give it a round shape:
	 * action-round
	 * decline-round
	 * confirm-round
	 * Defaults to: 'normal'
	 *  
	 */
	public String getUi() {
		return ui;
	}
	
	public void setWidth(double value) {
		setWidth(new Double(value));
	}
	
	public void setWidth(Double value) {
		this.width = value;
	}
	
	public Double getWidth() {
		return width;
	}
	
	public void setPacoteCodigoCustomizado(byte[] value) {
		this.pacoteCodigoCustomizado = value;
		if(value != null && value.length > 0) {
			pacoteCodigoCustomizadoTransformado = new String(value);
		}
	}
	
	public byte[] getPacoteCodigoCustomizado() {
		return pacoteCodigoCustomizado;
	}
	
	public void setJfxPreferedWidth(double value) {
		setJfxPreferedWidth(new Double(value));
	}
	
	public void setJfxPreferedWidth(Double value) {
		this.jfxPreferedWidth = value;
	}
	
	public Double getJfxPreferedWidth() {
		return jfxPreferedWidth;
	}
	
	public void setJfxPreferedHeight(double value) {
		setJfxPreferedHeight(new Double(value));
	}
	
	public void setJfxPreferedHeight(Double value) {
		this.jfxPreferedHeight = value;
	}
	
	public Double getJfxPreferedHeight() {
		return jfxPreferedHeight;
	}
	
	public void setImplementacoes(java.util.List<ImplementacaoEvento> value) {
		this.implementacoes = value;
	}
	
	public java.util.List<ImplementacaoEvento> getImplementacoes() {
		return implementacoes;
	}
	
	@Transient public String pacoteCodigoCustomizadoTransformado;
	
	public String getXType() {
		return "";
	}
	
	public static final br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Field getInstance(String type) {
				Field ret = null;
				switch (type.toUpperCase()) {
					case "SLIDER" : 
						ret = new Slider();
						break;
					case "TOGGLE" :
						ret = new Toggle();
						break;
					case "CHECKBOX" :
						ret = new CheckBox();
						break;
					case "TEXT" :
						ret = new Text();
						break;
					case "RADIO" :
						ret = new Radio();
						break;
					case "DATEPICKER" :
						ret = new DatePicker();
						break;
					case "EMAIL" :
						ret = new Email();
						break;
					case "HIDDEN" :
						ret = new Hidden();
						break;
					case "PASSWORD" : 
						ret = new Password();
						break;			
				}
				return ret;
	}
	
	public String getFieldType() {
		return this.getClass().getSimpleName();
	}
	
	public java.util.List<InfoPreenchimentoCampos> isPreenchimentoValido() {
		java.util.List<InfoPreenchimentoCampos> lst = new ArrayList<InfoPreenchimentoCampos>();
		if (StringUtils.isBlank(fieldId) || StringUtils.isEmpty(fieldId)) {
			InfoPreenchimentoCampos info = new InfoPreenchimentoCampos();
			info.nomeCampo = "Field ID";
			info.mensagem = "O campo \"Field ID\" deve ser preenchido";
			lst.add(info);
		} else if (fieldId.contains(" ")) {
			InfoPreenchimentoCampos info = new InfoPreenchimentoCampos();
			info.nomeCampo = "Field ID";
			info.mensagem = "O campo \"Field ID\" não pode conter espaços em branco";
			lst.add(info);
			
		}
		return lst;
	}
	
	public String toString() {
		return String.valueOf(getId());
	}
	
	public Component clone() {
		Component destino = new Component();
		this.clone(this, destino);
		return destino;
	}
	
	public void clone(Component origem, Component destino) {
		destino.setBaseCls((origem.getBaseCls() == null ? null : new String(origem.getBaseCls())));
		destino.setBottom(origem.getBottom());
		destino.setCentered(origem.getCentered());
		destino.setCls((origem.getCls() == null ? null : new String(origem.getCls())));
		destino.setDisabled(origem.getDisabled());
		destino.setDisabledCls(new String(origem.getDisabledCls()));
		destino.setDocked(new String(origem.getDocked()));
		destino.setFlex(origem.getFlex());
		destino.setFullscreen(origem.getFullscreen());
		destino.setHeight(origem.getHeight());
		destino.setHidden(origem.getHidden());
		destino.setHtml(new String(origem.getHtml()));
		destino.setLabelCls(new String(origem.getLabelCls()));
		destino.setLeft(origem.getLeft());
		destino.setMargin(origem.getMargin());
		destino.setMaxHeight(origem.getMaxHeight());
		destino.setMaxWidth(origem.getMaxWidth());
		destino.setMinHeight(origem.getMinHeight());
		destino.setMinWidth(origem.getMinWidth());
		destino.setRight(origem.getRight());
		destino.setStyle(origem.getStyle());
		destino.setTop(origem.getTop());
		destino.setUi(new String(origem.getUi()));
		destino.setWidth(origem.getWidth());
		destino.setFieldId(origem.getFieldId());
		destino.setDescricao(origem.getDescricao());
		destino.setPacoteCodigoCustomizado(origem.getPacoteCodigoCustomizado());
		destino.setCentered(origem.getCentered());
		destino.setJfxPreferedHeight(origem.getJfxPreferedHeight());
		destino.setJfxPreferedWidth(origem.getJfxPreferedWidth());
		
		for(ImplementacaoEvento met : origem.getImplementacoes()) {
			destino.getImplementacoes().add(met.clone());
		}
	}	
	
}
