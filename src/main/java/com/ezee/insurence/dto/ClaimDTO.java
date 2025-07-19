package com.ezee.insurence.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClaimDTO extends BaseDTO {
	private PolicyDTO policyDTO;
	private CustomerDTO customerDTO;
	private IncidentDTO incidentDTO;
	private String claimType;
	private String claimDate;
	private String claimDescription;
	private BigDecimal claimAmount;
	private String claimStatus;
}
