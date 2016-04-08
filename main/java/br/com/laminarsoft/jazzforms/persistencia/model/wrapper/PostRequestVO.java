package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="post_request")
@XmlAccessorType(XmlAccessType.FIELD)
public class PostRequestVO {
	
	@XmlAttribute public String nomeUsuario = "";
	@XmlAttribute public String senha = "";
	@XmlAttribute public Long timestamp = 0l;
	
	public List<ParametroVO> parametros = new ArrayList<ParametroVO>();
}
