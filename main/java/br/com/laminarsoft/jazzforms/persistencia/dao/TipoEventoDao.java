package br.com.laminarsoft.jazzforms.persistencia.dao;

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
@Repository("TipoEventoDao")
public class TipoEventoDao extends BaseDao<TipoEvento> {
	private static final long serialVersionUID = 1L;
	
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=30, 
			rollbackFor=Exception.class)	
	public Long persist(TipoEvento tipoEvento) throws ParametroException {
		Long ret = new Long(0);
		if (tipoEvento != null) {
			if(tipoEvento.getComponent().getId() > 0) {
				ComponentType ctype = hibernateTemplate.get(ComponentType.class, tipoEvento.getComponent().getId());
				tipoEvento.setComponent(ctype);
			}
			ret = super.persist(tipoEvento);
		} else {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
		}
		return ret;
	}	
}
