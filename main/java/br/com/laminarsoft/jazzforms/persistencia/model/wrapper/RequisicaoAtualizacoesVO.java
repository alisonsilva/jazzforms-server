package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("all")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="requisicao_atualizacoes")
public class RequisicaoAtualizacoesVO implements Serializable {
	@XmlAttribute public String login;
	@XmlAttribute public Integer ultimaMensagemId;
	@XmlAttribute public String token;
	
	public EquipamentoVO aparelho;
	public List<DeploymentVO> deployments;
	public List<EquipamentoVO> aparelhos;
}

