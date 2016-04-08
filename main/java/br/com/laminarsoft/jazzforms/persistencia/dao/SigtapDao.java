package br.com.laminarsoft.jazzforms.persistencia.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.sigtap.CidVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.sigtap.NacionalidadeVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.sigtap.OcupacaoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.sigtap.ProcedimentoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.sigtap.TriboIndigenaVO;

@Transactional(propagation=Propagation.SUPPORTS, 
	isolation=Isolation.READ_COMMITTED,  
	timeout=30)
@Repository("SigtapDao")
public class SigtapDao {

	//@Autowired protected HibernateTemplate hibernateSigtap;
	
	protected HibernateTemplate hibernateSigtap;
	public List<CidVO> getCids(){
		Session session = hibernateSigtap.getSessionFactory().getCurrentSession();
		final List<CidVO> cids = new ArrayList<CidVO>();
		
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				ResultSet rs = connection.createStatement().executeQuery("select co_cid, no_cid, tp_agravo, tp_sexo, dh_ultima_atualizacao from sigtap_cid");
				while(rs.next()) {
					CidVO vo = new CidVO();
					vo.coCid = rs.getString("co_cid");
					vo.noCid = rs.getString("no_cid");
					vo.tpAgravo = rs.getString("tp_agravo");
					vo.tpSexo = rs.getString("tp_sexo");
					vo.dhUltimaAtualizacao = rs.getDate("dh_ultima_atualizacao");
					cids.add(vo);
				}
			}
		});
		
		return cids;
	}
	
	public List<NacionalidadeVO> getNaconalidades(){
		Session session = hibernateSigtap.getSessionFactory().getCurrentSession();
		final List<NacionalidadeVO> nacionalidades = new ArrayList<NacionalidadeVO>();
		
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				ResultSet rs = connection.createStatement().executeQuery("select co_nacionalidade, ds_nacionalidade, dh_ultima_atualizacao from sgas_nacionalidade");
				while(rs.next()) {
					NacionalidadeVO vo = new NacionalidadeVO();
					vo.coNacionalidade = rs.getLong("co_nacionalidade");
					vo.dsNacionalidade = rs.getString("ds_nacionalidade");
					vo.dhUltimaAtualizacao = rs.getDate("dh_ultima_atualizacao");
					nacionalidades.add(vo);
				}
			}
		});
		
		return nacionalidades;
	}	
	
	public List<TriboIndigenaVO> getTribos(){
		Session session = hibernateSigtap.getSessionFactory().getCurrentSession();
		final List<TriboIndigenaVO> nacionalidades = new ArrayList<TriboIndigenaVO>();
		
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				ResultSet rs = connection.createStatement().executeQuery("select co_tribo_indigena, no_tribo_indigena, dh_ultima_atualizacao from sgas_tribo_indigena");
				while(rs.next()) {
					TriboIndigenaVO vo = new TriboIndigenaVO();
					vo.coTriboIndigena = rs.getLong("co_tribo_indigena");
					vo.noTriboIndigena = rs.getString("no_tribo_indigena");
					vo.dhUltimaAtualizacao = rs.getDate("dh_ultima_atualizacao");
					nacionalidades.add(vo);
				}
			}
		});
		
		return nacionalidades;
	}	
	
	public List<OcupacaoVO> getOcupacoes(){
		Session session = hibernateSigtap.getSessionFactory().getCurrentSession();
		final List<OcupacaoVO> nacionalidades = new ArrayList<OcupacaoVO>();
		
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				ResultSet rs = connection.createStatement().executeQuery("select co_ocupacao, no_ocupacao, dh_ultima_atualizacao from sigtap_ocupacao");
				while(rs.next()) {
					OcupacaoVO vo = new OcupacaoVO();
					vo.coOcupacao = rs.getString("co_ocupacao");
					vo.noOcupacao = rs.getString("no_ocupacao");
					vo.dhUltimaAtualizacao = rs.getDate("dh_ultima_atualizacao");
					nacionalidades.add(vo);
				}
			}
		});
		
		return nacionalidades;
	}	
	
	public List<ProcedimentoVO> getProcedimentos(){
		Session session = hibernateSigtap.getSessionFactory().getCurrentSession();
		final List<ProcedimentoVO> nacionalidades = new ArrayList<ProcedimentoVO>();
		
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				ResultSet rs = connection.createStatement().executeQuery("select co_procedimento, no_procedimento, dh_ultima_atualizacao from sigtap_procedimento");
				while(rs.next()) {
					ProcedimentoVO vo = new ProcedimentoVO();
					vo.coProcedimento = rs.getString("co_procedimento");
					vo.noProcedimento = rs.getString("no_procedimento");
					vo.dhUltimaAtualizacao = rs.getDate("dh_ultima_atualizacao");
					nacionalidades.add(vo);
				}
			}
		});
		
		return nacionalidades;
	}		
	
	public Long getUltimaAtualizacao(final String opcao) {
		final Date dhAtualizacaoDispositovo = new Date();
		
		Session session = hibernateSigtap.getSessionFactory().getCurrentSession();
		
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String tabela = "";
				switch (opcao) {
				case "cid" : 
					tabela = "sigtap_cid";
					break;
				case "nacionalidade" :
					tabela = "sgas_nacionalidade";
					break;
				case "tribo_indigena" :
					tabela = "sgas_tribo_indigena";
					break;
				case "ocupacao" :
					tabela = "sigtap_ocupacao";
					break;
				case "procedimento" :
					tabela = "sigtap_procedimento";
					break;
				}				
				
				ResultSet rs = connection.createStatement().executeQuery("select distinct dh_ultima_atualizacao from " + tabela);
				if(rs.next()) {
					dhAtualizacaoDispositovo.setTime(rs.getDate(1).getTime());
				}
			}
		});
		return dhAtualizacaoDispositovo.getTime();
	}
	
	/**
	 * Verifica se há a necessidade de se atualizar a listagem com as opções de cid
	 * @param ultimaAtualizacao data da ultima atualização no dispositivo formato timestamp
	 * @return
	 */
	public Boolean necessitaAtualizacao(Long ultimaAtualizacao, final String opcao) {
		final List<Boolean> ret = new ArrayList<Boolean>();
		ret.add(new Boolean(false));
		if (ultimaAtualizacao == null || ultimaAtualizacao == 0) {
			ret.clear();
			ret.add(true);
		} else {
			final Date dhAtualizacaoDispositovo = new Date();
			dhAtualizacaoDispositovo.setTime(ultimaAtualizacao);
			
			Session session = hibernateSigtap.getSessionFactory().getCurrentSession();
			
			session.doWork(new Work() {
				
				@Override
				public void execute(Connection connection) throws SQLException {
					String tabela = "";
					switch (opcao) {
					case "cid" : 
						tabela = "sigtap_cid";
						break;
					case "nacionalidade" :
						tabela = "sgas_nacionalidade";
						break;
					case "tribo_indigena" :
						tabela = "sgas_tribo_indigena";
						break;
					case "ocupacao" :
						tabela = "sigtap_ocupacao";
						break;
					case "procedimento" :
						tabela = "sigtap_procedimento";
						break;
					}
					ResultSet rs = connection.createStatement().executeQuery("select distinct dh_ultima_atualizacao from " + tabela);
					if(rs.next()) {
						if (rs.getDate(1).compareTo(dhAtualizacaoDispositovo) > 0 ) {
							ret.clear();
							ret.add(true);
						}
					}
				}
			});
		}		
		
		return ret.get(0);
	}
	
}
