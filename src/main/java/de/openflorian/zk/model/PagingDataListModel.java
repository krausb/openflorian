package de.openflorian.zk.model;

import java.lang.reflect.ParameterizedType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

/**
 * Paging Data List Model
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 * @param <T>
 */
@VariableResolver(DelegatingVariableResolver.class)
public abstract class PagingDataListModel<T> {

	protected Logger log = LoggerFactory.getLogger(getClass());
	
	protected Class<T> entityType;
	
	protected String sort;
	
	protected int pageSize = 15;
	protected int activePage = 0;
	protected long totalSize = 0;
	
	protected DataListModel<T> data = null;
	
	@SuppressWarnings("unchecked")
	public PagingDataListModel(String sort, int pageSize) throws Exception {
		this.entityType = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		
		this.sort = sort;
		this.pageSize = pageSize;
	}
	
	/**
	 * Get current data
	 * @return
	 */
	public DataListModel<T> getCurrentData() {
		return this.data;
	}
	
	/**
	 * Determines if model has more than one page
	 * 
	 * @return
	 */
	public boolean hasMoreThanOnePage() {
		return totalSize > pageSize;
	}
	
	/**
	 * ZK Spring Postconstruct
	 */
	@Init
	public void init() {
		refresh();
	}
	
	public abstract void refresh();

	/*
	 * GETTER / SETTER
	 */
	
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getActivePage() {
		return activePage;
	}

	public void setActivePage(int activePage) {
		this.activePage = activePage;
	}

	public long getTotalSize() {
		return totalSize;
	}
	
}
