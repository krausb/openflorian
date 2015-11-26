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

import de.openflorian.media.model.MediaItem;
import de.openflorian.media.model.MediaItemType;
import de.openflorian.media.util.VimeoUrlParser;
import de.openflorian.media.util.YoutubeUrlParse;

/**
 * {@link MediaItem} Factory<br/>
 * <br/>
 * Creates a new {@link MediaItem} instance
 * <br/>
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class MediaItemFactory {

	public static final String CONTENT_TYPE_YOUTUBE = "video/youtube";
	public static final String CONTENT_TYPE_VIMEO 	= "video/vimeo";
	
	/**
	 * Create a new {@link MediaItem} instance configured by given params
	 * 
	 * @param path
	 * @param name
	 * @param note
	 * @param contentType
	 * @param isLocal
	 * @param isPublic
	 * @return
	 */
	public static MediaItem createInstance(String path, String name, String note, String contentType, boolean isLocal, boolean isPublic) {
		
		MediaItem item = new MediaItem();

		item.setName(name);
		item.setLocal(isLocal);
		item.setPublic(isPublic);
		item.setNote(note);
		
		if(isLocal == true && contentType.toLowerCase().startsWith("video")) {
			item.setType(MediaItemType.LocalVideo);
			item.setPath(path);
			item.setContentType(contentType);
		} else if (isLocal == true && contentType.toLowerCase().startsWith("image")) {
			item.setType(MediaItemType.LocalImage);
			item.setPath(path);
			item.setContentType(contentType);
		} else if (isLocal == false && (path.toLowerCase().contains("youtube") || path.toLowerCase().contains("youtu.be"))) {
			item.setType(MediaItemType.YoutubeVideo);
			item.setPath(YoutubeUrlParse.parse(path));
			item.setContentType(CONTENT_TYPE_YOUTUBE);
		} else if (isLocal == false && path.toLowerCase().contains("vimeo")) {
			item.setPath(VimeoUrlParser.parse(path));
			item.setContentType(CONTENT_TYPE_VIMEO);
			item.setType(MediaItemType.VimeoVideo);
		} else {
			item.setType(MediaItemType.RemoteImage);
		}
		
		return item;
	}
	
}
