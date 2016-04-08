package br.com.laminarsoft.jazzforms.negocio.controller.security;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.directory.InitialDirContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import br.com.laminarsoft.jazzforms.negocio.controller.util.LDAPServiceController;
import br.com.laminarsoft.jazzforms.persistencia.dao.UsuarioDao;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LdapGrupoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LdapUsuarioVO;

import com.unboundid.ldap.sdk.LDAPException;

@SuppressWarnings("all")
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
	@Autowired private LDAPServiceController ldapServiceController;
	@Autowired private UsuarioDao usuarioDao;
	
	private static String ROLE_PREFIX = "ROLE_"; 
	
	@Override
	public synchronized Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        Authentication auth = null;
        
        try {
        	LdapUsuarioVO rst =  ldapServiceController.bind(name, password);
        	if (rst != null) {
			    List<GrantedAuthority> grantedAuths = new ArrayList<>();
			    List<LdapGrupoVO> grupos = ldapServiceController.getGruposUsuario(name);
			    for (LdapGrupoVO grupo : grupos) {
					grantedAuths.add(new SimpleGrantedAuthority(ROLE_PREFIX + grupo.getNome().toUpperCase()));
				}
				auth = new UsernamePasswordAuthenticationToken(name, password, grantedAuths);
			    return auth;
			}
		} catch (LDAPException e) {
			e.printStackTrace();
		}
        
        return auth;
    }

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	
	
	public Object chamadaMetodo(Object obj, String nomeMetodo, String usuario, String senha, Long timestamp, Object... args) 
			throws AuthenticationException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
		Object ret = null;
		Method[] metodos = obj.getClass().getDeclaredMethods();
		Method metodo = null;
		for(Method m : metodos) {
			if(m.getName().equalsIgnoreCase(nomeMetodo)) {
				metodo = m;
				break;
			}
		}		
		if (metodo != null) {
			Annotation[] declared = metodo.getDeclaredAnnotations();
			Field f = metodo.getClass().getDeclaredField("clazz");
			if (StringUtils.isNotEmpty(usuario) && StringUtils.isNotEmpty(senha) && (timestamp != null && timestamp > 0)) {
				Date data = new Date(timestamp);
				Date dataAtual = new Date();
				if (((dataAtual.getTime()/60000) - (data.getTime()/60000)) > 3) {
					throw new AuthenticationJazzformsException("Erro ao eutenticar usuário");
				} else {
					UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(usuario, senha);
					Authentication auth = authenticate(authRequest);
					if(auth == null) {
						throw new AuthenticationJazzformsException("Erro ao autenticar usuário");
					} 
					try {
						SecurityContextHolder.getContext().setAuthentication(auth);
						ret = metodo.invoke(obj, args);						
					} catch (Exception e) {						
						SecurityContextHolder.getContext().setAuthentication(null);
						throw e;
					} finally {
						SecurityContextHolder.getContext().setAuthentication(null);
					}
				}
			} else {
				throw new AuthenticationJazzformsException("Erro ao autenticar usuário");
			}
		} else {
			throw new RuntimeException("Não foi possível encontrar o método: " + nomeMetodo);
		}
		return ret;
	}
	
	public class AuthenticationJazzformsException extends AuthenticationException {
		
		public AuthenticationJazzformsException(String msg) {
			super(msg);
		}
		
		@Override
		public Authentication getAuthentication() {
			return super.getAuthentication();
		}

		@Override
		public void setAuthentication(Authentication authentication) {
			super.setAuthentication(authentication);
		}

		@Override
		public Object getExtraInformation() {
			return super.getExtraInformation();
		}

		@Override
		public void clearExtraInformation() {
			super.clearExtraInformation();
		}
	}
}
