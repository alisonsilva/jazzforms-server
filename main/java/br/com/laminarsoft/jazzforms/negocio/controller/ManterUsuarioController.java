package br.com.laminarsoft.jazzforms.negocio.controller;

import java.util.Date;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.laminarsoft.jazzforms.negocio.controller.security.AuthenticationService;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.IMensagensErro;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.ParametroException;
import br.com.laminarsoft.jazzforms.persistencia.model.util.InfoRetornoUsuario;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LdapUsuarioVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.RequestVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.UsuarioVO;
import br.com.laminarsoft.jazzforms.persistencia.util.DateUtil;

@Controller
@RequestMapping("/servicos/usuarioService")
@SuppressWarnings("all")
public class ManterUsuarioController {

	@Autowired private ResourceBundle bundle;	
	@Autowired private ManterUsuarioService usuarioService;
	@Autowired private AuthenticationService authenticationService;
	@Autowired private ManterLdapService ldapService;


	@RequestMapping(value="/usuario/findLocalizacoesUsuario", method=RequestMethod.POST)
	@ResponseBody
	public InfoRetornoUsuario findEquipamentos(@RequestBody RequestVO request) {
		InfoRetornoUsuario info = new InfoRetornoUsuario();
		try {
			authenticationService.validaToken(request.token);
		} catch (ParametroException e) {
			info.codigo = e.getCodigoErro();
			info.mensagem = e.getMessage();
			return info;
		}		
		try {
			String login = request.parametros[0];
	        LdapUsuarioVO ldapusr = ldapService.findUsuario(login);
	        UsuarioVO usuario = new UsuarioVO();
	        usuario.lgn = ldapusr.getLogin();
	        usuario.showName = ldapusr.getNome();

	        info.infoUsuario = usuario;
			info.localizacoes = usuarioService.getLocalizacoesUsuario(ldapusr.getUid());
			info.codigo = 0;
			info.mensagem = "Informações das localizacoes recuperadas com sucesso";
		} catch (Exception e) {
			e.printStackTrace();
			info.codigo = -1;
			info.mensagem = "Erro inesperado: " + e.getMessage();
		}
		return info;		
	}	
	
	@RequestMapping(value = "/usuario/ativaUsuario/{token}", method=RequestMethod.GET)
	@ResponseBody
	public void ativaUsuario(@PathVariable String token) {
		try {
			if(StringUtils.isEmpty(token)) {
				throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
			}
			String valor = authenticationService.decrypt(token);
			StringTokenizer strtok = new StringTokenizer(valor, "#");
			if(strtok.countTokens() == 2) {
				String timestamp = strtok.nextToken();
				
				Long tmstamp = Long.parseLong(timestamp);
				Date dtRegistro = new Date(tmstamp);
				Date dtAgora = new Date();
				Long qtdDias = DateUtil.diasDiferenca(dtRegistro, dtAgora, DateUtil.DIFERENCA_DIAS);
				if(qtdDias > 2) {
					return;
				}
				
				String idUsr = strtok.nextToken();
				usuarioService.ativaUsuario(Long.parseLong(idUsr.trim()));
			}
		} catch(Exception e) {			
		}
	}

}
