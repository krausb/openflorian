package de.openflorian.service;

import java.util.List;

import javax.xml.bind.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.openflorian.data.dao.media.MediaItemDao;
import de.openflorian.media.model.MediaItem;
import de.openflorian.media.ws.MediaItemResource;

/**
 * {@link MediaItemS} DAO Service Bean
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
@Service
public class MediaItemService implements MediaItemResource {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private MediaItemDao dao;

	public MediaItemService() {
		log.debug("MediaItemS service created...");
	}

	/**
	 * Find all {@link MediaItemS} entities.
	 * 
	 * @return
	 * 		{@link List}<{@link MediaItemS}>
	 */
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public List<MediaItem> findAll() {
		return dao.findAll("username ASC");
	}

	/**
	 * Lists {@link MediaItemS}s paged by
	 * 
	 * @param identifier
	 * @return
	 * 		{@link MediaItem}
	 */
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public MediaItem getByIdentifier(String identifier) {
		return dao.findByAttribute("identifier", identifier);
	}
	/**
	 * Find an {@link MediaItemS} by <code>id</code>.
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public MediaItem getById(long id) {
		return dao.findById(id);
	}

	/**
	 * Count the amount of available {@link MediaItemS} entities
	 * 
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public long count() {
		return dao.sizeOf();
	}

	/**
	 * Persist given <code>o</code>
	 * 
	 * @param o {@link MediaItemS}
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public MediaItem persist(MediaItem o) {
		try {
			return dao.merge(o);
		} catch (ValidationException e) {
			// TODO: change to " persist(...) throws ValidationException
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * Delete {@link MediaItemS} by given <code>id</code>
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void remove(long id) {
		dao.remove(id);
	}
	
}
