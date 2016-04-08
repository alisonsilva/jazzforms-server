package br.com.laminarsoft.jazzforms.persistencia.test.security;

import org.junit.Test;

import br.com.laminarsoft.jazzforms.negocio.controller.security.AuthenticationService;

public class AuthenticationServiceTest {

	@Test
	public void test1() {
		AuthenticationService aus = new AuthenticationService();
		String senhaCriptada = aus.encrypt("123455");
		System.out.println(senhaCriptada);
	}
}
