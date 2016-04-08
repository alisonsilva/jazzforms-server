package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("all")
@XmlRootElement(name="svn_projeto")
@XmlAccessorType(XmlAccessType.FIELD)
public class SVNProjetoVO implements Serializable, Comparable<SVNProjetoVO> {
	@XmlAttribute private String nome;
	@XmlAttribute private String descricao;
	@XmlAttribute private String versao;
	@XmlAttribute private String dataAtualizacao;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getVersao() {
		return versao;
	}
	public void setVersao(String versao) {
		this.versao = versao;
	}
	public String getDataAtualizacao() {
		return dataAtualizacao;
	}
	public void setDataAtualizacao(String dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}
	@Override
	public int compareTo(SVNProjetoVO o) {		
		return this.nome.compareTo(o.nome);
	}
	
	
}
