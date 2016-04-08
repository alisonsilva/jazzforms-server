package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("all")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="linha")
public class LinhaVO implements Serializable {
	@XmlAttribute private Integer numero;
	private List<CampoVO> campos = new ArrayList<CampoVO>();
	
	public LinhaVO(){}
	public LinhaVO(Integer numero) {
		this.numero = numero;
	}
	
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	public List<CampoVO> getCampos() {
		return campos;
	}
	public void setCampos(List<CampoVO> campos) {
		this.campos = campos;
	}
	
	
}
