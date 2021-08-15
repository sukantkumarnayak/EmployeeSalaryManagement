package com.example.employeesalarymanagement.exception;

import java.util.List;

public class ValidationException extends Exception {

	private List<String> validationResult;

	public List<String> getValidationResult() {
		return validationResult;
	}

	public ValidationException(List<String> validationResult) {
		super();
		this.validationResult = validationResult;
	}
	
}
