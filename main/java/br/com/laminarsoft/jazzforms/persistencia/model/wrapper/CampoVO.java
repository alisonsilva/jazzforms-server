package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("all")
@XmlRootElement(name="campo")
@XmlAccessorType(XmlAccessType.FIELD)
public class CampoVO implements Serializable {
	@XmlAttribute private Long id;
	@XmlAttribute private String nomeCampo;
	@XmlAttribute private String valorCampo;
	
	public CampoVO(){}
	public CampoVO(Long id, String nomeCampo, String valorCampo){
		this.id = id;
		this.nomeCampo = nomeCampo;
		this.valorCampo = valorCampo;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNomeCampo() {
		return nomeCampo;
	}
	public void setNomeCampo(String nomeCampo) {
		this.nomeCampo = nomeCampo;
	}
	public String getValorCampo() {
		return valorCampo;
	}
	public void setValorCampo(String valorCampo) {
		this.valorCampo = valorCampo;
	}
	
	
}
