package de.openflorian.ui.system;

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

import java.util.Map.Entry;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

/**
 * {@link Entry<String, String>} Listbox Listitem Renderer
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class InformationListItemRenderer implements ListitemRenderer<Entry<String, String>> {

	public void render(Listitem item, Entry<String, String> entry, int index) throws Exception {
		item.setValue(entry);
		
		Listcell key = new Listcell(entry.getKey());
		key.setParent(item);
		
		Listcell value = new Listcell(entry.getValue());
		value.setParent(item);		
	}

}
