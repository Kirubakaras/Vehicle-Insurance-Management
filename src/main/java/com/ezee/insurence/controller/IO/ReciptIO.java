package com.ezee.insurence.controller.IO;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReciptIO extends BaseIO {
	private PolicyIO policy;
	private CustomerIO customer;
	private RenewalIO renewal;
	private BigDecimal reciptAmount;
	private BigDecimal penaltyAmount;
	private String reciptDate;
	private String dueDate;
	private BigDecimal reciptTotalAmount;
	private String reciptStatus;
}
