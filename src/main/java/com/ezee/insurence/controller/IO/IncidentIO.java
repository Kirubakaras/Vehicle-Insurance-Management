package com.ezee.insurence.controller.IO;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IncidentIO extends BaseIO {
	private CustomerIO customer;
	private String incidentDate;
	private String incidentType;
	private String incidentInspector;
	private BigDecimal incidentCost;
	private String incidentDescription;
	private String incidentStatus;
}
