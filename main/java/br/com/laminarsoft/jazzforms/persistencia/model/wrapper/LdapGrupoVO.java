package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("all")
@XmlRootElement(name="ldapgrupo")
@XmlAccessorType(XmlAccessType.FIELD)
public class LdapGrupoVO implements Serializable, Comparable<LdapGrupoVO> {
	@XmlAttribute private String nome;
	@XmlAttribute private String cn;
	@XmlAttribute private String dn;
	@XmlAttribute private String description;
	@XmlAttribute private String token;
	
	private List<LdapUsuarioVO> usuarios = new ArrayList<LdapUsuarioVO>();
	
	@Override
	public int compareTo(LdapGrupoVO o) {
		return nome.compareTo(o.getNome());
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
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
	public List<LdapUsuarioVO> getUsuarios() {
		return usuarios;
	}
	public void setUsuarios(List<LdapUsuarioVO> usuarios) {
		this.usuarios = usuarios;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	
	
}
