package br.com.laminarsoft.jazzforms.negocio.controller;

import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.laminarsoft.jazzforms.persistencia.model.exception.ParametroException;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Icon;
import br.com.laminarsoft.jazzforms.persistencia.model.util.InfoRetornoIcon;
import br.com.laminarsoft.jazzforms.persistencia.model.util.InfoRetornoOpcoesDTV;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.DataviewOpcaoVO;

@Controller
@RequestMapping("/servicos/sigtapService")
@SuppressWarnings("all")
public class ManterSigtapController {

	@Autowired
	private ResourceBundle bundle;
	
	@Autowired
	private ManterSigtapService sigtapService;
	

	@RequestMapping(value="/cid/findAll", method=RequestMethod.GET)
	public InfoRetornoOpcoesDTV findAllCids() {
		InfoRetornoOpcoesDTV info = new InfoRetornoOpcoesDTV();
		try {
			List<DataviewOpcaoVO> opcoes = sigtapService.getAllCids();
			info.dhUltimaAtualizacao = sigtapService.getUltimaAtualizacaoCid();
			info.codigo = 0;
			info.mensagem = "CIDs recuperados com sucesso";
			info.opcoes = opcoes;
		} catch (Exception e) {
			info.codigo = 1;
			info.mensagem = "Erro ao recuperar listagem de cids";
		}
		return info;		
	}
	
	@RequestMapping(value="/nacionalidade/findAll", method=RequestMethod.GET)
	public InfoRetornoOpcoesDTV findAllNacionalidades() {
		InfoRetornoOpcoesDTV info = new InfoRetornoOpcoesDTV();
		try {
			List<DataviewOpcaoVO> opcoes = sigtapService.getAllNacionalidades();
			info.dhUltimaAtualizacao = sigtapService.getUltimaAtualizacaoNacionalidade();
			info.codigo = 0;
			info.mensagem = "Nacionalidades recuperadas com sucesso";
			info.opcoes = opcoes;
		} catch (Exception e) {
			info.codigo = 1;
			info.mensagem = "Erro ao recuperar listagem de nacionalidades";
		}
		return info;		
	}	
	
	@RequestMapping(value="/tribo/findAll", method=RequestMethod.GET)
	public InfoRetornoOpcoesDTV findAllTribos() {
		InfoRetornoOpcoesDTV info = new InfoRetornoOpcoesDTV();
		try {
			List<DataviewOpcaoVO> opcoes = sigtapService.getAllTribosIndigenas();
			info.dhUltimaAtualizacao = sigtapService.getUltimaAtualizacaoTriboIndigena();
			info.codigo = 0;
			info.mensagem = "Tribos recuperadas com sucesso";
			info.opcoes = opcoes;
		} catch (Exception e) {
			info.codigo = 1;
			info.mensagem = "Erro ao recuperar listagem de tribos";
		}
		return info;		
	}	
	
	@RequestMapping(value="/ocupacao/findAll", method=RequestMethod.GET)
	public InfoRetornoOpcoesDTV findAllOcupacoes() {
		InfoRetornoOpcoesDTV info = new InfoRetornoOpcoesDTV();
		try {
			List<DataviewOpcaoVO> opcoes = sigtapService.getAllOcupacoes();
			info.dhUltimaAtualizacao = sigtapService.getUltimaAtualizacaoOcupacao();
			info.codigo = 0;
			info.mensagem = "Ocupacoes recuperadas com sucesso";
			info.opcoes = opcoes;
		} catch (Exception e) {
			info.codigo = 1;
			info.mensagem = "Erro ao recuperar listagem de ocupacoes";
		}
		return info;		
	}		

	@RequestMapping(value="/procedimento/findAll", method=RequestMethod.GET)
	public InfoRetornoOpcoesDTV findAllProcedimentos() {
		InfoRetornoOpcoesDTV info = new InfoRetornoOpcoesDTV();
		try {
			List<DataviewOpcaoVO> opcoes = sigtapService.getAllProcedimentos();
			info.dhUltimaAtualizacao = sigtapService.getUltimaAtualizacaoProcedimento();
			info.codigo = 0;
			info.mensagem = "Procedimentos recuperados com sucesso";
			info.opcoes = opcoes;
		} catch (Exception e) {
			info.codigo = 1;
			info.mensagem = "Erro ao recuperar listagem de procedimentos";
		}
		return info;		
	}		

	
	@RequestMapping(value="/cid/necessidadeAtualizacao/{ultimaAtualizacao}", method=RequestMethod.GET)
	public InfoRetornoOpcoesDTV necessitaAtualizacaoCid(@PathVariable Long ultimaAtualizacao) {
		InfoRetornoOpcoesDTV info = new InfoRetornoOpcoesDTV();
		try {
			info.alteraOpcoes = sigtapService.necessitaAtualizacaoCid(ultimaAtualizacao);
			info.codigo = 0;
			info.mensagem = "necessidade recuperada com sucesso";
		} catch (Exception e) {
			info.codigo = 1;
			info.mensagem = "erro ao recuperar necessidade de atualizacao: " + e.getMessage();
		}
		return info;
	}

	@RequestMapping(value="/nacionalidade/necessidadeAtualizacao/{ultimaAtualizacao}", method=RequestMethod.GET)
	public InfoRetornoOpcoesDTV necessitaAtualizacaoNacionalidade(@PathVariable Long ultimaAtualizacao) {
		InfoRetornoOpcoesDTV info = new InfoRetornoOpcoesDTV();
		try {
			info.alteraOpcoes = sigtapService.necessitaAtualizacaoNacionalidade(ultimaAtualizacao);
			info.codigo = 0;
			info.mensagem = "necessidade recuperada com sucesso";
		} catch (Exception e) {
			info.codigo = 1;
			info.mensagem = "erro ao recuperar necessidade de atualizacao: " + e.getMessage();
		}
		return info;
	}	
	
	@RequestMapping(value="/tribo/necessidadeAtualizacao/{ultimaAtualizacao}", method=RequestMethod.GET)
	public InfoRetornoOpcoesDTV necessitaAtualizacaoTribo(@PathVariable Long ultimaAtualizacao) {
		InfoRetornoOpcoesDTV info = new InfoRetornoOpcoesDTV();
		try {
			info.alteraOpcoes = sigtapService.necessitaAtualizacaoTriboIndigena(ultimaAtualizacao);
			info.codigo = 0;
			info.mensagem = "necessidade recuperada com sucesso";
		} catch (Exception e) {
			info.codigo = 1;
			info.mensagem = "erro ao recuperar necessidade de atualizacao: " + e.getMessage();
		}
		return info;
	}		
	
	@RequestMapping(value="/ocupacao/necessidadeAtualizacao/{ultimaAtualizacao}", method=RequestMethod.GET)
	public InfoRetornoOpcoesDTV necessitaAtualizacaoOcupacao(@PathVariable Long ultimaAtualizacao) {
		InfoRetornoOpcoesDTV info = new InfoRetornoOpcoesDTV();
		try {
			info.alteraOpcoes = sigtapService.necessitaAtualizacaoOcupacao(ultimaAtualizacao);
			info.codigo = 0;
			info.mensagem = "necessidade recuperada com sucesso";
		} catch (Exception e) {
			info.codigo = 1;
			info.mensagem = "erro ao recuperar necessidade de atualizacao: " + e.getMessage();
		}
		return info;
	}		

	@RequestMapping(value="/procedimento/necessidadeAtualizacao/{ultimaAtualizacao}", method=RequestMethod.GET)
	public InfoRetornoOpcoesDTV necessitaAtualizacaoProcedimento(@PathVariable Long ultimaAtualizacao) {
		InfoRetornoOpcoesDTV info = new InfoRetornoOpcoesDTV();
		try {
			info.alteraOpcoes = sigtapService.necessitaAtualizacaoProcedimento(ultimaAtualizacao);
			info.codigo = 0;
			info.mensagem = "necessidade recuperada com sucesso";
		} catch (Exception e) {
			info.codigo = 1;
			info.mensagem = "erro ao recuperar necessidade de atualizacao: " + e.getMessage();
		}
		return info;
	}		
}
