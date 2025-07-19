package com.ezee.insurence.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReciptDTO extends BaseDTO {
	private PolicyDTO policyDTO;
	private CustomerDTO customerDTO;
	private RenewalDTO renewalDTO;
	private BigDecimal reciptAmount;
	private BigDecimal penaltyAmount;
	private String reciptDate;
	private String dueDate;
	private BigDecimal reciptTotalAmount;
	private String reciptStatus;
}
