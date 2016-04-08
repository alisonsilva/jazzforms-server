package br.com.laminarsoft.jazzforms.persistencia.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.laminarsoft.jazzforms.persistencia.model.Equipamento;
import br.com.laminarsoft.jazzforms.persistencia.model.GrupoEquipamento;
import br.com.laminarsoft.jazzforms.persistencia.model.Localizacao;
import br.com.laminarsoft.jazzforms.persistencia.model.Usuario;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.IMensagensErro;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.ParametroException;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.EquipamentoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.GrupoEquipamentoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LocalizacaoVO;

@Transactional(propagation=Propagation.SUPPORTS, 
	isolation=Isolation.READ_COMMITTED, 
	timeout=30)
@Repository("EquipamentoDao")
@SuppressWarnings("all")
public class EquipamentoDao extends BaseDao<Equipamento> {

	public static final long serialVersionUID = 10l;
	
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=30)
	public void persistInfoEquipamento(final EquipamentoVO vo) {
		if(vo == null) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_ID_INVALIDO);
		}

		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String qEquipamento = "select * from equipamento where serial = ?";
				String qUsuario = "select id from usuario where upper(login) = ?";
				String iEquipamento = "insert into equipamento (usuario_id, marca, modelo, serial, data_inclusao) values (?,?,?,?,?)";
				String iLocalicacao = "insert into localizacao (equipamento_id, latitude, longitude, data, idx) values (?,?,?,?,?)";
				
				PreparedStatement pstmtQEquip = connection.prepareStatement(qEquipamento);
				pstmtQEquip.setString(1, vo.getDeviceUUID());
				ResultSet rs = pstmtQEquip.executeQuery();
				if(rs.next()) {
					Long idEquip = rs.getLong("id");
					PreparedStatement pstmtILoc = connection.prepareStatement(iLocalicacao);
					pstmtILoc.setLong(1, idEquip);
					Double latitude = new Double(0);
					Double longitude = new Double(0);
					if (StringUtils.isNotEmpty(vo.getLatitude())) {
						latitude = Double.valueOf(vo.getLatitude());
						longitude = Double.valueOf(vo.getLongitude());
					}
					pstmtILoc.setDouble(2, latitude);
					pstmtILoc.setDouble(3, longitude);
					if (vo.getDhEvento() != null) {
						pstmtILoc.setTimestamp(4, new Timestamp(vo.getDhEvento()));
					} else {
						pstmtILoc.setTimestamp(4, new Timestamp(new Date().getTime()));
					}
					pstmtILoc.setInt(5, 1);
					pstmtILoc.executeUpdate();
					pstmtILoc.close();
				} else {
					PreparedStatement pstmtQUsr = connection.prepareStatement(qUsuario);
					pstmtQUsr.setString(1, vo.getLoginUsuario().toUpperCase());
					ResultSet rsUsr = pstmtQUsr.executeQuery();
					if(rsUsr.next()) {
						Long idUsr = rsUsr.getLong("id");
						PreparedStatement pstmtIEquip = connection.prepareStatement(iEquipamento, Statement.RETURN_GENERATED_KEYS);
						pstmtIEquip.setLong(1, idUsr);
						pstmtIEquip.setString(2, vo.getDevicePlatform());
						pstmtIEquip.setString(3, vo.getDeviceName());
						pstmtIEquip.setString(4, vo.getDeviceUUID());
						pstmtIEquip.setTimestamp(5, new Timestamp(new Date().getTime()));
						pstmtIEquip.executeUpdate();
						ResultSet keys = pstmtIEquip.getGeneratedKeys();
						if(keys.next()) {
							Long equipId = keys.getLong(1);
							PreparedStatement pstmtILoc = connection.prepareStatement(iLocalicacao);
							pstmtILoc.setLong(1, equipId);
							Double latitude = new Double(0);
							Double longitude = new Double(0);
							if (StringUtils.isNotEmpty(vo.getLatitude())) {
								latitude = Double.valueOf(vo.getLatitude());
								longitude = Double.valueOf(vo.getLongitude());
							}
							pstmtILoc.setDouble(2, latitude);
							pstmtILoc.setDouble(3, longitude);
							if (vo.getDhEvento() != null) {
								pstmtILoc.setTimestamp(4, new Timestamp(vo.getDhEvento()));
							} else {
								pstmtILoc.setTimestamp(4, new Timestamp(new Date().getTime()));
							}
							pstmtILoc.setInt(5, 1);
							pstmtILoc.executeUpdate();
							pstmtILoc.close();
						}
						keys.close();
						pstmtIEquip.close();
					}
					pstmtQUsr.close();
				}
			}
		});

	}
	
	public List<EquipamentoVO> findEquipamentos() {
		List<EquipamentoVO> equipamentos = new ArrayList<EquipamentoVO>();
		
		List eqps = hibernateTemplate.find("from Equipamento e ");
		for(Object o : eqps) {
			Equipamento eqp = (Equipamento)o;
			EquipamentoVO eqpVo = new EquipamentoVO();
			eqpVo.setDeviceName(eqp.getModelo());
			eqpVo.setDevicePlatform(eqp.getMarca());
			eqpVo.setDeviceUUID(eqp.getSerial());
			eqpVo.setId(eqp.getId());
			eqpVo.setDataInclusao(eqp.getDataInclusao());
			eqp.getLocalizacoes().removeAll(Collections.singleton(null));
			Collections.sort(eqp.getLocalizacoes());
			
			for(Localizacao loc : eqp.getLocalizacoes()) {
				LocalizacaoVO lcVo = new LocalizacaoVO();
				lcVo.setData(loc.getData());
				lcVo.setId(loc.getId());
				lcVo.setLatitude(loc.getLatitude() == null ? null : String.valueOf(loc.getLatitude()));
				lcVo.setLongitude(loc.getLongitude() == null ? null : String.valueOf(loc.getLongitude()));
				eqpVo.getLocalizacoes().add(lcVo);
			}
			if (eqpVo.getLocalizacoes().size() > 0) {
	            LocalizacaoVO ultLoc = eqpVo.getLocalizacoes().get(eqpVo.getLocalizacoes().size() - 1);
	            eqpVo.setLatitude(ultLoc.getLatitude() == null ? null : String.valueOf(ultLoc.getLatitude()));
	            eqpVo.setLongitude(ultLoc.getLongitude() == null ? null : String.valueOf(ultLoc.getLongitude()));
            }
			Usuario ultUsrLogado = eqp.getUltimoUsuarioLogado();
			if(ultUsrLogado == null) {
				eqpVo.setLoginUsuario("Não identificado");
				eqpVo.setNomeUsuario("Não identificado");
				
			} else {
				eqpVo.setLoginUsuario(ultUsrLogado.getLogin());
				eqpVo.setNomeUsuario(ultUsrLogado.getNome());
			}
			equipamentos.add(eqpVo);
		}
		
		return equipamentos;
	}
	
	public List<GrupoEquipamentoVO> findGruposEquipamentos() {
		List<GrupoEquipamentoVO> grupos = new ArrayList<GrupoEquipamentoVO>();
		List grps = hibernateTemplate.find("from GrupoEquipamento ge");
		for(Object grp : grps) {
			GrupoEquipamento grpEquipamento = (GrupoEquipamento)grp;
			GrupoEquipamentoVO grupoVo = new GrupoEquipamentoVO();
			grupoVo.setDescricao(grpEquipamento.getDescricao());
			grupoVo.setId(grpEquipamento.getId());
			grupoVo.setNome(grpEquipamento.getNome());
			for(Equipamento eq : grpEquipamento.getEquipamentos()) {
				Usuario usuario = eq.getUltimoUsuarioLogado();
				EquipamentoVO eqVo = new EquipamentoVO();
				eqVo.setId(eq.getId());
				eqVo.setDeviceUUID(eq.getSerial());
				eqVo.setDevicePlatform(eq.getMarca());
				eqVo.setDeviceName(eq.getModelo());
				eqVo.setDataInclusao(eq.getDataInclusao());
				eqVo.setLoginUsuario(usuario.getLogin());
				eqVo.setNomeUsuario(usuario.getNome());
				grupoVo.getEquipamentos().add(eqVo);
			}
			grupos.add(grupoVo);
		}
		return grupos;
	}
	
	public void adicionaEquipamentoAoGrupo(final Long grupoId, final Long equipamentoId) {
		if(grupoId == null || grupoId == 0 ||
				equipamentoId == null || equipamentoId == 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_ID_INVALIDO);
		}
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				connection.createStatement().executeUpdate("insert into grp_equipamento_equipamento (equipamentoid, grupo_equipamentoid) values (" + equipamentoId + ", " + grupoId +")");
			}
		});
	}
	
	public void removeEquipamentoDoGrupo(final Long grupoId, final Long equipamentoId) {
		if(grupoId == null || grupoId == 0 ||
				equipamentoId == null || equipamentoId == 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_ID_INVALIDO);
		}
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				connection.createStatement().executeUpdate("delete from grp_equipamento_equipamento where equipamentoid = " + equipamentoId + " and grupo_equipamentoid = " + grupoId);
			}
		});
	}
	
	public void novoGrupoEquipamento(final GrupoEquipamentoVO grupoEquipamento) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work(){

			@Override
            public void execute(Connection connection) throws SQLException {
				PreparedStatement pstmt = connection.prepareStatement("insert into grupo_equipamento (nome, descricao) values (?,?)");
				pstmt.setString(1, grupoEquipamento.getNome());
				pstmt.setString(2, grupoEquipamento.getDescricao());
				pstmt.executeUpdate();
				pstmt.close();
            }
		});
	}
	
	public void alteraGrupoEquipamento(final GrupoEquipamentoVO grupoEquipamento) {
		if(grupoEquipamento == null || grupoEquipamento.getId() == null || grupoEquipamento.getId() == 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_ID_INVALIDO);
		}
		
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				PreparedStatement pstmt = connection.prepareStatement("update grupo_equipamento set nome = ?, descricao = ? where id = ?");
				pstmt.setString(1, grupoEquipamento.getNome());
				pstmt.setString(2, grupoEquipamento.getDescricao());
				pstmt.setLong(3, grupoEquipamento.getId());
				pstmt.executeUpdate();
				pstmt.close();
			}
		});
	}
	
	public void removeGrupoEquipamento(final Long idGrupoEquipamento) {
		if(idGrupoEquipamento == null || idGrupoEquipamento == 0) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_ID_INVALIDO);
		}
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String dltGrpEquipamento_equipamento = "delete from grp_equipamento_equipamento where grupo_equipamentoid = " + idGrupoEquipamento;
				String dltMensagem = "delete from mensagem where grupo_equipamento_id = " + idGrupoEquipamento;
				String dltGrupoEquipamento = "delete from grupo_equipamento where id = " + idGrupoEquipamento;
				
				connection.createStatement().executeUpdate(dltGrpEquipamento_equipamento);
				connection.createStatement().executeUpdate(dltMensagem);
				connection.createStatement().executeUpdate(dltGrupoEquipamento);
			}
		});
	}
	
}
