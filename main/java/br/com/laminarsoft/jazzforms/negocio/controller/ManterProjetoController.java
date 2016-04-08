package br.com.laminarsoft.jazzforms.negocio.controller;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
import br.com.laminarsoft.jazzforms.negocio.controller.security.CustomAuthenticationProvider;
import br.com.laminarsoft.jazzforms.persistencia.model.Deployment;
import br.com.laminarsoft.jazzforms.persistencia.model.Grupo;
import br.com.laminarsoft.jazzforms.persistencia.model.Mensagem;
import br.com.laminarsoft.jazzforms.persistencia.model.Pagina;
import br.com.laminarsoft.jazzforms.persistencia.model.Projeto;
import br.com.laminarsoft.jazzforms.persistencia.model.Usuario;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.ParametroException;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.ParametroProjetoException;
import br.com.laminarsoft.jazzforms.persistencia.model.util.InfoRetornoDeployments;
import br.com.laminarsoft.jazzforms.persistencia.model.util.InfoRetornoPagina;
import br.com.laminarsoft.jazzforms.persistencia.model.util.InfoRetornoProjeto;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.ProjetoWrapperVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.RequestVO;

@Controller
@RequestMapping("/servicos/projetoService")
@SuppressWarnings("all")
public class ManterProjetoController {

	@Autowired private ResourceBundle bundle;	
	@Autowired private ManterProjetoService projetoService;	
	@Autowired private ManterPaginaService paginaService;
	@Autowired private CustomAuthenticationProvider customAuthenticationProvider;
	@Autowired private AuthenticationService authenticationService;
	
	@RequestMapping(value="/projeto/findById", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoProjeto findById(@RequestBody RequestVO request) {
		Projeto projeto = null;	
		InfoRetornoProjeto info = new InfoRetornoProjeto();
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}		
		try {
			Long id = Long.valueOf(request.parametros[0]);
			projeto = projetoService.findById(id);
			projeto.setHistoricos(null);
			info.codigo = 0;			
			info.mensagem = bundle.getString("localizar.projeto.id");
			info.projeto = projeto;
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			String msgErro = bundle.getString("localizar.projeto.id.erro");
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
	
	@RequestMapping(value = "/projeto/findAll", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoProjeto findAllNoLimit(@RequestBody RequestVO request) {
		InfoRetornoProjeto info = new InfoRetornoProjeto();
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}
		
		try {
			List<Projeto> projetos = projetoService.findAllNoLimit();
			List<Projeto> clProjetos = new ArrayList<Projeto>();
			for(Projeto projeto : projetos) {
				Projeto p = projeto.clone(); 
				p.getPaginas().clear();
				p.getHistoricos().clear();
				clProjetos.add(p);
			}
			info.projetos = clProjetos;
			info.mensagem = bundle.getString("localizar.projetos.id");
			info.codigo = 0;
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			String msgErro = bundle.getString("localizar.projeto.id.erro");
			info.mensagem = (MessageFormat.format(msgErro, e.getMessage()));
		} catch (AuthenticationException e) {
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
	
	@RequestMapping(value = "/projeto/findAllPublished", method=RequestMethod.POST)
	@ResponseBody	
	public InfoRetornoProjeto findAllPublished(@RequestBody RequestVO request) {
		InfoRetornoProjeto info = new InfoRetornoProjeto();
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}		
		try {
			List<Projeto> clProjetos = new ArrayList<Projeto>();
			List<Projeto> projetos = projetoService.findAllPublished();
			for(Projeto projeto : projetos) {
				Projeto p = projeto.clone();
				p.getPaginas().clear();
				p.getHistoricos().clear();
				for(Grupo grupo : p.getDeployment().getGruposPossiveis()) {
					grupo.setMensagens(null);
				}
				for(Usuario usuario : p.getDeployment().getUsuariosPossiveis()) {
					usuario.setMensagens(null);
				}
				clProjetos.add(p);
			}
			info.projetos = clProjetos;
			info.mensagem = bundle.getString("localizar.projetos.publicados");
			info.codigo = 0;
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			String msgErro = bundle.getString("localizar.projetos.publicados.erro");
			info.mensagem = (MessageFormat.format(msgErro, e.getMessage()));
		} catch (AuthenticationException e) {
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
	
	@RequestMapping(value="/projeto/deactivateDeployedProject", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoProjeto deactivateDeployedProject(@RequestBody RequestVO request) {
		Projeto projeto = null;
		String retorno = "";
		InfoRetornoProjeto info = new InfoRetornoProjeto();
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}		
		try {
			Long id = Long.valueOf(request.parametros[0]);
			Boolean flagActivate = Boolean.valueOf(request.parametros[1]);
			String usuario = request.parametros[2];
			retorno = projetoService.deactivatePublishedProject(id, flagActivate, usuario);
			
			if (retorno.trim().length() > 0) {
	            info.codigo = 1;
	            info.mensagem = retorno;
            } else {
            	info.codigo = 0;
    			info.mensagem = "";
            }
			info.projeto = projeto;
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			String msgErro = bundle.getString("desativar.projeto.id.erro");
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
	
	@RequestMapping(value="/projeto/persist", method=RequestMethod.POST)
	@ResponseBody	
	public InfoRetornoProjeto insereProjeto(@RequestBody ProjetoWrapperVO projeto) {
		InfoRetornoProjeto info = new InfoRetornoProjeto();
		try {
			authenticationService.validaToken(projeto.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}		
		try {
			projetoService.persist(projeto.projeto);
			info.codigo = 0;
			info.projeto = projeto.projeto;
			info.mensagem = bundle.getString("inserir.projeto.sucesso");
		} catch (ParametroProjetoException e) {
			info.codigo = 1;
			info.mensagem = "Erro nas identificações do projeto: " + e.getInfoValidacao().naoValidos.get(0).nomeCampo + " - " + e.getInfoValidacao().naoValidos.get(0).mensagem;
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = MessageFormat.format(bundle.getString("inserir.projeto.erro"), e.getMessage());
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
	
	@RequestMapping(value="/projeto/deploy", method=RequestMethod.POST)
	@ResponseBody	
	public InfoRetornoProjeto deployProjeto(@RequestBody ProjetoWrapperVO projetoWrapper) {
		InfoRetornoProjeto info = new InfoRetornoProjeto();
		try {
			authenticationService.validaToken(projetoWrapper.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}	
		
		Deployment deployment = projetoWrapper.projeto.getDeployment();
		deployment.setProjeto(projetoWrapper.projeto);
		try {
			if (projetoWrapper.projeto.getId() > 0) {
				boolean depAtivo = projetoService.existeDeploymentAtivo(projetoWrapper.projeto.getId());
				if (!depAtivo) {
					Projeto prjDeploy = projetoService.deploy(projetoWrapper.projeto);
					info.codigo = 0;
					prjDeploy.setHistoricos(null);
					info.projeto = prjDeploy;
					info.mensagem = bundle.getString("deploy.projeto.sucesso");
				} else {
					info.codigo = 1;
					info.mensagem = MessageFormat.format(bundle.getString("deploy.projeto.erro"), "Já existe uma publicação ativa para este projeto");
				}
				
			} else {
				info.codigo = 1;
				info.mensagem = MessageFormat.format(bundle.getString("deploy.projeto.erro"), "é necessário ter um projeto base anteriormente salvo");
			}
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = MessageFormat.format(bundle.getString("deploy.projeto.erro"), e.getMessage());
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
	
	@RequestMapping(value="/deployment/removeDeployment", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoDeployments removeDeployment(@RequestBody RequestVO request) {
		InfoRetornoDeployments info = new InfoRetornoDeployments();
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}		
		try {
			Long deploymentId = Long.valueOf(request.parametros[0]);
			String loginUsuario = request.parametros[1];
			projetoService.removeDeployment(deploymentId, loginUsuario);
			info.codigo = 0;
			info.mensagem = "Deployment removido com sucesso";
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

	@RequestMapping(value="/deployment/refreshProjetoDeployment", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoDeployments refreshProjetoDeployment(@RequestBody RequestVO request) {
		InfoRetornoDeployments info = new InfoRetornoDeployments();
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}		
		try {
			Long deploymentId = Long.valueOf(request.parametros[0]);
			String loginUsuario = request.parametros[1];
			Projeto projeto = projetoService.refreshProjetoDeployment(deploymentId, loginUsuario);
			if (projeto != null) {
				info.codigo = 0;
				info.mensagem = "Refresh de projeto realizado com sucesso";
			} else {
				info.codigo = 1;
				info.mensagem = "Refresh de projeto não realizado";
			}
		} catch(ParametroException e) {
			info.codigo = 1;
			info.mensagem = "Erro ao atualizar projeto do deployment: " + e.getMessage();
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
	
	@RequestMapping(value="/deployment/getDeploymentById", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoDeployments getDeploymentById(@RequestBody RequestVO request) {
		InfoRetornoDeployments info = new InfoRetornoDeployments();
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}		
		try {
			Long deploymentId = Long.valueOf(request.parametros[0]);
			Deployment deployment = projetoService.getDeployById(deploymentId);
			Deployment clDep = deployment.clone();
			for(Grupo grupo : clDep.getGruposPossiveis()) {
				grupo.setMensagens(new ArrayList<Mensagem>());
			}
			for(Usuario usr : clDep.getUsuariosPossiveis()) {
				usr.setMensagens(new ArrayList<Mensagem>());
				for(Grupo grupo : usr.getGrupos()) {
					grupo.setMensagens(new ArrayList<Mensagem>());
				}
			}
			info.deployment = clDep;
		} catch(ParametroException e) {
			info.codigo = 1;
			info.mensagem = "Erro ao recuperar deployment: " + e.getMessage();
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
	
	@RequestMapping(value="/deployment/alteraDestinatarios", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoProjeto alterarDestinatarios(@RequestBody ProjetoWrapperVO projetoWrapper) {
		InfoRetornoProjeto info = new InfoRetornoProjeto();
		try {
			authenticationService.validaToken(projetoWrapper.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}	

		try {
			projetoService.alteraDestinatariosDeployment(projetoWrapper.projeto);
			info.codigo = 0;
			info.mensagem = "Alteração realizada com sucesso";
			info.projeto = projetoWrapper.projeto;
		} catch (ParametroException e) {
			info.codigo = 1;
			info.mensagem = "Erro ao alterar destinatários do deployment: " + e.getMessage();
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
	
	
	@RequestMapping(value="/pagina/findById/{id}", method=RequestMethod.GET)
	@ResponseBody
	public InfoRetornoPagina findPaginaById(@PathVariable Long id) {
		Pagina projeto = null;
		InfoRetornoPagina info = new InfoRetornoPagina();
		try {
			projeto = paginaService.findById(id);
			info.codigo = 0;
			info.mensagem = bundle.getString("localizar.pagina.id");
			info.pagina = projeto;
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			String msgErro = bundle.getString("localizar.pagina.id.erro");
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
