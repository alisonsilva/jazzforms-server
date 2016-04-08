package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("all")
@XmlRootElement(name="equipamento")
@XmlAccessorType(XmlAccessType.FIELD)
public class EquipamentoVO implements Serializable, Comparable<EquipamentoVO> {

	@XmlAttribute private String deviceName;
	@XmlAttribute private String devicePlatform;
	@XmlAttribute private String deviceUUID;
	@XmlAttribute private String loginUsuario;
	@XmlAttribute private String nomeUsuario;
	@XmlAttribute private String latitude;
	@XmlAttribute private String longitude;
	@XmlAttribute private Long id;
	@XmlAttribute private Date dataInclusao;
	@XmlAttribute private Long dhEvento;
	@XmlAttribute private String token;
	
	public List<LocalizacaoVO> localizacoes = new ArrayList<LocalizacaoVO>();
	
	public String toString() {
		return this.getDeviceName();
	}	
	
	@Override
    public int compareTo(EquipamentoVO o) {
		return this.deviceUUID.compareTo(o.getDeviceUUID());
    }
	
	@Override
    public boolean equals(Object obj) {
		if(!(obj instanceof EquipamentoVO)) {
			return false;
		}
		EquipamentoVO ojbE = (EquipamentoVO)obj;
		if(this.getDeviceUUID().equalsIgnoreCase(ojbE.getDeviceUUID())) {
			return true;
		}
		return false;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getDevicePlatform() {
		return devicePlatform;
	}
	public void setDevicePlatform(String devicePlatform) {
		this.devicePlatform = devicePlatform;
	}
	public String getDeviceUUID() {
		return deviceUUID;
	}
	public void setDeviceUUID(String deviceUUID) {
		this.deviceUUID = deviceUUID;
	}
	
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLoginUsuario() {
		return loginUsuario;
	}
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	public List<LocalizacaoVO> getLocalizacoes() {
		return localizacoes;
	}

	public void setLocalizacoes(List<LocalizacaoVO> localizacoes) {
		this.localizacoes = localizacoes;
	}

	public Date getDataInclusao() {
		return dataInclusao;
	}

	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao = dataInclusao;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getDhEvento() {
		return dhEvento;
	}

	public void setDhEvento(Long dhEvento) {
		this.dhEvento = dhEvento;
	}
	
}
