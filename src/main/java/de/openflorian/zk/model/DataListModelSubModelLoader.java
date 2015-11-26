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
