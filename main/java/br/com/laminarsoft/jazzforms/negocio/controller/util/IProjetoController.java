package br.com.laminarsoft.jazzforms.negocio.controller.util;


public interface IProjetoController {
	public static final String LDAP_SERVER = "ldap.server";
	public static final String LDAP_PORT = "ldap.port";
	public static final String LDAP_ROOT_DN = "ldap.root.dn";
	public static final String LDAP_PESSOAS = "ldap.pessoas.ou";
	public static final String LDAP_GRUPOS = "ldap.grupos.ou";
	public static final String LDAP_GRUPO_USRS_CORPORATIVOS = "ldap.grupos.usuarios.corporativos";
	public static final String LDAP_GRUPO_USRS_COMUNS = "ldap.grupos.usuarios.comuns";
	public static final String LDAP_ADMIN_USER = "ldap.manager.usuario";
	public static final String LDAP_ADMIN_SENHA = "ldap.manager.password";
	
	public static final String LDAP_GRUPOS_PERMITIDOS = "ldap.grupos.jazzclient.users";
	
	public static final String SVN_REPO_URL = "svn.repositorio.url";
	public static final String SVN_REPO_USUARIO = "svn.repositorio.usuario";
	public static final String SVN_REPO_SENHA = "svn.repositorio.senha";
	public static final String SVN_LOCAL_REPO = "svn.repositorio.localcopy";
	public static final String SVN_REPO_DIRETORIO_PRINCIPAL = "svn.repositorio.diretorio_principal";
}
