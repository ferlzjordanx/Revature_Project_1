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
@WebServlet("/reimbursements/resolve")
public class ResolveReimbursementController extends AbstractController {

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
		String managerId = req.getParameter("manager-id");
		String status = req.getParameter("status");

		try (Connection conn = openConnection()) {
			reimbursementService.resolve(conn, Integer.parseInt(reimbursementId), Integer.parseInt(managerId),
					Integer.parseInt(status));
			
			List<Reimbursement> reimbursements = reimbursementService.findAll(conn);
			writeOk(reimbursements, resp);
			conn.commit();
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			writeError(ex, resp);
		}
	}
}
