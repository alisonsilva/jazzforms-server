package br.com.laminarsoft.jazzforms.negocio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/servicos/produtosService")
@Scope("request")
public class ProductsService {
	@Autowired private ProductsController products;

	@RequestMapping(value="/produto/getProductsList", method=RequestMethod.GET)
	@ResponseBody
	public String getProductsList() {
		return products.getProductsList();
	}
}