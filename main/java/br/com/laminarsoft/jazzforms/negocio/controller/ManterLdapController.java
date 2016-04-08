package br.com.laminarsoft.jazzforms.negocio.controller;

import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.laminarsoft.jazzforms.negocio.controller.security.AuthenticationService;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.ParametroException;
import br.com.laminarsoft.jazzforms.persistencia.model.util.InfoRetornoLdap;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LdapGrupoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LdapUsuarioVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.RequestVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.UsuarioVO;

import com.unboundid.ldap.sdk.LDAPException;

@Controller
@RequestMapping("/servicos/ldapService")
@SuppressWarnings("all")
public class ManterLdapController {

	@Autowired private ResourceBundle bundle;	
	@Autowired private ManterLdapService ldapService;
	@Autowired private AuthenticationService authenticationService;
	
	@RequestMapping(value="/ldap/findGrupo", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoLdap findGrupo(@RequestBody RequestVO request) {
		InfoRetornoLdap info = new InfoRetornoLdap();
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}		
		try{
			String nomeGrupo = request.parametros[0];
			LdapGrupoVO grupo = ldapService.findGrupo(nomeGrupo);
			info.grupo = grupo;
			info.codigo = 0;
			info.mensagem = "Grupo encontrado com sucesso.";
		} catch(ParametroException e) {
			info.codigo = 1;
			info.mensagem = "Erro ao encontrar grupo: " + e.getMessage();
		} catch (AuthenticationException e) {
			info.codigo = 2;
			info.mensagem = "Erro na chamada: " + e.getMessage();
		} catch(AccessDeniedException e) {
			info.codigo = 3;
			info.mensagem = "Acesso negado: privilégios insuficientes";
		} catch (Exception e) {
			info.codigo = -1;
			info.mensagem = "Erro inesperado: " + e.getMessage();
		}
		return info;		
	}
	
	@RequestMapping(value="/ldap/autenticaUsuario", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoLdap autenticaUsuario(@RequestBody UsuarioVO usuarioVo) {
		InfoRetornoLdap info = new InfoRetornoLdap();
		String[] fields = new String[]{usuarioVo.lgn};
		
		try {
			LdapUsuarioVO ldapusr = ldapService.autenticaUsuario(usuarioVo.lgn, usuarioVo.sn);
			info.usuario = ldapusr;
			info.codigo = 0;
			info.mensagem = "Usuario autenticado com sucesso";
			info.token = authenticationService.criaToken(usuarioVo.lgn, usuarioVo.sn);
			if (StringUtils.isEmpty(info.token)) {
				info.codigo = 2;
				info.mensagem = "Não foi possível gerar o token";
			}
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
			info.codigo = 2;
			info.mensagem = "Erro ao realizar autenticacao do usuario";
		}
		
		return info;
	}
	
	@RequestMapping(value="/ldap/recuperaToken", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoLdap recuperaToken(@RequestBody UsuarioVO usuarioVo) {
		InfoRetornoLdap info = new InfoRetornoLdap();
		info.token = authenticationService.criaToken(usuarioVo.lgn, usuarioVo.sn);
		return info;
	}

	@RequestMapping(value="/ldap/validaToken", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoLdap validaToken(@RequestBody RequestVO request) {
		InfoRetornoLdap info = new InfoRetornoLdap();
		try {
			authenticationService.validaToken(request.token);
			UsuarioVO usuario = authenticationService.getUsuarioFromToken(request.token);
			InfoRetornoLdap infoAuth = this.autenticaUsuario(usuario);
			if(infoAuth.codigo > 0) {
				info.codigo = infoAuth.codigo;
				info.mensagem = infoAuth.mensagem;
			} else {
				info.codigo = 0;
				info.mensagem = "Token validado com sucesso";
				info.usuario = infoAuth.usuario;
			}
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
		}		
		return info;
	}
	
	
	@RequestMapping(value="/ldap/findUsuario", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoLdap findUsuario(@RequestBody RequestVO request) {
		InfoRetornoLdap info = new InfoRetornoLdap();
		if (request == null || request.parametros.length == 0) {
			info.codigo = 2;
			info.mensagem = "Request está incompleto. Faltando parâmetro";
			return info;
		}

		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}
		
		String uid = request.parametros[0];
		try{
			LdapUsuarioVO usuario = ldapService.findUsuario(uid);
			info.usuario = usuario;
			info.codigo = 0;
			info.mensagem = "Usuário encontrado com sucesso.";
		} catch (AuthenticationException e) {
			info.codigo = 2;
			info.mensagem = "Erro na chamada: " + e.getMessage();
		} catch(AccessDeniedException e) {
			info.codigo = 3;
			info.mensagem = "Acesso negado: privilégios insuficientes";
		} catch (Exception e) {
			info.codigo = -1;
			info.mensagem = "Erro inesperado: " + e.getMessage();
		}
		return info;		
	}	
	
	@RequestMapping(value="/ldap/usuarioPodeInserirGrupo", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoLdap usuarioPodeInserirGrupo(@RequestBody RequestVO request) {
		InfoRetornoLdap info = new InfoRetornoLdap();
		if (request == null || request.parametros.length == 0) {
			info.codigo = 2;
			info.mensagem = "Request está incompleto. Faltando parâmetro";
			return info;
		}
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}

		
		String uid = request.parametros[0];
		try{
			boolean podeInserirGrupo = ldapService.usuarioPodeInserirGrupo(uid);
			info.codigo = 0;
			info.mensagem = "Resposta encontrada com sucesso.";
			info.usuarioPodeInserirGrupo = podeInserirGrupo;
		} catch(LDAPException e) {
			info.codigo = 1;
			info.mensagem = "Não foi possível responder à pergunta: " + e.getMessage();
		} catch (AuthenticationException e) {
			info.codigo = 2;
			info.mensagem = "Erro na chamada: " + e.getMessage();
		} catch(AccessDeniedException e) {
			info.codigo = 3;
			info.mensagem = "Acesso negado: privilégios insuficientes";
		} catch (Exception e) {
			info.codigo = -1;
			info.mensagem = "Erro inesperado: " + e.getMessage();
		}
		return info;		
	}	
	
	@RequestMapping(value="/ldap/addUserToGroup", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoLdap addUserToGroup(@RequestBody RequestVO request) {
		InfoRetornoLdap info = new InfoRetornoLdap();
		if (request == null || request.parametros.length <= 1) {
			info.codigo = 2;
			info.mensagem = "Request está incompleto. Faltando parâmetro";
			return info;
		}
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}

		String uid = request.parametros[0];
		String nomeGrupo = request.parametros[1];
		try{
			ldapService.adicionaUserToGroup(uid, nomeGrupo);
			info.codigo = 0;
			info.mensagem = "Usuário adicionado ao grupo com sucesso.";
		} catch(LDAPException e) {
			info.codigo = 1;
			info.mensagem = "Não foi possível adicionar o usuário ao grupo: " + e.getMessage();
		} catch (AuthenticationException e) {
			info.codigo = 2;
			info.mensagem = "Erro na chamada: " + e.getMessage();
		} catch(AccessDeniedException e) {
			info.codigo = 3;
			info.mensagem = "Acesso negado: privilégios insuficientes";
		} catch (Exception e) {
			info.codigo = -1;
			info.mensagem = "Erro inesperado: " + e.getMessage();
		}
		return info;		
	}	

	@RequestMapping(value="/ldap/removeUserFromGroup", method=RequestMethod.DELETE)
	@ResponseBody
	public InfoRetornoLdap removeUserFromGroup(@RequestBody RequestVO request) {
		InfoRetornoLdap info = new InfoRetornoLdap();
		if (request == null || request.parametros.length <= 1) {
			info.codigo = 2;
			info.mensagem = "Request está incompleto. Faltando parâmetro";
			return info;
		}
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}

		String uid = request.parametros[0];
		String nomeGrupo = request.parametros[1];
		try{
			ldapService.removeUsuarioDoGrupo(uid, nomeGrupo);
			info.codigo = 0;
			info.mensagem = "Usuário removido do grupo com sucesso.";
		} catch(LDAPException e) {
			info.codigo = 1;
			info.mensagem = "Não foi possível remover usuário do grupo: " + e.getMessage();
		} catch (AuthenticationException e) {
			info.codigo = 2;
			info.mensagem = "Erro na chamada: " + e.getMessage();
		} catch(AccessDeniedException e) {
			info.codigo = 3;
			info.mensagem = "Acesso negado: privilégios insuficientes";
		} catch (Exception e) {
			info.codigo = -1;
			info.mensagem = "Erro inesperado: " + e.getMessage();
		}
		return info;		
	}	
	
	@RequestMapping(value="/ldap/removeUser", method=RequestMethod.DELETE)
	@ResponseBody
	public InfoRetornoLdap removeUser(@RequestBody RequestVO request) {
		InfoRetornoLdap info = new InfoRetornoLdap();
		if (request == null || request.parametros.length == 0) {
			info.codigo = 2;
			info.mensagem = "Request está incompleto. Faltando parâmetro";
			return info;
		}
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}

		String uid = request.parametros[0];
		try{
			ldapService.removeUsuario(uid);
			info.codigo = 0;
			info.mensagem = "Usuário removido com sucesso.";
		} catch(LDAPException e) {
			info.codigo = 1;
			info.mensagem = "Não foi possível remover usuário: " + e.getMessage();
		} catch (AuthenticationException e) {
			info.codigo = 2;
			info.mensagem = "Erro na chamada: " + e.getMessage();
		} catch(AccessDeniedException e) {
			info.codigo = 3;
			info.mensagem = "Acesso negado: privilégios insuficientes";
		} catch (Exception e) {
			info.codigo = -1;
			info.mensagem = "Erro inesperado: " + e.getMessage();
		}
		return info;		
	}	
	
	@RequestMapping(value="/ldap/grupoTemDeployment", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoLdap grupoTemDeployment(@RequestBody RequestVO request) {
		InfoRetornoLdap info = new InfoRetornoLdap();
		if (request == null || request.parametros.length == 0) {
			info.codigo = 2;
			info.mensagem = "Request está incompleto. Faltando parâmetro";
			return info;
		}
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}

		String nomeGrupo = request.parametros[0];
		try{
			boolean grupoTemDeployment = ldapService.grupoTemDeployment(nomeGrupo);
			info.codigo = 0;
			info.mensagem = "Resposta encontrada com sucesso.";
			info.grupoTemDeployment = grupoTemDeployment;
		} catch (AuthenticationException e) {
			info.codigo = 2;
			info.mensagem = "Erro na chamada: " + e.getMessage();
		} catch(AccessDeniedException e) {
			info.codigo = 3;
			info.mensagem = "Acesso negado: privilégios insuficientes";
		} catch(Exception e) {
			info.codigo = 1;
			info.mensagem = "Não foi possível responder à pergunta: " + e.getMessage();
		}
		return info;		
	}
	
	@RequestMapping(value="/ldap/findUsuarios", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoLdap findAllUsers(@RequestBody RequestVO request) {
		InfoRetornoLdap info = new InfoRetornoLdap();
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}
		
		try{
			info.usuarios = ldapService.findUsuarios();
			info.codigo = 0;
			info.mensagem = "Usuários encontrados com sucesso.";
		} catch(LDAPException e) {
			info.codigo = 1;
			info.mensagem = "Não foi possível encontrar usuários: " + e.getMessage();
		} catch (AuthenticationException e) {
			info.codigo = 2;
			info.mensagem = "Erro na chamada: " + e.getMessage();
		} catch(AccessDeniedException e) {
			info.codigo = 3;
			info.mensagem = "Acesso negado: privilégios insuficientes";
		} catch (Exception e) {
			info.codigo = -1;
			info.mensagem = "Erro inesperado: " + e.getMessage();
		}
		return info;		
	}
	
	@RequestMapping(value="/ldap/findGrupos", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoLdap findAllGroups(@RequestBody RequestVO request) {
		InfoRetornoLdap info = new InfoRetornoLdap();
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}
		
		try{
			info.grupos = ldapService.findGrupos();
			info.codigo = 0;
			info.mensagem = "Grupos encontrados com sucesso.";
		} catch (AuthenticationException e) {
			info.codigo = 2;
			info.mensagem = "Erro na chamada: " + e.getMessage();
		} catch(AccessDeniedException e) {
			info.codigo = 3;
			info.mensagem = "Acesso negado: privilégios insuficientes";
		} catch (Exception e) {
			info.codigo = -1;
			info.mensagem = "Erro inesperado: " + e.getMessage();
		}
		return info;		
	}	
	
	
	@RequestMapping(value="/ldap/findGruposUsuario", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoLdap findGruposUsuario(@RequestBody RequestVO request) {
		InfoRetornoLdap info = new InfoRetornoLdap();
		if (request == null || request.parametros.length == 0) {
			info.codigo = 2;
			info.mensagem = "Request está incompleto. Faltando parâmetro";
			return info;
		}
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}
		
		String uid = request.parametros[0];
		try{
			info.grupos = ldapService.findGruposUsuario(uid);
			info.codigo = 0;
			info.mensagem = "Grupos encontrados com sucesso.";
		} catch (AuthenticationException e) {
			info.codigo = 2;
			info.mensagem = "Erro na chamada: " + e.getMessage();
		} catch(AccessDeniedException e) {
			info.codigo = 3;
			info.mensagem = "Acesso negado: privilégios insuficientes";
		} catch (Exception e) {
			info.codigo = -1;
			info.mensagem = "Erro inesperado: " + e.getMessage();
		}
		return info;		
	}		
	
	@RequestMapping(value="/ldap/removeGrupo", method=RequestMethod.DELETE)
	@ResponseBody
	public InfoRetornoLdap removeGrupo(@RequestBody RequestVO request) {
		InfoRetornoLdap info = new InfoRetornoLdap();
		if (request == null || request.parametros.length == 0) {
			info.codigo = 2;
			info.mensagem = "Request está incompleto. Faltando parâmetro";
			return info;
		}
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}
		
		String nomeGrupo = request.parametros[0];
		try{
			ldapService.removeGrupo(nomeGrupo);
			info.codigo = 0;
			info.mensagem = "Grupo removido com sucesso.";
		} catch(LDAPException e) {
			info.codigo = 1;
			info.mensagem = "Não foi possível remover o grupo: " + e.getMessage();
		} catch (AuthenticationException e) {
			info.codigo = 2;
			info.mensagem = "Erro na chamada: " + e.getMessage();
		} catch(AccessDeniedException e) {
			info.codigo = 3;
			info.mensagem = "Acesso negado: privilégios insuficientes";
		} catch (Exception e) {
			info.codigo = -1;
			info.mensagem = "Erro inesperado: " + e.getMessage();
		}
		return info;		
	}	
	
	@RequestMapping(value="/ldap/adicionaGrupo", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoLdap adicionaGrupo(@RequestBody LdapGrupoVO grupo) {
		InfoRetornoLdap info = new InfoRetornoLdap();		
		try {
			authenticationService.validaToken(grupo.getToken());
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}
		
		try {
			ldapService.adicionaGrupo(grupo);
	        info.codigo = 0;
	        info.mensagem = "Grupo adicionado com sucesso";
        } catch (AuthenticationException e) {
			info.codigo = 2;
			info.mensagem = "Erro na chamada: " + e.getMessage();
		} catch(AccessDeniedException e) {
			info.codigo = 3;
			info.mensagem = "Acesso negado: privilégios insuficientes";
		} catch (Exception e) {
			info.codigo = -1;
			info.mensagem = "Erro inesperado: " + e.getMessage();
		}
		return info;
	}	
	
	@RequestMapping(value="/ldap/adicionaUsuario", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoLdap adicionaUsuario(@RequestBody LdapUsuarioVO usuario) {
		InfoRetornoLdap info = new InfoRetornoLdap();		
		try {
			authenticationService.validaToken(usuario.getToken());
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}

		try {
			ldapService.adicionaUsuario(usuario);
	        info.codigo = 0;
	        info.mensagem = "Usuário adicionado com sucesso";
        } catch (AuthenticationException e) {
			info.codigo = 2;
			info.mensagem = "Erro na chamada: " + e.getMessage();
		} catch(AccessDeniedException e) {
			info.codigo = 3;
			info.mensagem = "Acesso negado: privilégios insuficientes";
		} catch (Exception e) {
			info.codigo = -1;
			info.mensagem = "Erro inesperado: " + e.getMessage();
		}
		return info;
	}	
	
}
