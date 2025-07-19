package com.ezee.insurence.cache.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class EmployeeCacheDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String code;
	private String name;
	private String username;
	private String employeeEmail;
	private String employeeMobile;
	private String password;
	private String employeeAddress;
	private String employeeRole;
	private int activeFlag;

}
