package org.example.reimbursement.reimbursement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.reimbursement.common.Repository;

/**
 * Repository for persisting reimbursements.
 * 
 * @author Jorge Ferreira.
 * @since 10/04/2021.
 */
public class ReimbursementRepository implements Repository<Reimbursement, Integer> {

	private final Logger logger = LogManager.getLogger(getClass().getSimpleName());

	/**
	 * Generic search method.
	 * 
	 * @param conn       The database connection.
	 * @param id         Reimbursement ID.
	 * @param employeeId Reimbursement employee who created.
	 * @param managerId  Reimbursement manager who resolved.
	 * @return
	 */
	public List<Reimbursement> find(Connection conn, Integer id, Integer employeeId, Integer managerId) {
		List<Reimbursement> reimbursements = new ArrayList<>();

		StringBuilder sql = new StringBuilder();
		sql.append("select \n");
		sql.append("  r.id, \n");
		sql.append("  r.amount, \n");
		sql.append("  r.status, \n");
		sql.append("  r.employee_id, \n");
		sql.append("  e.username as employee_username, \n");
		sql.append("  m.username as manager_username, \n");
		sql.append("  r.manager_id \n");
		sql.append("from " + Repository.SCHEMA + "tb_reimbursement r \n");
		sql.append("left join " + Repository.SCHEMA + "tb_user e on e.id = r.employee_id \n");
		sql.append("left join " + Repository.SCHEMA + "tb_user m on m.id = r.manager_id \n");
		sql.append("where 1 = 1 \n");

		if (id != null) {
			sql.append("and r.id = ? \n");
		}

		if (employeeId != null) {
			sql.append("and r.employee_id = ? \n");
		}

		if (managerId != null) {
			sql.append("and r.manager_id = ? \n");
		}

		sql.append("order by r.id desc \n");
		logger.info(sql.toString());

		try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
			int count = 1;

			if (id != null) {
				stmt.setInt(count++, id);
			}

			if (employeeId != null) {
				stmt.setInt(count++, employeeId);
			}

			if (managerId != null) {
				stmt.setInt(count++, managerId);
			}

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Reimbursement user = new Reimbursement();
				user.setId(rs.getInt("id"));
				user.setAmount(rs.getDouble("amount"));
				user.setStatus(ReimbursementStatus.findById(rs.getInt("status")));
				user.setEmployeeId(rs.getInt("employee_id"));
				user.setManagerId(rs.getInt("manager_id"));
				user.setEmployeeUsername(rs.getString("employee_username"));
				user.setManagerUsername(rs.getString("manager_username"));
				reimbursements.add(user);
			}
		} catch (Exception ex) {
			throw new RuntimeException("Could not retrieve reimbursements: " + ex.getLocalizedMessage(), ex);
		}

		return reimbursements;
	}

	/**
	 * Insert an entity in the repository.
	 *
	 * @param conn The SQL connection.
	 * @param obj  The entity to persist.
	 * @return The entity ID.
	 */
	@Override
	public Integer insert(Connection conn, Reimbursement reimbursement) {
		StringBuilder sql = new StringBuilder();
		
		sql.append(
				"insert into " + Repository.SCHEMA + "tb_reimbursement (amount, status, employee_id, manager_id) \n");
		
		sql.append("values (?, ?, ?, ?) \n");
		logger.info(sql.toString());

		try (PreparedStatement stmt = conn.prepareStatement(sql.toString(), PreparedStatement.RETURN_GENERATED_KEYS)) {
			int count = 1;
			stmt.setDouble(count++, reimbursement.getAmount());
			stmt.setInt(count++, reimbursement.getStatus().getId());
			stmt.setInt(count++, reimbursement.getEmployeeId());

			if (reimbursement.getManagerId() != null) {
				stmt.setInt(count++, reimbursement.getManagerId());
			} else {
				stmt.setNull(count++, Types.INTEGER);
			}

			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();

			if (rs.next()) {
				reimbursement.setId(rs.getInt(1));
			}
		} catch (Exception ex) {
			throw new RuntimeException("Could not insert reimbursement: " + ex.getLocalizedMessage(), ex);
		}

		return reimbursement.getId();
	}

	/**
	 * Find an entity by its ID.
	 *
	 * @param conn The SQL connection.
	 * @param id   The entity ID.
	 * @return The persisted entity.
	 */
	@Override
	public Reimbursement findById(Connection conn, Integer id) {
		List<Reimbursement> reimbursements = find(conn, id, null, null);
		return reimbursements.isEmpty() ? null : reimbursements.get(0);
	}

	/**
	 * Update an entity in the repository.
	 *
	 * @param conn The SQL connection.
	 * @param obj  The entity to persist.
	 * @return The entity ID.
	 */
	@Override
	public void update(Connection conn, Reimbursement reimbursement) {
		StringBuilder sql = new StringBuilder();

		sql.append("update " + Repository.SCHEMA
				+ "tb_reimbursement set amount = ?, status = ?, employee_id = ?, manager_id = ? where id = ? \n");

		logger.info(sql.toString());

		try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
			int count = 1;
			stmt.setDouble(count++, reimbursement.getAmount());
			stmt.setInt(count++, reimbursement.getStatus().getId());
			stmt.setInt(count++, reimbursement.getEmployeeId());

			if (reimbursement.getManagerId() != null) {
				stmt.setInt(count++, reimbursement.getManagerId());
			} else {
				stmt.setNull(count++, Types.INTEGER);
			}

			stmt.setInt(count++, reimbursement.getId());
			stmt.executeUpdate();
		} catch (Exception ex) {
			throw new RuntimeException("Could not update reimbursement: " + ex.getLocalizedMessage(), ex);
		}
	}

	/**
	 * Find all entities.
	 *
	 * @param conn The SQL connection.
	 * @return All persisted entities.
	 */
	@Override
	public List<Reimbursement> findAll(Connection conn) {
		return find(conn, null, null, null);
	}
}
