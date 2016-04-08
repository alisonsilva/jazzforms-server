package br.com.laminarsoft.jazzforms.negocio.controller.security;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import sun.misc.BASE64Decoder;
import br.com.laminarsoft.jazzforms.logging.types.LoggingAutenticacaoMSG;
import br.com.laminarsoft.jazzforms.negocio.controller.util.LogController;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.UsuarioVO;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class AuthenticationInterceptor extends HandlerInterceptorAdapter {
	@Autowired private LogController logController;
	
	private AuthenticationManager authenticationManager;	

    private final String cipherTransformation = "AES/CBC/NoPadding";    
    
    private final byte[] keyValue = new byte[]{'F', 'E', '1', 'B', 'D', '5', '7', '5', '6', '2', '7', 'D', 'F', 'a', '1', 'c'};
    private final byte[] ivValue = new byte[]{'f', 'e', 'd', 'c', 'b', 'a', '9', '8', '7', '6', '5', '4', '3', '2', '1', '0'};
    
    private IvParameterSpec ivspec = new IvParameterSpec(ivValue);
    private SecretKeySpec keyspec = new SecretKeySpec(keyValue, "AES");
    
	private Cipher cipher = null;
    
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		String cript = request.getParameter("par");
		String plat = request.getParameter("plat");
		if (cipher == null) {
			cipher = Cipher.getInstance(cipherTransformation,  new BouncyCastleProvider());
		}
		
		if (StringUtils.isEmpty(plat)) {
			sendErrorMsg("", new Date(), "Erro ao realizar autenticacao: campo plataforma invalido");
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
			return false;
		} else if(!plat.equalsIgnoreCase("mob") && !plat.equalsIgnoreCase("jazz") ) {
			sendErrorMsg("", new Date(), "Erro ao realizar autenticacao: campo plataforma invalido");
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
			return false;
		}
		
		String valores = decrypt(cript);
		
		if (StringUtils.isEmpty(valores)) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
			return false;
		}
		
		UsuarioVO usrVo = null;
		try {
			Gson gson = new Gson();
			JsonReader reader = new JsonReader(new StringReader(valores));
			reader.setLenient(true);
			usrVo = gson.fromJson(reader, UsuarioVO.class);
		} catch (Exception e) {
			sendErrorMsg("", new Date(), "Formato dos valores de autenticacao invalido: " + e.getMessage());
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
			return false;
		}	        		
		
		String usuario = usrVo.lgn;
		String password = usrVo.sn;
		String timestamp = usrVo.tm;

		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath();

		boolean estatico = false;
		
		if (requestURI.contains("jazzforms/estatico")) {
			estatico = true;
		} else if (StringUtils.isEmpty(timestamp)) {// se não vier o timestamp da requisição, erro
			sendErrorMsg(usuario, new Date(), "Timestamp invalido");
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
			return false;
		} else {
			Date data = new Date(Long.parseLong(timestamp));
			Date dataAtual = new Date();
			if (((dataAtual.getTime()/60000) - (data.getTime()/60000)) > 3) {//se a requisição tiver mais que três minutos, erro
				sendErrorMsg(usuario, new Date(), "Timestamp fora do intervalo");
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
				return false;
			}
		}
		
		String[] credentials = {usuario, password};
		assert credentials.length == 2;
		Authentication authentication = new UsernamePasswordAuthenticationToken(credentials[0], credentials[1]);
		
		Authentication successfulAuthentication = null;
		
		if (!estatico) {
			successfulAuthentication = authenticationManager.authenticate(authentication);
			requestURI = requestURI.substring(contextPath.length());
			if (successfulAuthentication == null) {
				sendErrorMsg(usuario, new Date(), "Usuario nao autorizado");
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED,	"Unauthorized");
				return false;
			}
		} else {
			List<GrantedAuthority> grantedAuths = new ArrayList<>();
		    grantedAuths.add(new SimpleGrantedAuthority("ROLE_ESTATICO"));
		    successfulAuthentication = new UsernamePasswordAuthenticationToken("estatico", "estatico", grantedAuths);
		    int idx = requestURI.lastIndexOf('/');
			requestURI = requestURI.substring(idx+1, requestURI.length());
		}
		SecurityContextHolder.getContext().setAuthentication(successfulAuthentication);
		return true;
	}
	
	private void sendErrorMsg(String login, Date data, String msg) {
		LoggingAutenticacaoMSG mensagem = new LoggingAutenticacaoMSG();
		mensagem.login = "";
		mensagem.setDhChamada(new Date());
		mensagem.mensagemErro = msg;
		logController.enviaMensagemLogAutenticacao(mensagem);
		
	}

	public String decrypt(String encryptedData)	{
		String decryptedValue = null; 
        try {
			cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
			encryptedData = encryptedData.replace("_p_", "+");
			byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
			byte[] decValue = cipher.doFinal(decordedValue);
			decryptedValue = new String(decValue, "UTF-8");
		} catch (InvalidKeyException e) {
			sendErrorMsg("", new Date(), "Erro ao realizar autenticacao: valores de acesso invalidos: "  + e.getMessage());
		} catch (InvalidAlgorithmParameterException e) {
			sendErrorMsg("", new Date(), "Erro ao realizar autenticacao: valores de acesso invalidos: "  + e.getMessage());
		} catch (IllegalBlockSizeException e) {
			sendErrorMsg("", new Date(), "Erro ao realizar autenticacao: valores de acesso invalidos: "  + e.getMessage());
		} catch (BadPaddingException e) {
			sendErrorMsg("", new Date(), "Erro ao realizar autenticacao: valores de acesso invalidos: "  + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			sendErrorMsg("", new Date(), "Erro ao realizar autenticacao: valores de acesso invalidos: "  + e.getMessage());
		} catch (IOException e) {
			sendErrorMsg("", new Date(), "Erro ao realizar autenticacao: valores de acesso invalidos: "  + e.getMessage());
		}
        return decryptedValue;
	}	
	
	

	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	public void setAuthenticationManager(
			AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
}
