package br.com.laminarsoft.jazzforms.negocio.controller.util;

public class AutenticacaoCorporativaFactory {
	
	public static AutenticacaoCorporativa getMetodoAutenticacaoCorporativa() {
		PropertiesServiceController props = PropertiesServiceController.getInstance();
		String tipoAutenticacaoCorporativa = props.getProperty("ldap.metodoautenticacao.corporativa");
		if("AD".equalsIgnoreCase(tipoAutenticacaoCorporativa)) {
			return ActiveDirectoryServiceController.getInstance();
		}
		return null;
	}
}
