package br.com.laminarsoft.jazzforms.persistencia.dao;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.classic.Session;
import org.hibernate.jdbc.Work;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.laminarsoft.jazzforms.persistencia.model.ProcessModel;
import br.com.laminarsoft.jazzforms.persistencia.model.ProcessNode;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.IMensagensErro;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.ParametroException;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.ValorBPInstanceVO;

@Transactional(propagation=Propagation.SUPPORTS, 
	isolation=Isolation.READ_COMMITTED, 
	timeout=30)
@Repository("ProcessModelDao")
@SuppressWarnings("all")
public class ProcessModelDao extends BaseDao<ProcessModel> {

	
	public ProcessModel findByProcessModelId(String processModelId) {
		ProcessModel ret = null;
		if(processModelId == null || processModelId.trim().length() == 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
		}
		List<ProcessModel> lstret = hibernateTemplate.find("from ProcessModel i where i.processId = ?", processModelId);
		if (lstret.size() > 0) {
			ret = lstret.get(0);
		}
				
		return ret;
	}
	
	public void salvaProcessModel(final ProcessModel model) {
		if (model == null) {
			throw new ParametroException("A entidade a ser inserida não pode estar vazia", 1);
		}		
		if (model.getId() == null || model.getId() == 0) {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			session.doWork(new Work() {
				
				@Override
				public void execute(Connection connection) throws SQLException {
					PreparedStatement pstmt = connection.prepareStatement("insert into process_model (process_id, process_name, process_image) values (?,?,?)", 
							Statement.RETURN_GENERATED_KEYS);
					pstmt.setString(1, model.getProcessId());
					pstmt.setString(2, model.getProcessName());
					ByteArrayInputStream is = new ByteArrayInputStream(model.getProcessImage());
					pstmt.setBinaryStream(3, is);
					
					pstmt.executeUpdate();
					ResultSet rs = pstmt.getGeneratedKeys();
					Long id = 0l;
					if(rs.next()) {
						id = rs.getLong(1);
					}
					if(id > 0) {
						int idx = 1;
						PreparedStatement pstmtN = connection.prepareStatement("insert into process_node (process_model_id, node_id, node_name, idx) "
								+ "values (?,?,?,?)");
						for(ProcessNode node : model.getNodes()) {
							pstmtN.clearParameters();
							pstmtN.setLong(1, id);
							pstmtN.setString(2, node.getNodeId());
							pstmtN.setString(3, node.getNodeName());
							pstmtN.setInt(4, idx++);
							pstmtN.executeUpdate();
						}
					}
				}
			});
		} else {
			updateProcessModel(model);
		}
	}
	
	public void updateProcessModel(final ProcessModel model) {
		if(model == null || model.getId() == null || model.getId() == 0) {
			throw new ParametroException("A entidade a ser atualizada não pode estar vazia", 1);
		}
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				PreparedStatement pstmt = connection.prepareStatement("update process_model set process_id = ?, process_name = ?, process_image = ? where id = ?");
				pstmt.setString(1, model.getProcessId());
				pstmt.setString(2, model.getProcessName());
				ByteArrayInputStream is = new ByteArrayInputStream(model.getProcessImage());
				pstmt.setBinaryStream(3, is);
				pstmt.setLong(4, model.getId());
				
				pstmt.executeUpdate();
				
				connection.createStatement().executeUpdate("delete from process_node where process_model_id = " + model.getId());
				int idx = 1;
				PreparedStatement pstmtN = connection.prepareStatement("insert into process_node (process_model_id, node_id, node_name, idx) "
						+ "values (?,?,?,?)");
				for(ProcessNode node : model.getNodes()) {
					pstmtN.clearParameters();
					pstmtN.setLong(1, model.getId());
					pstmtN.setString(2, node.getNodeId());
					pstmtN.setString(3, node.getNodeName());
					pstmtN.setInt(4, idx++);
					pstmtN.executeUpdate();
				}
				pstmtN.close();
				pstmt.close();
			}
		});
	}
	
	public List<ValorBPInstanceVO> getValoresBPInstance(final Long bpInstanceId) {
		final List<ValorBPInstanceVO> valores = new ArrayList<ValorBPInstanceVO>();
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				PreparedStatement pstmt = connection.prepareStatement("select * from valor_bp_instance where bp_instance_id = ?");
				pstmt.setLong(1, bpInstanceId);
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()) {
					ValorBPInstanceVO vo = new ValorBPInstanceVO();
					Timestamp tm = rs.getTimestamp("dh_alteracao");
					vo.dhAlteracao = tm == null ? null : new Date(tm.getTime());
					vo.idCampo = rs.getString("id_campo");
					vo.valorCampo = rs.getString("valor_campo");
					valores.add(vo);
				}
			}
		});
		return valores;
	}
	
	public List<ProcessModel> findAll() {
		List<ProcessModel> processos = hibernateTemplate.find("from ProcessModel i order by i.processId");
		return processos;
	}
	
	
}
