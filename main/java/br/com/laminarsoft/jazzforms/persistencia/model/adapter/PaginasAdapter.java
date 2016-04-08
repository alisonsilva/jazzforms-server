package br.com.laminarsoft.jazzforms.persistencia.model.adapter;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.lang.StringUtils;

import br.com.laminarsoft.jazzforms.persistencia.model.ImplementacaoEvento;
import br.com.laminarsoft.jazzforms.persistencia.model.Pagina;
import br.com.laminarsoft.jazzforms.persistencia.model.adapter.PaginasAdapter.ListaRetornoPaginas;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Carousel;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Component;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Container;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Tab;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.TabPanel;

public class PaginasAdapter extends XmlAdapter<ListaRetornoPaginas, List<Pagina>>{

	
	@Override
	public List<Pagina> unmarshal(ListaRetornoPaginas vlrsPagina) throws Exception {
		List<Pagina> paginas = new ArrayList<Pagina>();
		for(ValorPagina vlrPagina : vlrsPagina.paginas) {
			Pagina pagina = new Pagina();
			pagina.setId(vlrPagina.id);
			pagina.setNome(vlrPagina.nome);
			pagina.setDescricao(vlrPagina.descricao);
			pagina.setBackgroundColor(vlrPagina.backgroundColor);
			pagina.setBackgroundImage(vlrPagina.backgroundImage);
			pagina.setContainer(vlrPagina.container);
			pagina.setHeight(vlrPagina.height);
			pagina.setMetodos(vlrPagina.metodos);
			pagina.setOrdem(vlrPagina.ordem);
			pagina.setParentXtype(vlrPagina.parentXType);
			pagina.pacoteCodigoCustomizadoTransformado = vlrPagina.pacoteCodigoCustomizadoTransformado;
			pagina.setPacoteCodigoCustomizacao(vlrPagina.pacoteCodigoCustomizadoTransformado != null ? vlrPagina.pacoteCodigoCustomizadoTransformado.getBytes() : null);
			pagina.getContainer().setPacoteCodigoCustomizado(vlrPagina.container.pacoteCodigoCustomizadoTransformado != null ? vlrPagina.container.pacoteCodigoCustomizadoTransformado.getBytes() : null);
			for(ImplementacaoEvento imp : pagina.getMetodos()) {
				imp.setImplementacao(imp.implementacaoTransformada != null ? imp.implementacaoTransformada.getBytes() : null);
			}
			pagina.setWidth(vlrPagina.width);
			paginas.add(pagina);
			unmarshallComponent(vlrPagina.container);
		}
		return paginas;
	}

	@Override
	public ListaRetornoPaginas marshal(List<Pagina> paginas) throws Exception {
		ListaRetornoPaginas vlrsPagina = new ListaRetornoPaginas();
		try {
			for(Pagina pg : paginas) {
				ValorPagina vlrPagina = new ValorPagina();
				vlrPagina.id = pg.getId();
				vlrPagina.nome = pg.getNome();
				vlrPagina.ordem = pg.getOrdem();
				vlrPagina.descricao = pg.getDescricao();
				vlrPagina.width = pg.getWidth();
				vlrPagina.height = pg.getHeight();
				vlrPagina.parentXType = pg.getParentXtype();
				vlrPagina.backgroundColor = pg.getBackgroundColor();
				vlrPagina.backgroundImage = pg.getBackgroundImage();
				vlrPagina.pacoteCodigoCustomizado = pg.getPacoteCodigoCustomizacao();
				vlrPagina.pacoteCodigoCustomizadoTransformado = pg.getPacoteCodigoCustomizacao() != null ? new String(pg.getPacoteCodigoCustomizacao()) : null;
				vlrPagina.container = pg.getContainer();
				vlrPagina.container.pacoteCodigoCustomizadoTransformado = pg.getContainer().getPacoteCodigoCustomizado() != null ? new String(pg.getContainer().getPacoteCodigoCustomizado()) : null;
				for(ImplementacaoEvento imp : pg.getMetodos()) {
					imp.implementacaoTransformada = imp.getImplementacao() != null ? new String(imp.getImplementacao()) : null;
				}
				vlrPagina.metodos = pg.getMetodos();				
				vlrsPagina.paginas.add(vlrPagina);
				marshalComponent(vlrPagina.container);
			}
		} catch (Throwable e) {
		}
		return vlrsPagina;
	}

	
	private void marshalComponent(Component cmp) {
		cmp.pacoteCodigoCustomizadoTransformado = cmp.getPacoteCodigoCustomizado() != null ? new String(cmp.getPacoteCodigoCustomizado()) : null;
		for(ImplementacaoEvento evt : cmp.getImplementacoes()) {
			evt.implementacaoTransformada = evt.getImplementacao() != null ? new String(evt.getImplementacao()) : null;
		}

		if(cmp instanceof Carousel) {
			Carousel car = (Carousel)cmp;
			for(Tab pag : car.getPaginas())	{
				for(Component carCmp : pag.getItems()) {
					marshalComponent(carCmp);
				}
			}
		} else if (cmp instanceof TabPanel) {
			TabPanel tp = (TabPanel)cmp;
			for(Tab tab : tp.getTabs()) {
				for(Component tabCmp : tab.getItems()) {
					marshalComponent(tabCmp);
				}
			}
		} else if(cmp instanceof Container) {
			Container cnt = (Container)cmp;
			for(Component cntCmp : cnt.getItems()) {
				marshalComponent(cntCmp);
			}
		}
	}
	
	
	private void unmarshallComponent(Component cmp) {
		cmp.setPacoteCodigoCustomizado(StringUtils.isEmpty(cmp.pacoteCodigoCustomizadoTransformado) ? null : cmp.pacoteCodigoCustomizadoTransformado.getBytes());
		for(ImplementacaoEvento evt : cmp.getImplementacoes()) {
			evt.setImplementacao(StringUtils.isEmpty(evt.implementacaoTransformada) ? null : evt.implementacaoTransformada.getBytes());
		}
		if(cmp instanceof Carousel) {
			Carousel car = (Carousel)cmp;
			for(Tab pag : car.getPaginas())	{
				for(Component carCmp : pag.getItems()) {
					unmarshallComponent(carCmp);
				}
			}
		} else if (cmp instanceof TabPanel) {
			TabPanel tp = (TabPanel)cmp;
			for(Tab tab : tp.getTabs()) {
				for(Component tabCmp : tab.getItems()) {
					unmarshallComponent(tabCmp);
				}
			}
		} else if(cmp instanceof Container) {
			Container cnt = (Container)cmp;
			for(Component cntCmp : cnt.getItems()) {
				unmarshallComponent(cntCmp);
			}
		}		
	}
	
	
	
	@XmlRootElement(name="paginas")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class ListaRetornoPaginas {
		public List<ValorPagina> paginas = new ArrayList<ValorPagina>();
	}	
	
	@XmlRootElement(name="pagina")
	@XmlAccessorType(value=XmlAccessType.FIELD)
	@SuppressWarnings("all")
	public static class ValorPagina {
		
		@XmlAttribute
		public Long id;
		
		@XmlAttribute public Integer ordem;
		@XmlAttribute public String nome;
		
		
		public String descricao;
		@XmlAttribute public double width;
		@XmlAttribute public double height;
		@XmlAttribute public String backgroundColor;
		@XmlAttribute public String parentXType;
		public byte[] backgroundImage;
		public byte[] pacoteCodigoCustomizado;
		public String pacoteCodigoCustomizadoTransformado;

		public Container container;
		
		public List<ImplementacaoEvento> metodos = new ArrayList<ImplementacaoEvento>();
	}	
}
