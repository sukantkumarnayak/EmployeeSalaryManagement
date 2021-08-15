package com.example.employeesalarymanagement.exception;

public class EmployeeSalaryManagementException  extends Exception{

	private static final long serialVersionUID = 1L;

	public EmployeeSalaryManagementException(Throwable throwable) {
		super(throwable);
	}

	public EmployeeSalaryManagementException(String msg) {
		super(msg);
	}

	public EmployeeSalaryManagementException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

}
