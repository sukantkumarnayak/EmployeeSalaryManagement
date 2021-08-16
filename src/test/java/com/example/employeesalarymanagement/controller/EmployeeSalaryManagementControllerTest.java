package com.example.employeesalarymanagement.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.employeesalarymanagement.dto.EmployeeDetailsDto;
import com.example.employeesalarymanagement.exception.BadRequestException;
import com.example.employeesalarymanagement.exception.EmployeeNotFoundException;
import com.example.employeesalarymanagement.service.EmployeeSalaryManagementService;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeSalaryManagementControllerTest {
	@Autowired
	private MockMvc mockMvc;
	private final static String REQUEST = "{\"id\": \"e1\",\"login\": \"e11\", \"name\": \"test\",\"salary\": \"10\",\"startDate\": \"12-Nov-21\"}";

	@MockBean
	EmployeeSalaryManagementService service;

	@Test
	public void deleteReturn400() throws Exception {
		Mockito.doThrow(new BadRequestException("Bad input - no such employee")).when(service)
				.deleteEmployee(Mockito.anyString());
		this.mockMvc.perform(delete("/users/e0001")).andDo(print())
				.andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
				.andExpect(content().json("{\"message\":\"Bad input - no such employee\"}"));
	}

	@Test
	public void deleteSuccess() throws Exception {
		Mockito.doNothing().when(service).deleteEmployee(Mockito.anyString());
		this.mockMvc.perform(delete("/users/e0001")).andDo(print()).andExpect(status().is(HttpStatus.OK.value()))
				.andExpect(status().reason("Successfully deleted"));
	}

	@Test
	public void createEmployeeSuccess() throws Exception {
		Mockito.doNothing().when(service).createEmployee(Mockito.any());
		this.mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(REQUEST)).andDo(print())
				.andExpect(status().is(201)).andExpect(status().reason("Successfully created"));

	}

	@Test
	public void createEmployeeRecordExists() throws Exception {
		Mockito.doThrow(new BadRequestException("Employee ID already exists")).when(service)
				.createEmployee(Mockito.any());
		this.mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(REQUEST)).andDo(print())
				.andExpect(status().is(400)).andExpect(content().json("{\"message\": \"Employee ID already exists\"}"));
	}

	@Test
	public void createEmployeeLoginExists() throws Exception {
		Mockito.doThrow(new BadRequestException("Employee login not unique")).when(service)
				.createEmployee(Mockito.any());
		this.mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(REQUEST)).andDo(print())
				.andExpect(status().is(400)).andExpect(content().json("{\"message\": \"Employee login not unique\"}"));
	}

	@Test
	public void createEmployeeInvalidName() throws Exception {
		Mockito.doThrow(new BadRequestException("Invalid Name")).when(service).createEmployee(Mockito.any());
		this.mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(REQUEST)).andDo(print())
				.andExpect(status().is(400)).andExpect(content().json("{\"message\": \"Invalid Name\"}"));
	}

	@Test
	public void createEmployeeInvalidDate() throws Exception {
		Mockito.doThrow(new BadRequestException("Invalid date")).when(service).createEmployee(Mockito.any());
		this.mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(
				"{\"id\": \"e1\",\"login\": \"e11\",\"name\": \"test\",\"salary\": \"10\",\"startDate\": \"12-XX-21\"}"))
				.andExpect(status().is(400)).andExpect(content().json("{\"message\": \"Invalid date\"}"));
	}

	@Test
	public void getUserSuccess() throws Exception {
		EmployeeDetailsDto e = new EmployeeDetailsDto();
		e.setName("Harry Potter");
		e.setLogin("hpotter");
		e.setId("e0001");
		e.setSalary(1234.0);
		e.setStartDate("2021-08-10");
		when(service.getEmployee(Mockito.anyString())).thenReturn(e);
		this.mockMvc.perform(get("/users/e0001")).andDo(print()).andExpect(status().is(HttpStatus.OK.value()))
				.andExpect(content().json(
						"{\"id\":\"e0001\",\"login\":\"hpotter\",\"name\":\"Harry Potter\",\"salary\":1234.0,\"startDate\":\"2021-08-10\"}"));
	}

	@Test
	public void getUserFailed() throws Exception {
		when(service.getEmployee("e0001")).thenThrow(new EmployeeNotFoundException("Bad input - no such employee"));
		this.mockMvc.perform(get("/users/e0001")).andDo(print())
				.andExpect(content().json("{\"message\": \"Bad input - no such employee\"}"))
				.andExpect(status().is(HttpStatus.NOT_FOUND.value()));
	}

	@Test
	public void updateUserSuccess() throws Exception {

		Mockito.doNothing().when(service).updateEmployee(Mockito.anyString(), Mockito.any());
		this.mockMvc.perform(put("/users/e001").contentType(MediaType.APPLICATION_JSON).content(REQUEST))
				.andExpect(status().is(HttpStatus.OK.value())).andExpect(status().reason("Successfully updated"));
	}

	@Test
	public void updateUserNoEmployee() throws Exception {

		Mockito.doThrow(new BadRequestException("No such employee")).when(service).updateEmployee(Mockito.anyString(),
				Mockito.any());
		this.mockMvc.perform(put("/users/e001").contentType(MediaType.APPLICATION_JSON).content(REQUEST))
				.andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
				.andExpect(content().json("{\"message\": \"No such employee\"}"));
	}

	@Test
	public void updateUserLoginNotUnique() throws Exception {
		Mockito.doThrow(new BadRequestException("Employee login not unique")).when(service)
				.updateEmployee(Mockito.anyString(), Mockito.any());
		this.mockMvc.perform(put("/users/e001").contentType(MediaType.APPLICATION_JSON).content(REQUEST))
				.andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
				.andExpect(content().json("{\"message\": \"Employee login not unique\"}"));
	}

	@Test
	public void testUploadFileSuccessNoData() throws Exception {
		MockMultipartFile m = new MockMultipartFile("file", "filename.csv", "text/plain", ("").getBytes());
		this.mockMvc.perform(MockMvcRequestBuilders.multipart("/users/upload").file(m)).andExpect(status().is(200))
				.andExpect(content().string("Success but no data updated."));
	}

	@Test
	public void testUploadFile() throws Exception {
		String csvContent = "id,login,name,salary,startDate\r\n" + "e0001,hpotter,Harry Potter,0.00,16-Nov-01\r\n";
		String csvContent1 = "e0001,hpotter,Harry Potter,0.00,16-Nov-01";
		MockMultipartFile m = new MockMultipartFile("file", "filename.csv", "text/plain",
				(csvContent + csvContent1).getBytes());
		this.mockMvc.perform(MockMvcRequestBuilders.multipart("/users/upload").file(m)).andExpect(status().is(400))
				.andExpect(content().json("{\"message\": \"duplicate row\"}"));
	}

	@Test
	public void testUploadFileError() throws Exception {
		String csvContent = "id,login,name,salary,startDate\r\n" + "e0001,hpotter,Harry Potter,0.00,16-Nov-01\r\n";
		MockMultipartFile m = new MockMultipartFile("file", "filename.csv", "text/plain", (csvContent).getBytes());
		Mockito.doThrow(new BadRequestException("e0001: Invalid salary,e0002: Invalid salary")).when(service)
				.saveEmployeeData(Mockito.any());
		this.mockMvc.perform(MockMvcRequestBuilders.multipart("/users/upload").file(m)).andExpect(status().is(400))
				.andExpect(content().json("{\"message\": \"e0001: Invalid salary,e0002: Invalid salary\"}"));
	}
}
