package com.example.employeesalarymanagement.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.employeesalarymanagement.exception.BadRequestException;
import com.example.employeesalarymanagement.exception.EmployeeNotFoundException;
import com.example.employeesalarymanagement.response.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(EmployeeNotFoundException.class)
	@ResponseBody
	ResponseEntity<ErrorResponse> userNotFoundHandler(EmployeeNotFoundException ex) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), httpHeaders, HttpStatus.NOT_FOUND);
	}

	@ResponseBody
	@ExceptionHandler(BadRequestException.class)
	ResponseEntity<ErrorResponse> badRequestHandlerHandler(BadRequestException ex) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), httpHeaders, HttpStatus.BAD_REQUEST);
	}

}
