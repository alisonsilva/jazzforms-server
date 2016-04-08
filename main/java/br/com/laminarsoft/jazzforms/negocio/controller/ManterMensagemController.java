package br.com.laminarsoft.jazzforms.negocio.controller;

import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.laminarsoft.jazzforms.negocio.controller.security.AuthenticationService;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.ParametroException;
import br.com.laminarsoft.jazzforms.persistencia.model.util.InfoRetornoAnexo;
import br.com.laminarsoft.jazzforms.persistencia.model.util.InfoRetornoMensagem;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.AnexoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LdapGrupoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LdapUsuarioVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.MensagemVO;

@Controller
@RequestMapping("/servicos/mensagemService")
@SuppressWarnings("all")
public class ManterMensagemController {

	@Autowired private ResourceBundle bundle;	
	@Autowired private ManterMensagemService mensagemService;
	@Autowired private AuthenticationService authenticationService;
	@Autowired private ManterLdapService ldapService;

	@RequestMapping(value="/mensagem/novaMensagemUsuario", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoMensagem novaMensagemUsuario(@RequestBody MensagemVO mensagem) {
		InfoRetornoMensagem info = new InfoRetornoMensagem();
		try {
			authenticationService.validaToken(mensagem.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}		
		try {
			String remetenteUID = ldapService.findUsuario(mensagem.getRemetenteUID()).getUid();
			String destinatarioUID = ldapService.findUsuario(mensagem.getDestinatarioUID()).getUid();
			mensagem.setRemetenteUID(remetenteUID);
			mensagem.setDestinatarioUID(destinatarioUID);
			
			mensagemService.novaMensagemUsuario(mensagem);
	        
			info.codigo = 0;
	        info.mensagem = "Mensagem criada e aguardando envio";
        } catch (Exception e) {
        	info.codigo = 1;
        	info.mensagem = "Erro ao criar mensagem: " + e.getMessage();
        }
		return info;
	}
	
	@RequestMapping(value="/mensagem/novaMensagemBPM", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoMensagem novaMensagemBPM(@RequestBody String mensagem) {
		InfoRetornoMensagem info = new InfoRetornoMensagem();
		
		String strValorDataview = new String(mensagem.getBytes(), Charset.forName("UTF-8"));
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(MensagemVO.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			
			MensagemVO mensagemTransf = (MensagemVO)jaxbUnmarshaller.unmarshal(new StringReader(strValorDataview));
	        
			mensagemService.novaMensagemBPM(mensagemTransf);
	        
			info.codigo = 0;
	        info.mensagem = "Mensagem criada e aguardando envio";
        } catch (Exception e) {
        	info.codigo = 1;
        	info.mensagem = "Erro ao criar mensagem: " + e.getMessage();
        }
		return info;
	}	
	
	@RequestMapping(value="/mensagem/novaMensagem", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoMensagem novaMensagem(@RequestBody String mensagem) {
		InfoRetornoMensagem info = new InfoRetornoMensagem();
		
		String strValorDataview = new String(mensagem.getBytes(), Charset.forName("UTF-8"));
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(MensagemVO.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			
			MensagemVO mensagemTransf = (MensagemVO)jaxbUnmarshaller.unmarshal(new StringReader(strValorDataview));
	        
			mensagemService.novaMensagem(mensagemTransf);
	        
			info.codigo = 0;
	        info.mensagem = "Mensagem criada e aguardando envio";
        } catch (Exception e) {
        	info.codigo = 1;
        	info.mensagem = "Erro ao criar mensagem: " + e.getMessage();
        }
		return info;
	}	
	
	@RequestMapping(value="/mensagem/novaMensagemGrupo", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoMensagem novaMensagemGrupo(@RequestBody MensagemVO mensagem) {
		InfoRetornoMensagem info = new InfoRetornoMensagem();
		try {
			authenticationService.validaToken(mensagem.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}		
		try {
			for(String grupo : mensagem.getGrupos()) {
				LdapGrupoVO ldapGrupo = ldapService.findGrupo(grupo);
				ArrayList<String> uids = mensagem.uidsGrupos.get(grupo);
				if(uids == null) {
					uids = new ArrayList<String>();
					mensagem.uidsGrupos.put(grupo, uids);
				}
				for(LdapUsuarioVO usuario : ldapGrupo.getUsuarios()) {
					uids.add(usuario.getUid());
				}
			}
			
			mensagemService.novaMensagemGrupo(mensagem);
	        
			info.codigo = 0;
	        info.mensagem = "Mensagem criada e aguardando envio";
        } catch (Exception e) {
        	info.codigo = 1;
        	info.mensagem = "Erro ao criar mensagem: " + e.getMessage();
        }
		return info;
	}	
	
	@RequestMapping(value="/mensagem/novaMensagemEquipamento", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoMensagem novaMensagemEquipamento(@RequestBody MensagemVO mensagem) {
		InfoRetornoMensagem info = new InfoRetornoMensagem();
		try {
			authenticationService.validaToken(mensagem.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}		
		try {
			mensagemService.novaMensagemEquipamento(mensagem);
	        
			info.codigo = 0;
	        info.mensagem = "Mensagem criada e aguardando envio";
        } catch (Exception e) {
        	info.codigo = 1;
        	info.mensagem = "Erro ao criar mensagem: " + e.getMessage();
        }
		return info;
	}	
	
	@RequestMapping(value="/mensagem/novaMensagemGrupoEquipamento", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoMensagem novaMensagemGrupoEquipamento(@RequestBody MensagemVO mensagem) {
		InfoRetornoMensagem info = new InfoRetornoMensagem();
		try {
			authenticationService.validaToken(mensagem.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}		
		
		try {
			mensagemService.novaMensagemGrupoEquipamento(mensagem);
	        
			info.codigo = 0;
	        info.mensagem = "Mensagem criada e aguardando envio";
        } catch (Exception e) {
        	info.codigo = 1;
        	info.mensagem = "Erro ao criar mensagem: " + e.getMessage();
        }
		return info;
	}
	
	@RequestMapping(value="/mensagem/atribuiProjetoMensagem", method=RequestMethod.GET)
	@ResponseBody
	public InfoRetornoMensagem atribuiProjetoMensagem(@PathVariable Long idProjeto, @PathVariable Long idMensagem) {
		InfoRetornoMensagem info = new InfoRetornoMensagem();
		try {
			mensagemService.atribuiProjetoMensagem(idProjeto, idMensagem);
			info.codigo = 0;
			info.mensagem = "Projeto atribuido com sucesso";
		} catch(Exception e) {
			info.codigo = 1;
			info.mensagem = "Erro ao atribuir projeto à mensagem: " + e.getMessage();
		}
		return info;
	}
	
	@RequestMapping(value="/mensagem/anexoMensagemUsuario/{idMensagem}/{loginUsuario}", method=RequestMethod.GET)
	@ResponseBody
	public InfoRetornoAnexo anexoMensagemUsuario(@PathVariable Long idMensagem, @PathVariable String loginUsuario) {
		InfoRetornoAnexo info = new InfoRetornoAnexo();
		try {
			AnexoVO anexo = mensagemService.anexoMensagemUsuario(idMensagem, loginUsuario);
			info.arqAnexo = "";
			if (anexo.arqAnexo != null && anexo.arqAnexo.length > 1) {
				info.arqAnexo = new String(Base64.encodeBase64(anexo.arqAnexo));
			} 
			info.dhInclusao = (anexo.dhInclusao != null ? anexo.dhInclusao.getTime() : null);
			info.nomeArquivo = anexo.nomeArquivo;
			info.type = anexo.type;
			info.urlSite = "";
			info.id = anexo.id;
			info.codigo = 0;
			info.mensagem = "Arquivo recuperado com sucesso";
		} catch(Exception e) {
			info.codigo = 1;
			info.mensagem = "Erro ao recuperar arquivo anexo: " + e.getMessage();
		}
		return info;
	}	
}
