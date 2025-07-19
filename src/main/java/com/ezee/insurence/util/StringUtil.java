package com.ezee.insurence.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.ezee.insurence.exception.ErrorCode;
import com.ezee.insurence.exception.ServiceException;

@Component
public class StringUtil {
	private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final DateTimeFormatter DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public static String dateFormate(String dateStr) {
		String date = null;
		try {
			date = LocalDate.parse(dateStr, DATE).toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ErrorCode.DATE_FORMATE);
		}
		return date;

	}

	public static String dateTimeFormate(String dateStr) {
		String date = null;
		try {
			date = LocalDate.parse(dateStr, DATETIME).toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ErrorCode.DATE_TIME_FORMATE);
		}
		return date;
	}

	public static boolean notNull(String text) {
		boolean check = true;
		try {
			if (text == null) {
				check = false;
			}
		} catch (Exception e) {
			throw new ServiceException(ErrorCode.USERNAME_SHOULD_NOT_NULL);
		}
		return check;
	}

	public static boolean mobileNumCheck(String num) {
		boolean check = false;
		if (num.length() == 12) {
			int value = Integer.parseInt(num);
			if (value > 600000000 && value < 999999999) {
				check = true;
			}
		} else {
			throw new ServiceException(ErrorCode.USER_INVALID_MOBILE);
		}
		return check;
	}

	public static LocalDate getDateformate(String dateString) {
		LocalDate date = LocalDate.parse(dateString, DATE);
		return date;
	}

}
