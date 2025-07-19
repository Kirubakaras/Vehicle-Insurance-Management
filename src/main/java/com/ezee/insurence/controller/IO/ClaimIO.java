package com.ezee.insurence.controller.IO;

import java.math.BigDecimal;

import com.ezee.insurence.dto.CustomerDTO;
import com.ezee.insurence.dto.IncidentDTO;
import com.ezee.insurence.dto.PolicyDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClaimIO extends BaseIO {
	private PolicyIO policy;
	private CustomerIO customer;
	private IncidentIO incident;
	private String claimType;
	private String claimDate;
	private String claimDescription;
	private BigDecimal claimAmount;
	private String claimStatus;
}
