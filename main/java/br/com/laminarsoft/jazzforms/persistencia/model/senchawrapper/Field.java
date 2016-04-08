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
@DiscriminatorValue("Field")
@javax.xml.bind.annotation.XmlRootElement(name="field")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@XmlDiscriminatorValue("field")
@SuppressWarnings({ "all", "unchecked" })
public class Field extends br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Component implements Serializable {
	public Field() {
	}
	
	@Column(name="INPUT_CLS", nullable=true, length=80)
	@XmlAttribute
	protected String inputCls = "";
	
	@Column(name="LABEL", nullable=true, length=80)
	@XmlAttribute
	protected String label = "";
	
	@Column(name="LABEL_ALIGN", nullable=true, length=22)
	@XmlAttribute
	protected String labelAlign = "left";
	
	@Column(name="LABEL_WIDTH", nullable=true)
	@XmlAttribute
	protected Double labelWidth;
	
	@Column(name="LABEL_WRAP", nullable=true, length=1)
	@XmlAttribute
	protected boolean labelWrap = false;
	
	@Column(name="NAME", nullable=true, length=80)
	@XmlAttribute
	protected String name = "";
	
	@Column(name="PADDING", nullable=true)
	@XmlAttribute
	protected Double padding;
	
	@Column(name="REQUIRED", nullable=true, length=1)
	@XmlAttribute
	protected boolean required = false;
	
	@Column(name="REQUIRED_CLS", nullable=true, length=80)
	@XmlAttribute
	protected String requiredCls = "";
	
	@Column(name="TABINDEX", nullable=true, length=11)
	@XmlAttribute
	protected Integer tabindex;
	
	@Column(name="VALUE", nullable=true, length=80)
	@XmlAttribute
	protected String value = "";
	
	/**
	 * CSS class to add to the input element of this fields component
	 */
	public void setInputCls(String value) {
		this.inputCls = value;
	}
	
	/**
	 * CSS class to add to the input element of this fields component
	 */
	public String getInputCls() {
		return inputCls;
	}
	
	public void setLabel(String value) {
		this.label = value;
	}
	
	public String getLabel() {
		return label;
	}
	
	/**
	 * The position to render the label relative to the field input. Available options are: 'top', 'left', 'bottom' and 'right'
	 * Defaults to: 'left'
	 */
	public void setLabelAlign(String value) {
		this.labelAlign = value;
	}
	
	/**
	 * The position to render the label relative to the field input. Available options are: 'top', 'left', 'bottom' and 'right'
	 * Defaults to: 'left'
	 */
	public String getLabelAlign() {
		return labelAlign;
	}
	
	/**
	 * The width to make this field's label.
	 * Defaults to: '30%'
	 */
	public void setLabelWidth(double value) {
		setLabelWidth(new Double(value));
	}
	
	/**
	 * The width to make this field's label.
	 * Defaults to: '30%'
	 */
	public void setLabelWidth(Double value) {
		this.labelWidth = value;
	}
	
	/**
	 * The width to make this field's label.
	 * Defaults to: '30%'
	 */
	public Double getLabelWidth() {
		return labelWidth;
	}
	
	/**
	 * true to allow the label to wrap. If set to false, the label will be truncated with an ellipsis.
	 * Defaults to: false
	 */
	public void setLabelWrap(boolean value) {
		this.labelWrap = value;
	}
	
	/**
	 * true to allow the label to wrap. If set to false, the label will be truncated with an ellipsis.
	 * Defaults to: false
	 */
	public boolean getLabelWrap() {
		return labelWrap;
	}
	
	/**
	 * The field's HTML name attribute.
	 * Note: this property must be set if this field is to be automatically included with. form submit().
	 */
	public void setName(String value) {
		this.name = value;
	}
	
	/**
	 * The field's HTML name attribute.
	 * Note: this property must be set if this field is to be automatically included with. form submit().
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * The padding to use on this Component. Can be specified as a number (in which case all edges get the same padding) or a CSS string like '5 10 10 10'
	 */
	public void setPadding(double value) {
		setPadding(new Double(value));
	}
	
	/**
	 * The padding to use on this Component. Can be specified as a number (in which case all edges get the same padding) or a CSS string like '5 10 10 10'
	 */
	public void setPadding(Double value) {
		this.padding = value;
	}
	
	/**
	 * The padding to use on this Component. Can be specified as a number (in which case all edges get the same padding) or a CSS string like '5 10 10 10'
	 */
	public Double getPadding() {
		return padding;
	}
	
	/**
	 * The className to be applied to this Field when the required configuration is set to true.
	 * Defaults to: Ext.baseCSSPrefix + 'field-required'
	 */
	public void setRequired(boolean value) {
		this.required = value;
	}
	
	/**
	 * The className to be applied to this Field when the required configuration is set to true.
	 * Defaults to: Ext.baseCSSPrefix + 'field-required'
	 */
	public boolean getRequired() {
		return required;
	}
	
	public void setRequiredCls(String value) {
		this.requiredCls = value;
	}
	
	public String getRequiredCls() {
		return requiredCls;
	}
	
	public void setTabindex(int value) {
		setTabindex(new Integer(value));
	}
	
	public void setTabindex(Integer value) {
		this.tabindex = value;
	}
	
	public Integer getTabindex() {
		return tabindex;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public String toString() {
		return super.toString();
	}
	
	public void clone(Field origem, Field destino) {
		destino.setFieldId(new String(origem.getFieldId()));
		destino.setInputCls(new String(origem.getInputCls()));
		destino.setLabel(new String(origem.getLabel()));
		destino.setLabelAlign(new String(origem.getLabelAlign()));
		destino.setLabelWidth(origem.getLabelWidth());
		destino.setLabelWrap(origem.getLabelWrap());
		destino.setName(new String(origem.getName()));
		destino.setPadding(origem.getPadding());
		destino.setRequired(origem.getRequired());
		destino.setRequiredCls(new String(origem.getRequiredCls()));
		destino.setTabindex(origem.getTabindex());
		destino.setValue(new String(origem.getValue()));
		super.clone(origem, destino);
	}
	
	public Field clone() {
		Field destino = new Field();
		this.clone(this, destino);
		return destino;
	}
}
