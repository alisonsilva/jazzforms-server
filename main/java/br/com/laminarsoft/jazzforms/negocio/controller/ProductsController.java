package br.com.laminarsoft.jazzforms.negocio.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

@Component
@Secured({"ROLE_ADMINISTRADORES", "ROLE_LAMINARSOFTADMIN"})
public class ProductsController {
	public String getProductsList() {
		// Implementation of method
		return "produtos localizados";
	}
}