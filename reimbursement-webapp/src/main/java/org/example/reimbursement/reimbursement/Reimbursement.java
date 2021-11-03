package org.example.reimbursement.reimbursement;

/**
 * Class that represents a reimbursement request.
 *
 * @author Jorge Ferreira.
 * @since 10/02/2021.
 */
public class Reimbursement {

	private Integer id;
	private Double amount;
	private ReimbursementStatus status;
	private Integer employeeId;
	private String employeeUsername;
	private Integer managerId;
	private String managerUsername;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public ReimbursementStatus getStatus() {
		return status;
	}

	public void setStatus(ReimbursementStatus status) {
		this.status = status;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public Integer getManagerId() {
		return managerId;
	}

	public void setManagerId(Integer managerId) {
		this.managerId = managerId;
	}

	public String getEmployeeUsername() {
		return employeeUsername;
	}

	public void setEmployeeUsername(String employeeUsername) {
		this.employeeUsername = employeeUsername;
	}

	public String getManagerUsername() {
		if (managerUsername == null) {
			return "-";
		}
		
		return managerUsername;
	}

	public void setManagerUsername(String managerUsername) {
		this.managerUsername = managerUsername;
	}

}
