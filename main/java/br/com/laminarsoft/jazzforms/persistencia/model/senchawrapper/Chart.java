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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("Chart")
@XmlRootElement(name="chart")
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({ "all", "unchecked" })
public class Chart extends br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Container implements Serializable {
	public Chart() {
	}
	
	@Column(name="CHART_STORE", nullable=true)	
	private String chartStore = "";
	
	@Column(name="ANIMATE", nullable=true, length=1)
	@XmlAttribute
	private Boolean animate = true;
	
	@Column(name="AXES", nullable=true)
	private String axes = "";
	
	@Column(name="SERIES", nullable=true)	
	private String series = "";
	
	@Column(name="URL_ATUALIZACAO_CHART", nullable=true, length=255)	
	private String urlAtualizacao = "";
	
	@Column(name="CHART_LEGEND", nullable=true)	
	private String legend = "";
	
	@Column(name="CHART_INTERACTIONS", nullable=true)	
	private String interactions = "";
	
	@Column(name="IMPLEMENTACAO_CHART", nullable=true)	
	private byte[] implementacao;
	
	@Column(name="SHADOW", nullable=true, length=1)	
	@XmlAttribute
	private Boolean shadow = false;
	
	@Column(name="INSET_PADDING", nullable=true, length=5)	
	private Integer insetPadding;
	
	@Column(name="THEME", nullable=true, length=255)	
	private String theme;
	
	@Column(name="POLAR_CHART", nullable=true, length=1)	
	@XmlAttribute
	private Boolean polarChart = false;
	
	@Column(name="COLORS", nullable=true, length=400)	
	private String colors = "";
	
	@Column(name="CHART_SCROLLABLE", nullable=true, length=1)	
	private Boolean chartScrollable = false;
	
	@Column(name="FLIP_XY", nullable=true, length=1)	
	@XmlAttribute
	private Boolean flipXY = false;
	
	@OneToMany(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Chart.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.LOCK})	
	@JoinColumn(name="SNCHA_COMPONENTID", nullable=true)	
	@org.hibernate.annotations.IndexColumn(name="IDX")	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.FALSE)	
	private java.util.List<Chart> chartsDrill = new java.util.ArrayList<Chart>();
	
	public void setChartStore(String value) {
		this.chartStore = value;
	}
	
	public String getChartStore() {
		return chartStore;
	}
	
	public void setAnimate(boolean value) {
		setAnimate(new Boolean(value));
	}
	
	public void setAnimate(Boolean value) {
		this.animate = value;
	}
	
	public Boolean getAnimate() {
		return animate;
	}
	
	public void setAxes(String value) {
		this.axes = value;
	}
	
	public String getAxes() {
		return axes;
	}
	
	public void setSeries(String value) {
		this.series = value;
	}
	
	public String getSeries() {
		return series;
	}
	
	public String getXType() {
		return "chart";
	}

	public void setLegend(String value) {
		this.legend = value;
	}
	
	public String getLegend() {
		return legend;
	}

	public void setInteractions(String value) {
		this.interactions = value;
	}
	
	public String getInteractions() {
		return interactions;
	}
	public void setUrlAtualizacao(String value) {
		this.urlAtualizacao = value;
	}
	
	public String getUrlAtualizacao() {
		return urlAtualizacao;
	}
	
	public void setImplementacao(byte[] value) {
		this.implementacao = value;
	}
	
	public byte[] getImplementacao() {
		return implementacao;
	}

	
	public void setShadow(boolean value) {
		setShadow(new Boolean(value));
	}
	
	public void setShadow(Boolean value) {
		this.shadow = value;
	}
	
	public Boolean getShadow() {
		return shadow;
	}
	
	public void setInsetPadding(int value) {
		this.insetPadding = value;
	}
	
	public void setInsetPadding(Integer value) {
		this.insetPadding = value;
	}

	public Integer getInsetPadding() {
		return insetPadding;
	}
	
	public void setTheme(String value) {
		this.theme = value;
	}
	
	public String getTheme() {
		return theme;
	}

	public void setPolarChart(boolean value) {
		setPolarChart(new Boolean(value));
	}
	
	public void setPolarChart(Boolean value) {
		this.polarChart = value;
	}
	
	public Boolean getPolarChart() {
		return polarChart;
	}

	public void setColors(String value) {
		this.colors = value;
	}
	
	public String getColors() {
		return colors;
	}

	public void setChartScrollable(boolean value) {
		setChartScrollable(new Boolean(value));
	}
	
	public void setChartScrollable(Boolean value) {
		this.chartScrollable = value;
	}
	
	public Boolean getChartScrollable() {
		return chartScrollable;
	}


	public void setFlipXY(boolean value) {
		setFlipXY(new Boolean(value));
	}
	
	public void setFlipXY(Boolean value) {
		this.flipXY = value;
	}
	
	public Boolean getFlipXY() {
		return flipXY;
	}


	public void setChartsDrill(java.util.List<Chart> value) {
		this.chartsDrill = value;
	}
	
	public java.util.List<Chart> getChartsDrill() {
		return chartsDrill;
	}

	public Chart clone() {
		Chart newChart = new Chart();
		super.clone(this, newChart);
		
		newChart.setAnimate(this.getAnimate());
		newChart.setAxes(this.getAxes());
		newChart.setPolarChart(this.getPolarChart());
		newChart.setSeries(this.getSeries());
		newChart.setChartStore(this.getChartStore());
		newChart.setLegend(this.getLegend());
		newChart.setInteractions(this.getInteractions());
		newChart.setInsetPadding(this.getInsetPadding());
		newChart.setTheme(this.getTheme());
		newChart.setShadow(this.getShadow());
		newChart.setColors(this.getColors());
		newChart.setFlipXY(this.getFlipXY());
		newChart.setChartScrollable(this.getChartScrollable());
		return newChart;
	}
	
	public String toString() {
		return super.toString();
	}
	
}
