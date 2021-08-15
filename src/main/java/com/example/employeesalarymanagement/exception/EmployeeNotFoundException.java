package com.example.employeesalarymanagement.exception;

public class EmployeeNotFoundException extends RuntimeException {
	public EmployeeNotFoundException(Long id) {
		super("Could not find user " + id);
	}

	public EmployeeNotFoundException(String message) {
		super(message);
	}
}
