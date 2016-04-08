package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="bpinstance")
@SuppressWarnings("all")
public class BPInstanceVO implements Serializable {
	
	@XmlAttribute public Long id;
	@XmlAttribute public Long deploymentId;
	@XmlAttribute public String processInstanceId;
	@XmlAttribute public String sessionId;

	public DeploymentVO deploymentVo;
	
	public List<ValorBPInstanceVO> valores = new ArrayList<ValorBPInstanceVO>();
	
}
