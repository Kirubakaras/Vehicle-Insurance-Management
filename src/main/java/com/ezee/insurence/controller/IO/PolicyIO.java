package com.ezee.insurence.controller.IO;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PolicyIO extends BaseIO {
	private CustomerIO customer;
	private VehicleIO vehicle;
	private String policyNumber;
	private String startDate;
	private String expriyDate;
	private BigDecimal premiumAmount;
	private String paymentSchedule;
	private BigDecimal totalAmount;
	private String policyStatus;
	private String policyDescription;
	private List<ClaimIO> claim;
	private List<ReciptIO> recipt;
	private List<RenewalIO> renewal;
}
