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
@Table(name="ANEXO")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="anexo")
@SuppressWarnings({ "all", "unchecked" })
public class Anexo implements Serializable {
	public Anexo() {
	}
	
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue(generator="VC0A80103144829694D101DF0")	
	@org.hibernate.annotations.GenericGenerator(name="VC0A80103144829694D101DF0", strategy="native")
	@XmlAttribute
	private Long id;
	
	@Column(name="DH_INCLUSAO", nullable=true)
	@XmlAttribute
	private java.util.Date dhInclusao;
	
	@Column(name="ARQ_ANEXO", nullable=true)	
	private byte[] arqAnexo;
	
	@Column(name="NOME_ARQUIVO", nullable=true, length=255)
	@XmlAttribute
	private String nomeArquivo;
	
	@Column(name="URL_SITE", nullable=true, length=255)
    @XmlAttribute	
	private String urlSite;
	
	@Column(name="TYPE", nullable=true, length=80)
	@XmlAttribute
	private String type;
	
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
	
	public void setDhInclusao(java.util.Date value) {
		this.dhInclusao = value;
	}
	
	public java.util.Date getDhInclusao() {
		return dhInclusao;
	}
	
	public void setArqAnexo(byte[] value) {
		this.arqAnexo = value;
	}
	
	public byte[] getArqAnexo() {
		return arqAnexo;
	}
	
	public void setNomeArquivo(String value) {
		this.nomeArquivo = value;
	}
	
	public String getNomeArquivo() {
		return nomeArquivo;
	}
	
	public void setUrlSite(String value) {
		this.urlSite = value;
	}
	
	public String getUrlSite() {
		return urlSite;
	}
	
	public void setType(String value) {
		this.type = value;
	}
	
	public String getType() {
		return type;
	}
	
	public String toString() {
		return String.valueOf(getId());
	}
	
}
