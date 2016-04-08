package br.com.laminarsoft.jazzforms.persistencia.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.validator.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.laminarsoft.jazzforms.persistencia.model.Usuario;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.IMensagensErro;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.ParametroException;

@Transactional(propagation=Propagation.SUPPORTS, 
		isolation=Isolation.READ_COMMITTED, 
		timeout=30)
@Repository("loginUtil")
@SuppressWarnings("all")
public class LoginUtil {
	private static final String LOG_LOGIN_INVALIDO = "Não foi encontrado nenhum registro para o login.";

	private static final String HQL_LOGIN = "FROM " + Usuario.class.getSimpleName() + " WHERE login = :login";
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
	
	private Pattern patternLogin = Pattern.compile("^[a-zA-Z0-9]{3,15}$");
	

	private Log log;
	
	//@Autowired
	//FacesMessages facesMessages;
	
	@Autowired
	protected HibernateTemplate hibernateTemplate;
	
//	@In(create = true, value = "empresaLogin")
//	@Out(scope=ScopeType.SESSION, value="empresaLogin")
	private Usuario usuarioLogin;
	
	public Usuario getEmpresaLogin() {
		return usuarioLogin;
	}

	public void setEmpresaLogin(Usuario empresaLogin) {
		this.usuarioLogin = empresaLogin;
	}
	
	public boolean isEmpresaLogada() {
		if (usuarioLogin != null) {
			return true;
		}
		
		return false;
	} 

	
	public enum Papel {
		ADMIM("administrador"),
		EMPRESA("empresa");
		
		private String nome;
		
	    Papel(String nome) {
			this.nome = nome;
		}
	    
	    public String getNome() {
			return nome;
		}
	}
	
	public void validaLogin(String login) {
		if(StringUtils.isEmpty(login)) {
			throw new ParametroException("Login em branco", IMensagensErro.PARAMETRO_NULO_CODE);
		}
		
		EmailValidator emailValidator = EmailValidator.getInstance();
		Matcher matcher = patternLogin.matcher(login);
		if(!emailValidator.isValid(login) && !matcher.matches()) {
			throw new ParametroException("Formato do nome de usuário inválido", IMensagensErro.PARAMETRO_NULO_CODE);
		}
	}
	
	public static String senhaMd5(String senha) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));
	        String digest = hash.toString(16);
	        if (digest.length() % 2 != 0) {
	        	digest = "0" + digest;
	        }
	        return digest;
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return "";
        
	}
	
}
