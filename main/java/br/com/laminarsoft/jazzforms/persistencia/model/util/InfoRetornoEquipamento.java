package br.com.laminarsoft.jazzforms.persistencia.model.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.EquipamentoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.GrupoEquipamentoVO;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LocalizacaoVO;

@SuppressWarnings("all")
@XmlRootElement(name="info_equipamento")
@XmlAccessorType(XmlAccessType.FIELD)
public class InfoRetornoEquipamento extends InfoRetornoBase {
	
	public EquipamentoVO equipamento;
	public LocalizacaoVO ultimaLocalizacao;
	public List<EquipamentoVO> equipamentos = new ArrayList<EquipamentoVO>();
	public List<LocalizacaoVO> localizacoes = new ArrayList<LocalizacaoVO>(); 
	public List<GrupoEquipamentoVO> gruposEquipamentos = new ArrayList<GrupoEquipamentoVO>();
}
