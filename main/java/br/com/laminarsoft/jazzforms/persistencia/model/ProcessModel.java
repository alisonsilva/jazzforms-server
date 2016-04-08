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

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Table(name="PROCESS_MODEL")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="process_model")
@SuppressWarnings({ "all", "unchecked" })
public class ProcessModel implements Serializable, Cloneable {
	public ProcessModel() {
	}
	
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue(generator="VC0A801021467B46C5A80E620")	
	@org.hibernate.annotations.GenericGenerator(name="VC0A801021467B46C5A80E620", strategy="native")
	@XmlAttribute
	private Long id;
	
	@Column(name="PROCESS_ID", nullable=true, length=300)	
	private String processId;
	
	@Column(name="PROCESS_NAME", nullable=true, length=255)	
	private String processName;
	
	@Column(name="PROCESS_IMAGE", nullable=true)	
	private byte[] processImage;
	
	@OneToMany(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.ProcessNode.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})	
	@JoinColumn(name="PROCESS_MODEL_ID", nullable=true)	
	@org.hibernate.annotations.IndexColumn(name="IDX")	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.FALSE)	
	private java.util.List<ProcessNode> nodes = new java.util.ArrayList<ProcessNode>();
	
	public void setId(long value) {
		setId(new Long(value));
	}
	
	public void setId(Long value) {
		this.id = value;
	}
	
	public Long getId() {
		return id;
	}
	
	public Long getORMID() {
		return getId();
	}
	
	public void setProcessId(String value) {
		this.processId = value;
	}
	
	public String getProcessId() {
		return processId;
	}
	
	public void setProcessName(String value) {
		this.processName = value;
	}
	
	public String getProcessName() {
		return processName;
	}
	
	public void setProcessImage(byte[] value) {
		this.processImage = value;
	}
	
	public byte[] getProcessImage() {
		return processImage;
	}
	
	public void setNodes(java.util.List<ProcessNode> value) {
		this.nodes = value;
	}
	
	public java.util.List<ProcessNode> getNodes() {
		return nodes;
	}
	
	
	public String toString() {
		return String.valueOf(getId());
	}

	@Override
	public ProcessModel clone() {
		ProcessModel clmodel = new ProcessModel();
		clmodel.setId(this.getId().longValue());
		clmodel.setProcessId(this.getProcessId().toString());
		clmodel.setProcessImage(this.getProcessImage());
		clmodel.setProcessName(this.getProcessName().toString());
		for(ProcessNode node : this.getNodes()) {
			clmodel.getNodes().add(node.clone());
		}
		return clmodel;
	}

	
	
}
