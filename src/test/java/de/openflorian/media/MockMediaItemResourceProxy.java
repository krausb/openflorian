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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.openflorian.media.model.MediaItem;
import de.openflorian.media.ws.MediaItemResource;

/**
 * {@link MediaItem} REST Resource mock
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class MockMediaItemResourceProxy implements MediaItemResource {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	private Map<Long, MediaItem> database = new HashMap<Long, MediaItem>();
	
	@Override
	public MediaItem getById(long id) {
		return database.get(Long.valueOf(id));
	}

	@Override
	public MediaItem getByIdentifier(String identifier) {

		for(Entry<Long, MediaItem> item: database.entrySet()) {
			if(item.getValue().getIdentifier().equals(identifier))
				return item.getValue();
		}
		
		return null;
	}

	@Override
	public MediaItem persist(MediaItem o) {
		
		if(o.getMediaItemId() <= 0) {
			// new record
			database.put(Long.valueOf(database.size() + 1), o);
			
			// rebuild @PrePersist annotation behaviour
			Method method = null;
			try {
				method = o.getClass().getDeclaredMethod("onInsert");
				method.setAccessible(true);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			
			try {
				if(method != null)
					method.invoke(o);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			
		} else if(!database.containsValue(o.getMediaItemId())) {
			database.put(Long.valueOf(o.getMediaItemId()), o);
		} else if(o != database.get(o.getMediaItemId())) {
			database.remove(Long.valueOf(o.getMediaItemId()));
			database.put(Long.valueOf(o.getMediaItemId()), o);
		}
		
		return o;
	}

	@Override
	public void remove(long id) {
		if(database.containsKey(Long.valueOf(id)))
			database.remove(Long.valueOf(id));
	}

}
