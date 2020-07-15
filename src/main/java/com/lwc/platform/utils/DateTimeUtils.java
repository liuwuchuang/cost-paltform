package com.lwc.platform.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.util.StringUtils;

public class DateTimeUtils {

	private DateTimeUtils() {}
	
	public static Date parseStringToDate(String value,String format) {
		try {
			if(!StringUtils.isEmpty(value)) {
				SimpleDateFormat sdf=new SimpleDateFormat(format);
				return sdf.parse(value);
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String parseDateToString(Date date,String format) {
		SimpleDateFormat sdf=new SimpleDateFormat(format);
		return sdf.format(date);
	}
}
