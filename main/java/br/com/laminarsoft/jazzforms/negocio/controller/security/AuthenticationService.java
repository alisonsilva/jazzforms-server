package br.com.laminarsoft.jazzforms.negocio.controller.security;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sun.misc.BASE64Decoder;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import br.com.laminarsoft.jazzforms.logging.types.LoggingAutenticacaoMSG;
import br.com.laminarsoft.jazzforms.negocio.controller.util.LogController;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.ParametroException;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.RequestVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.UsuarioVO;


@Service(value="authenticationService")
@SuppressWarnings("all")
public class AuthenticationService {
	@Autowired private LogController logController;
	
	private AuthenticationManager authenticationManager;	

    private final String cipherTransformation = "AES/CBC/NoPadding";    
    
    private final byte[] keyValue = new byte[]{'F', 'E', '1', 'B', 'D', '5', '7', '5', '6', '2', '7', 'D', 'F', 'a', '1', 'c'};
    private final byte[] ivValue = new byte[]{'f', 'e', 'd', 'c', 'b', 'a', '9', '8', '7', '6', '5', '4', '3', '2', '1', '0'};
    
    private IvParameterSpec ivspec = new IvParameterSpec(ivValue);
    private SecretKeySpec keyspec = new SecretKeySpec(keyValue, "AES");
    
	private Cipher cipher = null;

	public AuthenticationService() {
		if (cipher == null) {
			try {
				cipher = Cipher.getInstance(cipherTransformation);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void validaToken(String token) {
		
		if (StringUtils.isEmpty(token)) {
			sendErrorMsg("Token de autenticacao esta vazio", new Date(), "Erro ao realizar autenticacao: campo plataforma invalido");
			throw new ParametroException("Token de autanticacao esta vazio", RequestVO.CODIGO_DECRIPT_TOKEN_ERROR);
		} 
		
		String valores = decrypt(token);
		
		if (StringUtils.isEmpty(valores)) {
			throw new ParametroException("Nao foi possivel decriptogravar token", RequestVO.CODIGO_DECRIPT_TOKEN_ERROR);
		}
		
		UsuarioVO usrVo = null;
		try {
			Gson gson = new Gson();
			JsonReader reader = new JsonReader(new StringReader(valores));
			reader.setLenient(true);
			usrVo = gson.fromJson(reader, UsuarioVO.class);
		} catch (Exception e) {
			sendErrorMsg("", new Date(), "Formato dos valores de autenticacao invalido: " + e.getMessage());
		}
		
		if(usrVo != null) {
			String usuario = usrVo.lgn;
			String password = usrVo.sn;
			String timestamp = usrVo.tm;


			boolean estatico = false;
			
			if (StringUtils.isEmpty(timestamp)) {// se não vier o timestamp da requisição, erro
				sendErrorMsg(usuario, new Date(), "Timestamp invalido");
				throw new ParametroException("Tempo de envio invalido", RequestVO.CODIGO_TIMESTAMP_EXPIRADO);
			} else {
				Date data = new Date(Long.parseLong(timestamp));
				Date dataAtual = new Date();
				if (((dataAtual.getTime()/60000) - (data.getTime()/60000)) > 5) {//se a requisição tiver mais que cinco minutos, erro
					sendErrorMsg(usuario, new Date(), "Timestamp fora do intervalo");
					throw new ParametroException("Tempo de envio expirado", RequestVO.CODIGO_TIMESTAMP_EXPIRADO);
				}
			}
		} else {
			throw new ParametroException("Nao foi possivel criar token", RequestVO.CODIGO_DECRIPT_TOKEN_ERROR);
		}
	}
	
	public UsuarioVO getUsuarioFromToken(String token) {
		String valores = decrypt(token);
		
		if (StringUtils.isEmpty(valores)) {
			throw new ParametroException("Nao foi possivel decriptogravar token", RequestVO.CODIGO_DECRIPT_TOKEN_ERROR);
		}
		
		UsuarioVO usrVo = null;
		try {
			Gson gson = new Gson();
			JsonReader reader = new JsonReader(new StringReader(valores));
			reader.setLenient(true);
			usrVo = gson.fromJson(reader, UsuarioVO.class);
		} catch (Exception e) {
			sendErrorMsg("", new Date(), "Formato dos valores de autenticacao invalido: " + e.getMessage());
		}
		return usrVo;
	}
	
	public String criaToken(String login, String pass) {
		if (StringUtils.isEmpty(login) || StringUtils.isEmpty(pass)) {
			throw new ParametroException("Login vazio ou senha vazios", 1);
		}
		String token = null;
		try {
			String forma = "{lgn: " + login + ", sn: " + pass + ", tm: " + System.currentTimeMillis() + "}";
			String formaPadded = padding(forma);
			cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
			byte[] encrypted = cipher.doFinal(formaPadded.getBytes());
            token = new String(Base64.encodeBase64(encrypted));
            token = token.replace("+", "_p_");
            token = token.replace("/", "_b_");
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		}
		return token;
	}
	
	public String encrypt(String palavra) {
		if (StringUtils.isEmpty(palavra)) {
			throw new ParametroException("A palavra a ser criptografada não pode estar vazia", 1);
		}
		String token = null;
		try {
			String formaPadded = padding(palavra);
			cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
			byte[] encrypted = cipher.doFinal(formaPadded.getBytes());
            token = new String(Base64.encodeBase64(encrypted));
            token = token.replace("+", "_p_");
            token = token.replace("/", "_b_");
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		}
		return token;
	}
	
	private String padding(String stringToPad) {
		int size = 16;
		String paddingChar = " ";
		int x = stringToPad.length() % size;
		int padLength = size - x;
		for(int i = 0; i < padLength; i++) {
			stringToPad += paddingChar;
		}
		return stringToPad;
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
			encryptedData = encryptedData.replace("_b_", "/");
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
	
}
