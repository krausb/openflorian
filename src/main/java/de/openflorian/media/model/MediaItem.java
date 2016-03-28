package de.openflorian.media.model;

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
import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * {@link MediaItem} DTO<br/>
 * <br/>
 * Represents a media item with a specific {@link MediaItemType}.<br/>
 * The DTO automaticaly generates a public identifier to stream it<br/>
 * out to a web client.
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
@Entity
@Access(AccessType.FIELD)
@Table(name="of_media_item")
@XmlRootElement
public class MediaItem implements Serializable {
	private static final long serialVersionUID = 8322557464837911640L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long mediaItemId;
	
	@Column(name="itemName")
	private String name;
	
	@Column
	protected String identifier;
	
	@Lob 
	@Column
	private String note;
	
	@Column
	private String path;
	
	@Column
	private String contentType;
	
	@Column
	private String extension;
	
	@Column
	private MediaItemType type;
	
	@Column
	private boolean isLocal = true;
	
	@Column
	private boolean isPublic = false;

	public long getMediaItemId() {
		return mediaItemId;
	}

	public void setMediaItemId(long mediaItemId) {
		this.mediaItemId = mediaItemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getContentType() {
		return this.contentType;
	}
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public String getExtension() {
		return this.extension;
	}
	
	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	public MediaItemType getType() {
		return type;
	}

	public void setType(MediaItemType type) {
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public boolean isLocal() {
		return isLocal;
	}

	public void setLocal(boolean isLocal) {
		this.isLocal = isLocal;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
	
	public String getIdentifier() {
		return this.identifier;
	}
	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	/**
	 * PrePersist listener for {@link MediaItem}
	 */
    @PrePersist
    protected void onInsert() {
    	if(this.identifier == null || this.identifier.length() == 0)
    		this.identifier = UUID.randomUUID().toString();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((contentType == null) ? 0 : contentType.hashCode());
		result = prime * result
				+ ((extension == null) ? 0 : extension.hashCode());
		result = prime * result
				+ ((identifier == null) ? 0 : identifier.hashCode());
		result = prime * result + (isLocal ? 1231 : 1237);
		result = prime * result + (isPublic ? 1231 : 1237);
		result = prime * result + (int) (mediaItemId ^ (mediaItemId >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((note == null) ? 0 : note.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MediaItem other = (MediaItem) obj;
		if (contentType == null) {
			if (other.contentType != null)
				return false;
		} else if (!contentType.equals(other.contentType))
			return false;
		if (extension == null) {
			if (other.extension != null)
				return false;
		} else if (!extension.equals(other.extension))
			return false;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		if (isLocal != other.isLocal)
			return false;
		if (isPublic != other.isPublic)
			return false;
		if (mediaItemId != other.mediaItemId)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
}
