package com.example.employeesalarymanagement.reader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataReaderFactory {

	@Autowired
	private CsvReader csvReader;

	public DataReader createDataReader(String format) {
		return csvReader;
	}

}
