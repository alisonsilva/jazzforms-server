package br.com.laminarsoft.jazzforms.persistencia.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.laminarsoft.jazzforms.persistencia.model.exception.IMensagensErro;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.ParametroException;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Icon;

@Transactional(propagation=Propagation.SUPPORTS, 
	isolation=Isolation.READ_COMMITTED, 
	timeout=30)
@Repository("IconDao")
@SuppressWarnings("all")
public class IconDao extends BaseDao<Icon> {

	
	public Icon findByName(String name) {
		Icon ret = null;
		if(name == null || name.trim().length() == 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
		}
		List<Icon> lstret = hibernateTemplate.find("from Icon i where i.nome = ?", name);
		if (lstret.size() > 0) {
			ret = lstret.get(0);
		}
				
		return ret;
	}
	
	public List<Icon> findAllWithNames() {
		List<Icon> icons = hibernateTemplate.find("from Icon i where i.nome is not null and i.nome <> '' order by i.nome");
		return icons;
	}
}
