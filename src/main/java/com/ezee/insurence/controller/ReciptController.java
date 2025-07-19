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
import com.ezee.insurence.controller.IO.PolicyIO;
import com.ezee.insurence.controller.IO.ReciptIO;
import com.ezee.insurence.controller.IO.RenewalIO;
import com.ezee.insurence.controller.IO.ResponseIO;
import com.ezee.insurence.controller.IO.VehicleIO;
import com.ezee.insurence.dto.AuthDTO;
import com.ezee.insurence.dto.CustomerDTO;
import com.ezee.insurence.dto.PolicyDTO;
import com.ezee.insurence.dto.ReciptDTO;
import com.ezee.insurence.dto.RenewalDTO;
import com.ezee.insurence.service.AuthService;
import com.ezee.insurence.service.ReciptService;

@RestController
@RequestMapping("/recipt")
public class ReciptController {

	@Autowired
	private AuthService authService;

	@Autowired
	private ReciptService reciptService;

	@PostMapping("/add")
	public ResponseIO<String> addRecipt(@RequestHeader("authToken") String authToken, @RequestBody ReciptIO reciptIO) {
		AuthDTO authDTO = authService.verification(authToken);

		ReciptDTO reciptDTO = new ReciptDTO();

		PolicyDTO policyDTO = new PolicyDTO();
		policyDTO.setCode(reciptIO.getPolicy().getCode());
		reciptDTO.setPolicyDTO(policyDTO);

		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setCode(reciptIO.getCustomer().getCode());
		reciptDTO.setCustomerDTO(customerDTO);

		RenewalDTO renewalDTO = new RenewalDTO();
		renewalDTO.setCode(reciptIO.getRenewal().getCode());
		reciptDTO.setRenewalDTO(renewalDTO);

		reciptDTO.setReciptAmount(reciptIO.getReciptAmount());
		reciptDTO.setReciptDate(reciptIO.getReciptDate());
		reciptDTO.setReciptStatus(reciptIO.getReciptStatus());
		reciptDTO.setActiveFlag(reciptIO.getActiveFlag());

		reciptService.addRecipt(authDTO, reciptDTO);

		return ResponseIO.success("Receipt added successfully");
	}

	@PostMapping("/update/{code}")
	public ResponseIO<String> updateRecipt(@RequestHeader("authToken") String authToken, @PathVariable String code,
			@RequestBody ReciptIO reciptIO) {
		AuthDTO authDTO = authService.verification(authToken);

		ReciptDTO reciptDTO = new ReciptDTO();

		reciptDTO.setCode(code);
		PolicyDTO policyDTO = new PolicyDTO();
		policyDTO.setCode(reciptIO.getPolicy().getCode());
		reciptDTO.setPolicyDTO(policyDTO);

		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setCode(reciptIO.getCustomer().getCode());
		reciptDTO.setCustomerDTO(customerDTO);

		RenewalDTO renewalDTO = new RenewalDTO();
		renewalDTO.setCode(reciptIO.getRenewal().getCode());
		reciptDTO.setRenewalDTO(renewalDTO);

		reciptDTO.setReciptAmount(reciptIO.getReciptAmount());
		reciptDTO.setReciptDate(reciptIO.getReciptDate());
		reciptDTO.setReciptStatus(reciptIO.getReciptStatus());
		reciptDTO.setActiveFlag(reciptIO.getActiveFlag());

		reciptService.updateRecipt(authDTO, reciptDTO);

		return ResponseIO.success("Receipt update successfully");
	}

	@GetMapping("/{code}")
	public ResponseIO<ReciptIO> getReciptByCode(@RequestHeader("authToken") String authToken,
			@PathVariable String code) {
		authService.verification(authToken);

		ReciptDTO reciptDTO = reciptService.getReciptByCode(code);

		ReciptIO reciptIO = new ReciptIO();
		reciptIO.setCode(reciptDTO.getCode());

		if (reciptDTO.getPolicyDTO() != null) {
			PolicyIO policyIO = new PolicyIO();

			policyIO.setCode(reciptDTO.getPolicyDTO().getCode());
			VehicleIO vehicleIO = new VehicleIO();
			vehicleIO.setCode(reciptDTO.getPolicyDTO().getVehicleDTO().getCode());
			vehicleIO.setVehiclePlateNum(reciptDTO.getPolicyDTO().getVehicleDTO().getVehiclePlateNum());
			vehicleIO.setVehicleType(reciptDTO.getPolicyDTO().getVehicleDTO().getVehicleType());
			vehicleIO.setVehicleEnginNum(reciptDTO.getPolicyDTO().getVehicleDTO().getVehicleEnginNum());
			vehicleIO.setVehicleChasisNum(reciptDTO.getPolicyDTO().getVehicleDTO().getVehicleChasisNum());
			vehicleIO.setVehicleNumber(reciptDTO.getPolicyDTO().getVehicleDTO().getVehicleNumber());
			vehicleIO.setVehicleModelNum(reciptDTO.getPolicyDTO().getVehicleDTO().getVehicleModelNum());
			policyIO.setVehicle(vehicleIO);

			policyIO.setPolicyNumber(reciptDTO.getPolicyDTO().getPolicyNumber());
			policyIO.setStartDate(reciptDTO.getPolicyDTO().getStartDate());
			policyIO.setExpriyDate(reciptDTO.getPolicyDTO().getExpriyDate());
			policyIO.setPremiumAmount(reciptDTO.getPolicyDTO().getPremiumAmount());
			policyIO.setPaymentSchedule(reciptDTO.getPolicyDTO().getPaymentSchedule());
			policyIO.setTotalAmount(reciptDTO.getPolicyDTO().getTotalAmount());
			policyIO.setPolicyStatus(reciptDTO.getPolicyDTO().getPolicyStatus());
			policyIO.setPolicyDescription(reciptDTO.getPolicyDTO().getPolicyDescription());
			reciptIO.setPolicy(policyIO);

		} else {
			RenewalIO renewalIO = new RenewalIO();
			renewalIO.setCode(reciptDTO.getRenewalDTO().getCode());

			PolicyIO policyIO = new PolicyIO();

			policyIO.setCode(reciptDTO.getRenewalDTO().getPolicyDTO().getCode());

			VehicleIO vehicleIO = new VehicleIO();
			vehicleIO.setCode(reciptDTO.getRenewalDTO().getPolicyDTO().getVehicleDTO().getCode());
			vehicleIO.setVehiclePlateNum(reciptDTO.getRenewalDTO().getPolicyDTO().getVehicleDTO().getVehiclePlateNum());
			vehicleIO.setVehicleType(reciptDTO.getRenewalDTO().getPolicyDTO().getVehicleDTO().getVehicleType());
			vehicleIO.setVehicleEnginNum(reciptDTO.getRenewalDTO().getPolicyDTO().getVehicleDTO().getVehicleEnginNum());
			vehicleIO.setVehicleChasisNum(
					reciptDTO.getRenewalDTO().getPolicyDTO().getVehicleDTO().getVehicleChasisNum());
			vehicleIO.setVehicleNumber(reciptDTO.getRenewalDTO().getPolicyDTO().getVehicleDTO().getVehicleNumber());
			vehicleIO.setVehicleModelNum(reciptDTO.getRenewalDTO().getPolicyDTO().getVehicleDTO().getVehicleModelNum());
			policyIO.setVehicle(vehicleIO);

			policyIO.setPolicyNumber(reciptDTO.getRenewalDTO().getPolicyDTO().getPolicyNumber());
			policyIO.setStartDate(reciptDTO.getRenewalDTO().getPolicyDTO().getStartDate());
			policyIO.setExpriyDate(reciptDTO.getRenewalDTO().getPolicyDTO().getExpriyDate());
			policyIO.setPremiumAmount(reciptDTO.getRenewalDTO().getPolicyDTO().getPremiumAmount());
			policyIO.setPaymentSchedule(reciptDTO.getRenewalDTO().getPolicyDTO().getPaymentSchedule());
			policyIO.setTotalAmount(reciptDTO.getRenewalDTO().getPolicyDTO().getTotalAmount());
			policyIO.setPolicyStatus(reciptDTO.getRenewalDTO().getPolicyDTO().getPolicyStatus());
			policyIO.setPolicyDescription(reciptDTO.getRenewalDTO().getPolicyDTO().getPolicyDescription());

			renewalIO.setPolicy(policyIO);
			renewalIO.setRenewalDate(reciptDTO.getRenewalDTO().getRenewalDate());
			renewalIO.setNewExpriyDate(reciptDTO.getRenewalDTO().getNewExpriyDate());
			renewalIO.setRenewalAmount(reciptDTO.getRenewalDTO().getRenewalAmount());
			renewalIO.setRenewalStatus(reciptDTO.getRenewalDTO().getRenewalStatus());
			reciptIO.setRenewal(renewalIO);
		}

		CustomerIO customerIO = new CustomerIO();
		customerIO.setCode(reciptDTO.getCustomerDTO().getCode());
		customerIO.setName(reciptDTO.getCustomerDTO().getName());
		customerIO.setCustomerDOB(reciptDTO.getCustomerDTO().getCustomerDOB());
		customerIO.setCustomerGender(reciptDTO.getCustomerDTO().getCustomerGender());
		customerIO.setCustomerAddress(reciptDTO.getCustomerDTO().getCustomerAddress());
		customerIO.setCustomerNumber(reciptDTO.getCustomerDTO().getCustomerNumber());
		customerIO.setCustomerEmail(reciptDTO.getCustomerDTO().getCustomerEmail());
		customerIO.setCustomerLicenseNum(reciptDTO.getCustomerDTO().getCustomerLicenseNum());
		reciptIO.setCustomer(customerIO);

		reciptIO.setReciptAmount(reciptDTO.getReciptAmount());
		reciptIO.setPenaltyAmount(reciptDTO.getPenaltyAmount());
		reciptIO.setReciptDate(reciptDTO.getReciptDate());
		reciptIO.setDueDate(reciptDTO.getDueDate());
		reciptIO.setReciptTotalAmount(reciptDTO.getReciptTotalAmount());
		reciptIO.setReciptStatus(reciptDTO.getReciptStatus());

		return ResponseIO.success(reciptIO);
	}

	@GetMapping("/")
	public ResponseIO<List<ReciptIO>> getAllRecipts(@RequestHeader("authToken") String authToken) {
		authService.verification(authToken);
		List<ReciptDTO> listDTO = reciptService.getAllRecipt();
		List<ReciptIO> listIO = new ArrayList<>();

		for (ReciptDTO reciptDTO : listDTO) {
			ReciptIO reciptIO = new ReciptIO();
			reciptIO.setCode(reciptDTO.getCode());

			if (reciptDTO.getPolicyDTO() != null) {
				PolicyIO policyIO = new PolicyIO();

				policyIO.setCode(reciptDTO.getPolicyDTO().getCode());
				VehicleIO vehicleIO = new VehicleIO();
				vehicleIO.setCode(reciptDTO.getPolicyDTO().getVehicleDTO().getCode());
				vehicleIO.setVehiclePlateNum(reciptDTO.getPolicyDTO().getVehicleDTO().getVehiclePlateNum());
				vehicleIO.setVehicleType(reciptDTO.getPolicyDTO().getVehicleDTO().getVehicleType());
				vehicleIO.setVehicleEnginNum(reciptDTO.getPolicyDTO().getVehicleDTO().getVehicleEnginNum());
				vehicleIO.setVehicleChasisNum(reciptDTO.getPolicyDTO().getVehicleDTO().getVehicleChasisNum());
				vehicleIO.setVehicleNumber(reciptDTO.getPolicyDTO().getVehicleDTO().getVehicleNumber());
				vehicleIO.setVehicleModelNum(reciptDTO.getPolicyDTO().getVehicleDTO().getVehicleModelNum());
				policyIO.setVehicle(vehicleIO);

				policyIO.setPolicyNumber(reciptDTO.getPolicyDTO().getPolicyNumber());
				policyIO.setStartDate(reciptDTO.getPolicyDTO().getStartDate());
				policyIO.setExpriyDate(reciptDTO.getPolicyDTO().getExpriyDate());
				policyIO.setPremiumAmount(reciptDTO.getPolicyDTO().getPremiumAmount());
				policyIO.setPaymentSchedule(reciptDTO.getPolicyDTO().getPaymentSchedule());
				policyIO.setTotalAmount(reciptDTO.getPolicyDTO().getTotalAmount());
				policyIO.setPolicyStatus(reciptDTO.getPolicyDTO().getPolicyStatus());
				policyIO.setPolicyDescription(reciptDTO.getPolicyDTO().getPolicyDescription());
				reciptIO.setPolicy(policyIO);

			}else {
				reciptIO.setPolicy(null);
			}
			if(reciptDTO.getRenewalDTO() != null) {
				RenewalIO renewalIO = new RenewalIO();
				
				renewalIO.setCode(reciptDTO.getRenewalDTO().getCode());

				PolicyIO policyIO = new PolicyIO();

				policyIO.setCode(reciptDTO.getRenewalDTO().getPolicyDTO().getCode());

				VehicleIO vehicleIO = new VehicleIO();
				vehicleIO.setCode(reciptDTO.getRenewalDTO().getPolicyDTO().getVehicleDTO().getCode());
				vehicleIO.setVehiclePlateNum(
						reciptDTO.getRenewalDTO().getPolicyDTO().getVehicleDTO().getVehiclePlateNum());
				vehicleIO.setVehicleType(reciptDTO.getRenewalDTO().getPolicyDTO().getVehicleDTO().getVehicleType());
				vehicleIO.setVehicleEnginNum(
						reciptDTO.getRenewalDTO().getPolicyDTO().getVehicleDTO().getVehicleEnginNum());
				vehicleIO.setVehicleChasisNum(
						reciptDTO.getRenewalDTO().getPolicyDTO().getVehicleDTO().getVehicleChasisNum());
				vehicleIO.setVehicleNumber(reciptDTO.getRenewalDTO().getPolicyDTO().getVehicleDTO().getVehicleNumber());
				vehicleIO.setVehicleModelNum(
						reciptDTO.getRenewalDTO().getPolicyDTO().getVehicleDTO().getVehicleModelNum());
				policyIO.setVehicle(vehicleIO);

				policyIO.setPolicyNumber(reciptDTO.getRenewalDTO().getPolicyDTO().getPolicyNumber());
				policyIO.setStartDate(reciptDTO.getRenewalDTO().getPolicyDTO().getStartDate());
				policyIO.setExpriyDate(reciptDTO.getRenewalDTO().getPolicyDTO().getExpriyDate());
				policyIO.setPremiumAmount(reciptDTO.getRenewalDTO().getPolicyDTO().getPremiumAmount());
				policyIO.setPaymentSchedule(reciptDTO.getRenewalDTO().getPolicyDTO().getPaymentSchedule());
				policyIO.setTotalAmount(reciptDTO.getRenewalDTO().getPolicyDTO().getTotalAmount());
				policyIO.setPolicyStatus(reciptDTO.getRenewalDTO().getPolicyDTO().getPolicyStatus());
				policyIO.setPolicyDescription(reciptDTO.getRenewalDTO().getPolicyDTO().getPolicyDescription());

				renewalIO.setPolicy(policyIO);
				renewalIO.setRenewalDate(reciptDTO.getRenewalDTO().getRenewalDate());
				renewalIO.setNewExpriyDate(reciptDTO.getRenewalDTO().getNewExpriyDate());
				renewalIO.setRenewalAmount(reciptDTO.getRenewalDTO().getRenewalAmount());
				renewalIO.setRenewalStatus(reciptDTO.getRenewalDTO().getRenewalStatus());
				reciptIO.setRenewal(renewalIO);
			}else {
				reciptIO.setRenewal(null);
			}

			CustomerIO customerIO = new CustomerIO();
			customerIO.setCode(reciptDTO.getCustomerDTO().getCode());
			customerIO.setName(reciptDTO.getCustomerDTO().getName());
			customerIO.setCustomerDOB(reciptDTO.getCustomerDTO().getCustomerDOB());
			customerIO.setCustomerGender(reciptDTO.getCustomerDTO().getCustomerGender());
			customerIO.setCustomerAddress(reciptDTO.getCustomerDTO().getCustomerAddress());
			customerIO.setCustomerNumber(reciptDTO.getCustomerDTO().getCustomerNumber());
			customerIO.setCustomerEmail(reciptDTO.getCustomerDTO().getCustomerEmail());
			customerIO.setCustomerLicenseNum(reciptDTO.getCustomerDTO().getCustomerLicenseNum());
			reciptIO.setCustomer(customerIO);

			reciptIO.setReciptAmount(reciptDTO.getReciptAmount());
			reciptIO.setPenaltyAmount(reciptDTO.getPenaltyAmount());
			reciptIO.setReciptDate(reciptDTO.getReciptDate());
			reciptIO.setDueDate(reciptDTO.getDueDate());
			reciptIO.setReciptTotalAmount(reciptDTO.getReciptTotalAmount());
			reciptIO.setReciptStatus(reciptDTO.getReciptStatus());

			listIO.add(reciptIO);
		}

		return ResponseIO.success(listIO);
	}

}
