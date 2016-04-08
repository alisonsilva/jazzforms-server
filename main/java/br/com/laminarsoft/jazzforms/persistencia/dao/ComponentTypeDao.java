package br.com.laminarsoft.jazzforms.persistencia.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.laminarsoft.jazzforms.persistencia.model.ComponentType;
import br.com.laminarsoft.jazzforms.persistencia.model.TipoEvento;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.IMensagensErro;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.ParametroException;

@Transactional(propagation=Propagation.SUPPORTS, 
		isolation=Isolation.READ_COMMITTED, 
		timeout=30)
@Repository("ComponentTypeDao")
@SuppressWarnings("all")
public class ComponentTypeDao extends BaseDao<ComponentType> {
	private static final long serialVersionUID = 1L;
	
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=30, 
			rollbackFor=Exception.class)	
	public Long persist(ComponentType tipoEvento) throws ParametroException {
		Long ret = new Long(0);
		if (tipoEvento != null) {
			ret = super.persist(tipoEvento);
		} else {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
		}
		return ret;
	}	
	
	public List<ComponentType> findByName(String nome) {
		if (nome == null || nome.trim().length() == 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
		}
		List<ComponentType> cmps = hibernateTemplate.find("from ComponentType c where c.nomeComponent = ?", nome);
		return cmps;
	}
	
	public List<TipoEvento> getTiposEventoPorCType(Long ctypeId) {
		if (ctypeId == null || ctypeId == 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
		}
		
		List<TipoEvento> tipos = new ArrayList<TipoEvento>();
		tipos = hibernateTemplate.find("from TipoEvento tp where tp.component.id = ?", ctypeId); 
		return tipos;
	}
}
