package com.ezee.insurence.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezee.insurence.controller.IO.CustomerIO;
import com.ezee.insurence.controller.IO.PaymentIO;
import com.ezee.insurence.controller.IO.PolicyIO;
import com.ezee.insurence.controller.IO.ReciptIO;
import com.ezee.insurence.controller.IO.RenewalIO;
import com.ezee.insurence.controller.IO.ResponseIO;
import com.ezee.insurence.controller.IO.VehicleIO;
import com.ezee.insurence.dto.AuthDTO;
import com.ezee.insurence.dto.CustomerDTO;
import com.ezee.insurence.dto.PaymentDTO;
import com.ezee.insurence.dto.ReciptDTO;
import com.ezee.insurence.service.AuthService;
import com.ezee.insurence.service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {
	@Autowired
	private AuthService authService;

	@Autowired
	private PaymentService paymentService;

	@PostMapping("/add")
	public ResponseIO<PaymentIO> addPayment(@RequestHeader("authToken") String authToken,
			@RequestBody PaymentIO paymentIO) {
		AuthDTO authDTO = authService.verification(authToken);

		PaymentDTO paymentDTO = new PaymentDTO();

		ReciptDTO reciptDTO = new ReciptDTO();
		reciptDTO.setCode(paymentIO.getRecipt().getCode());
		paymentDTO.setReciptDTO(reciptDTO);

		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setCode(paymentIO.getCustomer().getCode());
		paymentDTO.setCustomerDTO(customerDTO);

		paymentDTO.setPaymentMode(paymentIO.getPaymentMode());
		paymentDTO.setActiveFlag(1);

		PaymentDTO dto = paymentService.addPayment(authDTO, paymentDTO);
		return ResponseIO.success(getDTOtoIO(dto));
	}

	@PostMapping("/update/{code}")
	public ResponseIO<PaymentIO> updatePayment(@RequestHeader("authToken") String authToken, @PathVariable String code,
			@RequestBody PaymentIO paymentIO) {
		AuthDTO authDTO = authService.verification(authToken);

		PaymentDTO paymentDTO = new PaymentDTO();

		paymentDTO.setCode(code);

		ReciptDTO reciptDTO = new ReciptDTO();
		reciptDTO.setCode(paymentIO.getRecipt().getCode());
		paymentDTO.setReciptDTO(reciptDTO);

		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setCode(paymentIO.getCustomer().getCode());
		paymentDTO.setCustomerDTO(customerDTO);

		paymentDTO.setPaymentDate(paymentIO.getPaymentDate());
		paymentDTO.setPaymentAmount(paymentIO.getPaymentAmount());
		paymentDTO.setPaymentMode(paymentIO.getPaymentMode());
		paymentDTO.setActiveFlag(paymentIO.getActiveFlag());

		PaymentDTO dto = paymentService.updatePayment(authDTO, paymentDTO);
		return ResponseIO.success(getDTOtoIO(dto));
	}

	@GetMapping("/{code}")
	public ResponseIO<PaymentIO> getPaymentByCode(@RequestHeader("authToken") String authToken,
			@PathVariable String code) {
		authService.verification(authToken);
		PaymentDTO paymentDTO = paymentService.getPaymentByCode(code);

		PaymentIO paymentIO = new PaymentIO();
		paymentIO.setCode(paymentDTO.getCode());

		ReciptIO reciptIO = new ReciptIO();
		reciptIO.setCode(paymentDTO.getReciptDTO().getCode());

		if (paymentDTO.getReciptDTO().getPolicyDTO() != null) {
			PolicyIO policyIO = new PolicyIO();

			policyIO.setCode(paymentDTO.getReciptDTO().getPolicyDTO().getCode());
			VehicleIO vehicleIO = new VehicleIO();
			vehicleIO.setCode(paymentDTO.getReciptDTO().getPolicyDTO().getVehicleDTO().getCode());
			vehicleIO.setVehiclePlateNum(paymentDTO.getReciptDTO().getPolicyDTO().getVehicleDTO().getVehiclePlateNum());
			vehicleIO.setVehicleType(paymentDTO.getReciptDTO().getPolicyDTO().getVehicleDTO().getVehicleType());
			vehicleIO.setVehicleEnginNum(paymentDTO.getReciptDTO().getPolicyDTO().getVehicleDTO().getVehicleEnginNum());
			vehicleIO.setVehicleChasisNum(
					paymentDTO.getReciptDTO().getPolicyDTO().getVehicleDTO().getVehicleChasisNum());
			vehicleIO.setVehicleNumber(paymentDTO.getReciptDTO().getPolicyDTO().getVehicleDTO().getVehicleNumber());
			vehicleIO.setVehicleModelNum(paymentDTO.getReciptDTO().getPolicyDTO().getVehicleDTO().getVehicleModelNum());
			policyIO.setVehicle(vehicleIO);

			policyIO.setPolicyNumber(paymentDTO.getReciptDTO().getPolicyDTO().getPolicyNumber());
			policyIO.setStartDate(paymentDTO.getReciptDTO().getPolicyDTO().getStartDate());
			policyIO.setExpriyDate(paymentDTO.getReciptDTO().getPolicyDTO().getExpriyDate());
			policyIO.setPremiumAmount(paymentDTO.getReciptDTO().getPolicyDTO().getPremiumAmount());
			policyIO.setPaymentSchedule(paymentDTO.getReciptDTO().getPolicyDTO().getPaymentSchedule());
			policyIO.setTotalAmount(paymentDTO.getReciptDTO().getPolicyDTO().getTotalAmount());
			policyIO.setPolicyStatus(paymentDTO.getReciptDTO().getPolicyDTO().getPolicyStatus());
			policyIO.setPolicyDescription(paymentDTO.getReciptDTO().getPolicyDTO().getPolicyDescription());
			reciptIO.setPolicy(policyIO);

		} else {
			RenewalIO renewalIO = new RenewalIO();
			renewalIO.setCode(paymentDTO.getReciptDTO().getRenewalDTO().getCode());

			PolicyIO policyIO = new PolicyIO();

			policyIO.setCode(paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getCode());

			VehicleIO vehicleIO = new VehicleIO();
			vehicleIO.setCode(paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getVehicleDTO().getCode());
			vehicleIO.setVehiclePlateNum(
					paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getVehicleDTO().getVehiclePlateNum());
			vehicleIO.setVehicleType(
					paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getVehicleDTO().getVehicleType());
			vehicleIO.setVehicleEnginNum(
					paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getVehicleDTO().getVehicleEnginNum());
			vehicleIO.setVehicleChasisNum(
					paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getVehicleDTO().getVehicleChasisNum());
			vehicleIO.setVehicleNumber(
					paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getVehicleDTO().getVehicleNumber());
			vehicleIO.setVehicleModelNum(
					paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getVehicleDTO().getVehicleModelNum());
			policyIO.setVehicle(vehicleIO);

			policyIO.setPolicyNumber(paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getPolicyNumber());
			policyIO.setStartDate(paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getStartDate());
			policyIO.setExpriyDate(paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getExpriyDate());
			policyIO.setPremiumAmount(paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getPremiumAmount());
			policyIO.setPaymentSchedule(paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getPaymentSchedule());
			policyIO.setTotalAmount(paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getTotalAmount());
			policyIO.setPolicyStatus(paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getPolicyStatus());
			policyIO.setPolicyDescription(
					paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getPolicyDescription());

			renewalIO.setPolicy(policyIO);
			renewalIO.setRenewalDate(paymentDTO.getReciptDTO().getRenewalDTO().getRenewalDate());
			renewalIO.setNewExpriyDate(paymentDTO.getReciptDTO().getRenewalDTO().getNewExpriyDate());
			renewalIO.setRenewalAmount(paymentDTO.getReciptDTO().getRenewalDTO().getRenewalAmount());
			renewalIO.setRenewalStatus(paymentDTO.getReciptDTO().getRenewalDTO().getRenewalStatus());
			reciptIO.setRenewal(renewalIO);
		}

		reciptIO.setReciptAmount(paymentDTO.getReciptDTO().getReciptAmount());
		reciptIO.setPenaltyAmount(paymentDTO.getReciptDTO().getPenaltyAmount());
		reciptIO.setReciptDate(paymentDTO.getReciptDTO().getReciptDate());
		reciptIO.setDueDate(paymentDTO.getReciptDTO().getDueDate());
		reciptIO.setReciptTotalAmount(paymentDTO.getReciptDTO().getReciptTotalAmount());
		reciptIO.setReciptStatus(paymentDTO.getReciptDTO().getReciptStatus());
		paymentIO.setRecipt(reciptIO);

		CustomerIO customerIO = new CustomerIO();
		customerIO.setCode(paymentDTO.getCustomerDTO().getCode());
		customerIO.setName(paymentDTO.getCustomerDTO().getName());
		customerIO.setCustomerDOB(paymentDTO.getCustomerDTO().getCustomerDOB());
		customerIO.setCustomerGender(paymentDTO.getCustomerDTO().getCustomerGender());
		customerIO.setCustomerAddress(paymentDTO.getCustomerDTO().getCustomerAddress());
		customerIO.setCustomerNumber(paymentDTO.getCustomerDTO().getCustomerNumber());
		customerIO.setCustomerEmail(paymentDTO.getCustomerDTO().getCustomerEmail());
		customerIO.setCustomerLicenseNum(paymentDTO.getCustomerDTO().getCustomerLicenseNum());
		paymentIO.setCustomer(customerIO);

		paymentIO.setPaymentDate(paymentDTO.getPaymentDate());
		paymentIO.setPaymentAmount(paymentDTO.getPaymentAmount());
		paymentIO.setPaymentMode(paymentDTO.getPaymentMode());

		return ResponseIO.success(paymentIO);
	}

	@GetMapping("/")
	public ResponseIO<List<PaymentIO>> getAllPayments(@RequestHeader("authToken") String authToken) {
		authService.verification(authToken);
		List<PaymentDTO> listDTO = paymentService.getAllPayments();
		List<PaymentIO> listIO = new ArrayList<>();

		for (PaymentDTO paymentDTO : listDTO) {
			PaymentIO paymentIO = new PaymentIO();
			paymentIO.setCode(paymentDTO.getCode());

			ReciptIO reciptIO = new ReciptIO();
			reciptIO.setCode(paymentDTO.getReciptDTO().getCode());

			if (paymentDTO.getReciptDTO().getPolicyDTO() != null) {
				PolicyIO policyIO = new PolicyIO();

				policyIO.setCode(paymentDTO.getReciptDTO().getPolicyDTO().getCode());
				VehicleIO vehicleIO = new VehicleIO();
				vehicleIO.setCode(paymentDTO.getReciptDTO().getPolicyDTO().getVehicleDTO().getCode());
				vehicleIO.setVehiclePlateNum(
						paymentDTO.getReciptDTO().getPolicyDTO().getVehicleDTO().getVehiclePlateNum());
				vehicleIO.setVehicleType(paymentDTO.getReciptDTO().getPolicyDTO().getVehicleDTO().getVehicleType());
				vehicleIO.setVehicleEnginNum(
						paymentDTO.getReciptDTO().getPolicyDTO().getVehicleDTO().getVehicleEnginNum());
				vehicleIO.setVehicleChasisNum(
						paymentDTO.getReciptDTO().getPolicyDTO().getVehicleDTO().getVehicleChasisNum());
				vehicleIO.setVehicleNumber(paymentDTO.getReciptDTO().getPolicyDTO().getVehicleDTO().getVehicleNumber());
				vehicleIO.setVehicleModelNum(
						paymentDTO.getReciptDTO().getPolicyDTO().getVehicleDTO().getVehicleModelNum());
				policyIO.setVehicle(vehicleIO);

				policyIO.setPolicyNumber(paymentDTO.getReciptDTO().getPolicyDTO().getPolicyNumber());
				policyIO.setStartDate(paymentDTO.getReciptDTO().getPolicyDTO().getStartDate());
				policyIO.setExpriyDate(paymentDTO.getReciptDTO().getPolicyDTO().getExpriyDate());
				policyIO.setPremiumAmount(paymentDTO.getReciptDTO().getPolicyDTO().getPremiumAmount());
				policyIO.setPaymentSchedule(paymentDTO.getReciptDTO().getPolicyDTO().getPaymentSchedule());
				policyIO.setTotalAmount(paymentDTO.getReciptDTO().getPolicyDTO().getTotalAmount());
				policyIO.setPolicyStatus(paymentDTO.getReciptDTO().getPolicyDTO().getPolicyStatus());
				policyIO.setPolicyDescription(paymentDTO.getReciptDTO().getPolicyDTO().getPolicyDescription());
				reciptIO.setPolicy(policyIO);

			} else {
				RenewalIO renewalIO = new RenewalIO();
				renewalIO.setCode(paymentDTO.getReciptDTO().getRenewalDTO().getCode());

				PolicyIO policyIO = new PolicyIO();

				policyIO.setCode(paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getCode());

				VehicleIO vehicleIO = new VehicleIO();
				vehicleIO.setCode(paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getVehicleDTO().getCode());
				vehicleIO.setVehiclePlateNum(
						paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getVehicleDTO().getVehiclePlateNum());
				vehicleIO.setVehicleType(
						paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getVehicleDTO().getVehicleType());
				vehicleIO.setVehicleEnginNum(
						paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getVehicleDTO().getVehicleEnginNum());
				vehicleIO.setVehicleChasisNum(
						paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getVehicleDTO().getVehicleChasisNum());
				vehicleIO.setVehicleNumber(
						paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getVehicleDTO().getVehicleNumber());
				vehicleIO.setVehicleModelNum(
						paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getVehicleDTO().getVehicleModelNum());
				policyIO.setVehicle(vehicleIO);

				policyIO.setPolicyNumber(paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getPolicyNumber());
				policyIO.setStartDate(paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getStartDate());
				policyIO.setExpriyDate(paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getExpriyDate());
				policyIO.setPremiumAmount(paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getPremiumAmount());
				policyIO.setPaymentSchedule(
						paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getPaymentSchedule());
				policyIO.setTotalAmount(paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getTotalAmount());
				policyIO.setPolicyStatus(paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getPolicyStatus());
				policyIO.setPolicyDescription(
						paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getPolicyDescription());

				renewalIO.setPolicy(policyIO);
				renewalIO.setRenewalDate(paymentDTO.getReciptDTO().getRenewalDTO().getRenewalDate());
				renewalIO.setNewExpriyDate(paymentDTO.getReciptDTO().getRenewalDTO().getNewExpriyDate());
				renewalIO.setRenewalAmount(paymentDTO.getReciptDTO().getRenewalDTO().getRenewalAmount());
				renewalIO.setRenewalStatus(paymentDTO.getReciptDTO().getRenewalDTO().getRenewalStatus());
				reciptIO.setRenewal(renewalIO);
			}

			reciptIO.setReciptAmount(paymentDTO.getReciptDTO().getReciptAmount());
			reciptIO.setPenaltyAmount(paymentDTO.getReciptDTO().getPenaltyAmount());
			reciptIO.setReciptDate(paymentDTO.getReciptDTO().getReciptDate());
			reciptIO.setDueDate(paymentDTO.getReciptDTO().getDueDate());
			reciptIO.setReciptTotalAmount(paymentDTO.getReciptDTO().getReciptTotalAmount());
			reciptIO.setReciptStatus(paymentDTO.getReciptDTO().getReciptStatus());
			paymentIO.setRecipt(reciptIO);

			CustomerIO customerIO = new CustomerIO();
			customerIO.setCode(paymentDTO.getCustomerDTO().getCode());
			customerIO.setName(paymentDTO.getCustomerDTO().getName());
			customerIO.setCustomerDOB(paymentDTO.getCustomerDTO().getCustomerDOB());
			customerIO.setCustomerGender(paymentDTO.getCustomerDTO().getCustomerGender());
			customerIO.setCustomerAddress(paymentDTO.getCustomerDTO().getCustomerAddress());
			customerIO.setCustomerNumber(paymentDTO.getCustomerDTO().getCustomerNumber());
			customerIO.setCustomerEmail(paymentDTO.getCustomerDTO().getCustomerEmail());
			customerIO.setCustomerLicenseNum(paymentDTO.getCustomerDTO().getCustomerLicenseNum());
			paymentIO.setCustomer(customerIO);

			paymentIO.setPaymentDate(paymentDTO.getPaymentDate());
			paymentIO.setPaymentAmount(paymentDTO.getPaymentAmount());
			paymentIO.setPaymentMode(paymentDTO.getPaymentMode());
			listIO.add(paymentIO);
		}

		return ResponseIO.success(listIO);
	}

	public PaymentIO getDTOtoIO(PaymentDTO paymentDTO) {

		PaymentIO paymentIO = new PaymentIO();
		paymentIO.setCode(paymentDTO.getCode());

		ReciptIO reciptIO = new ReciptIO();
		reciptIO.setCode(paymentDTO.getReciptDTO().getCode());

		if (paymentDTO.getReciptDTO().getPolicyDTO() != null) {
			PolicyIO policyIO = new PolicyIO();

			policyIO.setCode(paymentDTO.getReciptDTO().getPolicyDTO().getCode());
			VehicleIO vehicleIO = new VehicleIO();
			vehicleIO.setCode(paymentDTO.getReciptDTO().getPolicyDTO().getVehicleDTO().getCode());
			vehicleIO.setVehiclePlateNum(paymentDTO.getReciptDTO().getPolicyDTO().getVehicleDTO().getVehiclePlateNum());
			vehicleIO.setVehicleType(paymentDTO.getReciptDTO().getPolicyDTO().getVehicleDTO().getVehicleType());
			vehicleIO.setVehicleEnginNum(paymentDTO.getReciptDTO().getPolicyDTO().getVehicleDTO().getVehicleEnginNum());
			vehicleIO.setVehicleChasisNum(
					paymentDTO.getReciptDTO().getPolicyDTO().getVehicleDTO().getVehicleChasisNum());
			vehicleIO.setVehicleNumber(paymentDTO.getReciptDTO().getPolicyDTO().getVehicleDTO().getVehicleNumber());
			vehicleIO.setVehicleModelNum(paymentDTO.getReciptDTO().getPolicyDTO().getVehicleDTO().getVehicleModelNum());
			policyIO.setVehicle(vehicleIO);

			policyIO.setPolicyNumber(paymentDTO.getReciptDTO().getPolicyDTO().getPolicyNumber());
			policyIO.setStartDate(paymentDTO.getReciptDTO().getPolicyDTO().getStartDate());
			policyIO.setExpriyDate(paymentDTO.getReciptDTO().getPolicyDTO().getExpriyDate());
			policyIO.setPremiumAmount(paymentDTO.getReciptDTO().getPolicyDTO().getPremiumAmount());
			policyIO.setPaymentSchedule(paymentDTO.getReciptDTO().getPolicyDTO().getPaymentSchedule());
			policyIO.setTotalAmount(paymentDTO.getReciptDTO().getPolicyDTO().getTotalAmount());
			policyIO.setPolicyStatus(paymentDTO.getReciptDTO().getPolicyDTO().getPolicyStatus());
			policyIO.setPolicyDescription(paymentDTO.getReciptDTO().getPolicyDTO().getPolicyDescription());
			reciptIO.setPolicy(policyIO);

		} else {
			RenewalIO renewalIO = new RenewalIO();
			renewalIO.setCode(paymentDTO.getReciptDTO().getRenewalDTO().getCode());

			PolicyIO policyIO = new PolicyIO();

			policyIO.setCode(paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getCode());

			VehicleIO vehicleIO = new VehicleIO();
			vehicleIO.setCode(paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getVehicleDTO().getCode());
			vehicleIO.setVehiclePlateNum(
					paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getVehicleDTO().getVehiclePlateNum());
			vehicleIO.setVehicleType(
					paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getVehicleDTO().getVehicleType());
			vehicleIO.setVehicleEnginNum(
					paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getVehicleDTO().getVehicleEnginNum());
			vehicleIO.setVehicleChasisNum(
					paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getVehicleDTO().getVehicleChasisNum());
			vehicleIO.setVehicleNumber(
					paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getVehicleDTO().getVehicleNumber());
			vehicleIO.setVehicleModelNum(
					paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getVehicleDTO().getVehicleModelNum());
			policyIO.setVehicle(vehicleIO);

			policyIO.setPolicyNumber(paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getPolicyNumber());
			policyIO.setStartDate(paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getStartDate());
			policyIO.setExpriyDate(paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getExpriyDate());
			policyIO.setPremiumAmount(paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getPremiumAmount());
			policyIO.setPaymentSchedule(paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getPaymentSchedule());
			policyIO.setTotalAmount(paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getTotalAmount());
			policyIO.setPolicyStatus(paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getPolicyStatus());
			policyIO.setPolicyDescription(
					paymentDTO.getReciptDTO().getRenewalDTO().getPolicyDTO().getPolicyDescription());

			renewalIO.setPolicy(policyIO);
			renewalIO.setRenewalDate(paymentDTO.getReciptDTO().getRenewalDTO().getRenewalDate());
			renewalIO.setNewExpriyDate(paymentDTO.getReciptDTO().getRenewalDTO().getNewExpriyDate());
			renewalIO.setRenewalAmount(paymentDTO.getReciptDTO().getRenewalDTO().getRenewalAmount());
			renewalIO.setRenewalStatus(paymentDTO.getReciptDTO().getRenewalDTO().getRenewalStatus());
			reciptIO.setRenewal(renewalIO);
		}

		reciptIO.setReciptAmount(paymentDTO.getReciptDTO().getReciptAmount());
		reciptIO.setPenaltyAmount(paymentDTO.getReciptDTO().getPenaltyAmount());
		reciptIO.setReciptDate(paymentDTO.getReciptDTO().getReciptDate());
		reciptIO.setDueDate(paymentDTO.getReciptDTO().getDueDate());
		reciptIO.setReciptTotalAmount(paymentDTO.getReciptDTO().getReciptTotalAmount());
		reciptIO.setReciptStatus(paymentDTO.getReciptDTO().getReciptStatus());
		paymentIO.setRecipt(reciptIO);

		CustomerIO customerIO = new CustomerIO();
		customerIO.setCode(paymentDTO.getCustomerDTO().getCode());
		customerIO.setName(paymentDTO.getCustomerDTO().getName());
		customerIO.setCustomerDOB(paymentDTO.getCustomerDTO().getCustomerDOB());
		customerIO.setCustomerGender(paymentDTO.getCustomerDTO().getCustomerGender());
		customerIO.setCustomerAddress(paymentDTO.getCustomerDTO().getCustomerAddress());
		customerIO.setCustomerNumber(paymentDTO.getCustomerDTO().getCustomerNumber());
		customerIO.setCustomerEmail(paymentDTO.getCustomerDTO().getCustomerEmail());
		customerIO.setCustomerLicenseNum(paymentDTO.getCustomerDTO().getCustomerLicenseNum());
		paymentIO.setCustomer(customerIO);

		paymentIO.setPaymentDate(paymentDTO.getPaymentDate());
		paymentIO.setPaymentAmount(paymentDTO.getPaymentAmount());
		paymentIO.setPaymentMode(paymentDTO.getPaymentMode());

		return paymentIO;
	}

}
