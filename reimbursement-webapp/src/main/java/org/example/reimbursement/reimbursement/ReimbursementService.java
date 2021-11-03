package org.example.reimbursement.reimbursement;

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

import org.example.reimbursement.user.User;
import org.example.reimbursement.user.UserService;

/**
 * Service layer for reimbursements.
 * 
 * @author Jorge Ferreira.
 * @since 10/04/2021.
 */
public class ReimbursementService {

	private final ReimbursementRepository reimbursementRepository = new ReimbursementRepository();
	private final UserService userService = new UserService();

	/**
	 * Insert an entity in the repository.
	 *
	 * @param conn The SQL connection.
	 * @param obj  The entity to persist.
	 * @return The entity ID.
	 */
	public Integer insert(Connection conn, Reimbursement reimbursement) {
		return reimbursementRepository.insert(conn, reimbursement);
	}

	/**
	 * Find an entity by its ID.
	 *
	 * @param conn The SQL connection.
	 * @param id   The entity ID.
	 * @return The persisted entity.
	 */
	public Reimbursement findById(Connection conn, Integer id) {
		return reimbursementRepository.findById(conn, id);
	}

	/**
	 * Update an entity in the repository.
	 *
	 * @param conn The SQL connection.
	 * @param obj  The entity to persist.
	 * @return The entity ID.
	 */
	public void update(Connection conn, Reimbursement reimbursement) {
		reimbursementRepository.update(conn, reimbursement);
	}

	/**
	 * Find all entities.
	 *
	 * @param conn The SQL connection.
	 * @return All persisted entities.
	 */
	public List<Reimbursement> findAll(Connection conn) {
		return reimbursementRepository.findAll(conn);
	}

	/**
	 * Find all entities by employee.
	 *
	 * @param conn The SQL connection.
	 * @return All persisted entities.
	 */
	public List<Reimbursement> findByEmployee(Connection conn, int employeeId) {
		return reimbursementRepository.find(conn, null, employeeId, null);
	}

	/**
	 * Resolve a reimbursement.
	 * 
	 * @param conn      The SQL connection.
	 * @param id        The entity ID.
	 * @param managerId The manager ID.
	 */
	public void resolve(Connection conn, int id, int managerId, int status) {
		Reimbursement reimbursement = findById(conn, id);
		reimbursement.setStatus(ReimbursementStatus.findById(status));
		reimbursement.setManagerId(managerId);
		update(conn, reimbursement);

		User employee = userService.findById(conn, reimbursement.getEmployeeId());
		User manager = userService.findById(conn, reimbursement.getManagerId());
		sendEmail(reimbursement, employee, manager);
	}

	private void sendEmail(Reimbursement reimbursement, User employee, User manager) {
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
					InternetAddress.parse(emailUsername + "," + employee.getEmail()));

			message.setSubject("Reimbursement " + reimbursement.getStatus().getName());
			
			message.setText("Reimbursement #" + reimbursement.getId() + " was " + reimbursement.getStatus().getName()
					+ " by " + manager.getName());

			Transport.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
