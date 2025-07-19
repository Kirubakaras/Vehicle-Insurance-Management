package com.ezee.insurence.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class IncidentDTO extends BaseDTO{
	private CustomerDTO customerDTO;
	private String incidentDate;
	private String incidentType;
	private String incidentInspector;
	private BigDecimal incidentCost;
	private String incidentDescription;
	private String incidentStatus;
}
