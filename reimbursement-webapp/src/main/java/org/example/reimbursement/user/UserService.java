package org.example.reimbursement.user;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * User service layer.
 * 
 * @author Jorge Ferreira.
 * @since 10/04/2021.
 */
public class UserService {

	private final UserRepository userRepository = new UserRepository();

	/**
	 * Find an entity by its ID.
	 *
	 * @param conn The SQL connection.
	 * @param id   The entity ID.
	 * @return The persisted entity.
	 */
	public User findById(Connection conn, Integer id) {
		return userRepository.findById(conn, id);
	}

	/**
	 * Insert an entity in the repository.
	 *
	 * @param conn The SQL connection.
	 * @param obj  The entity to persist.
	 * @return The entity ID.
	 */
	public Integer insert(Connection conn, User user) {
		return userRepository.insert(conn, user);
	}

	/**
	 * Update an entity in the repository.
	 *
	 * @param conn The SQL connection.
	 * @param obj  The entity to persist.
	 * @return The entity ID.
	 */
	public void update(Connection conn, User user) {
		userRepository.update(conn, user);
	}

	/**
	 * Find all entities.
	 *
	 * @param conn The SQL connection.
	 * @return All persisted entities.
	 */
	public List<User> findAll(Connection conn) {
		return userRepository.findAll(conn);
	}

	public User findByUsername(Connection conn, String username) {
		List<User> users = userRepository.find(conn, null, username);
		return users.isEmpty() ? null : users.get(0);
	}

	/**
	 * Reset user password with default value.
	 * 
	 * @param conn   The SQL connection.
	 * @param userId The user ID.
	 * @return The user changed.
	 */
	public User resetPassword(Connection conn, int userId) {
		User user = findById(conn, userId);
		user.setPassword("1234");
		update(conn, user);
		sendEmail(user);
		return user;
	}

	private void sendEmail(User user) {
		// Application account.
		String emailUsername = "ferlzjordan@gmail.com";
		String emailPassword = "neglpewiqgmdrpph";

		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true");

		Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(emailUsername, emailPassword);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("hlalvesbr@gmail.com"));

			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(emailUsername + "," + user.getEmail()));

			message.setSubject("Password reset");
			message.setText("Your password was reset to the default value '1234'");
			Transport.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
