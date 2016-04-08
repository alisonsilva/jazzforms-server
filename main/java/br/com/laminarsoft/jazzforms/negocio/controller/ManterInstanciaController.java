package br.com.laminarsoft.jazzforms.negocio.controller;

import java.util.ResourceBundle;

import org.apache.commons.codec.binary.Base64;
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
import br.com.laminarsoft.jazzforms.persistencia.model.Foto;
import br.com.laminarsoft.jazzforms.persistencia.model.Usuario;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.ParametroException;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Option;
import br.com.laminarsoft.jazzforms.persistencia.model.util.InfoRetornoDeployments;
import br.com.laminarsoft.jazzforms.persistencia.model.util.InfoRetornoInstancia;
import br.com.laminarsoft.jazzforms.persistencia.model.util.InfoRetornoOpcaoSelect;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.RequestVO;

@Controller
@RequestMapping("/servicos/instanciaService")
@SuppressWarnings("all")
public class ManterInstanciaController {

	@Autowired private ResourceBundle bundle;	
	@Autowired private ManterInstanciaService instanciaService;
	@Autowired private AuthenticationService authenticationService;

	@RequestMapping(value="/instancia/reenviaInstanciaUsuario", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoInstancia reenviaInstanciaUsuario(@RequestBody RequestVO request) {
		InfoRetornoInstancia info = new InfoRetornoInstancia();
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}		
		try{
			Long instanciaId = Long.valueOf(request.parametros[0]);
			String loginUsuario = request.parametros[1];
			String loginUsuarioAlteracao = request.parametros[2];
			instanciaService.reenviarInstanciaUsuario(instanciaId, loginUsuario, loginUsuarioAlteracao);
			info.codigo = 0;
			info.mensagem = "Reenvio realizado com sucesso.";
		} catch(ParametroException e) {
			info.codigo = 1;
			info.mensagem = "Erro ao reenviar formulário preenchido para usuário: " + e.getMessage();
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
	
	@RequestMapping(value="/instancia/desfazerReenvioInstancia", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoInstancia desfazerReenvioInstancia(@RequestBody RequestVO request) {
		InfoRetornoInstancia info = new InfoRetornoInstancia();
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}	
		try{
			Long instanciaId = Long.valueOf(request.parametros[0]);
			String loginUsuarioAlteracao = request.parametros[1];
			instanciaService.desfazerReenvioInstancia(instanciaId, loginUsuarioAlteracao);
			info.codigo = 0;
			info.mensagem = "Reenvio desfeito com sucesso.";
		} catch(ParametroException e) {
			info.codigo = 1;
			info.mensagem = "Erro ao desfazer reenvio de formulário: " + e.getMessage();
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
	
	@RequestMapping(value="/instancia/usuariosReenvioInstancia", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoInstancia usuariosReenvioInstancia(@RequestBody RequestVO request) {
		InfoRetornoInstancia info = new InfoRetornoInstancia();
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}		
		try{
			Long instanciaId = Long.valueOf(request.parametros[0]);
			info.usuariosReenvioInstancia = instanciaService.usuariosPossiveisReenvioInstancia(instanciaId);
			for(Usuario usr : info.usuariosReenvioInstancia) {
				usr.setGrupos(null);
				usr.setHistoricos(null);
				usr.setDeployment(null);
				usr.setMensagens(null);
			}
			info.codigo = 0;
			info.mensagem = "Usuários de reenvio recuperados com sucesso.";
		} catch(ParametroException e) {
			info.codigo = 1;
			info.mensagem = "Erro ao recuperar usuários de reenvio: " + e.getMessage();
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

	@RequestMapping(value="/instancia/fotosPorInstancia", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoInstancia getFotosPorInstancia(@RequestBody RequestVO request) {
		InfoRetornoInstancia info = new InfoRetornoInstancia();
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}	
		
		try {
			Long instanciaId = Long.valueOf(request.parametros[0]);
			info.fotos = instanciaService.getFotosPorInstancia(instanciaId);
			for(Foto foto : info.fotos) {				
				foto.setPicture(new Base64().decode(foto.pictStr.getBytes()));
			}
			info.codigo = 0;
			info.mensagem = "Fotos recuperadas com sucesso";
		} catch(ParametroException e) {
			info.codigo = 1;
			info.mensagem = "Erro ao recuperar fotos para a instancia: " + e.getMessage();
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
	
	@RequestMapping(value="/instancia/opcoesTeste", method=RequestMethod.GET)
	public InfoRetornoOpcaoSelect getInfoSelectOptions() {
		InfoRetornoOpcaoSelect info = new InfoRetornoOpcaoSelect();
		Option opt = new Option();
		opt.setId(1); opt.setValue("vl1");opt.setText("Texto valor 1");
		info.opcoes.add(opt);
		
		opt = new Option();
		opt.setId(2); opt.setValue("vl2");opt.setText("Texto valor 2");
		info.opcoes.add(opt);
		
		opt = new Option();
		opt.setId(3); opt.setValue("vl3");opt.setText("Texto valor 3");
		info.opcoes.add(opt);

		info.codigo = 0;
		info.mensagem = "Opções recuperadas com sucesso";
		return info;
	}
	
}
