package br.com.laminarsoft.jazzforms.negocio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.laminarsoft.jazzforms.persistencia.dao.ProcessModelDao;
import br.com.laminarsoft.jazzforms.persistencia.model.ProcessModel;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.ParametroException;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Icon;


@Service(value="processModelService")
@SuppressWarnings("all")
@Transactional(propagation=Propagation.SUPPORTS, 
		isolation=Isolation.READ_UNCOMMITTED, 
		timeout=30, 
		rollbackFor=Exception.class)
public class ManterProcessModelService {

	@Autowired
	private ProcessModelDao processModelDao;
	
	
	public ProcessModel findByProcessID(String processId) throws ParametroException {
		return processModelDao.findByProcessModelId(processId);
	}
	
	public List<ProcessModel> findAllWithName() throws ParametroException {
		return processModelDao.findAll();
	}	
	
	public void persist(ProcessModel model) {
		if(model.getId() == null || model.getId() == 0) {
			ProcessModel pmodel = processModelDao.findByProcessModelId(model.getProcessId());
			if (pmodel == null) {
				processModelDao.salvaProcessModel(model);
			} else {
				model.setId(pmodel.getId());
				processModelDao.updateProcessModel(model);
			}
		} else {
			processModelDao.updateProcessModel(model);
		}
	}
}
