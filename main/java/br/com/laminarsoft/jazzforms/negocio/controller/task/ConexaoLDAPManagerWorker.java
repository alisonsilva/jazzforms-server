package br.com.laminarsoft.jazzforms.negocio.controller.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.laminarsoft.jazzforms.negocio.controller.util.LDAPServiceController;

@Component("conexaoLdapWorker")
public class ConexaoLDAPManagerWorker implements Runnable {
	@Autowired private LDAPServiceController ldapServiceController;
	
	@Override
	public void run() {
		//ldapServiceController.atualizaLdapConnection();
	}

}
