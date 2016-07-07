package de.openflorian.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.openflorian.data.dao.OperationDao;

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

import io.vertx.core.AbstractVerticle;

/**
 * Workaround for stalled JDBC Connections
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class DatabaseConnectionKeepaliveVerticle extends AbstractVerticle {

	private static final Logger log = LoggerFactory.getLogger(DatabaseConnectionKeepaliveVerticle.class);

	@Override
	public void start() throws Exception {

		vertx.setPeriodic(60000, id -> {
			long count;
			try {
				count = new OperationDao().count();
				if (log.isTraceEnabled())
					log.trace("Current operations: " + count);
			}
			catch (final Exception e) {
				log.error(e.getMessage(), e);
			}
		});

	}

}
