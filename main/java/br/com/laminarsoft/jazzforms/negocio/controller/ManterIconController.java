package br.com.laminarsoft.jazzforms.negocio.controller;

import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;

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
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Icon;
import br.com.laminarsoft.jazzforms.persistencia.model.util.InfoRetornoIcon;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.RequestVO;

@Controller
@RequestMapping("/servicos/iconService")
@SuppressWarnings("all")
public class ManterIconController {

	@Autowired private ResourceBundle bundle;
	@Autowired private ManterIconService iconService;
	@Autowired private AuthenticationService authenticationService;

	@RequestMapping(value="/icon/findByName/{name}", method=RequestMethod.GET)
	@ResponseBody
	public InfoRetornoIcon findByName(@PathVariable String name) {
		Icon icon = null;
		InfoRetornoIcon info = new InfoRetornoIcon();
		try {
			icon = iconService.findByName(name);
			info.codigo = 0;
			info.mensagem = bundle.getString("localizar.icon.nome");
			info.icon = icon;
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			String msgErro = bundle.getString("localizar.icon.erro");
			info.mensagem = (MessageFormat.format(msgErro, e.getMessage()));
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

	@RequestMapping(value="/icon/findAllWithName", method=RequestMethod.GET)
	@ResponseBody
	public InfoRetornoIcon findAllWithName(@RequestBody RequestVO request) {
		List<Icon> icons = null;
		InfoRetornoIcon info = new InfoRetornoIcon();
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}
		try {
			icons = iconService.findAllWithName();
			info.codigo = 0;
			info.mensagem = bundle.getString("localizar.icon");
			info.icons = icons;
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			String msgErro = bundle.getString("localizar.icon.erro");
			info.mensagem = (MessageFormat.format(msgErro, e.getMessage()));
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
