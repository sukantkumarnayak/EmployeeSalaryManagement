package com.example.employeesalarymanagement.reader;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import com.example.employeesalarymanagement.dto.EmployeeDetailsDto;
import com.example.employeesalarymanagement.exception.BadRequestException;
import com.example.employeesalarymanagement.exception.EmployeeSalaryManagementException;
import com.example.employeesalarymanagement.service.EmployeeSalaryManagementService;
@ExtendWith(MockitoExtension.class)
public class CsvReaderTest {

	@Mock
	EmployeeSalaryManagementService service;
	@InjectMocks
	CsvReader csvReader;
	private String csvContent="id,login,name,salary,startDate\r\n"
			+ "e0001,hpotter,Harry Potter,0.00,16-Nov-01\r\n";
	private String csvContent1="e0001,hpotter,Harry Potter,0.00,16-Nov-01";
	private String csvContent2="e0002,hpotter1,Harry Potter,0.00,16-Nov-01";
	@Test
	public void readDataFileMissing() throws BadRequestException, EmployeeSalaryManagementException {
		
		BadRequestException b = Assertions.assertThrows(BadRequestException.class, () -> {
			csvReader.readData( null);
		});
		Assertions.assertEquals(b.getMessage(), "the input file is not csv");
	}
	
	@Test
	public void readDataDuplicateRow() throws BadRequestException, EmployeeSalaryManagementException {
		MockMultipartFile m=new MockMultipartFile("data", "filename.csv", "text/plain", (csvContent+csvContent1).getBytes());
		BadRequestException b = Assertions.assertThrows(BadRequestException.class, () -> {
			csvReader.readData( m);
		});
		Assertions.assertEquals(b.getMessage(), "duplicate row");
	}
	
	@Test
	public void readDataSkipRow() throws BadRequestException, EmployeeSalaryManagementException {
		MockMultipartFile m=new MockMultipartFile("data", "filename.csv", "text/plain", (csvContent+"#"+csvContent1).getBytes());
		
		List<EmployeeDetailsDto> l=csvReader.readData( m);
		Assertions.assertEquals(1, l.size());
		
	}
	
	@Test
	public void readDataSuccess() throws BadRequestException, EmployeeSalaryManagementException {
		MockMultipartFile m=new MockMultipartFile("data", "filename.csv", "text/plain", (csvContent+csvContent2).getBytes());
		
		List<EmployeeDetailsDto> l=csvReader.readData( m);
		Assertions.assertEquals(2, l.size());
		
	}
	
	
}
