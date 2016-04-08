package br.com.laminarsoft.jazzforms.negocio.controller.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@SuppressWarnings("all")
public class RestUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private static final String URL_PREFIX = "http://192.168.1.2";
	private static final String URL_ORIGINAL = "url_original";
	
	@Override
	protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
		boolean retVal = false;
		String username = request.getParameter("j_username");
		String password = request.getParameter("j_password");
		if (username != null && password != null) {
			Authentication authResult = null;
			try {
				authResult = attemptAuthentication(request, response);
				if (authResult == null) {
					retVal = false;
				}
			} catch (AuthenticationException failed) {
				try {
					unsuccessfulAuthentication(request, response, failed);
				} catch (IOException e) {
					retVal = false;
				}
				catch (ServletException e) {
					retVal = false;
				}
				retVal = false;
			}
			try {
				String conteudo = request.getParameter("conteudo");
				String requestURI = request.getRequestURI();
				String contextPath = request.getContextPath();
				requestURI = requestURI.substring(contextPath.length());
				if (StringUtils.isNotEmpty(conteudo)) {
					requestURI += conteudo;
//					requestURI = URL_PREFIX + requestURI;
				}
				setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler(requestURI)) ;
				HttpSession session = request.getSession();
				successfulAuthentication(request, response, authResult);
			} catch (IOException e) {
				retVal = false;
			}
			catch (ServletException e) {
				retVal = false;
			}
			return false;
		} else {
			retVal = true;
		}
		return retVal;
	}

//	@Override
//	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//		boolean retVal = false;
//		
//		Authentication authResult = null;
//		String username = request.getParameter("j_username");
//		String password = request.getParameter("j_password");
//		String tempo = request.getParameter("timestamp");
//		if (username != null && password != null) {
//			try {					
//		        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
//		        setDetails(request, authRequest);
//		        authResult = this.getAuthenticationManager().authenticate(authRequest);
//				if (authResult == null) {
//					throw new UsernameNotFoundException("Não foi possível autenticar usuário");
//				}
//			} catch (AuthenticationException failed) {
//				throw new UsernameNotFoundException("Não foi possível autenticar usuário");
//			}
//
//		} else {
//			throw new UsernameNotFoundException("Não foi possível autenticar usuário");
//		}
//		return authResult;
//	}
//
//	@Override
//	protected void successfulAuthentication(HttpServletRequest request,	HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//		String conteudo = request.getParameter("conteudo");
//		String requestURI = request.getRequestURI();
//		String contextPath = request.getContextPath();
//		requestURI = requestURI.substring(contextPath.length());
//		if (StringUtils.isNotEmpty(conteudo)) {
//			requestURI += conteudo;
////			requestURI = URL_PREFIX + requestURI;
//		}
//		setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler(requestURI)) ;
//		super.successfulAuthentication(request, response, chain, authResult);
////		response.sendRedirect(requestURI);
//	}

}
