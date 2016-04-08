package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;

@javax.xml.bind.annotation.XmlRootElement(name="projetoImplantado")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@SuppressWarnings({ "all", "unchecked" })
public class ProjetoImplantadoVO implements Serializable, Comparable<ProjetoImplantadoVO> {

	@XmlAttribute private Long deployId;
	@XmlAttribute private Date dhPublicacao;
	@XmlAttribute private Boolean ativo;
	@XmlAttribute private Boolean removido;
	@XmlAttribute private Long projetoId;
	@XmlAttribute private String nomeProjeto;
	private String descProjeto;
	@XmlAttribute private Date dhCriacao;
	@XmlAttribute private Boolean publicado;
	@XmlAttribute private Boolean substituido;
	@XmlAttribute private Long qtdInstancias;
	@XmlAttribute private Date ultimaAlteracao;
	@XmlAttribute private String descUltimaAlteracao;
	@XmlAttribute private Long historicoId;
	
	private ProjetoImplantadoHistoricoVO dadosUsuario = new ProjetoImplantadoHistoricoVO();
	
	
	@Override
    public int compareTo(ProjetoImplantadoVO o) {
		if (o == null || o.getUltimaAlteracao() == null) {
			return 1;
		} else if(ultimaAlteracao == null) {
			return -1;
		}
	    return ultimaAlteracao.compareTo(o.getUltimaAlteracao());
    }
	
	
	public Long getDeployId() {
		return deployId;
	}
	public void setDeployId(Long deployId) {
		this.deployId = deployId;
	}
	public Date getDhPublicacao() {
		return dhPublicacao;
	}
	public void setDhPublicacao(Date dhPublicacao) {
		this.dhPublicacao = dhPublicacao;
	}
	public Boolean getAtivo() {
		return ativo;
	}
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	public Boolean getRemovido() {
		return removido;
	}
	public void setRemovido(Boolean removido) {
		this.removido = removido;
	}
	public Long getProjetoId() {
		return projetoId;
	}
	public void setProjetoId(Long projetoId) {
		this.projetoId = projetoId;
	}
	public String getNomeProjeto() {
		return nomeProjeto;
	}
	public void setNomeProjeto(String nomeProjeto) {
		this.nomeProjeto = nomeProjeto;
	}
	public String getDescProjeto() {
		return descProjeto;
	}
	public void setDescProjeto(String descProjeto) {
		this.descProjeto = descProjeto;
	}
	public Date getDhCriacao() {
		return dhCriacao;
	}
	public void setDhCriacao(Date dhCriacao) {
		this.dhCriacao = dhCriacao;
	}
	public Boolean getPublicado() {
		return publicado;
	}
	
	
	
	public Boolean getSubstituido() {
		return substituido;
	}


	public void setSubstituido(Boolean substituido) {
		this.substituido = substituido;
	}


	public void setPublicado(Boolean publicado) {
		this.publicado = publicado;
	}
	public Long getQtdInstancias() {
		return qtdInstancias;
	}
	public void setQtdInstancias(Long qtdInstancias) {
		this.qtdInstancias = qtdInstancias;
	}
	public Date getUltimaAlteracao() {
		return ultimaAlteracao;
	}
	public void setUltimaAlteracao(Date ultimaAlteracao) {
		this.ultimaAlteracao = ultimaAlteracao;
	}
	public String getDescUltimaAlteracao() {
		return descUltimaAlteracao;
	}
	public void setDescUltimaAlteracao(String descUltimaAlteracao) {
		this.descUltimaAlteracao = descUltimaAlteracao;
	}
	public Long getHistoricoId() {
		return historicoId;
	}
	public void setHistoricoId(Long historicoId) {
		this.historicoId = historicoId;
	}


	public ProjetoImplantadoHistoricoVO getDadosUsuario() {
		return dadosUsuario;
	}


	public void setDadosUsuario(ProjetoImplantadoHistoricoVO dadosUsuario) {
		this.dadosUsuario = dadosUsuario;
	}
	
	
}
