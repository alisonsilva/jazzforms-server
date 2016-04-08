package br.com.laminarsoft.jazzforms.persistencia.model.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.ValorBPInstanceVO;

@SuppressWarnings("all")
@XmlRootElement(name="info_bp_instance")
@XmlAccessorType(XmlAccessType.FIELD)
public class InfoRetornoValorBPInstance extends InfoRetornoBase implements Serializable{
	@XmlAttribute public Long bpInstanceId;
	
	public List<ValorBPInstanceVO> valores = new ArrayList<ValorBPInstanceVO>();
	
	
}
