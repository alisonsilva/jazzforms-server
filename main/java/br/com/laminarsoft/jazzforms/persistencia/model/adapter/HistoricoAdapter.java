package br.com.laminarsoft.jazzforms.persistencia.model.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.hibernate.Hibernate;
import org.hibernate.LazyInitializationException;

import br.com.laminarsoft.jazzforms.persistencia.model.Historico;
import br.com.laminarsoft.jazzforms.persistencia.model.Usuario;
import br.com.laminarsoft.jazzforms.persistencia.model.adapter.HistoricoAdapter.ListaRetornoHistoricos;

public class HistoricoAdapter extends XmlAdapter<ListaRetornoHistoricos, List<Historico>> {


	@Override
	public List<Historico> unmarshal(ListaRetornoHistoricos valorHistoricos) throws Exception {
		List<Historico> historicos = new ArrayList<Historico>();
		for(ValorHistorico vlrHist : valorHistoricos.historicos) {
			Historico hist = new Historico();
			hist.setDescricao(vlrHist.descricao);
			hist.setId(vlrHist.id);
			Usuario usr = new Usuario();
			usr.setId(vlrHist.userId);
			SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
			hist.setDhAlteracao(sdFormat.parse(vlrHist.dhAlteracao));
			historicos.add(hist);
			hist.setUsuario(vlrHist.usuario);
		}
		return historicos;
	}

	@Override
	public ListaRetornoHistoricos marshal(List<Historico> historicos)
			throws Exception {
		ListaRetornoHistoricos valores = new ListaRetornoHistoricos();
		try {
			if (historicos != null && historicos.size() > 0) {
				for (Historico historico : historicos) {
					if (Hibernate.isInitialized(historico)) {
						ValorHistorico vlr = new ValorHistorico();
						vlr.id = historico.getId();
						vlr.descricao = historico.getDescricao();
						vlr.userId = historico.getUsuario().getId();
						SimpleDateFormat sp = new SimpleDateFormat(
								"yyyyMMdd HH:mm:ss");
						vlr.dhAlteracao = sp.format(historico.getDhAlteracao());
						valores.historicos.add(vlr);
						
						vlr.usuario = historico.getUsuario();
					}
				}
			}
		} catch (LazyInitializationException  e) {
		}
		return valores;
	}
	
	@XmlRootElement(name="historicos")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class ListaRetornoHistoricos {
		public List<ValorHistorico> historicos = new ArrayList<ValorHistorico>();
	}	

	@XmlRootElement(name="historico")
	@XmlAccessorType(value=XmlAccessType.FIELD)
	public static class ValorHistorico {
		@XmlAttribute
		public Long id;
		
		@XmlAttribute
		public String descricao;
		
		@XmlAttribute
		public String dhAlteracao;
		
		@XmlAttribute
		public Long userId;
		
		public Usuario usuario;
	}
}
