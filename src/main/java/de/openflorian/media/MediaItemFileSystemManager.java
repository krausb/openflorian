package de.openflorian.media;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.openflorian.media.model.MediaItem;

/**
 * {@link MediaItem} File System Storage Manager<br/>
 * <br/>
 * Handles storage of {@link MediaItem} to the local file system
 * 
 * @author Bastian Kraus <bk@pogo-systems.de>
 */
public class MediaItemFileSystemManager {

	public Logger log = LoggerFactory.getLogger(getClass());
	
	private static String FILE_STORE_PATH = "";
	public static String getFileStorePath() {
		return FILE_STORE_PATH;
	}
	
    static {
    	Logger log = LoggerFactory.getLogger(MediaItemFileSystemManager.class);
    	InitialContext ctx = null;
    	try {
    		ctx = new InitialContext();
    	} catch (NamingException e) {
    		log.error(e.getMessage(), e);
    	}
    	
    	synchronized (ctx) {
    		try {
    			FILE_STORE_PATH = (String) ctx.lookup( MediaManager.CONTEXT_MEDIA_DIR );
    		} catch (NamingException e) {
    			// Config for Tests
    			if(!StringUtils.isEmpty(System.getenv("media.dir")))
    				FILE_STORE_PATH = System.getenv("media.dir");
    			else
    				FILE_STORE_PATH = "mediastorage/";
    		}
		}
    	
    	if(StringUtils.isEmpty(FILE_STORE_PATH)) {
    		log.error("MediaManager.FILE_STORE_PATH not found in initial context!");
    		throw new RuntimeException("MediaManager.FILE_STORE_PATH not found in initial context!");
    	} else {
    		log.info("Configured file store path: " + FILE_STORE_PATH);
    	}
    }

    /**
     * Stores a given temporary {@link File} and returns a relativ path
     * where the file is stored.
     * 
     * @param source
     * @param contentType
     * @return
     * @throws IOException 
     */
	public String store(File source) throws IOException {
		// generate path
		String hashPath = UUID.randomUUID().toString().replace("-", "/");
		
		// copy file
		File newFile = new File(FILE_STORE_PATH, hashPath);
		FileUtils.copyFile(source, newFile);
		log.trace("created new file: " + newFile);
		
		return hashPath;
	}

	/**
     * Stores given {@link byte}[] data to a {@link File} and returns a relativ path
     * where the file is stored.
     * 
     * @param source
     * @param contentType
     * @return
     * @throws IOException 
     */
	public String store(byte[] data) throws IOException {
		// generate path
		String hashPath = UUID.randomUUID().toString().replace("-", "/");
		
		// copy file
		File newFile = new File(FILE_STORE_PATH, hashPath);
		FileUtils.writeByteArrayToFile(newFile, data);
		
		return hashPath;
	}
	
	/**
	 * Remove a media items stored file from file system by given <code>relativePath</code>
	 * 
	 * @param relativePath
	 * @throws FileNotFoundException
	 */
	public void remove(String relativePath) throws FileNotFoundException {
		File f = get(relativePath);
		
		if(f.exists())
			f.delete();
		else
			throw new FileNotFoundException(f.getAbsolutePath());
	}
	
	/**
	 * Returns the absolute path of the given <code>item</code>.<br/>
	 * The relative path of the given item will also be validated.
	 * 
	 * @param relativePath relative storage path
	 * @return
	 * @throws FileNotFoundException
	 */
	public String getAbsolutePath(String relativePath) throws FileNotFoundException {
		return get(relativePath).getAbsolutePath();
	}

	/**
	 * Returns the real {@link File} for given <code>item</code>
	 *  
	 * @param relativePath relative storage path
	 * @return
	 * @throws FileNotFoundException
	 */
	public File get(String relativePath) throws FileNotFoundException {
		File storedMediaItemFile = new File(FILE_STORE_PATH, relativePath);
		if(!storedMediaItemFile.exists())
			throw new FileNotFoundException("Local file for media item does not exist (" + relativePath + ")");
		
		return storedMediaItemFile;
	}
	
}
