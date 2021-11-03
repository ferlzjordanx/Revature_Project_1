package org.example.reimbursement.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.reimbursement.common.Repository;

public class UserRepository implements Repository<User, Integer> {

	private final Logger logger = LogManager.getLogger(getClass().getSimpleName());

	public List<User> find(Connection conn, Integer id, String username) {
		List<User> users = new ArrayList<User>();

		StringBuilder sql = new StringBuilder();
		sql.append("select \n");
		sql.append("  id, \n");
		sql.append("  username, \n");
		sql.append("  password, \n");
		sql.append("  name, \n");
		sql.append("  email, \n");
		sql.append("  job_title \n");
		sql.append("from " + Repository.SCHEMA + "tb_user u \n");
		sql.append("where 1 = 1 \n");

		if (id != null) {
			sql.append("and u.id = ? \n");
		}

		if (username != null) {
			sql.append("and u.username = ? \n");
		}

		sql.append("order by u.name \n");
		logger.info(sql.toString());

		try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
			int count = 1;

			if (id != null) {
				stmt.setInt(count++, id);
			}

			if (username != null) {
				stmt.setString(count++, username);
			}

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setName(rs.getString("name"));
				user.setEmail(rs.getString("email"));
				user.setJobTitle(JobTitle.findById(rs.getInt("job_title")));
				users.add(user);
			}
		} catch (Exception ex) {
			throw new RuntimeException("Could not retrieve users: " + ex.getLocalizedMessage(), ex);
		}

		return users;
	}

	/**
	 * Insert an entity in the repository.
	 *
	 * @param conn The SQL connection.
	 * @param obj  The entity to persist.
	 * @return The entity ID.
	 */
	@Override
	public Integer insert(Connection conn, User user) {
		StringBuilder sql = new StringBuilder();
		sql.append("insert into " + Repository.SCHEMA + "tb_user (username, password, name, email, job_title) \n");
		sql.append("values (?, ?, ?, ?, ?) \n");
		logger.info(sql.toString());

		try (PreparedStatement stmt = conn.prepareStatement(sql.toString(), PreparedStatement.RETURN_GENERATED_KEYS)) {
			int count = 1;
			stmt.setString(count++, user.getUsername());
			stmt.setString(count++, user.getPassword());
			stmt.setString(count++, user.getName());
			stmt.setString(count++, user.getEmail());
			stmt.setInt(count++, user.getJobTitle().getId());

			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();

			if (rs.next()) {
				user.setId(rs.getInt(1));
			}
		} catch (Exception ex) {
			throw new RuntimeException("Could not insert user: " + ex.getLocalizedMessage(), ex);
		}

		return user.getId();
	}

	/**
	 * Find an entity by its ID.
	 *
	 * @param conn The SQL connection.
	 * @param id   The entity ID.
	 * @return The persisted entity.
	 */
	@Override
	public User findById(Connection conn, Integer id) {
		List<User> users = find(conn, id, null);
		return users.isEmpty() ? null : users.get(0);
	}

	/**
	 * Update an entity in the repository.
	 *
	 * @param conn The SQL connection.
	 * @param obj  The entity to persist.
	 * @return The entity ID.
	 */
	@Override
	public void update(Connection conn, User user) {
		StringBuilder sql = new StringBuilder();

		sql.append("update " + Repository.SCHEMA
				+ "tb_user set password = ?, name = ?, email = ?, job_title = ? where id = ? \n");

		logger.info(sql.toString());

		try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
			int count = 1;
			stmt.setString(count++, user.getPassword());
			stmt.setString(count++, user.getName());
			stmt.setString(count++, user.getEmail());
			stmt.setInt(count++, user.getJobTitle().getId());

			stmt.setInt(count++, user.getId());
			stmt.executeUpdate();
		} catch (Exception ex) {
			throw new RuntimeException("Could not update user: " + ex.getLocalizedMessage(), ex);
		}
	}

	/**
	 * Find all entities.
	 *
	 * @param conn The SQL connection.
	 * @return All persisted entities.
	 */
	@Override
	public List<User> findAll(Connection conn) {
		return find(conn, null, null);
	}
}
