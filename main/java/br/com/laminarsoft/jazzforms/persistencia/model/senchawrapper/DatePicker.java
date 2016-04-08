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
 * A date picker component which shows a Date Picker on the screen. This class extends from Ext.picker.Picker and Ext.Sheet so it is a popup.
 * 
 * 
 * ....
 * 
 * Default value for the field and the internal Ext.picker.Date component. Accepts an object of 'year', 'month' and 'day' values, all of which should be numbers, or a Date.
 * Examples:
 * {year: 1989, day: 1, month: 5} = 1st May 1989
 * new Date() = current date
 *  
 */
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("DatePicker")
@javax.xml.bind.annotation.XmlRootElement(name="datepicker")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@XmlDiscriminatorValue("datepicker")
@SuppressWarnings({ "all", "unchecked" })
public class DatePicker extends br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Text implements Serializable {
	public DatePicker() {
	}
	
	@Column(name="DESTROY_PICKER_ON_HIDE", nullable=true, length=1)
	@XmlAttribute
	private boolean destroyPickerOnHide = false;
	
	@Column(name="DAY_TEXT", nullable=true, length=20)	
	@XmlAttribute
	private String dayText = "Dia";
	
	@Column(name="MONTH_TEXT", nullable=true, length=20)	
	@XmlAttribute
	private String monthText = "Mês";
	
	@Column(name="YEAR_FROM", nullable=true, length=11)	
	@XmlAttribute
	private Integer yearFrom;
	
	@Column(name="YEAR_TEXT", nullable=true, length=20)	
	@XmlAttribute
	private String yearText = "Ano";
	
	@Column(name="YEAR_TO", nullable=true, length=11)	
	@XmlAttribute
	private Integer yearTo;
	
	@Column(name="PICKER", nullable=true, length=300)	
	private String picker = null;
	
	/**
	 * The label to show for the day column.
	 * Defaults to: 'Day'
	 */
	public void setDayText(String value) {
		this.dayText = value;
	}
	
	/**
	 * The label to show for the day column.
	 * Defaults to: 'Day'
	 */
	public String getDayText() {
		return dayText;
	}
	
	/**
	 * Whether or not to destroy the picker widget on hide. This save memory if it's not used frequently, but increase delay time on the next show due to re-instantiation.
	 * Defaults to: false
	 */
	public void setDestroyPickerOnHide(boolean value) {
		this.destroyPickerOnHide = value;
	}
	
	/**
	 * Whether or not to destroy the picker widget on hide. This save memory if it's not used frequently, but increase delay time on the next show due to re-instantiation.
	 * Defaults to: false
	 */
	public boolean getDestroyPickerOnHide() {
		return destroyPickerOnHide;
	}
	
	/**
	 * The label to show for the month column.
	 * Defaults to: 'Month'
	 */
	public void setMonthText(String value) {
		this.monthText = value;
	}
	
	/**
	 * The label to show for the month column.
	 * Defaults to: 'Month'
	 */
	public String getMonthText() {
		return monthText;
	}
	
	/**
	 * The start year for the date picker. If yearFrom is greater than yearTo then the order of years will be reversed.
	 * Defaults to: 1980
	 */
	public void setYearFrom(int value) {
		setYearFrom(new Integer(value));
	}
	
	/**
	 * The start year for the date picker. If yearFrom is greater than yearTo then the order of years will be reversed.
	 * Defaults to: 1980
	 */
	public void setYearFrom(Integer value) {
		this.yearFrom = value;
	}
	
	/**
	 * The start year for the date picker. If yearFrom is greater than yearTo then the order of years will be reversed.
	 * Defaults to: 1980
	 */
	public Integer getYearFrom() {
		return yearFrom;
	}
	
	/**
	 * The label to show for the year column.
	 * Defaults to: 'Ano'
	 */
	public void setYearText(String value) {
		this.yearText = value;
	}
	
	/**
	 * The label to show for the year column.
	 * Defaults to: 'Ano'
	 */
	public String getYearText() {
		return yearText;
	}
	
	/**
	 * The last year for the date picker. If yearFrom is greater than yearTo then the order of years will be reversed.
	 * Defaults to: new Date().getFullYear()
	 */
	public void setYearTo(int value) {
		setYearTo(new Integer(value));
	}
	
	/**
	 * The last year for the date picker. If yearFrom is greater than yearTo then the order of years will be reversed.
	 * Defaults to: new Date().getFullYear()
	 */
	public void setYearTo(Integer value) {
		this.yearTo = value;
	}
	
	/**
	 * The last year for the date picker. If yearFrom is greater than yearTo then the order of years will be reversed.
	 * Defaults to: new Date().getFullYear()
	 */
	public Integer getYearTo() {
		return yearTo;
	}
	
	
	public void setPicker(String value) {
		this.picker = value;
	}
	
	public String getPicker() {
		return picker;
	}
	
	public String getXType() {
		return "datepickerfield";
	}
	
	public String toString() {
		return super.toString();
	}
	
	public DatePicker clone() {
		DatePicker destino = new DatePicker();
		destino.setDestroyPickerOnHide(this.getDestroyPickerOnHide());
		destino.setYearFrom(this.getYearFrom().intValue());
		destino.setYearTo(this.getYearTo().intValue());
		destino.setYearText(new String(this.getYearText()));
		destino.setDayText(new String(this.getDayText()));
		destino.setMonthText(new String(this.getMonthText()));
		destino.setPicker(this.getPicker());
		super.clone(this, destino);
		return destino;
	}
	
}
