package com.ezee.insurence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InsuranceApplication {

	private static final Logger logger = LogManager.getLogger("com.ezee.insurence.InsuranceApplication");

	public static void main(String[] args) {
		try {
			SpringApplication.run(InsuranceApplication.class, args);
		} catch (Exception e) {
			logger.error("Application is failed start ", e);
		}

	}

}
