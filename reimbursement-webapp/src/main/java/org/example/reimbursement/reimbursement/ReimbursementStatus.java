package org.example.reimbursement.reimbursement;

/**
 * Reimbursement types.
 * 
 * @author Jorge Ferreira.
 * @since 10/04/2021.
 */
public enum ReimbursementStatus {

	PENDING(1, "Pending"), RESOLVED(2, "Resolved"), DENIED(3, "Denied");

	/**
	 * Find type by id.
	 * 
	 * @param id The type id.
	 * @return The type by id.
	 */
	public static ReimbursementStatus findById(Integer id) {
		switch (id) {
		case 1:
			return PENDING;
		case 2:
			return RESOLVED;
		case 3:
			return DENIED;
		default:
			return null;
		}
	}

	private Integer id;
	private String name;

	/**
	 * Contructor.
	 * 
	 * @param id The type id.
	 * @param name The type name.
	 */
	private ReimbursementStatus(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
