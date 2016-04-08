package br.com.laminarsoft.jazzforms.negocio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.laminarsoft.jazzforms.persistencia.dao.ComponentTypeDao;
import br.com.laminarsoft.jazzforms.persistencia.model.ComponentType;
import br.com.laminarsoft.jazzforms.persistencia.model.TipoEvento;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.ParametroException;


@Service(value="eventoService")
@SuppressWarnings("all")
@Transactional(propagation=Propagation.SUPPORTS, 
		isolation=Isolation.READ_UNCOMMITTED, 
		timeout=30, 
		rollbackFor=Exception.class)
public class ManterEventoService {

	@Autowired
	private ComponentTypeDao componentTypeDao;
	
	
	public List<ComponentType> findByName(String nome) throws ParametroException {
		return componentTypeDao.findByName(nome);
	}
	
	public List<TipoEvento> findTiposEventoByCT(Long ctypeId) {
		return componentTypeDao.getTiposEventoPorCType(ctypeId);
	}
}
