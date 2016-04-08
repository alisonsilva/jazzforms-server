package br.com.laminarsoft.jazzforms.persistencia.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.laminarsoft.jazzforms.negocio.controller.util.PropertiesServiceController;
import br.com.laminarsoft.jazzforms.persistencia.model.Grupo;
import br.com.laminarsoft.jazzforms.persistencia.model.Land;
import br.com.laminarsoft.jazzforms.persistencia.model.LandEntry;

@Transactional(propagation=Propagation.SUPPORTS, 
	isolation=Isolation.READ_COMMITTED, 
	timeout=30)
@Repository("LandDao")
@SuppressWarnings("all")
public class LandDao extends BaseDao<Land> {

	public static final long serialVersionUID = 10l;

	@Autowired private PropertiesServiceController propertiesServiceController;


	public List<Land> getLandsToCrawl() {
		final List<Land> lands = new ArrayList<Land>();
		
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String qLand = "select l.* "
						+ "from land l "
						+ "where l.url is not null and l.classe_manipulacao is not null";
				
				String qGrupoLand = "select g.* from grupo g "
						+ "inner join grupo_land gl on gl.grupo_id = g.id "
						+ "where gl.land_id = ?";
				
				ResultSet rs = connection.createStatement().executeQuery(qLand);
				PreparedStatement pstmt = connection.prepareStatement(qGrupoLand);
				while(rs.next()) {
					pstmt.clearParameters();
					Land land = new Land();
					
					pstmt.setLong(1, rs.getLong("id"));
					ResultSet rsGrupo = pstmt.executeQuery();
					
					while (rsGrupo.next()) {
						Grupo grupo = new Grupo();
						grupo.setId(rsGrupo.getLong("id"));
						grupo.setNome(rsGrupo.getString("nome"));
						grupo.setDescricao(rsGrupo.getString("descricao"));
						land.getGrupos().add(grupo);
					}
					
					land.setId(rs.getLong("id"));
					land.setUrl(rs.getString("url"));
					land.setIconCls(rs.getString("icon_cls"));
					land.setDhInclusao(rs.getTimestamp("dh_inclusao"));
					land.setPadraoExclusao(rs.getString("padrao_exclusao"));
					land.setCategoria(rs.getString("categoria"));
					land.setQtdNiveis(rs.getInt("qtd_niveis"));
					land.setNomeFonte(rs.getString("nome_fonte"));
					land.setClasseManipulacao(rs.getString("classe_manipulacao"));
					lands.add(land);
				}
			}
		});
		
		return lands;
	}
	
	
	@Transactional(propagation=Propagation.REQUIRED, 
			isolation=Isolation.READ_COMMITTED, 
			timeout=50)		
	public void gravaLandEntries(final Land land) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String qRemoveEntries = "delete from land_entry where land_id = ?";
				String qInsertEtries = "insert into land_entry (land_id, titulo, texto, url, dh_inclusao, icone, abrir_url, idx, icone_url, icone_tipo, dh_noticia) values (?, ?, ?, ?, ?, ?, ?, "
						+ "(select case when max(le.idx) is null then 1 else max(le.idx) + 1 end  as idx_novo from land_entry le), ?, ?, ?)";
				PreparedStatement pstmt = connection.prepareStatement(qRemoveEntries);
				pstmt.setLong(1, land.getId());
				pstmt.executeUpdate();
				pstmt.close();
				
				pstmt = connection.prepareStatement(qInsertEtries);
				
				for(LandEntry entry : land.getEntries()) {
					pstmt.clearParameters();
					int idx = 1;
					pstmt.setLong(idx++, land.getId());
					pstmt.setString(idx++, entry.getTitulo());
					pstmt.setString(idx++, entry.getTexto());
					pstmt.setString(idx++, entry.getUrl());
					pstmt.setTimestamp(idx++, new Timestamp(entry.getDhInclusao().getTime()));
					if (entry.getIcone() != null) {
						pstmt.setBytes(idx++, entry.getIcone());
					} else {
						pstmt.setNull(idx++, Types.BLOB);
					}
					pstmt.setBoolean(idx++, entry.getAbrirUrlDiretamente());
					pstmt.setString(idx++, entry.getIconeUrl());
					pstmt.setString(idx++, entry.getIconeTipo());
					if(entry.getDhNoticia() != null) {
						pstmt.setTimestamp(idx++, new Timestamp(entry.getDhNoticia().getTime()));
					} else {
						pstmt.setNull(idx++, Types.TIMESTAMP);
					}
					pstmt.executeUpdate();
				}				
			}
		});
	}
	
	public List<Land> getLandEntriesPorUsuario(final String login) {
		final List<Land> lands = new ArrayList<Land>();
		
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				Map<String, Land> recuperados = new HashMap<String, Land>();
				String qLandsUsuario = "select l.* from land l "
						+ "inner join grupo_land gl on l.id = gl.land_id "
						+ "where gl.grupo_id in (select gu.grupoid from usuario_grupo gu where gu.usuarioid = "
						+ "(select id from usuario where upper(login) = ?))";
				
				String qGrupo = "select g.* from grupo g "
						+ "inner join grupo_land gl on gl.grupo_id = g.id "
						+ "where gl.land_id = ?";
				
				String qLandEntries = "select * from land_entry where land_id = ?";
				
				PreparedStatement pstmt = connection.prepareStatement(qLandsUsuario);
				PreparedStatement pstmtGrupo = connection.prepareStatement(qGrupo);
				pstmt.setString(1, login.toUpperCase());
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()) {
					Land land = new Land();
					
					pstmtGrupo.clearParameters();
					pstmtGrupo.setLong(1, rs.getLong("id"));
					ResultSet rsGrupo = pstmtGrupo.executeQuery();
					
					while (rsGrupo.next()) {
						Grupo grp = new Grupo();
						grp.setId(rsGrupo.getLong("id"));
						grp.setDescricao(rsGrupo.getString("descricao"));	
						grp.setNome(rsGrupo.getString("nome"));
						land.getGrupos().add(grp);
					}
					land.setIconCls(rs.getString("icon_cls"));
					land.setUrl(rs.getString("url"));
					land.setDhInclusao(rs.getTimestamp("dh_inclusao"));
					land.setCategoria(rs.getString("categoria"));
					land.setNomeFonte(rs.getString("nome_fonte"));
					land.setQtdNiveis(rs.getInt("qtd_niveis"));
					land.setPadraoExclusao(rs.getString("padrao_exclusao"));
					land.setClasseManipulacao(rs.getString("classe_manipulacao"));
					land.setId(rs.getLong("id"));
					recuperados.put(land.getNomeFonte(), land);
				}
				
				rs.close();
				pstmt.close();
				
				pstmt = connection.prepareStatement(qLandEntries);
				for(Land land : recuperados.values()) {
					pstmt.clearParameters();
					pstmt.setLong(1, land.getId());
					rs = pstmt.executeQuery();
					while(rs.next()) {
						LandEntry entry = new LandEntry();
						entry.setIcone(rs.getBytes("icone"));
						entry.setId(rs.getLong("id"));
						entry.setTitulo(rs.getString("titulo"));
						entry.setTexto(rs.getString("texto"));
						entry.setUrl(rs.getString("url"));
						entry.setDhInclusao(rs.getTimestamp("dh_inclusao"));
						entry.setAbrirUrlDiretamente(rs.getBoolean("abrir_url"));
						entry.setIconeUrl(rs.getString("icone_url"));
						entry.setIconeTipo(rs.getString("icone_tipo"));
						land.getEntries().add(entry);
						entry.setLand(land);
					}
					rs.close();
				}
				pstmt.close();
				
				lands.addAll(recuperados.values());
				
			}
		});
		
		return lands;
	}
}
