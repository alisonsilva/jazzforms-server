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
import br.com.laminarsoft.jazzforms.persistencia.model.ComponentType;
import br.com.laminarsoft.jazzforms.persistencia.model.TipoEvento;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.ParametroException;
import br.com.laminarsoft.jazzforms.persistencia.model.util.InfoRetornoEvento;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.RequestVO;

@Controller
@RequestMapping("/servicos/eventoService")
@SuppressWarnings("all")
public class ManterEventoController {

	@Autowired private ResourceBundle bundle;	
	@Autowired private ManterEventoService eventoService;
	@Autowired private AuthenticationService authenticationService;

	@RequestMapping(value="/evento/findByName", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoEvento findById(@RequestBody RequestVO request) {
		List<ComponentType> ctypes = null;
		InfoRetornoEvento info = new InfoRetornoEvento();
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}
		try {
			String name = request.parametros[0];
			ctypes = eventoService.findByName(name);
			info.codigo = 0;
			info.mensagem = bundle.getString("localizar.tipoeventos.nome");
			info.ctypes = ctypes;
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			String msgErro = bundle.getString("localizar.tipoeventos.erro");
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
	
	
	
	@RequestMapping(value="/evento/findTiposByCTypeId", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoEvento findTiposEventoByCT(@RequestBody RequestVO request) {
		InfoRetornoEvento info = new InfoRetornoEvento();
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}			
		try {
			Long ctypeId = Long.valueOf(request.parametros[0]);
			List<TipoEvento> eventos = eventoService.findTiposEventoByCT(ctypeId);
			info.codigo = 0;
			info.mensagem = "Tipos de evento recuperados com sucesso";
			info.tiposEvento = eventos;
		} catch(ParametroException e) {
			info.codigo = 1;
			String msgErro = "Erro ao recuperar tipos de evento: " + e.getMessage();
			info.mensagem = msgErro;
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
