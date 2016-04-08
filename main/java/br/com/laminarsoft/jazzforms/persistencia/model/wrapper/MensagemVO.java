package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("all")
@XmlRootElement(name="mensagem")
@XmlAccessorType(XmlAccessType.FIELD)
public class MensagemVO implements Serializable {
	public String conteudo;
	public String titulo;	
	@XmlAttribute public Long id;
	@XmlAttribute public String destinatarioUID;
	@XmlAttribute public String remetenteUID;	
	@XmlAttribute public Date data;
	@XmlAttribute public Long dataLong;
	@XmlAttribute public Long idProjeto;
	@XmlAttribute public Long bpInstanceId;
	@XmlAttribute public String token;
	@XmlAttribute public String loginUsuario;
	

	public List<AnexoVO> anexos = new ArrayList<AnexoVO>();
	public List<Long> equipamentos = new ArrayList<Long>();
	public List<Long> gruposEquipamentos = new ArrayList<Long>();
	public List<String> grupos = new ArrayList<String>();
	public Map<String, ArrayList<String>> uidsGrupos = new HashMap<String, ArrayList<String>>();
	public List<String> destinatariosUids = new ArrayList<String>();
	public DeploymentVO deployment = null;
	public BPInstanceVO bpInstance = null;
	
	public String getConteudo() {
		return conteudo;
	}
	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
		this.dataLong = data.getTime()/1000;
	}
	public String getDestinatarioUID() {
		return destinatarioUID;
	}
	public void setDestinatarioUID(String destinatarioUID) {
		this.destinatarioUID = destinatarioUID;
	}
	public String getRemetenteUID() {
		return remetenteUID;
	}
	public void setRemetenteUID(String remetenteUID) {
		this.remetenteUID = remetenteUID;
	}
	public List<String> getGrupos() {
		return grupos;
	}
	public void setGrupos(List<String> grupos) {
		this.grupos = grupos;
	}
	public List<Long> getEquipamentos() {
		return equipamentos;
	}
	public void setEquipamentos(List<Long> equipamentos) {
		this.equipamentos = equipamentos;
	}
	public List<Long> getGrupoEquipamentos() {
		return gruposEquipamentos;
	}
	public void setGruposEquipamentos(List<Long> gruposEquipamentos) {
		this.gruposEquipamentos = gruposEquipamentos;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<Long> getGruposEquipamentos() {
		return gruposEquipamentos;
	}
	public Long getDataLong() {
		return dataLong;
	}
	public void setDataLong(Long dataLong) {
		this.dataLong = dataLong;
	}
	public List<AnexoVO> getAnexos() {
		return anexos;
	}
	public void setAnexos(List<AnexoVO> anexos) {
		this.anexos = anexos;
	}
	public DeploymentVO getDeployment() {
		return deployment;
	}
	public void setDeployment(DeploymentVO deployment) {
		this.deployment = deployment;
	}
	public BPInstanceVO getBpInstance() {
		return bpInstance;
	}
	public void setBpInstance(BPInstanceVO bpInstance) {
		this.bpInstance = bpInstance;
	}
	
	
}
