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
import java.util.Collections;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Table(name="BP_INSTANCE")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@javax.xml.bind.annotation.XmlRootElement(name="instance")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@SuppressWarnings({ "all", "unchecked" })
public class BPInstance implements Serializable, Cloneable {
	public BPInstance() {
	}
	
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue(generator="VC0A8380113F5C7DFA29051BB")	
	@org.hibernate.annotations.GenericGenerator(name="VC0A8380113F5C7DFA29051BB", strategy="native")	
	@XmlAttribute
	private long id;
	
	@OneToOne(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.ProcessModel.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.LOCK})	
	@JoinColumns({ @JoinColumn(name="PROCESS_MODEL_ID") })	
	private br.com.laminarsoft.jazzforms.persistencia.model.ProcessModel processModel;
	
	@Column(name="PROCESS_INSTANCE_ID", nullable=false, length=100)	
	private String processInstanceId;
	
	@Column(name="SESSION_ID", nullable=true, length=100)	
	private String sessionId;
	
	@OneToMany(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.ValorBPInstance.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})	
	@JoinColumn(name="BP_INSTANCE_ID", nullable=true)	
	@org.hibernate.annotations.IndexColumn(name="IDX")	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.FALSE)	
	private java.util.List<ValorBPInstance> valores = new java.util.ArrayList<ValorBPInstance>();
	
	public void setId(long value) {
		this.id = value;
	}
	
	public long getId() {
		return id;
	}
	
	public long getORMID() {
		return getId();
	}
	
	public void setProcessInstanceId(String value) {
		this.processInstanceId = value;
	}
	
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	
	public void setSessionId(String value) {
		this.sessionId = value;
	}
	
	public String getSessionId() {
		return sessionId;
	}
	
	public void setProcessModel(br.com.laminarsoft.jazzforms.persistencia.model.ProcessModel value) {
		this.processModel = value;
	}
	
	public br.com.laminarsoft.jazzforms.persistencia.model.ProcessModel getProcessModel() {
		return processModel;
	}

	public void setValores(java.util.List<ValorBPInstance> value) {
		this.valores = value;
	}
	
	public java.util.List<ValorBPInstance> getValores() {
		return valores;
	}
	
	
	public String toString() {
		return String.valueOf(getId());
	}

	@Override
	public BPInstance clone() {
		BPInstance ins = new BPInstance();
		ins.setId(this.getId());
		ins.setProcessInstanceId(this.getProcessInstanceId().toString());
		ins.setProcessModel(this.getProcessModel() == null ? null : this.getProcessModel().clone());
		ins.setSessionId(this.getSessionId().toString());
		this.getValores().removeAll(Collections.singleton(null));
		for(ValorBPInstance vlr : this.getValores()) {
			ins.getValores().add(vlr.clone());
		}
		return ins;
	}
	
	
	
}
