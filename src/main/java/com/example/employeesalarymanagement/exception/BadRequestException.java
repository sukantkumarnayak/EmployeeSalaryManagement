package com.example.employeesalarymanagement.exception;

public class BadRequestException extends RuntimeException{
	public BadRequestException() {
	    super(" Bad input, ie. bad parameters");
	  }

	public BadRequestException(String msg) {
		super(msg);
	}

}
