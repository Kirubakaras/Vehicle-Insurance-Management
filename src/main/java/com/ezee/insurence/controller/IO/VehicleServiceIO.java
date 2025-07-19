package com.ezee.insurence.controller.IO;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleServiceIO extends BaseIO {
	private VehicleIO vehicle;
	private ClaimIO claim;
	private String serviceDate;
	private String serviceDescription;
	private BigDecimal serviceCost;
	private String serviceStatus;
}
