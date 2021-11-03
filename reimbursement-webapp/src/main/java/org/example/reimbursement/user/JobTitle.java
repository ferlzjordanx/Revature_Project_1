package org.example.reimbursement.user;

/**
 * Types o users.
 * 
 * @author Jorge Ferreira.
 * @since 10/04/2021.
 */
public enum JobTitle {

	EMPLOYEE(1, "Employee"), MANAGER(2, "Manager");
	
	/**
	 * Find type by id.
	 * 
	 * @param id The type id.
	 * @return The type by id.
	 */
	public static JobTitle findById(Integer id) {
		switch (id) {
		case 1:
			return EMPLOYEE;
		case 2:
			return MANAGER;
		default:
			return null;
		}
	}

	private Integer id;
	private String name;

	/**
	 * Default constructor.
	 * 
	 * @param id Type id.
	 * @param name Type name.
	 */
	private JobTitle(Integer id, String name) {
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
