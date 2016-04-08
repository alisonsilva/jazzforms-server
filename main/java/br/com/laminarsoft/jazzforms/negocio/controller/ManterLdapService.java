package br.com.laminarsoft.jazzforms.negocio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.laminarsoft.jazzforms.logging.types.MessageData;
import br.com.laminarsoft.jazzforms.negocio.controller.util.LDAPServiceController;
import br.com.laminarsoft.jazzforms.negocio.controller.util.PropertiesServiceController;
import br.com.laminarsoft.jazzforms.persistencia.dao.AlertaDao;
import br.com.laminarsoft.jazzforms.persistencia.dao.DeploymentDao;
import br.com.laminarsoft.jazzforms.persistencia.dao.GrupoDao;
import br.com.laminarsoft.jazzforms.persistencia.dao.UsuarioDao;
import br.com.laminarsoft.jazzforms.persistencia.model.Grupo;
import br.com.laminarsoft.jazzforms.persistencia.model.Usuario;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.IMensagensErro;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.ParametroException;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LdapGrupoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LdapUsuarioVO;

import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPSearchException;
import com.unboundid.ldif.LDIFException;

@Service(value="ldapService")
@SuppressWarnings("all")
@Transactional(propagation=Propagation.SUPPORTS, 
		isolation=Isolation.READ_UNCOMMITTED, 
		timeout=30)
public class ManterLdapService {
	
	@Autowired private LDAPServiceController ldapServiceController;
	@Autowired private UsuarioDao usuarioDao;
	@Autowired private GrupoDao grupoDao;
	@Autowired private AlertaDao alertaDao;
	@Autowired private DeploymentDao deploymentDao;
	@Autowired private PropertiesServiceController propertiesServiceController;
	
	public LdapGrupoVO findGrupo(String nomeGrupo) throws LDAPSearchException {
		return ldapServiceController.getGrupoFromDatabase(nomeGrupo);
	}
	
	public LdapUsuarioVO autenticaUsuario(String nomeUsuario, String senhaUsuario){
		LdapUsuarioVO usuario = null;
		usuario = ldapServiceController.getUsuarioAutenticado(nomeUsuario, senhaUsuario);
		return usuario;
	}
	
	public LdapUsuarioVO findUsuario(String login) {
		return ldapServiceController.getUsuarioDB(login);
	}	
	
	public List<LdapGrupoVO> findGruposUsuario(String uid) {
		return ldapServiceController.getGruposUsuarioDB(uid);
	}
	
	public List<LdapGrupoVO> findGrupos() {
		return ldapServiceController.getTodosGruposDB();
	}
	
	public List<LdapUsuarioVO> findUsuarios() throws LDAPException {
		return ldapServiceController.getTodosUsuariosDB();
	}
	
	public void adicionaGrupo(LdapGrupoVO grupo) {
		Grupo grp = usuarioDao.adicionaGrupo(grupo);
		ldapServiceController.addGroupCache(grupo.getNome(), grupo.getDescription());
	}
	
	public void adicionaUsuario(LdapUsuarioVO usuario) {
		Usuario usr = usuarioDao.adicionarUsuario(usuario);
		ldapServiceController.addUserCache(usr);
	}
	
	public void adicionaUsuarioPublico(LdapUsuarioVO usuario) throws LDAPException, LDIFException {
		try {
			if(!usuarioDao.isUsuarioAtivoMail(usuario.getMail()) && usuarioDao.isUsuarioExterno(usuario.getMail())) {
				LdapUsuarioVO usr = usuarioDao.getInfoUsuarioPorLogin(usuario.getMail());
				ldapServiceController.removeUsuarioDB(usr.getLogin());
				usuarioDao.removeUsuarioPorMailPermanentemente(usr.getMail());
			}
		} catch (Exception e) {
		}
		usuarioDao.adicionarUsuarioPublico(usuario);
		ldapServiceController.addUserPublicoDB(usuario);
		usuarioDao.enviaMailUsuario(usuario, "Ativação da conta", propertiesServiceController.getProperty("mail.confirmacao.criacao.usuario"));
	}
	
	public LdapUsuarioVO alteraUsuarioPublico(LdapUsuarioVO usuario) throws LDAPException, LDIFException {
		if(!usuarioDao.isUsuarioExterno(usuario.getLogin())) {
			throw new ParametroException(IMensagensErro.USUARIO_CORPORATIVO_MSG, IMensagensErro.USUARIO_CORPORATIVO_CODE);
		}
		usuarioDao.validarEntradaAlteracaoUsuarioPublico(usuario);
		usuarioDao.alteraUsuarioPublico(usuario);
		ldapServiceController.alteraUserPublicoDB(usuario);
		if(!usuario.getMail().equalsIgnoreCase(usuario.getLogin())) {
			usuarioDao.enviaMailUsuario(usuario, "Confirmação de email", propertiesServiceController.getProperty("mail.confirmacao.alteracao.usuario"));
		}
		return usuario;
	}	
	
	public LdapUsuarioVO enviarLembreteSenha(LdapUsuarioVO usuario) {
		usuarioDao.validarLembreteUsuarioPublico(usuario);
		return usuarioDao.enviaLembreteMailUsuario(usuario);
	}
	
	public void adicionaUserToGroup(String uid, String nomeGrupo) throws LDAPException {
		grupoDao.adicionaUsuarioAoGrupo(uid, nomeGrupo);
		ldapServiceController.addUserGroupCache(uid, nomeGrupo);
		if(deploymentDao.getDeploymentIdsByGroupName(nomeGrupo).size() > 0) {
			alertaDao.enviaAlertaUsuario(uid, "Serviço novo", "Há novos serviços disponíveis", MessageData.ATUALIZACAO_APLICACAO);
		}
	}
	
	public void removeUsuarioDoGrupo(String uid, String nomeGrupo) throws LDAPException {
		usuarioDao.removeUserFromGroup(uid, nomeGrupo);
		ldapServiceController.removeUserFromGroupCache(uid, nomeGrupo);
	}	
	
	public void removeUsuario(String uid) throws LDAPException {
		usuarioDao.removeUsuario(uid);
		ldapServiceController.removeUsuarioCache(uid);
	}
	
	public boolean usuarioPodeInserirGrupo(String uid) throws LDAPSearchException {
		return ldapServiceController.usuarioPodeInserirGrupoDB(uid);
	}
	
	public void removeGrupo(String grupo) throws LDAPException {
		usuarioDao.removeGrupo(grupo);
		ldapServiceController.removeGrupoCache(grupo);
	}
	
	public Boolean grupoTemDeployment(String nomeGrupo) {
		return usuarioDao.grupoTemDeployment(nomeGrupo);
	}
}
