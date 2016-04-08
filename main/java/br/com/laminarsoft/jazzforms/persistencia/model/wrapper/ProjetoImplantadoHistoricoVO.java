package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;

@javax.xml.bind.annotation.XmlRootElement(name="projetoImplantadoHistorico")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@SuppressWarnings({ "all", "unchecked" })
public class ProjetoImplantadoHistoricoVO implements Serializable {

	
	@XmlAttribute private Long idProjeto;
	@XmlAttribute private Long idUsuarioProjeto;
	@XmlAttribute private String loginUsuarioProjeto;
	@XmlAttribute private String nomeUsuarioProjeto;
	@XmlAttribute private String descricaoAcaoUsuarioProjeto;
	@XmlAttribute private Date dhAlteracaoProjeto;
	
	@XmlAttribute private Long idUsuarioDeployment;
	@XmlAttribute private String loginUsuarioDeployment;
	@XmlAttribute private String nomeUsuarioDeployment;
	@XmlAttribute private String descricaoAcaoUsuarioDeployment;
	@XmlAttribute private Date dhAlteracaoDeployment;
	
	
	public Long getIdProjeto() {
		return idProjeto;
	}
	public void setIdProjeto(Long idProjeto) {
		this.idProjeto = idProjeto;
	}
	public Long getIdUsuarioProjeto() {
		return idUsuarioProjeto;
	}
	public void setIdUsuarioProjeto(Long idUsuarioProjeto) {
		this.idUsuarioProjeto = idUsuarioProjeto;
	}
	public String getLoginUsuarioProjeto() {
		return loginUsuarioProjeto;
	}
	public void setLoginUsuarioProjeto(String loginUsuarioProjeto) {
		this.loginUsuarioProjeto = loginUsuarioProjeto;
	}
	public String getNomeUsuarioProjeto() {
		return nomeUsuarioProjeto;
	}
	public void setNomeUsuarioProjeto(String nomeUsuarioProjeto) {
		this.nomeUsuarioProjeto = nomeUsuarioProjeto;
	}
	public String getDescricaoAcaoUsuarioProjeto() {
		return descricaoAcaoUsuarioProjeto;
	}
	public void setDescricaoAcaoUsuarioProjeto(String descricaoAcaoUsuarioProjeto) {
		this.descricaoAcaoUsuarioProjeto = descricaoAcaoUsuarioProjeto;
	}
	public Long getIdUsuarioDeployment() {
		return idUsuarioDeployment;
	}
	public void setIdUsuarioDeployment(Long idUsuarioDeployment) {
		this.idUsuarioDeployment = idUsuarioDeployment;
	}
	public String getLoginUsuarioDeployment() {
		return loginUsuarioDeployment;
	}
	public void setLoginUsuarioDeployment(String loginUsuarioDeployment) {
		this.loginUsuarioDeployment = loginUsuarioDeployment;
	}
	public String getNomeUsuarioDeployment() {
		return nomeUsuarioDeployment;
	}
	public void setNomeUsuarioDeployment(String nomeUsuarioDeployment) {
		this.nomeUsuarioDeployment = nomeUsuarioDeployment;
	}
	public String getDescricaoAcaoUsuarioDeployment() {
		return descricaoAcaoUsuarioDeployment;
	}
	public void setDescricaoAcaoUsuarioDeployment(String descricaoAcaoUsuarioDeployment) {
		this.descricaoAcaoUsuarioDeployment = descricaoAcaoUsuarioDeployment;
	}
	public Date getDhAlteracaoProjeto() {
		return dhAlteracaoProjeto;
	}
	public void setDhAlteracaoProjeto(Date dhAlteracaoProjeto) {
		this.dhAlteracaoProjeto = dhAlteracaoProjeto;
	}
	public Date getDhAlteracaoDeployment() {
		return dhAlteracaoDeployment;
	}
	public void setDhAlteracaoDeployment(Date dhAlteracaoDeployment) {
		this.dhAlteracaoDeployment = dhAlteracaoDeployment;
	}
}
