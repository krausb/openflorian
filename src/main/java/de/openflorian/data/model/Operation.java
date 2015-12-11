package de.openflorian.data.model;

/*
 * This file is part of Openflorian.
 * 
 * Copyright (C) 2015  Bastian Kraus
 * 
 * Openflorian is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version)
 *     
 * Openflorian is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *     
 * You should have received a copy of the GNU General Public License
 * along with Openflorian.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import de.openflorian.data.model.principal.Station;

/**
 * {@link Operation} DTO
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
@Entity
@Table(name="of_operation")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Operation implements Serializable {
	private static final long serialVersionUID = 718788446662271203L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected long id;
    
    @OneToOne
    @JoinColumn(name = "stationId", nullable = true, insertable=true, updatable=true)
	protected Station station;
	
	@Column
	protected String operationNr;
	
	@Column
	protected double positionLongitude;
	
	@Column
	protected double positionLatitude;
	
	@Column
	protected String object;
	
	@Column
	protected String street;
	
	@Column
	protected String crossway;
	
	@Column
	protected String city;
	
	@Column
	protected String priority;
	
	@Column
	protected String keyword;
	
	@Column
	protected String buzzword;
	
	@Column
	protected String resourcesRaw;
	
	@Column
	protected Date incurredAt;
	
	@Column
	protected Date takenOverAt;
	
	@Column
	protected Date dispatchedAt;
	
	@ManyToMany
	@JoinTable(
			joinColumns={@JoinColumn(name="operation_id",referencedColumnName="id")},
			inverseJoinColumns={@JoinColumn(name="operation_resource_id",referencedColumnName="id")}
			)
	protected List<OperationResource> resources;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOperationNr() {
		return operationNr;
	}

	public void setOperationNr(String operationNr) {
		this.operationNr = operationNr;
	}

	public double getPositionLongitude() {
		return this.positionLongitude;
	}
	
	public void setPositionLongitude(double positionLongitude) {
		this.positionLongitude = positionLongitude;
	}
	
	public double getPositionLatitude() {
		return this.positionLatitude;
	}
	
	public void setPositionLatitude(double positionLatitude) {
		this.positionLatitude = positionLatitude;
	}
	
	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCrossway() {
		return crossway;
	}

	public void setCrossway(String crossway) {
		this.crossway = crossway;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getBuzzword() {
		return buzzword;
	}

	public void setBuzzword(String buzzword) {
		this.buzzword = buzzword;
	}

	public String getResourcesRaw() {
		return resourcesRaw;
	}

	public void setResourcesRaw(String resources) {
		this.resourcesRaw = resources;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public List<OperationResource> getResources() {
		return resources;
	}

	public void setResources(List<OperationResource> resources) {
		this.resources = resources;
	}
	
}
