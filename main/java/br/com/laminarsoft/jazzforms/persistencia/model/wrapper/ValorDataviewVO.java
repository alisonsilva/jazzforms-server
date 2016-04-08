package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;

import br.com.laminarsoft.jazzforms.persistencia.model.Linha;

@javax.xml.bind.annotation.XmlRootElement(name="valor_dataview")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@SuppressWarnings("all")
public class ValorDataviewVO implements Serializable, IValor, Cloneable{
	@XmlAttribute private Long id;
	@XmlAttribute private String fieldId;
	@XmlAttribute private String fieldType;
	@XmlAttribute private Long componentId;
	@XmlAttribute private Long instanciaId;
	@XmlAttribute private String fieldLabel;
	@XmlAttribute private Integer qtdLinhas;
	
	private List<LinhaVO> linhas = new ArrayList<LinhaVO>();
	
    public boolean equals(Object obj) {
		if (!(obj instanceof ValorDataviewVO)) {
			return false;
		}
		ValorDataviewVO objEntrada = (ValorDataviewVO)obj;
	    return (id == objEntrada.getId()) ;
    }

	public ValorDataviewVO clone()  {
		ValorDataviewVO clon = new ValorDataviewVO();
		clon.setId(this.getId().longValue());
		clon.setFieldId(new String(this.getFieldId()));
		clon.setFieldType(new String(this.getFieldType()));
		clon.setComponentId(this.getComponentId().longValue());
		clon.setFieldLabel(new String(this.getFieldLabel()));
		return clon;
    }

	@Override
	public String toString() {
		String ret = fieldLabel;
		return ret;
    }
	
	public Integer getQtdLinhas() {
		return qtdLinhas;
	}

	public void setQtdLinhas(Integer qtdLinhas) {
		this.qtdLinhas = qtdLinhas;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFieldId() {
		return fieldId;
	}
	public void setFieldId(String fildId) {
		this.fieldId = fildId;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public Long getComponentId() {
		return componentId;
	}
	public void setComponentId(Long componentId) {
		this.componentId = componentId;
	}
	public String getFieldLabel() {
		return fieldLabel;
	}
	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}

	public Long getInstanciaId() {
		return instanciaId;
	}

	public void setInstanciaId(Long instanciaId) {
		this.instanciaId = instanciaId;
	}

	public List<LinhaVO> getLinhas() {
		return linhas;
	}

	public void setLinhas(List<LinhaVO> linhas) {
		this.linhas = linhas;
	}

	@Override
    public String getValorField() {
	    return null;
    }

	@Override
    public void setValorField(String valor) {
    }
	
	
}
