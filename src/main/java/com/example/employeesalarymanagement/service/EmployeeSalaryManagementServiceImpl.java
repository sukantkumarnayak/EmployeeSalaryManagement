package com.example.employeesalarymanagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.employeesalarymanagement.dao.EmployeeDetailsDAO;
import com.example.employeesalarymanagement.dto.EmployeeDetailsDto;
import com.example.employeesalarymanagement.entity.EmployeeDetails;
import com.example.employeesalarymanagement.exception.BadRequestException;
import com.example.employeesalarymanagement.exception.EmployeeNotFoundException;
import com.example.employeesalarymanagement.exception.ValidationException;
import com.example.employeesalarymanagement.utils.DateTimeUtils;


@Service
public class EmployeeSalaryManagementServiceImpl implements EmployeeSalaryManagementService {

	Logger logger = LogManager.getLogger(EmployeeSalaryManagementServiceImpl.class);

	@Autowired
	EmployeeDetailsDAO dao;

	/**
	 * accepts a list of employee data ,validates the contents and saves in DB
	 */
	public void saveEmployeeData(List<EmployeeDetailsDto> employeeData) throws ValidationException {
		logger.info("add record " + employeeData);

		validateEmployeeDetails(employeeData);
		employeeData.forEach(x -> {
			Optional<EmployeeDetails> o = dao.findById(x.getId());
			if (o.isPresent()) {
				EmployeeDetails eDetails = o.get();
				eDetails.setLogin(x.getLogin());
				eDetails.setName(x.getName());
				eDetails.setSalary(x.getSalary());
				eDetails.setStartDate(DateTimeUtils.parse(x.getStartDate()));
				if(!x.getLogin().equals(eDetails.getLogin()))
				if (dao.isLoginExists(eDetails.getLogin()).isPresent()) {
					logger.error("Entered employee login already exists");
					throw new BadRequestException("Employee login not unique");

				}
				dao.save(eDetails);
			} else {
				
				EmployeeDetails e =EmployeeSalaryManagementServiceImpl.toEntity(x);
				if (dao.findById(e.getId()).isPresent()) {
					throw new BadRequestException("Employee ID already exists");

				}

				if (dao.isLoginExists(e.getLogin()).isPresent()) {
					logger.error("Entered employee login already exists");
					throw new BadRequestException("Employee login not unique");

				}
				dao.save(e);
			}
		});
	}

	
	/**
	 * @param employeeDetails
	 * @throws ValidationException
	 * 
	 */
	private void validateEmployeeDetails(List<EmployeeDetailsDto> employeeDetails) throws ValidationException {
		List<String> validationResult = new ArrayList<>();
		for (EmployeeDetailsDto employeeDetail : employeeDetails) {
			try {
				validate(employeeDetail);
			} catch (BadRequestException e){
				validationResult.add(employeeDetail.getId()+": "+ e.getMessage());
			}

		}
		if (!validationResult.isEmpty()) {
			throw new ValidationException(validationResult);
		}
	}

	/**
	 *used for creating new employee, if the employee is not present and if the login is unique 
	 *and if all the details are valid then 
	 *the record will  be inserted in DB else the respective error message will be shown
	 */
	public void createEmployee(EmployeeDetailsDto employeeData) throws BadRequestException {
		logger.info("add record " + employeeData);
		if (dao.findById(employeeData.getId()).isPresent()) {
			logger.error("Entered employee Employee ID already exists");
			throw new BadRequestException("Employee ID already exists");

		}

		if (dao.isLoginExists(employeeData.getLogin()).isPresent()) {
			logger.error("Entered employee login already exists");
			throw new BadRequestException("Employee login not unique");

		}

		validate(employeeData);
		dao.save(EmployeeSalaryManagementServiceImpl.toEntity(employeeData));
	}

	/**
	 * @param employeeDetail
	 * validated name,login,salary,startdate
	 */
	private void validate(EmployeeDetailsDto employeeDetail) {
		if (StringUtils.isEmpty(employeeDetail.getName())) {
			logger.error("Record is invalid because of missing Name " + employeeDetail.getName());
			throw new BadRequestException("Invalid Name");
		}
		if (StringUtils.isEmpty(employeeDetail.getLogin())) {
			logger.error("Record is invalid because of missing login " + employeeDetail.getLogin());
			throw new BadRequestException("Invalid login");
		}
		if (employeeDetail.getSalary() == null || !(employeeDetail.getSalary() >= 0)) {
			logger.error("Record is invalid because of invalid salary " + employeeDetail.getSalary());
			throw new BadRequestException("Invalid salary");
		}
		if (StringUtils.isEmpty(employeeDetail.getStartDate())) {
			logger.error("Record is invalid because of missing startDate " + employeeDetail.getStartDate());
			throw new BadRequestException("Invalid date");
		}
		if (!DateTimeUtils.isValidDateFormat(employeeDetail.getStartDate())) {
			logger.error("Record is invalid because of wrong date format " + employeeDetail.getStartDate());
			throw new BadRequestException("Invalid date");
		}

	}

	private static EmployeeDetails toEntity(EmployeeDetailsDto employeeDetailsDto) {

		EmployeeDetails employeeDetails = new EmployeeDetails();

		employeeDetails.setId(employeeDetailsDto.getId());
		employeeDetails.setLogin(employeeDetailsDto.getLogin());
		employeeDetails.setName((employeeDetailsDto.getName()));
		employeeDetails.setSalary(employeeDetailsDto.getSalary());
		employeeDetails.setStartDate(DateTimeUtils.parse(employeeDetailsDto.getStartDate()));
		return employeeDetails;
	}

	private static EmployeeDetailsDto toDto(EmployeeDetails employeeDetailsDto) {

		EmployeeDetailsDto employeeDetails = new EmployeeDetailsDto();

		employeeDetails.setId(employeeDetailsDto.getId());
		employeeDetails.setLogin(employeeDetailsDto.getLogin());
		employeeDetails.setName((employeeDetailsDto.getName()));
		employeeDetails.setSalary(employeeDetailsDto.getSalary());

		employeeDetails.setStartDate(DateTimeUtils.format(employeeDetailsDto.getStartDate()));
		return employeeDetails;
	}

	/**
	 * used for updating an existing employee. Checks if the id exists and login is unique.
	 * If the details are correct then the details will be saved in DB
	 *
	 */
	public void updateEmployee(String id, EmployeeDetailsDto e) throws BadRequestException {
		validate(e);

		if (dao.findById(id).isEmpty()) {
			logger.error("Entered employee ID does not exist");
			throw new BadRequestException("No such employee");
		}
		EmployeeDetails eDetails=dao.findById(id).get();
		if(!e.getLogin().equals(eDetails.getLogin()))
		if (dao.isLoginExists(e.getLogin()).isPresent()) {
			logger.error("Entered employee login already exists");
			throw new BadRequestException("Employee login not unique");
		}

		dao.save(EmployeeSalaryManagementServiceImpl.toEntity(e));

	}

	/**
	 * deletes an employee from DB if it exists else respective error message is shown
	 *
	 */
	public void deleteEmployee(String id) {
		Optional<EmployeeDetails> o = dao.findById(id);
		if (o.isEmpty()) {
			logger.error("Entered employee ID does not exist");
			throw new BadRequestException("no Such Employee");
		}
		dao.deleteById(o.get().getId());
	}

	
	/**
	 * Fetches the details of employees, filters the employees with salary range
	 */
	public List<EmployeeDetailsDto> getAllEmployees(Optional<Double> minSalary, Optional<Double> maxSalary,
			Optional<Integer> offset, Optional<Integer> limit) {
		int limitAsInt = limit.isPresent() && limit.get()>0 ? limit.get() : Integer.MAX_VALUE;
		int offsetAsInt = offset.isPresent() ? offset.get() : 0;
		int page = limitAsInt > 0 && offsetAsInt / limitAsInt > 0 ? limitAsInt : 0;
		if(!(minSalary.get()>=0 || maxSalary.get()>=0)) throw new BadRequestException(" Bad input, ie. bad parameters ");
		List<EmployeeDetails> result =dao.findAllEmployees(minSalary.get(), maxSalary.get(), PageRequest.of(page, limitAsInt))
				.orElseThrow(() -> new BadRequestException()); 
 
		return result.stream().map(employee -> toDto(employee)).collect(Collectors.toList());

	}

	/**
	 *Retrives the details of employee if the id exists else respective error message is shown
	 */
	public EmployeeDetailsDto getEmployee(String id) {
		return toDto(dao.findById(id).orElseThrow(() -> new EmployeeNotFoundException("Bad input - no such employee")));
	}

	
}
