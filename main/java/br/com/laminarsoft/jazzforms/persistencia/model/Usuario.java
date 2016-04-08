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
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.StringUtils;
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Table(name="USUARIO")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@javax.xml.bind.annotation.XmlRootElement(name="usuario")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@SuppressWarnings({ "all", "unchecked" })
public class Usuario implements Serializable, Comparable<Usuario>, Cloneable {
	public Usuario() {
	}
	
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue(generator="VC0A8380113E3C7E330606DEB")	
	@org.hibernate.annotations.GenericGenerator(name="VC0A8380113E3C7E330606DEB", strategy="native")
	@XmlAttribute
	private long id;
	
	@Column(name="NOME", nullable=false, length=100)
	@XmlAttribute
	private String nome;
	
	@Column(name="LOGIN", nullable=false, length=30)
	@XmlAttribute
	private String login;
	
	@Column(name="ATIVO", nullable=true, length=1)
	@XmlAttribute	
	private Boolean ativo = true;
	
	@Column(name="DN", nullable=true, length=100)	
	@XmlAttribute
	private String dn;
	
	@Column(name="UID", nullable=true, length=100)	
	@XmlAttribute
	private String uid;
	
	@Column(name="CPF", nullable=true, length=12)	
	@XmlAttribute
	private String cpf;
	
	@Column(name="TELEFONE", nullable=true, length=12)	
	@XmlAttribute
	private String telefone;
	
	@Column(name="DATA_NASCIMENTO", nullable=true)	
	@XmlAttribute
	private java.util.Date dataNascimento;
	
	@Column(name="EMAIL", nullable=true, length=80)	
	@XmlAttribute
	private String email;
	
	@Column(name="USUARIO_EXTERNO", nullable=false, length=1)	
	@XmlAttribute
	private boolean usuarioExterno = false;
	
	@Column(name="DATA_CADASTRO", nullable=true)
	@XmlAttribute	
	private java.util.Date dataCadastro;
	
	@Column(name="SENHA", nullable=true, length=255)	
	@XmlAttribute
	private String senha;
	
	@Column(name="ADVOGADO", nullable=true, length=1)	
	@XmlAttribute
	private Boolean advogado = false;
	
	@Column(name="MATRICULA", nullable=true, length=30)	
	@XmlAttribute
	private String matricula;
	
	@OneToMany(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Historico.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})	
	@JoinColumn(name="USUARIO_ID", nullable=false)	
	@org.hibernate.annotations.IndexColumn(name="IDX")	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)	
	@XmlTransient
	private java.util.List<Historico> historicos = new java.util.ArrayList<Historico>();
	
	@ManyToMany(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Deployment.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.LOCK})	
	@JoinTable(name="DEPLOYMENT_USUARIO", joinColumns={ @JoinColumn(name="USUARIOID") }, inverseJoinColumns={ @JoinColumn(name="DEPLOYMENTID") })	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)	
	@XmlTransient
	private java.util.List<Deployment> deployment = new java.util.ArrayList<Deployment>();
	
	@ManyToMany(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Grupo.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.LOCK})	
	@JoinTable(name="USUARIO_GRUPO", joinColumns={ @JoinColumn(name="USUARIOID") }, inverseJoinColumns={ @JoinColumn(name="GRUPOID") })	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.FALSE)	
	private java.util.List<Grupo> grupos = new java.util.ArrayList();

	@OneToMany(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Mensagem.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})	
	@JoinColumn(name="USUARIO_MENSAGEM_ID", nullable=true)	
	@org.hibernate.annotations.IndexColumn(name="IDX")	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)	
	private java.util.List<Mensagem> mensagens = new java.util.ArrayList<Mensagem>();

	@OneToMany(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Sugestao.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.LOCK})	
	@JoinColumn(name="USUARIO_ID", nullable=true)	
	@org.hibernate.annotations.IndexColumn(name="IDX")	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)	
	private java.util.List<Sugestao> sugestoes = new java.util.ArrayList<Sugestao>();
	
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
	
	public void setMatricula(String value) {
		this.matricula = value;
	}
	
	public String getMatricula() {
		return matricula;
	}
	
	public void setLogin(String value) {
		this.login = value;
	}
	
	public String getLogin() {
		return login;
	}
	
	public void setAtivo(boolean value) {
		setAtivo(new Boolean(value));
	}
	
	public void setAtivo(Boolean value) {
		this.ativo = value;
	}
	
	public Boolean getAtivo() {
		return ativo;
	}
	
	public void setDn(String value) {
		this.dn = value;
	}
	
	public String getDn() {
		return dn;
	}
	
	public void setUid(String value) {
		this.uid = value;
	}
	
	public String getUid() {
		return uid;
	}
	
	public void setCpf(String value) {
		this.cpf = value;
	}
	
	public String getCpf() {
		return cpf;
	}
	
	public void setTelefone(String value) {
		this.telefone = value;
	}
	
	public String getTelefone() {
		return telefone;
	}
	
	public void setDataNascimento(java.util.Date value) {
		this.dataNascimento = value;
	}
	
	public java.util.Date getDataNascimento() {
		return dataNascimento;
	}
	
	public void setEmail(String value) {
		this.email = value;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setUsuarioExterno(boolean value) {
		this.usuarioExterno = value;
	}
	
	public boolean getUsuarioExterno() {
		return usuarioExterno;
	}
	
	public void setDataCadastro(java.util.Date value) {
		this.dataCadastro = value;
	}
	
	public java.util.Date getDataCadastro() {
		return dataCadastro;
	}
	
	public void setSenha(String value) {
		this.senha = value;
	}
	
	public String getSenha() {
		return senha;
	}
	
	public void setAdvogado(boolean value) {
		setAdvogado(new Boolean(value));
	}
	
	public void setAdvogado(Boolean value) {
		this.advogado = value;
	}
	
	public Boolean getAdvogado() {
		return advogado;
	}
	
	public void setHistoricos(java.util.List value) {
		this.historicos = value;
	}
	
	public java.util.List<Historico> getHistoricos() {
		return historicos;
	}
	
	
	public void setDeployment(java.util.List<Deployment> value) {
		this.deployment = value;
	}
	
	public java.util.List<Deployment> getDeployment() {
		return deployment;
	}
	
	
	public void setGrupos(java.util.List<Grupo> value) {
		this.grupos = value;
	}
	
	public java.util.List<Grupo> getGrupos() {
		return grupos;
	}
	
	
	public void setMensagens(java.util.List<Mensagem> value) {
		this.mensagens = value;
	}
	
	public java.util.List<Mensagem> getMensagens() {
		return mensagens;
	}


	public void setSugestoes(java.util.List<Sugestao> value) {
		this.sugestoes = value;
	}
	
	public java.util.List<Sugestao> getSugestoes() {
		return sugestoes;
	}	
	
	@Transient	private String cn;
	
	public String getCn() {
		return cn;
	}
	
	public void setCn(String aCn) {
		cn = aCn;
	}
		

	@Override
    public int hashCode() {
	    return this.getLogin().hashCode();
    }

	@Override
    public boolean equals(Object obj) {
		if (obj instanceof Usuario) {
			Usuario novo = (Usuario)obj;
			if(novo.getLogin() == null) {
				return false;
			} else if(novo.getLogin().equalsIgnoreCase(this.getLogin())) {
				return true;
			}
		}
		return false;
    }

	@Override
    public Usuario clone()  {
		Usuario novoUsuario = new Usuario();
		novoUsuario.setAtivo(this.getAtivo());
		novoUsuario.setCn(this.getCn());
		novoUsuario.setDn(this.getDn());
		novoUsuario.setLogin(this.getLogin());
		novoUsuario.setNome(this.getNome());
		novoUsuario.setUid(this.getUid());
		novoUsuario.setId(this.getId());
		novoUsuario.setCpf(this.getCpf());
		novoUsuario.setTelefone(this.getTelefone());
		novoUsuario.setMatricula(this.getMatricula());
		novoUsuario.setDataNascimento(this.getDataNascimento() == null ? null : new Date(this.getDataNascimento().getTime()));
		novoUsuario.setEmail(this.getEmail());
		novoUsuario.setUsuarioExterno(this.getUsuarioExterno());
		novoUsuario.setDataCadastro(this.getDataCadastro() == null ? null : this.getDataCadastro());
		novoUsuario.setSenha(StringUtils.isEmpty(this.getSenha()) ? null : this.getSenha());		
		novoUsuario.setAdvogado(this.getAdvogado());
		
		//novoUsuario.setSugestoes(null);
		return novoUsuario;
    }

	
	@Override
    public int compareTo(Usuario o) {
		if (o == null || o.getLogin() == null) {
			return 1;
		}
		return getLogin().compareTo(o.getLogin());
    }

	public String toString() {
		return String.valueOf(getId());
	}
	
}
