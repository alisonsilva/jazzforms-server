package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="alerta")
@SuppressWarnings("all")
public class AlertaVO implements Serializable {
	@XmlAttribute public Long id;
	@XmlAttribute public String loginUsuario;
	@XmlAttribute public String mensagem;
	@XmlAttribute public Date dhEnvio;
	@XmlAttribute public Boolean enviado = false;
}
