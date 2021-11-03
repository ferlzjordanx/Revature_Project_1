package org.example.reimbursement.common;

import java.sql.Connection;
import java.util.List;

/**
 * Repository basic methods.
 *
 * @param <T> Entity type.
 * @param <ID> Entity ID type.
 */
public interface Repository<T, ID> {
	
	static final String SCHEMA = "reimbursement.";

	/**
	 * Insert an entity in the repository.
	 *
	 * @param conn The SQL connection.
	 * @param obj The entity to persist.
	 * @return The entity ID.
	 */
	ID insert(Connection conn, T obj);

	/**
	 * Find an entity by its ID.
	 *
	 * @param conn The SQL connection.
	 * @param id The entity ID.
	 * @return The persisted entity.
	 */
	T findById(Connection conn, ID id);

	/**
	 * Update an entity in the repository.
	 *
	 * @param conn The SQL connection.
	 * @param obj The entity to persist.
	 * @return The entity ID.
	 */
	void update(Connection conn, T obj);

	/**
	 * Find all entities.
	 *
	 * @param conn The SQL connection.
	 * @return All persisted entities.
	 */
	List<T> findAll(Connection conn);
}
