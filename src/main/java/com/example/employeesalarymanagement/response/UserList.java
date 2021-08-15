package com.example.employeesalarymanagement.response;

import java.util.List;

import com.example.employeesalarymanagement.dto.EmployeeDetailsDto;

public class UserList {
private List<EmployeeDetailsDto> results;

public List<EmployeeDetailsDto> getResults() {
	return results;
}

public void setResults(List<EmployeeDetailsDto> results) {
	this.results = results;
}

}
