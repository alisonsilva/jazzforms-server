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
package br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;

import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;
/**
 * Provides a cross browser class for retrieving location information.
 * Based on the Geolocation API Specification
 * When instantiated, by default this class immediately begins tracking location information, firing a locationupdate event when new location information is available. To disable this location tracking (which may be battery intensive on mobile devices), set autoUpdate to false.
 * When this is done, only calls to updateLocation will trigger a location retrieval.
 * A locationerror event is raised when an error occurs retrieving the location, either due to a user denying the application access to it, or the browser not supporting it.
 * The below code shows a GeoLocation making a single retrieval of location information.
 */
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Table(name="SNCHA_GEOLOCATION")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@javax.xml.bind.annotation.XmlRootElement(name="geolocation")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@XmlDiscriminatorValue("geolocation")
@SuppressWarnings({ "all", "unchecked" })
public class Geolocation implements Serializable {
	public Geolocation() {
	}
	
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue(generator="VC0A8380113FF6C2997F0C1F8")	
	@org.hibernate.annotations.GenericGenerator(name="VC0A8380113FF6C2997F0C1F8", strategy="native")
	@XmlAttribute
	private Long id;
	
	@Column(name="ALLOW_HIGH_ACCURACY", nullable=false, length=1)
	@XmlAttribute
	private boolean allowHighAccuracy = false;
	
	@Column(name="AUTO_UPDATE", nullable=false, length=1)
	@XmlAttribute
	private boolean autoUpdate = true;
	
	@Column(name="FREQUENCY", nullable=true, length=11)
	@XmlAttribute
	private Integer frequency;
	
	@Column(name="MAXIMUM_AGE", nullable=true, length=11)
	@XmlAttribute
	private Integer maximumAge;
	
	@Column(name="TIMEOUT", nullable=true, length=11)
	@XmlAttribute
	private Integer timeout;
	
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
	
	/**
	 * When set to true, provide a hint that the application would like to receive the best possible results. This may result in slower response times or increased power consumption. The user might also deny this capability, or the device might not be able to provide more accurate results than if this option was set tofalse.
	 * Defaults to: false
	 */
	public void setAllowHighAccuracy(boolean value) {
		this.allowHighAccuracy = value;
	}
	
	/**
	 * When set to true, provide a hint that the application would like to receive the best possible results. This may result in slower response times or increased power consumption. The user might also deny this capability, or the device might not be able to provide more accurate results than if this option was set tofalse.
	 * Defaults to: false
	 */
	public boolean getAllowHighAccuracy() {
		return allowHighAccuracy;
	}
	
	/**
	 * When set to true, continually monitor the location of the device (beginning immediately) and fire locationupdate and locationerror events.
	 * Defaults to: true
	 */
	public void setAutoUpdate(boolean value) {
		this.autoUpdate = value;
	}
	
	/**
	 * When set to true, continually monitor the location of the device (beginning immediately) and fire locationupdate and locationerror events.
	 * Defaults to: true
	 */
	public boolean getAutoUpdate() {
		return autoUpdate;
	}
	
	/**
	 * The frequency of each update if autoUpdate is set to true.
	 * Defaults to: 10000
	 */
	public void setFrequency(int value) {
		setFrequency(new Integer(value));
	}
	
	/**
	 * The frequency of each update if autoUpdate is set to true.
	 * Defaults to: 10000
	 */
	public void setFrequency(Integer value) {
		this.frequency = value;
	}
	
	/**
	 * The frequency of each update if autoUpdate is set to true.
	 * Defaults to: 10000
	 */
	public Integer getFrequency() {
		return frequency;
	}
	
	/**
	 * This option indicates that the application is willing to accept cached location information whose age is no greater than the specified time in milliseconds. IfmaximumAge is set to 0, an attempt to retrieve new location information is made immediately.
	 * Setting the maximumAge to Infinity returns a cached position regardless of its age.
	 * If the device does not have cached location information available whose age is no greater than the specified maximumAge, then it must acquire new location information.
	 * For example, if location information no older than 10 minutes is required, set this property to 600000.
	 * Defaults to: 0
	 */
	public void setMaximumAge(int value) {
		setMaximumAge(new Integer(value));
	}
	
	/**
	 * This option indicates that the application is willing to accept cached location information whose age is no greater than the specified time in milliseconds. IfmaximumAge is set to 0, an attempt to retrieve new location information is made immediately.
	 * Setting the maximumAge to Infinity returns a cached position regardless of its age.
	 * If the device does not have cached location information available whose age is no greater than the specified maximumAge, then it must acquire new location information.
	 * For example, if location information no older than 10 minutes is required, set this property to 600000.
	 * Defaults to: 0
	 */
	public void setMaximumAge(Integer value) {
		this.maximumAge = value;
	}
	
	/**
	 * This option indicates that the application is willing to accept cached location information whose age is no greater than the specified time in milliseconds. IfmaximumAge is set to 0, an attempt to retrieve new location information is made immediately.
	 * Setting the maximumAge to Infinity returns a cached position regardless of its age.
	 * If the device does not have cached location information available whose age is no greater than the specified maximumAge, then it must acquire new location information.
	 * For example, if location information no older than 10 minutes is required, set this property to 600000.
	 * Defaults to: 0
	 */
	public Integer getMaximumAge() {
		return maximumAge;
	}
	
	/**
	 * The maximum number of milliseconds allowed to elapse between a location update operation and the corresponding locationupdate event being raised. If a location was not successfully acquired before the given timeout elapses (and no other internal errors have occurred in this interval), then a locationerror event will be raised indicating a timeout as the cause.
	 * Note that the time that is spent obtaining the user permission is not included in the period covered by the timeout. The timeout attribute only applies to the location acquisition operation.
	 * In the case of calling updateLocation, the locationerror event will be raised only once.
	 * If autoUpdate is set to true, the locationerror event could be raised repeatedly. The first timeout is relative to the moment autoUpdate was set to true (or this Ext.util.Geolocation was initialized with the autoUpdate config option set to true). Subsequent timeouts are relative to the moment when the device determines that it's position has changed.
	 */
	public void setTimeout(int value) {
		setTimeout(new Integer(value));
	}
	
	/**
	 * The maximum number of milliseconds allowed to elapse between a location update operation and the corresponding locationupdate event being raised. If a location was not successfully acquired before the given timeout elapses (and no other internal errors have occurred in this interval), then a locationerror event will be raised indicating a timeout as the cause.
	 * Note that the time that is spent obtaining the user permission is not included in the period covered by the timeout. The timeout attribute only applies to the location acquisition operation.
	 * In the case of calling updateLocation, the locationerror event will be raised only once.
	 * If autoUpdate is set to true, the locationerror event could be raised repeatedly. The first timeout is relative to the moment autoUpdate was set to true (or this Ext.util.Geolocation was initialized with the autoUpdate config option set to true). Subsequent timeouts are relative to the moment when the device determines that it's position has changed.
	 */
	public void setTimeout(Integer value) {
		this.timeout = value;
	}
	
	/**
	 * The maximum number of milliseconds allowed to elapse between a location update operation and the corresponding locationupdate event being raised. If a location was not successfully acquired before the given timeout elapses (and no other internal errors have occurred in this interval), then a locationerror event will be raised indicating a timeout as the cause.
	 * Note that the time that is spent obtaining the user permission is not included in the period covered by the timeout. The timeout attribute only applies to the location acquisition operation.
	 * In the case of calling updateLocation, the locationerror event will be raised only once.
	 * If autoUpdate is set to true, the locationerror event could be raised repeatedly. The first timeout is relative to the moment autoUpdate was set to true (or this Ext.util.Geolocation was initialized with the autoUpdate config option set to true). Subsequent timeouts are relative to the moment when the device determines that it's position has changed.
	 */
	public Integer getTimeout() {
		return timeout;
	}
	
	public String toString() {
		return String.valueOf(getId());
	}
	
	public void clone(Geolocation origem, Geolocation destino) {
		destino.setAllowHighAccuracy(origem.getAllowHighAccuracy());
		destino.setAutoUpdate(origem.getAutoUpdate());
		destino.setFrequency(origem.getFrequency());
		destino.setMaximumAge(origem.getMaximumAge());
		destino.setTimeout(origem.getTimeout());
	}
	
	public Geolocation clone() {
		Geolocation destino = new Geolocation();
		this.clone(this, destino);
		return destino;
	}
}
