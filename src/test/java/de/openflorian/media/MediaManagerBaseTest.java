package de.openflorian.media;

import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base Test Functionality<br/>
 * <br/>
 * Creates a test environment, mocks needed objects, contexts, etc.
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
@Ignore
public abstract class MediaManagerBaseTest {

	protected static Logger log = LoggerFactory.getLogger(MediaManagerBaseTest.class);
	
	protected static final String TEST_MEDIA_DIR = "mediastorage/";
	protected static final String TEST_PERSISTENCE_PATH = "http://yourserver.com/rest/persistence";
	
	protected static final String TESTIMAGE_1 = "src/test/resources/testfiles/image-1.png";
	protected static final String TESTIMAGE_2 = "src/test/resources/testfiles/image-2.jpg";
	protected static final String TESTIMAGE_3 = "src/test/resources/testfiles/image-3.jpg";

    protected static File testMediaDir = null;
    
    protected static MediaItemPersistenceProxyFactory mockedProxyFactory;
    protected static MockMediaItemResourceProxy mockedRestProxy = new MockMediaItemResourceProxy();
    
    public static String VIMEO_VIDEO_ID = "12983746";
    public static String YOUTUBE_VIDEO_ID = "QDK8U-VIH_o";
    
    protected static Map<String, Boolean> vimeoTestCases;
    protected static Map<String, Boolean> youtubeTestCases;
    
    @BeforeClass
    public static void setup() {
		youtubeTestCases = new HashMap<String, Boolean>();
		youtubeTestCases.put("http://www.youtube.com/watch?v=" + YOUTUBE_VIDEO_ID + "&feature=feedrec_grec_index", true);
		youtubeTestCases.put("http://www.youtube.com/user/SomeUser#p/a/u/1/" + YOUTUBE_VIDEO_ID + "?portrait=0&color=333", true);
		youtubeTestCases.put("http://www.youtube.com/v/" + YOUTUBE_VIDEO_ID + "?fs=1&amp;hl=en_US&amp;rel=0", true);
		youtubeTestCases.put("http://www.youtube.com/watch?v=" + YOUTUBE_VIDEO_ID + "#t=0m10s", true);
		youtubeTestCases.put("http://www.youtube.com/embed/" + YOUTUBE_VIDEO_ID + "?rel=0", true);
		youtubeTestCases.put("http://www.youtube.com/watch?v=" + YOUTUBE_VIDEO_ID + "?portrait=0&color=333", true);
		youtubeTestCases.put("http://youtu.be/" + YOUTUBE_VIDEO_ID, true);
    	
		vimeoTestCases = new HashMap<String, Boolean>();
		vimeoTestCases.put("http://player.vimeo.com/video/" + VIMEO_VIDEO_ID + "?portrait=0&color=333", true);
		vimeoTestCases.put("player.vimeo.com/video/" + VIMEO_VIDEO_ID + "?portrait=0&color=333", true);
		vimeoTestCases.put("https://vimeo.com/moogaloop.swf?clip_id=" + VIMEO_VIDEO_ID + "&server=vimeo.com&color=00adef&fullscreen=1", true);
		vimeoTestCases.put("vimeo.com/" + VIMEO_VIDEO_ID + "?portrait=0&color=333", true);
		
		testMediaDir = new File(TEST_MEDIA_DIR);
		if(!testMediaDir.exists())
			testMediaDir.mkdirs();
		
		assertTrue(TESTIMAGE_1 + " does not exist.", new File(TESTIMAGE_1).exists());
		assertTrue(TESTIMAGE_2 + " does not exist.", new File(TESTIMAGE_2).exists());
		assertTrue(TESTIMAGE_3 + " does not exist.", new File(TESTIMAGE_3).exists());
		
		mockedProxyFactory = mock(MediaItemPersistenceProxyFactory.class);
		when(mockedProxyFactory.getInstance()).thenReturn(mockedRestProxy);
    }
    
    /**
     * Simulates Spring Container Manager
     * 
     * @return
     * @throws Exception 
     */
    protected MediaManager getMediaManager() {
    	DefaultMediaManager manager = null;
    	try {
	    	manager = new DefaultMediaManager();
	    	manager.setMediaItemFileSystemManager(new MediaItemFileSystemManager());
	    	
	    	MediaItemPersistenceManager persistenceManager = new MediaItemPersistenceManager();
	    	persistenceManager.setMediaItemPersistenceProxyFactory(mockedProxyFactory);
	    	persistenceManager.afterPropertiesSet();
	    	manager.setMediaItemPersistenceManager(persistenceManager);
	    	
	    	return manager;
    	} catch (Exception e) {
    		log.error(e.getMessage(), e);
    	}
    	return manager;
    }
    
	
	@AfterClass
	public static void teardown() {
		try {
			// clean up
    		if(testMediaDir != null && testMediaDir.exists())
    			FileUtils.deleteDirectory(testMediaDir);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

}
