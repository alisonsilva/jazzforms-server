package br.com.laminarsoft.jazzforms.negocio.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNRevisionProperty;
import org.tmatesoft.svn.core.io.SVNFileRevision;

import br.com.laminarsoft.jazzforms.negocio.controller.util.SVNController;
import br.com.laminarsoft.jazzforms.persistencia.model.Projeto;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.SVNProjetoVO;

@Service(value="svnService")
@SuppressWarnings("all")
@Transactional(propagation=Propagation.SUPPORTS, 
		isolation=Isolation.READ_UNCOMMITTED, 
		timeout=30)
public class ManterSVNService {

	@Autowired private SVNController svnController;

	public List<SVNProjetoVO> recuperaVersoesProjeto(String nomeProjeto) throws SVNException {
		List<SVNProjetoVO> versoes = new ArrayList<SVNProjetoVO>();
		Collection coll = null;
		try {
			coll = svnController.getFileLog(nomeProjeto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(coll != null) {
			Iterator iter = coll.iterator();
			while(iter.hasNext()) {
				SVNFileRevision rev = (SVNFileRevision)iter.next();
				SVNProjetoVO vo = new SVNProjetoVO();
				vo.setVersao(String.valueOf(rev.getRevision()));
				vo.setDescricao(rev.getRevisionProperties().getStringValue(SVNRevisionProperty.LOG));
				vo.setDataAtualizacao(rev.getRevisionProperties().getStringValue(SVNRevisionProperty.DATE));
				vo.setNome(nomeProjeto);
				versoes.add(vo);
			}
		}
		return versoes;
	}
	
	public Projeto checkOutProjeto(String nomeProjeto, String versao) throws SVNException {
		return svnController.checkoutProjeto(nomeProjeto, versao);
	}

}
