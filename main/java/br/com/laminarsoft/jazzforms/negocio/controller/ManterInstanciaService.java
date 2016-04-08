package br.com.laminarsoft.jazzforms.negocio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.laminarsoft.jazzforms.persistencia.dao.InstanciaDao;
import br.com.laminarsoft.jazzforms.persistencia.model.Foto;
import br.com.laminarsoft.jazzforms.persistencia.model.Usuario;


@Service(value="instanciaService")
@SuppressWarnings("all")
@Transactional(propagation=Propagation.SUPPORTS, 
		isolation=Isolation.READ_UNCOMMITTED, 
		timeout=30, 
		rollbackFor=Exception.class)
public class ManterInstanciaService {

	@Autowired
	private InstanciaDao instanciaDao;
	
	
	public void reenviarInstanciaUsuario(Long instanciaId, String loginUsuario, String loginUsuarioAlteracao) {
		instanciaDao.reenviarInstanciaUsuario(instanciaId, loginUsuario, loginUsuarioAlteracao);
	}
	
	public void desfazerReenvioInstancia(Long instanciaId, String loginUsuarioAlteracao) {
		instanciaDao.desfazerReenvioInstancia(instanciaId, loginUsuarioAlteracao);
	}
	
	public List<Usuario> usuariosPossiveisReenvioInstancia(Long instanciaId) {
		return instanciaDao.usuariosPossiveisReenvioInstancia(instanciaId);
	}
	
	public List<Foto> getFotosPorInstancia(Long instanciaId) {
		return instanciaDao.getFotosPorInstancia(instanciaId);
	}
}
