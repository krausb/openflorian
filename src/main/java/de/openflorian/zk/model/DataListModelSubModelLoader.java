package de.openflorian.zk.model;

import java.util.List;

import org.zkoss.zul.Combobox;

/**
 * SubModel Loader to provide data for {@link Combobox} autocomplete feature
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public interface DataListModelSubModelLoader<T> {

	/**
	 * Provide data by startswith like {@code filter} limited by {@code rows}
	 * 
	 * @param filter
	 * @param rows
	 * @return {@link List}<{@link T}
	 */
	public List<T> list(String filter, int rows);
	
}
