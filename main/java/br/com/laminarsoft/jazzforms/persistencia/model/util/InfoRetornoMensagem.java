package br.com.laminarsoft.jazzforms.persistencia.model.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.laminarsoft.jazzforms.persistencia.model.Mensagem;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.MensagemVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.TipoSugestaoVO;

@SuppressWarnings("all")
@XmlRootElement(name="info_mensagem")
@XmlAccessorType(XmlAccessType.FIELD)
public class InfoRetornoMensagem extends InfoRetornoBase {
	
	public Mensagem deviceMessage;
	
	/**
	 * json com a seguinte formatação
	 * {tarefaAtribuida: true/false, usuarioAtribuido: loginusuario}
	 */
	public String tarefaAtribuida;
	public List<MensagemVO> deviceMessages = new ArrayList<MensagemVO>();
	public List<TipoSugestaoVO> tiposSugestao = new ArrayList<TipoSugestaoVO>();
	
}
