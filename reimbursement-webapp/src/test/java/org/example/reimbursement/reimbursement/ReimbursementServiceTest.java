package org.example.reimbursement.reimbursement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.example.reimbursement.PostgreSQLFactory;
import org.junit.jupiter.api.Test;

class ReimbursementServiceTest {

	private final ReimbursementService reimbursementService = new ReimbursementService();

	@Test
	void insertAndFindById() throws SQLException {
		try (Connection conn = PostgreSQLFactory.open()) {
			Reimbursement reimbursement = new Reimbursement();
			reimbursement.setAmount(20.0);
			reimbursement.setEmployeeId(2);
			reimbursement.setStatus(ReimbursementStatus.PENDING);
			reimbursementService.insert(conn, reimbursement);

			reimbursement = reimbursementService.findById(conn, reimbursement.getId());
			assertEquals(20.0, reimbursement.getAmount());
			conn.rollback();
		}
	}

	@Test
	void updateAndFindAll() throws SQLException {
		try (Connection conn = PostgreSQLFactory.open()) {
			Reimbursement reimbursement = new Reimbursement();
			reimbursement.setAmount(20.0);
			reimbursement.setEmployeeId(2);
			reimbursement.setStatus(ReimbursementStatus.PENDING);
			reimbursementService.insert(conn, reimbursement);

			reimbursement.setEmployeeId(3);
			reimbursementService.update(conn, reimbursement);

			List<Reimbursement> reimbursements = reimbursementService.findAll(conn);
			assertTrue(reimbursements.size() > 1);

			conn.rollback();
		}
	}
}