package br.com.laminarsoft.jazzforms.negocio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.laminarsoft.jazzforms.persistencia.dao.PaginaDao;
import br.com.laminarsoft.jazzforms.persistencia.dao.ProjetoDao;
import br.com.laminarsoft.jazzforms.persistencia.model.Pagina;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.ParametroException;


@Service(value="paginaService")
@SuppressWarnings("all")
@Transactional(propagation=Propagation.SUPPORTS, 
		isolation=Isolation.READ_UNCOMMITTED, 
		timeout=30, 
		rollbackFor=Exception.class)
public class ManterPaginaService {

	@Autowired
	private ProjetoDao projetoDao;
	
	@Autowired
	private PaginaDao paginaDao;	
	
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=30, 
			rollbackFor=Exception.class,
			readOnly=false)
	public Pagina persist(Pagina pagina) throws ParametroException {
		pagina.setId(paginaDao.persist(pagina));
		return pagina;
	}	
	
	public Pagina findById(Long id) throws ParametroException {
		return paginaDao.findById(id);
	}
	
	public List<Pagina> findByProjetoId(Long projetoId) throws ParametroException {
		return paginaDao.findByProjetoId(projetoId);
	}
}
