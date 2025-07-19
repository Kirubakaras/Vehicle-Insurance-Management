package com.ezee.insurence.service;

import java.util.List;

import com.ezee.insurence.dto.AuthDTO;
import com.ezee.insurence.dto.PaymentDTO;

public interface PaymentService {
	public PaymentDTO addPayment(AuthDTO authDTO, PaymentDTO paymentDTO);

	public PaymentDTO updatePayment(AuthDTO authDTO, PaymentDTO paymentDTO);

	public PaymentDTO getPaymentByCode(String code);

	public List<PaymentDTO> getAllPayments();

}
