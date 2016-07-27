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
import de.openflorian.data.model.OperationResource;

/**
 * {@link OperationResourceDao}
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class OperationResourceDao extends DatabaseConnector {

	private static final Logger log = LoggerFactory.getLogger(OperationDao.class);

	private static final String INSERT_FIELD_LIST = "callName, crew, description, licensePlate, type";
	private static final String UPDATE_FIELD_LIST = "callName = ?, crew = ?, description = ?, licensePlate = ?, type = ?";
	private static final String FIELD_LIST = "id, " + INSERT_FIELD_LIST;

	/**
	 * Get the amount of all available {@link OperationResource} in persistence context.
	 * 
	 * @return {@link long}
	 */
	public long count() throws Exception {
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		final String stmt = "SELECT count(id) AS oc FROM of_operation_resource";

		try {
			dbConnection = getDataSource().getConnection();
			preparedStatement = dbConnection.prepareStatement(stmt);

			// execute select SQL stetement
			final ResultSet rs = preparedStatement.executeQuery();

			if (log.isDebugEnabled())
				log.debug(rs.getStatement().toString());

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
	 * Get an {@link OperationResource} by <code>callname</code>
	 *
	 * @param callname
	 * @return {@link OperationResource} or null
	 * @throws Exception
	 */
	public OperationResource getResourceByCallname(String callname) throws Exception {
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		final String stmt = "SELECT " + FIELD_LIST + " FROM of_operation_resource WHERE callName = ?";

		try {
			dbConnection = getDataSource().getConnection();
			preparedStatement = dbConnection.prepareStatement(stmt);
			preparedStatement.setString(1, callname);

			// execute select SQL stetement
			final ResultSet rs = preparedStatement.executeQuery();

			if (log.isDebugEnabled())
				log.debug(rs.getStatement().toString());

			if (rs.next()) {
				return getFromResultSet(rs);
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
	 * Gets all registered {@link OperationResource}s
	 * 
	 * @return {@link List}<{@link OperationResource}>
	 * @throws Exception
	 */
	public List<OperationResource> getAll() throws Exception {
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		final String stmt = "SELECT " + FIELD_LIST + " FROM of_operation_resource ORDER BY callName ASC";

		try {
			dbConnection = getDataSource().getConnection();
			preparedStatement = dbConnection.prepareStatement(stmt);

			// execute select SQL stetement
			final ResultSet rs = preparedStatement.executeQuery();

			if (log.isDebugEnabled())
				log.debug(rs.getStatement().toString());

			final List<OperationResource> resList = new ArrayList<>();
			while (rs.next()) {
				resList.add(getFromResultSet(rs));
			}
			return resList;
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
	 * Gets all {@link OperationResource}s from given operation <code>id</code>
	 * 
	 * @param id
	 * @return {@link List}<{@link OperationResource}>
	 * @throws Exception
	 */
	public List<OperationResource> getByOperationId(Long id) throws Exception {
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		final String stmt = "SELECT " + FIELD_LIST + " FROM of_operation_resource AS res "
				+ "JOIN of_operation_of_operation_resource AS op_res ON res.id = op_res.operation_resource_id WHERE operation_id = ?";

		try {
			dbConnection = getDataSource().getConnection();
			preparedStatement = dbConnection.prepareStatement(stmt);
			preparedStatement.setLong(1, id);

			// execute select SQL stetement
			final ResultSet rs = preparedStatement.executeQuery();

			if (log.isDebugEnabled())
				log.debug(rs.getStatement().toString());

			final List<OperationResource> resList = new ArrayList<>();
			while (rs.next()) {
				resList.add(getFromResultSet(rs));
			}
			return resList;
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
	 * Persist given <code>o</code> {@link OperationResource} to persistence context
	 * 
	 * @param object
	 * @return persisted <code>object</code>
	 */
	public OperationResource insert(OperationResource o) throws Exception {
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		final String stmt = "INSERT INTO of_operation_resource (" + INSERT_FIELD_LIST + ") VALUES(?,?,?,?,?)";

		try {
			dbConnection = getDataSource().getConnection();
			preparedStatement = dbConnection.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);

			// callName = ?, crew = ?, description = ?, licensePlate = ?, type = ?
			preparedStatement.setString(1, o.getCallName());
			preparedStatement.setString(2, o.getCrew());
			preparedStatement.setString(3, o.getDescription());
			preparedStatement.setString(4, o.getLicensePlate());
			preparedStatement.setString(5, o.getType());

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
	 * Update given <code>o</code> {@link OperationResource} to persistence context
	 * 
	 * @param object
	 * @return persisted <code>object</code>
	 */
	public void update(OperationResource o) throws Exception {
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		final String stmt = "UPDATE of_operation_resource SET " + UPDATE_FIELD_LIST + " WHERE id = ?";

		try {
			dbConnection = getDataSource().getConnection();
			preparedStatement = dbConnection.prepareStatement(stmt);

			// callName = ?, crew = ?, description = ?, licensePlate = ?, type = ?
			preparedStatement.setString(1, o.getCallName());
			preparedStatement.setString(2, o.getCrew());
			preparedStatement.setString(3, o.getDescription());
			preparedStatement.setString(4, o.getLicensePlate());
			preparedStatement.setString(5, o.getType());
			preparedStatement.setLong(6, o.getId());

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
		Statement statement = null;

		final String stmtDelRelationship = "DELETE FROM of_operation_of_operation_resource WHERE operation_resource_id = "
				+ id;
		final String stmtDel = "DELETE FROM of_operation_resource WHERE id = " + id;

		try {
			dbConnection = getDataSource().getConnection();
			statement = dbConnection.createStatement();
			statement.addBatch(stmtDelRelationship);
			statement.addBatch(stmtDel);

			statement.executeBatch();

			if (log.isDebugEnabled()) {
				log.debug(stmtDelRelationship);
				log.debug(stmtDel);
			}
		}
		catch (final SQLException e) {
			log.error("Error while executing SQL Query: " + stmtDel + " ; " + stmtDelRelationship + " - " + e.getMessage(),
					e);
			throw e;
		}
		finally {

			if (statement != null) {
				statement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}
	}

	/**
	 * Helper: Transform operation resource in given <code>rs</code> to an {@link OperationResource}
	 * 
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	private OperationResource getFromResultSet(ResultSet rs) throws Exception {
		final OperationResource r = new OperationResource();

		r.setId(rs.getLong("id"));
		r.setCallName(rs.getString("callName"));
		r.setCrew(rs.getString("crew"));
		r.setDescription(rs.getString("description"));
		r.setLicensePlate(rs.getString("licensePlate"));
		r.setType(rs.getString("type"));

		return r;
	}

}
