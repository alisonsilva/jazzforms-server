package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;

import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Option;

@javax.xml.bind.annotation.XmlRootElement(name="valor_formulario")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@SuppressWarnings("all")
public class ValorFormularioVO implements Serializable, IValor, Cloneable {

	@XmlAttribute private Long id;
	@XmlAttribute private String valor;
	@XmlAttribute private Long componentId;
	@XmlAttribute private String fieldId;
	@XmlAttribute private String fieldType;
	@XmlAttribute private String fieldLabel;
	
	private List<Option> options = new ArrayList<Option>();
	
    public boolean equals(Object obj) {
		if (!(obj instanceof ValorFormularioVO)) {
			return false;
		}
		ValorFormularioVO objEntrada = (ValorFormularioVO)obj;
	    return (id == objEntrada.getId()) ;
    }

	public ValorFormularioVO clone()  {
		ValorFormularioVO clon = new ValorFormularioVO();
		clon.setId(this.getId().longValue());
		clon.setFieldId(new String(this.getFieldId()));
		clon.setFieldType(new String(this.getFieldType()));
		clon.setValor(new String(this.getValor()));
		clon.setComponentId(new Long(this.getComponentId().longValue()));
		return clon;
    }

	@Override
	public String toString() {
		String ret = getValorField();
		return ret;
    }
	
	
	
	@Override
    public String getValorField() {
		String ret = getValor();
		if (fieldType.equalsIgnoreCase("DatePicker") && valor != null && valor.trim().length() > 0 && !valor.trim().contains("/")) {
			try {
	            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	            ret = sdf.format(new Date(Double.valueOf(valor).longValue()));
            } catch (NumberFormatException e) {
	            e.printStackTrace();
			}
		} else if((this.fieldType.equalsIgnoreCase("CheckBox") || this.fieldType.equalsIgnoreCase("Toggle"))) {
			if(valor != null && valor.trim().length() > 0 && valor.equalsIgnoreCase("true")) {
				ret = "X";
			} else {
				ret = "";
			}
		}
		return ret;
    }

	@Override
    public void setValorField(String valor) {
		if(fieldType.equalsIgnoreCase("DatePicker") && valor != null && valor.trim().length() > 0 && valor.trim().contains("/")) {
			try {
	            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	            valor = sdf.parse(valor).getTime() + "";
            } catch (ParseException e) {
	            e.printStackTrace();
			}			
		} else if((this.fieldType.equalsIgnoreCase("CheckBox") || this.fieldType.equalsIgnoreCase("Toggle"))) {
			if(valor != null && valor.trim().length() > 0 && valor.equalsIgnoreCase("X")) {
				valor = "true";
			} else {
				valor = "false";
			}
		}
		this.setValor(valor);
    }
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public Long getComponentId() {
		return componentId;
	}
	public void setComponentId(Long componentId) {
		this.componentId = componentId;
	}
	public String getFieldId() {
		return fieldId;
	}
	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getFieldLabel() {
		return fieldLabel;
	}

	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}

	public List<Option> getOptions() {
		return options;
	}

	public void setOptions(List<Option> options) {
		this.options = options;
	}
	
	
}
