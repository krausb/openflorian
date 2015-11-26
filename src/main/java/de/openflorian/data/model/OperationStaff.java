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
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;

import de.openflorian.data.model.principal.Country;
import de.openflorian.data.model.principal.Department;
import de.openflorian.data.model.principal.Station;
import de.openflorian.media.model.MediaItem;

/**
 * {@link OperationStaff} DTO
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
@Entity
@Table(name="of_coredata_operationstaff")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class OperationStaff implements Serializable{
	private static final long serialVersionUID = 5532041428488563815L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected long id;
	
    @OneToOne
    @JoinColumn(name = "departmentId", nullable = false, insertable=true, updatable=true)
	protected Department department;
    
    @OneToOne
    @JoinColumn(name = "stationId", nullable = false, insertable=true, updatable=true)
	protected Station station;

    @Column
    protected String staffNr;

    @Column
    protected String firstname;
    
    @Column
    protected String middlename;
    
    @Column
    protected String lastname;
    
    @Column
    protected Date birthdate;
    
    @Column
    protected String birthplace;
    
	@Column
	protected String street;
	
	@Column
	protected Integer zip;
	
	@Column
	protected String city;
	
    @OneToOne
    @JoinColumn(name = "countryId", nullable = false, insertable=true, updatable=true)
	protected Country country;
    
    @OneToOne
    @JoinColumn(name = "citizenshipCountryId", nullable = false, insertable=true, updatable=true)
	protected Country citizenshipCountry;
	
	@Column
	protected String email;
	
	@Column
	protected String phonePrivate;

	@Column
	protected String phoneBusiness;
	
	@Column
	protected String mobilePrivate;

	@Column
	protected String mobileBusiness;
	
	@Column
	protected String faxPrivate;

	@Column
	protected String faxBusiness;
	
	@Lob
	protected String note;
	
	@OneToOne
    @JoinColumn(name = "photoId", nullable = true, insertable=true, updatable=true)
	private MediaItem photo;

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		if(!StringUtils.isEmpty(lastname))
			sb.append(lastname);
		
		if(!StringUtils.isEmpty(firstname)) {
			if(sb.length() != 0) sb.append(", ");
			sb.append(firstname);
		}
		
		return sb.toString();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public String getStaffNr() {
		return staffNr;
	}

	public void setStaffNr(String staffNr) {
		this.staffNr = staffNr;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getMiddlename() {
		return middlename;
	}

	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public String getBirthplace() {
		return birthplace;
	}

	public void setBirthplace(String birthplace) {
		this.birthplace = birthplace;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public Integer getZip() {
		return zip;
	}

	public void setZip(Integer zip) {
		this.zip = zip;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Country getCitizenshipCountry() {
		return citizenshipCountry;
	}

	public void setCitizenshipCountry(Country citizenshipCountry) {
		this.citizenshipCountry = citizenshipCountry;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhonePrivate() {
		return phonePrivate;
	}

	public void setPhonePrivate(String phonePrivate) {
		this.phonePrivate = phonePrivate;
	}

	public String getPhoneBusiness() {
		return phoneBusiness;
	}

	public void setPhoneBusiness(String phoneBusiness) {
		this.phoneBusiness = phoneBusiness;
	}

	public String getMobilePrivate() {
		return mobilePrivate;
	}

	public void setMobilePrivate(String mobilePrivate) {
		this.mobilePrivate = mobilePrivate;
	}

	public String getMobileBusiness() {
		return mobileBusiness;
	}

	public void setMobileBusiness(String mobileBusiness) {
		this.mobileBusiness = mobileBusiness;
	}

	public String getFaxPrivate() {
		return faxPrivate;
	}

	public void setFaxPrivate(String faxPrivate) {
		this.faxPrivate = faxPrivate;
	}

	public String getFaxBusiness() {
		return faxBusiness;
	}

	public void setFaxBusiness(String faxBusiness) {
		this.faxBusiness = faxBusiness;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public MediaItem getPhoto() {
		return photo;
	}

	public void setPhoto(MediaItem photo) {
		this.photo = photo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((birthdate == null) ? 0 : birthdate.hashCode());
		result = prime * result
				+ ((birthplace == null) ? 0 : birthplace.hashCode());
		result = prime
				* result
				+ ((citizenshipCountry == null) ? 0 : citizenshipCountry
						.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result
				+ ((department == null) ? 0 : department.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((faxBusiness == null) ? 0 : faxBusiness.hashCode());
		result = prime * result
				+ ((faxPrivate == null) ? 0 : faxPrivate.hashCode());
		result = prime * result
				+ ((firstname == null) ? 0 : firstname.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result
				+ ((lastname == null) ? 0 : lastname.hashCode());
		result = prime * result
				+ ((middlename == null) ? 0 : middlename.hashCode());
		result = prime * result
				+ ((mobileBusiness == null) ? 0 : mobileBusiness.hashCode());
		result = prime * result
				+ ((mobilePrivate == null) ? 0 : mobilePrivate.hashCode());
		result = prime * result + ((note == null) ? 0 : note.hashCode());
		result = prime * result
				+ ((phoneBusiness == null) ? 0 : phoneBusiness.hashCode());
		result = prime * result
				+ ((phonePrivate == null) ? 0 : phonePrivate.hashCode());
		result = prime * result + ((photo == null) ? 0 : photo.hashCode());
		result = prime * result + ((staffNr == null) ? 0 : staffNr.hashCode());
		result = prime * result + ((station == null) ? 0 : station.hashCode());
		result = prime * result + ((street == null) ? 0 : street.hashCode());
		result = prime * result + ((zip == null) ? 0 : zip.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof OperationStaff))
			return false;
		OperationStaff other = (OperationStaff) obj;
		if (birthdate == null) {
			if (other.birthdate != null)
				return false;
		} else if (!birthdate.equals(other.birthdate))
			return false;
		if (birthplace == null) {
			if (other.birthplace != null)
				return false;
		} else if (!birthplace.equals(other.birthplace))
			return false;
		if (citizenshipCountry == null) {
			if (other.citizenshipCountry != null)
				return false;
		} else if (!citizenshipCountry.equals(other.citizenshipCountry))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (department == null) {
			if (other.department != null)
				return false;
		} else if (!department.equals(other.department))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (faxBusiness == null) {
			if (other.faxBusiness != null)
				return false;
		} else if (!faxBusiness.equals(other.faxBusiness))
			return false;
		if (faxPrivate == null) {
			if (other.faxPrivate != null)
				return false;
		} else if (!faxPrivate.equals(other.faxPrivate))
			return false;
		if (firstname == null) {
			if (other.firstname != null)
				return false;
		} else if (!firstname.equals(other.firstname))
			return false;
		if (id != other.id)
			return false;
		if (lastname == null) {
			if (other.lastname != null)
				return false;
		} else if (!lastname.equals(other.lastname))
			return false;
		if (middlename == null) {
			if (other.middlename != null)
				return false;
		} else if (!middlename.equals(other.middlename))
			return false;
		if (mobileBusiness == null) {
			if (other.mobileBusiness != null)
				return false;
		} else if (!mobileBusiness.equals(other.mobileBusiness))
			return false;
		if (mobilePrivate == null) {
			if (other.mobilePrivate != null)
				return false;
		} else if (!mobilePrivate.equals(other.mobilePrivate))
			return false;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		if (phoneBusiness == null) {
			if (other.phoneBusiness != null)
				return false;
		} else if (!phoneBusiness.equals(other.phoneBusiness))
			return false;
		if (phonePrivate == null) {
			if (other.phonePrivate != null)
				return false;
		} else if (!phonePrivate.equals(other.phonePrivate))
			return false;
		if (photo == null) {
			if (other.photo != null)
				return false;
		} else if (!photo.equals(other.photo))
			return false;
		if (staffNr == null) {
			if (other.staffNr != null)
				return false;
		} else if (!staffNr.equals(other.staffNr))
			return false;
		if (station == null) {
			if (other.station != null)
				return false;
		} else if (!station.equals(other.station))
			return false;
		if (street == null) {
			if (other.street != null)
				return false;
		} else if (!street.equals(other.street))
			return false;
		if (zip == null) {
			if (other.zip != null)
				return false;
		} else if (!zip.equals(other.zip))
			return false;
		return true;
	}

}
