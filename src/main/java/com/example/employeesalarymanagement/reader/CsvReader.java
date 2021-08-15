package com.example.employeesalarymanagement.reader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.employeesalarymanagement.dto.CsvColumnNames;
import com.example.employeesalarymanagement.dto.EmployeeDetailsDto;
import com.example.employeesalarymanagement.exception.BadRequestException;
import com.example.employeesalarymanagement.exception.EmployeeSalaryManagementException;
import com.example.employeesalarymanagement.service.EmployeeSalaryManagementService;

@Component
public class CsvReader implements DataReader {

	Logger logger = LogManager.getLogger(CsvReader.class);

	@Autowired
	public EmployeeSalaryManagementService service;

	/**
	 * accepts a String argument to check for csv reads from the csv file
	 * 
	 * @throws EmployeeSalaryManagementException
	 */

	public List<EmployeeDetailsDto> readData( MultipartFile file)
			throws BadRequestException, EmployeeSalaryManagementException {
		logger.info("inside readFromCSV");
		if (file == null || !file.getOriginalFilename().endsWith("csv")) {
			logger.error("Filename is missing.");
			throw new BadRequestException("the input file is not csv");
		}
		List<EmployeeDetailsDto> employeeDetailsList = new ArrayList<>();
		Set<String> empId = new HashSet<>();
		Set<String> login = new HashSet<>();
		try (Reader reader = new InputStreamReader(file.getInputStream());
				CSVParser csvParser = new CSVParser(reader,
						CSVFormat.EXCEL.withFirstRecordAsHeader().withIgnoreEmptyLines().withTrim());) {
			for (CSVRecord csvRecord : csvParser) {
				if (csvRecord.get(CsvColumnNames.ROW_ID.getColumnName()).startsWith("#"))
					continue;
				EmployeeDetailsDto detailsDto = toDto(csvRecord);
				if (!empId.add(csvRecord.get(CsvColumnNames.ROW_ID.getColumnName()))) {
					logger.error("duplicate id found");
					throw new BadRequestException("duplicate row");
				}
				if (!login.add(csvRecord.get(CsvColumnNames.LOGIN.getColumnName()))) {
					logger.error("duplicate id found");
					throw new BadRequestException("Employee login not unique");
				}
				logger.info("add record " + detailsDto);
				employeeDetailsList.add(detailsDto);
			}
			return employeeDetailsList;
		} catch (IOException e) {
			logger.error("Error while reading csv-file ", e);
			throw new EmployeeSalaryManagementException("Internal server Error- Error occured while parsing the csv");

		}
	}

	private EmployeeDetailsDto toDto(CSVRecord csvRecord) {

		EmployeeDetailsDto detailsDto = new EmployeeDetailsDto();
		detailsDto.setId(csvRecord.get(CsvColumnNames.ROW_ID.getColumnName()));
		detailsDto.setLogin(csvRecord.get(CsvColumnNames.LOGIN.getColumnName()));
		detailsDto.setName(csvRecord.get(CsvColumnNames.NAME.getColumnName()));
		detailsDto.setSalary(Double.parseDouble(csvRecord.get(CsvColumnNames.SALARY.getColumnName())));
		detailsDto.setStartDate(csvRecord.get(CsvColumnNames.STARTDATE.getColumnName()));
		return detailsDto;
	}
}