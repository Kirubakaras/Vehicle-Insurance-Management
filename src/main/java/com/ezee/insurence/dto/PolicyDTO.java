package com.ezee.insurence.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PolicyDTO extends BaseDTO {
	private CustomerDTO customerDTO;
	private VehicleDTO vehicleDTO;
	private String policyNumber;
	private String startDate;
	private String expriyDate;
	private BigDecimal premiumAmount;
	private String paymentSchedule;
	private BigDecimal totalAmount;
	private String policyStatus;
	private String policyDescription;
	private List<ClaimDTO> claimDTO;
	private List<ReciptDTO> reciptDTO;
	private List<RenewalDTO> renewalDTO;
}
