package br.com.laminarsoft.jazzforms.persistencia.model.senchatransform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SenchaImplementacao {
	public Impl[] implementacoes = null;	
	
	public SenchaImplementacao() {
		//implementacoes[0] = new Impl("fake", "function(){}");
	}

	public void addImplementacao(String metodo, String implementacao) {
		List<Impl> impls = new ArrayList<Impl>();
		if (implementacoes != null) {
			impls = new ArrayList<Impl>(Arrays.asList(implementacoes));
		}
		impls.add(new Impl(metodo, implementacao));
		implementacoes = impls.toArray(new Impl[impls.size()]);
	}
	
	public void addImplementacao(String element, String event, String metodo, String implementacao) {
		List<Impl> impls = new ArrayList<Impl>();
		if (implementacoes != null) {
			impls = new ArrayList<Impl>(Arrays.asList(implementacoes));
		}
		impls.add(new Impl(element, event, metodo, implementacao));
		implementacoes = impls.toArray(new Impl[impls.size()]);
	}
	
	
	public class Impl {
		public String element;
		public String event;
		public String nomeMetodo;
		public String implementacao;

		public Impl(String nomeMetodo, String implementacao) {
			this.nomeMetodo = nomeMetodo;
			this.implementacao = implementacao;
		}
		
		public Impl(String element, String event, String nomeMetodo, String implementacao) {
			this.element = element;
			this.event = event;
			this.nomeMetodo = nomeMetodo;
			this.implementacao = implementacao;
		}		
	}
}


