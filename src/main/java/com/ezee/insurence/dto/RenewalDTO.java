package com.ezee.insurence.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RenewalDTO extends BaseDTO {
	private PolicyDTO policyDTO;
	private String renewalDate;
	private String newExpriyDate;
	private BigDecimal renewalAmount;
	private String renewalStatus;
	private List<ReciptDTO> reciptDTO; 
}
