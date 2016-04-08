package br.com.laminarsoft.jazzforms.persistencia.test.ldap;

import java.util.List;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import br.com.laminarsoft.jazzforms.negocio.controller.util.ActiveDirectoryServiceController;
import br.com.laminarsoft.jazzforms.negocio.controller.util.IProjetoController;
import br.com.laminarsoft.jazzforms.negocio.controller.util.LDAPServiceController;
import br.com.laminarsoft.jazzforms.negocio.controller.util.PropertiesServiceController;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LdapGrupoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LdapUsuarioVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.UsuarioVO;

import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPSearchException;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;

public class LdapTest1 {

	private LDAPConnection connection;
	private PropertiesServiceController propertiesServiceController = PropertiesServiceController.getInstance();
	
	@SuppressWarnings("all")
	public void carregaContexto() {
		try {
			String dn = propertiesServiceController.getProperty(IProjetoController.LDAP_ADMIN_USER);
			
			connection = new LDAPConnection(propertiesServiceController.getProperty(IProjetoController.LDAP_SERVER), 
					Integer.parseInt(propertiesServiceController.getProperty(IProjetoController.LDAP_PORT)),
					dn,
					propertiesServiceController.getProperty(IProjetoController.LDAP_ADMIN_SENHA));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (LDAPException e) {
			e.printStackTrace();
		}
	}
	
	@Ignore
	public void teste1() {
		try {
			SearchResult searchResult = connection.search("ou=Pessoal,dc=laminarsoft,dc=com,dc=br", 
					SearchScope.SUB, "(uid=asilva)");
			Assert.assertTrue(searchResult.getEntryCount() > 0);
			SearchResultEntry entry = searchResult.getSearchEntries().get(0);
			String[] objClass = entry.getObjectClassValues();
		} catch (LDAPSearchException e) {
			Assert.assertTrue(false);
			e.printStackTrace();
		}
	}
	
	@Ignore
	public void teste2_retrievingGroups() {
		try {
			SearchResult searchResult = connection.search("ou=Grupos,dc=laminarsoft,dc=com,dc=br", SearchScope.SUB, 
					"(member=uid=asilva,ou=Pessoal,dc=laminarsoft,dc=com,dc=br)");
			Assert.assertTrue(searchResult.getEntryCount() > 0);
			SearchResultEntry entry = searchResult.getSearchEntries().get(0);
			String[] objClass = entry.getObjectClassValues();
		} catch (LDAPSearchException e) {
			Assert.assertTrue(false);
			e.printStackTrace();
		}		
	}
	
	@Ignore
	public void teste3_AutenticandoUsuario() {
		LDAPServiceController controller = LDAPServiceController.getInstance();
		try {
			LdapUsuarioVO ret = controller.bind("t318203", "AliSil@$01");
			Assert.assertNotNull("Não foi possível autenticar o usuario", ret);
		} catch (LDAPException e) {
			e.printStackTrace();
		}
	}
	
	@Ignore
	public void teste4_RecuperaTodosUsuariosAD() {
		ActiveDirectoryServiceController controller = ActiveDirectoryServiceController.getInstance();
		try {
			List<UsuarioVO> usuarios =  controller.getTodosUsuarios();
			Assert.assertTrue("Não foram encontrados usuários", usuarios.size() > 0);
			System.out.println("Quantidade de usuarios: " + usuarios.size());
			for(UsuarioVO usuario : usuarios) {
				System.out.println("CN: " + usuario.cn + "   -   DN: " + usuario.distinguishedName + "  - userAccountControl: " + usuario.ativo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Ignore
	public void teste5_removeUsuariosCorporativosLDAP() {
		LDAPServiceController controller = LDAPServiceController.getInstance();
		try {
			controller.removeTodosUsuariosDoGrupo("UsuariosCorporativos");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void teste6_removeUsuariosCriterio() {
		LDAPServiceController controller = LDAPServiceController.getInstance();
		try {
			controller.removeUsuariosCriterio("(uid=*@*)");
		} catch (LDAPSearchException e) {
			e.printStackTrace();
		} catch (LDAPException e) {
			e.printStackTrace();
		}
	}
}
