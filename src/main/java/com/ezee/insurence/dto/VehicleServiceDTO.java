package com.ezee.insurence.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class VehicleServiceDTO extends BaseDTO {
	private VehicleDTO vehicleDTO;
	private ClaimDTO claimDTO;
	private String serviceDate;
	private String serviceDescription;
	private BigDecimal serviceCost;
	private String serviceStatus;
}
