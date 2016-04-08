package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("all")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="requisicao_atu_localizacao")
public class RequisicaoAtualizacaoLocalizacaoVO implements Serializable {
	@XmlAttribute public String login;
	@XmlAttribute public String token;
	@XmlAttribute public String deviceName;
	@XmlAttribute public String devicePlatform;
	@XmlAttribute public String deviceUUID;
	
	public List<LocalizacaoVO> localizacoes = new ArrayList<LocalizacaoVO>();
}

