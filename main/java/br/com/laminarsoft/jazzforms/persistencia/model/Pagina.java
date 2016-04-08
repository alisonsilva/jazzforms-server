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
package br.com.laminarsoft.jazzforms.persistencia.model;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Carousel;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Chart;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.DataView;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.FieldSet;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.FormPanel;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.List;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.NavigationView;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.NestedList;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Panel;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.SegmentedButton;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Sheet;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.TabPanel;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.TitleBar;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.ToolBar;
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Table(name="PAGINA")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@javax.xml.bind.annotation.XmlRootElement(name="pagina")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@XmlDiscriminatorValue("pagina")
@SuppressWarnings({ "all", "unchecked" })
public class Pagina implements Serializable {
	public Pagina() {
	}
	
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue(generator="VC0A8380113E3C7E330806E29")	
	@org.hibernate.annotations.GenericGenerator(name="VC0A8380113E3C7E330806E29", strategy="native")
	@javax.xml.bind.annotation.XmlAttribute
	private Long id;
	
	@OneToOne(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Container.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE, org.hibernate.annotations.CascadeType.PERSIST})	
	@JoinColumns({ @JoinColumn(name="SNCHA_COMPONENTID") })
	@XmlElements({
		@XmlElement(name="toolbar", type=ToolBar.class),
		@XmlElement(name="carousel", type=Carousel.class),
		@XmlElement(name="dataview", type=DataView.class),
		@XmlElement(name="fieldset", type=FieldSet.class),
		@XmlElement(name="formpanel", type=FormPanel.class),
		@XmlElement(name="navigationview", type=NavigationView.class),
		@XmlElement(name="list", type=List.class),
		@XmlElement(name="nestedlist", type=NestedList.class),
		@XmlElement(name="panel", type=Panel.class),
		@XmlElement(name="segmentedbutton", type=SegmentedButton.class),
		@XmlElement(name="sheet", type=Sheet.class),
		@XmlElement(name="tabpanel", type=TabPanel.class),
		@XmlElement(name="titlebar", type=TitleBar.class),
		@XmlElement(name="list", type=List.class)
	})
	private br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Container container;
	
	@ManyToOne(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Projeto.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})	
	@JoinColumns({ @JoinColumn(name="PROJETO_ID", referencedColumnName="ID", insertable=false, updatable=false) })	
	@XmlTransient
	private br.com.laminarsoft.jazzforms.persistencia.model.Projeto projeto;
	
	@Column(name="NOME", nullable=false, length=50)	
	@javax.xml.bind.annotation.XmlAttribute	
	private String nome = "";
	
	@Column(name="XTYPE", nullable=true, length=30)	
	private String xtype = "";
	
	@Column(name="PARENT_XTYPE", nullable=true, length=30)	
	private String parentXtype = "";
	
	@Column(name="DESCRICAO", nullable=true, length=255)	
	private String descricao = "";
	
	@Column(name="ORDEM", nullable=false, length=3)	
	@javax.xml.bind.annotation.XmlAttribute	
	private int ordem;
	
	@Column(name="WIDTH", nullable=true)	
	@javax.xml.bind.annotation.XmlAttribute	
	private double width;
	
	@Column(name="HEIGHT", nullable=true)	
	@javax.xml.bind.annotation.XmlAttribute	
	private double height;
	
	@Column(name="BACKGROUND_COLOR", nullable=true, length=40)	
	private String backgroundColor = "";
	
	@Column(name="BACKGROUND_IMAGE", nullable=true)	
	private byte[] backgroundImage;
	
	@Column(name="PACOTE_CD_CUSTOMIZACAO", nullable=true)	
	private byte[] pacoteCodigoCustomizacao;
	
	@Column(name="PAGINA_JSON", nullable=true)	
	private byte[] paginaJson;
	
	@Column(name="STYLE", nullable=true)	
	private byte[] style;
	
	@OneToMany(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.ImplementacaoEvento.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})	
	@JoinColumn(name="PAGINA_ID", nullable=true)	
	@org.hibernate.annotations.IndexColumn(name="IDX")	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.FALSE)
	private java.util.List<ImplementacaoEvento> metodos = new java.util.ArrayList<ImplementacaoEvento>();
	
	public void setId(Long value) {
		this.id = value;
	}
	
	public Long getId() {
		return id;
	}
	
	public long getORMID() {
		return getId();
	}
	
	public void setNome(String value) {
		this.nome = value;
	}
	
	public String getNome() {
		return nome;
	}
	 
	public void setParentXtype(String value) {
		this.parentXtype = value;
	}
	
	public String getParentXtype() {
		return parentXtype;
	}

	public void setDescricao(String value) {
		this.descricao = value;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setOrdem(int value) {
		this.ordem = value;
	}
	
	public int getOrdem() {
		return ordem;
	}
	
	public void setWidth(double value) {
		this.width = value;
	}
	
	public double getWidth() {
		return width;
	}
	
	public void setHeight(double value) {
		this.height = value;
	}
	
	public double getHeight() {
		return height;
	}
	
	public void setBackgroundColor(String value) {
		this.backgroundColor = value;
	}
	
	public String getBackgroundColor() {
		return backgroundColor;
	}
	
	public void setBackgroundImage(byte[] value) {
		this.backgroundImage = value;
	}
	
	public byte[] getBackgroundImage() {
		return backgroundImage;
	}
	
	public void setStyle(byte[] value) {
		this.style = value;
	}
	
	public byte[] getStyle() {
		return style;
	}


	public void setPacoteCodigoCustomizacao(byte[] value) {
		this.pacoteCodigoCustomizacao = value;
		if (value != null && value.length > 0) {
			this.pacoteCodigoCustomizadoTransformado = new String(value);
		}
	}
	
	public byte[] getPacoteCodigoCustomizacao() {
		return pacoteCodigoCustomizacao;
	}

	public void setPaginaJson(byte[] value) {
		this.paginaJson = value;
		if (value != null && value.length > 0) {
			this.paginaJsonTransformado = new String(value);
		}
	}
	
	public byte[] getPaginaJson() {
		return paginaJson;
	}
	
	public void setXtype(String value) {
		//this.xtype = value;
		this.xtype = "container";
	}
	
	public String getXtype() {
		return "container";
	}
	

	public void setProjeto(br.com.laminarsoft.jazzforms.persistencia.model.Projeto value) {
		this.projeto = value;
	}
	
	public br.com.laminarsoft.jazzforms.persistencia.model.Projeto getProjeto() {
		return projeto;
	}
	
	public void setMetodos(java.util.List<ImplementacaoEvento> value) {
		this.metodos = value;
	}
	
	public java.util.List<ImplementacaoEvento> getMetodos() {
		return metodos;
	}
	
	
	public void setContainer(br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Container value) {
		this.container = value;
	}
	
	public br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Container getContainer() {
		return container;
	}
	
	@Transient public String paginaJsonTransformado;
	
	@Transient public String pacoteCodigoCustomizadoTransformado;
	
	public String getFieldType() {
		return "PANEL";
	}
	
	public String toString() {
		return String.valueOf(getId());
	}
	
	@Override
	public Pagina clone() {
		Pagina p = new Pagina();
		p.setId(this.getId());
		p.nome = this.nome;
		p.xtype = this.xtype;
		p.ordem = this.ordem;
		p.descricao = this.descricao;
		p.ordem = this.ordem;
		p.width = this.width;
		p.height = this.height;
		p.backgroundColor = this.backgroundColor;
		p.backgroundImage = this.backgroundImage;
		p.pacoteCodigoCustomizacao = this.pacoteCodigoCustomizacao;
		p.style = this.style;
		p.parentXtype = this.parentXtype;
		p.setPaginaJson(this.getPaginaJson() == null ? null : this.getPaginaJson());
		p.setPacoteCodigoCustomizacao(this.getPacoteCodigoCustomizacao() == null ? null : this.getPacoteCodigoCustomizacao());
		for(ImplementacaoEvento miEvento : this.getMetodos()) {
			p.getMetodos().add(miEvento.clone());
		}
		if (this.getContainer() != null) {
			p.setContainer(this.getContainer().clone());
		}
		return p;
	}
	
}
