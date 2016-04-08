package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("all")
@XmlRootElement(name="ldapusuario")
@XmlAccessorType(XmlAccessType.FIELD)
public class LdapUsuarioVO implements Serializable, Comparable<LdapUsuarioVO> {
	@XmlAttribute private Long id;
	@XmlAttribute private String nome;
	@XmlAttribute private String uid;
	@XmlAttribute private String login;
	@XmlAttribute private String cn;
	@XmlAttribute private String dn;
	@XmlAttribute private String groupDn;
	@XmlAttribute private String passwd;
	@XmlAttribute private String mail;
	@XmlAttribute private String employeeType = "1";
	@XmlAttribute private String token;
	@XmlAttribute private String distinguishedName;
	@XmlAttribute private String canonicalName;
	@XmlAttribute private String matricula;
	@XmlAttribute private String dataNascimento;
	@XmlAttribute private String cpf;
	@XmlAttribute private String telefone;
	@XmlAttribute private boolean ativo = true;
	@XmlAttribute private boolean advogado = false;
	@XmlAttribute private boolean usuarioExterno = false;
	@XmlAttribute private String senha;
	
	private List<LdapGrupoVO> grupos = new ArrayList<LdapGrupoVO>();
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int compareTo(LdapUsuarioVO o) {
		if (nome == null && o.getNome() == null) {
			return 0;
		} else if (nome == null) {
			return -1;
		} else if(nome != null) {
			return 1;
		}
		return nome.compareTo(o.getNome());
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getCn() {
		return cn;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

	public String getDn() {
		return dn;
	}

	public void setDn(String dn) {
		this.dn = dn;
	}

	public String getGroupDn() {
		return groupDn;
	}

	public void setGroupDn(String groupDn) {
		this.groupDn = groupDn;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public List<LdapGrupoVO> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<LdapGrupoVO> grupos) {
		this.grupos = grupos;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getDistinguishedName() {
		return distinguishedName;
	}

	public void setDistinguishedName(String distinguishedName) {
		this.distinguishedName = distinguishedName;
	}

	public String getCanonicalName() {
		return canonicalName;
	}

	public void setCanonicalName(String canonicalName) {
		this.canonicalName = canonicalName;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(String dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public boolean isAdvogado() {
		return advogado;
	}

	public void setAdvogado(boolean advogado) {
		this.advogado = advogado;
	}

	public boolean isUsuarioExterno() {
		return usuarioExterno;
	}

	public void setUsuarioExterno(boolean usuarioExterno) {
		this.usuarioExterno = usuarioExterno;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}	
	
	
}
