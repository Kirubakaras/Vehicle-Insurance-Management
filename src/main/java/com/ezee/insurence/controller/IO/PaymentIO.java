package com.ezee.insurence.controller.IO;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentIO extends BaseIO {
	private ReciptIO recipt;
	private CustomerIO customer;
	private String paymentDate;
	private BigDecimal paymentAmount;
	private String paymentMode;
}
