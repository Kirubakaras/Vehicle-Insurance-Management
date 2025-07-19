package com.ezee.insurence.controller.IO;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleIO extends BaseIO {
	private CustomerIO customer;
	private String vehiclePlateNum;
	private String vehicleType;
	private String vehicleEnginNum;
	private String vehicleChasisNum;
	private String vehicleNumber;
	private String vehicleModelNum;
	private List<ClaimIO> claim;
	private List<PolicyIO> policy;
	private List<VehicleServiceIO> vehicleServcie;
}
