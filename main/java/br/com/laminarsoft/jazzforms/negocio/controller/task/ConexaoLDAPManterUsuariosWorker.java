package br.com.laminarsoft.jazzforms.negocio.controller.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.unboundid.ldap.sdk.LDAPSearchException;

import br.com.laminarsoft.jazzforms.negocio.controller.util.LDAPServiceController;

@Component("conexaoManterUsuariosLdapWorker")
public class ConexaoLDAPManterUsuariosWorker implements Runnable {
	@Autowired private LDAPServiceController ldapServiceController;
	
	@Override
	public void run() {
		try {
			ldapServiceController.addTodosUsuariosCorporativos();
		} catch (LDAPSearchException e) {
			e.printStackTrace();
		}
	}

}
