package com.example.employeesalarymanagement.reader;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.employeesalarymanagement.dto.EmployeeDetailsDto;
import com.example.employeesalarymanagement.exception.EmployeeSalaryManagementException;

public interface DataReader {
	List<EmployeeDetailsDto> readData(MultipartFile file1) throws EmployeeSalaryManagementException;
}
