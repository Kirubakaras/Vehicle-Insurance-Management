package com.ezee.insurence.dto;

import lombok.Data;

@Data
public class AuthDTO {
	private String authToken;
	private EmployeeDTO emlployeeDTO;
}
