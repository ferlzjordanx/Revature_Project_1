package org.example.reimbursement.user;

import java.io.IOException;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.example.reimbursement.common.AbstractController;

/**
 * Login REST controller.
 *
 * @author Jorge Ferreira.
 * @since 10/02/2021.
 */
@WebServlet("/login")
public class LoginController extends AbstractController {

	private static final long serialVersionUID = 1L;

	private final UserService userService = new UserService();
	private final Logger logger = Logger.getLogger(getClass().getSimpleName());

	/**
	 * Process POST requests of login.
	 *
	 * @param req Request parameters.
	 * @param resp Response objects.
	 * @throws ServletException Error while processing.
	 * @throws IOException Error while processing.
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User incoming = getObjectMapper().readValue(req.getInputStream(), User.class);

		try (Connection conn = openConnection()) {
			User user = userService.findByUsername(conn, incoming.getUsername());

			if (user != null && user.getPassword().equals(incoming.getPassword())) {
				String json = getObjectMapper().writeValueAsString(user);
				resp.setStatus(200);
				resp.setContentType("application/json");
				resp.getWriter().write(json);
			} else {
				throw new RuntimeException("Invalid username and/or password.");
			}
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			writeError(ex, resp);
		}
	}
}
