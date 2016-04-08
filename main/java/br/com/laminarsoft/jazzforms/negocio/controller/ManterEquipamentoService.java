package br.com.laminarsoft.jazzforms.negocio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.laminarsoft.jazzforms.persistencia.dao.EquipamentoDao;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.EquipamentoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.GrupoEquipamentoVO;


@Service(value="equipamentoService")
@SuppressWarnings("all")
@Transactional(propagation=Propagation.SUPPORTS, 
		isolation=Isolation.READ_UNCOMMITTED, 
		timeout=30, 
		rollbackFor=Exception.class)
public class ManterEquipamentoService {

	@Autowired
	private EquipamentoDao equipamentoDao;
	
	public List<EquipamentoVO> findEquipamentos() {
		return equipamentoDao.findEquipamentos();
	}
	
	public List<GrupoEquipamentoVO> findGruposEquipamentos() {
		return equipamentoDao.findGruposEquipamentos();
	}

	public void novoGrupoEquipamento(GrupoEquipamentoVO grupoEquipamento) {
		equipamentoDao.novoGrupoEquipamento(grupoEquipamento);
	}
	
	public void alteraGrupoEquipamento(GrupoEquipamentoVO grupoEquipamento) {
		equipamentoDao.alteraGrupoEquipamento(grupoEquipamento);
	}
	
	public void removeGrupoEquipamento(Long idGrupoEquipamento) {
		equipamentoDao.removeGrupoEquipamento(idGrupoEquipamento);
	}
	
	public void adicionaEquipamentoAoGrupo(Long grupoId, Long equipamentoId) {
		equipamentoDao.adicionaEquipamentoAoGrupo(grupoId, equipamentoId);
	}

	public void removeEquipamentoDoGrupo(Long grupoId, Long equipamentoId) {
		equipamentoDao.removeEquipamentoDoGrupo(grupoId, equipamentoId);
	}
}
