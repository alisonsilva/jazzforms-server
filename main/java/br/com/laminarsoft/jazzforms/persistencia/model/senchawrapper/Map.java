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
 * 
 * Wraps a Google Map in an Ext.Component using the Google Maps API.
 * To use this component you must include an additional JavaScript file from Google:
 * <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=true"></script>
 */
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("Map")
@javax.xml.bind.annotation.XmlRootElement(name="map")
@javax.xml.bind.annotation.XmlAccessorType(value=javax.xml.bind.annotation.XmlAccessType.FIELD)
@XmlDiscriminatorValue("map")
@SuppressWarnings({ "all", "unchecked" })
public class Map extends br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Container implements Serializable {
	public Map() {
	}
	
	@OneToOne(targetEntity=br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Geolocation.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.ALL})	
	@JoinColumns({ @JoinColumn(name="GEOLOCATION_ID") })	
	private br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Geolocation geo;
	
	@Column(name="MAP_OPTIONS", nullable=true, length=255)	
	private String mapOptions = "";
	
	@Column(name="USE_CURRENT_LOCATION", nullable=true, length=1)
	@XmlAttribute
	private boolean useCurrentLocation = true;
	
	/**
	 * MapOptions as specified by the Google Documentation: http://code.google.com/apis/maps/documentation/v3/reference.html
	 * Defaults to: {}
	 */
	public void setMapOptions(String value) {
		this.mapOptions = value;
	}
	
	/**
	 * MapOptions as specified by the Google Documentation: http://code.google.com/apis/maps/documentation/v3/reference.html
	 * Defaults to: {}
	 */
	public String getMapOptions() {
		return mapOptions;
	}
	
	/**
	 * Pass in true to center the map based on the geolocation coordinates or pass a GeoLocation config to have more control over your GeoLocation options
	 * Defaults to: false
	 */
	public void setUseCurrentLocation(boolean value) {
		this.useCurrentLocation = value;
	}
	
	/**
	 * Pass in true to center the map based on the geolocation coordinates or pass a GeoLocation config to have more control over your GeoLocation options
	 * Defaults to: false
	 */
	public boolean getUseCurrentLocation() {
		return useCurrentLocation;
	}
	
	public void setGeo(br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Geolocation value) {
		this.geo = value;
	}
	
	public br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Geolocation getGeo() {
		return geo;
	}
	
	public String getXType() {
		return "map";
	}
	
	public String toString() {
		return super.toString();
	}
	
	public void clone(Map origem, Map destino) {
		destino.setMapOptions(new String(origem.getMapOptions()));
		destino.setUseCurrentLocation(origem.getUseCurrentLocation());
		if (origem.getGeo() != null) {
			destino.setGeo(origem.getGeo().clone());
		}
		super.clone(origem, destino);
	}
	
	public Map clone() {
		Map destino = new Map();
		this.clone(this, destino);
		return destino;
	}
}
