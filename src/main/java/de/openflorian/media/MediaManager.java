package de.openflorian.media;

import java.io.File;
import java.io.FileNotFoundException;

import de.openflorian.media.model.MediaItem;

/**
 * Default {@link MediaManager} Implementation<br/>
 * <br/>
 * Configuration:<br/>
 * The WebMediaManager uses {@link MediaManager#CONTEXT_MEDIA_DIR} from container context<br/>
 * as storage location for local media. 
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public interface MediaManager {

	public static final String CONTEXT_MEDIA_DIR = "java:/comp/env/media.dir";
	public static final String CONTEXT_MEDIA_PERSISTENCE_PATH = "java:/comp/env/media.persistence.path";
	
	/**
	 * Persist a local file to {@link MediaManager#CONTEXT_MEDIA_DIR} with Content-Type
	 * auto detection.
	 * 
	 * @param source
	 * @param name
	 * @param contentType
	 * @return
	 * 		{@link MediaItem} successful stored local media item
	 */
	public MediaItem store(byte[] data, String name, String note, boolean isPublic) throws MediaItemManagerException;
	
	/**
	 * Persist a local file to {@link MediaManager#CONTEXT_MEDIA_DIR} with Content-Type
	 * auto detection.
	 * 
	 * @param source
	 * @param name
	 * @param contentType
	 * @return
	 * 		{@link MediaItem} successful stored local media item
	 */
	public MediaItem store(File source, String name, String note, boolean isPublic) throws MediaItemManagerException;

	/**
	 * Persist a file to {@link MediaManager#CONTEXT_MEDIA_DIR}
	 * 
	 * @param path
	 * @param name
	 * @param note
	 * @param contentType
	 * @param isPublic
	 * @return
	 * 		{@link MediaItem} successful stored remote media item
	 */
	public MediaItem store(String path, String name, String note, String contentType, boolean isLocal, boolean isPublic) throws MediaItemManagerException;
	
	/**
	 * Persist a local file to {@link MediaManager#CONTEXT_MEDIA_DIR}
	 * 
	 * @param source
	 * @param name
	 * @param contentType
	 * @return
	 * 		{@link MediaItem} new item ready to persist
	 */
	public void remove(MediaItem item) throws MediaItemManagerException;
	
	/**
	 * Get a {@link MediaItem} by given <code>identifier</code> from persistence layer.
	 * 
	 * @param identifier
	 */
	public MediaItem getByIdentifier(String identifier);

	/**
	 * Get a {@link MediaItem} by given primary key <code>id</code> from persistence layer.
	 * 
	 * @param id
	 */
	public MediaItem getById(long id);
	
	/**
	 * Get {@link File} from <code>item</item>
	 * @param item
	 * @return
	 * @throws MediaItemManagerException 
	 */
	public File getStoredFile(MediaItem item) throws FileNotFoundException, MediaItemManagerException;
	
	/**
	 * Retrieve an absolute path to the given <code>item</code> on the file system.
	 * 
	 * @param item
	 * @return
	 */
	public String getAbsolutePath(MediaItem item) throws FileNotFoundException;
	
}
