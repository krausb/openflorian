package de.openflorian.media;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.openflorian.media.model.MediaItem;

/**
 * TestCases for {@link MediaManager} and {@link DefaultMediaManager}
 * 
 * Verfying the behaviour of the main {@link DefaultMediaManager} service class.
 * 
 * @author Bastian Kraus <bk@pogo-systems.de>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/config/mediaManagerContext.xml" })
public class MediaItemFileServerServletTest extends MediaManagerBaseTest {

	public static final String SERVLET_CONTEXT 	= "/textcontext";
	public static final String SERVLET_PATH  	= "/testservlet";
	public static final String SERVLET_TEST_URI = SERVLET_CONTEXT + SERVLET_PATH;
	
	@Resource
	protected DefaultMediaManager mediaManager;

	private HttpServletRequest 		mockedRequest = mock(HttpServletRequest.class);
	private HttpServletResponse 	mockedResponse = mock(HttpServletResponse.class);
	
	private ServletConfig			mockedServletConfig= mock(ServletConfig.class);
	private MockServletContext		mockedServletContext = new MockServletContext(SERVLET_CONTEXT);
	
	protected MediaItemFileServerServlet servlet;
	
	@Before
	public void setupTest() throws Exception {
		mediaManager.persistenceManager
				.setMediaItemPersistenceProxyFactory(mockedProxyFactory);
		mediaManager.persistenceManager.afterPropertiesSet();

		mockedServletContext.setContextPath(SERVLET_CONTEXT);
		
		when(mockedRequest.getContextPath()).thenReturn(SERVLET_CONTEXT);
		when(mockedRequest.getServletPath()).thenReturn(SERVLET_PATH);
		when(mockedResponse.getOutputStream()).thenReturn(mock(ServletOutputStream.class));
		when(mockedServletConfig.getServletContext()).thenReturn(mockedServletContext);
		
		servlet = new MediaItemFileServerServlet();
		// TODO: implement spring integration testing
		//servlet.init(mockedServletConfig);
		servlet.setMediaManager(mediaManager);
	}

	@Test
	public void streamFile() throws IOException, MediaItemManagerException, ServletException {
		File[] filesToStore = new File[] {new File(TESTIMAGE_1), new File(TESTIMAGE_2), new File(TESTIMAGE_3)};
		
		List<MediaItem> storedItems = new ArrayList<MediaItem>();
		for(File srcFile: filesToStore) {
			MediaItem storedItem = null; 
			
			storedItem = mediaManager.store(srcFile, srcFile.getName(), "storage of testfile 1", true);
			storedItems.add(storedItem);
			
			assertFalse("There is no stored media item.", storedItem == null);
			assertFalse("The MediaItem has no relative file storage path.", !StringUtils.isNotEmpty(storedItem.getPath()));
			
			File storedItemFile = mediaManager.getStoredFile(storedItem);
			assertEquals("File sizes do not match. Expecting: " + srcFile.length() + "; current: " + storedItemFile.length(), srcFile.length(), storedItemFile.length());
		}
		
		MediaItem itemToStream = storedItems.get(1);
		when(mockedRequest.getParameter(MediaItemFileServerServlet.REQUEST_MEDIA_ITEM_IDENTIFIER)).thenReturn(itemToStream.getIdentifier());
		when(mockedRequest.getRequestURI()).thenReturn(SERVLET_TEST_URI + "/" + itemToStream.getIdentifier());
		
		servlet.doGet(mockedRequest, mockedResponse);
		
		verify(mockedResponse, times(1)).setContentType(itemToStream.getContentType());
		verify(mockedResponse, times(1)).addHeader("Content-Disposition", "inline; filename="
				+ itemToStream.getName());
		verify(mockedResponse, times(1)).setContentLength((int) filesToStore[1].length());
	}

}
