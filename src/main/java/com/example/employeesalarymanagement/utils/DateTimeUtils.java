package com.example.employeesalarymanagement.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DateTimeUtils {

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[yyyy-MM-dd][dd-MMM-yy]",
			Locale.US);
	static Logger logger = LogManager.getLogger(DateTimeUtils.class);
	/**
	 * @param timeStamp
	 * @return
	 */
	public static LocalDate parse(String timeStamp) {
		return LocalDate.parse(timeStamp, formatter);

	}

	/**
	 * @param timeStamp
	 * @return
	 */
	public static String format(Temporal timeStamp) {
		return DateTimeFormatter.ofPattern("[yyyy-MM-dd]",
				Locale.US).format(timeStamp);

	}

	/**
	 * @param date
	 * @return
	 */
	public static boolean isValidDateFormat(String date) {
		try {
			LocalDate.parse(date, formatter);
			return true;
		} catch (Exception e) {
			logger.error("Error Occurred during parsing date",e);
			return false;
		}

	}
}
