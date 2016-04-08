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
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import br.com.laminarsoft.jazzforms.persistencia.model.adapter.HistoricoAdapter;
import br.com.laminarsoft.jazzforms.persistencia.model.adapter.PaginasAdapter;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Carousel;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Component;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Container;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Tab;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.TabPanel;

@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Table(name="PROJETO")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@javax.xml.bind.annotation.XmlRootElement(name="projeto")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@SuppressWarnings({ "all", "unchecked" })
public class Projeto implements Serializable {
	public Projeto() {
	}
	
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue(generator="VC0A8380113E399A9E960AE4A")	
	@org.hibernate.annotations.GenericGenerator(name="VC0A8380113E399A9E960AE4A", strategy="native")
	@javax.xml.bind.annotation.XmlAttribute	
	private long id;
	
	@OneToOne(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Projeto.class)	
	@org.hibernate.annotations.Cascade({})	
	@JoinColumns({ @JoinColumn(name="PROJETOID") })	
	@Basic(fetch=FetchType.LAZY)
	@XmlTransient
	private br.com.laminarsoft.jazzforms.persistencia.model.Projeto projetoBase;
	
	@Column(name="NOME", nullable=false, length=50)	
	@javax.xml.bind.annotation.XmlAttribute	
	private String nome;
	
	@Column(name="DESCRICAO", nullable=true, length=255)	
	private String descricao;
	
	@Column(name="DH_CRIACAO", nullable=true)
	@XmlAttribute
	private java.util.Date dhCriacao;
	
	@Column(name="PUBLICADO", nullable=false, length=1)
	@XmlAttribute
	private boolean publicado = false;
	
	@Column(name="DEPLOYMENT_ID", nullable=true, length=10)
    @XmlAttribute	
	private Long deploymentId;
	
	@Column(name="APLICACAO", nullable=true, length=1)	
	@XmlAttribute
	private Boolean aplicacao = false;
	
	@OneToMany(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Historico.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE, org.hibernate.annotations.CascadeType.PERSIST})	
	@JoinColumn(name="PROJETO_ID", nullable=true)	
	@org.hibernate.annotations.IndexColumn(name="IDX")	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.FALSE)	
	@XmlJavaTypeAdapter(HistoricoAdapter.class)
	private java.util.List<Historico> historicos = new java.util.ArrayList<Historico>();
	
	@OneToMany(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Pagina.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE, org.hibernate.annotations.CascadeType.PERSIST})	
	@JoinColumn(name="PROJETO_ID", nullable=false)	
	@org.hibernate.annotations.IndexColumn(name="IDX")	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.FALSE)	
	@XmlJavaTypeAdapter(PaginasAdapter.class)
	private java.util.List<Pagina> paginas = new java.util.ArrayList<Pagina>();

	@OneToOne(mappedBy="projeto", targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.Deployment.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE, org.hibernate.annotations.CascadeType.PERSIST})	
	@Basic(fetch=FetchType.LAZY)	
	private br.com.laminarsoft.jazzforms.persistencia.model.Deployment deployment;
	
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
	
	public void setDhCriacao(java.util.Date value) {
		this.dhCriacao = value;
	}
	
	public java.util.Date getDhCriacao() {
		return dhCriacao;
	}
	
	public void setPublicado(boolean value) {
		this.publicado = value;
	}
	
	public boolean getPublicado() {
		return publicado;
	}

	public void setDeploymentId(long value) {
		setDeploymentId(new Long(value));
	}
	
	public void setDeploymentId(Long value) {
		this.deploymentId = value;
	}
	
	public Long getDeploymentId() {
		return deploymentId;
	}

	public void setHistoricos(java.util.List<Historico> value) {
		this.historicos = value;
	}

	public void setAplicacao(boolean value) {
		setAplicacao(new Boolean(value));
	}
	
	public void setAplicacao(Boolean value) {
		this.aplicacao = value;
	}
	
	public Boolean getAplicacao() {
		return aplicacao;
	}
		
	
	public java.util.List<Historico> getHistoricos() {
		return historicos;
	}
	
	
	public void setPaginas(java.util.List<Pagina> value) {
		this.paginas = value;
	}
	
	public java.util.List<Pagina> getPaginas() {
		return paginas;
	}
	
	
	public void setDeployment(br.com.laminarsoft.jazzforms.persistencia.model.Deployment value) {
		this.deployment = value;
	}
	
	public br.com.laminarsoft.jazzforms.persistencia.model.Deployment getDeployment() {
		return deployment;
	}
	
	
	public void setProjetoBase(br.com.laminarsoft.jazzforms.persistencia.model.Projeto value) {
		this.projetoBase = value;
	}
	
	public br.com.laminarsoft.jazzforms.persistencia.model.Projeto getProjetoBase() {
		return projetoBase;
	}
	
	@XmlAttribute
	@Transient
	public Boolean versionControl = new Boolean(true);
	
	public Boolean getVersionControl() {
		return versionControl;
	}
	
	public void setVersionControl(Boolean vcontrol) {
		this.versionControl = vcontrol;
	}
	
	@XmlAttribute
	@Transient
	public String versionControllDesc;
	
	
	
	public String getVersionControllDesc() {
		return versionControllDesc;
	}

	public void setVersionControllDesc(String versionControllDesc) {
		this.versionControllDesc = versionControllDesc;
	}

	public Instancia criaNovaInstanciaVazia() {
		Instancia instancia = new Instancia();
		instancia.setDhCriacaoP(new Date());
		for(Pagina pag : this.getPaginas()) {
			criaNovaInstanciaFormulario(instancia, pag.getContainer());
		}
		instancia.setProjeto(this);
		return instancia;
	}
	
	@Override
	public String toString() {
		return String.valueOf(getId());
	}
	
	@Override
	public Projeto clone() {
		Projeto p = new Projeto();
		p.descricao = this.descricao;
		p.nome = this.nome;
		p.id = this.id;
		p.setAplicacao(this.getAplicacao().booleanValue());
		p.setDeploymentId(this.getDeploymentId() == null ? null : this.getDeploymentId().longValue());
		p.setPublicado(this.getPublicado());
		p.dhCriacao = new Date(System.currentTimeMillis());
		p.publicado = this.getAplicacao();
		p.setAplicacao(this.getAplicacao().booleanValue());
		if (this.getDeployment() != null) {
			p.setDeployment(this.getDeployment().clone());
			p.getDeployment().setProjeto(p);
		}
		for(Pagina pag : this.getPaginas()) {
			p.getPaginas().add(pag.clone());
			pag.setProjeto(p);
		}
		
		return p;
	}
	
	
	/**
	 *     	     
	 * @param instancia
	 * @param componente
	 */
	private void criaNovaInstanciaFormulario(Instancia instancia, Component componente) {
		if (componente.getXType().equalsIgnoreCase("datepickerfield") ||
				componente.getXType().equalsIgnoreCase("textfield") ||
				componente.getXType().equalsIgnoreCase("togglefield") ||
				componente.getXType().equalsIgnoreCase("selectfield") ||
				componente.getXType().equalsIgnoreCase("checkboxfield") ||
				componente.getXType().equalsIgnoreCase("radiofield") ||
				componente.getXType().equalsIgnoreCase("emailfield") ||
				componente.getXType().equalsIgnoreCase("spinnerfield") ||
				componente.getXType().equalsIgnoreCase("textareafield") ||
				componente.getXType().equalsIgnoreCase("numberfield") ||
				componente.getXType().equalsIgnoreCase("hiddenfield") ||
				componente.getXType().equalsIgnoreCase("passwordfield") ||
				componente.getXType().equalsIgnoreCase("numberfield")) {
			ValorFormulario valor = new ValorFormulario();
			valor.setComponente(componente);
			instancia.getValoresFormulario().add(valor);
			valor.setFieldId(componente.getFieldId());
			valor.setDhPreenchimento(System.currentTimeMillis());			
		} else if(componente.getXType().equalsIgnoreCase("dataview")) {
			ValorDataview vlDtv = new ValorDataview();
			vlDtv.setDataView(componente);
			vlDtv.setFieldId(componente.getFieldId());
			instancia.getValoresDataview().add(vlDtv);
		} else {
			if (componente.getXType().equalsIgnoreCase("carousel")) {
				for(Tab tab : ((Carousel)componente).getPaginas()) {
					for(Component cmp : tab.getItems()) {
						criaNovaInstanciaFormulario(instancia, cmp);
					}
				}
			} else if(componente.getXType().equalsIgnoreCase("tabpanel")) {
				for(Tab tab : ((TabPanel)componente).getTabs()) {
					for(Component cmp : tab.getItems()) {
						criaNovaInstanciaFormulario(instancia, cmp);
					}
				}
			} else if(componente instanceof Container) {
				for(Component cmp : ((Container)componente).getItems()) {
					criaNovaInstanciaFormulario(instancia, cmp);
				}
			}
		}
	}
}
