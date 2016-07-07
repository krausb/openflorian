package de.openflorian.data.jdbc;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class providing functions to obtain the datasource from the application server jndi context.
 * 
 * @author ceth
 */
public abstract class DatabaseConnector {

	private static final Logger log = LoggerFactory.getLogger(DatabaseConnector.class);

	/**
	 * Obtain jdbc datasource
	 * 
	 * @return
	 */
	protected static DataSource getDataSource() {
		Context ctx = null;
		try {
			ctx = new InitialContext();
			return (DataSource) ctx.lookup("java:comp/env/jdbc/openflorian");
		}
		catch (NamingException e) {
			log.error(e.getMessage(), e);
			throw new IllegalStateException("Openflorian DataSource not found!", e);
		}
	}

}
