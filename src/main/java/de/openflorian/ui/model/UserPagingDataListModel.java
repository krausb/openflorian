package de.openflorian.ui.model;

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
