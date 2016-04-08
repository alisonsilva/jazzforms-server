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
@Table(name="IMPLEMENTACAO_EVENTO")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@javax.xml.bind.annotation.XmlRootElement(name="implementacao_evento")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@SuppressWarnings({ "all", "unchecked" })
public class ImplementacaoEvento implements Serializable {
	public ImplementacaoEvento() {
	}
	
	@Column(name="Id", nullable=false)	
	@Id	
	@GeneratedValue(generator="VC0A8380113FF6B066310582C")	
	@org.hibernate.annotations.GenericGenerator(name="VC0A8380113FF6B066310582C", strategy="native")	
	@XmlAttribute
	private long id;
	
	@ManyToOne(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.TipoEvento.class)	
	@org.hibernate.annotations.Cascade({})	
	@JoinColumns({ @JoinColumn(name="TIPO_EVENTO_ID", referencedColumnName="ID") })	
	private br.com.laminarsoft.jazzforms.persistencia.model.TipoEvento tipoEvento;
	
	@Column(name="NOME", nullable=true, length=100)
	@XmlAttribute
	private String nome = "";
	
	@Column(name="DESCRICAO", nullable=true, length=255)	
	private String descricao = "";
	
	@Column(name="IMPLEMENTACAO", nullable=false)	
	private byte[] implementacao;
	
	public void setId(long value) {
		this.id = value;
	}
	
	public long getId() {
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
	
	public void setDescricao(String value) {
		this.descricao = value;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setImplementacao(byte[] value) {
		this.implementacao = value;
		if(value != null && value.length > 0) {
			implementacaoTransformada = new String(value);
		}
	}
	
	public byte[] getImplementacao() {
		return implementacao;
	}
	
	public void setTipoEvento(br.com.laminarsoft.jazzforms.persistencia.model.TipoEvento value) {
		this.tipoEvento = value;
	}
	
	public br.com.laminarsoft.jazzforms.persistencia.model.TipoEvento getTipoEvento() {
		return tipoEvento;
	}
	
	public String toString() {
		return String.valueOf(getId());
	}
	
	@Transient public String implementacaoTransformada;
	
	@Override
	public ImplementacaoEvento clone() {
		ImplementacaoEvento m = new ImplementacaoEvento();
		m.nome = this.nome;
		m.descricao = this.descricao;
		m.tipoEvento = this.tipoEvento;
		m.implementacao = this.implementacao;
		return m;
	}
}
