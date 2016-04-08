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
import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Table(name="COMPONENT_TYPE")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@javax.xml.bind.annotation.XmlRootElement(name="component_type")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@SuppressWarnings({ "all", "unchecked" })
public class ComponentType implements Serializable {
	public ComponentType() {
	}
	
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue(generator="VC0A83801140112331FE09AC2")	
	@org.hibernate.annotations.GenericGenerator(name="VC0A83801140112331FE09AC2", strategy="native")
	@XmlAttribute
	private long id;
	
	@Column(name="NOME_COMPONENT", nullable=false, length=40)
	@XmlAttribute
	private String nomeComponent;
	
	@Column(name="DESCRICAO", nullable=true, length=255)	
	private String descricao;
	
	public void setId(long value) {
		this.id = value;
	}
	
	public long getId() {
		return id;
	}
	
	public long getORMID() {
		return getId();
	}
	
	public void setNomeComponent(String value) {
		this.nomeComponent = value;
	}
	
	public String getNomeComponent() {
		return nomeComponent;
	}
	
	public void setDescricao(String value) {
		this.descricao = value;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	@Transient	
	public static final String COMPONENT = "COMPONENT";
	
	@Transient	
	public static final String BUTTON = "BUTTON";
	
	@Transient	
	public static final String IMAGE = "IMAGE";
	
	@Transient	
	public static final String SPACER = "SPACER";
	
	@Transient	
	public static final String LABEL = "LABEL";
	
	@Transient	
	public static final String SLIDER = "SLIDER";
	
	@Transient	
	public static final String TOGGLE = "TOGGLE";
	
	@Transient	
	public static final String CHECKBOX = "CHECKBOX";
	
	@Transient	
	public static final String RADIO = "RADIO";
	
	@Transient	
	public static final String TEXT = "TEXT";
	
	@Transient	
	public static final String SELECT = "SELECT";
	
	@Transient	
	public static final String DATEPICKER = "DATEPICKER";
	
	@Transient	
	public static final String EMAIL = "EMAIL";
	
	@Transient	
	public static final String HIDDEN = "HIDDEN";
	
	@Transient	
	public static final String PASSWORD = "PASSWORD";
	
	@Transient	
	public static final String NUMBER = "NUMBER";
	
	@Transient	
	public static final String TEXTAREA = "TEXTAREA";
	
	@Transient	
	public static final String SPINNER = "SPINNER";
	
	@Transient	
	public static final String CONTAINER = "CONTAINER";
	
	@Transient	
	public static final String PANEL = "PANEL";
	
	@Transient	
	public static final String FIELDSET = "FIELDSET";
	
	@Transient	
	public static final String TABPANEL = "TABPANEL";
	
	@Transient	
	public static final String NAVIGATIONVIEW = "NAVIGATIONVIEW";
	
	@Transient	
	public static final String DATAVIEW = "DATAVIEW";
	
	@Transient	
	public static final String MAP = "MAP";
	
	@Transient	
	public static final String TOOLBAR = "TOOLBAR";
	
	@Transient	
	public static final String TITLEBAR = "TITLEBAR";
	
	@Transient	
	public static final String SEGMENTEDBUTTON = "SEGMENTEDBUTTON";
	
	@Transient	
	public static final String LIST = "LIST";
	
	@Transient	
	public static final String NESTEDLIST = "NESTEDLIST";
	
	@Transient	
	public static final String FORMPANEL = "FORMPANEL";
	
	@Transient	
	public static final String SHEET = "SHEET";
	
	@Transient	
	public static final String ACTIONSHEET = "ACTIONSHEET";
	
	@Transient	
	public static final String MESSAGEBOX = "MESSAGEBOX";
	
	@Transient	
	public static final String PICKER = "PICKER";
	
	@Transient	
	public static final String CHART = "CHART";
	
	@Transient	
	public static final String GPS = "GPS";
	
	public String toString() {
		return String.valueOf(getId());
	}
	
}
