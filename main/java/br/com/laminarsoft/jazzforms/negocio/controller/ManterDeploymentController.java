package br.com.laminarsoft.jazzforms.negocio.controller;

import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.List;
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
import br.com.laminarsoft.jazzforms.persistencia.model.ValorDataview;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.ParametroException;
import br.com.laminarsoft.jazzforms.persistencia.model.util.InfoRetornoDeployments;
import br.com.laminarsoft.jazzforms.persistencia.model.util.InfoRetornoInstancia;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.DataViewWrapperVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.InstanciaVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.InstanciasVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.ProjetoImplantadoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.RequestVO;

@Controller
@RequestMapping("/servicos/deploymentService")
@SuppressWarnings("all")
public class ManterDeploymentController {

	@Autowired private ResourceBundle bundle;	
	@Autowired private ManterDeploymentService deploymentService;	
	@Autowired private ManterPaginaService paginaService;
	@Autowired private AuthenticationService authenticationService;
	
	@RequestMapping(value="/deployment/findInfoDeploymentsProject", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoDeployments findInfoDeploymentsProjet(@RequestBody RequestVO request) {
		InfoRetornoDeployments info = new InfoRetornoDeployments();
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}			
		try {
			List<ProjetoImplantadoVO> lista = deploymentService.findInfoDeploymentsProjet();
			info.listaDeployments = lista;
			info.codigo = 0;
			info.mensagem = "Informações das implantações recuperadas com sucesso";
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			String msgErro = "Erro recuperando informações das implantações";
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
	
	
	@RequestMapping(value="/deployment/removeDeploymentFisico", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoDeployments removeDeploymentFisico(@RequestBody RequestVO request) {
		InfoRetornoDeployments info = new InfoRetornoDeployments();
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}			
		try {
			Long projetoId = Long.valueOf(request.parametros[0]);
			deploymentService.removeDeploymentFisico(projetoId);
			info.codigo = 0;
			info.mensagem = "Remoção realizada com sucesso";
		} catch (AuthenticationException e) {
			info.codigo = 2;
			info.mensagem = "Erro na chamada: " + e.getMessage();
		} catch(AccessDeniedException e) {
			info.codigo = 3;
			info.mensagem = "Acesso negado: privilégios insuficientes";
		} catch (Exception e) {
			info.codigo = 1;
			info.mensagem = "Erro ao remover deployment fisicamente: " + e.getMessage();
		}
		return info;
	}
	
	@RequestMapping(value="/deployment/findInstanciasPorProjeto", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoDeployments findInstanciasPorProjeto(@RequestBody RequestVO request) {
		InfoRetornoDeployments info = new InfoRetornoDeployments();
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}	
		try {
			Long projetoId = Long.parseLong(request.parametros[0]);
			List<InstanciaVO> instancias = deploymentService.findInstaniasPorProjeto(projetoId);
			info.instancias = instancias;
			info.codigo = 0;
			info.mensagem = "Informações de formulários recuperadas com sucesso";
		}  catch (AuthenticationException e) {
			info.codigo = 2;
			info.mensagem = "Erro na chamada: " + e.getMessage();
		} catch(AccessDeniedException e) {
			info.codigo = 3;
			info.mensagem = "Acesso negado: privilégios insuficientes";
		} catch (Exception e) {
			info.codigo = 1;
			info.mensagem = "Erro ao recuperar dados dos formulários para o projeto";
		}
		return info;
	}

	@RequestMapping(value="/deployment/findValoresDataviewPorId", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoInstancia findValoresDataviewPorId(@RequestBody RequestVO request) {
		InfoRetornoInstancia info = new InfoRetornoInstancia();
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}			
		try {
			Long dataviewId = Long.valueOf(request.parametros[0]);
			ValorDataview valor = deploymentService.findValoresDataviewPorId(dataviewId);
			info.valorDataview = valor;
			info.codigo = 0;
			info.mensagem = "Valores do dataview recuperados com sucesso";
		}  catch (AuthenticationException e) {
			info.codigo = 2;
			info.mensagem = "Erro na chamada: " + e.getMessage();
		} catch(AccessDeniedException e) {
			info.codigo = 3;
			info.mensagem = "Acesso negado: privilégios insuficientes";
		} catch (Exception e) {
			info.codigo = 1;
			info.mensagem = "Erro ao recuperar valores para o dataview";
		}
		return info;
	}

	@RequestMapping(value="/deployment/novaInstanciaProjeto", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoInstancia novaInstanciaUsuario(@RequestBody RequestVO request) {
		InfoRetornoInstancia info = new InfoRetornoInstancia();
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}		
		try {
			Long projetoId = Long.valueOf(request.parametros[0]);
			String loginUsuario = request.parametros[1];
			InstanciaVO vo = deploymentService.novaInstanciaParaProjeto(projetoId, loginUsuario);
			info.instancia = vo;
			info.codigo = 0;
			info.mensagem = "Instancia criada com sucesso";
		}  catch (AuthenticationException e) {
			info.codigo = 2;
			info.mensagem = "Erro na chamada: " + e.getMessage();
		} catch(AccessDeniedException e) {
			info.codigo = 3;
			info.mensagem = "Acesso negado: privilégios insuficientes";
		} catch (Exception e) {
			info.codigo = 1;
			info.mensagem = "Erro ao criar instancia";
		}
		return info;
	}	
	
	
	@RequestMapping(value="/deployment/novaInstanciaLinhaDataview", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoInstancia novaInstanciaLinhaDataview(@RequestBody RequestVO request) {
		InfoRetornoInstancia info = new InfoRetornoInstancia();
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}	
		try {
			Long dataviewId = Long.valueOf(request.parametros[0]);
			ValorDataview vo = deploymentService.novaInstanciaLinhaDataview(dataviewId);
			info.valorDataview = vo;
			info.codigo = 0;
			info.mensagem = "Linha de campo de dados criada com sucesso";
		}  catch (AuthenticationException e) {
			info.codigo = 2;
			info.mensagem = "Erro na chamada: " + e.getMessage();
		} catch(AccessDeniedException e) {
			info.codigo = 3;
			info.mensagem = "Acesso negado: privilégios insuficientes";
		} catch (Exception e) {
			info.codigo = 1;
			info.mensagem = "Erro ao criar linha de campo de dados: " + e.getMessage();
		}
		return info;
	}		
	
	@RequestMapping(value="/deployment/removeLinhaInstanciaDataview", method=RequestMethod.DELETE)
	@ResponseBody
	public InfoRetornoInstancia removeLinhaInstanciaDataview(@RequestBody RequestVO request) {
		InfoRetornoInstancia info = new InfoRetornoInstancia();
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}			
		try {
			Long linhaId = Long.valueOf(request.parametros[0]);
	        deploymentService.removeLinhaInstanciaDataview(linhaId);
	        info.codigo = 0;
	        info.mensagem = "Remoção realizada com sucesso";
        }  catch (AuthenticationException e) {
			info.codigo = 2;
			info.mensagem = "Erro na chamada: " + e.getMessage();
		} catch(AccessDeniedException e) {
			info.codigo = 3;
			info.mensagem = "Acesso negado: privilégios insuficientes";
		} catch (Exception e) {
        	info.codigo = 1;
        	info.mensagem = "Erro ao executar operação de remoção: " + e.getMessage();
        }
		return info;
	}

	@RequestMapping(value="/deployment/removeInstancia", method=RequestMethod.DELETE)
	@ResponseBody
	public InfoRetornoInstancia removeInstancia(@RequestBody RequestVO request) {
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
	        deploymentService.removeInstancia(instanciaId);
	        info.codigo = 0;
	        info.mensagem = "Remoção realizada com sucesso";
        }  catch (AuthenticationException e) {
			info.codigo = 2;
			info.mensagem = "Erro na chamada: " + e.getMessage();
		} catch(AccessDeniedException e) {
			info.codigo = 3;
			info.mensagem = "Acesso negado: privilégios insuficientes";
		} catch (Exception e) {
        	info.codigo = 1;
        	info.mensagem = "Erro ao executar operação de remoção: " + e.getMessage();
        }
		return info;
	}	
	
	@RequestMapping(value="/deployment/alteraLinhasInstanciaDataview", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoInstancia alteraValorDataview(@RequestBody DataViewWrapperVO valorDataview) {
		InfoRetornoInstancia info = new InfoRetornoInstancia();
		try {
			authenticationService.validaToken(valorDataview.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}		
		try {
			ValorDataview unValorDataview = valorDataview.valorDataview;
	        deploymentService.alteraValoresDataview(unValorDataview);
	        info.codigo = 0;
	        info.mensagem = "Valores do campo de dados alterado com sucesso";
        }  catch (AuthenticationException e) {
			info.codigo = 2;
			info.mensagem = "Erro na chamada: " + e.getMessage();
		} catch(AccessDeniedException e) {
			info.codigo = 3;
			info.mensagem = "Acesso negado: privilégios insuficientes";
		} catch (Exception e) {
        	info.codigo = 1;
        	info.mensagem = "Erro ao alterar valores do campo de dados: " + e.getMessage();
        }
		return info;
	}


	@RequestMapping(value="/deployment/alteraLinhasInstancias", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoInstancia alteraLinhasInstancia(@RequestBody InstanciasVO instancias) {
		InfoRetornoInstancia info = new InfoRetornoInstancia();
		try {
			authenticationService.validaToken(instancias.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}			
		try {
	        deploymentService.alteraValoresInstancias(instancias);
	        info.codigo = 0;
	        info.mensagem = "Valores dos formulários alterados com sucesso";
        }  catch (AuthenticationException e) {
			info.codigo = 2;
			info.mensagem = "Erro na chamada: " + e.getMessage();
		} catch(AccessDeniedException e) {
			info.codigo = 3;
			info.mensagem = "Acesso negado: privilégios insuficientes";
		} catch (Exception e) {
        	info.codigo = 1;
        	info.mensagem = "Erro ao alterar valores dos formulários: " + e.getMessage();
        }
		return info;
	}}
