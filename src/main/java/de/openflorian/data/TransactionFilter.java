package de.openflorian.data;

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

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.openflorian.data.jpa.GenericDao;

/**
 * JPA Transaction Filter<br/>
 * <br/>
 * Each servlet request opens a new transaction. Will rollback transaction on
 * failure.
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class TransactionFilter implements Filter {

	private static final Logger log = LoggerFactory.getLogger(TransactionFilter.class);

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		EntityManager em = GenericDao.getLocalEntityManager();

		try {
			em.getTransaction().begin();

			chain.doFilter(req, res);
			EntityTransaction t = em.getTransaction();

			if (t.getRollbackOnly())
				t.rollback();
			else
				t.commit();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			throw new ServletException(e);
		} finally {
			try {
				em.close();
			} catch (Exception e) {
				throw new ServletException(e);
			}
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// nothing to do here
	}

	@Override
	public void destroy() {
		// nothing to do here
	}

}
