package com.ezee.insurence.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LogAuditDTO extends BaseDTO {
	private EmployeeDTO employeeDTO;
	private String login;
	private String logout;
}
