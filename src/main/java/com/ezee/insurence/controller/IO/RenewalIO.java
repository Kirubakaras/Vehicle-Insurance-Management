package com.ezee.insurence.controller.IO;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RenewalIO extends BaseIO {
	private PolicyIO policy;
	private String renewalDate;
	private String newExpriyDate;
	private BigDecimal renewalAmount;
	private String renewalStatus;
	private List<ReciptIO> recipt;
}
