package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="opcao_dataview")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataviewOpcaoVO implements IDtvOpcao {
	@XmlAttribute public String texto;
	@XmlAttribute public String valor;
	
	@Override
	public void setTexto(String texto) {
		this.texto = texto;
	}

	@Override
	public void setValor(String valor) {
		this.valor = valor;
	}

	@Override
	public String getTexto() {
		return texto;
	}

	@Override
	public String getValor() {
		return valor;
	}

}
