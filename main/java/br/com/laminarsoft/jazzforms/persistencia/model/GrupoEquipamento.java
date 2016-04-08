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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Table(name="GRUPO_EQUIPAMENTO")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@XmlRootElement(name="grupo_equipamento")
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({ "all", "unchecked" })
public class GrupoEquipamento implements Serializable {
	public GrupoEquipamento() {
	}
	
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue(generator="VC0A82B9C143EE7433A20780F")	
	@org.hibernate.annotations.GenericGenerator(name="VC0A82B9C143EE7433A20780F", strategy="native")
	@XmlAttribute
	private Long id;
	
	@Column(name="NOME", nullable=true, length=50)
	@XmlAttribute
	private String nome;
	
	@Column(name="DESCRICAO", nullable=true, length=255)	
	private String descricao;
	
	@ManyToMany(mappedBy="grupos", targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Equipamento.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE, org.hibernate.annotations.CascadeType.LOCK})	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.FALSE)	
	private java.util.List<Equipamento> equipamentos = new java.util.ArrayList<Equipamento>();
	
	@OneToMany(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Mensagem.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE, org.hibernate.annotations.CascadeType.LOCK})	
	@JoinColumn(name="GRUPO_EQUIPAMENTO_ID", nullable=true)	
	@org.hibernate.annotations.IndexColumn(name="IDX")	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.FALSE)	
	private java.util.List<Mensagem> mensagens = new java.util.ArrayList<Mensagem>();
	
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
	
	public void setEquipamentos(java.util.List<Equipamento> value) {
		this.equipamentos = value;
	}
	
	public java.util.List<Equipamento> getEquipamentos() {
		return equipamentos;
	}
	
	
	public void setMensagens(java.util.List<Mensagem> value) {
		this.mensagens = value;
	}
	
	public java.util.List<Mensagem> getMensagens() {
		return mensagens;
	}
	
	
	public String toString() {
		return String.valueOf(getId());
	}
	
}
