package br.com.laminarsoft.jazzforms.negocio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.laminarsoft.jazzforms.persistencia.dao.MensagemDao;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.AnexoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.MensagemVO;


@Service(value="mensagemService")
@SuppressWarnings("all")
@Transactional(propagation=Propagation.SUPPORTS, 
		isolation=Isolation.READ_UNCOMMITTED, 
		timeout=30, 
		rollbackFor=Exception.class)
public class ManterMensagemService {

	@Autowired private MensagemDao mensagemDao;
	
	public void novaMensagemUsuario(MensagemVO mensagem) {
		mensagemDao.novaMensagemUsuario(mensagem);
	}
	
	public void novaMensagemBPM(MensagemVO mensagem) {
		mensagemDao.novaMensagemBPM(mensagem);
	}
	
	public void novaMensagem(MensagemVO mensagem) {
		mensagemDao.novaMensagem(mensagem);
	}	
	
	public void novaMensagemGrupo(MensagemVO mensagem) {
		mensagemDao.novaMensagemGrupo(mensagem);
	}
	
	public void novaMensagemEquipamento(MensagemVO mensagem) {
		mensagemDao.novaMensagemEquipamento(mensagem);
	}
	
	public void novaMensagemGrupoEquipamento(MensagemVO mensagem) {
		mensagemDao.novaMensagemGrupoEquipamentos(mensagem);
	}
	
	public void atribuiProjetoMensagem(Long idProjeto, Long idMensagem) {
		mensagemDao.atribuiProjetoMensagem(idProjeto, idMensagem);
	}
	
	public AnexoVO anexoMensagemUsuario(Long idMensagem, String loginUsuario) {
		return mensagemDao.getAnexoPorMensagemUsuario(idMensagem, loginUsuario);
	}
}
