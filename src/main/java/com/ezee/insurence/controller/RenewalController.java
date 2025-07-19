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
import com.ezee.insurence.dto.PolicyDTO;
import com.ezee.insurence.dto.ReciptDTO;
import com.ezee.insurence.dto.RenewalDTO;
import com.ezee.insurence.service.AuthService;
import com.ezee.insurence.service.RenewalService;
import com.ezee.insurence.util.StringUtil;

@RestController
@RequestMapping("/renewal")
public class RenewalController {
	@Autowired
	private AuthService authService;

	@Autowired
	private RenewalService renewalService;

	@PostMapping("/add")
	public ResponseIO<String> addRenewal(@RequestHeader("authToken") String authToken,
			@RequestBody RenewalIO renewalIO) {
		AuthDTO authDTO = authService.verification(authToken);

		RenewalDTO renewalDTO = new RenewalDTO();

		PolicyDTO policyDTO = new PolicyDTO();
		policyDTO.setCode(renewalIO.getPolicy().getCode());
		renewalDTO.setPolicyDTO(policyDTO);

		renewalDTO.setRenewalDate(renewalIO.getRenewalDate());
		renewalDTO.setNewExpriyDate(StringUtil.dateFormate(renewalIO.getNewExpriyDate()));
		renewalDTO.setRenewalStatus(renewalIO.getRenewalStatus());

		renewalService.addRenewal(authDTO, renewalDTO);

		return ResponseIO.success("Renewal added successfully");
	}

	@PostMapping("/update/{code}")
	public ResponseIO<String> updateRenewal(@RequestHeader("authToken") String authToken, @PathVariable String code,
			@RequestBody RenewalIO renewalIO) {
		AuthDTO authDTO = authService.verification(authToken);

		RenewalDTO renewalDTO = new RenewalDTO();
		renewalDTO.setCode(code);

		PolicyDTO policyDTO = new PolicyDTO();
		policyDTO.setCode(renewalIO.getPolicy().getCode());
		renewalDTO.setPolicyDTO(policyDTO);

		renewalDTO.setRenewalDate(renewalIO.getRenewalDate());
		renewalDTO.setNewExpriyDate(renewalIO.getNewExpriyDate());
		renewalDTO.setRenewalStatus(renewalIO.getRenewalStatus());
		renewalDTO.setActiveFlag(renewalIO.getActiveFlag());

		renewalService.updateRenewal(authDTO, renewalDTO);

		return ResponseIO.success("Renewal updated successfully");
	}

	@GetMapping("/{code}")
	public ResponseIO<RenewalIO> getRenewalByCode(@RequestHeader("authToken") String authToken,
			@PathVariable String code) {
		authService.verification(authToken);
		RenewalDTO renewalDTO = renewalService.getRenewalByCode(code);

		RenewalIO renewalIO = new RenewalIO();
		renewalIO.setCode(renewalDTO.getCode());

		PolicyIO policyIO = new PolicyIO();

		policyIO.setCode(renewalDTO.getPolicyDTO().getCode());

		CustomerIO customerIO = new CustomerIO();
		customerIO.setCode(renewalDTO.getPolicyDTO().getCustomerDTO().getCode());
		customerIO.setName(renewalDTO.getPolicyDTO().getCustomerDTO().getName());
		customerIO.setCustomerDOB(renewalDTO.getPolicyDTO().getCustomerDTO().getCustomerDOB());
		customerIO.setCustomerGender(renewalDTO.getPolicyDTO().getCustomerDTO().getCustomerGender());
		customerIO.setCustomerAddress(renewalDTO.getPolicyDTO().getCustomerDTO().getCustomerAddress());
		customerIO.setCustomerNumber(renewalDTO.getPolicyDTO().getCustomerDTO().getCustomerNumber());
		customerIO.setCustomerLicenseNum(renewalDTO.getPolicyDTO().getCustomerDTO().getCustomerLicenseNum());
		policyIO.setCustomer(customerIO);

		VehicleIO vehicleIO = new VehicleIO();
		vehicleIO.setCode(renewalDTO.getPolicyDTO().getVehicleDTO().getCode());
		vehicleIO.setVehiclePlateNum(renewalDTO.getPolicyDTO().getVehicleDTO().getVehiclePlateNum());
		vehicleIO.setVehicleType(renewalDTO.getPolicyDTO().getVehicleDTO().getVehicleType());
		vehicleIO.setVehicleEnginNum(renewalDTO.getPolicyDTO().getVehicleDTO().getVehicleEnginNum());
		vehicleIO.setVehicleChasisNum(renewalDTO.getPolicyDTO().getVehicleDTO().getVehicleChasisNum());
		vehicleIO.setVehicleNumber(renewalDTO.getPolicyDTO().getVehicleDTO().getVehicleNumber());
		vehicleIO.setVehicleModelNum(renewalDTO.getPolicyDTO().getVehicleDTO().getVehicleModelNum());
		policyIO.setVehicle(vehicleIO);

		policyIO.setPolicyNumber(renewalDTO.getPolicyDTO().getPolicyNumber());
		policyIO.setStartDate(renewalDTO.getPolicyDTO().getStartDate());
		policyIO.setExpriyDate(renewalDTO.getPolicyDTO().getExpriyDate());
		policyIO.setPremiumAmount(renewalDTO.getPolicyDTO().getPremiumAmount());
		policyIO.setPaymentSchedule(renewalDTO.getPolicyDTO().getPaymentSchedule());
		policyIO.setTotalAmount(renewalDTO.getPolicyDTO().getTotalAmount());
		policyIO.setPolicyStatus(renewalDTO.getPolicyDTO().getPolicyStatus());
		policyIO.setPolicyDescription(renewalDTO.getPolicyDTO().getPolicyDescription());

		renewalIO.setPolicy(policyIO);
		renewalIO.setRenewalDate(renewalDTO.getRenewalDate());
		renewalIO.setNewExpriyDate(renewalDTO.getNewExpriyDate());
		renewalIO.setRenewalAmount(renewalDTO.getRenewalAmount());
		renewalIO.setRenewalStatus(renewalDTO.getRenewalStatus());

		return ResponseIO.success(renewalIO);
	}

	@GetMapping("/")
	public ResponseIO<List<RenewalIO>> getAllRenewals(@RequestHeader("authToken") String authToken) {
		authService.verification(authToken);
		List<RenewalDTO> listDTO = renewalService.getAllRenewal();
		List<RenewalIO> listIO = new ArrayList<>();

		for (RenewalDTO renewalDTO : listDTO) {
			RenewalIO renewalIO = new RenewalIO();
			renewalIO.setCode(renewalDTO.getCode());

			PolicyIO policyIO = new PolicyIO();

			policyIO.setCode(renewalDTO.getPolicyDTO().getCode());

			CustomerIO customerIO = new CustomerIO();
			customerIO.setCode(renewalDTO.getPolicyDTO().getCustomerDTO().getCode());
			customerIO.setName(renewalDTO.getPolicyDTO().getCustomerDTO().getName());
			customerIO.setCustomerDOB(renewalDTO.getPolicyDTO().getCustomerDTO().getCustomerDOB());
			customerIO.setCustomerGender(renewalDTO.getPolicyDTO().getCustomerDTO().getCustomerGender());
			customerIO.setCustomerAddress(renewalDTO.getPolicyDTO().getCustomerDTO().getCustomerAddress());
			customerIO.setCustomerNumber(renewalDTO.getPolicyDTO().getCustomerDTO().getCustomerNumber());
			customerIO.setCustomerLicenseNum(renewalDTO.getPolicyDTO().getCustomerDTO().getCustomerLicenseNum());
			policyIO.setCustomer(customerIO);

			VehicleIO vehicleIO = new VehicleIO();
			vehicleIO.setCode(renewalDTO.getPolicyDTO().getVehicleDTO().getCode());
			vehicleIO.setVehiclePlateNum(renewalDTO.getPolicyDTO().getVehicleDTO().getVehiclePlateNum());
			vehicleIO.setVehicleType(renewalDTO.getPolicyDTO().getVehicleDTO().getVehicleType());
			vehicleIO.setVehicleEnginNum(renewalDTO.getPolicyDTO().getVehicleDTO().getVehicleEnginNum());
			vehicleIO.setVehicleChasisNum(renewalDTO.getPolicyDTO().getVehicleDTO().getVehicleChasisNum());
			vehicleIO.setVehicleNumber(renewalDTO.getPolicyDTO().getVehicleDTO().getVehicleNumber());
			vehicleIO.setVehicleModelNum(renewalDTO.getPolicyDTO().getVehicleDTO().getVehicleModelNum());
			policyIO.setVehicle(vehicleIO);

			policyIO.setPolicyNumber(renewalDTO.getPolicyDTO().getPolicyNumber());
			policyIO.setStartDate(renewalDTO.getPolicyDTO().getStartDate());
			policyIO.setExpriyDate(renewalDTO.getPolicyDTO().getExpriyDate());
			policyIO.setPremiumAmount(renewalDTO.getPolicyDTO().getPremiumAmount());
			policyIO.setPaymentSchedule(renewalDTO.getPolicyDTO().getPaymentSchedule());
			policyIO.setTotalAmount(renewalDTO.getPolicyDTO().getTotalAmount());
			policyIO.setPolicyStatus(renewalDTO.getPolicyDTO().getPolicyStatus());
			policyIO.setPolicyDescription(renewalDTO.getPolicyDTO().getPolicyDescription());

			renewalIO.setPolicy(policyIO);
			renewalIO.setRenewalDate(renewalDTO.getRenewalDate());
			renewalIO.setNewExpriyDate(renewalDTO.getNewExpriyDate());
			renewalIO.setRenewalAmount(renewalDTO.getRenewalAmount());
			renewalIO.setRenewalStatus(renewalDTO.getRenewalStatus());
			listIO.add(renewalIO);
		}
		return ResponseIO.success(listIO);
	}

	@GetMapping("/getreciptbyrenewal/{code}")
	public ResponseIO<RenewalIO> getRenewalWithRecipts(@RequestHeader("authToken") String authToken,
			@PathVariable String code) {
		authService.verification(authToken);
		RenewalDTO renewalDTO = new RenewalDTO();
		renewalDTO.setCode(code);
		renewalDTO = renewalService.getReciptByRenewalCode(renewalDTO);

		RenewalIO renewalIO = new RenewalIO();
		renewalIO.setCode(renewalDTO.getCode());
		PolicyIO policyIO = new PolicyIO();

		policyIO.setCode(renewalDTO.getPolicyDTO().getCode());

		CustomerIO customerIO = new CustomerIO();
		customerIO.setCode(renewalDTO.getPolicyDTO().getCustomerDTO().getCode());
		customerIO.setName(renewalDTO.getPolicyDTO().getCustomerDTO().getName());
		customerIO.setCustomerDOB(renewalDTO.getPolicyDTO().getCustomerDTO().getCustomerDOB());
		customerIO.setCustomerGender(renewalDTO.getPolicyDTO().getCustomerDTO().getCustomerGender());
		customerIO.setCustomerAddress(renewalDTO.getPolicyDTO().getCustomerDTO().getCustomerAddress());
		customerIO.setCustomerNumber(renewalDTO.getPolicyDTO().getCustomerDTO().getCustomerNumber());
		customerIO.setCustomerLicenseNum(renewalDTO.getPolicyDTO().getCustomerDTO().getCustomerLicenseNum());
		policyIO.setCustomer(customerIO);

		VehicleIO vehicleIO = new VehicleIO();
		vehicleIO.setCode(renewalDTO.getPolicyDTO().getVehicleDTO().getCode());
		vehicleIO.setVehiclePlateNum(renewalDTO.getPolicyDTO().getVehicleDTO().getVehiclePlateNum());
		vehicleIO.setVehicleType(renewalDTO.getPolicyDTO().getVehicleDTO().getVehicleType());
		vehicleIO.setVehicleEnginNum(renewalDTO.getPolicyDTO().getVehicleDTO().getVehicleEnginNum());
		vehicleIO.setVehicleChasisNum(renewalDTO.getPolicyDTO().getVehicleDTO().getVehicleChasisNum());
		vehicleIO.setVehicleNumber(renewalDTO.getPolicyDTO().getVehicleDTO().getVehicleNumber());
		vehicleIO.setVehicleModelNum(renewalDTO.getPolicyDTO().getVehicleDTO().getVehicleModelNum());
		policyIO.setVehicle(vehicleIO);

		policyIO.setPolicyNumber(renewalDTO.getPolicyDTO().getPolicyNumber());
		policyIO.setStartDate(renewalDTO.getPolicyDTO().getStartDate());
		policyIO.setExpriyDate(renewalDTO.getPolicyDTO().getExpriyDate());
		policyIO.setPremiumAmount(renewalDTO.getPolicyDTO().getPremiumAmount());
		policyIO.setPaymentSchedule(renewalDTO.getPolicyDTO().getPaymentSchedule());
		policyIO.setTotalAmount(renewalDTO.getPolicyDTO().getTotalAmount());
		policyIO.setPolicyStatus(renewalDTO.getPolicyDTO().getPolicyStatus());
		policyIO.setPolicyDescription(renewalDTO.getPolicyDTO().getPolicyDescription());

		renewalIO.setPolicy(policyIO);
		renewalIO.setRenewalDate(renewalDTO.getRenewalDate());
		renewalIO.setNewExpriyDate(renewalDTO.getNewExpriyDate());
		renewalIO.setRenewalAmount(renewalDTO.getRenewalAmount());
		renewalIO.setRenewalStatus(renewalDTO.getRenewalStatus());

		List<ReciptIO> reciptIOList = new ArrayList<>();
		for (ReciptDTO reciptDTO : renewalDTO.getReciptDTO()) {
			ReciptIO reciptIO = new ReciptIO();
			reciptIO.setCode(reciptDTO.getCode());
			reciptIO.setReciptAmount(reciptDTO.getReciptAmount());
			reciptIO.setPenaltyAmount(reciptDTO.getPenaltyAmount());
			reciptIO.setReciptDate(reciptDTO.getReciptDate());
			reciptIO.setDueDate(reciptDTO.getDueDate());
			reciptIO.setReciptTotalAmount(reciptDTO.getReciptTotalAmount());
			reciptIO.setReciptStatus(reciptDTO.getReciptStatus());
			reciptIOList.add(reciptIO);
		}

		renewalIO.setRecipt(reciptIOList);
		return ResponseIO.success(renewalIO);
	}
}
