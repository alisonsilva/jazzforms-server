package br.com.laminarsoft.jazzforms.persistencia.model.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LandVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LdapUsuarioVO;

@SuppressWarnings("all")
@XmlRootElement(name="info_lands")
@XmlAccessorType(XmlAccessType.FIELD)
public class InfoRetornoCriacaoUsuarioPublico extends InfoRetornoBase {
	
	@XmlAttribute public String urlResposta;
	@XmlAttribute public String email;
	
	public LdapUsuarioVO infoUsuario;
}
