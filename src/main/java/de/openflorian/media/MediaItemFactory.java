package de.openflorian.media;

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
