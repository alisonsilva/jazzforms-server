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
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Table(name="HISTORICO")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@javax.xml.bind.annotation.XmlRootElement(name="historico")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@SuppressWarnings({ "all", "unchecked" })
public class Historico implements Serializable {
	public Historico() {
	}
	
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue(generator="VC0A8380113E3C7E330406DEA")	
	@org.hibernate.annotations.GenericGenerator(name="VC0A8380113E3C7E330406DEA", strategy="native")	
	private long id;
	
	@ManyToOne(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Usuario.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})	
	@JoinColumns({ @JoinColumn(name="USUARIO_ID", referencedColumnName="ID", insertable=false, updatable=false) })	
	private br.com.laminarsoft.jazzforms.persistencia.model.Usuario usuario;
	
	@Column(name="DH_ALTERACAO", nullable=true)	
	private java.util.Date dhAlteracao;
	
	@Column(name="DESCRICAO", nullable=true, length=255)	
	private String descricao;
	
	@Column(name="CODIGO", nullable=true, length=4)	
	private Integer codigo;
	
	public void setId(long value) {
		this.id = value;
	}
	
	public long getId() {
		return id;
	}
	
	public long getORMID() {
		return getId();
	}
	
	public void setDhAlteracao(java.util.Date value) {
		this.dhAlteracao = value;
	}
	
	public java.util.Date getDhAlteracao() {
		return dhAlteracao;
	}
	
	public void setDescricao(String value) {
		this.descricao = value;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setCodigo(int value) {
		setCodigo(new Integer(value));
	}
	
	public void setCodigo(Integer value) {
		this.codigo = value;
	}
	
	public Integer getCodigo() {
		return codigo;
	}
	
	public void setUsuario(br.com.laminarsoft.jazzforms.persistencia.model.Usuario value) {
		this.usuario = value;
	}
	
	public br.com.laminarsoft.jazzforms.persistencia.model.Usuario getUsuario() {
		return usuario;
	}
	
	public String toString() {
		return String.valueOf(getId());
	}
	
	@Transient public static final int CODIGO_REFRESH_DEPLOYMENT = 102;
	@Transient public static final int CODIGO_DEPLOY = 101;
	@Transient public static final int CODIGO_ALTERACAO_DEPLOYMENT = 103; 
	@Transient public static final int CODIGO_ATIVADESATIVA_DEPLOYMENT = 104;
	
}
