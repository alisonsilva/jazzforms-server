package br.com.laminarsoft.jazzforms.persistencia.model.util;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.laminarsoft.jazzforms.persistencia.model.Deployment;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.InstanciaVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.ProjetoImplantadoVO;

@SuppressWarnings("all")
@XmlRootElement(name="info_deployment")
@XmlAccessorType(XmlAccessType.FIELD)
public class InfoRetornoDeployments extends InfoRetornoBase {
	@XmlAttribute public Integer situacao;
	
	public List<Deployment> deployments;
	public List<ProjetoImplantadoVO> listaDeployments;
	public List<InstanciaVO> instancias;
	public Deployment deployment;
}
