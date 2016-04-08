package br.com.laminarsoft.jazzforms.persistencia.model.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.laminarsoft.jazzforms.persistencia.model.Projeto;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LdapGrupoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LdapUsuarioVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.SVNProjetoVO;

@SuppressWarnings("all")
@XmlRootElement(name="info_svn")
@XmlAccessorType(XmlAccessType.FIELD)
public class InfoRetornoSVN extends InfoRetornoBase {
	
	public List<SVNProjetoVO> lstProjetos = new ArrayList<SVNProjetoVO>();
	public Projeto projeto;
}
