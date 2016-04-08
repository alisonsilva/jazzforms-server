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
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Table(name="SNCHA_LAYOUT")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@javax.xml.bind.annotation.XmlRootElement(name="layout")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@SuppressWarnings({ "all", "unchecked" })
public class Layout implements Serializable {
	@Transient public static final String LAYOUT_FIT = "fit";
	@Transient public static final String LAYOUT_HBOX = "hbox";
	@Transient public static final String LAYOUT_VBOX = "vbox";
	@Transient public static final String LAYOUT_CARD = "card";
	
	public static String[] getTypes() {
		String[] valores = new String[4];
		int idx = 0;
		valores[idx++] = LAYOUT_FIT;
		valores[idx++] = LAYOUT_HBOX;
		valores[idx++] = LAYOUT_VBOX;
		valores[idx++] = LAYOUT_CARD;
		return valores;
	}
	
	public Layout() {
	}
	
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue(generator="VC0A8380113FF6C299640C1F7")	
	@org.hibernate.annotations.GenericGenerator(name="VC0A8380113FF6C299640C1F7", strategy="native")	
	private Long id;
	
	@Column(name="FULL_SCREEN", nullable=false, length=1)	
	@XmlAttribute
	private boolean fullScreen = true;
	
	@Column(name="NOME", nullable=true, length=30)
	@XmlAttribute
	private String nome = "";
	
	@Column(name="DESCRICAO", nullable=true, length=80)	
	private String descricao = "";
	
	public void setId(long value) {
		setId(new Long(value));
	}
	
	public void setId(Long value) {
		this.id = value;
	}
	
	public Long getId() {
		return id;
	}
	
	public Long getORMID() {
		return getId();
	}
	
	public void setFullScreen(boolean value) {
		this.fullScreen = value;
	}
	
	public boolean getFullScreen() {
		return fullScreen;
	}
	
	public void setNome(String value) {
		this.nome = value;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setDescricao(String value) {
		this.descricao = value;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public String toString() {
		return String.valueOf(getId());
	}

	public void clone(Layout origem, Layout destino) {
		destino.setFullScreen(origem.getFullScreen());
		destino.setNome(new String(origem.getNome()));
		destino.setDescricao(new String(origem.getDescricao()));
	}
	
	public Layout clone() {
		Layout destino = new Layout();
		this.clone(this, destino);
		return destino;
	}
}
