package com.tellyouiam.alittlebitaboutspring.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringHelper {
	
	private static final String DATE_TIME_FORMAT_IN_CSV = "dd/MM/yyyy h:m:s a";
	
	public static String csvValue(Object value) {
		if (value == null) {
			return "";
		} else if (value instanceof String) {
			return String.format("\"%s\"", ((String) value).replace('"', '\'').replace("\ufe0f", ""));
		} else if (value instanceof Date) {
			return new SimpleDateFormat(DATE_TIME_FORMAT_IN_CSV).format((Date) value).toString();
		}
		
		return value.toString();
	}
}
