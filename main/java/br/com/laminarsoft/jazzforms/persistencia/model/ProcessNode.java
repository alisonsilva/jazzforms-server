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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Table(name="PROCESS_NODE")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="process_node")
@SuppressWarnings({ "all", "unchecked" })
public class ProcessNode implements Serializable, Cloneable {
	public ProcessNode() {
	}
	
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue(generator="VC0A801021467B46C5C90E621")	
	@org.hibernate.annotations.GenericGenerator(name="VC0A801021467B46C5C90E621", strategy="native")	
	@XmlAttribute
	private Long id;
	
	@Column(name="NODE_ID", nullable=true, length=100)
	@XmlAttribute
	private String nodeId;
	
	@Column(name="NODE_NAME", nullable=true, length=255)
	@XmlAttribute
	private String nodeName;
	
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
	
	public void setNodeId(String value) {
		this.nodeId = value;
	}
	
	public String getNodeId() {
		return nodeId;
	}
	
	public void setNodeName(String value) {
		this.nodeName = value;
	}
	
	public String getNodeName() {
		return nodeName;
	}
	
	public String toString() {
		return String.valueOf(getId());
	}

	@Override
	public ProcessNode clone() {
		ProcessNode clnode = new ProcessNode();
		clnode.setId(this.getId().longValue());
		clnode.setNodeId(this.getNodeId().toString());
		clnode.setNodeName(this.getNodeName().toString());
		return clnode;
	}
	
	
	
}
