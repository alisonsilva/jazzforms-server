package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("all")
@XmlRootElement(name="grupo_equipamento")
@XmlAccessorType(XmlAccessType.FIELD)
public class GrupoEquipamentoVO implements Serializable, Comparable<GrupoEquipamentoVO> {
	@XmlAttribute public String nome;
	@XmlAttribute public Long id;
	@XmlAttribute public String token;
	public String descricao;
	public List<EquipamentoVO> equipamentos = new ArrayList<EquipamentoVO>();
	
	@Override
    public int compareTo(GrupoEquipamentoVO o) {
	    return nome.compareTo(o.getNome());
    }
	
	@Override
    public boolean equals(Object obj) {
		if(!(obj instanceof GrupoEquipamentoVO)) {
			return false;
		}
		GrupoEquipamentoVO grp = (GrupoEquipamentoVO)obj;
		if(this.getId() == grp.getId()) {
			return true;
		}
		return false;
    }

	public String toString() {
		return nome;
	}

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<EquipamentoVO> getEquipamentos() {
		return equipamentos;
	}

	public void setEquipamentos(List<EquipamentoVO> equipamentos) {
		this.equipamentos = equipamentos;
	}
}
