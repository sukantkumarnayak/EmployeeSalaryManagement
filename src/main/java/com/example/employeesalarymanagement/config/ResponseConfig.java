package com.example.employeesalarymanagement.config;

import java.util.Map;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

@Component
public class ResponseConfig {
	@Bean
	public ErrorAttributes errorAttributes() {
	    return new DefaultErrorAttributes() {
	        @Override
	        public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
	            Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
	            errorAttributes.remove("timestamp");
	            errorAttributes.remove("status");
	            errorAttributes.remove("error");
	            errorAttributes.remove("path");
	            return errorAttributes;
	        }

	   };
	}
}
