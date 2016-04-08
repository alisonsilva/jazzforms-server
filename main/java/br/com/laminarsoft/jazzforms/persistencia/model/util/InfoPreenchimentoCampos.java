package br.com.laminarsoft.jazzforms.persistencia.model.util;

public class InfoPreenchimentoCampos {
	public String nomeCampo;
	public String mensagem;
	
	public String toString() {
		String msg = "Erro no preenchimento do campo: " + nomeCampo + "; mensagem: " + mensagem;
		return msg;
	}
}
