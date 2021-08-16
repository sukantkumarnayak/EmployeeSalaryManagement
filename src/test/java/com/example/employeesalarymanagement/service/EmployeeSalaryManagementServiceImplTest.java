package com.example.employeesalarymanagement.service;

import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.employeesalarymanagement.dao.EmployeeDetailsDAO;
import com.example.employeesalarymanagement.dto.EmployeeDetailsDto;
import com.example.employeesalarymanagement.entity.EmployeeDetails;
import com.example.employeesalarymanagement.exception.BadRequestException;
import com.example.employeesalarymanagement.exception.EmployeeNotFoundException;

@ExtendWith(MockitoExtension.class)
public class EmployeeSalaryManagementServiceImplTest {
	@Mock
	EmployeeDetailsDAO dao;
	@InjectMocks
	EmployeeSalaryManagementServiceImpl employeeSalaryManagementServiceImpl;

	@Test
	public void createEmployeeSuccess() {
		EmployeeDetails eDetails = new EmployeeDetails();
		eDetails.setName("Harry Potter");
		eDetails.setLogin("hpotter");
		eDetails.setId("e0001");
		eDetails.setSalary(1234.0);
		eDetails.setStartDate(LocalDate.now());
		Optional.empty();
		when(dao.findById(Mockito.anyString())).thenReturn(Optional.empty());
		when(dao.isLoginExists(Mockito.anyString())).thenReturn(Optional.empty());
		when(dao.save(Mockito.any())).thenReturn(eDetails);
		EmployeeDetailsDto ed = getEmployeeDetailsDto();
		employeeSalaryManagementServiceImpl.createEmployee(ed);

	}

	@Test
	public void createEmployeeEmployeeExists() {
		EmployeeDetails eDetails = getEmployeeDetails();

		Optional<EmployeeDetails> o = Optional.of(eDetails);
		Optional.empty();
		when(dao.findById(Mockito.anyString())).thenReturn(o);
		dao.save(eDetails);
		EmployeeDetailsDto ed = getEmployeeDetailsDto();

		BadRequestException b = Assertions.assertThrows(BadRequestException.class, () -> {
			employeeSalaryManagementServiceImpl.createEmployee(ed);
		});
		Assertions.assertEquals(b.getMessage(), "Employee ID already exists");
	}

	@Test
	public void createEmployeeLoginExists() {
		EmployeeDetails eDetails = getEmployeeDetails();
		Optional<EmployeeDetails> o = Optional.of(eDetails);
		Optional.empty();
		when(dao.isLoginExists(Mockito.anyString())).thenReturn(o);
		dao.save(eDetails);
		EmployeeDetailsDto ed = getEmployeeDetailsDto();

		BadRequestException b = Assertions.assertThrows(BadRequestException.class, () -> {
			employeeSalaryManagementServiceImpl.createEmployee(ed);
		});
		Assertions.assertEquals(b.getMessage(), "Employee login not unique");
	}

	@Test
	public void createEmployeeInvalidName() {

		EmployeeDetailsDto ed = getEmployeeDetailsDto();
		ed.setName(null);
		BadRequestException b = Assertions.assertThrows(BadRequestException.class, () -> {
			employeeSalaryManagementServiceImpl.createEmployee(ed);
		});
		Assertions.assertEquals(b.getMessage(), "Invalid Name");
	}

	@Test
	public void createEmployeeInvalidLogin() {

		EmployeeDetailsDto ed = getEmployeeDetailsDto();
		ed.setLogin(null);

		BadRequestException thrown = Assertions.assertThrows(BadRequestException.class,
				() -> employeeSalaryManagementServiceImpl.createEmployee(ed)

		);
		Assertions.assertEquals(thrown.getMessage(), "Invalid login");
	}

	@Test
	public void createEmployeeInvalidStartDate() {

		EmployeeDetailsDto ed = getEmployeeDetailsDto();
		ed.setStartDate(null);

		BadRequestException thrown = Assertions.assertThrows(BadRequestException.class,
				() -> employeeSalaryManagementServiceImpl.createEmployee(ed)

		);
		Assertions.assertEquals(thrown.getMessage(), "Invalid date");
	}

	@Test
	public void createEmployeeWrongStartDate() {

		EmployeeDetailsDto ed = getEmployeeDetailsDto();
		ed.setStartDate("123");

		BadRequestException thrown = Assertions.assertThrows(BadRequestException.class,
				() -> employeeSalaryManagementServiceImpl.createEmployee(ed)

		);
		Assertions.assertEquals(thrown.getMessage(), "Invalid date");
	}

	@Test
	public void createEmployeeInvalidSalary() {
		EmployeeDetailsDto ed = getEmployeeDetailsDto();
		ed.setSalary(-1.0);

		BadRequestException thrown = Assertions.assertThrows(BadRequestException.class,
				() -> employeeSalaryManagementServiceImpl.createEmployee(ed)

		);
		Assertions.assertEquals(thrown.getMessage(), "Invalid salary");

	}

	@Test
	public void updateEmployeeSuccess() {
		EmployeeDetails eDetails = new EmployeeDetails();
		eDetails.setName("Harry Potter");
		eDetails.setLogin("hpotter");
		eDetails.setId("e0001");
		eDetails.setSalary(1234.0);
		eDetails.setStartDate(LocalDate.of(2021, 8, 10));
		when(dao.findById(Mockito.anyString())).thenReturn(Optional.of(eDetails));

		EmployeeDetailsDto ed = getEmployeeDetailsDto();
		ArgumentCaptor<EmployeeDetails> capture = ArgumentCaptor.forClass(EmployeeDetails.class);
		employeeSalaryManagementServiceImpl.updateEmployee("e0001", ed);
		Mockito.verify(dao).save(capture.capture());
		Assertions.assertEquals(ed.getId(), capture.getValue().getId());
		Assertions.assertEquals(ed.getSalary(), capture.getValue().getSalary());
		Assertions.assertEquals(ed.getLogin(), capture.getValue().getLogin());
		Assertions.assertEquals(ed.getName(), capture.getValue().getName());
		Assertions.assertEquals(LocalDate.of(2021, 8, 10), capture.getValue().getStartDate());

	}

	@Test
	public void updateEmployeeEmployeeNotExists() {
		EmployeeDetails eDetails = getEmployeeDetails();

		when(dao.findById(Mockito.anyString())).thenReturn(Optional.empty());
		dao.save(eDetails);
		EmployeeDetailsDto ed = getEmployeeDetailsDto();

		BadRequestException b = Assertions.assertThrows(BadRequestException.class, () -> {
			employeeSalaryManagementServiceImpl.updateEmployee("e0001", ed);
		});
		Assertions.assertEquals(b.getMessage(), "No such employee");
	}

	@Test
	public void updateEmployeeLoginExists() {
		EmployeeDetails eDetails = getEmployeeDetails();
		Optional<EmployeeDetails> o = Optional.of(eDetails);
		Optional.empty();
		when(dao.findById(Mockito.anyString())).thenReturn(o);
		eDetails.setLogin(eDetails.getLogin() + "1");
		when(dao.isLoginExists(Mockito.anyString())).thenReturn(Optional.of(eDetails));
		dao.save(eDetails);
		EmployeeDetailsDto ed = getEmployeeDetailsDto();

		BadRequestException b = Assertions.assertThrows(BadRequestException.class, () -> {
			employeeSalaryManagementServiceImpl.updateEmployee("e0001", ed);
		});
		Assertions.assertEquals(b.getMessage(), "Employee login not unique");
	}

	@Test
	public void updateEmployeeInvalidName() {

		EmployeeDetailsDto ed = getEmployeeDetailsDto();
		ed.setName(null);
		BadRequestException b = Assertions.assertThrows(BadRequestException.class, () -> {
			employeeSalaryManagementServiceImpl.updateEmployee("e0001", ed);
		});
		Assertions.assertEquals(b.getMessage(), "Invalid Name");
	}

	@Test
	public void updateEmployeeInvalidLogin() {

		EmployeeDetailsDto ed = getEmployeeDetailsDto();
		ed.setLogin(null);

		BadRequestException thrown = Assertions.assertThrows(BadRequestException.class,
				() -> employeeSalaryManagementServiceImpl.updateEmployee("e0001", ed)

		);
		Assertions.assertEquals(thrown.getMessage(), "Invalid login");
	}

	@Test
	public void updateEmployeeInvalidStartDate() {

		EmployeeDetailsDto ed = getEmployeeDetailsDto();
		ed.setStartDate(null);

		BadRequestException thrown = Assertions.assertThrows(BadRequestException.class,
				() -> employeeSalaryManagementServiceImpl.updateEmployee("e0001", ed)

		);
		Assertions.assertEquals(thrown.getMessage(), "Invalid date");
	}

	@Test
	public void updateEmployeeWrongStartDate() {

		EmployeeDetailsDto ed = getEmployeeDetailsDto();
		ed.setStartDate("123");

		BadRequestException thrown = Assertions.assertThrows(BadRequestException.class,
				() -> employeeSalaryManagementServiceImpl.updateEmployee("e0001", ed)

		);
		Assertions.assertEquals(thrown.getMessage(), "Invalid date");
	}

	@Test
	public void updateEmployeeInvalidSalary() {
		EmployeeDetailsDto ed = getEmployeeDetailsDto();
		ed.setSalary(-1.0);

		BadRequestException thrown = Assertions.assertThrows(BadRequestException.class,
				() -> employeeSalaryManagementServiceImpl.updateEmployee("e0001", ed)

		);
		Assertions.assertEquals(thrown.getMessage(), "Invalid salary");

	}

	@Test
	public void deleteEmployeeSuccess() {
		EmployeeDetails ed = getEmployeeDetails();
		Optional<EmployeeDetails> o = Optional.of(ed);
		when(dao.findById(Mockito.anyString())).thenReturn(o);

		ArgumentCaptor<String> capture = ArgumentCaptor.forClass(String.class);
		employeeSalaryManagementServiceImpl.deleteEmployee(ed.getId());
		Mockito.verify(dao).findById(capture.capture());
		Assertions.assertEquals(ed.getId(), capture.getValue());

	}

	@Test
	public void deleteEmployeeFailed() {
		when(dao.findById(Mockito.anyString())).thenReturn(Optional.empty());
		BadRequestException thrown = Assertions.assertThrows(BadRequestException.class,
				() -> employeeSalaryManagementServiceImpl.deleteEmployee("e0001")

		);
		Assertions.assertEquals(thrown.getMessage(), "no Such Employee");

	}

	@Test
	public void getAllEmployeesMinSalaryNegative() {
		BadRequestException thrown = Assertions.assertThrows(BadRequestException.class,
				() -> employeeSalaryManagementServiceImpl.getAllEmployees(Optional.of(-1.0), Optional.of(1.0),
						Optional.of(1), Optional.of(1))

		);
		Assertions.assertEquals(thrown.getMessage(), " Bad input, ie. bad parameters");

	}

	@Test
	public void getAllEmployeesMaxSalaryNegative() {
		BadRequestException thrown = Assertions.assertThrows(BadRequestException.class,
				() -> employeeSalaryManagementServiceImpl.getAllEmployees(Optional.of(1.0), Optional.of(-1.0),
						Optional.of(1), Optional.of(1))

		);
		Assertions.assertEquals(thrown.getMessage(), " Bad input, ie. bad parameters");

	}

	@Test
	public void getAllEmployeesLimitNegative() {
		BadRequestException thrown = Assertions.assertThrows(BadRequestException.class,
				() -> employeeSalaryManagementServiceImpl.getAllEmployees(Optional.of(1.0), Optional.of(1.0),
						Optional.of(1), Optional.of(-1))

		);
		Assertions.assertEquals(thrown.getMessage(), " Bad input, ie. bad parameters");

	}

	@Test
	public void getEmployeeFailed() {
		when(dao.findById(Mockito.anyString())).thenReturn(Optional.empty());
		EmployeeNotFoundException thrown = Assertions.assertThrows(EmployeeNotFoundException.class,
				() -> employeeSalaryManagementServiceImpl.getEmployee("1")

		);
		Assertions.assertEquals(thrown.getMessage(), "Bad input - no such employee");

	}

	@Test
	public void getEmployeeSuccess() {
		EmployeeDetails eDetails = new EmployeeDetails();
		eDetails.setName("Harry Potter");
		eDetails.setLogin("hpotter");
		eDetails.setId("e0001");
		eDetails.setSalary(1234.0);
		eDetails.setStartDate(LocalDate.of(2021, 8, 10));
		Optional<EmployeeDetails> o = Optional.of(eDetails);
		when(dao.findById(Mockito.anyString())).thenReturn(o);
		ArgumentCaptor<String> capture = ArgumentCaptor.forClass(String.class);
		employeeSalaryManagementServiceImpl.getEmployee("e0001");
		Mockito.verify(dao).findById(capture.capture());

	}

	private EmployeeDetails getEmployeeDetails() {
		EmployeeDetails eDetails = new EmployeeDetails();
		eDetails.setName("Harry Potter");
		eDetails.setLogin("hpotter");
		eDetails.setId("e0001");
		eDetails.setSalary(1234.0);
		eDetails.setStartDate(LocalDate.now());
		return eDetails;
	}

	private EmployeeDetailsDto getEmployeeDetailsDto() {
		EmployeeDetailsDto ed = new EmployeeDetailsDto();
		ed.setName("Harry Potter");
		ed.setLogin("hpotter");
		ed.setId("e0001");
		ed.setSalary(1234.0);
		ed.setStartDate("2021-08-10");
		return ed;
	}
}
