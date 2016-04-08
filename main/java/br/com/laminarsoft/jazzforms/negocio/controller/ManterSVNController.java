package br.com.laminarsoft.jazzforms.negocio.controller;

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
import br.com.laminarsoft.jazzforms.persistencia.model.util.InfoRetornoSVN;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.RequestVO;

@Controller
@RequestMapping("/servicos/svnService")
@SuppressWarnings("all")
public class ManterSVNController {

	@Autowired private ResourceBundle bundle;
	@Autowired private ManterSVNService svnService;
	@Autowired private AuthenticationService authenticationService;
	
	@RequestMapping(value="/svn/recuperaVersoes", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoSVN findGrupo(@RequestBody RequestVO request) {
		InfoRetornoSVN info = new InfoRetornoSVN();
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}		
		try {
			String nomeProjeto = request.parametros[0];
			if(nomeProjeto.contains("_i_")) {
				nomeProjeto = nomeProjeto.replace("_i_", " ");
			}
			info.lstProjetos.addAll(svnService.recuperaVersoesProjeto(nomeProjeto));
			info.codigo = 0;
			info.mensagem = "Versoes do projeto encontradas com sucesso.";
		} catch(ParametroException e) {
			info.codigo = 1;
			info.mensagem = "Erro ao encontrar versoes do projeto: " + e.getMessage();
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
	

	@RequestMapping(value="/svn/checkOutProjeto", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoSVN checkOutProjeto(@RequestBody RequestVO request) {
		InfoRetornoSVN info = new InfoRetornoSVN();
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}			
		try{
			String nomeProjeto = request.parametros[0];
			String versao = request.parametros[1];
			if(nomeProjeto.contains("_i_")) {
				nomeProjeto = nomeProjeto.replace("_i_", " ");
			}
			info.projeto = svnService.checkOutProjeto(nomeProjeto, versao);
			info.codigo = 0;
			info.mensagem = "Projeto recuperado com sucesso.";
		} catch(ParametroException e) {
			info.codigo = 1;
			info.mensagem = "Erro ao recuperar projeto: " + e.getMessage();
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
