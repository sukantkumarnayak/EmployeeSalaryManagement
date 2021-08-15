package com.example.employeesalarymanagement.dto; 

public enum CsvColumnNames {
	ROW_ID ("id"),
	LOGIN("login"),
	NAME("name"),
	SALARY("salary"),
	STARTDATE("startDate");
	
	private String columnName ;
	
	private CsvColumnNames(String columnName) {
		this.columnName = columnName;
	}
	
	public String getColumnName() {
		return columnName;
	}

}
