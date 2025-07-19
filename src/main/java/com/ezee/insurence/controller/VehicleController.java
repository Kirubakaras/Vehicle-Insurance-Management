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

import com.ezee.insurence.controller.IO.ClaimIO;
import com.ezee.insurence.controller.IO.CustomerIO;
import com.ezee.insurence.controller.IO.IncidentIO;
import com.ezee.insurence.controller.IO.PolicyIO;
import com.ezee.insurence.controller.IO.ResponseIO;
import com.ezee.insurence.controller.IO.VehicleIO;
import com.ezee.insurence.controller.IO.VehicleServiceIO;
import com.ezee.insurence.dto.AuthDTO;
import com.ezee.insurence.dto.CustomerDTO;
import com.ezee.insurence.dto.PolicyDTO;
import com.ezee.insurence.dto.VehicleDTO;
import com.ezee.insurence.dto.VehicleServiceDTO;
import com.ezee.insurence.service.AuthService;
import com.ezee.insurence.service.VehicleServcie;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {
	@Autowired
	AuthService authService;

	@Autowired
	VehicleServcie vehicleService;

	@PostMapping("/add")
	public ResponseIO<String> addVehicle(@RequestHeader("authToken") String authToken,
			@RequestBody VehicleIO vehicleIO) {
		AuthDTO authDTO = authService.verification(authToken);

		VehicleDTO vehicleDTO = new VehicleDTO();

		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setCode(vehicleIO.getCustomer().getCode());
		vehicleDTO.setCustomerDTO(customerDTO);

		vehicleDTO.setVehiclePlateNum(vehicleIO.getVehiclePlateNum());
		vehicleDTO.setVehicleType(vehicleIO.getVehicleType());
		vehicleDTO.setVehicleEnginNum(vehicleIO.getVehicleEnginNum());
		vehicleDTO.setVehicleChasisNum(vehicleIO.getVehicleChasisNum());
		vehicleDTO.setVehicleNumber(vehicleIO.getVehicleNumber());
		vehicleDTO.setVehicleModelNum(vehicleIO.getVehicleModelNum());
		vehicleService.addVehicle(authDTO, vehicleDTO);
		return ResponseIO.success("Insert the vehicle record");
	}

	@PostMapping("/update/{code}")
	public ResponseIO<String> updateVehicle(@RequestHeader("authToken") String authToken, @PathVariable String code,
			@RequestBody VehicleIO vehicleIO) {
		AuthDTO authDTO = authService.verification(authToken);

		VehicleDTO vehicleDTO = new VehicleDTO();

		vehicleDTO.setCode(code);

		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setCode(vehicleIO.getCustomer().getCode());
		vehicleDTO.setCustomerDTO(customerDTO);

		vehicleDTO.setVehiclePlateNum(vehicleIO.getVehiclePlateNum());
		vehicleDTO.setVehicleType(vehicleIO.getVehicleType());
		vehicleDTO.setVehicleEnginNum(vehicleIO.getVehicleEnginNum());
		vehicleDTO.setVehicleChasisNum(vehicleIO.getVehicleChasisNum());
		vehicleDTO.setVehicleNumber(vehicleIO.getVehicleNumber());
		vehicleDTO.setVehicleModelNum(vehicleIO.getVehicleModelNum());
		vehicleService.updateVehicle(authDTO, vehicleDTO);
		return ResponseIO.success("Update the vehicle record");
	}

	@GetMapping("/{code}")
	public ResponseIO<VehicleIO> getVehicleDTO(@RequestHeader("authToken") String authToken,
			@PathVariable String code) {
		authService.verification(authToken);

		VehicleIO vehicleIO = new VehicleIO();
		VehicleDTO vehicleDTO = vehicleService.getVehicleDTO(code);

		vehicleIO.setCode(vehicleDTO.getCode());

		CustomerIO customerIO = new CustomerIO();
		customerIO.setCode(vehicleDTO.getCustomerDTO().getCode());
		customerIO.setName(vehicleDTO.getCustomerDTO().getName());
		customerIO.setCustomerDOB(vehicleDTO.getCustomerDTO().getCustomerDOB());
		customerIO.setCustomerGender(vehicleDTO.getCustomerDTO().getCustomerGender());
		customerIO.setCustomerAddress(vehicleDTO.getCustomerDTO().getCustomerAddress());
		customerIO.setCustomerNumber(vehicleDTO.getCustomerDTO().getCustomerNumber());
		customerIO.setCustomerEmail(vehicleDTO.getCustomerDTO().getCustomerEmail());
		customerIO.setCustomerLicenseNum(vehicleDTO.getCustomerDTO().getCustomerLicenseNum());
		vehicleIO.setCustomer(customerIO);

		vehicleIO.setVehiclePlateNum(vehicleDTO.getVehiclePlateNum());
		vehicleIO.setVehicleType(vehicleDTO.getVehicleType());
		vehicleIO.setVehicleEnginNum(vehicleDTO.getVehicleEnginNum());
		vehicleIO.setVehicleChasisNum(vehicleDTO.getVehicleChasisNum());
		vehicleIO.setVehicleNumber(vehicleDTO.getVehicleNumber());
		vehicleIO.setVehicleModelNum(vehicleDTO.getVehicleModelNum());

		return ResponseIO.success(vehicleIO);
	}

	@GetMapping("/")
	public ResponseIO<List<VehicleIO>> getAllVehicle(@RequestHeader("authToken") String authToken) {
		authService.verification(authToken);
		List<VehicleIO> listIO = new ArrayList<VehicleIO>();

		List<VehicleDTO> listDTO = vehicleService.getAllvehicle();

		for (VehicleDTO vehicleDTO : listDTO) {

			VehicleIO vehicleIO = new VehicleIO();
			vehicleIO.setCode(vehicleDTO.getCode());

			CustomerIO customerIO = new CustomerIO();
			customerIO.setCode(vehicleDTO.getCustomerDTO().getCode());
			customerIO.setName(vehicleDTO.getCustomerDTO().getName());
			customerIO.setCustomerDOB(vehicleDTO.getCustomerDTO().getCustomerDOB());
			customerIO.setCustomerGender(vehicleDTO.getCustomerDTO().getCustomerGender());
			customerIO.setCustomerAddress(vehicleDTO.getCustomerDTO().getCustomerAddress());
			customerIO.setCustomerNumber(vehicleDTO.getCustomerDTO().getCustomerNumber());
			customerIO.setCustomerEmail(vehicleDTO.getCustomerDTO().getCustomerEmail());
			customerIO.setCustomerLicenseNum(vehicleDTO.getCustomerDTO().getCustomerLicenseNum());
			vehicleIO.setCustomer(customerIO);

			vehicleIO.setVehiclePlateNum(vehicleDTO.getVehiclePlateNum());
			vehicleIO.setVehicleType(vehicleDTO.getVehicleType());
			vehicleIO.setVehicleEnginNum(vehicleDTO.getVehicleEnginNum());
			vehicleIO.setVehicleChasisNum(vehicleDTO.getVehicleChasisNum());
			vehicleIO.setVehicleNumber(vehicleDTO.getVehicleNumber());
			vehicleIO.setVehicleModelNum(vehicleDTO.getVehicleModelNum());
			listIO.add(vehicleIO);
		}
		return ResponseIO.success(listIO);
	}

	@GetMapping("/getpolicybyvehicle/{code}")
	public ResponseIO<List<VehicleIO>> getPolicyByVehicle(@RequestHeader("authToken") String authToken,
			@PathVariable String code) {
		authService.verification(authToken);
		List<VehicleIO> listIO = new ArrayList<>();

		VehicleDTO vehicleDTO = new VehicleDTO();
		vehicleDTO.setCode(code);
		vehicleDTO = vehicleService.getPolicyByVehicle(vehicleDTO);

		VehicleIO vehicleIO = new VehicleIO();
		vehicleIO.setCode(vehicleDTO.getCode());

		CustomerDTO customer = vehicleDTO.getCustomerDTO();
		CustomerIO customerIO = new CustomerIO();
		customerIO.setCode(customer.getCode());
		customerIO.setName(customer.getName());
		customerIO.setCustomerDOB(customer.getCustomerDOB());
		customerIO.setCustomerGender(customer.getCustomerGender());
		customerIO.setCustomerAddress(customer.getCustomerAddress());
		customerIO.setCustomerNumber(customer.getCustomerNumber());
		customerIO.setCustomerEmail(customer.getCustomerEmail());
		customerIO.setCustomerLicenseNum(customer.getCustomerLicenseNum());
		vehicleIO.setCustomer(customerIO);

		vehicleIO.setVehiclePlateNum(vehicleDTO.getVehiclePlateNum());
		vehicleIO.setVehicleType(vehicleDTO.getVehicleType());
		vehicleIO.setVehicleEnginNum(vehicleDTO.getVehicleEnginNum());
		vehicleIO.setVehicleChasisNum(vehicleDTO.getVehicleChasisNum());
		vehicleIO.setVehicleNumber(vehicleDTO.getVehicleNumber());
		vehicleIO.setVehicleModelNum(vehicleDTO.getVehicleModelNum());

		List<PolicyIO> listPolicyIO = new ArrayList<>();

		for (PolicyDTO policyDTO : vehicleDTO.getPolicyDTO()) {
			PolicyIO policyIO = new PolicyIO();

			policyIO.setCode(policyDTO.getCode());

			policyIO.setPolicyNumber(policyDTO.getPolicyNumber());
			policyIO.setStartDate(policyDTO.getStartDate());
			policyIO.setExpriyDate(policyDTO.getExpriyDate());
			policyIO.setPremiumAmount(policyDTO.getPremiumAmount());
			policyIO.setPaymentSchedule(policyDTO.getPaymentSchedule());
			policyIO.setTotalAmount(policyDTO.getTotalAmount());
			policyIO.setPolicyStatus(policyDTO.getPolicyStatus());
			policyIO.setPolicyDescription(policyDTO.getPolicyDescription());
			listPolicyIO.add(policyIO);
		}
		vehicleIO.setPolicy(listPolicyIO);
		listIO.add(vehicleIO);
		return ResponseIO.success(listIO);
	}

	@GetMapping("/getvehicleservicebyvehicle/{code}")
	public ResponseIO<List<VehicleIO>> getVehicleServiceByVehicle(@RequestHeader("authToken") String authToken,
			@PathVariable String code) {
		authService.verification(authToken);
		List<VehicleIO> listIO = new ArrayList<>();

		VehicleDTO vehicleDTO = new VehicleDTO();
		vehicleDTO.setCode(code);
		vehicleDTO = vehicleService.getVehicleServiceByVehicle(vehicleDTO);

		VehicleIO vehicleIO = new VehicleIO();
		vehicleIO.setCode(vehicleDTO.getCode());

		CustomerIO customerIO = new CustomerIO();
		customerIO.setCode(vehicleDTO.getCustomerDTO().getCode());
		customerIO.setName(vehicleDTO.getCustomerDTO().getName());
		customerIO.setCustomerDOB(vehicleDTO.getCustomerDTO().getCustomerDOB());
		customerIO.setCustomerGender(vehicleDTO.getCustomerDTO().getCustomerGender());
		customerIO.setCustomerAddress(vehicleDTO.getCustomerDTO().getCustomerAddress());
		customerIO.setCustomerNumber(vehicleDTO.getCustomerDTO().getCustomerNumber());
		customerIO.setCustomerEmail(vehicleDTO.getCustomerDTO().getCustomerEmail());
		customerIO.setCustomerLicenseNum(vehicleDTO.getCustomerDTO().getCustomerLicenseNum());
		vehicleIO.setCustomer(customerIO);

		vehicleIO.setVehiclePlateNum(vehicleDTO.getVehiclePlateNum());
		vehicleIO.setVehicleType(vehicleDTO.getVehicleType());
		vehicleIO.setVehicleEnginNum(vehicleDTO.getVehicleEnginNum());
		vehicleIO.setVehicleChasisNum(vehicleDTO.getVehicleChasisNum());
		vehicleIO.setVehicleNumber(vehicleDTO.getVehicleNumber());
		vehicleIO.setVehicleModelNum(vehicleDTO.getVehicleModelNum());

		List<VehicleServiceIO> listVehicleServiceIO = new ArrayList<>();

		for (VehicleServiceDTO serviceDTO : vehicleDTO.getVehicleServcieDTO()) {
			VehicleServiceIO serviceIO = new VehicleServiceIO();
			serviceIO.setCode(serviceDTO.getCode());

			ClaimIO claimIO = new ClaimIO();
			claimIO.setCode(serviceDTO.getClaimDTO().getCode());

			IncidentIO incidentIO = new IncidentIO();
			incidentIO.setCode(serviceDTO.getClaimDTO().getIncidentDTO().getCode());
			incidentIO.setIncidentDate(serviceDTO.getClaimDTO().getIncidentDTO().getIncidentDate());
			incidentIO.setIncidentType(serviceDTO.getClaimDTO().getIncidentDTO().getIncidentType());
			incidentIO.setIncidentInspector(serviceDTO.getClaimDTO().getIncidentDTO().getIncidentInspector());
			incidentIO.setIncidentCost(serviceDTO.getClaimDTO().getIncidentDTO().getIncidentCost());
			incidentIO.setIncidentDescription(serviceDTO.getClaimDTO().getIncidentDTO().getIncidentDescription());
			incidentIO.setIncidentStatus(serviceDTO.getClaimDTO().getIncidentDTO().getIncidentStatus());
			claimIO.setIncident(incidentIO);

			claimIO.setClaimType(serviceDTO.getClaimDTO().getClaimType());
			claimIO.setClaimDate(serviceDTO.getClaimDTO().getClaimDate());
			claimIO.setClaimDescription(serviceDTO.getClaimDTO().getClaimDescription());
			claimIO.setClaimAmount(serviceDTO.getClaimDTO().getClaimAmount());
			claimIO.setClaimStatus(serviceDTO.getClaimDTO().getClaimStatus());
			serviceIO.setClaim(claimIO);

			serviceIO.setServiceDate(serviceDTO.getServiceDate());
			serviceIO.setServiceDescription(serviceDTO.getServiceDescription());
			serviceIO.setServiceCost(serviceDTO.getServiceCost());
			serviceIO.setServiceStatus(serviceDTO.getServiceStatus());
			listVehicleServiceIO.add(serviceIO);
		}
		vehicleIO.setVehicleServcie(listVehicleServiceIO);
		listIO.add(vehicleIO);
		return ResponseIO.success(listIO);
	}

}
