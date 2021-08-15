package com.example.employeesalarymanagement.service;

import java.util.List;
import java.util.Optional;

import com.example.employeesalarymanagement.dto.EmployeeDetailsDto;
import com.example.employeesalarymanagement.exception.ValidationException;


public interface EmployeeSalaryManagementService {
	public void saveEmployeeData(List<EmployeeDetailsDto> employeeData) throws ValidationException;
	public void createEmployee(EmployeeDetailsDto employeeData);
	public void updateEmployee(String id, EmployeeDetailsDto employeeData);
	public EmployeeDetailsDto getEmployee(String id);
	public void deleteEmployee(String id);
	public List<EmployeeDetailsDto> getAllEmployees(Optional<Double> minSalary,Optional<Double> maxSalary,Optional<Integer> offset,Optional<Integer> limit) ;
}
