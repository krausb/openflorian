package de.openflorian.zk.model;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zul.AbstractListModel;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListSubModel;
import org.zkoss.zul.event.ListDataListener;

/**
 * Generic Data List Model
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 * @param <T>
 */
public class DataListModel<T> extends AbstractListModel<T> implements ListSubModel<T> {
	private static final long serialVersionUID = -3631334919077573003L;
	
	protected Logger log = LoggerFactory.getLogger(getClass());
	
	private LinkedList<T> 					data			= null; 
	private List<ListDataListener> 			dataListener	= null;
	private DataListModelSubModelLoader<T>  subModelLoader  = null;
	
	private boolean prefixItem;

	/**
	 * CTOR for DataListModel to work only with SubModel for autocomplete
	 * 
	 * @param subModelLoader
	 */
	public DataListModel(List<T> data, DataListModelSubModelLoader<T> subModelLoader) {
		this(data);
		this.subModelLoader = subModelLoader;
	}
	
	/**
	 * CTOR for DataListModel to work only with SubModel for autocomplete
	 * 
	 * @param subModelLoader
	 */
	public DataListModel(DataListModelSubModelLoader<T> subModelLoader) {
		this();
		this.subModelLoader = subModelLoader;
		this.data = new LinkedList<T>();
	}
	
	/**
	 * CTOR where {@code prefixItem} param decides wether to insert a "Select..." item at the beginning of the list.
	 * 
	 * @param data
	 * @param prefixItem
	 */
	public DataListModel(List<T> data, boolean prefixItem) {
		this(data);
		this.prefixItem = prefixItem;
	}
	
	/**
	 * CTOR for a non "Select..." prefixed list
	 * 
	 * @param data
	 * @param prefixItem
	 */
	public DataListModel(List<T> data) {
		this();
		this.prefixItem = false;
		
		// clone data list
		this.data = new LinkedList<T>();
		for(T o: data) {
			this.data.add(o);
		}
	}
	
	public DataListModel() {
		this.dataListener = new LinkedList<ListDataListener>();
	}
	
	/**
	 * 
	 * @param e {@link T}
	 * @return int index
	 */
	public int getIndexFromElement(T e) {
		if(this.prefixItem && e == null)
			return 0;
		
		int index = (this.prefixItem ? 0 : -1);
		
		for(T item: this.data) {
			index++;
			if(e.equals(item))
				break;
		}
		return index;
	}
	
	@Override
	public void addListDataListener(ListDataListener l) {
		this.dataListener.add(l);
	}

	@Override
	public T getElementAt(int index) {
		if(this.prefixItem && index == 0)
			return null;
			
		return this.data.get((this.prefixItem ? index-1 : index));
	}

	@Override
	public int getSize() {
		return (this.prefixItem ? this.data.size() + 1 : this.data.size());
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		this.dataListener.remove(l);
	}

	@Override
	public ListModel<T> getSubModel(Object value, int rows) {
		if(subModelLoader != null) {
			this.data.clear();
			this.data.addAll(subModelLoader.list((String)value, rows));
			return new DataListModel<T>(this.data);
		} else {
			return new DataListModel<T>(new LinkedList<T>());
		}
			
	}
	
	/**
	 * Accessor to internal data list
	 * @return
	 */
	public List<T> getData() {
		return this.data;
	}

}
