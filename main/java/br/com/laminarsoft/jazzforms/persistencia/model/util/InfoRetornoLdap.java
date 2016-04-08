package br.com.laminarsoft.jazzforms.persistencia.model.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LdapGrupoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LdapUsuarioVO;

@SuppressWarnings("all")
@XmlRootElement(name="info_ldap")
@XmlAccessorType(XmlAccessType.FIELD)
public class InfoRetornoLdap extends InfoRetornoBase {
	public LdapGrupoVO grupo;
	public List<LdapGrupoVO> grupos = new ArrayList<LdapGrupoVO>();
	public LdapUsuarioVO usuario;
	public Boolean usuarioPodeInserirGrupo;
	public Boolean grupoTemDeployment;
	public List<LdapUsuarioVO> usuarios = new ArrayList<LdapUsuarioVO>();
}
