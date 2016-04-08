package br.com.laminarsoft.jazzforms.persistencia.model;

import java.util.ArrayList;
import java.util.List;

public class AgrupadorInstancias {
	
	private List<Instancia> instancias = new ArrayList<Instancia>();

	public List<Instancia> getInstancias() {
		return instancias;
	}

	public void setInstancias(List<Instancia> instancias) {
		this.instancias = instancias;
	}	
}
