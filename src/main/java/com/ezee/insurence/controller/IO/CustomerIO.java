package com.ezee.insurence.controller.IO;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerIO extends BaseIO{
	private String customerDOB;
	private String customerGender;
	private String customerAddress;
	private String customerNumber;
	private String customerEmail;
	private String customerLicenseNum;
	private List<VehicleIO> vehicle;
	private List<PolicyIO> policy;
	private List<IncidentIO> incident;
	private List<ClaimIO> claim;
	private List<ReciptIO> recipt;
	private List<PaymentIO> payment;
}
