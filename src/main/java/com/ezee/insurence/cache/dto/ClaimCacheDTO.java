package com.ezee.insurence.cache.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.ezee.insurence.dto.CustomerDTO;
import com.ezee.insurence.dto.IncidentDTO;
import com.ezee.insurence.dto.PolicyDTO;

import lombok.Data;

@Data
public class ClaimCacheDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5974563233724719203L;

	private int id;
	private String code;
	private PolicyDTO policyDTO;
	private CustomerDTO customerDTO;
	private IncidentDTO incidentDTO;
	private String claimType;
	private String claimDate;
	private String claimDescription;
	private BigDecimal claimAmount;
	private String claimStatus;
	private String activeflag;
}
