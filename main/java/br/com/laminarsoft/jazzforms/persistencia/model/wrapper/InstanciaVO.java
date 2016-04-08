package br.com.laminarsoft.jazzforms.persistencia.model.wrapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;

@javax.xml.bind.annotation.XmlRootElement(name="instancia")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@SuppressWarnings("all")
public class InstanciaVO implements Serializable, Comparable<InstanciaVO>, Cloneable {
	
	@XmlAttribute private Long id;
	@XmlAttribute private Date dhCriacao;
	@XmlAttribute private Date dhAlteracao;
	@XmlAttribute private Long dhCriacaoLng;
	@XmlAttribute private Long dhAlteracaoLng;
	@XmlAttribute private Long projetoId;
	@XmlAttribute private String userLogin;
	@XmlAttribute private String userName;
	@XmlAttribute private Long userId;
	@XmlAttribute private Boolean reenviado;
	@XmlAttribute private String loginUsuarioReenvio;
	@XmlAttribute private String nomeUsuarioReenvio;
	@XmlAttribute private Long idUsuarioReenvio;
	
	private List<ValorFormularioVO> valoresFormulario = new ArrayList<ValorFormularioVO>();
	private List<ValorDataviewVO> valoresDataview = new ArrayList<ValorDataviewVO>();
	private List<FotoVO> valoresFoto = new ArrayList<FotoVO>();
	
	@Override
    public int compareTo(InstanciaVO o) {
		if (o == null) {
			return -1;
		}
	    return this.dhCriacao.compareTo(o.dhCriacao);
    }		
	
	@Override
    public InstanciaVO clone() {
		InstanciaVO novo = new InstanciaVO();
		novo.setId(null);
		novo.setDhCriacao(new Date());
		novo.setDhAlteracao(null);
		novo.setProjetoId(new Long(this.getProjetoId().longValue()));
		novo.setUserLogin(this.getUserLogin());
		novo.setUserName(this.getUserName());
		novo.setProjetoId(new Long(this.projetoId.longValue()));
		novo.setReenviado(false);
		novo.setLoginUsuarioReenvio(null);
		novo.setNomeUsuarioReenvio(null);
		for(ValorFormularioVO vo : this.getValoresFormulario()) {
			novo.getValoresFormulario().add(vo.clone());
		}
		for(ValorDataviewVO vo : this.getValoresDataview()) {
			novo.getValoresDataview().add(vo.clone());
		}
		for(FotoVO vo : this.getValoresFoto()) {
			novo.getValoresFoto().add(vo.clone());
		}
		return novo;
    }

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getDhCriacao() {
		return dhCriacao;
	}
	public void setDhCriacao(Date dhCriacao) {
		this.dhCriacao = dhCriacao;
		this.dhCriacaoLng = (dhCriacao == null ? null : dhCriacao.getTime());
	}
	public Date getDhAlteracao() {
		return dhAlteracao;
	}
	public void setDhAlteracao(Date dhAlteracao) {
		this.dhAlteracao = dhAlteracao;
		this.dhAlteracaoLng = (dhAlteracao == null ? null : dhAlteracao.getTime());
	}
	public Long getDhCriacaoLng() {
		return dhCriacaoLng;
	}
	public void setDhCriacaoLng(Long dhCriacaoLng) {
		this.dhCriacaoLng = dhCriacaoLng;
	}
	public Long getDhAlteracaoLng() {
		return dhAlteracaoLng;
	}
	public void setDhAlteracaoLng(Long dhAlteracaoLng) {
		this.dhAlteracaoLng = dhAlteracaoLng;
	}
	public Long getProjetoId() {
		return projetoId;
	}
	public void setProjetoId(Long projetoId) {
		this.projetoId = projetoId;
	}
	public String getUserLogin() {
		return userLogin;
	}
	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<ValorFormularioVO> getValoresFormulario() {
		return valoresFormulario;
	}
	public void setValoresFormulario(List<ValorFormularioVO> valoresFormulario) {
		this.valoresFormulario = valoresFormulario;
	}
	public List<ValorDataviewVO> getValoresDataview() {
		return valoresDataview;
	}
	public void setValoresDataview(List<ValorDataviewVO> valoresDataview) {
		this.valoresDataview = valoresDataview;
	}
	
	public List<FotoVO> getValoresFoto() {
		return valoresFoto;
	}

	public void setValoresFoto(List<FotoVO> valoresFoto) {
		this.valoresFoto = valoresFoto;
	}

	public Boolean getReenviado() {
		return reenviado;
	}

	public void setReenviado(Boolean reenviado) {
		this.reenviado = reenviado;
	}
	
	public void setReenviado(boolean reenviado) {
		this.reenviado = reenviado;
	}

	public String getLoginUsuarioReenvio() {
		return loginUsuarioReenvio;
	}

	public void setLoginUsuarioReenvio(String loginUsuarioReenvio) {
		this.loginUsuarioReenvio = loginUsuarioReenvio;
	}

	public String getNomeUsuarioReenvio() {
		return nomeUsuarioReenvio;
	}

	public void setNomeUsuarioReenvio(String nomeUsuarioReenvio) {
		this.nomeUsuarioReenvio = nomeUsuarioReenvio;
	}

	public Long getIdUsuarioReenvio() {
		return idUsuarioReenvio;
	}

	public void setIdUsuarioReenvio(Long idUsuarioReenvio) {
		this.idUsuarioReenvio = idUsuarioReenvio;
	}	
}
