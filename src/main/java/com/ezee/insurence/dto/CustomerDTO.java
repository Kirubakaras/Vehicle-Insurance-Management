package com.ezee.insurence.dto;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerDTO extends BaseDTO {
	private String customerDOB;
	private String customerGender;
	private String customerAddress;
	private String customerNumber;
	private String customerEmail;
	private String customerLicenseNum;
	private List<VehicleDTO> vehicleDTO;
	private List<PolicyDTO> policyDTO;
	private List<IncidentDTO> incidentDTO;
	private List<ClaimDTO> claimDTO;
	private List<ReciptDTO> reciptDTO;
	private List<PaymentDTO> paymentDTO;
 }
