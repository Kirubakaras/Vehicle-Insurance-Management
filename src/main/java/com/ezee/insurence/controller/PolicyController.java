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
import com.ezee.insurence.controller.IO.ReciptIO;
import com.ezee.insurence.controller.IO.RenewalIO;
import com.ezee.insurence.controller.IO.ResponseIO;
import com.ezee.insurence.controller.IO.VehicleIO;
import com.ezee.insurence.dto.AuthDTO;
import com.ezee.insurence.dto.ClaimDTO;
import com.ezee.insurence.dto.CustomerDTO;
import com.ezee.insurence.dto.PolicyDTO;
import com.ezee.insurence.dto.ReciptDTO;
import com.ezee.insurence.dto.RenewalDTO;
import com.ezee.insurence.dto.VehicleDTO;
import com.ezee.insurence.service.AuthService;
import com.ezee.insurence.service.PolicyService;

@RestController
@RequestMapping("/policy")
public class PolicyController {

	@Autowired
	AuthService authService;

	@Autowired
	PolicyService policyService;

	@PostMapping("/add")
	public ResponseIO<String> addPolicy(@RequestHeader("authToken") String authToken, @RequestBody PolicyIO policyIO) {
		AuthDTO authDTO = authService.verification(authToken);

		PolicyDTO policyDTO = new PolicyDTO();

		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setCode(policyIO.getCustomer().getCode());
		policyDTO.setCustomerDTO(customerDTO);

		VehicleDTO vehicleDTO = new VehicleDTO();
		vehicleDTO.setCode(policyIO.getVehicle().getCode());
		policyDTO.setVehicleDTO(vehicleDTO);

		policyDTO.setStartDate(policyIO.getStartDate());
		policyDTO.setExpriyDate(policyIO.getExpriyDate());
		policyDTO.setPaymentSchedule(policyIO.getPaymentSchedule());
		policyDTO.setTotalAmount(policyIO.getTotalAmount());
		policyDTO.setPolicyStatus(policyIO.getPolicyStatus());
		policyDTO.setPolicyDescription(policyIO.getPolicyDescription());
		policyService.addPolicy(authDTO, policyDTO);
		return ResponseIO.success("Policy inserted successfully");
	}

	@PostMapping("/update/{code}")
	public ResponseIO<String> updatePolicy(@RequestHeader("authToken") String authToken, @PathVariable String code,
			@RequestBody PolicyIO policyIO) {
		AuthDTO authDTO = authService.verification(authToken);
		PolicyDTO policyDTO = new PolicyDTO();
		
		policyDTO.setCode(code);

		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setCode(policyIO.getCustomer().getCode());
		policyDTO.setCustomerDTO(customerDTO);

		VehicleDTO vehicleDTO = new VehicleDTO();
		vehicleDTO.setCode(policyIO.getVehicle().getCode());
		policyDTO.setVehicleDTO(vehicleDTO);

		policyDTO.setStartDate(policyIO.getStartDate());
		policyDTO.setExpriyDate(policyIO.getExpriyDate());
		policyDTO.setPaymentSchedule(policyIO.getPaymentSchedule());
		policyDTO.setTotalAmount(policyIO.getTotalAmount());
		policyDTO.setPolicyStatus(policyIO.getPolicyStatus());
		policyDTO.setPolicyDescription(policyIO.getPolicyDescription());

		policyService.updatePolicy(authDTO, policyDTO);
		return ResponseIO.success("Policy updated successfully");
	}

	@GetMapping("/{code}")
	public ResponseIO<PolicyIO> getPolicyByCode(@RequestHeader("authToken") String authToken,
			@PathVariable String code) {
		authService.verification(authToken);

		PolicyDTO policyDTO = policyService.getPolicyByCode(code);
		PolicyIO policyIO = new PolicyIO();

		policyIO.setCode(policyDTO.getCode());

		CustomerIO customerIO = new CustomerIO();
		customerIO.setCode(policyDTO.getCustomerDTO().getCode());
		customerIO.setName(policyDTO.getCustomerDTO().getName());
		customerIO.setCustomerDOB(policyDTO.getCustomerDTO().getCustomerDOB());
		customerIO.setCustomerGender(policyDTO.getCustomerDTO().getCustomerGender());
		customerIO.setCustomerAddress(policyDTO.getCustomerDTO().getCustomerAddress());
		customerIO.setCustomerNumber(policyDTO.getCustomerDTO().getCustomerNumber());
		customerIO.setCustomerEmail(policyDTO.getCustomerDTO().getCustomerEmail());
		customerIO.setCustomerLicenseNum(policyDTO.getCustomerDTO().getCustomerLicenseNum());
		policyIO.setCustomer(customerIO);

		VehicleIO vehicleIO = new VehicleIO();
		vehicleIO.setCode(policyDTO.getVehicleDTO().getCode());
		vehicleIO.setVehiclePlateNum(policyDTO.getVehicleDTO().getVehiclePlateNum());
		vehicleIO.setVehicleType(policyDTO.getVehicleDTO().getVehicleType());
		vehicleIO.setVehicleEnginNum(policyDTO.getVehicleDTO().getVehicleEnginNum());
		vehicleIO.setVehicleChasisNum(policyDTO.getVehicleDTO().getVehicleChasisNum());
		vehicleIO.setVehicleNumber(policyDTO.getVehicleDTO().getVehicleNumber());
		vehicleIO.setVehicleModelNum(policyDTO.getVehicleDTO().getVehicleModelNum());
		policyIO.setVehicle(vehicleIO);

		policyIO.setPolicyNumber(policyDTO.getPolicyNumber());
		policyIO.setStartDate(policyDTO.getStartDate());
		policyIO.setExpriyDate(policyDTO.getExpriyDate());
		policyIO.setPremiumAmount(policyDTO.getPremiumAmount());
		policyIO.setPaymentSchedule(policyDTO.getPaymentSchedule());
		policyIO.setTotalAmount(policyDTO.getTotalAmount());
		policyIO.setPolicyStatus(policyDTO.getPolicyStatus());
		policyIO.setPolicyDescription(policyDTO.getPolicyDescription());

		return ResponseIO.success(policyIO);
	}

	@GetMapping("/")
	public ResponseIO<List<PolicyIO>> getAllPolicies(@RequestHeader("authToken") String authToken) {
		authService.verification(authToken);

		List<PolicyDTO> policyList = policyService.getAllPolicy();
		List<PolicyIO> listIO = new ArrayList<>();
		for (PolicyDTO policyDTO : policyList) {
			PolicyIO policyIO = new PolicyIO();

			policyIO.setCode(policyDTO.getCode());

			CustomerDTO customer = policyDTO.getCustomerDTO();
			CustomerIO customerIO = new CustomerIO();
			customerIO.setCode(customer.getCode());
			customerIO.setName(customer.getName());
			customerIO.setCustomerDOB(customer.getCustomerDOB());
			customerIO.setCustomerGender(customer.getCustomerGender());
			customerIO.setCustomerAddress(customer.getCustomerAddress());
			customerIO.setCustomerNumber(customer.getCustomerNumber());
			customerIO.setCustomerEmail(customer.getCustomerEmail());
			customerIO.setCustomerLicenseNum(customer.getCustomerLicenseNum());
			policyIO.setCustomer(customerIO);

			VehicleDTO vehicle = policyDTO.getVehicleDTO();
			VehicleIO vehicleIO = new VehicleIO();
			vehicleIO.setCode(vehicle.getCode());
			vehicleIO.setVehiclePlateNum(vehicle.getVehiclePlateNum());
			vehicleIO.setVehicleType(vehicle.getVehicleType());
			vehicleIO.setVehicleEnginNum(vehicle.getVehicleEnginNum());
			vehicleIO.setVehicleChasisNum(vehicle.getVehicleChasisNum());
			vehicleIO.setVehicleNumber(vehicle.getVehicleNumber());
			vehicleIO.setVehicleModelNum(vehicle.getVehicleModelNum());
			policyIO.setVehicle(vehicleIO);

			policyIO.setPolicyNumber(policyDTO.getPolicyNumber());
			policyIO.setStartDate(policyDTO.getStartDate());
			policyIO.setExpriyDate(policyDTO.getExpriyDate());
			policyIO.setPremiumAmount(policyDTO.getPremiumAmount());
			policyIO.setPaymentSchedule(policyDTO.getPaymentSchedule());
			policyIO.setTotalAmount(policyDTO.getTotalAmount());
			policyIO.setPolicyStatus(policyDTO.getPolicyStatus());
			policyIO.setPolicyDescription(policyDTO.getPolicyDescription());
			listIO.add(policyIO);

		}
		return ResponseIO.success(listIO);
	}

	@GetMapping("/getclaimbypolicy/{code}")
	public ResponseIO<List<PolicyIO>> getClaimByPolicy(@RequestHeader("authToken") String authToken,
			@PathVariable String code) {
		authService.verification(authToken);

		List<PolicyIO> listIO = new ArrayList<PolicyIO>();

		PolicyDTO policyDTO = new PolicyDTO();
		policyDTO.setCode(code);
		policyDTO = policyService.getClaimByPolicy(policyDTO);

		PolicyIO policyIO = new PolicyIO();

		policyIO.setCode(policyDTO.getCode());

		CustomerDTO customer = policyDTO.getCustomerDTO();
		CustomerIO customerIO = new CustomerIO();
		customerIO.setCode(customer.getCode());
		customerIO.setName(customer.getName());
		customerIO.setCustomerDOB(customer.getCustomerDOB());
		customerIO.setCustomerGender(customer.getCustomerGender());
		customerIO.setCustomerAddress(customer.getCustomerAddress());
		customerIO.setCustomerNumber(customer.getCustomerNumber());
		customerIO.setCustomerEmail(customer.getCustomerEmail());
		customerIO.setCustomerLicenseNum(customer.getCustomerLicenseNum());
		policyIO.setCustomer(customerIO);

		VehicleDTO vehicle = policyDTO.getVehicleDTO();
		VehicleIO vehicleIO = new VehicleIO();
		vehicleIO.setCode(vehicle.getCode());
		vehicleIO.setVehiclePlateNum(vehicle.getVehiclePlateNum());
		vehicleIO.setVehicleType(vehicle.getVehicleType());
		vehicleIO.setVehicleEnginNum(vehicle.getVehicleEnginNum());
		vehicleIO.setVehicleChasisNum(vehicle.getVehicleChasisNum());
		vehicleIO.setVehicleNumber(vehicle.getVehicleNumber());
		vehicleIO.setVehicleModelNum(vehicle.getVehicleModelNum());
		policyIO.setVehicle(vehicleIO);

		policyIO.setPolicyNumber(policyDTO.getPolicyNumber());
		policyIO.setStartDate(policyDTO.getStartDate());
		policyIO.setExpriyDate(policyDTO.getExpriyDate());
		policyIO.setPremiumAmount(policyDTO.getPremiumAmount());
		policyIO.setPaymentSchedule(policyDTO.getPaymentSchedule());
		policyIO.setTotalAmount(policyDTO.getTotalAmount());
		policyIO.setPolicyStatus(policyDTO.getPolicyStatus());
		policyIO.setPolicyDescription(policyDTO.getPolicyDescription());
		List<ClaimIO> listClaimIO = new ArrayList<>();
		for (ClaimDTO claimDTO : policyDTO.getClaimDTO()) {
			ClaimIO claimIO = new ClaimIO();

			claimIO.setCode(claimDTO.getCode());

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
			listClaimIO.add(claimIO);
		}
		policyIO.setClaim(listClaimIO);
		listIO.add(policyIO);
		return ResponseIO.success(listIO);
	}

	@GetMapping("/getclaimbypolicynum/{policyNum}")
	public ResponseIO<List<PolicyIO>> getClaimByPolicyNum(@RequestHeader("authToken") String authToken,
			@PathVariable String policyNum) {
		authService.verification(authToken);

		List<PolicyIO> listIO = new ArrayList<PolicyIO>();

		PolicyDTO policyDTO = new PolicyDTO();
		policyDTO.setPolicyNumber(policyNum);
		policyDTO = policyService.getClaimByPolicyNum(policyDTO);

		PolicyIO policyIO = new PolicyIO();

		policyIO.setCode(policyDTO.getCode());

		CustomerDTO customer = policyDTO.getCustomerDTO();
		CustomerIO customerIO = new CustomerIO();
		customerIO.setCode(customer.getCode());
		customerIO.setName(customer.getName());
		customerIO.setCustomerDOB(customer.getCustomerDOB());
		customerIO.setCustomerGender(customer.getCustomerGender());
		customerIO.setCustomerAddress(customer.getCustomerAddress());
		customerIO.setCustomerNumber(customer.getCustomerNumber());
		customerIO.setCustomerEmail(customer.getCustomerEmail());
		customerIO.setCustomerLicenseNum(customer.getCustomerLicenseNum());
		policyIO.setCustomer(customerIO);

		VehicleDTO vehicle = policyDTO.getVehicleDTO();
		VehicleIO vehicleIO = new VehicleIO();
		vehicleIO.setCode(vehicle.getCode());
		vehicleIO.setVehiclePlateNum(vehicle.getVehiclePlateNum());
		vehicleIO.setVehicleType(vehicle.getVehicleType());
		vehicleIO.setVehicleEnginNum(vehicle.getVehicleEnginNum());
		vehicleIO.setVehicleChasisNum(vehicle.getVehicleChasisNum());
		vehicleIO.setVehicleNumber(vehicle.getVehicleNumber());
		vehicleIO.setVehicleModelNum(vehicle.getVehicleModelNum());
		policyIO.setVehicle(vehicleIO);

		policyIO.setPolicyNumber(policyDTO.getPolicyNumber());
		policyIO.setStartDate(policyDTO.getStartDate());
		policyIO.setExpriyDate(policyDTO.getExpriyDate());
		policyIO.setPremiumAmount(policyDTO.getPremiumAmount());
		policyIO.setPaymentSchedule(policyDTO.getPaymentSchedule());
		policyIO.setTotalAmount(policyDTO.getTotalAmount());
		policyIO.setPolicyStatus(policyDTO.getPolicyStatus());
		policyIO.setPolicyDescription(policyDTO.getPolicyDescription());
		List<ClaimIO> listClaimIO = new ArrayList<>();
		for (ClaimDTO claimDTO : policyDTO.getClaimDTO()) {
			ClaimIO claimIO = new ClaimIO();

			claimIO.setCode(claimDTO.getCode());

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
			listClaimIO.add(claimIO);
		}
		policyIO.setClaim(listClaimIO);
		listIO.add(policyIO);
		return ResponseIO.success(listIO);
	}

	@GetMapping("/getreciptbypolicy/{policyNum}")
	public ResponseIO<List<PolicyIO>> getReciptByPolicyNum(@RequestHeader("authToken") String authToken,
			@PathVariable String policyNum) {
		authService.verification(authToken);

		List<PolicyIO> listIO = new ArrayList<PolicyIO>();

		PolicyDTO policyDTO = new PolicyDTO();
		policyDTO.setPolicyNumber(policyNum);
		policyDTO = policyService.getReciptByPolicyNum(policyDTO);

		PolicyIO policyIO = new PolicyIO();

		policyIO.setCode(policyDTO.getCode());

		CustomerDTO customer = policyDTO.getCustomerDTO();
		CustomerIO customerIO = new CustomerIO();
		customerIO.setCode(customer.getCode());
		customerIO.setName(customer.getName());
		customerIO.setCustomerDOB(customer.getCustomerDOB());
		customerIO.setCustomerGender(customer.getCustomerGender());
		customerIO.setCustomerAddress(customer.getCustomerAddress());
		customerIO.setCustomerNumber(customer.getCustomerNumber());
		customerIO.setCustomerEmail(customer.getCustomerEmail());
		customerIO.setCustomerLicenseNum(customer.getCustomerLicenseNum());
		policyIO.setCustomer(customerIO);

		VehicleDTO vehicle = policyDTO.getVehicleDTO();
		VehicleIO vehicleIO = new VehicleIO();
		vehicleIO.setCode(vehicle.getCode());
		vehicleIO.setVehiclePlateNum(vehicle.getVehiclePlateNum());
		vehicleIO.setVehicleType(vehicle.getVehicleType());
		vehicleIO.setVehicleEnginNum(vehicle.getVehicleEnginNum());
		vehicleIO.setVehicleChasisNum(vehicle.getVehicleChasisNum());
		vehicleIO.setVehicleNumber(vehicle.getVehicleNumber());
		vehicleIO.setVehicleModelNum(vehicle.getVehicleModelNum());
		policyIO.setVehicle(vehicleIO);

		policyIO.setPolicyNumber(policyDTO.getPolicyNumber());
		policyIO.setStartDate(policyDTO.getStartDate());
		policyIO.setExpriyDate(policyDTO.getExpriyDate());
		policyIO.setPremiumAmount(policyDTO.getPremiumAmount());
		policyIO.setPaymentSchedule(policyDTO.getPaymentSchedule());
		policyIO.setTotalAmount(policyDTO.getTotalAmount());
		policyIO.setPolicyStatus(policyDTO.getPolicyStatus());
		policyIO.setPolicyDescription(policyDTO.getPolicyDescription());

		List<ReciptIO> listReciptIO = new ArrayList<>();
		for (ReciptDTO reciptDTO : policyDTO.getReciptDTO()) {
			ReciptIO reciptIO = new ReciptIO();
			reciptIO.setCode(reciptDTO.getCode());
			reciptIO.setReciptAmount(reciptDTO.getReciptAmount());
			reciptIO.setPenaltyAmount(reciptDTO.getPenaltyAmount());
			reciptIO.setReciptDate(reciptDTO.getReciptDate());
			reciptIO.setDueDate(reciptDTO.getDueDate());
			reciptIO.setReciptTotalAmount(reciptDTO.getReciptTotalAmount());
			reciptIO.setReciptStatus(reciptDTO.getReciptStatus());
			listReciptIO.add(reciptIO);
		}
		policyIO.setRecipt(listReciptIO);
		listIO.add(policyIO);
		return ResponseIO.success(listIO);
	}

	@GetMapping("/getrenewalbypolicynum/{policyNum}")
	public ResponseIO<List<PolicyIO>> getRenewalByPolicyNum(@RequestHeader("authToken") String authToken,
			@PathVariable String policyNum) {

		authService.verification(authToken);

		List<PolicyIO> listIO = new ArrayList<PolicyIO>();

		PolicyDTO policyDTO = new PolicyDTO();
		policyDTO.setPolicyNumber(policyNum);
		policyDTO = policyService.getRenewalByPolicyNumber(policyDTO);

		PolicyIO policyIO = new PolicyIO();

		policyIO.setCode(policyDTO.getCode());

		CustomerDTO customer = policyDTO.getCustomerDTO();
		CustomerIO customerIO = new CustomerIO();
		customerIO.setCode(customer.getCode());
		customerIO.setName(customer.getName());
		customerIO.setCustomerDOB(customer.getCustomerDOB());
		customerIO.setCustomerGender(customer.getCustomerGender());
		customerIO.setCustomerAddress(customer.getCustomerAddress());
		customerIO.setCustomerNumber(customer.getCustomerNumber());
		customerIO.setCustomerEmail(customer.getCustomerEmail());
		customerIO.setCustomerLicenseNum(customer.getCustomerLicenseNum());
		policyIO.setCustomer(customerIO);

		VehicleDTO vehicle = policyDTO.getVehicleDTO();
		VehicleIO vehicleIO = new VehicleIO();
		vehicleIO.setCode(vehicle.getCode());
		vehicleIO.setVehiclePlateNum(vehicle.getVehiclePlateNum());
		vehicleIO.setVehicleType(vehicle.getVehicleType());
		vehicleIO.setVehicleEnginNum(vehicle.getVehicleEnginNum());
		vehicleIO.setVehicleChasisNum(vehicle.getVehicleChasisNum());
		vehicleIO.setVehicleNumber(vehicle.getVehicleNumber());
		vehicleIO.setVehicleModelNum(vehicle.getVehicleModelNum());
		policyIO.setVehicle(vehicleIO);

		policyIO.setPolicyNumber(policyDTO.getPolicyNumber());
		policyIO.setStartDate(policyDTO.getStartDate());
		policyIO.setExpriyDate(policyDTO.getExpriyDate());
		policyIO.setPremiumAmount(policyDTO.getPremiumAmount());
		policyIO.setPaymentSchedule(policyDTO.getPaymentSchedule());
		policyIO.setTotalAmount(policyDTO.getTotalAmount());
		policyIO.setPolicyStatus(policyDTO.getPolicyStatus());
		policyIO.setPolicyDescription(policyDTO.getPolicyDescription());

		List<RenewalIO> listRenewalIO = new ArrayList<>();
		for (RenewalDTO renewalDTO : policyDTO.getRenewalDTO()) {
			RenewalIO renewalIO = new RenewalIO();
			renewalIO.setCode(renewalDTO.getCode());
			renewalIO.setRenewalDate(renewalDTO.getRenewalDate());
			renewalIO.setNewExpriyDate(renewalDTO.getNewExpriyDate());
			renewalIO.setRenewalAmount(renewalDTO.getRenewalAmount());
			renewalIO.setRenewalStatus(renewalDTO.getRenewalStatus());
			listRenewalIO.add(renewalIO);
		}
		policyIO.setRenewal(listRenewalIO);
		listIO.add(policyIO);
		return ResponseIO.success(listIO);
	}
}
