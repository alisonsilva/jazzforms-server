package br.com.laminarsoft.jazzforms.persistencia.model.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.laminarsoft.jazzforms.persistencia.model.Land;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LandVO;

@SuppressWarnings("all")
@XmlRootElement(name="info_lands")
@XmlAccessorType(XmlAccessType.FIELD)
public class InfoRetornoLands extends InfoRetornoBase {
	public List<LandVO> landItems = new ArrayList<LandVO>();
}
