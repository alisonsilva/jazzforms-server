package br.com.laminarsoft.jazzforms.persistencia.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.laminarsoft.jazzforms.persistencia.model.Componente;
import br.com.laminarsoft.jazzforms.persistencia.model.ImplementacaoEvento;
import br.com.laminarsoft.jazzforms.persistencia.model.Pagina;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.IMensagensErro;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.ParametroException;


@Transactional(propagation=Propagation.SUPPORTS, 
		isolation=Isolation.READ_UNCOMMITTED, 
		timeout=30)
@Repository("PaginaDao")
@SuppressWarnings("all")
public class PaginaDao extends BaseDao<Pagina> {
	private static final long serialVersionUID = 1L;
	
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=30, 
			rollbackFor=Exception.class)
	@Override
	public Long persist(Pagina pagina) throws ParametroException {
		if (pagina == null) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
		}
		return super.persist(pagina);
	}
	
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=30, 
			rollbackFor=Exception.class)
	public void delete(Pagina pagina) throws ParametroException {
		if (pagina == null) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
		}
		super.remove(pagina);
	}	
	
	@Override
	public Pagina findById(Long id) throws ParametroException {
		if (id == null || id <= 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
		}
		Pagina pagina = super.findById(id);
		for(ImplementacaoEvento metodo : pagina.getMetodos()) {
			metodo.getId();
			metodo.getDescricao();
			metodo.getNome();
		}
		if (pagina.getContainer() != null) {
			pagina.getContainer().getId();
			pagina.getContainer().getAutoDestroy();
		}
		return pagina;
	}
	
	public List<Pagina> findByProjetoId(Long projetoId) throws ParametroException {
		if (projetoId == null || projetoId <= 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
		}
		List<Pagina> paginas = hibernateTemplate.find("from Pagina p where p.projeto.id = ?", projetoId);
		return paginas;
	}
}
