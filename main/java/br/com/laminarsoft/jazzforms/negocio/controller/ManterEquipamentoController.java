package br.com.laminarsoft.jazzforms.negocio.controller;

import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

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
import br.com.laminarsoft.jazzforms.persistencia.model.util.InfoRetornoEquipamento;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.GrupoEquipamentoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.RequestVO;

@Controller
@RequestMapping("/servicos/equipamentoService")
@SuppressWarnings("all")
public class ManterEquipamentoController {

	@Autowired private ResourceBundle bundle;	
	@Autowired private ManterEquipamentoService equipamentoService;
	@Autowired private AuthenticationService authenticationService;

	@RequestMapping(value="/equipamento/findEquipamentos", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoEquipamento findEquipamentos(@RequestBody RequestVO request) {
		InfoRetornoEquipamento info = new InfoRetornoEquipamento();
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}		
		try {
			info.equipamentos = equipamentoService.findEquipamentos();
			info.codigo = 0;
			info.mensagem = "Informações dos equipamentos recuperadas com sucesso";
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			String msgErro = "Erro recuperando informações dos equipamentos";
			info.mensagem = msgErro;
		}  catch (AuthenticationException e) {
			info.codigo = 2;
			info.mensagem = "Erro na chamada: " + e.getMessage();
		} catch(AccessDeniedException e) {
			info.codigo = 3;
			info.mensagem = "Acesso negado: privilégios insuficientes";
		} catch (Exception e) {
			e.printStackTrace();
			info.codigo = -1;
			info.mensagem = "Erro inesperado: " + e.getMessage();
		}
		return info;		
	}	
	
	@RequestMapping(value="/equipamento/findGruposEquipamentos", method=RequestMethod.GET)
	@ResponseBody
	public InfoRetornoEquipamento findGruposEquipamentos(@RequestBody RequestVO request) {
		InfoRetornoEquipamento info = new InfoRetornoEquipamento();
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}
		try {
			info.gruposEquipamentos = equipamentoService.findGruposEquipamentos();
			info.codigo = 0;
			info.mensagem = "Informações dos grupos dos equipamentos recuperadas com sucesso";
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			String msgErro = "Erro recuperando informações dos grupos dos equipamentos";
			info.mensagem = msgErro;
		}  catch (AuthenticationException e) {
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
	
	@RequestMapping(value="/equipamento/novoGrupoEquipamento", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoEquipamento novoGrupoEquipamento(@RequestBody GrupoEquipamentoVO grupoEquipamento) {
		InfoRetornoEquipamento info = new InfoRetornoEquipamento();
		try {
			authenticationService.validaToken(grupoEquipamento.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}		
		try {
			equipamentoService.novoGrupoEquipamento(grupoEquipamento);
			info.codigo = 0;
	        info.mensagem = "Grupo equipamento criado com sucesso";
        }  catch (AuthenticationException e) {
			info.codigo = 2;
			info.mensagem = "Erro na chamada: " + e.getMessage();
		} catch(AccessDeniedException e) {
			info.codigo = 3;
			info.mensagem = "Acesso negado: privilégios insuficientes";
		} catch (Exception e) {
        	info.codigo = 1;
        	info.mensagem = "Erro ao criar grupo equipamento: " + e.getMessage();
        } 
		return info;
	}

	
	@RequestMapping(value="/equipamento/alteraGrupoEquipamento", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoEquipamento alteraGrupoEquipamento(@RequestBody GrupoEquipamentoVO grupoEquipamento) {
		InfoRetornoEquipamento info = new InfoRetornoEquipamento();
		
		try {
			authenticationService.validaToken(grupoEquipamento.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}		
		try {
			equipamentoService.alteraGrupoEquipamento(grupoEquipamento);
	        
			info.codigo = 0;
	        info.mensagem = "Grupo equipamento alterado com sucesso";
        }  catch (AuthenticationException e) {
			info.codigo = 2;
			info.mensagem = "Erro na chamada: " + e.getMessage();
		} catch(AccessDeniedException e) {
			info.codigo = 3;
			info.mensagem = "Acesso negado: privilégios insuficientes";
		} catch (Exception e) {
        	info.codigo = 1;
        	info.mensagem = "Erro ao alterar grupo equipamento: " + e.getMessage();
        }
		return info;
	}	
	
	@RequestMapping(value="/equipamento/removeGrupoEquipamento", method=RequestMethod.DELETE)
	@ResponseBody
	public InfoRetornoEquipamento removeGrupoEquipamento(@RequestBody RequestVO request) {
		InfoRetornoEquipamento info = new InfoRetornoEquipamento();
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}		
		try {
			Long idGrupoEquipamento = Long.valueOf(request.parametros[0]);
			equipamentoService.removeGrupoEquipamento(idGrupoEquipamento);
			info.codigo = 0;
			info.mensagem = "Grupo equipamento removido com sucesso";
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			String msgErro = "Não foi possível remover grupo equipamento: " + e.getMessage();
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
	
	@RequestMapping(value="/equipamento/adicionaEquipamentoAoGrupo", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoEquipamento adicionaEquipamentoAoGrupo(@RequestBody GrupoEquipamentoVO grupoEquipamento) {
		InfoRetornoEquipamento info = new InfoRetornoEquipamento();
		try {
			authenticationService.validaToken(grupoEquipamento.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}			
		try {	        
			equipamentoService.adicionaEquipamentoAoGrupo(grupoEquipamento.getId(), grupoEquipamento.getEquipamentos().get(0).getId());	        
			info.codigo = 0;
	        info.mensagem = "Grupo equipamento alterado com sucesso";
        } catch (ParametroException e) {
        	info.codigo = 1;
        	info.mensagem = "Erro ao alterar grupo equipamento: " + e.getMessage();
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
	
	@RequestMapping(value="/equipamento/removeEquipamentoDoGrupo", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoEquipamento removeEquipamentoDoGrupo(@RequestBody GrupoEquipamentoVO grupoEquipamento) {
		InfoRetornoEquipamento info = new InfoRetornoEquipamento();
		try {
			authenticationService.validaToken(grupoEquipamento.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}		
		try {
			equipamentoService.removeEquipamentoDoGrupo(grupoEquipamento.getId(), grupoEquipamento.getEquipamentos().get(0).getId());	        
			info.codigo = 0;
	        info.mensagem = "Grupo equipamento alterado com sucesso";
        } catch (ParametroException e) {
        	info.codigo = 1;
        	info.mensagem = "Erro ao alterar grupo equipamento: " + e.getMessage();
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
