package de.openflorian.data.dao;

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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.openflorian.data.jdbc.DatabaseConnector;
import de.openflorian.data.model.Operation;
import de.openflorian.data.model.OperationResource;

/**
 * {@link OperationDao}
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class OperationDao extends DatabaseConnector {

	private static final Logger log = LoggerFactory.getLogger(OperationDao.class);

	private static final String INSERT_FIELD_LIST = "object, keyword, operationNr, city, crossway, priority, buzzword, street, positionLatitude, positionLongitude, resourcesRaw, incurredAt, takenOverAt, dispatchedAt";
	private static final String UPDATE_FIELD_LIST = "object = ?, keyword = ?, operationNr = ?, city = ?, crossway = ?, priority = ?, buzzword = ?, street = ?, positionLatitude = ?, positionLongitude = ?, resourcesRaw = ?, incurredAt = ?, takenOverAt = ?, dispatchedAt = ?";
	private static final String FIELD_LIST = "id, " + INSERT_FIELD_LIST;

	/**
	 * Get the amount of all available {@link Operation} in persistence context.
	 * 
	 * @return {@link long}
	 */
	public long count() throws Exception {
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		final String stmt = "SELECT count(id) AS oc FROM of_operation";

		try {
			dbConnection = getDataSource().getConnection();
			preparedStatement = dbConnection.prepareStatement(stmt);

			// execute select SQL stetement
			final ResultSet rs = preparedStatement.executeQuery();

			if (log.isDebugEnabled()) {
				log.debug(rs.getStatement().toString());
			}

			if (rs.next()) {
				return rs.getLong("oc");
			}
			else {
				return 0;
			}

		}
		catch (final SQLException e) {
			log.error("Error while executing SQL Query: " + stmt + " - " + e.getMessage(), e);
			throw e;
		}
		finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}
	}

	/**
	 * Gets a specific {@link Operation} by <code>id</code>
	 * 
	 * @param id
	 * @return {@link Operation}
	 * @throws Exception
	 */
	public Operation getById(Long id) throws Exception {
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		final String stmt = "SELECT " + FIELD_LIST + " FROM of_operation WHERE id = ?";

		try {
			dbConnection = getDataSource().getConnection();
			preparedStatement = dbConnection.prepareStatement(stmt);
			preparedStatement.setLong(1, id);

			// execute select SQL stetement
			final ResultSet rs = preparedStatement.executeQuery();

			if (log.isDebugEnabled()) {
				log.debug(rs.getStatement().toString());
			}

			if (rs.next()) {
				return getOperationFromResultSet(rs);
			}
			else {
				return null;
			}

		}
		catch (final SQLException e) {
			log.error("Error while executing SQL Query: " + stmt + " - " + e.getMessage(), e);
			throw e;
		}
		finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}
	}

	/**
	 * Lists all available {@link Operation}s
	 * 
	 * @return {@link List}<{@link Operation}>
	 * @throws Exception
	 */
	public List<Operation> list() throws Exception {
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		final String stmt = "SELECT " + FIELD_LIST + " FROM of_operation";

		try {
			dbConnection = getDataSource().getConnection();
			preparedStatement = dbConnection.prepareStatement(stmt);

			// execute select SQL stetement
			final ResultSet rs = preparedStatement.executeQuery();

			if (log.isDebugEnabled()) {
				log.debug(rs.getStatement().toString());
			}

			final List<Operation> ops = new ArrayList<>();
			while (rs.next()) {
				ops.add(getOperationFromResultSet(rs));
			}
			return ops;
		}
		catch (final SQLException e) {
			log.error("Error while executing SQL Query: " + stmt + " - " + e.getMessage(), e);
			throw e;
		}
		finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}
	}

	/**
	 * Lists {@link Operation}s paged by
	 * 
	 * @param order
	 * @param activePage
	 * @param pageSize
	 * @return {@link List}<{@link Operation}>
	 * @throws Exception
	 */
	public List<Operation> listByPage(String order, int activePage, int pageSize) throws Exception {
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		final String stmt = "SELECT " + FIELD_LIST + " FROM of_operation ORDER BY " + order + " LIMIT "
				+ ((activePage - 1) * pageSize) + ", " + pageSize;

		try {
			dbConnection = getDataSource().getConnection();
			preparedStatement = dbConnection.prepareStatement(stmt);

			// execute select SQL stetement
			final ResultSet rs = preparedStatement.executeQuery();

			if (log.isDebugEnabled()) {
				log.debug(rs.getStatement().toString());
			}

			final List<Operation> ops = new ArrayList<>();
			while (rs.next()) {
				ops.add(getOperationFromResultSet(rs));
			}
			return ops;
		}
		catch (final SQLException e) {
			log.error("Error while executing SQL Query: " + stmt + " - " + e.getMessage(), e);
			throw e;
		}
		finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}
	}

	/**
	 * Persist given <code>o</code> {@link Operation} to persistence context
	 * 
	 * @param object
	 * @return persisted <code>object</code>
	 */
	public Operation insert(Operation o) throws Exception {
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		final String stmt = "INSERT INTO of_operation (" + INSERT_FIELD_LIST + ") VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		try {
			dbConnection = getDataSource().getConnection();
			preparedStatement = dbConnection.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);

			preparedStatement.setString(1, o.getObject());
			preparedStatement.setString(2, o.getKeyword());
			preparedStatement.setString(3, o.getOperationNr());
			preparedStatement.setString(4, o.getCity());
			preparedStatement.setString(5, o.getCrossway());
			preparedStatement.setString(6, o.getPriority());
			preparedStatement.setString(7, o.getBuzzword());
			preparedStatement.setString(8, o.getStreet());
			preparedStatement.setDouble(9, o.getPositionLatitude());
			preparedStatement.setDouble(10, o.getPositionLongitude());
			preparedStatement.setString(11, o.getResourcesRaw());
			preparedStatement.setDate(12, new java.sql.Date(o.getIncurredAt().getTime()));
			if (o.getDispatchedAt() != null)
				preparedStatement.setDate(13, new java.sql.Date(o.getDispatchedAt().getTime()));
			else
				preparedStatement.setDate(13, null);
			if (o.getTakenOverAt() != null)
				preparedStatement.setDate(14, new java.sql.Date(o.getTakenOverAt().getTime()));
			else
				preparedStatement.setDate(14, null);

			preparedStatement.executeUpdate();

			if (log.isDebugEnabled()) {
				log.debug(stmt);
				log.debug(o.toString());
			}

			final ResultSet rs = preparedStatement.getGeneratedKeys();
			if (rs.next()) {
				o.setId(rs.getLong(1));
			}
			else {
				throw new Exception("Insert failed - no generated key: " + stmt);
			}

			if (o.getResources() != null)
				o.getResources().forEach(res -> {
					try {
						addResourceToOperation(o, res);
					}
					catch (final Exception e) {
						log.error(e.getMessage(), e);
					}
				});

			return o;
		}
		catch (final SQLException e) {
			log.error("Error while executing SQL Query: " + stmt + " - " + e.getMessage(), e);
			throw e;
		}
		finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}
	}

	/**
	 * Persist given <code>o</code> {@link Operation} to persistence context
	 * 
	 * @param object
	 * @return persisted <code>object</code>
	 */
	public void addResourceToOperation(Operation o, OperationResource res) throws Exception {
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		final String stmt = "INSERT INTO of_operation_of_operation_resource (operation_id, operation_resource_id) VALUES(?,?)";

		try {
			dbConnection = getDataSource().getConnection();
			preparedStatement = dbConnection.prepareStatement(stmt);

			preparedStatement.setLong(1, o.getId());
			preparedStatement.setLong(2, res.getId());

			preparedStatement.executeUpdate();

			if (log.isDebugEnabled()) {
				log.debug(stmt);
				log.debug(o.toString());
				log.debug(res.toString());
			}
		}
		catch (final SQLException e) {
			log.error("Error while executing SQL Query: " + stmt + " - " + e.getMessage(), e);
			throw e;
		}
		finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}
	}

	/**
	 * Update given <code>o</code> {@link Operation} to persistence context
	 * 
	 * @param object
	 * @return persisted <code>object</code>
	 */
	public void update(Operation o) throws Exception {
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		final String stmt = "UPDATE of_operation SET " + UPDATE_FIELD_LIST + " WHERE id = ?";

		try {
			dbConnection = getDataSource().getConnection();
			preparedStatement = dbConnection.prepareStatement(stmt);

			preparedStatement.setString(1, o.getObject());
			preparedStatement.setString(2, o.getKeyword());
			preparedStatement.setString(3, o.getOperationNr());
			preparedStatement.setString(4, o.getCity());
			preparedStatement.setString(5, o.getCrossway());
			preparedStatement.setString(6, o.getPriority());
			preparedStatement.setString(7, o.getBuzzword());
			preparedStatement.setString(8, o.getStreet());
			preparedStatement.setDouble(9, o.getPositionLatitude());
			preparedStatement.setDouble(10, o.getPositionLongitude());
			preparedStatement.setString(11, o.getResourcesRaw());
			preparedStatement.setDate(12, new java.sql.Date(o.getIncurredAt().getTime()));
			if (o.getDispatchedAt() != null)
				preparedStatement.setDate(13, new java.sql.Date(o.getDispatchedAt().getTime()));
			else
				preparedStatement.setDate(13, null);
			if (o.getTakenOverAt() != null)
				preparedStatement.setDate(14, new java.sql.Date(o.getTakenOverAt().getTime()));
			else
				preparedStatement.setDate(14, null);
			preparedStatement.setLong(15, o.getId());

			preparedStatement.executeUpdate();

			if (log.isDebugEnabled()) {
				log.debug(stmt);
				log.debug(o.toString());
			}
		}
		catch (final SQLException e) {
			log.error("Error while executing SQL Query: " + stmt + " - " + e.getMessage(), e);
			throw e;
		}
		finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}
	}

	/**
	 * Remove given entity by <code>id</code> from persistence context
	 * 
	 * @param id
	 * @throws Exception
	 */
	public void remove(long id) throws Exception {
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		final String stmt = "DELETE FROM of_operation WHERE id = ?";

		try {
			dbConnection = getDataSource().getConnection();
			preparedStatement = dbConnection.prepareStatement(stmt);
			preparedStatement.setLong(1, id);

			preparedStatement.executeUpdate();

			if (log.isDebugEnabled()) {
				log.debug(stmt);
				log.debug("ID: " + id);
			}
		}
		catch (final SQLException e) {
			log.error("Error while executing SQL Query: " + stmt + " - " + e.getMessage(), e);
			throw e;
		}
		finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}
	}

	private Operation getOperationFromResultSet(ResultSet rs) throws Exception {
		final Operation o = new Operation();

		o.setId(rs.getLong("id"));
		o.setObject(rs.getString("object"));
		o.setKeyword(rs.getString("keyword"));
		o.setOperationNr(rs.getString("operationNr"));
		o.setCity(rs.getString("city"));
		o.setCrossway(rs.getString("crossway"));
		o.setPriority(rs.getString("priority"));
		o.setBuzzword(rs.getString("buzzword"));
		o.setStreet(rs.getString("street"));
		o.setPositionLatitude(rs.getDouble("positionLatitude"));
		o.setPositionLongitude(rs.getDouble("positionLongitude"));
		o.setResourcesRaw(rs.getString("resourcesRaw"));
		o.setIncurredAt(rs.getDate("incurredAt"));
		o.setDispatchedAt(rs.getDate("dispatchedAt"));
		o.setTakenOverAt(rs.getDate("takenOverAt"));
		o.setResources(new ArrayList<>());

		return o;
	}

}
