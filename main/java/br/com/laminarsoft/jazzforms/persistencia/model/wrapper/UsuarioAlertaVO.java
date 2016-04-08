package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="usuario_alerta")
@SuppressWarnings("all")
public class UsuarioAlertaVO implements Serializable {
	@XmlAttribute public String loginUsuario;
	@XmlAttribute public String idMsgUsuario;
	@XmlAttribute public Date dhCadastro;
	@XmlAttribute public String tipoAparelho;
	@XmlAttribute public String serialAparelho;
}
