package com.ezee.insurence.cache.dto;

import java.io.Serializable;

import com.ezee.insurence.dto.EmployeeDTO;

import lombok.Data;

@Data
public class AuthCacheDTO implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private String authToken;
	private EmployeeDTO employeeDTO;

}
