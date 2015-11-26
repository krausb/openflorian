package de.openflorian.data.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.StringUtils;

import de.openflorian.data.model.principal.Station;

/**
 * {@link OperationResource} DTO
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
@Entity
@Table(name="of_operation_resource")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class OperationResource implements Serializable {
	private static final long serialVersionUID = -878705966123966091L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
    
    @OneToOne
    @JoinColumn(name = "stationId", nullable = false, insertable=true, updatable=true)
	protected Station station;
	
	@Column
	private String callName;
	
	@Column
	private String type;
	
	@Column
	private String crew;
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if(this.station != null && StringUtils.isEmpty(this.station.getName()))
				sb.append(this.station.getName() + " ");
		sb.append(this.callName);
		return this.callName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public String getCallName() {
		return callName;
	}

	public void setCallName(String callName) {
		this.callName = callName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCrew() {
		return crew;
	}

	public void setCrew(String crew) {
		this.crew = crew;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((callName == null) ? 0 : callName.hashCode());
		result = prime * result + ((crew == null) ? 0 : crew.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((station == null) ? 0 : station.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof OperationResource))
			return false;
		OperationResource other = (OperationResource) obj;
		if (callName == null) {
			if (other.callName != null)
				return false;
		} else if (!callName.equals(other.callName))
			return false;
		if (crew == null) {
			if (other.crew != null)
				return false;
		} else if (!crew.equals(other.crew))
			return false;
		if (id != other.id)
			return false;
		if (station == null) {
			if (other.station != null)
				return false;
		} else if (!station.equals(other.station))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
}
