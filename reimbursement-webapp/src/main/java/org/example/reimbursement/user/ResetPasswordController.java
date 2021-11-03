package org.example.reimbursement.user;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.reimbursement.common.AbstractController;

/**
 * Controller for reseting users passwords.
 * 
 * @author Jorge Ferreira.
 * @since 10/08/2021.
 */
@WebServlet("/users/reset-password")
public class ResetPasswordController extends AbstractController {

	private static final long serialVersionUID = 1L;

	private UserService userService = new UserService();
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
		String userId = req.getParameter("id");

		try (Connection conn = openConnection()) {
			User user = userService.resetPassword(conn, Integer.parseInt(userId));
			writeOk(user, resp);
			conn.commit();
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			writeError(ex, resp);
		}
	}
}
