package org.example.reimbursement.user;

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
 * Users controller.
 * 
 * @author Jorge Ferreira.
 * @since 10/04/2021.
 */
@WebServlet("/users")
public class UserController extends AbstractController {

	private static final long serialVersionUID = 1L;

	private UserService userService = new UserService();
	private final Logger logger = LogManager.getLogger(getClass().getSimpleName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String userId = req.getParameter("id");

		try (Connection conn = openConnection()) {
			if (userId == null) {
				List<User> users = userService.findAll(conn);
				writeOk(users, resp);
			} else {
				User user = userService.findById(conn, Integer.parseInt(userId));
				writeOk(user, resp);
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			writeError(ex, resp);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = getObjectMapper().readValue(req.getInputStream(), User.class);
		
		try (Connection conn = openConnection()) {
			if (user.getId() == null) {
				userService.insert(conn, user);
			} else {
				userService.update(conn, user);
			}
			
			writeOk(user, resp);
			conn.commit();
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			writeError(ex, resp);
		}
	}
}
