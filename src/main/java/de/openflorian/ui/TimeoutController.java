package de.openflorian.ui;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;

import de.openflorian.web.user.AuthenticatedUserSessionLocal;

/**
 * Session Timeout Controller<br/>
 * <br/>
 * ZUL: /timeout.zul
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class TimeoutController extends GenericForwardComposer {
	private static final long serialVersionUID = 8133567451702629273L;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
    @Override
    public void doAfterCompose( Component comp ) throws Exception {
        super.doAfterCompose(comp);
        
        if(AuthenticatedUserSessionLocal.get() != null) {
        	AuthenticatedUserSessionLocal.set(null);
        	execution.sendRedirect(ZkGlobals.PAGE_LOGIN);
        } else {
        	execution.sendRedirect("/index.zul");
        }
    }
}
