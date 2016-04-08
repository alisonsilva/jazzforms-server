package br.com.laminarsoft.jazzforms.persistencia.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.classic.Session;
import org.hibernate.jdbc.Work;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.laminarsoft.jazzforms.persistencia.model.Grupo;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.IMensagensErro;
import br.com.laminarsoft.jazzforms.persistencia.model.exception.ParametroException;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LdapGrupoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LdapUsuarioVO;

@Transactional(propagation=Propagation.SUPPORTS, 
	isolation=Isolation.READ_COMMITTED, 
	timeout=30)
@Repository("GrupoDao")
public class GrupoDao extends BaseDao<Grupo> {

	public static final long serialVersionUID = 11l;
	
	public List<LdapGrupoVO> getGrupos() {
		final List<LdapGrupoVO> grupos = new ArrayList<LdapGrupoVO>();
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String qGrupos = "select * from grupo";
				String qUsuariosGrupo = "select u.* from usuario u inner join usuario_grupo ug on ug.usuarioid = u.id where ug.grupoid = (select g.id from grupo g where g.nome = ?)";
				PreparedStatement pstmtUsuarios = connection.prepareStatement(qUsuariosGrupo);
				ResultSet rs = connection.createStatement().executeQuery(qGrupos);
				
				while(rs.next()) {
					LdapGrupoVO grupo = new LdapGrupoVO();
					grupo.setCn(rs.getString("nome"));
					grupo.setDescription(rs.getString("descricao"));
					grupo.setNome(grupo.getCn());
					grupos.add(grupo);
					pstmtUsuarios.clearParameters();
					pstmtUsuarios.setString(1, grupo.getNome());
					ResultSet rsUsuario = pstmtUsuarios.executeQuery();
					while(rsUsuario.next()) {
						LdapUsuarioVO usuario = new LdapUsuarioVO();
						usuario.setNome(rsUsuario.getString("nome"));
						usuario.setLogin(rsUsuario.getString("login"));
						usuario.setAtivo(rsUsuario.getBoolean("ativo"));
						usuario.setUid(rsUsuario.getString("uid"));
						usuario.setMail(rsUsuario.getString("email"));
						usuario.setAdvogado(rsUsuario.getBoolean("advogado"));
						usuario.setUsuarioExterno(rsUsuario.getBoolean("usuario_externo"));
						grupo.getUsuarios().add(usuario);
					}
				}
			}
		});
		return grupos;
	}
	
	public LdapGrupoVO getGrupo(final String grupoNome) {
		if(StringUtils.isEmpty(grupoNome)) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
		}
		final List<LdapGrupoVO> grupos = new ArrayList<LdapGrupoVO>();
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String qGrupo = "select * from grupo where nome = ?";
				PreparedStatement pstmt = connection.prepareStatement(qGrupo);
				pstmt.setString(1, grupoNome);
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()) {
					LdapGrupoVO grupo = new LdapGrupoVO();
					grupo.setNome(rs.getString("nome"));
					grupo.setCn(rs.getString("nome"));
					grupo.setDescription(rs.getString("descricao"));
					grupos.add(grupo);
				}
			}
		});
		return grupos.size() > 0 ? grupos.get(1) : null;
	}
	
	public List<LdapGrupoVO> getGruposUsuario(final String login) {
		final List<LdapGrupoVO> grupos = new ArrayList<LdapGrupoVO>();
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String qGruposUsuario = "select g.* from grupo g where g.id in (select gu.grupoid from usuario_grupo gu where gu.usuarioid = (select u.id from usuario u where upper(u.login) = ?))";
				PreparedStatement pstmt = connection.prepareStatement(qGruposUsuario);
				pstmt.setString(1, login.toUpperCase());
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()) {
					LdapGrupoVO grupo = new LdapGrupoVO();
					grupo.setNome(rs.getString("nome"));
					grupo.setDescription(rs.getString("descricao"));
					grupo.setCn(rs.getString("nome"));
					grupos.add(grupo);
				}
			}
		});
		return grupos;
	}
	
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=30)		
	public void removeUsuarioDoGrupo(final String nomeGrupo, final String loginUsuario) {
		if(StringUtils.isEmpty(nomeGrupo) || StringUtils.isEmpty(loginUsuario)) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
		}
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String uUG = "delete from usuario_grupo ug where ug.usuarioid = (select u.id from usuario u where upper(u.login) = ?) and ug.grupoid = (select g.id from grupo g where g.nome = ?)";
				PreparedStatement pstmt = connection.prepareStatement(uUG);
				pstmt.setString(1, loginUsuario.toUpperCase());
				pstmt.setString(2, nomeGrupo);
				pstmt.executeUpdate();				
			}
		});
	}
	
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=30)		
	public void adicionaUsuarioAoGrupo(final String loginUsuario, final String nomeGrupo) {
		if(StringUtils.isEmpty(nomeGrupo) || StringUtils.isEmpty(loginUsuario)) {
			throw new ParametroException(IMensagensErro.PARAMETRO_NULO, IMensagensErro.PARAMETRO_NULO_CODE);
		}
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String uUG = "insert into usuario_grupo (usuarioid, grupoid) values ((select u.id from usuario u where upper(u.login) = ?), (select g.id from grupo g where g.nome = ?))";
				PreparedStatement pstmt = connection.prepareStatement(uUG);
				pstmt.setString(1, loginUsuario.toUpperCase());
				pstmt.setString(2, nomeGrupo);
				pstmt.executeUpdate();				
			}
		});
	}
}
