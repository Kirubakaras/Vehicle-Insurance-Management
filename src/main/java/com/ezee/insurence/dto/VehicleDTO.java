package com.ezee.insurence.dto;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class VehicleDTO extends BaseDTO {
	private CustomerDTO customerDTO;
	private String vehiclePlateNum;
	private String vehicleType;
	private String vehicleEnginNum;
	private String vehicleChasisNum;
	private String vehicleNumber;
	private String vehicleModelNum;
	private List<ClaimDTO> claimDTO;
	private List<PolicyDTO> policyDTO;
	private List<VehicleServiceDTO> vehicleServcieDTO;
}
