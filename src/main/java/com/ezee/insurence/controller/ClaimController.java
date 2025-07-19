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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ezee.insurence.controller.IO.ClaimIO;
import com.ezee.insurence.controller.IO.CustomerIO;
import com.ezee.insurence.controller.IO.IncidentIO;
import com.ezee.insurence.controller.IO.PolicyIO;
import com.ezee.insurence.controller.IO.ResponseIO;
import com.ezee.insurence.controller.IO.VehicleIO;
import com.ezee.insurence.dto.AuthDTO;
import com.ezee.insurence.dto.ClaimDTO;
import com.ezee.insurence.dto.CustomerDTO;
import com.ezee.insurence.dto.IncidentDTO;
import com.ezee.insurence.dto.PolicyDTO;
import com.ezee.insurence.service.AuthService;
import com.ezee.insurence.service.ClaimService;

@RestController
@RequestMapping("/claim")
public class ClaimController {
	@Autowired
	private AuthService authService;

	@Autowired
	private ClaimService claimService;

	@PostMapping("/add")
	public ResponseIO<String> addClaim(@RequestHeader("authToken") String authToken, @RequestBody ClaimIO claimIO) {
		AuthDTO authDTO = authService.verification(authToken);

		ClaimDTO claimDTO = new ClaimDTO();

		PolicyDTO policyDTO = new PolicyDTO();
		policyDTO.setCode(claimIO.getPolicy().getCode());
		claimDTO.setPolicyDTO(policyDTO);

		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setCode(claimIO.getCustomer().getCode());
		claimDTO.setCustomerDTO(customerDTO);

		IncidentDTO incidentDTO = new IncidentDTO();
		incidentDTO.setCode(claimIO.getIncident().getCode());
		claimDTO.setIncidentDTO(incidentDTO);

		claimDTO.getIncidentDTO().setCode(claimIO.getIncident().getCode());

		claimDTO.setClaimType(claimIO.getClaimType());
		claimDTO.setClaimDate(claimIO.getClaimDate());
		claimDTO.setClaimDescription(claimIO.getClaimDescription());
		claimDTO.setClaimAmount(claimIO.getClaimAmount());
		claimDTO.setClaimStatus(claimIO.getClaimStatus());

		claimService.addClaim(authDTO, claimDTO);
		return ResponseIO.success("Claim record inserted successfully");
	}

	@PostMapping("/update/{code}")
	public ResponseIO<String> updateClaim(@RequestHeader("authToken") String authToken, @PathVariable String code,
			@RequestBody ClaimIO claimIO) {
		AuthDTO authDTO = authService.verification(authToken);

		ClaimDTO claimDTO = new ClaimDTO();

		claimDTO.setCode(code);

		PolicyDTO policyDTO = new PolicyDTO();
		policyDTO.setCode(claimIO.getPolicy().getCode());
		claimDTO.setPolicyDTO(policyDTO);

		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setCode(claimIO.getPolicy().getCode());
		claimDTO.setCustomerDTO(customerDTO);

		IncidentDTO incidentDTO = new IncidentDTO();
		incidentDTO.setCode(claimIO.getIncident().getCode());
		claimDTO.setIncidentDTO(incidentDTO);

		claimDTO.getIncidentDTO().setCode(claimIO.getIncident().getCode());

		claimDTO.setClaimType(claimIO.getClaimType());
		claimDTO.setClaimDate(claimIO.getClaimDate());
		claimDTO.setClaimDescription(claimIO.getClaimDescription());
		claimDTO.setClaimAmount(claimIO.getClaimAmount());
		claimDTO.setClaimStatus(claimIO.getClaimStatus());

		claimService.updateClaim(authDTO, claimDTO);

		return ResponseIO.success("Claim record is update");
	}

	@GetMapping("/{code}")
	public ResponseIO<ClaimIO> getClaimByCode(@RequestHeader("authToken") String authToken, @PathVariable String code) {
		authService.verification(authToken);

		ClaimDTO claimDTO = claimService.getClaimByCode(code);
		ClaimIO claimIO = new ClaimIO();

		claimIO.setCode(claimDTO.getCode());
		PolicyIO policyIO = new PolicyIO();
		policyIO.setCode(claimDTO.getPolicyDTO().getCode());

		VehicleIO vehicleIO = new VehicleIO();
		vehicleIO.setCode(claimDTO.getPolicyDTO().getVehicleDTO().getCode());
		vehicleIO.setVehiclePlateNum(claimDTO.getPolicyDTO().getVehicleDTO().getVehiclePlateNum());
		vehicleIO.setVehicleType(claimDTO.getPolicyDTO().getVehicleDTO().getVehicleType());
		vehicleIO.setVehicleEnginNum(claimDTO.getPolicyDTO().getVehicleDTO().getVehicleEnginNum());
		vehicleIO.setVehicleChasisNum(claimDTO.getPolicyDTO().getVehicleDTO().getVehicleChasisNum());
		vehicleIO.setVehicleNumber(claimDTO.getPolicyDTO().getVehicleDTO().getVehicleNumber());
		vehicleIO.setVehicleModelNum(claimDTO.getPolicyDTO().getVehicleDTO().getVehicleModelNum());

		policyIO.setVehicle(vehicleIO);
		policyIO.setPolicyNumber(claimDTO.getPolicyDTO().getPolicyNumber());
		policyIO.setStartDate(claimDTO.getPolicyDTO().getStartDate());
		policyIO.setExpriyDate(claimDTO.getPolicyDTO().getExpriyDate());
		policyIO.setPremiumAmount(claimDTO.getPolicyDTO().getPremiumAmount());
		policyIO.setPaymentSchedule(claimDTO.getPolicyDTO().getPaymentSchedule());
		policyIO.setTotalAmount(claimDTO.getPolicyDTO().getTotalAmount());
		policyIO.setPolicyStatus(claimDTO.getPolicyDTO().getPolicyStatus());
		claimIO.setPolicy(policyIO);

		CustomerIO customerIO = new CustomerIO();
		customerIO.setCode(claimDTO.getCustomerDTO().getCode());
		customerIO.setName(claimDTO.getCustomerDTO().getName());
		customerIO.setCustomerDOB(claimDTO.getCustomerDTO().getCustomerDOB());
		customerIO.setCustomerGender(claimDTO.getCustomerDTO().getCustomerGender());
		customerIO.setCustomerAddress(claimDTO.getCustomerDTO().getCustomerAddress());
		customerIO.setCustomerNumber(claimDTO.getCustomerDTO().getCustomerNumber());
		customerIO.setCustomerEmail(claimDTO.getCustomerDTO().getCustomerEmail());
		customerIO.setCustomerLicenseNum(claimDTO.getCustomerDTO().getCustomerLicenseNum());
		claimIO.setCustomer(customerIO);

		IncidentIO incidentIO = new IncidentIO();
		incidentIO.setCode(claimDTO.getIncidentDTO().getCode());
		incidentIO.setIncidentDate(claimDTO.getIncidentDTO().getIncidentDate());
		incidentIO.setIncidentType(claimDTO.getIncidentDTO().getIncidentType());
		incidentIO.setIncidentInspector(claimDTO.getIncidentDTO().getIncidentInspector());
		incidentIO.setIncidentCost(claimDTO.getIncidentDTO().getIncidentCost());
		incidentIO.setIncidentDescription(claimDTO.getIncidentDTO().getIncidentDescription());
		incidentIO.setIncidentStatus(claimDTO.getIncidentDTO().getIncidentStatus());
		claimIO.setIncident(incidentIO);

		claimIO.setClaimType(claimDTO.getClaimType());
		claimIO.setClaimDate(claimDTO.getClaimDate());
		claimIO.setClaimDescription(claimDTO.getClaimDescription());
		claimIO.setClaimAmount(claimDTO.getClaimAmount());
		claimIO.setClaimStatus(claimDTO.getClaimStatus());

		return ResponseIO.success(claimIO);
	}

	@GetMapping("/")
	public ResponseIO<List<ClaimIO>> getAllClaims(@RequestHeader("authToken") String authToken) {
		authService.verification(authToken);

		List<ClaimDTO> listDTO = claimService.getAllClaim();
		List<ClaimIO> listIO = new ArrayList<>();

		for (ClaimDTO claimDTO : listDTO) {
			ClaimIO claimIO = new ClaimIO();

			claimIO.setCode(claimDTO.getCode());
			PolicyIO policyIO = new PolicyIO();
			policyIO.setCode(claimDTO.getPolicyDTO().getCode());

			VehicleIO vehicleIO = new VehicleIO();
			vehicleIO.setCode(claimDTO.getPolicyDTO().getVehicleDTO().getCode());
			vehicleIO.setVehiclePlateNum(claimDTO.getPolicyDTO().getVehicleDTO().getVehiclePlateNum());
			vehicleIO.setVehicleType(claimDTO.getPolicyDTO().getVehicleDTO().getVehicleType());
			vehicleIO.setVehicleEnginNum(claimDTO.getPolicyDTO().getVehicleDTO().getVehicleEnginNum());
			vehicleIO.setVehicleChasisNum(claimDTO.getPolicyDTO().getVehicleDTO().getVehicleChasisNum());
			vehicleIO.setVehicleNumber(claimDTO.getPolicyDTO().getVehicleDTO().getVehicleNumber());
			vehicleIO.setVehicleModelNum(claimDTO.getPolicyDTO().getVehicleDTO().getVehicleModelNum());

			policyIO.setVehicle(vehicleIO);
			policyIO.setPolicyNumber(claimDTO.getPolicyDTO().getPolicyNumber());
			policyIO.setStartDate(claimDTO.getPolicyDTO().getStartDate());
			policyIO.setExpriyDate(claimDTO.getPolicyDTO().getExpriyDate());
			policyIO.setPremiumAmount(claimDTO.getPolicyDTO().getPremiumAmount());
			policyIO.setPaymentSchedule(claimDTO.getPolicyDTO().getPaymentSchedule());
			policyIO.setTotalAmount(claimDTO.getPolicyDTO().getTotalAmount());
			policyIO.setPolicyStatus(claimDTO.getPolicyDTO().getPolicyStatus());
			claimIO.setPolicy(policyIO);

			CustomerIO customerIO = new CustomerIO();
			customerIO.setCode(claimDTO.getCustomerDTO().getCode());
			customerIO.setName(claimDTO.getCustomerDTO().getName());
			customerIO.setCustomerDOB(claimDTO.getCustomerDTO().getCustomerDOB());
			customerIO.setCustomerGender(claimDTO.getCustomerDTO().getCustomerGender());
			customerIO.setCustomerAddress(claimDTO.getCustomerDTO().getCustomerAddress());
			customerIO.setCustomerNumber(claimDTO.getCustomerDTO().getCustomerNumber());
			customerIO.setCustomerEmail(claimDTO.getCustomerDTO().getCustomerEmail());
			customerIO.setCustomerLicenseNum(claimDTO.getCustomerDTO().getCustomerLicenseNum());
			claimIO.setCustomer(customerIO);

			IncidentIO incidentIO = new IncidentIO();
			incidentIO.setCode(claimDTO.getIncidentDTO().getCode());
			incidentIO.setIncidentDate(claimDTO.getIncidentDTO().getIncidentDate());
			incidentIO.setIncidentType(claimDTO.getIncidentDTO().getIncidentType());
			incidentIO.setIncidentInspector(claimDTO.getIncidentDTO().getIncidentInspector());
			incidentIO.setIncidentCost(claimDTO.getIncidentDTO().getIncidentCost());
			incidentIO.setIncidentDescription(claimDTO.getIncidentDTO().getIncidentDescription());
			incidentIO.setIncidentStatus(claimDTO.getIncidentDTO().getIncidentStatus());
			claimIO.setIncident(incidentIO);

			claimIO.setClaimType(claimDTO.getClaimType());
			claimIO.setClaimDate(claimDTO.getClaimDate());
			claimIO.setClaimDescription(claimDTO.getClaimDescription());
			claimIO.setClaimAmount(claimDTO.getClaimAmount());
			claimIO.setClaimStatus(claimDTO.getClaimStatus());
			listIO.add(claimIO);
		}

		return ResponseIO.success(listIO);
	}

	@GetMapping("/getactiveclaim")
	public ResponseIO<List<ClaimIO>> getActiveClaims(@RequestHeader("authToken") String authToken,
			@RequestParam String claimStatus) {
		authService.verification(authToken);
		ClaimDTO cDTO = new ClaimDTO();
		cDTO.setClaimStatus(claimStatus.toUpperCase());

		List<ClaimDTO> listDTO = claimService.getActiveClaim(cDTO);
		List<ClaimIO> listIO = new ArrayList<>();

		for (ClaimDTO claimDTO : listDTO) {
			ClaimIO claimIO = new ClaimIO();

			claimIO.setCode(claimDTO.getCode());
			PolicyIO policyIO = new PolicyIO();
			policyIO.setCode(claimDTO.getPolicyDTO().getCode());

			VehicleIO vehicleIO = new VehicleIO();
			vehicleIO.setCode(claimDTO.getPolicyDTO().getVehicleDTO().getCode());
			vehicleIO.setVehiclePlateNum(claimDTO.getPolicyDTO().getVehicleDTO().getVehiclePlateNum());
			vehicleIO.setVehicleType(claimDTO.getPolicyDTO().getVehicleDTO().getVehicleType());
			vehicleIO.setVehicleEnginNum(claimDTO.getPolicyDTO().getVehicleDTO().getVehicleEnginNum());
			vehicleIO.setVehicleChasisNum(claimDTO.getPolicyDTO().getVehicleDTO().getVehicleChasisNum());
			vehicleIO.setVehicleNumber(claimDTO.getPolicyDTO().getVehicleDTO().getVehicleNumber());
			vehicleIO.setVehicleModelNum(claimDTO.getPolicyDTO().getVehicleDTO().getVehicleModelNum());

			policyIO.setVehicle(vehicleIO);
			policyIO.setPolicyNumber(claimDTO.getPolicyDTO().getPolicyNumber());
			policyIO.setStartDate(claimDTO.getPolicyDTO().getStartDate());
			policyIO.setExpriyDate(claimDTO.getPolicyDTO().getExpriyDate());
			policyIO.setPremiumAmount(claimDTO.getPolicyDTO().getPremiumAmount());
			policyIO.setPaymentSchedule(claimDTO.getPolicyDTO().getPaymentSchedule());
			policyIO.setTotalAmount(claimDTO.getPolicyDTO().getTotalAmount());
			policyIO.setPolicyStatus(claimDTO.getPolicyDTO().getPolicyStatus());
			claimIO.setPolicy(policyIO);

			CustomerIO customerIO = new CustomerIO();
			customerIO.setCode(claimDTO.getCustomerDTO().getCode());
			customerIO.setName(claimDTO.getCustomerDTO().getName());
			customerIO.setCustomerDOB(claimDTO.getCustomerDTO().getCustomerDOB());
			customerIO.setCustomerGender(claimDTO.getCustomerDTO().getCustomerGender());
			customerIO.setCustomerAddress(claimDTO.getCustomerDTO().getCustomerAddress());
			customerIO.setCustomerNumber(claimDTO.getCustomerDTO().getCustomerNumber());
			customerIO.setCustomerEmail(claimDTO.getCustomerDTO().getCustomerEmail());
			customerIO.setCustomerLicenseNum(claimDTO.getCustomerDTO().getCustomerLicenseNum());
			claimIO.setCustomer(customerIO);

			IncidentIO incidentIO = new IncidentIO();
			incidentIO.setCode(claimDTO.getIncidentDTO().getCode());
			incidentIO.setIncidentDate(claimDTO.getIncidentDTO().getIncidentDate());
			incidentIO.setIncidentType(claimDTO.getIncidentDTO().getIncidentType());
			incidentIO.setIncidentInspector(claimDTO.getIncidentDTO().getIncidentInspector());
			incidentIO.setIncidentCost(claimDTO.getIncidentDTO().getIncidentCost());
			incidentIO.setIncidentDescription(claimDTO.getIncidentDTO().getIncidentDescription());
			incidentIO.setIncidentStatus(claimDTO.getIncidentDTO().getIncidentStatus());
			claimIO.setIncident(incidentIO);

			claimIO.setClaimType(claimDTO.getClaimType());
			claimIO.setClaimDate(claimDTO.getClaimDate());
			claimIO.setClaimDescription(claimDTO.getClaimDescription());
			claimIO.setClaimAmount(claimDTO.getClaimAmount());
			claimIO.setClaimStatus(claimDTO.getClaimStatus());
			listIO.add(claimIO);
		}

		return ResponseIO.success(listIO);
	}
}
