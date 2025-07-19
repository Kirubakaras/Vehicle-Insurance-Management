package com.ezee.insurence.dto;

import lombok.Data;

@Data
public class BaseDTO {
	private int id;
	private String code;
	private String name;
	private int activeFlag = 1;
}
