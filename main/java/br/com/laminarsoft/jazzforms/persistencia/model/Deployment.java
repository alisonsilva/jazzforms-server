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
import java.util.Collections;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import br.com.laminarsoft.jazzforms.persistencia.model.adapter.DataHoraAdapter;

@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Table(name="DEPLOYMENT")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@javax.xml.bind.annotation.XmlRootElement(name="deployment")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@SuppressWarnings({ "all", "unchecked" })
public class Deployment implements Serializable, Cloneable {
	public Deployment() {
	}
	
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue(generator="VC0A8380113F5C505DA603F32")	
	@org.hibernate.annotations.GenericGenerator(name="VC0A8380113F5C505DA603F32", strategy="native")	
	@XmlAttribute
	private long id;
	
	@ManyToOne(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Projeto.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.PERSIST, org.hibernate.annotations.CascadeType.DELETE})	
	@JoinColumns({ @JoinColumn(name="PROJETO_ID", referencedColumnName="ID", insertable=false, updatable=false) })	
	@Basic(fetch=FetchType.LAZY)	
	@XmlTransient
	private br.com.laminarsoft.jazzforms.persistencia.model.Projeto projeto;
	
	@Column(name="DH_PUBLICACAO", nullable=false)
	@XmlJavaTypeAdapter(value=DataHoraAdapter.class)
	private java.util.Date dhPublicacao;
	
	@Column(name="NOME_PROCESSO_NEGOCIO", nullable=true, length=255)	
	private String nomeProcessoNegocio;
	
	@Column(name="NOME_ATIVIDADE_NEGOCIO", nullable=true, length=255)	
	private String nomeAtividadeNegocio;
	
	@Column(name="ATIVO", nullable=false, length=1)
    @XmlAttribute	
	private boolean ativo = true;
	
	@Column(name="REMOVIDO", nullable=false, length=1)
    @XmlAttribute	
	private boolean removido = false;
	
	@Column(name="DEP_MENSAGEM", nullable=false, length=1)
	@XmlAttribute
	private boolean projetoMensagem = false;
	
	@OneToMany(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.BPInstance.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})	
	@JoinColumn(name="DEPLOYMENT_ID", nullable=false)	
	@org.hibernate.annotations.IndexColumn(name="IDX")	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.FALSE)	
	private java.util.List<BPInstance> instancias = new java.util.ArrayList<BPInstance>();

    @ManyToMany(mappedBy="deployment", targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Usuario.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.LOCK})	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.FALSE)	
	private java.util.List<Usuario> usuariosPossiveis = new java.util.ArrayList<Usuario>();
	
	@ManyToMany(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Grupo.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.LOCK})	
	@JoinTable(name="DEPLOYMENT_GRUPO", joinColumns={ @JoinColumn(name="DEPLOYMENTID") }, inverseJoinColumns={ @JoinColumn(name="GRUPOID") })	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.FALSE)	
	private java.util.List<Grupo> gruposPossiveis = new java.util.ArrayList<Grupo>();
	
	public void setId(long value) {
		this.id = value;
	}
	
	public long getId() {
		return id;
	}
	
	public long getORMID() {
		return getId();
	}
	
	public void setDhPublicacao(java.util.Date value) {
		this.dhPublicacao = value;
	}
	
	public java.util.Date getDhPublicacao() {
		return dhPublicacao;
	}
	
	public void setNomeProcessoNegocio(String value) {
		this.nomeProcessoNegocio = value;
	}
	
	public String getNomeProcessoNegocio() {
		return nomeProcessoNegocio;
	}
	
	public void setNomeAtividadeNegocio(String value) {
		this.nomeAtividadeNegocio = value;
	}
	
	public String getNomeAtividadeNegocio() {
		return nomeAtividadeNegocio;
	}
	
	public void setAtivo(boolean value) {
		this.ativo = value;
	}
	
	public boolean getAtivo() {
		return ativo;
	}
	
	public void setRemovido(boolean value) {
		this.removido = value;
	}
	
	public boolean getRemovido() {
		return removido;
	}
	
	public void setProjetoMensagem(boolean value) {
		this.projetoMensagem = value;
	}
	
	public boolean getProjetoMensagem() {
		return projetoMensagem;
	}
	
	public void setProjeto(br.com.laminarsoft.jazzforms.persistencia.model.Projeto value) {
		this.projeto = value;
	}
	
	public br.com.laminarsoft.jazzforms.persistencia.model.Projeto getProjeto() {
		return projeto;
	}

	
	
	public void setInstancias(java.util.List<BPInstance> value) {
		this.instancias = value;
	}
	
	public java.util.List<BPInstance> getInstancias() {
		return instancias;
	}
	
	
	public void setUsuariosPossiveis(java.util.List<Usuario> value) {
		this.usuariosPossiveis = value;
	}
	
	public java.util.List<Usuario> getUsuariosPossiveis() {
		return usuariosPossiveis;
	}
	
	
	public void setGruposPossiveis(java.util.List<Grupo> value) {
		this.gruposPossiveis = value;
	}
	
	public java.util.List<Grupo> getGruposPossiveis() {
		return gruposPossiveis;
	}
	
	
	@Transient	
	@XmlAttribute
	public long idProjeto;
	
	@Transient	
	@XmlAttribute
	public String nomeProjeto;
	
	@Transient	
	@XmlAttribute
	public String descricaoProjeto;
	
	@Transient
	@XmlAttribute
	public int projetoAplicacao = 0;
	
	@Transient
    @XmlAttribute	
	public long dhUltimaAtualizacao;
	
	public String toString() {
		return String.valueOf(getId());
	}

	@Override
	public Deployment clone() {
		Deployment cldep = new Deployment();
		cldep.setId(this.getId());
		cldep.setAtivo(this.getAtivo());
		cldep.setDhPublicacao(this.getDhPublicacao() == null ? null : new java.util.Date(this.getDhPublicacao().getTime()));
		cldep.setNomeAtividadeNegocio(this.getNomeAtividadeNegocio());
		cldep.setNomeProcessoNegocio(this.getNomeProcessoNegocio());
		cldep.setProjetoMensagem(this.getProjetoMensagem());
		cldep.setRemovido(this.getRemovido());
		cldep.idProjeto = this.idProjeto;
		cldep.nomeProjeto = this.nomeProjeto;
		cldep.descricaoProjeto = this.descricaoProjeto;
		cldep.projetoAplicacao = this.projetoAplicacao;
		cldep.dhUltimaAtualizacao = this.dhUltimaAtualizacao;
		
		for(Grupo grp : this.getGruposPossiveis()) {
			cldep.getGruposPossiveis().add(grp.clone());
		}
		
		for(Usuario usr : this.getUsuariosPossiveis()) {
			cldep.getUsuariosPossiveis().add(usr.clone());
		}
		
		this.getInstancias().removeAll(Collections.singleton(null));
		for(BPInstance inst : this.getInstancias()) {
			cldep.getInstancias().add(inst.clone());
		}
		
		return cldep;		
	}
	
	
}
