package de.openflorian.media;

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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.openflorian.media.model.MediaItem;

/**
 * Default Media Manager Facade<br/>
 * 
 * @see {@link MediaManager}
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class DefaultMediaManager implements MediaManager {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	protected MediaItemFileSystemManager fileManager;
	protected MediaItemPersistenceManager persistenceManager;
	
	public DefaultMediaManager() {
		log.trace("Initializing DefaultMediaManager...");
	}
	
	@Override
	public MediaItem store(File source, String filename, String note, boolean isPublic) throws MediaItemManagerException {
		MediaItem item = null;
		String relativePath = null;
		
		try {
			String contentType = URLConnection.guessContentTypeFromName(source.getAbsolutePath());
			
			// store given file to managed context
			relativePath = fileManager.store(source);
			item = store(relativePath, filename, note, contentType, true, isPublic);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			
			// --rollback--
			try {
				if(relativePath != null && fileManager.get(relativePath).exists())
					fileManager.get(relativePath).delete();
			} catch (FileNotFoundException fnfe) {}
			
			if(item != null)
				persistenceManager.remove(item);
			
			throw new MediaItemManagerException("Error while storing local file media item occured!", e);
		}
	
		return item;
	}
	
	@Override
	public MediaItem store(byte[] data, String filename, String note, boolean isPublic) throws MediaItemManagerException {
		MediaItem item = null;
		String relativePath = null;
		
		try {
			String contentType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(data));
			
			// store given file to managed context
			relativePath = fileManager.store(data);
			item = store(relativePath, filename, note, contentType, true, isPublic);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			
			// --rollback--
			try {
				if(relativePath != null && fileManager.get(relativePath).exists())
					fileManager.get(relativePath).delete();
			} catch (FileNotFoundException fnfe) {}
			
			if(item != null)
				persistenceManager.remove(item);
			
			throw new MediaItemManagerException("Error while storing local file media item occured!", e);
		}
	
		return item;
	}

	@Override
	public MediaItem store(String path, String name, String note, String contentType, boolean isLocal, boolean isPublic) throws MediaItemManagerException {
		MediaItem item = null;
		
		try {
			item = MediaItemFactory.createInstance(path, name, note, contentType, isLocal, isPublic);
			item = persistenceManager.store(item);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			
			if(item != null) {
				persistenceManager.remove(item);
				item = null;
			}
			
			throw new MediaItemManagerException("Error while storing file media item occured!", e);
		}
	
		return item;
	}

	@Override
	public void remove(MediaItem item) throws MediaItemManagerException {
		try {
			persistenceManager.remove(item);
			if(item.isLocal())
				fileManager.remove(item.getPath());
		} catch (Exception e) {
			throw new MediaItemManagerException("Error while storing file media item occured!", e);
		}
	}

	@Override
	public MediaItem getByIdentifier(String identifier) {
		return persistenceManager.get(identifier);
	}

	@Override
	public MediaItem getById(long id) {
		return persistenceManager.get(id);
	}

	@Override
	public String getAbsolutePath(MediaItem item) throws FileNotFoundException {
		if(item.isLocal())
			return fileManager.getAbsolutePath(item.getPath());
		else
			return item.getPath();
	}

	@Override
	public File getStoredFile(MediaItem item) throws FileNotFoundException, MediaItemManagerException {
		if(!item.isLocal())
			throw new MediaItemManagerException("Given media item is not a local item.");
		
		return fileManager.get(item.getPath());
	}

	/**
	 * Dependency injection: {@link MediaItemFileSystemManager}
	 * @param fileManager
	 */
	public void setMediaItemFileSystemManager(MediaItemFileSystemManager fileManager) {
		this.fileManager = fileManager;		
	}
	
	/**
	 * Dependency injection: {@link MediaItemPersistenceManager}
	 * @param persistenceManager
	 */
	public void setMediaItemPersistenceManager(MediaItemPersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
	}
	
}
