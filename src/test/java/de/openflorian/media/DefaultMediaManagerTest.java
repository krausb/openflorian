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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import de.openflorian.media.model.MediaItem;

/**
 * TestCases for {@link MediaManager} and {@link DefaultMediaManager}<br/>
 * <br/>
 * Verfying the behaviour of the main {@link DefaultMediaManager} service class.
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(MediaItemPersistenceProxyFactory.class)
public class DefaultMediaManagerTest extends MediaManagerBaseTest {

	@Test
	public void initializeTest() {
		// check for 
		assertEquals("DefaultMediaManager initialized wrong file storage path. Expected: " + TEST_MEDIA_DIR + "; is: " + MediaItemFileSystemManager.getFileStorePath(), TEST_MEDIA_DIR, 
				MediaItemFileSystemManager.getFileStorePath());
		assertEquals("DefaultMediaManager initialized wrong file storage path. Expected: " + TEST_PERSISTENCE_PATH + "; is: " + MediaItemPersistenceManager.getPersistenceRestPath(), TEST_PERSISTENCE_PATH, 
				MediaItemPersistenceManager.getPersistenceRestPath());
	}
	
	@Test
	public void storageTest() throws MediaItemManagerException, IOException {
		File[] filesToStore = new File[] {new File(TESTIMAGE_1), new File(TESTIMAGE_2), new File(TESTIMAGE_3)};
		MediaManager manager = getMediaManager();
		
		for(File srcFile: filesToStore) {
			MediaItem storedItem = null;
			
			storedItem = manager.store(srcFile, srcFile.getName(), "storage of testfile 1", true);
			
			assertFalse("There is no stored media item.", storedItem == null);
			assertFalse("The MediaItem has no relative file storage path.", !StringUtils.isNotEmpty(storedItem.getPath()));
			
			File storedItemFile = manager.getStoredFile(storedItem);
			assertEquals("File sizes do not match. Expecting: " + srcFile.length() + "; current: " + storedItemFile.length(), srcFile.length(), storedItemFile.length());
			
			assertFalse("The stored media item has no identifier set.", StringUtils.isEmpty(storedItem.getIdentifier()));
		}
	}

	@Test
	public void storageBinaryDataTest() throws MediaItemManagerException, IOException {
		File[] filesToStore = new File[] {new File(TESTIMAGE_1), new File(TESTIMAGE_2), new File(TESTIMAGE_3)};
		MediaManager manager = getMediaManager();
		
		for(File srcFile: filesToStore) {
			MediaItem storedItem = null;
			
			byte[] fileContent = new byte[(int)srcFile.length()];
			new FileInputStream(srcFile).read(fileContent);
			
			storedItem = manager.store(fileContent, srcFile.getName(), "storage of testfile 1", true);
			
			assertFalse("There is no stored media item.", storedItem == null);
			assertFalse("The MediaItem has no relative file storage path.", !StringUtils.isNotEmpty(storedItem.getPath()));
			
			File storedItemFile = manager.getStoredFile(storedItem);
			assertEquals("File sizes do not match. Expecting: " + srcFile.length() + "; current: " + storedItemFile.length(), srcFile.length(), storedItemFile.length());
			
			assertFalse("The stored media item has no identifier set.", StringUtils.isEmpty(storedItem.getIdentifier()));
		}
	}
	
	@Test
	public void removeTest() throws IOException, MediaItemManagerException {
		File[] filesToStore = new File[] {new File(TESTIMAGE_1), new File(TESTIMAGE_2), new File(TESTIMAGE_3)};
		MediaManager manager = getMediaManager();
		
		// store items
		List<MediaItem> storedItems = new ArrayList<MediaItem>();
		for(File srcFile: filesToStore) {
			MediaItem storedItem = null;
			
			storedItem = manager.store(srcFile, srcFile.getName(), "storage of testfile 1", true);
			storedItems.add(storedItem);
			
			assertFalse("There is no stored media item.", storedItem == null);
			assertFalse("The MediaItem has no relative file storage path.", !StringUtils.isNotEmpty(storedItem.getPath()));
			
			File storedItemFile = manager.getStoredFile(storedItem);
			assertEquals("File sizes do not match. Expecting: " + srcFile.length() + "; current: " + storedItemFile.length(), srcFile.length(), storedItemFile.length());
		}
		
		// remove items
		MediaItem itemToDelete = storedItems.get(1); 
		manager.remove(itemToDelete);
		
		// check if manager "has removed" the file
		boolean exceptionOccured = false;
		try {
			@SuppressWarnings("unused")
			File fileOfDeleted = manager.getStoredFile(itemToDelete);
		} catch(FileNotFoundException e) {
			// everything alright
			exceptionOccured = true;
		}
		assertTrue("No FileNotFoundException occured.", exceptionOccured);
		
		// check if file is not found in persistence
		MediaItem selectedDeletedItem = manager.getById(itemToDelete.getMediaItemId());
		assertEquals("The media item was not removed from persistence properly.", selectedDeletedItem, null);
		
		// check if the file is really deleted from disk
		File fileOfDeleted = new File(MediaItemFileSystemManager.getFileStorePath(), itemToDelete.getPath());
		assertFalse("File was not deleted properly", fileOfDeleted.exists());
	}
	
	/**
	 * Test storage of youtube video - especially for correct extraction of video id
	 */
	@Test
	public void youtubeStorageTest() {
		MediaManager manager = getMediaManager();
		
		for(Entry<String, Boolean> testCase: youtubeTestCases.entrySet()) {
			
			MediaItem item = null;
			try {
				item = manager.store(testCase.getKey(), "youtube", "youtube", "", false, true);
			} catch (MediaItemManagerException e) {
				fail("Exception occured: " + e.getMessage());
			}	
			assertEquals("Extracted video id not matching.", YOUTUBE_VIDEO_ID, item.getPath());
		}
	}
	
	/**
	 * Test storage of imeo video - especially for correct extraction of video id
	 */
	@Test
	public void vimeoStorageTest() {
		MediaManager manager = getMediaManager();
		
		for(Entry<String, Boolean> testCase: vimeoTestCases.entrySet()) {
			
			MediaItem item = null;
			try {
				item = manager.store(testCase.getKey(), "vimeo", "vimeo", "", false, true);
			} catch (MediaItemManagerException e) {
				fail("Exception occured: " + e.getMessage());
			}	
			assertEquals("Extracted video id not matching.", VIMEO_VIDEO_ID, item.getPath());
		}
	}
	
}
