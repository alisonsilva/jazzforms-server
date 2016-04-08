package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="deployment")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeploymentVO {
	@XmlAttribute public Long id;
	@XmlAttribute public Long dhPublicacao;
	@XmlAttribute public Long dhAlteracao;
	@XmlAttribute public Boolean ativo;
	@XmlAttribute public Boolean removido;
}
