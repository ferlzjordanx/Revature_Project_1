package org.example.reimbursement.reimbursement;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.reimbursement.common.AbstractController;

/**
 * The reimbursement REST controller.
 * 
 * @author Jorge Ferreira.
 * @since 10/02/2021.
 */
@WebServlet("/reimbursements")
public class ReimbursementController extends AbstractController {

	private static final long serialVersionUID = 1L;

	private ReimbursementService reimbursementService = new ReimbursementService();
	private final Logger logger = LogManager.getLogger(getClass().getSimpleName());

	/**
	 * Process GET requests of reimbursements.
	 *
	 * @param req  Request parameters.
	 * @param resp Response objects.
	 * @throws ServletException Error while processing.
	 * @throws IOException      Error while processing.
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String reimbursementId = req.getParameter("id");
		String employeeId = req.getParameter("employee-id");

		try (Connection conn = openConnection()) {
			if (reimbursementId != null) {
				Reimbursement reimbursement = reimbursementService.findById(conn, Integer.parseInt(reimbursementId));
				writeOk(reimbursement, resp);
			} else if (employeeId != null) {
				List<Reimbursement> reimbursements = reimbursementService.findByEmployee(conn,
						Integer.parseInt(employeeId));

				writeOk(reimbursements, resp);
			} else {
				List<Reimbursement> reimbursements = reimbursementService.findAll(conn);
				writeOk(reimbursements, resp);
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			writeError(ex, resp);
		}
	}

	/**
	 * Process POST requests of reimbursements.
	 *
	 * @param req  Request parameters.
	 * @param resp Response objects.
	 * @throws ServletException Error while processing.
	 * @throws IOException      Error while processing.
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Reimbursement reimbursement = getObjectMapper().readValue(req.getInputStream(), Reimbursement.class);

		try (Connection conn = openConnection()) {
			if (reimbursement.getId() == null) {
				reimbursementService.insert(conn, reimbursement);
			} else {
				reimbursementService.update(conn, reimbursement);
			}

			writeOk(reimbursement, resp);
			conn.commit();
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			writeError(ex, resp);
		}
	}
}
