package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@javax.xml.bind.annotation.XmlRootElement(name="instancias")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@SuppressWarnings("all")
public class InstanciasVO implements Serializable {
	@XmlAttribute private String loginUsuarioAlteracao; 
	@XmlAttribute public String token;
	
	@XmlElement(name="instancia")
	private List<InstanciaVO> instancias = new ArrayList<InstanciaVO>();

	public List<InstanciaVO> getInstancias() {
		return instancias;
	}

	public void setInstancias(List<InstanciaVO> instancias) {
		this.instancias = instancias;
	}

	public String getLoginUsuarioAlteracao() {
		return loginUsuarioAlteracao;
	}

	public void setLoginUsuarioAlteracao(String loginUsuarioAlteracao) {
		this.loginUsuarioAlteracao = loginUsuarioAlteracao;
	}	
}
