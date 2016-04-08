package br.com.laminarsoft.jazzforms.persistencia.model.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.DataviewOpcaoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LdapGrupoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LdapUsuarioVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.sigtap.CidVO;

@SuppressWarnings("all")
@XmlRootElement(name="info_opt_dtv")
@XmlAccessorType(XmlAccessType.FIELD)
public class InfoRetornoOpcoesDTV extends InfoRetornoBase {
	@XmlAttribute public boolean alteraOpcoes = false;
	@XmlAttribute public Long dhUltimaAtualizacao;
	
	public List<DataviewOpcaoVO> opcoes = new ArrayList<DataviewOpcaoVO>();
}
