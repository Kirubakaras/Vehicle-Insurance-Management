package com.ezee.insurence.cache.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.ezee.insurence.dto.CustomerDTO;
import com.ezee.insurence.dto.VehicleDTO;

import lombok.Data;

@Data
public class PolicyCacheDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3941217166077145531L;

	private int id;
	private String code;
	private CustomerDTO customerDTO;
	private VehicleDTO vehicleDTO;
	private String policyNumber;
	private String startDate;
	private String expriyDate;
	private BigDecimal perimunAmount;
	private String paymentSchedule;
	private BigDecimal totalAmount;
	private String policyStatus;
	private String policyDescription;
	private int activeFlag;
}
