package com.ezee.insurence.controller.IO;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeIO extends BaseIO {
	private String username;
	private String employeeEmail;
	private String employeeMobile;
	private String password;
	private String employeeAddress;
	private String employeeRole;

}
