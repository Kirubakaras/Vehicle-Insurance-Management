package com.ezee.insurence.cache.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.ezee.insurence.dto.PolicyDTO;

import lombok.Data;

@Data
public class RenewalCacheDTO implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -8009093073069477265L;

	private int id;
	private String code;
	private PolicyDTO policyDTO;
	private String renewalDate;
	private String newExpriyDate;
	private BigDecimal renewalAmount;
	private String renewalStatus;
	private int activeFlag;

}
