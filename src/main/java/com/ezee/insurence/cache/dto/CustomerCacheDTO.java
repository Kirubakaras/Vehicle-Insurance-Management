package com.ezee.insurence.cache.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class CustomerCacheDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9136013116679557652L;

	private int id;
	private String code;
	private String name;
	private String customerDOB;
	private String customerGender;
	private String customerAddress;
	private String customerNumber;
	private String customerEmail;
	private String customerLicenseNum;
	private int activeFlag;
}
