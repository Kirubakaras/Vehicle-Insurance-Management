package com.ezee.insurence.cache.dto;

import java.io.Serializable;

import com.ezee.insurence.dto.CustomerDTO;

import lombok.Data;

@Data
public class VehicleCacheDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7559641862970134257L;

	private int id;
	private String code;
	private CustomerDTO customerDTO;
	private String vehiclePlateNum;
	private String vehicleType;
	private String vehicleEnginNum;
	private String vehicleChasisNum;
	private String vehicleNumber;
	private String vehicleModelNum;
	private int activeFlag;
	
}
