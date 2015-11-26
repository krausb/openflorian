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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;

import org.apache.commons.lang.StringUtils;
import org.jboss.resteasy.util.HttpResponseCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import de.openflorian.media.model.MediaItem;

/**
 * Media Item File Server Servlet<br/>
 * <br/>
 * Serves physical {@link MediaItem} files through
 * {@link MediaManager#getStoredFile(MediaItem)}. <br/>
 * Requests have to provide a request param with the name
 * {@link MediaItemFileServerServlet#REQUEST_MEDIA_ITEM_IDENTIFIER}.
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class MediaItemFileServerServlet extends HttpServlet implements Servlet {

	public static final String REQUEST_MEDIA_ITEM_IDENTIFIER = "id";

	private static final long serialVersionUID = -5109720372433390091L;

	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	protected DefaultMediaManager mediaManager;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		serveMediaItem(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		serveMediaItem(request, response);
	}

	/**
	 * File Server: Serves a requested {@link MediaItem} out to
	 * {@link HttpServletResponse#getOutputStream()}<br/>
	 * as Content-Disposition: attachment; mit dem jeweiligen
	 * {@link MediaItem#getContentType()}
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void serveMediaItem(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String requestIdentifier = extractIdentifier(request);
		if (StringUtils.isEmpty(requestIdentifier))
			throw new WebApplicationException(new IllegalArgumentException("Required param value missing."), HttpResponseCodes.SC_BAD_REQUEST);

		log.trace("Processing media item request for: " + requestIdentifier);
		
		MediaItem item = null;
		File realFile = null;

		FileInputStream fileInputStream = null;
		try {
			item = mediaManager.getByIdentifier(requestIdentifier);
			if(item != null) {
				realFile = mediaManager.getStoredFile(item);
	
				// set response header
				response.setContentType(item.getContentType());
				response.addHeader("Content-Disposition", "inline; filename="
						+ item.getName());
				response.setContentLength((int) realFile.length());
	
				log.trace("Media item with  name: " + item.getName() + "; Content Type: " + item.getContentType() + "; name: " + item.getName() + ")");
				log.trace("Real file: " + realFile.getAbsolutePath());
				
				// stream file
				fileInputStream = new FileInputStream(realFile);
				OutputStream responseOutputStream = response.getOutputStream();
				int bytes;
				while ((bytes = fileInputStream.read()) != -1) {
					responseOutputStream.write(bytes);
				}
			}
		} catch (FileNotFoundException e) {
			if (item != null)
				log.error(
						"Real file not found for requested media item "
								+ item.getIdentifier() + " - id: "
								+ item.getMediaItemId(), e);
		} catch (MediaItemManagerException e) {
			log.error(e.getMessage(), e);
		} finally {
			if (fileInputStream != null)
				fileInputStream.close();
		}
	}
	
	/**
	 * Helper: Extract the UUID identifier from the given URL of format:<br/>
	 * /contextpath/servletpath/-identifier-<br/>
	 * <br/>
	 * The identifier is cropped to 36 chars
	 * 
	 * @param url
	 * @return
	 */
	private String extractIdentifier(HttpServletRequest request) {
		return request.getRequestURI().replace(request.getContextPath() + request.getServletPath() + "/", "").substring(0,36);
	}

	/**
	 * Initialize this servlet by adding SpringBean Autowiring Support<br/>
	 * to inject {@link MediaManager} dependency.
	 */
	@Override
	public void init(ServletConfig config) {
		log.trace("Initializing File Server Servlet...");
		try {
			super.init(config);
		} catch (ServletException e) {
		}
		log.trace("Enabling spring autowiring on instance...");
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
				config.getServletContext());
	}

	/**
	 * Dependency injection: {@link MediaManager}
	 * 
	 * @param manager
	 */
	public void setMediaManager(DefaultMediaManager manager) {
		this.mediaManager = (DefaultMediaManager) manager;
	}

}
