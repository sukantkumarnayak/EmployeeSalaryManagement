package com.example.employeesalarymanagement.dto;

public class EmployeeDetailsDto {

	private String id;
	private String login;
	private String name;
	private Double salary;
	private String startDate;

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {
		return "EmployeeDetailsDto [id=" + id + ", login=" + login + ", name=" + name + ", salary=" + salary
				+ ", startDate=" + startDate + "]";
	}

}
