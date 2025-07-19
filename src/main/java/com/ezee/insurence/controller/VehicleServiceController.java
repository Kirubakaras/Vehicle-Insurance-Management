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
import com.ezee.insurence.dto.ClaimDTO;
import com.ezee.insurence.dto.PolicyDTO;
import com.ezee.insurence.dto.VehicleDTO;
import com.ezee.insurence.dto.VehicleServiceDTO;
import com.ezee.insurence.service.AuthService;
import com.ezee.insurence.service.VehicleServiceService;

@RestController
@RequestMapping("/vehicleservice")
public class VehicleServiceController {

	@Autowired
	private AuthService authService;

	@Autowired
	private VehicleServiceService vehicleService;

	@PostMapping("/add")
	public ResponseIO<String> addVehicleService(@RequestHeader("authToken") String authToken,
			@RequestBody VehicleServiceIO serviceIO) {
		AuthDTO authDTO = authService.verification(authToken);

		VehicleServiceDTO serviceDTO = new VehicleServiceDTO();

		VehicleDTO vehicleDTO = new VehicleDTO();
		vehicleDTO.setCode(serviceIO.getVehicle().getCode());
		serviceDTO.setVehicleDTO(vehicleDTO);

		ClaimDTO claimDTO = new ClaimDTO();
		claimDTO.setCode(serviceIO.getClaim().getCode());
		serviceDTO.setClaimDTO(claimDTO);

		serviceDTO.setServiceDate(serviceIO.getServiceDate());
		serviceDTO.setServiceDescription(serviceIO.getServiceDescription());
		serviceDTO.setServiceCost(serviceIO.getServiceCost());
		serviceDTO.setServiceStatus(serviceIO.getServiceStatus());

		vehicleService.addVehicleService(authDTO, serviceDTO);
		return ResponseIO.success("Vehicle service record inserted successfully");
	}

	@PostMapping("/update/{code}")
	public ResponseIO<String> updateVehicleService(@RequestHeader("authToken") String authToken,
			@PathVariable String code, @RequestBody VehicleServiceIO serviceIO) {
		AuthDTO authDTO = authService.verification(authToken);

		VehicleServiceDTO serviceDTO = new VehicleServiceDTO();

		serviceDTO.setCode(code);

		VehicleDTO vehicleDTO = new VehicleDTO();
		vehicleDTO.setCode(serviceIO.getVehicle().getCode());
		serviceDTO.setVehicleDTO(vehicleDTO);

		ClaimDTO claimDTO = new ClaimDTO();
		claimDTO.setCode(serviceIO.getClaim().getCode());
		serviceDTO.setClaimDTO(claimDTO);

		serviceDTO.setServiceDate(serviceIO.getServiceDate());
		serviceDTO.setServiceDescription(serviceIO.getServiceDescription());
		serviceDTO.setServiceCost(serviceIO.getServiceCost());
		serviceDTO.setServiceStatus(serviceIO.getServiceStatus());

		vehicleService.updateVehicleService(authDTO, serviceDTO);
		return ResponseIO.success("Vehicle service record updated successfully");
	}

	@GetMapping("/{code}")
	public ResponseIO<VehicleServiceIO> getVehicleServiceByCode(@RequestHeader("authToken") String authToken,
			@PathVariable String code) {
		authService.verification(authToken);
		VehicleServiceDTO serviceDTO = vehicleService.getVehicleServiceDTO(code);

		VehicleServiceIO serviceIO = new VehicleServiceIO();
		serviceIO.setCode(serviceDTO.getCode());

		VehicleIO vehicleIO = new VehicleIO();

		vehicleIO.setCode(serviceDTO.getVehicleDTO().getCode());
		vehicleIO.setVehiclePlateNum(serviceDTO.getVehicleDTO().getVehiclePlateNum());
		vehicleIO.setVehicleType(serviceDTO.getVehicleDTO().getVehicleType());
		vehicleIO.setVehicleEnginNum(serviceDTO.getVehicleDTO().getVehicleEnginNum());
		vehicleIO.setVehicleChasisNum(serviceDTO.getVehicleDTO().getVehicleChasisNum());
		vehicleIO.setVehicleNumber(serviceDTO.getVehicleDTO().getVehicleNumber());
		vehicleIO.setVehicleModelNum(serviceDTO.getVehicleDTO().getVehicleModelNum());
		serviceIO.setVehicle(vehicleIO);

		ClaimIO claimIO = new ClaimIO();
		claimIO.setCode(serviceDTO.getClaimDTO().getCode());

		PolicyDTO policyDTO = serviceDTO.getClaimDTO().getPolicyDTO();

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
		claimIO.setPolicy(policyIO);

		CustomerIO customerIO = new CustomerIO();
		customerIO.setCode(serviceDTO.getClaimDTO().getCustomerDTO().getCode());
		customerIO.setName(serviceDTO.getClaimDTO().getCustomerDTO().getName());
		customerIO.setCustomerDOB(serviceDTO.getClaimDTO().getCustomerDTO().getCustomerDOB());
		customerIO.setCustomerGender(serviceDTO.getClaimDTO().getCustomerDTO().getCustomerGender());
		customerIO.setCustomerAddress(serviceDTO.getClaimDTO().getCustomerDTO().getCustomerAddress());
		customerIO.setCustomerNumber(serviceDTO.getClaimDTO().getCustomerDTO().getCustomerNumber());
		customerIO.setCustomerEmail(serviceDTO.getClaimDTO().getCustomerDTO().getCustomerEmail());
		customerIO.setCustomerLicenseNum(serviceDTO.getClaimDTO().getCustomerDTO().getCustomerLicenseNum());
		claimIO.setCustomer(customerIO);

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

		return ResponseIO.success(serviceIO);
	}

	@GetMapping("/")
	public ResponseIO<List<VehicleServiceIO>> getAllVehicleService(@RequestHeader("authToken") String authToken) {
		authService.verification(authToken);
		List<VehicleServiceDTO> listDTO = vehicleService.getAllVehicleService();
		List<VehicleServiceIO> listIO = new ArrayList<>();

		for (VehicleServiceDTO serviceDTO : listDTO) {
			VehicleServiceIO serviceIO = new VehicleServiceIO();
			serviceIO.setCode(serviceDTO.getCode());

			VehicleIO vehicleIO = new VehicleIO();

			vehicleIO.setCode(serviceDTO.getVehicleDTO().getCode());
			vehicleIO.setVehiclePlateNum(serviceDTO.getVehicleDTO().getVehiclePlateNum());
			vehicleIO.setVehicleType(serviceDTO.getVehicleDTO().getVehicleType());
			vehicleIO.setVehicleEnginNum(serviceDTO.getVehicleDTO().getVehicleEnginNum());
			vehicleIO.setVehicleChasisNum(serviceDTO.getVehicleDTO().getVehicleChasisNum());
			vehicleIO.setVehicleNumber(serviceDTO.getVehicleDTO().getVehicleNumber());
			vehicleIO.setVehicleModelNum(serviceDTO.getVehicleDTO().getVehicleModelNum());
			serviceIO.setVehicle(vehicleIO);

			ClaimIO claimIO = new ClaimIO();
			claimIO.setCode(serviceDTO.getClaimDTO().getCode());

			PolicyDTO policyDTO = serviceDTO.getClaimDTO().getPolicyDTO();

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
			claimIO.setPolicy(policyIO);

			CustomerIO customerIO = new CustomerIO();
			customerIO.setCode(serviceDTO.getClaimDTO().getCustomerDTO().getCode());
			customerIO.setName(serviceDTO.getClaimDTO().getCustomerDTO().getName());
			customerIO.setCustomerDOB(serviceDTO.getClaimDTO().getCustomerDTO().getCustomerDOB());
			customerIO.setCustomerGender(serviceDTO.getClaimDTO().getCustomerDTO().getCustomerGender());
			customerIO.setCustomerAddress(serviceDTO.getClaimDTO().getCustomerDTO().getCustomerAddress());
			customerIO.setCustomerNumber(serviceDTO.getClaimDTO().getCustomerDTO().getCustomerNumber());
			customerIO.setCustomerEmail(serviceDTO.getClaimDTO().getCustomerDTO().getCustomerEmail());
			customerIO.setCustomerLicenseNum(serviceDTO.getClaimDTO().getCustomerDTO().getCustomerLicenseNum());
			claimIO.setCustomer(customerIO);

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

			listIO.add(serviceIO);
		}

		return ResponseIO.success(listIO);
	}

}
