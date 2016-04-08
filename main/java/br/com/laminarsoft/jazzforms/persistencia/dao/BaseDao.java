package br.com.laminarsoft.jazzforms.persistencia.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.laminarsoft.jazzforms.persistencia.model.exception.MuitosResultadosException;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.ParametroException;

/**
 * 
 * @author reiser
 *
 * @param <T> entidade a ser tratada nas transacoes.
 */
@Transactional(propagation=Propagation.SUPPORTS, 
		isolation=Isolation.READ_COMMITTED,  
		timeout=30)
@Repository("BaseDao")
public class BaseDao<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	protected ResourceBundle bundle;
	
	@Autowired
	protected HibernateTemplate hibernateTemplate;

	
	private Class<T> persistenceClazz;
	
	public BaseDao() {
	}
	
	public BaseDao(Class<T> persistenceClazz) {
		this.persistenceClazz = persistenceClazz;
	}

	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED,
			timeout=120)	
	public void remove(T entidade) {
		if (entidade == null) {
			throw new ParametroException("A entidade a ser removida não pode estar vazia", 1);
		}
			
		hibernateTemplate.delete(entidade);
	}

	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			rollbackFor=Exception.class,
			timeout=30)	
	public Long persist(T entidade) {
		if (entidade == null) {
			throw new ParametroException("A entidade a ser inserida não pode estar vazia", 1);
		}
		Long ret = (Long)hibernateTemplate.save(entidade);
		return ret;
	}
	
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			rollbackFor=Exception.class,
			timeout=30)	
	public void merge(T entidade) {
		if (entidade == null) {
			throw new ParametroException("A entidade a ser inserida não pode estar vazia", 1);
		}
		hibernateTemplate.merge(entidade);
	}
	

	public T findById(Long id) {
		return hibernateTemplate.get(getPersistenceClass(), id);
	}

	public List<T> findAll() throws MuitosResultadosException {
		return findAll(50);
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findAll(int limit) throws MuitosResultadosException {
		Query query = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery("SELECT c FROM " 
				+ getPersistenceClass().getSimpleName()
				+ " c ORDER BY id").setMaxResults(limit);
		List<T> retorno = query.list();
		
		if (retorno.size() >= limit) {
			throw new MuitosResultadosException();
		}
		return retorno;
	}

	@SuppressWarnings("unchecked")
	public List<T> findAllNoLimit() {
		List<T> retorno = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery("SELECT c FROM " 
				+ getPersistenceClass().getSimpleName() 
				+ " c ORDER BY id").list();
		return retorno;
	}

	public List<T> findAllFiltred(T entity) throws MuitosResultadosException {		
//		TODO: implementar utilizando reflecao
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private Class<T> getPersistenceClass() {
		if (persistenceClazz == null) {
			persistenceClazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		}
		return (Class<T>) persistenceClazz;
	}

}
