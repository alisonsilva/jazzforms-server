package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="foto")
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings(value="all")
public class FotoVO implements Serializable, Comparable<FotoVO>, Cloneable, IValor {
	
	@XmlAttribute private Long id;
	@XmlAttribute private Long dhPicture;
	@XmlAttribute private Long componentId;
	@XmlAttribute private String valor;
	@XmlAttribute private String fieldId;
	@XmlAttribute private String fieldType;
	@XmlAttribute private String fieldLabel;
	
	private byte[] foto;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getDhPicture() {
		return dhPicture;
	}
	public void setDhPicture(Long dhPicture) {
		this.dhPicture = dhPicture;
	}
	public byte[] getFoto() {
		return foto;
	}
	public void setFoto(byte[] foto) {
		this.foto = foto;
	}
	
	@Override
	public int compareTo(FotoVO o) {
		return this.id.compareTo(o.id);
	}
	@Override
	protected FotoVO clone(){
		FotoVO nova = new FotoVO();
		nova.dhPicture = this.dhPicture.longValue();
		nova.foto = this.foto;
		return nova;
	}
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof FotoVO)) {
			return false;
		}
		FotoVO comp = (FotoVO)obj;
		return this.id == comp.id;
	}
	@Override
	public int hashCode() {
		return ("foto_" + id).hashCode();
	}
	@Override
	public String toString() {
		return String.valueOf(id);
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
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	@Override
	public String getValorField() {
		String ret = getValor();
		return ret;
	}
	@Override
	public void setValorField(String valor) {
		this.valor = valor;
	}
	
	
}
