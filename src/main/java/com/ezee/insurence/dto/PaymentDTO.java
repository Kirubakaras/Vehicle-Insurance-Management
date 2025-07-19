package com.ezee.insurence.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentDTO extends BaseDTO {
	private ReciptDTO reciptDTO;
	private CustomerDTO customerDTO;
	private String paymentDate;
	private BigDecimal paymentAmount;
	private String paymentMode;
}
