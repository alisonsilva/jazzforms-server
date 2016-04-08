package br.com.laminarsoft.jazzforms.persistencia.model.exception;

import br.com.laminarsoft.jazzforms.persistencia.dao.ProjetoDao;

@SuppressWarnings("all")
public class ParametroProjetoException extends ParametroException {
	
	private ProjetoDao.ValidaPreenchimentoProjeto infoValidacao;
	
	public ParametroProjetoException() {
		super("O parametro de projeto não é válido: erro genérico");
	}
	
	public ParametroProjetoException(ProjetoDao.ValidaPreenchimentoProjeto infoValidacao) {
		this.infoValidacao = infoValidacao;
	}
	
	public ProjetoDao.ValidaPreenchimentoProjeto getInfoValidacao() {
		return infoValidacao;
	}
}
