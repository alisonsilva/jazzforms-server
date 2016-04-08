/**
 * "Visual Paradigm: DO NOT MODIFY THIS FILE!"
 * 
 * This is an automatic generated file. It will be regenerated every time 
 * you generate persistence class.
 * 
 * Modifying its content may cause the program not work, or your work may lost.
 */

/**
 * Licensee: DuKe TeAm
 * License Type: Purchased
 */
package br.com.laminarsoft.jazzforms.persistencia.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Table(name="COMPONENTE")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="DISCRIMINATOR", discriminatorType=DiscriminatorType.STRING)
@DiscriminatorValue("Componente")
@javax.xml.bind.annotation.XmlRootElement(name="componente")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@SuppressWarnings({ "all", "unchecked" })
public abstract class Componente implements Serializable {
	public Componente() {
	}
	
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue(generator="VC0A8380113E3C7E330F06E2C")	
	@org.hibernate.annotations.GenericGenerator(name="VC0A8380113E3C7E330F06E2C", strategy="native")	
	private long id;
	
	@Column(name="NOME", nullable=false, length=50)	
	@javax.xml.bind.annotation.XmlAttribute	
	private String nome;
	
	@Column(name="DESCRICAO", nullable=true, length=255)	
	private String descricao;
	
	@Column(name="TEXTO", nullable=true, length=30)	
	@javax.xml.bind.annotation.XmlAttribute	
	private String texto;
	
	@Column(name="POSICAO_X", nullable=false)	
	@javax.xml.bind.annotation.XmlAttribute	
	private double posicaoX;
	
	@Column(name="POSICAO_Y", nullable=false)	
	@javax.xml.bind.annotation.XmlAttribute	
	private double posicaoY;
	
	@Column(name="WIDTH", nullable=false)	
	@javax.xml.bind.annotation.XmlAttribute	
	private double width;
	
	@Column(name="HEIGHT", nullable=false)	
	@javax.xml.bind.annotation.XmlAttribute	
	private double height;
	
	@Column(name="BACKGROUND_COLOR", nullable=true, length=40)	
	private String backgroundColor;
	
	@Column(name="PACOTE_CD_CUSTOMIZACAO", nullable=true)	
	private byte[] pacoteCodigoCustomizacao;
	
	@Column(name="HABILITADO", nullable=false, length=1)	
	private boolean habilitado;
	
	
	public void setId(long value) {
		this.id = value;
	}
	
	public long getId() {
		return id;
	}
	
	public long getORMID() {
		return getId();
	}
	
	public void setNome(String value) {
		this.nome = value;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setDescricao(String value) {
		this.descricao = value;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setTexto(String value) {
		this.texto = value;
	}
	
	public String getTexto() {
		return texto;
	}
	
	public void setPosicaoX(double value) {
		this.posicaoX = value;
	}
	
	public double getPosicaoX() {
		return posicaoX;
	}
	
	public void setPosicaoY(double value) {
		this.posicaoY = value;
	}
	
	public double getPosicaoY() {
		return posicaoY;
	}
	
	public void setWidth(double value) {
		this.width = value;
	}
	
	public double getWidth() {
		return width;
	}
	
	public void setHeight(double value) {
		this.height = value;
	}
	
	public double getHeight() {
		return height;
	}
	
	public void setBackgroundColor(String value) {
		this.backgroundColor = value;
	}
	
	public String getBackgroundColor() {
		return backgroundColor;
	}
	
	public void setPacoteCodigoCustomizacao(byte[] value) {
		this.pacoteCodigoCustomizacao = value;
		if (value != null && value.length > 0) {
			this.pacoteCodigoCustomizadoTransformado = new String(value);
		}
	}
	
	public byte[] getPacoteCodigoCustomizacao() {
		return pacoteCodigoCustomizacao;
	}
	
	public void setHabilitado(boolean value) {
		this.habilitado = value;
	}
	
	public boolean getHabilitado() {
		return habilitado;
	}
	
	@Transient public String pacoteCodigoCustomizadoTransformado;

	public abstract String getTipoComponente();

	public static final br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Component getInstancia(String tipoInstancia) {
		br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Component ret = null;
//				switch (tipoInstancia) {
//					case "ListBox" : 
//						ret = new ListBox();
//						break;
//					case "TitledPane" :
//						ret = new TitledPane();
//						break;
//					case "TextField" :
//						ret = new TextField();
//						break;
//					case "Grafico" :
//						ret = new Grafico();
//						break;
//					case "Image" :
//						ret = new Image();
//						break;
//					case "Label" :
//						ret = new Label();
//						break;
//					case "CheckBox" :
//						ret = new CheckBox();
//						break;
//					case "RadioButton" :
//						ret = new RadioButton();
//						break;
//					case "Button" : 
//						ret = new Button();
//						break;			
//				}
				return ret;
	}
	
	public String toString() {
		return String.valueOf(getId());
	}
	
	public abstract Componente clone();
	
	public void clone(Componente origem, Componente destino) {
		destino.nome = origem.nome;
		destino.descricao = origem.descricao;
		destino.texto = origem.texto;
		destino.posicaoX = origem.posicaoX;
		destino.posicaoY = origem.posicaoY;
		destino.width = origem.width;
		destino.backgroundColor = origem.backgroundColor;
		destino.pacoteCodigoCustomizacao = origem.pacoteCodigoCustomizacao;
		destino.habilitado = origem.habilitado;

	}
}
