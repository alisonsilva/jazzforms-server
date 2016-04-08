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
import br.com.laminarsoft.jazzforms.persistencia.model.ProcessModel;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.ParametroException;
import br.com.laminarsoft.jazzforms.persistencia.model.util.InfoRetornoProcessModel;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.RequestVO;

@Controller
@RequestMapping("/servicos/processModelService")
@SuppressWarnings("all")
public class ManterProcessModelController {

	@Autowired private ResourceBundle bundle;	
	@Autowired private ManterProcessModelService processModelService;
	@Autowired private AuthenticationService authenticationService;

	@RequestMapping(value="/processmodel/findByProcessID/{processId}", method=RequestMethod.GET)
	@ResponseBody
	public InfoRetornoProcessModel findByName(@PathVariable String processId) {
		ProcessModel processModel = null;
		InfoRetornoProcessModel info = new InfoRetornoProcessModel();
		try {
			processModel = processModelService.findByProcessID(processId);
			info.codigo = 0;
			info.mensagem = "Processo recuperado com sucesso";
			info.processModel = processModel;
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();			
			info.mensagem = "Erro ao recuperar processo: " + e.getMessage();
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

	@RequestMapping(value="/processmodel/findAll", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoProcessModel findAllWithName(@RequestBody RequestVO request) {
		List<ProcessModel> processModels = null;
		InfoRetornoProcessModel info = new InfoRetornoProcessModel();
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}		
		try {
			processModels = processModelService.findAllWithName();
			info.codigo = 0;
			info.mensagem = "Processos recuperados com sucesso";
			info.processModels = processModels;
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = "Erro ao recuperar processos de negócio";
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
	
	@RequestMapping(value="/processmodel/updateModel", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoProcessModel updateModel(@RequestBody String model) {
		InfoRetornoProcessModel info = new InfoRetornoProcessModel();
		
		String strValorDataview = new String(model.getBytes(), Charset.forName("UTF-8"));
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(ProcessModel.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			
			ProcessModel processModel = (ProcessModel)jaxbUnmarshaller.unmarshal(new StringReader(strValorDataview));
			processModelService.persist(processModel);
	        
			info.codigo = 0;
	        info.mensagem = "Process model atualizado com sucesso";
        } catch (Exception e) {
        	info.codigo = 1;
        	info.mensagem = "Erro ao criar process model: " + e.getMessage();
        }
		return info;
	}	

}
