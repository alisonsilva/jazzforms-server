package br.com.laminarsoft.jazzforms.persistencia.model.adapter;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.hibernate.LazyInitializationException;

import br.com.laminarsoft.jazzforms.persistencia.model.Componente;
import br.com.laminarsoft.jazzforms.persistencia.model.Grafico;
import br.com.laminarsoft.jazzforms.persistencia.model.ListBox;
import br.com.laminarsoft.jazzforms.persistencia.model.Picture;
import br.com.laminarsoft.jazzforms.persistencia.model.TextField;
import br.com.laminarsoft.jazzforms.persistencia.model.adapter.ComponenteAdapter.ListaRetornoComponentes;

public class ComponenteAdapter extends XmlAdapter<ListaRetornoComponentes, List<Componente>> {

	@Override
	public ListaRetornoComponentes marshal(List<Componente> componentes) throws Exception {
		ListaRetornoComponentes valores = new ListaRetornoComponentes();

		try {
			for (Componente componente : componentes) {
				ValorComponente valor = new ValorComponente();
				valor.backgroundColor = componente.getBackgroundColor();
				valor.descricao = componente.getDescricao();
				valor.height = componente.getHeight();
				valor.id = componente.getId();
				valor.nome = componente.getNome();
				valor.pacoteCodigoCustomizacao = componente.getPacoteCodigoCustomizacao();
				valor.posicaoX = componente.getPosicaoX();
				valor.posicaoY = componente.getPosicaoY();
				valor.texto = componente.getTexto();
				valor.width = componente.getWidth();
				valor.tipoComponente = componente.getTipoComponente();
				valor.habilitado = componente.getHabilitado();
						
				if(componente instanceof ListBox) {
					valor.multiplaSelecao = ((ListBox)componente).getMultiplaSelecao();
					valor.combo = ((ListBox)componente).getCombo();
				} else if (componente instanceof TextField) {
					valor.password = ((TextField)componente).getPassword();
					valor.mascara = ((TextField)componente).getMascara();
					valor.qtdMinima = ((TextField)componente).getQtdMinima();
					valor.qtdMaxima = ((TextField)componente).getQtdMaxima();
					valor.qtdLInhaApresentacao = ((TextField)componente).getQtdLinhaApresentacao();
					valor.datePicker = ((TextField)componente).getDatePicker();
					valor.expressaoRegular = ((TextField)componente).getExpressaoRegular();
				} else if(componente instanceof Grafico) {
					valor.tipoGrafico = ((Grafico)componente).getTipoGrafico();
				} else if(componente instanceof Picture) {
					valor.imagem = ((Picture)componente).getImagem();
				}
						
						
						
				valores.componente.add(valor);
			}
		} catch (LazyInitializationException e) {
		}
		return valores;
	}

	@Override
	public List<Componente> unmarshal(ListaRetornoComponentes valores) throws Exception {
		List<Componente> componentes = new ArrayList<Componente>();
		for(ValorComponente valor : valores.componente) {
//			Componente componente = Componente.getInstancia(valor.tipoComponente);
//			componente.setId(valor.id);
//			componente.setBackgroundColor(valor.backgroundColor);
//			componente.setDescricao(valor.descricao);
//			componente.setHeight(valor.height);
//			componente.setNome(valor.nome);
//			componente.setPacoteCodigoCustomizacao(valor.pacoteCodigoCustomizacao);
//			componente.setPosicaoX(valor.posicaoX);
//			componente.setPosicaoY(valor.posicaoY);
//			componente.setTexto(valor.texto);
//			componente.setWidth(valor.width);
//			componente.setHabilitado(valor.habilitado);
//			if(componente instanceof ListBox) {
//				((ListBox)componente).setMultiplaSelecao(valor.multiplaSelecao);
//				((ListBox)componente).setCombo(valor.combo);
//			} else if(componente instanceof TextField) {
//				((TextField)componente).setPassword(valor.password);
//				((TextField)componente).setMascara(valor.mascara);
//				((TextField)componente).setQtdMaxima(valor.qtdMaxima);
//				((TextField)componente).setQtdMinima(valor.qtdMinima);
//				((TextField)componente).setQtdLinhaApresentacao(valor.qtdLInhaApresentacao);
//				((TextField)componente).setExpressaoRegular(valor.expressaoRegular);
//				((TextField)componente).setDatePicker(valor.datePicker);
//			} else if(componente instanceof Grafico) {
//				((Grafico)componente).setTipoGrafico(valor.tipoGrafico);
//			} else if (componente instanceof Picture) {
//				((Picture)componente).setImagem(valor.imagem);
//			}
//			componentes.add(componente);
		}
		
		return componentes;
	}
	
	@XmlRootElement(name="componentes")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class ListaRetornoComponentes {
		public List<ValorComponente> componente = new ArrayList<ValorComponente>();
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name="componente")
	public static class ValorComponente {
		@XmlAttribute public Long id;
		
		@XmlAttribute public String tipoComponente;
		
		@XmlAttribute public String nome; 	
		
		public String descricao;
		
		@XmlAttribute public String texto;
		
		@XmlAttribute public double posicaoX;
		
		@XmlAttribute public double posicaoY;
		
		@XmlAttribute public double width;
		
		@XmlAttribute public double height;
		
		@XmlAttribute public boolean multiplaSelecao;
		@XmlAttribute public boolean combo;
		@XmlAttribute public boolean datePicker;
		@XmlAttribute public boolean password;
		@XmlAttribute public boolean habilitado;
		@XmlAttribute public int qtdMaxima;
		@XmlAttribute public int qtdMinima;
		@XmlAttribute public int qtdLInhaApresentacao;
		@XmlAttribute public int tipoGrafico;
		
		public String expressaoRegular;
		
		public String mascara;
		
		public String backgroundColor;
		
		public byte[] pacoteCodigoCustomizacao;
		public byte[] imagem;
	}	
}


