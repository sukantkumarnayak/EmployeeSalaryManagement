package com.example.employeesalarymanagement.controller;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.employeesalarymanagement.dao.EmployeeDetailsDAO;
import com.example.employeesalarymanagement.dto.EmployeeDetailsDto;
import com.example.employeesalarymanagement.exception.BadRequestException;
import com.example.employeesalarymanagement.exception.EmployeeSalaryManagementException;
import com.example.employeesalarymanagement.exception.ValidationException;
import com.example.employeesalarymanagement.reader.DataReaderFactory;
import com.example.employeesalarymanagement.response.UserList;
import com.example.employeesalarymanagement.service.EmployeeSalaryManagementService;

@RestController
public class EmployeeSalaryManagementController {

	Logger logger = LogManager.getLogger(EmployeeSalaryManagementController.class);
	@Autowired
	EmployeeDetailsDAO dao1;
	@Autowired
	EmployeeSalaryManagementService service;
	
	@Autowired
	DataReaderFactory factory;
	
	/**
	 * @param file
	 * @return
	 * @throws EmployeeSalaryManagementException
	 */
	@PostMapping("/users/upload")
	public ResponseEntity<?> uploadUsers(@RequestPart(required = true) MultipartFile file) throws EmployeeSalaryManagementException {

		 List<EmployeeDetailsDto> details=	factory.createDataReader(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1)).readData(file);
		if(details.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body("Success but no data updated.");
		}

		try {
			service.saveEmployeeData(details);
		} catch (ValidationException e){
			throw new BadRequestException(String.join(",", e.getValidationResult()));
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body("Data created or uploaded.");
		 
		
	}

	/**
	 * @param e
	 * @throws EmployeeSalaryManagementException
	 */
	@PostMapping("/users")
	@ResponseStatus(code = HttpStatus.CREATED, reason = "Successfully created")
	public void createNewUser(@RequestBody EmployeeDetailsDto e) throws EmployeeSalaryManagementException {
		service.createEmployee(e);
	}

	/**
	 * @param id
	 */
	@DeleteMapping("/users/{id}")
	@ResponseBody
	@ResponseStatus(code = HttpStatus.OK, reason = "Successfully deleted")
	public void deleteEmployee(@PathVariable String id) {
		service.deleteEmployee(id);
	}
	
	/**
	 * @param id
	 * @param e
	 * @throws EmployeeSalaryManagementException
	 */
	@PutMapping("/users/{id}")
	@ResponseStatus(code = HttpStatus.OK, reason = "Successfully updated")
	public void updateUser(@PathVariable String id,@RequestBody EmployeeDetailsDto e) throws EmployeeSalaryManagementException{
		service.updateEmployee(id,e);
	}

	/**
	 * @param minSalary
	 * @param maxSalary
	 * @param offset
	 * @param limit
	 * @return
	 */
	@GetMapping("/users")
	@ResponseBody
	UserList getAllUsers(@RequestParam(defaultValue = "0", required = false) Optional<Double> minSalary,
			@RequestParam(defaultValue = "4000.00", required = false) Optional<Double> maxSalary,
			@RequestParam(defaultValue = "0", required = false) Optional<Integer> offset,
			@RequestParam(defaultValue = "0", required = false) Optional<Integer> limit) {
		UserList s=new UserList();
		s.setResults(service.getAllEmployees(minSalary, maxSalary, offset, limit));
		return s;
	}

	/**
	 * @param id
	 * @return
	 */
	@GetMapping("/users/{id}")
	EmployeeDetailsDto getEmployee(@PathVariable String id) {

		return service.getEmployee(id);
	}
}
