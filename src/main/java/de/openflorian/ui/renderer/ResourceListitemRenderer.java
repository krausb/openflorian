package de.openflorian.ui.renderer;

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

import java.text.SimpleDateFormat;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import de.openflorian.data.model.Operation;
import de.openflorian.ui.ZkGlobals;
import de.openflorian.web.user.User;

/**
 * {@link Operation} Listbox Listitem Renderer
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class ResourceListitemRenderer implements ListitemRenderer<Operation> {
	
	public void render(Listitem item, Operation operation, int index) throws Exception {
		item.setValue(operation);
		
		Listcell operationNr = null;
		if(operation.getOperationNr() != null) 
			operationNr = new Listcell(operation.getOperationNr());
		else
			operationNr = new Listcell("");
		operationNr.setParent(item);
		
		Listcell buzzword = null;
		if(operation.getBuzzword() != null) 
			buzzword = new Listcell(operation.getBuzzword());
		else
			buzzword = new Listcell("");
		buzzword.setParent(item);
		
		Listcell priority = null;
		if(operation.getPriority() != null) 
			priority = new Listcell(operation.getPriority());
		else
			priority = new Listcell("");
		priority.setParent(item);
		
		Listcell city = null;
		if(operation.getCity() != null) 
			city = new Listcell(operation.getCity());
		else
			city = new Listcell("");
		city.setParent(item);
		
		Listcell street = null;
		if(operation.getStreet() != null) 
			street = new Listcell(operation.getStreet());
		else
			street = new Listcell("");
		street.setParent(item);
		
		SimpleDateFormat format = new SimpleDateFormat(ZkGlobals.FORMAT_DATETIME);
		
		Listcell incurredAt = null;
		if(operation.getIncurredAt() != null) 
			incurredAt = new Listcell(format.format(operation.getIncurredAt()));
		else
			incurredAt = new Listcell("");
		incurredAt.setParent(item);
		
		Listcell dispatchedAt = null;
		if(operation.getDispatchedAt() != null) 
			dispatchedAt = new Listcell(format.format(operation.getDispatchedAt()));
		else
			dispatchedAt = new Listcell("");
		dispatchedAt.setParent(item);
		
	}

}
