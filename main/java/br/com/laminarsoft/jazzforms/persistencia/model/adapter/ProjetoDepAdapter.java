package br.com.laminarsoft.jazzforms.persistencia.model.adapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.hibernate.LazyInitializationException;

import br.com.laminarsoft.jazzforms.persistencia.model.Projeto;
import br.com.laminarsoft.jazzforms.persistencia.model.adapter.ProjetoDepAdapter.ProjetoDeployment;

public class ProjetoDepAdapter extends XmlAdapter<ProjetoDeployment, Projeto> {


	@Override
	public Projeto unmarshal(ProjetoDeployment prjDeploy) throws Exception {
		Projeto projeto = new Projeto();
		if (prjDeploy != null) {
			projeto.setId(prjDeploy.id);
			projeto.setNome(prjDeploy.nome);
			projeto.setDescricao(prjDeploy.descricao);
		}
		return projeto;
	}

	@Override
	public ProjetoDeployment marshal(Projeto projeto) throws Exception {
		ProjetoDeployment prjDeploy = new ProjetoDeployment();
		try {
			prjDeploy.id = projeto.getId();
			prjDeploy.nome = projeto.getNome();
			prjDeploy.descricao = projeto.getDescricao();
		} catch (Throwable  e) {
			
		}
		return prjDeploy;
	}
	

	@XmlRootElement(name="projeto")
	@XmlAccessorType(value=XmlAccessType.FIELD)
	public static class ProjetoDeployment {
		@XmlAttribute public Long id;
		@XmlAttribute public String nome;
		@XmlAttribute public String descricao;

	}
}
