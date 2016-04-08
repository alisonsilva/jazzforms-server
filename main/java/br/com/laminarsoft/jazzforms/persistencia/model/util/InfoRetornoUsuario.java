package br.com.laminarsoft.jazzforms.persistencia.model.util;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.laminarsoft.jazzforms.persistencia.model.Usuario;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LocalizacaoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.UsuarioVO;

@SuppressWarnings("all")
@XmlRootElement(name="info_usuario")
@XmlAccessorType(XmlAccessType.FIELD)
public class InfoRetornoUsuario extends InfoRetornoBase {
	public Usuario usuario;
	public UsuarioVO infoUsuario;
	
	public List<LocalizacaoVO> localizacoes;
}
