package br.com.laminarsoft.jazzforms.negocio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.laminarsoft.jazzforms.negocio.controller.util.LDAPServiceController;
import br.com.laminarsoft.jazzforms.persistencia.dao.UsuarioDao;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LdapUsuarioVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LocalizacaoVO;

import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldif.LDIFException;


@Service(value="usuarioService")
@SuppressWarnings("all")
@Transactional(propagation=Propagation.SUPPORTS, 
		isolation=Isolation.READ_UNCOMMITTED, 
		timeout=30, 
		rollbackFor=Exception.class)
public class ManterUsuarioService {

	@Autowired private UsuarioDao usuarioDao;
	@Autowired private LDAPServiceController ldapServiceController;
	
	public List<LocalizacaoVO> getLocalizacoesUsuario(String usuario) {
		return usuarioDao.getLocalizacoesUsuario(usuario);
	}
	
	public void ativaUsuario(Long id)  throws LDAPException, LDIFException {
		LdapUsuarioVO usr = usuarioDao.ativaUsuario(id);
//		ldapServiceController.ativaUsuario(usr);
	}
}
