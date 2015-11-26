package de.openflorian.ui.model;

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

import de.openflorian.service.UserService;
import de.openflorian.web.core.ContainerManager;
import de.openflorian.web.user.User;
import de.openflorian.zk.model.DataListModel;
import de.openflorian.zk.model.PagingDataListModel;

/**
 * {@link PagingDataListModel} Implementation for {@link Admin}
 * <br/>
 * Supplies paged data lists from specific dao
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class UserPagingDataListModel extends PagingDataListModel<User> {

	public UserPagingDataListModel(String sort, int pageSize) throws Exception {
		super(sort, pageSize);
	}

	/**
	 * Refresh command triggered by ZK Paging Component
	 * @throws Exception 
	 */
	public void refresh() {

		UserService service = (UserService)ContainerManager.getComponent("userService");
		
		totalSize = service.count();
		// amout of data changed
		if(activePage*pageSize>=totalSize){
            activePage = 0;
        }
		List<User> list = service.listByPage(this.sort, activePage, pageSize);
		this.data = new DataListModel<User>(list);
	}
	
}
