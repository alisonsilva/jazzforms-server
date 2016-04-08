package br.com.laminarsoft.jazzforms.persistencia.model.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.laminarsoft.jazzforms.persistencia.model.Foto;
import br.com.laminarsoft.jazzforms.persistencia.model.Usuario;
import br.com.laminarsoft.jazzforms.persistencia.model.ValorDataview;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.InstanciaVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.ValorDataviewVO;

@SuppressWarnings("all")
@XmlRootElement(name="info_instancia")
@XmlAccessorType(XmlAccessType.FIELD)
public class InfoRetornoInstancia extends InfoRetornoBase {
	public ValorDataview valorDataview;
	public InstanciaVO instancia;
	public ValorDataviewVO valorDataviewVo;
	public List<Usuario> usuariosReenvioInstancia;
	public List<InstanciaVO> instancias = new ArrayList<InstanciaVO>();
	public List<Foto> fotos;
}
