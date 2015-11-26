package de.openflorian.zk.model;

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
