package br.com.laminarsoft.jazzforms.negocio.controller;

import java.io.StringReader;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.laminarsoft.jazzforms.logging.types.LoggingErrorMSG;
import br.com.laminarsoft.jazzforms.logging.types.MobileLoggingMSG;
import br.com.laminarsoft.jazzforms.negocio.controller.security.AuthenticationService;
import br.com.laminarsoft.jazzforms.negocio.controller.util.LDAPServiceController;
import br.com.laminarsoft.jazzforms.negocio.controller.util.LogController;
import br.com.laminarsoft.jazzforms.persistencia.model.Deployment;
import br.com.laminarsoft.jazzforms.persistencia.model.Grupo;
import br.com.laminarsoft.jazzforms.persistencia.model.Historico;
import br.com.laminarsoft.jazzforms.persistencia.model.Instancia;
import br.com.laminarsoft.jazzforms.persistencia.model.Pagina;
import br.com.laminarsoft.jazzforms.persistencia.model.Projeto;
import br.com.laminarsoft.jazzforms.persistencia.model.Sugestao;
import br.com.laminarsoft.jazzforms.persistencia.model.Usuario;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.IMensagensErro;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.ParametroException;
import br.com.laminarsoft.jazzforms.persistencia.model.util.InfoRetornoCriacaoUsuarioPublico;
import br.com.laminarsoft.jazzforms.persistencia.model.util.InfoRetornoDadosAtualizacao;
import br.com.laminarsoft.jazzforms.persistencia.model.util.InfoRetornoDeployments;
import br.com.laminarsoft.jazzforms.persistencia.model.util.InfoRetornoInstancia;
import br.com.laminarsoft.jazzforms.persistencia.model.util.InfoRetornoLands;
import br.com.laminarsoft.jazzforms.persistencia.model.util.InfoRetornoMensagem;
import br.com.laminarsoft.jazzforms.persistencia.model.util.InfoRetornoUsuario;
import br.com.laminarsoft.jazzforms.persistencia.model.util.InfoRetornoValorBPInstance;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.EquipamentoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.InstanciasWrapperVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LandVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LdapUsuarioVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.RequestVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.RequisicaoAtualizacaoLocalizacaoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.RequisicaoAtualizacoesVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.SugestaoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.UsuarioAlertaVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.UsuarioVO;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

@Controller
@RequestMapping("/servicos/senchaService")
@SuppressWarnings("all")
public class ManterSenchaRequestsController {

	@Autowired private ResourceBundle bundle;
	@Autowired private ManterSenchaRequestsService senchaService;
	@Autowired private LDAPServiceController ldapServiceController;
	@Autowired private LogController logController;
	@Autowired private ManterLdapService ldapService;
	@Autowired private AuthenticationService authenticationService;
	@Autowired private ManterLdapService manterLdapService;
	
	
	@RequestMapping(value="/usuario/autenticate", method=RequestMethod.POST, consumes = "application/javascript", produces = "application/javascript")
	public InfoRetornoUsuario autenticaUsuarioJson(@RequestBody Usuario usuario) {
		InfoRetornoUsuario infoUsuario = new InfoRetornoUsuario();
		infoUsuario.codigo = 0;
		infoUsuario.mensagem = "Usuário autenticado com sucesso";		
		return infoUsuario;
	}
	
	@RequestMapping(value="/usuario/userByLogin", method=RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public InfoRetornoUsuario getUsuario(@RequestBody String usuarioJson) {
		InfoRetornoUsuario infoUsuario = new InfoRetornoUsuario();
		
		usuarioJson = new String(usuarioJson.getBytes(), Charset.forName("ISO-8859-1"));

		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(usuarioJson));
		reader.setLenient(true);
		UsuarioVO usrVo = null;
		try {
			usrVo = gson.fromJson(reader, UsuarioVO.class);	        
	        
			LdapUsuarioVO ldapusr = ldapService.autenticaUsuario(usrVo.lgn, usrVo.sn);
			
//			boolean usuarioAtivo = senchaService.isUsuarioAtivo(usrVo.lgn);
			if(!ldapusr.isAtivo()) {
				throw new ParametroException(IMensagensErro.USUARIO_INATIVO_MSG, IMensagensErro.USUARIO_INATIVO_CODE);
			}
			
			senchaService.atualizarRelacionamentosUsuario(ldapusr);
			Usuario usr = senchaService.getUsuarioPorLogin(ldapusr.getLogin());
			usr.setMatricula(usrVo.lgn);
			
			infoUsuario.codigo = 0;
			infoUsuario.mensagem = "Usuário autenticado com sucesso";
			infoUsuario.usuario = usr;
			infoUsuario.token = authenticationService.criaToken(usrVo.lgn, usrVo.sn);
			if (StringUtils.isEmpty(infoUsuario.token)) {
				infoUsuario.codigo = 2;
				infoUsuario.mensagem = "Não foi possível gerar o token";
			}			
        } catch(ParametroException e) {
        	infoUsuario.codigo = e.getCodigoErro();
        	infoUsuario.mensagem = e.getMessage();
        } catch (Exception e) {
        	infoUsuario.codigo = 1;
        	infoUsuario.mensagem = "Erro ao buscar usuário: (" + (usrVo != null ? usrVo.lgn : "" ) +")";
        	e.printStackTrace();
        }		
		return infoUsuario;
	}
	
	
	@RequestMapping(value="/usuario/permisoesByLogin", method=RequestMethod.POST)
	public InfoRetornoUsuario getPermissoesByLogin(@RequestBody String usuarioJson) {
		InfoRetornoUsuario infoUsuario = new InfoRetornoUsuario();
		
		usuarioJson = new String(usuarioJson.getBytes(), Charset.forName("ISO-8859-1"));

		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(usuarioJson));
		reader.setLenient(true);
		UsuarioVO usrVo = null;
		try {
			usrVo = gson.fromJson(reader, UsuarioVO.class);	        
	        authenticationService.validaToken(usrVo.token);
	        LdapUsuarioVO usrLdap = ldapService.findUsuario(usrVo.lgn);
	        
	        infoUsuario.infoUsuario = senchaService.getPermissoesByLogin(usrLdap.getLogin());
	        infoUsuario.codigo = 0;
	        infoUsuario.mensagem = "Informações de usuario recuperadas com sucesso";
	        
        } catch (Exception e) {
        	infoUsuario.codigo = 1;
        	infoUsuario.mensagem = "Erro ao buscar usuário: (" + (usrVo != null ? usrVo.lgn : "" ) +")";
        	e.printStackTrace();
        }		
		return infoUsuario;
	}	

	@RequestMapping(value="/usuario/deploymentsByLogin", method=RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public InfoRetornoDeployments getProjetosImplantadosPorUsuario(@RequestBody String requestInfo) {
		InfoRetornoDeployments info = new InfoRetornoDeployments();
		String login = "";
		
		requestInfo = new String(requestInfo.getBytes(), Charset.forName("ISO-8859-1"));
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(requestInfo));
		reader.setLenient(true);
		RequestVO request = null;

		try {
	        request = gson.fromJson(reader, RequestVO.class);
	        
	        authenticationService.validaToken(request.token);
	        
	        if(request.parametros == null || request.parametros.length != 1) {
	        	String mensagem = MessageFormat.format(IMensagensErro.PARAMETRO_QUANTIDADE_ERRADA_MSG, "1");
	        	throw new ParametroException(mensagem, IMensagensErro.PARAMETRO_QUANTIDADE_ERRADA_CODE);
	        }
	        
	        login = request.parametros[0];
	        LdapUsuarioVO ldapusr = ldapService.findUsuario(login);
	        
	        List<Deployment> lstClnDep = new ArrayList<Deployment>();
	        List<Deployment> lstDeployment = senchaService.findDeploymentsByLogin(ldapusr.getLogin());
	        for (Deployment depo : lstDeployment) {
	        	
	        	Deployment depc = senchaService.getDeploymentById(depo.getId()); 
	        	Deployment dep = preparaDeploymentEnvio(depc);
	        	Projeto prj = dep.getProjeto();
	        	dep.descricaoProjeto = prj.getDescricao();
	        	dep.nomeProjeto = prj.getNome();
	        	dep.projetoAplicacao = prj.getAplicacao() != null && prj.getAplicacao() ? 1 : 0;
	        	lstClnDep.add(dep);
	        }
			info.codigo = 0;
			info.mensagem = "Deployments recuperados com sucesso";
			info.deployments = lstClnDep;
        } catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		} catch (Exception e) {
        	info.codigo = 1;
        	info.mensagem = "Erro ao localizar deployments por usuário: " + e.getMessage();
        	info.deployments = null;
        	
        	sendMessageError("sencha.deploymentsByLogin", "Erro ao localizar deployments por usuário: " + e.getMessage(), "{usuario: " + login + "}");
        }
		
		return info;
	}
	
	
	@RequestMapping(value="/usuario/getLandsByLogin", method=RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public InfoRetornoLands getLandsByLogin(@RequestBody String requestInfo) {
		InfoRetornoLands info = new InfoRetornoLands();
		String login = "";
		
		requestInfo = new String(requestInfo.getBytes(), Charset.forName("ISO-8859-1"));
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(requestInfo));
		reader.setLenient(true);
		RequestVO request = null;

		try {
	        request = gson.fromJson(reader, RequestVO.class);
	        
	        authenticationService.validaToken(request.token);	        
	        
	        if(request.parametros == null || request.parametros.length != 1) {
	        	String mensagem = MessageFormat.format(IMensagensErro.PARAMETRO_QUANTIDADE_ERRADA_MSG, "1");
	        	throw new ParametroException(mensagem, IMensagensErro.PARAMETRO_QUANTIDADE_ERRADA_CODE);
	        }
	        
	        login = request.parametros[0];
	        LdapUsuarioVO usrLdap = ldapService.findUsuario(login);
	        
	        List<LandVO> lands = senchaService.getLandsPorUsuario(usrLdap.getLogin());
	        info.landItems = lands;
			info.codigo = 0;
			info.mensagem = "Lands recuperadas com sucesso";
			
			sendMessageAcompanhamento("getLandsByLogin", login, "{login: '"+ login +"' }");
        } catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		} catch (Exception e) {
        	info.codigo = 1;
        	info.mensagem = "Erro ao localizar deployments por usuário: " + e.getMessage();
        }
		
		return info;
	}	
	
	
	
	@RequestMapping(value="/usuario/listLandsByLogin", method=RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public InfoRetornoLands listLandsByLogin(@RequestBody String requestInfo) {
		InfoRetornoLands info = new InfoRetornoLands();
		String login = "";
		
		requestInfo = new String(requestInfo.getBytes(), Charset.forName("ISO-8859-1"));
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(requestInfo));
		reader.setLenient(true);
		RequestVO request = null;

		try {
	        request = gson.fromJson(reader, RequestVO.class);
	        
	        authenticationService.validaToken(request.token);	        
	        
	        if(request.parametros == null || request.parametros.length != 1) {
	        	String mensagem = MessageFormat.format(IMensagensErro.PARAMETRO_QUANTIDADE_ERRADA_MSG, "1");
	        	throw new ParametroException(mensagem, IMensagensErro.PARAMETRO_QUANTIDADE_ERRADA_CODE);
	        }
	        
	        login = request.parametros[0];
	        
	        info.landItems.add(new LandVO(1l, "jazzmoon-world", "ATOS NORMATIVOS", "ATOS NORMATIVOS", "http://www.tjdft.jus.br/institucional/corregedoria/atos-normativos", (new Date()).getTime(), "Institucional"));
	        info.landItems.add(new LandVO(2l, "jazzmoon-world", "CERTIDÃO DE NADA CONSTA", "CERTIDÃO DE NADA CONSTA", "http://www.tjdft.jus.br/servicos/certidao-nada-consta", (new Date()).getTime(), "Institucional"));
	        info.landItems.add(new LandVO(3l, "jazzmoon-world", "CORREIÇÕES JUDICIAIS", "CORREIÇÕES JUDICIAIS", "http://www.tjdft.jus.br/institucional/corregedoria/correicoes-judiciais", (new Date()).getTime(), "Institucional"));
	        info.landItems.add(new LandVO(4l, "jazzmoon-world", "ESTRUTURA ADMINISTRATIVA", "ESTRUTURA ADMINISTRATIVA", "http://www.tjdft.jus.br/institucional/corregedoria/estrutura-administrativa-da-corregedoria", (new Date()).getTime(), "Institucional"));
	        info.landItems.add(new LandVO(5l, "jazzmoon-world", "ESTRUTURA NOS FÓRUNS", "ESTRUTURA NOS FÓRUNS", "http://www.tjdft.jus.br/institucional/corregedoria/estrutura-administrativas-nos-foruns", (new Date()).getTime(), "Institucional"));
	        info.landItems.add(new LandVO(6l, "jazzmoon-world", "INFÂNCIA E JUVENTUDE", "INFÂNCIA E JUVENTUDE", "http://www.tjdft.jus.br/cidadaos/infancia-e-juventude", (new Date()).getTime(), "Institucional"));
	        info.landItems.add(new LandVO(7l, "jazzmoon-world", "JUIZADOS ESPECIAIS", "JUIZADOS ESPECIAIS", "http://www.tjdft.jus.br/cidadaos/juizados-especiais", (new Date()).getTime(), "Institucional"));
	        info.landItems.add(new LandVO(8l, "jazzmoon-world", "EXTRAJUDICIAIS", "EXTRAJUDICIAIS", "http://www.tjdft.jus.br/cidadaos/extrajudicial", (new Date()).getTime(), "Institucional"));

			info.codigo = 0;
			info.mensagem = "Lands recuperadas com sucesso";
        } catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		} catch (Exception e) {
        	info.codigo = 1;
        	info.mensagem = "Erro ao localizar deployments por usuário: " + e.getMessage();
        }
		
		return info;
	}	
	
	@RequestMapping(value="/usuario/deploymentById", method=RequestMethod.POST)
	public InfoRetornoDeployments getProjetoImplantadoPorId(@RequestBody String requestInfo) {
		InfoRetornoDeployments info = new InfoRetornoDeployments();
		
		requestInfo = new String(requestInfo.getBytes(), Charset.forName("ISO-8859-1"));
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(requestInfo));
		reader.setLenient(true);
		RequestVO request = null;

		try {
	        request = gson.fromJson(reader, RequestVO.class);
	        
	        authenticationService.validaToken(request.token);

	        if(request.parametros == null || request.parametros.length != 1) {
	        	String mensagem = MessageFormat.format(IMensagensErro.PARAMETRO_QUANTIDADE_ERRADA_MSG, "1");
	        	throw new ParametroException(mensagem, IMensagensErro.PARAMETRO_QUANTIDADE_ERRADA_CODE);
	        }
	        
	        
	        Long deploymentId = Long.valueOf(request.parametros[0]);

	        Deployment dep = senchaService.getDeploymentById(deploymentId);
	        
			Deployment depRet = preparaDeploymentEnvio(dep);

	        info.deployment = depRet;
	        info.codigo = 0;
	        info.mensagem = "Deployment recuperado com sucesso";
        } catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		} catch (Exception e) {
        	e.printStackTrace();
        	info.codigo = 1;
        	info.mensagem = "Erro ao recuperar deployment: " + e.getMessage();
        	info.deployment = null;        	
        }
		return info;
	}

	private Deployment preparaDeploymentEnvio(Deployment dep) {
		Projeto p = dep.getProjeto().clone();
		Deployment depRet = p.getDeployment(); 
		depRet.getProjeto().setHistoricos(new ArrayList<Historico>());
		depRet.getProjeto().setDeployment(null);
		depRet.getProjeto().setProjetoBase(null);
		depRet.getProjeto().getPaginas().removeAll(Collections.singleton(null));
		for(Pagina pag : depRet.getProjeto().getPaginas()) {
			pag.setProjeto(null);
			pag.paginaJsonTransformado = new String(pag.getPaginaJson());
			pag.pacoteCodigoCustomizadoTransformado = (pag.getPacoteCodigoCustomizacao() != null && pag.getPacoteCodigoCustomizacao().length > 0 ?  
					new String(pag.getPacoteCodigoCustomizacao()) : null);
		}
		depRet.setUsuariosPossiveis(new ArrayList<Usuario>());
		depRet.setGruposPossiveis(new ArrayList<Grupo>());
		
		depRet.descricaoProjeto = p.getDescricao();
		depRet.nomeProjeto = p.getNome();
		depRet.idProjeto = p.getId();
		depRet.projetoAplicacao = p.getAplicacao() ? 1 : 0;
		
		return depRet;
	}	
	
	@RequestMapping(value="/usuario/messagesByLogin", method=RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public InfoRetornoMensagem getMensagensPorUsuario(@RequestBody String requestInfo) {
		InfoRetornoMensagem info = new InfoRetornoMensagem();
		
		requestInfo = new String(requestInfo.getBytes(), Charset.forName("ISO-8859-1"));
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(requestInfo));
		reader.setLenient(true);
		RequestVO request = null;

        String login = "";
        String equipamentoUUID = "";
        Long dataUltimaMensagem = 0l;
		try {
	        request = gson.fromJson(reader, RequestVO.class);
	        authenticationService.validaToken(request.token);
	        if(request.parametros == null || request.parametros.length != 3) {
	        	String mensagem = MessageFormat.format(IMensagensErro.PARAMETRO_QUANTIDADE_ERRADA_MSG, "3");
	        	throw new ParametroException(mensagem, IMensagensErro.PARAMETRO_QUANTIDADE_ERRADA_CODE);
	        }
	        
	        
	        login = request.parametros[0];
	        LdapUsuarioVO usrLdap = ldapService.findUsuario(login);
	        equipamentoUUID = request.parametros[1];
	        dataUltimaMensagem = Long.valueOf(request.parametros[2]);

	        info.deviceMessages = senchaService.getMensagensPorUsuario(usrLdap.getLogin(), equipamentoUUID, dataUltimaMensagem);
			info.codigo = 0;
			info.mensagem = "Mensagens recuperadas com sucesso";
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		} catch(Exception e) {
			info.codigo = 1;
			info.mensagem = "Erro ao recuperar mensagens por usuário: " + e.getMessage();
			
			sendMessageError("sencha.messagesByLogin", "Erro ao recuperar mensagens por usuário: " + e.getMessage(), 
					"{usuario: " + login + ", equipamentoUUID: " + equipamentoUUID + ", dataUltimaMensagem: " + dataUltimaMensagem +  "}");			
		}
		return info;
	}
	
	@RequestMapping(value="/usuario/atribuiTarefaMensagem", method=RequestMethod.POST)
	public InfoRetornoMensagem atribuiTarefaMensagem(@RequestBody String requestInfo) {
		InfoRetornoMensagem info = new InfoRetornoMensagem();
		
		requestInfo = new String(requestInfo.getBytes(), Charset.forName("ISO-8859-1"));
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(requestInfo));
		reader.setLenient(true);
		RequestVO request = null;
		
		try {
	        request = gson.fromJson(reader, RequestVO.class);	        
	        authenticationService.validaToken(request.token);		

	        if(request.parametros == null || request.parametros.length != 2) {
	        	String mensagem = MessageFormat.format(IMensagensErro.PARAMETRO_QUANTIDADE_ERRADA_MSG, "2");
	        	throw new ParametroException(mensagem, IMensagensErro.PARAMETRO_QUANTIDADE_ERRADA_CODE);
	        }
	        
	        Long idMensagem = Long.valueOf(request.parametros[0]);
	        String loginUsuario = request.parametros[1];
	        LdapUsuarioVO usrLdap = ldapService.findUsuario(loginUsuario);
			senchaService.atribuiTarefaMensagem(idMensagem, usrLdap.getLogin());
			info.codigo = 0;
			info.mensagem = "Mensagem atribuída com sucesso";
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
		} catch (Exception e) {
			info.codigo = 1;
			info.mensagem = e.getMessage();
		}
		return info;
		
	}
	
	
	/**
	 * Recupera algumas informações da atribuição de uma determinada tarefa
	 * @param idMensagem Identificação única da mensagem de tarefa
	 * @return InfoRetornoMensagem com um dos campos <i>tarefaAtribuida</i> preenchido com uma string JSON contendo informações da atribuição {tarefaAtribuida: true/false, usuarioAtribuido: loginusuario}
	 */
	@RequestMapping(value="/usuario/isTarefaAtribuida", method=RequestMethod.POST)
	public InfoRetornoMensagem isTarefaAtribuida(@RequestBody String requestInfo) {
		InfoRetornoMensagem info = new InfoRetornoMensagem();
		requestInfo = new String(requestInfo.getBytes(), Charset.forName("ISO-8859-1"));
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(requestInfo));
		reader.setLenient(true);
		RequestVO request = null;
		
		try {
	        request = gson.fromJson(reader, RequestVO.class);	        
	        authenticationService.validaToken(request.token);	      
	        if(request.parametros == null || request.parametros.length != 1) {
	        	String mensagem = MessageFormat.format(IMensagensErro.PARAMETRO_QUANTIDADE_ERRADA_MSG, "1");
	        	throw new ParametroException(mensagem, IMensagensErro.PARAMETRO_QUANTIDADE_ERRADA_CODE);
	        }
	        
	        Long idMensagem = Long.valueOf(request.parametros[0]);
	        
			info.tarefaAtribuida = senchaService.isTarefaAtribuida(idMensagem); 
			info.codigo = 0;
			info.mensagem = "Recuperação do status de atribuição com sucesso";
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
		} catch (Exception e) {
			info.codigo = 1;
			info.mensagem = e.getMessage();
		}
		return info;
	}	
	
	@RequestMapping(value="/usuario/valoresBPInstance", method=RequestMethod.POST)
	public InfoRetornoValorBPInstance getValoresBPInstance(@RequestBody String requestInfo) {
		InfoRetornoValorBPInstance info = new InfoRetornoValorBPInstance();

		requestInfo = new String(requestInfo.getBytes(), Charset.forName("ISO-8859-1"));
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(requestInfo));
		reader.setLenient(true);
		RequestVO request = null;

		try {
	        request = gson.fromJson(reader, RequestVO.class);
	        authenticationService.validaToken(request.token);
	        if(request.parametros == null || request.parametros.length != 1) {
	        	String mensagem = MessageFormat.format(IMensagensErro.PARAMETRO_QUANTIDADE_ERRADA_MSG, "1");
	        	throw new ParametroException(mensagem, IMensagensErro.PARAMETRO_QUANTIDADE_ERRADA_CODE);
	        }
	        Long bpInstanceId = Long.valueOf(request.parametros[0]);
	        
			info.valores = senchaService.getValoresBPInstance(bpInstanceId);
			info.codigo = 0;
			info.mensagem = "Valores recuperados com sucesso";
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
		} catch (Exception e) {
			info.codigo = 1;
			info.mensagem = "Erro ao recuperar valores: " + e.getMessage();
		}
		return info;
	}
	
	@RequestMapping(value="/usuario/situacaoDeployment", method=RequestMethod.POST)
	public InfoRetornoDeployments getSituacaoDeployment(@RequestBody String requestInfo) {
		InfoRetornoDeployments info = new InfoRetornoDeployments();
		
		requestInfo = new String(requestInfo.getBytes(), Charset.forName("ISO-8859-1"));
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(requestInfo));
		reader.setLenient(true);
		RequestVO request = null;

		try {
	        request = gson.fromJson(reader, RequestVO.class);	        
	        authenticationService.validaToken(request.token);	        
	        if(request.parametros == null || request.parametros.length != 1) {
	        	String mensagem = MessageFormat.format(IMensagensErro.PARAMETRO_QUANTIDADE_ERRADA_MSG, "1");
	        	throw new ParametroException(mensagem, IMensagensErro.PARAMETRO_QUANTIDADE_ERRADA_CODE);
	        }
	        Long deploymentId = Long.valueOf(request.parametros[0]);
		
	        info.situacao = senchaService.getSituacaoDeployment(deploymentId);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;			
		}
		return info;
	}

	@RequestMapping(value="/instancia/instanciasReenviadasUsuario", method=RequestMethod.POST)
	public InfoRetornoInstancia findInstanciasReenviadasUsuario(@RequestBody String requestInfo) {
		InfoRetornoInstancia info = new InfoRetornoInstancia();

		requestInfo = new String(requestInfo.getBytes(), Charset.forName("ISO-8859-1"));
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(requestInfo));
		reader.setLenient(true);
		RequestVO request = null;

		try {
	        request = gson.fromJson(reader, RequestVO.class);	        
	        authenticationService.validaToken(request.token);
	        if(request.parametros == null || request.parametros.length != 1) {
	        	String mensagem = MessageFormat.format(IMensagensErro.PARAMETRO_QUANTIDADE_ERRADA_MSG, "1");
	        	throw new ParametroException(mensagem, IMensagensErro.PARAMETRO_QUANTIDADE_ERRADA_CODE);
	        }
	        String loginUsuario = request.parametros[0];
	        LdapUsuarioVO usrLdap = ldapService.findUsuario(loginUsuario);

	        info.instancias = senchaService.getInstanciasReenviadas(usrLdap.getLogin());
			info.codigo = 0;
			info.mensagem = "Instancias recuperadas com sucesso";
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;			
		} catch(Exception e) {
			e.printStackTrace();
			info.codigo = 1;
			info.mensagem = "Erro ao recuperar instancias: " + e.getMessage();
		}
		return info;
	}
	
	@RequestMapping(value="/instancia/reportProjetoInexistente", method=RequestMethod.POST)
	public InfoRetornoInstancia reportProjetoInexistente(@RequestBody String requestInfo) {
		InfoRetornoInstancia info = new InfoRetornoInstancia();

		requestInfo = new String(requestInfo.getBytes(), Charset.forName("ISO-8859-1"));
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(requestInfo));
		reader.setLenient(true);
		RequestVO request = null;

		try {
	        request = gson.fromJson(reader, RequestVO.class);	        
	        authenticationService.validaToken(request.token);
	        if(request.parametros == null || request.parametros.length != 2) {
	        	String mensagem = MessageFormat.format(IMensagensErro.PARAMETRO_QUANTIDADE_ERRADA_MSG, "2");
	        	throw new ParametroException(mensagem, IMensagensErro.PARAMETRO_QUANTIDADE_ERRADA_CODE);
	        }
	        Long idInstancia = Long.valueOf(request.parametros[0]);
	        String loginUsuario = request.parametros[1];
	        LdapUsuarioVO usrLdap = ldapService.findUsuario(loginUsuario);

	        senchaService.reportProjetoInexistente(idInstancia, usrLdap.getLogin());
			info.codigo = 0;
			info.mensagem = "Reporte realizado com sucesso";
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;			
		} catch(Exception e) {
			info.codigo = 1;
			info.mensagem = "Erro ao reportar projeto inexistente no aparelho";
		}
		return info;
	}
	
	@RequestMapping(value="/instancia/removeMensagemUsuario", method=RequestMethod.POST)
	public InfoRetornoMensagem removeMensagemUsuario(@RequestBody String requestInfo) {
		InfoRetornoMensagem info = new InfoRetornoMensagem();
		
		requestInfo = new String(requestInfo.getBytes(), Charset.forName("ISO-8859-1"));
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(requestInfo));
		reader.setLenient(true);
		RequestVO request = null;

		try {
	        request = gson.fromJson(reader, RequestVO.class);	        
	        authenticationService.validaToken(request.token);

	        if(request.parametros == null || request.parametros.length != 2) {
	        	String mensagem = MessageFormat.format(IMensagensErro.PARAMETRO_QUANTIDADE_ERRADA_MSG, "2");
	        	throw new ParametroException(mensagem, IMensagensErro.PARAMETRO_QUANTIDADE_ERRADA_CODE);
	        }
	        Long idMensagem = Long.valueOf(request.parametros[0]);
	        String loginUsuario = request.parametros[1];
	        LdapUsuarioVO usrLdap = ldapService.findUsuario(loginUsuario);

	        info = senchaService.removeMensagemUsuario(idMensagem, usrLdap.getLogin());
			info.codigo = 0;
			info.mensagem = "Mensagem para usuário removida com sucesso";
			sendMessageAcompanhamento("removeMensagemUsuario", loginUsuario, "{idMensagem: } "+idMensagem+"}");
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;			
		} catch (Exception e) {
			info.codigo = 1;
			info.mensagem = "Erro ao remover mensagem para o usuário: " + e.getMessage();
		}
		return info;
	}
	
	@RequestMapping(value="/alerta/adicionaUsuarioAlerta", method=RequestMethod.POST)
	public InfoRetornoMensagem addUsuarioAlerta(@RequestBody String  requestInfo) {
		InfoRetornoMensagem info = new InfoRetornoMensagem();
		requestInfo = new String(requestInfo.getBytes(), Charset.forName("ISO-8859-1"));
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(requestInfo));
		reader.setLenient(true);
		RequestVO request = null;

		try {
	        request = gson.fromJson(reader, RequestVO.class);	        
	        authenticationService.validaToken(request.token);

	        if(request.parametros == null || request.parametros.length != 4) {
	        	String mensagem = MessageFormat.format(IMensagensErro.PARAMETRO_QUANTIDADE_ERRADA_MSG, "4");
	        	throw new ParametroException(mensagem, IMensagensErro.PARAMETRO_QUANTIDADE_ERRADA_CODE);
	        }
	        String loginUsuario = request.parametros[0];
	        String idMsgUsuario = request.parametros[1];
	        String tipoAparelho = request.parametros[2];
	        String serialAparelho = request.parametros[3];
	        
	        UsuarioAlertaVO usr = new UsuarioAlertaVO();

	        LdapUsuarioVO usrLdap = ldapService.findUsuario(loginUsuario);

	        usr.loginUsuario = usrLdap.getLogin();
	        usr.idMsgUsuario = idMsgUsuario;
	        usr.tipoAparelho = tipoAparelho;
	        usr.serialAparelho = serialAparelho;
	        usr.dhCadastro = new Date();
	        
	        senchaService.addUsuarioAlerta(usr);	        
			info.codigo = 0;
			info.mensagem = "Usuario destino de mensagem cadastrado com sucesso";
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;			
		} catch (Exception e) {
			info.codigo = 1;
			info.mensagem = "Erro ao cadastrar usuário destinatário de mensagem: " + e.getMessage();
		}
		return info;
	}
	
	
	@RequestMapping(value="/alerta/notificaRecebimentoAlerta", method=RequestMethod.POST)
	public InfoRetornoMensagem notificaRecebimentoAlerta(@RequestBody String  requestInfo) {
		InfoRetornoMensagem info = new InfoRetornoMensagem();
		requestInfo = new String(requestInfo.getBytes(), Charset.forName("ISO-8859-1"));
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(requestInfo));
		reader.setLenient(true);
		RequestVO request = null;

		try {
	        request = gson.fromJson(reader, RequestVO.class);	        
	        authenticationService.validaToken(request.token);
	        if(request.parametros == null || request.parametros.length != 1) {
	        	String mensagem = MessageFormat.format(IMensagensErro.PARAMETRO_QUANTIDADE_ERRADA_MSG, "1");
	        	throw new ParametroException(mensagem, IMensagensErro.PARAMETRO_QUANTIDADE_ERRADA_CODE);
	        }

	        String loginUsuario = request.parametros[0];
	        Long idMensagem = Long.valueOf(request.parametros[1]);
	        
	        senchaService.removeAlerta(idMensagem);
			info.codigo = 0;
			info.mensagem = "Mensagem apagada com sucesso";
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;			
		} catch (Exception e) {
			info.codigo = 1;
			info.mensagem = "Erro ao apagar mensagem: " + e.getMessage();
		}
		return info;
	}	
	
	@RequestMapping(value="/usuario/publicaInstancia", method=RequestMethod.POST)
	public InfoRetornoInstancia publicaInstancia(@RequestBody String instanciasWrapper) {
		InfoRetornoInstancia info = new InfoRetornoInstancia();
		info.codigo = 0;
		info.mensagem = "Formulários recuperados com sucesso.";
		
		instanciasWrapper = new String(instanciasWrapper.getBytes(), Charset.forName("ISO-8859-1"));

		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(instanciasWrapper));
		reader.setLenient(true);
		
		InstanciasWrapperVO wrapper = null;
		Instancia[] agrpInstancias = null;
		try {
			wrapper = gson.fromJson(reader, InstanciasWrapperVO.class);
			authenticationService.validaToken(wrapper.token);
	        agrpInstancias = wrapper.instancias;
	        for(int idx = 0; idx < agrpInstancias.length; idx++) {
	        	agrpInstancias[idx].setDhCriacaoP(new Date((agrpInstancias[idx].getDhCriacao() == null ? System.currentTimeMillis() : agrpInstancias[idx].getDhCriacao())));
	        	agrpInstancias[idx].setDhAlteracaoP((agrpInstancias[idx].getDhAlteracao() == null ? null : new Date(agrpInstancias[idx].getDhAlteracao())));
	        }
	        
	        senchaService.persistInstancias(agrpInstancias);
        } catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;			
		} catch (Exception e) {
        	info.codigo = 1;
        	info.mensagem = "Erro ao recuperar informações dos formulários: " + e.getMessage();
	        e.printStackTrace();
        }	
		return info;
	}
		
	@RequestMapping(value="/usuario/enviaInfoEquipamento", method=RequestMethod.POST)
	public InfoRetornoInstancia enviaInfoEquipamento(@RequestBody String equipamento) {
		InfoRetornoInstancia info = new InfoRetornoInstancia();
		info.codigo = 0;
		info.mensagem = "Info equipamento enviada com sucesso.";
		
		equipamento = new String(equipamento.getBytes(), Charset.forName("ISO-8859-1"));

		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(equipamento));
		reader.setLenient(true);
		EquipamentoVO equipamentoVo = null;
		try {
	        equipamentoVo = gson.fromJson(reader, EquipamentoVO.class);	        
			authenticationService.validaToken(equipamentoVo.getToken());
			
	        LdapUsuarioVO usrLdap = ldapService.findUsuario(equipamentoVo.getLoginUsuario());
	        equipamentoVo.setLoginUsuario(usrLdap.getLogin());

	        senchaService.persistInfoEquipamento(equipamentoVo);
        } catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;			
		} catch (Exception e) {
        	info.codigo = 1;
        	info.mensagem = "Erro ao enviar informações do equipamento: " + e.getMessage();
	        e.printStackTrace();
        }	
		return info;
	}	
	
	
	@RequestMapping(value="/usuario/recuperaInfoAtualizacoes", method=RequestMethod.POST)
	public InfoRetornoDadosAtualizacao recuperaInfoAtualizacoes(@RequestBody String infAtualizacao) {
		InfoRetornoDadosAtualizacao info = new InfoRetornoDadosAtualizacao();
		info.codigo = 0;
		info.mensagem = "Informações de atualização recuperadas com sucesso.";
		
		infAtualizacao = new String(infAtualizacao.getBytes(), Charset.forName("ISO-8859-1"));

		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(infAtualizacao));
		reader.setLenient(true);
		RequisicaoAtualizacoesVO reqAtualizacoes = null;
		try {
	        reqAtualizacoes = gson.fromJson(reader, RequisicaoAtualizacoesVO.class);
	        authenticationService.validaToken(reqAtualizacoes.token);
	        
	        LdapUsuarioVO usrLdap = ldapService.findUsuario(reqAtualizacoes.login);
	        reqAtualizacoes.login = usrLdap.getLogin();
	        if(reqAtualizacoes.aparelho != null){
	        	reqAtualizacoes.aparelho.setLoginUsuario(usrLdap.getLogin());
	        }
	        info.qtdAtualizacoes = senchaService.getAtualizacoesUsuario(reqAtualizacoes);        
	        
	        info.codigo = 0;
	        info.mensagem = "Recuperando informações de atualização";
	        sendMessageAcompanhamento("recuperaInfoAtualizacoes", usrLdap.getLogin(), "");
        } catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
		} catch (Exception e) {
        	info.codigo = 1;
        	info.mensagem = "Erro ao recuperar informações de atualização: " + e.getMessage();
	        e.printStackTrace();
        }	
		return info;
	}	
	

	@RequestMapping(value="/usuario/localizacoesAparelho", method=RequestMethod.POST)
	public InfoRetornoDadosAtualizacao localizacoesAparelho(@RequestBody String infAtualizacao) {
		InfoRetornoDadosAtualizacao info = new InfoRetornoDadosAtualizacao();
		info.codigo = 0;
		info.mensagem = "Informações de localização enviadas com sucesso.";
		
		infAtualizacao = new String(infAtualizacao.getBytes(), Charset.forName("ISO-8859-1"));

		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(infAtualizacao));
		reader.setLenient(true);
		RequisicaoAtualizacaoLocalizacaoVO reqAtualizacoes = null;
		try {
	        reqAtualizacoes = gson.fromJson(reader, RequisicaoAtualizacaoLocalizacaoVO.class);
	        authenticationService.validaToken(reqAtualizacoes.token);
	        
	        LdapUsuarioVO usrLdap = ldapService.findUsuario(reqAtualizacoes.login);
	        reqAtualizacoes.login = usrLdap.getLogin();
	        
	        if(reqAtualizacoes.localizacoes != null && reqAtualizacoes.localizacoes.size() > 0) {
		        senchaService.atualizaLocalizacaoAparelho(reqAtualizacoes);
	        }
	        
        } catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
		} catch (Exception e) {
        	info.codigo = 1;
        	info.mensagem = "Erro ao enviar informações de localização: " + e.getMessage();
	        e.printStackTrace();
        }	
		return info;
	}	

	@RequestMapping(value="/usuario/gravaSugestaoUsuario", method=RequestMethod.POST)
	public InfoRetornoMensagem gravaSugestaoUsuario(@RequestBody String infSugestao) {
		InfoRetornoMensagem info = new InfoRetornoMensagem();
		info.codigo = 0;
		info.mensagem = "Sugestão enviada com sucesso.";
		
		infSugestao = new String(infSugestao.getBytes(), Charset.forName("ISO-8859-1"));

		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(infSugestao));
		reader.setLenient(true);
		SugestaoVO reqSugestao = null;
		try {
	        reqSugestao = gson.fromJson(reader, SugestaoVO.class);
	        authenticationService.validaToken(reqSugestao.token);
	        
	        senchaService.novaSugestao(reqSugestao);
	        
        } catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
		} catch (Exception e) {
        	info.codigo = 1;
        	info.mensagem = "Erro ao armazenar sugestão: " + e.getMessage();
	        e.printStackTrace();
        }	
		return info;
	}	
	
	@RequestMapping(value="/usuario/recuperaTiposSugestao", method=RequestMethod.POST)
	public InfoRetornoMensagem recuperaTiposSugestao(@RequestBody String infSugestao) {
		InfoRetornoMensagem info = new InfoRetornoMensagem();
		info.codigo = 0;
		info.mensagem = "Tipos recuperados com sucesso.";
		
		infSugestao = new String(infSugestao.getBytes(), Charset.forName("ISO-8859-1"));

		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(infSugestao));
		reader.setLenient(true);
		SugestaoVO reqSugestao = null;
		try {
	        reqSugestao = gson.fromJson(reader, SugestaoVO.class);
	        authenticationService.validaToken(reqSugestao.token);
	        
	        info.tiposSugestao = senchaService.getTiposSugestao();
	        
        } catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
		} catch (Exception e) {
        	info.codigo = 1;
        	info.mensagem = "Erro ao recuperar tipos sugestão: " + e.getMessage();
	        e.printStackTrace();
        }	
		return info;
	}	
	
	
	@RequestMapping(value="/usuario/adicionaUsuarioPublico", method=RequestMethod.POST)
	public InfoRetornoCriacaoUsuarioPublico adicionaUsuarioPublico(@RequestBody String reqInfo) {
		InfoRetornoCriacaoUsuarioPublico info = new InfoRetornoCriacaoUsuarioPublico();
		reqInfo = new String(reqInfo.getBytes(), Charset.forName("ISO-8859-1"));
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(reqInfo));
		reader.setLenient(true);
		RequestVO request = null;

		try {
	        request = gson.fromJson(reader, RequestVO.class);	        
	        if(request.parametros == null || request.parametros.length != 7) {
	        	String mensagem = MessageFormat.format(IMensagensErro.PARAMETRO_QUANTIDADE_ERRADA_MSG, "7");
	        	throw new ParametroException(mensagem, IMensagensErro.PARAMETRO_QUANTIDADE_ERRADA_CODE);
	        }
	        String email = request.parametros[0];
	        String nome = request.parametros[1];
	        String dataNascimento = request.parametros[2];
	        String telefone = request.parametros[3];
	        String cpf = request.parametros[4];
	        String advogado = request.parametros[5];
	        String senha = request.parametros[6];
	        
	        LdapUsuarioVO usrVo = new LdapUsuarioVO();
	        usrVo.setMail(email);
	        usrVo.setNome(nome.toUpperCase());
	        usrVo.setDataNascimento(dataNascimento);
	        usrVo.setLogin(email);
	        usrVo.setPasswd(senha);
	        usrVo.setCpf(cpf);
	        usrVo.setAdvogado("true".equalsIgnoreCase(advogado) ? true : false); 
	        usrVo.setTelefone(telefone);	        
	        manterLdapService.adicionaUsuarioPublico(usrVo);	        
			info.codigo = 0;
			info.mensagem = "Usuario cadastrado com sucesso";
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;			
		} catch (Exception e) {
			info.codigo = 1;
			info.mensagem = "Erro ao apagar mensagem: " + e.getMessage();
		}
		return info;
	}

	@RequestMapping(value="/usuario/alteraDadosCadastrais", method=RequestMethod.POST)
	public InfoRetornoCriacaoUsuarioPublico alteraDadosCadastrais(@RequestBody String reqInfo) {
		InfoRetornoCriacaoUsuarioPublico info = new InfoRetornoCriacaoUsuarioPublico();
		reqInfo = new String(reqInfo.getBytes(), Charset.forName("ISO-8859-1"));
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(reqInfo));
		reader.setLenient(true);
		RequestVO request = null;

		try {
	        request = gson.fromJson(reader, RequestVO.class);	      
	        authenticationService.validaToken(request.token);
	        if(request.parametros == null || request.parametros.length != 8) {
	        	String mensagem = MessageFormat.format(IMensagensErro.PARAMETRO_QUANTIDADE_ERRADA_MSG, "8");
	        	throw new ParametroException(mensagem, IMensagensErro.PARAMETRO_QUANTIDADE_ERRADA_CODE);
	        }
	        
	        
	        String email = request.parametros[0];
	        String nome = request.parametros[1];
	        String dataNascimento = request.parametros[2];
	        String telefone = request.parametros[3];
	        String cpf = request.parametros[4];
	        String advogado = request.parametros[5];
	        String senha = request.parametros[6];
	        String login = request.parametros[7];
	        
	        LdapUsuarioVO usrVo = new LdapUsuarioVO();
	        usrVo.setMail(email);
	        usrVo.setNome(nome.toUpperCase());
	        usrVo.setDataNascimento(dataNascimento);
	        usrVo.setLogin(email);
	        usrVo.setPasswd(senha);
	        usrVo.setCpf(cpf);
	        usrVo.setAdvogado("true".equalsIgnoreCase(advogado) ? true : false); 
	        usrVo.setTelefone(telefone);
	        usrVo.setLogin(login);
	        
	        LdapUsuarioVO usr = manterLdapService.alteraUsuarioPublico(usrVo);
	        
	        info.infoUsuario = usr;
			info.codigo = 0;
			info.mensagem = "Usuario alterado com sucesso";
			sendMessageAcompanhamento("alteraDadosCadastrais", cpf, "email: " + email + ", nome: " + nome + ", dataNascimento: " + dataNascimento + 
					", telefone: " + telefone + ", cpf: " + cpf + ", advogado: " + advogado + ", login: " + login);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;			
		} catch (Exception e) {
			info.codigo = 1;
			info.mensagem = "Erro ao alterar usuário: " + e.getMessage();
		}
		return info;
	}
	
	
	@RequestMapping(value="/usuario/recuperaDadosCadastrais", method=RequestMethod.POST)
	public InfoRetornoCriacaoUsuarioPublico recuperaDadosCadastrais(@RequestBody String reqInfo) {
		InfoRetornoCriacaoUsuarioPublico info = new InfoRetornoCriacaoUsuarioPublico();
		reqInfo = new String(reqInfo.getBytes(), Charset.forName("ISO-8859-1"));
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(reqInfo));
		reader.setLenient(true);
		RequestVO request = null;

		try {
	        request = gson.fromJson(reader, RequestVO.class);	      
	        authenticationService.validaToken(request.token);     
	        if(request.parametros == null || request.parametros.length != 1) {
	        	String mensagem = MessageFormat.format(IMensagensErro.PARAMETRO_QUANTIDADE_ERRADA_MSG, "1");
	        	throw new ParametroException(mensagem, IMensagensErro.PARAMETRO_QUANTIDADE_ERRADA_CODE);
	        }
	        
	        String login = request.parametros[0];
	        
	        LdapUsuarioVO usuario = senchaService.getInfoUsuario(login);
	        info.infoUsuario = usuario;
			info.codigo = 0;
			info.mensagem = "Informações de usuário recuperadas com sucesso";
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;			
		} catch (Exception e) {
			info.codigo = 1;
			info.mensagem = "Erro ao recuperar informações de usuário: " + e.getMessage();
		}
		return info;
	}	
	
	@RequestMapping(value="/usuario/enviarLembreteSenha", method=RequestMethod.POST)
	public InfoRetornoCriacaoUsuarioPublico enviarLembreteSenha(@RequestBody String reqInfo) {
		InfoRetornoCriacaoUsuarioPublico info = new InfoRetornoCriacaoUsuarioPublico();
		reqInfo = new String(reqInfo.getBytes(), Charset.forName("ISO-8859-1"));
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(reqInfo));
		reader.setLenient(true);
		RequestVO request = null;

		try {
	        request = gson.fromJson(reader, RequestVO.class);	        
	        if(request.parametros == null || request.parametros.length != 1) {
	        	String mensagem = MessageFormat.format(IMensagensErro.PARAMETRO_QUANTIDADE_ERRADA_MSG, "1");
	        	throw new ParametroException(mensagem, IMensagensErro.PARAMETRO_QUANTIDADE_ERRADA_CODE);
	        }
	        String mail = request.parametros[0];
	        
	        LdapUsuarioVO usrVo = new LdapUsuarioVO();
	        usrVo.setMail(mail);
	        
	        usrVo = manterLdapService.enviarLembreteSenha(usrVo);
	        
			info.codigo = 0;
			info.mensagem = "Mensagem enviada com sucesso";
			info.email = usrVo.getMail();
			sendMessageAcompanhamento("enviarLembreteSenha", mail, "");
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;			
		} catch (Exception e) {
			info.codigo = 1;
			info.mensagem = "Erro ao apagar mensagem: " + e.getMessage();
		}
		return info;
	}	
	
	private void sendMessageAcompanhamento(String servicoChamado, String login, String parametros) {
		MobileLoggingMSG acompanhamento = new MobileLoggingMSG();
		acompanhamento.aparelhoOrigem = ""; 
		acompanhamento.servicoChamado = servicoChamado;
		acompanhamento.login = login;
		acompanhamento.parametrosChamada = parametros; 
				
		logController.enviaMensagemLogMobile(acompanhamento);		
	}	
	
	private void sendMessageError(String servicoChamado, String msgErro, String parametros) {
		LoggingErrorMSG erro = new LoggingErrorMSG();
		erro.setDhChamada(new Date()); 
		erro.servicoChamado = servicoChamado;
		erro.mensagemErro = msgErro;
		erro.parametrosChamada = parametros; 
				
		logController.enviaMensagemErro(erro);
		
	}
		
}
	
