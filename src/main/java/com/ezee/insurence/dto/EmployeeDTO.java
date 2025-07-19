package com.ezee.insurence.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class EmployeeDTO extends BaseDTO {
	private String username;
	private String employeeEmail;
	private String employeeMobile;
	private String password;
	private String employeeAddress;
	private String employeeRole;
}
