package br.com.laminarsoft.jazzforms.negocio.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.laminarsoft.jazzforms.persistencia.dao.SigtapDao;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.DataviewOpcaoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.sigtap.CidVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.sigtap.NacionalidadeVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.sigtap.OcupacaoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.sigtap.ProcedimentoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.sigtap.TriboIndigenaVO;

@Service(value="sigtapService")
@SuppressWarnings("all")
@Transactional(propagation=Propagation.SUPPORTS, 
		isolation=Isolation.READ_UNCOMMITTED, 
		timeout=30)
public class ManterSigtapService {

	@Autowired private SigtapDao sigtapDao;	
	
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=30,
			readOnly=false)
	public List<DataviewOpcaoVO> getAllCids() throws Exception {
		List<CidVO> cids = sigtapDao.getCids();
		List<DataviewOpcaoVO> opcoes = new ArrayList<DataviewOpcaoVO>();
		for(CidVO vo : cids) {
			DataviewOpcaoVO opcao = new DataviewOpcaoVO();
			opcao.setTexto(vo.coCid + " - " + vo.noCid);
			opcao.setValor(vo.coCid);
			opcoes.add(opcao);
		}
		return opcoes;
	}
	
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=30,
			readOnly=false)
	public List<DataviewOpcaoVO> getAllNacionalidades() throws Exception {
		List<NacionalidadeVO> nacionalidades = sigtapDao.getNaconalidades();
		List<DataviewOpcaoVO> opcoes = new ArrayList<DataviewOpcaoVO>();
		for(NacionalidadeVO vo : nacionalidades) {
			DataviewOpcaoVO opcao = new DataviewOpcaoVO();
			opcao.setTexto(vo.coNacionalidade + " - " + vo.dsNacionalidade);
			opcao.setValor(vo.coNacionalidade + "");
			opcoes.add(opcao);
		}
		return opcoes;
	}	

	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=30,
			readOnly=false)
	public List<DataviewOpcaoVO> getAllTribosIndigenas() throws Exception {
		List<TriboIndigenaVO> nacionalidades = sigtapDao.getTribos();
		List<DataviewOpcaoVO> opcoes = new ArrayList<DataviewOpcaoVO>();
		for(TriboIndigenaVO vo : nacionalidades) {
			DataviewOpcaoVO opcao = new DataviewOpcaoVO();
			opcao.setTexto(vo.coTriboIndigena + " - " + vo.noTriboIndigena);
			opcao.setValor(vo.coTriboIndigena + "");
			opcoes.add(opcao);
		}
		return opcoes;
	}
	
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=30,
			readOnly=false)
	public List<DataviewOpcaoVO> getAllOcupacoes() throws Exception {
		List<OcupacaoVO> ocupacoes = sigtapDao.getOcupacoes();
		List<DataviewOpcaoVO> opcoes = new ArrayList<DataviewOpcaoVO>();
		for(OcupacaoVO vo : ocupacoes) {
			DataviewOpcaoVO opcao = new DataviewOpcaoVO();
			opcao.setTexto(vo.coOcupacao + " - " + vo.noOcupacao);
			opcao.setValor(vo.coOcupacao);
			opcoes.add(opcao);
		}
		return opcoes;
	}
		
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=30,
			readOnly=false)
	public List<DataviewOpcaoVO> getAllProcedimentos() throws Exception {
		List<ProcedimentoVO> procedimentos = sigtapDao.getProcedimentos();
		List<DataviewOpcaoVO> opcoes = new ArrayList<DataviewOpcaoVO>();
		for(ProcedimentoVO vo : procedimentos) {
			DataviewOpcaoVO opcao = new DataviewOpcaoVO();
			opcao.setTexto(vo.coProcedimento + " - " + vo.noProcedimento);
			opcao.setValor(vo.coProcedimento);
			opcoes.add(opcao);
		}
		return opcoes;
	}	
	
	public Boolean necessitaAtualizacaoCid(Long ultimaAtualizacao) {
		return sigtapDao.necessitaAtualizacao(ultimaAtualizacao, "cid");
	}
	
	public Boolean necessitaAtualizacaoNacionalidade(Long ultimaAtualizacao) {
		return sigtapDao.necessitaAtualizacao(ultimaAtualizacao, "nacionalidade");
	}

	public Boolean necessitaAtualizacaoTriboIndigena(Long ultimaAtualizacao) {
		return sigtapDao.necessitaAtualizacao(ultimaAtualizacao, "tribo_indigena");
	}

	public Boolean necessitaAtualizacaoProcedimento(Long ultimaAtualizacao) {
		return sigtapDao.necessitaAtualizacao(ultimaAtualizacao, "procedimento");
	}
	
	public Boolean necessitaAtualizacaoOcupacao(Long ultimaAtualizacao) {
		return sigtapDao.necessitaAtualizacao(ultimaAtualizacao, "ocupacao");
	}	
	
	public Long getUltimaAtualizacaoCid() {
		return sigtapDao.getUltimaAtualizacao("cid");
	}
	
	public Long getUltimaAtualizacaoNacionalidade() {
		return sigtapDao.getUltimaAtualizacao("nacionalidade");
	}
	
	public Long getUltimaAtualizacaoTriboIndigena() {
		return sigtapDao.getUltimaAtualizacao("tribo_indigena");
	}
	
	public Long getUltimaAtualizacaoOcupacao() {
		return sigtapDao.getUltimaAtualizacao("ocupacao");
	}	
	
	public Long getUltimaAtualizacaoProcedimento() {
		return sigtapDao.getUltimaAtualizacao("procedimento");
	}	
}
