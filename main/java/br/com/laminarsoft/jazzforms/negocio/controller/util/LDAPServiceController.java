package br.com.laminarsoft.jazzforms.negocio.controller.util;

import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import javax.naming.ldap.InitialLdapContext;
import javax.net.ssl.SSLSocketFactory;

import net.sf.ehcache.Ehcache;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Controller;

import br.com.laminarsoft.jazzforms.persistencia.dao.GrupoDao;
import br.com.laminarsoft.jazzforms.persistencia.dao.UsuarioDao;
import br.com.laminarsoft.jazzforms.persistencia.model.Usuario;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.IMensagensErro;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.ParametroException;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LdapGrupoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LdapUsuarioVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.UsuarioVO;

import com.unboundid.ldap.sdk.AddRequest;
import com.unboundid.ldap.sdk.BindResult;
import com.unboundid.ldap.sdk.DeleteRequest;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPSearchException;
import com.unboundid.ldap.sdk.Modification;
import com.unboundid.ldap.sdk.ModificationType;
import com.unboundid.ldap.sdk.ModifyRequest;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;
import com.unboundid.ldif.LDIFException;
import com.unboundid.util.ssl.SSLUtil;
import com.unboundid.util.ssl.TrustAllTrustManager;

@Controller
@SuppressWarnings("all")
public class LDAPServiceController implements Serializable{
	
	private static LDAPServiceController LDAP_CONTROLLER;
	private static final String NOME_CACHE_GRUPOS = "grupos_usuarios";
	private static final String NOME_CACHE_USUARIOS = "usuarios";
	private static String OBJ_SYNC = "Objeto de sincronizacao";
	
	@Autowired private EhCacheCacheManager cacheManager;
	@Autowired private UsuarioDao usuarioDao;
	@Autowired private GrupoDao grupoDao;
	
	private LDAPConnection ldapConnection;
	public String basePessoalDn;
	public String baseGruposDn;
	private String grpUsuariosCorporativosDn;
	private String grpUsuariosComunsDn;
	private String grpUsuariosCorporativosCn;
	private String grpUsuariosComunsCn;
	private String ldapHost;
	private String ldapPort;
	
	@Autowired private PropertiesServiceController propertiesServiceController;

	private AutenticacaoCorporativa autenticacaoCorporativaController = AutenticacaoCorporativaFactory.getMetodoAutenticacaoCorporativa();
	
	public LDAPServiceController() {
		if (propertiesServiceController == null) {
			propertiesServiceController = PropertiesServiceController.getInstance();
		}
		basePessoalDn = propertiesServiceController.getProperty(IProjetoController.LDAP_PESSOAS) + "," +
				propertiesServiceController.getProperty(IProjetoController.LDAP_ROOT_DN);
		baseGruposDn = 	propertiesServiceController.getProperty(IProjetoController.LDAP_GRUPOS) + "," + 
				propertiesServiceController.getProperty(IProjetoController.LDAP_ROOT_DN);
		grpUsuariosComunsDn = "cn=" + propertiesServiceController.getProperty(IProjetoController.LDAP_GRUPO_USRS_COMUNS) + "," + baseGruposDn;
		grpUsuariosCorporativosDn = "cn=" + propertiesServiceController.getProperty(IProjetoController.LDAP_GRUPO_USRS_CORPORATIVOS) + "," + baseGruposDn;
		grpUsuariosComunsCn = propertiesServiceController.getProperty(IProjetoController.LDAP_GRUPO_USRS_COMUNS);
		grpUsuariosCorporativosCn = propertiesServiceController.getProperty(IProjetoController.LDAP_GRUPO_USRS_CORPORATIVOS);
		
		ldapHost = propertiesServiceController.getProperty(IProjetoController.LDAP_SERVER);
		ldapPort = propertiesServiceController.getProperty(IProjetoController.LDAP_PORT);

		
		//atualizaLdapConnection();
	}



	public void atualizaLdapConnection() {
		SSLUtil sslUtil = new SSLUtil(new TrustAllTrustManager());
		try {
			SSLSocketFactory socketFactory = sslUtil.createSSLSocketFactory();
			
			String dn = propertiesServiceController.getProperty(IProjetoController.LDAP_ADMIN_USER);
			
			ldapConnection = new LDAPConnection(propertiesServiceController.getProperty(IProjetoController.LDAP_SERVER), 
					Integer.parseInt(propertiesServiceController.getProperty(IProjetoController.LDAP_PORT)),
					dn,
					propertiesServiceController.getProperty(IProjetoController.LDAP_ADMIN_SENHA));
		} catch (NumberFormatException e) {
		} catch (LDAPException e) {
			e.printStackTrace();
		} catch (GeneralSecurityException e) {			
		}
	}
	

	
	public static LDAPServiceController getInstance() {
		if (LDAP_CONTROLLER == null) {
			LDAP_CONTROLLER = new LDAPServiceController();
		}
		return LDAP_CONTROLLER;
	}
	
	public void addTodosUsuariosCorporativos() throws LDAPSearchException{
		boolean autenticacaoCorporativaHabilitada = propertiesServiceController.getProperty("ldap.activedirectory.ativo").equalsIgnoreCase("true");
		if(autenticacaoCorporativaHabilitada && autenticacaoCorporativaController != null && autenticacaoCorporativaController.autenticacaoCorporativa()){
			List<UsuarioVO> usuariosCorporativos = autenticacaoCorporativaController.getTodosUsuarios();
			for(UsuarioVO usuario : usuariosCorporativos) {
				LdapUsuarioVO usr = new LdapUsuarioVO();
				usr.setCn(usuario.cn);
				usr.setNome(usuario.cn);
				usr.setPasswd(null);
				usr.setUid(usuario.lgn != null ? usuario.lgn.toUpperCase() : usuario.lgn);
				StringTokenizer tok = new StringTokenizer(usuario.lgn, "@");
				if(StringUtils.isEmpty(usr.getUid())) {
					continue;
				}
				usr.setLogin(tok.nextToken().toUpperCase());
				usr.setEmployeeType(usuario.ativo ? "1" : "0");				
				usr.setDn(basePessoalDn);
				//usr.setGroupDn(grpUsuariosCorporativosCn);
				usr.setMail(usuario.email);
				usr.setCanonicalName(usuario.canonicalName);
				usr.setDistinguishedName(usuario.distinguishedName);
				LdapGrupoVO grp = new LdapGrupoVO();
				grp.setCn(grpUsuariosCorporativosCn);
				grp.setNome(grpUsuariosCorporativosCn);
				grp.setDn(grpUsuariosCorporativosDn);
				grp.getUsuarios().add(usr);
				usr.getGrupos().add(grp);
				
				List<LdapGrupoVO> gruposUsuario = grupoDao.getGruposUsuario(usr.getLogin());
				for(LdapGrupoVO grpUsr : gruposUsuario) {
					if (!grpUsr.getNome().equalsIgnoreCase(grpUsuariosCorporativosCn)) {
						usr.getGrupos().add(grpUsr);
					}
				}
				try {
					this.addUserDatabaseOnly(usr);
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
	
	
		// atualiza cache de grupos de acordo com o que está no LDAP
		Cache cache = cacheManager.getCache(NOME_CACHE_GRUPOS);
		Ehcache ehcache = (Ehcache)cache.getNativeCache();
		List valores = ehcache.getKeys();
		
		List<LdapGrupoVO> grupos = grupoDao.getGrupos();
		for (LdapGrupoVO grupo : grupos) {
			//cache.put(grupo.getCn(), grupo);
			cache.put(grupo.getNome(), grupo);
		}
	}
	
	public LdapUsuarioVO bindDBOnly(String login, String senha) {
		LdapUsuarioVO usr = null;
		String dn = "uid=" + login + "," + basePessoalDn;
		boolean usuarioAutenticadoSucesso = false;
		
		boolean autenticacaoCorporativaHabilitada = propertiesServiceController.getProperty("ldap.activedirectory.ativo").equalsIgnoreCase("true");
		boolean autenticacaoCorporativaRealizada = false;
		List<LdapGrupoVO> grupos = grupoDao.getGruposUsuario(login);
		
		for(LdapGrupoVO grp : grupos) { // é um usuário externo. não será autenticado no AD
			if(grp.getCn().equalsIgnoreCase(grpUsuariosComunsCn)) {
				autenticacaoCorporativaHabilitada = false;
				break;
			}
		}		

		if(autenticacaoCorporativaHabilitada && autenticacaoCorporativaController != null && autenticacaoCorporativaController.autenticacaoCorporativa()) {
			InitialLdapContext context = autenticacaoCorporativaController.loginDominio(login, senha);
			if(context != null) {
				autenticacaoCorporativaRealizada = true;				
			} else {
				autenticacaoCorporativaRealizada = false;
			}
		}
		
		if(autenticacaoCorporativaHabilitada && autenticacaoCorporativaRealizada) {
			usuarioAutenticadoSucesso = true;
			UsuarioVO usrCorporativo = autenticacaoCorporativaController.getDadosUsuario(login, senha);
			usr = usuarioDao.getInfoUsuario(login);
			if (!StringUtils.isEmpty(usr.getLogin())) {
//				usr.setUid(usr.getUid());
//				usr.setMail(usrCorporativo.email);
//				usr.setPasswd(usrCorporativo.sn);
//				usr.setAtivo(usrCorporativo.ativo);
//				usuarioDao.adicionarUsuario(usr);
			} else {
				usr.setMatricula(login);
				usr = new LdapUsuarioVO();
				usr.setCn(usrCorporativo.cn);
				usr.setPasswd(senha);
				usr.setLogin(login);
				usr.setUid(usrCorporativo.lgn);
				usr.setEmployeeType(usrCorporativo.ativo ? "1" : "0");				
				usr.setDn(basePessoalDn);
				usr.setGroupDn(grpUsuariosCorporativosDn);
				usr.setMail(usrCorporativo.email);
				usr.setAtivo(true);
				usr.setNome(usrCorporativo.showName);
				LdapGrupoVO grp = new LdapGrupoVO();
				grp.setNome(grpUsuariosCorporativosCn);
				grp.setCn(grpUsuariosCorporativosCn);
				grp.setDn(grpUsuariosCorporativosDn);
				grp.getUsuarios().add(usr);
				usr.getGrupos().add(grp);
				usuarioDao.adicionarUsuario(usr);
				this.addUserGroupCache(usr.getLogin(), grpUsuariosCorporativosCn);				
			}
			if (usr != null) {
				usr.setMatricula(login);
				usr.getGrupos().addAll(grupoDao.getGruposUsuario(usr.getLogin()));
			}
			try {
				usr.setMatricula(login);
				Cache usrcache = cacheManager.getCache(NOME_CACHE_USUARIOS);
				String dnUsuario = "uid="+ usr.getLogin() +"," + basePessoalDn;
				LdapUsuarioVO user = new LdapUsuarioVO();
				user.setDn(dnUsuario);
				user.setNome(usr.getNome());
				user.setUid(usr.getUid());
				user.setLogin(usr.getLogin());
				usrcache.put(user.getUid(), user);
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}		
		if(!autenticacaoCorporativaRealizada) {
			usr = usuarioDao.autenticaUsuarioDB(login, senha);
		}
		return usr;		
	}
	
	public LdapUsuarioVO bind(String login, String senha) throws LDAPException {
		LdapUsuarioVO usr = null;
		String dn = "uid=" + login + "," + basePessoalDn;
		boolean usuarioAutenticadoSucesso = false;
		
		boolean autenticacaoCorporativaHabilitada = propertiesServiceController.getProperty("ldap.activedirectory.ativo").equalsIgnoreCase("true");
		boolean autenticacaoCorporativaRealizada = false;
		List<LdapGrupoVO> grupos = getGruposUsuario(login);
		
		for(LdapGrupoVO grp : grupos) { // é um usuário externo. não será autenticado no AD
			if(grp.getCn().equalsIgnoreCase(grpUsuariosComunsCn)) {
				autenticacaoCorporativaHabilitada = false;
				break;
			}
		}
		
		if(autenticacaoCorporativaHabilitada && autenticacaoCorporativaController != null && autenticacaoCorporativaController.autenticacaoCorporativa()) {
			InitialLdapContext context = autenticacaoCorporativaController.loginDominio(login, senha);
			if(context != null) {
				autenticacaoCorporativaRealizada = true;				
			} else {
				autenticacaoCorporativaRealizada = false;
			}
		}
		
		if(autenticacaoCorporativaHabilitada && autenticacaoCorporativaRealizada) {
			usuarioAutenticadoSucesso = true;
			UsuarioVO usrCorporativo = autenticacaoCorporativaController.getDadosUsuario(login, senha);
			usr = getUsuario(usrCorporativo.lgn);
			usr.setMatricula(login);
			if (usr != null) {
//				try {
//					usr.setLogin(login);
//					usr.setUid(usr.getUid());
//					usr.setMail(usrCorporativo.email);
//					usr.setPasswd(usrCorporativo.sn);
//					usr.setEmployeeType(usrCorporativo.ativo ? "1" : "0" );
//					modifyUsr(usr);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
			} else {
				usr = new LdapUsuarioVO();
				usr.setCn(usrCorporativo.cn);
				usr.setPasswd(senha);
				usr.setLogin(login);
				usr.setUid(usrCorporativo.lgn);
				usr.setEmployeeType(usrCorporativo.ativo ? "1" : "0");				
				usr.setDn(basePessoalDn);
				usr.setGroupDn(grpUsuariosCorporativosDn);
				usr.setMail(usrCorporativo.email);
				usr.setAtivo(true);
				LdapGrupoVO grp = new LdapGrupoVO();
				grp.setCn(grpUsuariosCorporativosCn);
				grp.setDn(grpUsuariosCorporativosDn);
				grp.getUsuarios().add(usr);
				usr.getGrupos().add(grp);
				try {
					this.addUser(usr);
				} catch (LDIFException e) {
					e.printStackTrace();
				}
			}
		} 
		
		if(!autenticacaoCorporativaRealizada) {
			BindResult res = ldapConnection.bind(dn, senha);
			usuarioAutenticadoSucesso = res.getResultCode().equals(ResultCode.SUCCESS);
			if(!usuarioAutenticadoSucesso) {
				usr = null;
			} else {
				usr = getUsuario(login);
				usr.setMatricula(login);
			}
		}
		return usr;
	}
	
	public LdapUsuarioVO getUsuarioDB(String login) {
		LdapUsuarioVO ret = usuarioDao.getInfoUsuario(login);
		ret.getGrupos().addAll(grupoDao.getGruposUsuario(login));
		return ret;
	}
	
	/**
	 * Recupera o usuário cujo login é passado
	 * @param uid Login do usuário
	 * @return Usuário com suas informações
	 * @throws LDAPSearchException
	 */
	public LdapUsuarioVO getUsuario(String uid) throws LDAPSearchException {
		LdapUsuarioVO ret = null;
		SearchResult sresult = ldapConnection.search(basePessoalDn, SearchScope.SUB, "(uid=" + uid + "*)");
		if (sresult.getEntryCount() > 0) {
			ret = new LdapUsuarioVO();
			SearchResultEntry entry = sresult.getSearchEntries().get(0);
			ret.setCn(entry.getAttributeValue("cn"));
			ret.setDn(entry.getDN());
			ret.setUid(entry.getAttributeValue("uid"));
			ret.setLogin(uid);
			ret.setEmployeeType(entry.getAttributeValue("employeeType"));
			ret.setMail(entry.getAttributeValue("mail"));
			ret.setNome(ret.getCn());
			String departmentNumber = entry.getAttributeValue("departmentNumber");
			if(departmentNumber != null && departmentNumber.equals("0")) {
				ret.setAtivo(false);
			} else {
				ret.setAtivo(true);				
			}
			ret.getGrupos().addAll(getGruposUsuario(uid));
		}
		return ret;
	}
	
	public boolean isUsuarioCorporativo(String uid) {
		boolean ret = false;
		try {
			LdapUsuarioVO usr = getUsuario(uid);
			if(usr != null) {
				List<LdapGrupoVO> grupos = usr.getGrupos();
				for(LdapGrupoVO grp : grupos) {
					if(grp.getNome().equalsIgnoreCase(grpUsuariosCorporativosCn)) {
						ret = true;
						break;
					}
				}
			}
		} catch (LDAPSearchException e) {
		}
		return ret;
	}
	
	/**
	 * Recupera o usuário cujo login é passado
	 * @param uid Login do usuário
	 * @return Usuário com suas informações
	 * @throws LDAPSearchException
	 */
	public LdapUsuarioVO getUsuarioWithoutGroups(String uid) throws LDAPSearchException {
		LdapUsuarioVO ret = null;
		SearchResult sresult = ldapConnection.search(basePessoalDn, SearchScope.SUB, "(uid=" + uid + ")");
		if (sresult.getEntryCount() > 0) {
			ret = new LdapUsuarioVO();
			SearchResultEntry entry = sresult.getSearchEntries().get(0);
			ret.setCn(entry.getAttributeValue("cn"));
			ret.setDn(entry.getDN());
			ret.setUid(uid);
			ret.setEmployeeType(entry.getAttributeValue("employeeType"));
			ret.setMail(entry.getAttributeValue("mail"));
			ret.setNome(ret.getCn());
			
		}
		return ret;
	}
	
	public LdapGrupoVO getGrupoFromDatabase(String grpCn) {
		Cache cache = null;
		try {
			cache = cacheManager.getCache(NOME_CACHE_GRUPOS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		LdapGrupoVO ret = null;
		
		if (cache != null) {
			ret = cache.get(grpCn) == null ? null : (LdapGrupoVO) (cache.get(grpCn).get());
		}
		if (ret == null) {
			ret = grupoDao.getGrupo(grpCn);
			if(ret != null) {
				List<LdapUsuarioVO> usuarios = usuarioDao.getUsuariosPorGrupo(ret.getNome());
				ret.getUsuarios().addAll(usuarios);
			}
			if (cache != null) {
				cache.put(ret.getCn(), ret);
			}			
		}
		return ret;
	}
	
	public LdapGrupoVO getGrupo(String grpCn) throws LDAPSearchException {
		Cache cache = null;
		try {
			cache = cacheManager.getCache(NOME_CACHE_GRUPOS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		LdapGrupoVO ret = null;
		
		if (cache != null) {
			ret = cache.get(grpCn) == null ? null : (LdapGrupoVO) (cache.get(grpCn).get());
		}
		if (ret == null) {
			SearchResult sresult = ldapConnection.search(baseGruposDn, SearchScope.SUB, "(cn=" + grpCn + ")");
			if (sresult.getEntryCount() > 0) {
				ret = new LdapGrupoVO();
				SearchResultEntry entry = sresult.getSearchEntries().get(0);
				ret.setCn(grpCn);
				ret.setNome(grpCn);
				ret.setDescription(entry.getAttributeValue("description"));
				ret.setDn(entry.getAttributeValue("dn"));
				String[] usuarios = entry.getAttributeValues("member");
				for (String usrFullName : usuarios) {
					LdapUsuarioVO ldapusr = getUsuario(usrFullName.replace("uid=", "").replace("," + basePessoalDn, ""));
					if (ldapusr != null) {
						ldapusr.setNome(ldapusr.getCn());
						ret.getUsuarios().add(ldapusr);
					}
				}
				if (cache != null) {
					cache.put(ret.getCn(), ret);
				}
			}
		}
		return ret;
	}	
	
	public void removeTodosUsuariosDoGrupo(String grpCn) throws LDAPSearchException, LDAPException {
		SearchResult sresult = ldapConnection.search(baseGruposDn, SearchScope.SUB, "(cn=" + grpCn + ")");
		if (sresult.getEntryCount() > 0) {
			SearchResultEntry entry = sresult.getSearchEntries().get(0);
			String[] usuarios = entry.getAttributeValues("member");
			for(int idx = 0; idx < usuarios.length; idx++) {
				try {
					String userUid = usuarios[idx];
					String grupoDn = "cn=" + entry.getAttributeValue("cn") + "," + this.baseGruposDn;

					Modification mod = new Modification(ModificationType.DELETE, "member", userUid);
					ModifyRequest req = new ModifyRequest(grupoDn, mod);
					ldapConnection.modify(req);

					ldapConnection.delete(userUid);				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void removeUsuariosCriterio(String criterio) throws LDAPSearchException, LDAPException {
		//SearchResult sresult = ldapConnection.search(baseGruposDn, SearchScope.SUB, "(cn=" + grpCn + ")");
		SearchResult sresult = ldapConnection.search(basePessoalDn, SearchScope.SUB, criterio);
		int entryCount = sresult.getEntryCount();
		for(int i = 0; i < entryCount; i++) {
			SearchResultEntry entry = sresult.getSearchEntries().get(i);
			String usrUid = entry.getAttributeValue("uid");
			String usrDn = "uid= " + usrUid + "," + this.basePessoalDn;
			try {
				ldapConnection.delete(usrDn);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
		
	
	public LdapUsuarioVO getUsuarioAutenticado(String login, String credential) {
		if(StringUtils.isEmpty(login) || StringUtils.isEmpty(credential)) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
		}
 		login = login.toUpperCase();
		LdapUsuarioVO usr = bindDBOnly(login, credential);
		if(usr != null) {
			usr.getGrupos().clear();
			usr.getGrupos().addAll(getGruposUsuarioDB(usr.getLogin()));
		} 
		return usr;
	}

	public boolean usuarioPodeInserirGrupoDB(String uid) throws LDAPSearchException {
		boolean ret = false;
		List<LdapGrupoVO> grupos = grupoDao.getGruposUsuario(uid);
		for(LdapGrupoVO grupo : grupos) {
			if (grupo.getNome().equalsIgnoreCase(propertiesServiceController.getProperty("ldap.grupo.jazzforms.admin"))) {
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	
	public boolean usuarioPodeInserirGrupo(String uid) throws LDAPSearchException {
		boolean ret = false;
		List<LdapGrupoVO> grupos = getGruposUsuario(uid);
		for(LdapGrupoVO grupo : grupos) {
			if (grupo.getNome().equalsIgnoreCase(propertiesServiceController.getProperty("ldap.grupo.jazzforms.admin"))) {
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	public List<LdapGrupoVO> getGruposUsuarioDB(String login) {
		return grupoDao.getGruposUsuario(login);
	}
	
	
	/**
	 * Retorna uma lista com os grupos referentes ao uid do usuário 
	 * @param uid Identificação do usuário (login)
	 * @return Lista com os grupos aos quais esse usuário pertence
	 * @throws LDAPSearchException
	 */
	public List<LdapGrupoVO> getGruposUsuario(String uid) throws LDAPSearchException {
		List<LdapGrupoVO> grupos = new ArrayList<LdapGrupoVO>();
		String memberURL = "(member=uid=" + uid +"," + basePessoalDn  + ")";
		SearchResult searchResult = this.search(baseGruposDn, SearchScope.SUB, memberURL);
		for(SearchResultEntry entry : searchResult.getSearchEntries()) {
			LdapGrupoVO grupo = new LdapGrupoVO();
			grupo.setCn(entry.getAttribute("cn").getValue());
			grupo.setDn(entry.getDN());
			grupo.setNome(grupo.getCn());
			grupo.setDescription((entry.getAttribute("description") != null ? entry.getAttribute("description").getValue() : ""));
			grupos.add(grupo);
		}
		return grupos;
	}
	
	public List<LdapUsuarioVO> getTodosUsuariosDB() {
		Cache cache = cacheManager.getCache(NOME_CACHE_USUARIOS);
		List<LdapUsuarioVO> usuarios = new ArrayList<LdapUsuarioVO>();

		Ehcache usrcache = (Ehcache)cache.getNativeCache();
		if (usrcache.getKeys().size() == 0) {
			List<LdapUsuarioVO> usrs = usuarioDao.getTodosUsuarios();
			for(LdapUsuarioVO usr : usrs) {
				LdapUsuarioVO usuario = new LdapUsuarioVO();
				usuario.setCn(usr.getCn());
				usuario.setNome(usr.getNome());
				usuario.setLogin(usr.getLogin());
				usuario.setUid(usr.getUid());
				usuario.setDn(usr.getUid() + "," + basePessoalDn);
				usuarios.add(usuario);
				cache.put(usuario.getUid(), usuario);
			}
		} else {
			List chaves = usrcache.getKeys();
			for(int i = 0; i < chaves.size(); i++) {
				LdapUsuarioVO usrVo = (LdapUsuarioVO)cache.get(chaves.get(i)).get();
				usuarios.add(usrVo);
			}
		}
		
		return usuarios;
	}
	
	public List<LdapUsuarioVO> getTodosUsuarios() throws LDAPException {
		Cache cache = cacheManager.getCache(NOME_CACHE_USUARIOS);
		List<LdapUsuarioVO> usuarios = new ArrayList<LdapUsuarioVO>();

		Ehcache usrcache = (Ehcache)cache.getNativeCache();
		if (usrcache.getKeys().size() == 0) {
			SearchResult searchResult = ldapConnection.search(basePessoalDn, SearchScope.SUB, "(objectClass=person)");
			for(SearchResultEntry entry : searchResult.getSearchEntries()) {
				LdapUsuarioVO usuario = new LdapUsuarioVO();
				usuario.setCn(entry.getAttributeValue("cn"));
				usuario.setNome(entry.getAttributeValue("cn"));
				usuario.setUid(entry.getAttributeValue("uid"));
				usuario.setDn(entry.getAttributeValue("uid") + "," + basePessoalDn);
				usuarios.add(usuario);
				cache.put(usuario.getUid(), usuario);
			}
		} else {
			List chaves = usrcache.getKeys();
			for(int i = 0; i < chaves.size(); i++) {
				LdapUsuarioVO usrVo = (LdapUsuarioVO)cache.get(chaves.get(i)).get();
				usuarios.add(usrVo);
			}
		}
		
		return usuarios;
	}
	
	public List<LdapGrupoVO> getTodosGruposDB() {
		Cache cache = cacheManager.getCache(NOME_CACHE_GRUPOS);
		Ehcache ehcache = (Ehcache)cache.getNativeCache();
		
		List<LdapGrupoVO> grupos = new ArrayList<LdapGrupoVO>();
		
		List chaves = ehcache.getKeys();
		for(int i = 0; i < chaves.size(); i++) {
			Object ch = chaves.get(i);
			LdapGrupoVO grupo = (LdapGrupoVO)(cache.get(ch).get());
			grupos.add(grupo);
		}

		if(grupos.size() == 0) {
			grupos = grupoDao.getGrupos();
			for(LdapGrupoVO grupo : grupos) {
				cache.put(grupo.getNome(), grupo);
			}
		}
		return grupos;
	}
	
	/**
	 * Recupera todos os grupos criados na OU Grupos juntamente com os usuários cadastrados em cada um
	 * @return Lista com os grupos recuperados na OU Grupos
	 * @throws LDAPSearchException
	 */
	public List<LdapGrupoVO> getTodosGrupos() throws LDAPSearchException {
		Cache cache = cacheManager.getCache(NOME_CACHE_GRUPOS);
		Ehcache ehcache = (Ehcache)cache.getNativeCache();
		
		List<LdapGrupoVO> grupos = new ArrayList<LdapGrupoVO>();
		
		List chaves = ehcache.getKeys();
		for(int i = 0; i < chaves.size(); i++) {
			Object ch = chaves.get(i);
			LdapGrupoVO grupo = (LdapGrupoVO)(cache.get(ch).get());
			grupos.add(grupo);
		}
		
		if(grupos.size() == 0) {
			grupos = this.getGruposFromLDAP();
			for(LdapGrupoVO grupo : grupos) {
				cache.put(grupo.getCn(), grupo);
			}
		}
		
		return grupos;
	}



	private List<LdapGrupoVO> getGruposFromLDAP() throws LDAPSearchException {
		Cache usrCache = cacheManager.getCache(NOME_CACHE_USUARIOS);
		List<LdapGrupoVO> grupos = new ArrayList<LdapGrupoVO>();
		SearchResult searchResult = ldapConnection.search(baseGruposDn, SearchScope.SUB, "(objectClass=groupOfNames)");
		for(SearchResultEntry entry : searchResult.getSearchEntries()) {
			LdapGrupoVO grupo = new LdapGrupoVO();
			grupo.setNome(entry.getAttributeValue("cn"));
			grupo.setCn(entry.getAttributeValue("cn"));
			grupo.setDn(entry.getDN());
			grupo.setDescription((entry.getAttribute("description") != null ? entry.getAttribute("description").getValue() : ""));
			if (grupo.getNome() == null || grupo.getNome().length() == 0) continue;
			String[] dnUsuarios = entry.getAttributeValues("member");
			for(int i = 0; dnUsuarios != null && i < dnUsuarios.length; i++) {
				String dnUsuario = dnUsuarios[i].trim();
				if (dnUsuario.isEmpty()) {
					continue;
				}
				StringTokenizer tok = new StringTokenizer(dnUsuario, ",");
				String cnUsuario = tok.nextToken();
				try {
					Entry usrentry = ldapConnection.getEntry(dnUsuario, "cn", "uid");

					//SearchResult searchResultPessoa = search(basePessoalDn, SearchScope.SUB, "(" + cnUsuario + ")");
					if(usrentry != null) {
						//SearchResultEntry pessoa = searchResultPessoa.getSearchEntries().get(0);
						LdapUsuarioVO user = new LdapUsuarioVO();
						user.setDn(dnUsuario);
						user.setNome(usrentry.getAttributeValue("cn"));
						user.setCn(usrentry.getAttributeValue("cn"));
						user.setUid(usrentry.getAttributeValue("uid"));
						usrCache.put(user.getUid(), user);
						grupo.getUsuarios().add(user);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			grupos.add(grupo);
		}
		return grupos;
	}
	
	public void addGroupCache(String groupName, String groupDescription) {
		LdapGrupoVO grupo = new LdapGrupoVO();
		grupo.setNome(groupName);
		grupo.setDescription(groupDescription);
		Cache cache = cacheManager.getCache(NOME_CACHE_GRUPOS);
		cache.put(groupName, grupo);		
	}
	
	public void addGroup(String groupName, String groupDescription) throws LDAPException, LDIFException{
		String[] ldifLines = {"dn: cn="+ groupName +"," + baseGruposDn, "changetype: add", "cn:" + groupName, 
				"description:" + groupDescription, "objectClass: groupOfNames", "objectClass: top", "member:", "ou: Grupos"};
		ldapConnection.add(new AddRequest(ldifLines));
		
		Cache cache = cacheManager.getCache(NOME_CACHE_GRUPOS);
		cache.put(groupName, this.getGrupo(groupName));
	}
	
	private void modifyUsr(LdapUsuarioVO usuario) throws LDAPException, LDIFException  {
		
		Modification mod1 = new Modification(ModificationType.REPLACE, "userPassword", new String(Base64.encodeBase64(usuario.getPasswd().getBytes())));
		ModifyRequest req = new ModifyRequest( "uid=" + usuario.getUid() + "," + basePessoalDn, mod1);
		try {
			ldapConnection.modify(req);
		} catch (Exception e) {
			throw new ParametroException(IMensagensErro.MUDAR_SENHA_MSG, IMensagensErro.MUDAR_SENHA_CODE);
		}

		Modification mod2 = new Modification(ModificationType.REPLACE, "employeeType", usuario.getEmployeeType());
		Modification mod3 = new Modification(ModificationType.REPLACE, "mail", usuario.getMail());
		Modification mod4 = new Modification(ModificationType.REPLACE, "departmentNumber", usuario.isAtivo() ? "1" : "0");
		req = new ModifyRequest( "uid=" + usuario.getUid() + "," + basePessoalDn, mod2, mod3, mod4);
		ldapConnection.modify(req);
	}
	
	public void ativaUsuario(LdapUsuarioVO usuario) throws LDAPException, LDIFException  {
		Modification mod1 = new Modification(ModificationType.REPLACE, "departmentNumber", "1");
		ModifyRequest req = new ModifyRequest( "uid=" + usuario.getLogin() + "," + basePessoalDn, mod1);
		ldapConnection.modify(req);
	}
	
	public void addUser(LdapUsuarioVO vo)  throws LDAPException, LDIFException {
		addUser(vo.getUid(), vo.getPasswd(), vo.getCn(), vo.getGroupDn(), vo.getEmployeeType(), vo.getMail(), vo.isAtivo() ? "1" : "0");
	}
	
	private void addOrModifyUser(LdapUsuarioVO vo) throws LDAPException, LDIFException {
		String login = vo.getUid();
		String usrDn = "uid=" + login + "," + basePessoalDn;
		
		try {
			Entry entry = ldapConnection.getEntry(usrDn, "1.1");
			if (entry == null) {
				addUser(vo.getUid(), vo.getPasswd(), vo.getCn(), vo.getGroupDn(), vo.getEmployeeType(), vo.getMail(), (vo.isAtivo() ? "1" : "0"));
			}
		} catch (LDAPException e) {
			e.printStackTrace();
		}
	}
	
	public void addUserDatabaseOnly(LdapUsuarioVO usuario) {
		try {
			usuarioDao.adicionarUsuario(usuario);
			String userLogin = usuario.getLogin();

			Cache usrcache = cacheManager.getCache(NOME_CACHE_USUARIOS);
			String dnUsuario = "uid="+ userLogin +"," + basePessoalDn;

			//SearchResultEntry pessoa = searchResultPessoa.getSearchEntries().get(0);
			LdapUsuarioVO user = new LdapUsuarioVO();
			user.setDn(dnUsuario);
			user.setNome(usuario.getNome());
			user.setUid(usuario.getUid());
			usrcache.put(user.getUid(), user);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public void addUserCache(Usuario usr) {
		try {
			Cache usrcache = cacheManager.getCache(NOME_CACHE_USUARIOS);
			String dnUsuario = "uid="+ usr.getLogin() +"," + basePessoalDn;

			LdapUsuarioVO user = new LdapUsuarioVO();
			user.setDn(dnUsuario);
			user.setNome(usr.getNome());
			user.setUid(usr.getUid());
			usrcache.put(user.getUid(), user);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	public void addUsuarioToGrupoCache(LdapUsuarioVO usr, LdapGrupoVO grupo) {
		try {
			Cache cache = cacheManager.getCache(NOME_CACHE_GRUPOS);
			LdapGrupoVO grpVo = (LdapGrupoVO) (cache.get(grupo.getCn()) != null ? cache.get(grupo.getCn()).get() : null);
			if (grpVo == null) {
				grpVo = grupoDao.getGrupo(grupo.getCn());
				if(grpVo != null) {
					cache.put(grpVo.getCn(), grpVo);
				}
				grpVo.getUsuarios().add(usr);
			} else {
				grpVo.getUsuarios().add(usr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	
	public void addUser(String userLogin, String password, String userName, String nomeGrupoDn, String employeeType, String email, String usuarioAtivo) throws LDAPException, LDIFException {
		StringTokenizer tok = new StringTokenizer(userName, " ");
		String sobrenome = "NA";
		while (tok.hasMoreTokens()) {
			sobrenome = tok.nextToken();
		}
		
		String[] ldifLines = {"dn: uid="+ userLogin +"," + basePessoalDn, "changetype: add", "cn:" + userName, "sn: " + sobrenome,  "employeeType: " + employeeType,  
				"objectClass: inetOrgPerson", "objectClass: organizationalPerson", "objectClass: person", "mail: " + email, "departmentNumber: " + usuarioAtivo,
				"objectClass: simpleSecurityObject", "ou: " + nomeGrupoDn, "uid: " + userLogin};
		List<String> ldifLst = Arrays.asList(ldifLines);
		List<String> lstMod = new ArrayList<String>();
		lstMod.addAll(ldifLst);

		if(!StringUtils.isEmpty(password)) {
			String alteraPassword = "userPassword:: " + new String(Base64.encodeBase64(password.getBytes()));
			lstMod.add(alteraPassword);
		} else {
			String alteraPassword = "userPassword:: " + new String(Base64.encodeBase64("123455".getBytes()));
			lstMod.add(alteraPassword);
		}
		
		String[] ldifLinesFinal = lstMod.toArray(new String[ldifLst.size()]);
		
		ldapConnection.add(new AddRequest(ldifLinesFinal));
		addUserToGroup(userLogin, nomeGrupoDn);
		
		try {
			Cache usrcache = cacheManager.getCache(NOME_CACHE_USUARIOS);
			String dnUsuario = "uid="+ userLogin +"," + basePessoalDn;
			Entry usrentry = ldapConnection.getEntry(dnUsuario, "cn", "uid");

			//SearchResult searchResultPessoa = search(basePessoalDn, SearchScope.SUB, "(" + cnUsuario + ")");
			if(usrentry != null) {
				//SearchResultEntry pessoa = searchResultPessoa.getSearchEntries().get(0);
				LdapUsuarioVO user = new LdapUsuarioVO();
				user.setDn(dnUsuario);
				user.setNome(usrentry.getAttributeValue("cn"));
				user.setUid(usrentry.getAttributeValue("uid"));
				usrcache.put(user.getUid(), user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

	public void addUserPublicoDB(LdapUsuarioVO usuario) throws LDAPException, LDIFException  {
		
		try {
			LdapUsuarioVO usrNovo = usuarioDao.getInfoUsuario(usuario.getLogin());
			Cache cache = cacheManager.getCache(NOME_CACHE_USUARIOS);
			cache.evict(usrNovo.getMail());
			cache.put(usrNovo.getLogin(), usrNovo);
			
			LdapGrupoVO grupoVo = new LdapGrupoVO();
			grupoVo.setNome("UsuariosComuns");
			grupoVo.setCn("UsuariosComuns");
//			usuarioDao.addUserToGroup(usrNovo, grupoVo);
			
			this.addUsuarioToGrupoCache(usrNovo, grupoVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	public void alteraUserPublicoDB(LdapUsuarioVO usuario) throws LDAPException, LDIFException  {
		
		try {
			LdapUsuarioVO usrNovo = usuarioDao.getInfoUsuario(usuario.getLogin());
			Cache cache = cacheManager.getCache(NOME_CACHE_USUARIOS);
			cache.evict(usrNovo.getMail());
			cache.put(usrNovo.getLogin(), usrNovo);
			
			LdapGrupoVO grupoVo = new LdapGrupoVO();
			grupoVo.setNome("UsuariosComuns");
			grupoVo.setCn("UsuariosComuns");
			
			List<LdapGrupoVO> grupos = grupoDao.getGruposUsuario(usrNovo.getLogin());
			Cache cacheGrp = cacheManager.getCache(NOME_CACHE_GRUPOS);
			for(LdapGrupoVO grp : grupos) {
				LdapGrupoVO grpVoCached = (LdapGrupoVO) (cache.get(grp.getCn()) != null ? cache.get(grp.getCn()).get() : null);
				if(grpVoCached != null) {
					int idx = 0;
					boolean encontrado = false;
					for(int i = 0; i < grpVoCached.getUsuarios().size(); i++) {
						LdapUsuarioVO usrCached = grpVoCached.getUsuarios().get(i);
						if(usrCached.getLogin().equalsIgnoreCase(usrNovo.getMail())) {
							idx = i;
							encontrado = true;
							break;
						}
					}
					if (encontrado) {
						grpVoCached.getUsuarios().remove(idx);
						grpVoCached.getUsuarios().add(usuario);
					}
				}				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void addUserPublico(LdapUsuarioVO usuario) throws LDAPException, LDIFException  {
		LdapUsuarioVO usrExt = this.getUsuario(usuario.getMail());
		usuario.setAtivo(false);
		usuario.setUid(usuario.getMail());
		if(usrExt != null) {
			removeUsuario(usuario.getMail());
		}
		
		StringTokenizer tok = new StringTokenizer(usuario.getNome(), " ");
		String sobrenome = "NA";
		while (tok.hasMoreTokens()) {
			sobrenome = tok.nextToken();
		}
		String nomeGrupo = "UsuariosComuns";
		String nomeGrupoDn = "cn=" + nomeGrupo + "," + baseGruposDn;
		
		String[] ldifLines = {"dn: uid="+ usuario.getMail() +"," + basePessoalDn, "changetype: add", "cn:" + usuario.getNome(), "sn: " + sobrenome,    
				"objectClass: inetOrgPerson", "objectClass: organizationalPerson", "objectClass: person", "mail: " + usuario.getMail(),
				"objectClass: simpleSecurityObject", "ou: " + nomeGrupoDn, "uid: " + usuario.getMail(), "telephoneNumber: " + usuario.getTelefone(), "departmentNumber: " + 0};
		List<String> ldifLst = Arrays.asList(ldifLines);
		List<String> lstMod = new ArrayList<String>();
		lstMod.addAll(ldifLst);

		
		
		if(!StringUtils.isEmpty(usuario.getPasswd())) {
			String alteraPassword = "userPassword:: " + new String(org.apache.commons.codec.binary.Base64.encodeBase64(usuario.getPasswd().getBytes()));
			lstMod.add(alteraPassword);
		} else {
			String alteraPassword = "userPassword:: " + new String(Base64.encodeBase64("123455".getBytes()));
			lstMod.add(alteraPassword);
		}
		
		String[] ldifLinesFinal = lstMod.toArray(new String[ldifLst.size()]);
		
		ldapConnection.add(new AddRequest(ldifLinesFinal));
		addUserToGroup(usuario.getMail(), nomeGrupoDn);
		
		try {
			LdapUsuarioVO usrNovo = this.getUsuario(usuario.getMail());
			Cache cache = cacheManager.getCache(NOME_CACHE_USUARIOS);
			cache.evict(usrNovo.getUid());
			cache.put(usrNovo.getUid(), usrNovo);
			
			if (usrExt != null) {
				for (LdapGrupoVO grpVo : usrExt.getGrupos()) {
					if(!grpVo.getCn().equalsIgnoreCase(nomeGrupo)) {
						addUserToGroup(usrNovo.getLogin(), grpVo.getCn());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void alteraUserPublico(LdapUsuarioVO usuario) throws LDAPException, LDIFException  {
		LdapUsuarioVO usrAtual = this.getUsuario(usuario.getLogin());
		LdapUsuarioVO usrExt = this.getUsuario(usuario.getMail());
		usuario.setAtivo(usuario.getMail().equalsIgnoreCase(usuario.getLogin()));
		usuario.setUid(usuario.getMail());
	
		if(usrExt != null && !usuario.getMail().equalsIgnoreCase(usuario.getLogin())) {
			throw new ParametroException(IMensagensErro.USUARIO_EXISTENTE_MSG, IMensagensErro.USUARIO_EXISTENTE_CODE);
		}
		
		if(usrExt != null) {
			usuario.setGrupos(usrExt.getGrupos());
		} else {
			usuario.setGrupos(usrAtual.getGrupos());
		}

		removeUsuario(usuario.getLogin());
		
		StringTokenizer tok = new StringTokenizer(usuario.getNome(), " ");
		String sobrenome = "NA";
		while (tok.hasMoreTokens()) {
			sobrenome = tok.nextToken();
		}
		String nomeGrupo = "UsuariosComuns";
		String nomeGrupoDn = "cn=" + nomeGrupo + "," + baseGruposDn;
		
		String[] ldifLines = {"dn: uid="+ usuario.getMail() +"," + basePessoalDn, "changetype: add", "cn:" + usuario.getNome(), "sn: " + sobrenome,    
				"objectClass: inetOrgPerson", "objectClass: organizationalPerson", "objectClass: person", "mail: " + usuario.getMail(),
				"objectClass: simpleSecurityObject", "ou: " + nomeGrupoDn, "uid: " + usuario.getMail(), "telephoneNumber: " + usuario.getTelefone(), "departmentNumber: " + (usuario.isAtivo() ? 1 : 0)};
		List<String> ldifLst = Arrays.asList(ldifLines);
		List<String> lstMod = new ArrayList<String>();
		lstMod.addAll(ldifLst);

		
		
		if(!StringUtils.isEmpty(usuario.getPasswd())) {
			String alteraPassword = "userPassword:: " + new String(Base64.encodeBase64(usuario.getPasswd().getBytes()));
			lstMod.add(alteraPassword);
		} else {
			String alteraPassword = "userPassword:: " + new String(Base64.encodeBase64("123455".getBytes()));
			lstMod.add(alteraPassword);
		}
		
		String[] ldifLinesFinal = lstMod.toArray(new String[ldifLst.size()]);
		
		ldapConnection.add(new AddRequest(ldifLinesFinal));
		
		for(LdapGrupoVO grp : usuario.getGrupos()) {
			addUserToGroup(usuario.getMail(), grpUsuariosComunsDn);
		}
		
		try {
			Cache usrcache = cacheManager.getCache(NOME_CACHE_USUARIOS);
			String dnUsuario = "uid="+ usuario.getMail() +"," + basePessoalDn;
			Entry usrentry = ldapConnection.getEntry(dnUsuario, "cn", "uid");

			usrcache.evict(usuario.getLogin());
			
			//SearchResult searchResultPessoa = search(basePessoalDn, SearchScope.SUB, "(" + cnUsuario + ")");
			if(usrentry != null) {
				//SearchResultEntry pessoa = searchResultPessoa.getSearchEntries().get(0);
				LdapUsuarioVO user = new LdapUsuarioVO();
				user.setDn(dnUsuario);
				user.setNome(usrentry.getAttributeValue("cn"));
				user.setUid(usrentry.getAttributeValue("uid"));
				usrcache.put(user.getUid(), user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}	
	
	public void addUserGroupCache(String userLogin, String groupName) {
		if(StringUtils.isEmpty(groupName)) {
			throw new ParametroException("Group name deve ser indicado", 1001);
		}
		
		Cache cache = cacheManager.getCache(NOME_CACHE_GRUPOS);
		LdapGrupoVO grpVo = (LdapGrupoVO) (cache.get(groupName) != null ? cache.get(groupName).get() : null);
		if (grpVo == null) {
			grpVo = grupoDao.getGrupo(groupName);			
			if(grpVo != null) {
				List<LdapUsuarioVO> usuariosGrupo = usuarioDao.getUsuariosPorGrupo(groupName);
				cache.put(grpVo.getCn(), grpVo);
				LdapUsuarioVO usuario = usuarioDao.getInfoUsuario(userLogin);
				if (usuario != null) {
					usuarioDao.addUserToGroup(usuario, grpVo);
				}
			}
		} else {
			LdapUsuarioVO usuario = usuarioDao.getInfoUsuario(userLogin);
			if (usuario != null) {
				usuarioDao.addUserToGroup(usuario, grpVo);
			}
		}
	}
	
	public void addUserToGroup(String userLogin, String groupName) throws LDAPException {
		if(StringUtils.isEmpty(groupName)) {
			throw new ParametroException("Group name deve ser indicado", 1001);
		}
		Modification mod = new Modification(ModificationType.ADD, "member", "uid="+ userLogin +"," + basePessoalDn);
		ModifyRequest req = new ModifyRequest( (groupName.contains("cn=") ? groupName : "cn=" + groupName + "," + baseGruposDn), mod);
		ldapConnection.modify(req);
		
		Cache cache = cacheManager.getCache(NOME_CACHE_GRUPOS);
		LdapGrupoVO grpVo = (LdapGrupoVO) (cache.get(groupName) != null ? cache.get(groupName).get() : null);
		if (grpVo == null) {
			grpVo = this.getGrupo(groupName);
			if(grpVo != null) {
				cache.put(grpVo.getCn(), grpVo);
			}
		} else {
			LdapUsuarioVO usuario = this.getUsuario(userLogin);
			grpVo.getUsuarios().add(usuario);
		}
	}
	
	public void removeUserFromGroup(String userLogin, String groupName) throws LDAPException {
		Modification mod = new Modification(ModificationType.DELETE, "member", "uid=" + userLogin + "," + basePessoalDn);
		ModifyRequest req = new ModifyRequest("cn=" + groupName + "," + baseGruposDn, mod);
		ldapConnection.modify(req);
		
		Cache cache = cacheManager.getCache(NOME_CACHE_GRUPOS);
		LdapGrupoVO grupo = (LdapGrupoVO)cache.get(groupName).get();
		int idxUsr = -1;
		for(int i =0; i < grupo.getUsuarios().size(); i++) {
			if(grupo.getUsuarios().get(i).getUid().equalsIgnoreCase(userLogin)) {
				idxUsr = i;
				break;
			}
		}
		if(idxUsr >= 0) {
			grupo.getUsuarios().remove(idxUsr);
		}
	}
	

	public void removeUserFromGroupCache(String userLogin, String groupName) throws LDAPException {
		Cache cache = cacheManager.getCache(NOME_CACHE_GRUPOS);
		LdapGrupoVO grupo = (LdapGrupoVO)cache.get(groupName).get();
		int idxUsr = -1;
		for(int i =0; i < grupo.getUsuarios().size(); i++) {
			if(grupo.getUsuarios().get(i).getLogin().equalsIgnoreCase(userLogin)) {
				idxUsr = i;
				break;
			}
		}
		if(idxUsr >= 0) {
			grupo.getUsuarios().remove(idxUsr);
		}
	}
	
	
	public void removeGrupoCache(String groupName) throws LDAPException {
		Cache cache = cacheManager.getCache(NOME_CACHE_GRUPOS);
		cache.evict(groupName);
	}	
	
	public void removeGrupo(String groupName) throws LDAPException {
		DeleteRequest delRequest = new DeleteRequest("cn="+ groupName +"," + baseGruposDn);
		ldapConnection.delete(delRequest);
		
		Cache cache = cacheManager.getCache(NOME_CACHE_GRUPOS);
		cache.evict(groupName);
	}
	
	
	public void removeUsuarioDB(String userLogin) {
		Cache cacheGrp = null; 
		try {
			cacheGrp = cacheManager.getCache(NOME_CACHE_GRUPOS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<LdapGrupoVO> gruposUsr = grupoDao.getGruposUsuario(userLogin);
		if (cacheGrp != null) {
			for (LdapGrupoVO grpUsr : gruposUsr) {
				String grpNome = grpUsr.getCn();
				LdapGrupoVO grpLdap = (LdapGrupoVO) cacheGrp.get(grpNome).get();
				int idxUsr = -1;
				for (int i = 0; i < grpLdap.getUsuarios().size(); i++) {
					if (userLogin.equalsIgnoreCase(grpLdap.getUsuarios().get(i).getUid())) {
						idxUsr = i;
						break;
					}
				}
				if (idxUsr >= 0) {
					grpLdap.getUsuarios().remove(idxUsr);
				}
			}
		}
		for(LdapGrupoVO grupo : gruposUsr) {
			grupoDao.removeUsuarioDoGrupo(userLogin, grupo.getNome());
		}
		
		Cache cacheUsr = null;
		try {
			cacheUsr = cacheManager.getCache(NOME_CACHE_USUARIOS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (cacheUsr != null) {
			cacheUsr.evict(userLogin);
		}		
	}

	public void removeUsuarioCache(String userLogin) throws LDAPException {
		Cache cacheGrp = null; 
		try {
			cacheGrp = cacheManager.getCache(NOME_CACHE_GRUPOS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<LdapGrupoVO> gruposUsr = grupoDao.getGruposUsuario(userLogin);
		if (cacheGrp != null) {
			for (LdapGrupoVO grpUsr : gruposUsr) {
				String grpNome = grpUsr.getCn();
				LdapGrupoVO grpLdap = (LdapGrupoVO) cacheGrp.get(grpNome).get();
				int idxUsr = -1;
				for (int i = 0; i < grpLdap.getUsuarios().size(); i++) {
					if (userLogin.equalsIgnoreCase(grpLdap.getUsuarios().get(i)
							.getUid())) {
						idxUsr = i;
						break;
					}
				}
				if (idxUsr >= 0) {
					grpLdap.getUsuarios().remove(idxUsr);
				}
			}
		}
		Cache cacheUsr = null;
		try {
			cacheUsr = cacheManager.getCache(NOME_CACHE_USUARIOS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (cacheUsr != null) {
			cacheUsr.evict(userLogin);
		}		
	}
	
	
	public void removeUsuario(String userLogin) throws LDAPException {
		Cache cacheGrp = null; 
		try {
			cacheGrp = cacheManager.getCache(NOME_CACHE_GRUPOS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<LdapGrupoVO> gruposUsr = this.getGruposUsuario(userLogin);
		if (cacheGrp != null) {
			for (LdapGrupoVO grpUsr : gruposUsr) {
				String grpNome = grpUsr.getCn();
				LdapGrupoVO grpLdap = (LdapGrupoVO) cacheGrp.get(grpNome).get();
				int idxUsr = -1;
				for (int i = 0; i < grpLdap.getUsuarios().size(); i++) {
					if (userLogin.equalsIgnoreCase(grpLdap.getUsuarios().get(i)
							.getUid())) {
						idxUsr = i;
						break;
					}
				}
				if (idxUsr >= 0) {
					grpLdap.getUsuarios().remove(idxUsr);
				}
			}
		}
		for(LdapGrupoVO grupo : gruposUsr) {
			removeUserFromGroup(userLogin, grupo.getNome());
		}
		DeleteRequest delRequest = new DeleteRequest("uid="+ userLogin +"," + basePessoalDn);
		try {
			ldapConnection.delete(delRequest);
		} catch (Exception e1) {			
		}
		
		Cache cacheUsr = null;
		try {
			cacheUsr = cacheManager.getCache(NOME_CACHE_USUARIOS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (cacheUsr != null) {
			cacheUsr.evict(userLogin);
		}		
	}
	
	public SearchResult search(String baseDn, SearchScope scope, String criteria) throws LDAPSearchException {
		return ldapConnection.search(baseDn, scope, criteria);
	}
}
