package com.ezee.insurence.util;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class TokenGenerator {
	public static Map<Integer, String> NumStrMap;
	public static AtomicInteger sequence = new AtomicInteger(-1);

	public static Integer getNextSequence() {
		if (sequence.get() > 999) {
			sequence.getAndSet(-1);
		}
		return sequence.incrementAndGet();
	}

	// generate authToken

	public static String authTokenGenerator() {
		String token = UUID.randomUUID().toString().replace("-", "");
		return token;
	}

	// generate the code for specific subject

	public static String generateCode(String prifix, int length) {
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
		Random random = new Random();

		StringBuilder code = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			code.append(chars.charAt(random.nextInt(chars.length())));
		}
		return prifix + code.toString();
	}
	
	public static String generateNum(String prifix, int length) {
		String chars = "1234567890";
		Random random = new Random();

		StringBuilder code = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			code.append(chars.charAt(random.nextInt(chars.length())));
		}
		return prifix + code.toString();
	}

	public static String generateCode(String prefixCode) {
		int sequenceId = 0;
		try {
			sequenceId = getNextSequence();
			LocalDateTime datetime = LocalDateTime.now();
			prefixCode = prefixCode + getNumStrMap(datetime.getYear() % 1000) + getNumStrMap(datetime.getDayOfYear())
					+ getNumStrMap(datetime.getDayOfMonth()) + getNumStrMap(datetime.getHour())
					+ getNumStrMap(datetime.getMinute()) + getNumStrMap(datetime.getSecond())
					+ getNumStrMap(sequenceId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prefixCode;
	}

	private static String getNumStrMap(int key) {
		if (NumStrMap == null || NumStrMap.isEmpty()) {
			initialize();
		}

		if (NumStrMap.get(key) != null) {
			return NumStrMap.get(key);
		}
		return String.valueOf(key);
	}

	public static void initialize() {
		NumStrMap = new HashMap<Integer, String>();
		NumStrMap.put(0, "0");
		NumStrMap.put(1, "1");
		NumStrMap.put(2, "2");
		NumStrMap.put(3, "3");
		NumStrMap.put(4, "4");
		NumStrMap.put(5, "5");
		NumStrMap.put(6, "6");
		NumStrMap.put(7, "7");
		NumStrMap.put(8, "8");
		NumStrMap.put(9, "9");
		NumStrMap.put(10, "A");
		NumStrMap.put(11, "B");
		NumStrMap.put(12, "C");
		NumStrMap.put(13, "D");
		NumStrMap.put(14, "E");
		NumStrMap.put(15, "F");
		NumStrMap.put(16, "G");
		NumStrMap.put(17, "H");
		NumStrMap.put(18, "I");
		NumStrMap.put(19, "J");
		NumStrMap.put(20, "K");
		NumStrMap.put(21, "L");
		NumStrMap.put(22, "M");
		NumStrMap.put(23, "N");
		NumStrMap.put(24, "O");
		NumStrMap.put(25, "P");
		NumStrMap.put(26, "Q");
		NumStrMap.put(27, "R");
		NumStrMap.put(28, "S");
		NumStrMap.put(29, "T");
		NumStrMap.put(30, "U");
		NumStrMap.put(31, "V");
		NumStrMap.put(32, "W");
		NumStrMap.put(33, "X");
		NumStrMap.put(34, "Y");
		NumStrMap.put(35, "Z");
	}

}
