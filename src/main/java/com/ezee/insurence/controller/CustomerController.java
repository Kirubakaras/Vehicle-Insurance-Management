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
import com.ezee.insurence.controller.IO.PaymentIO;
import com.ezee.insurence.controller.IO.PolicyIO;
import com.ezee.insurence.controller.IO.ReciptIO;
import com.ezee.insurence.controller.IO.RenewalIO;
import com.ezee.insurence.controller.IO.ResponseIO;
import com.ezee.insurence.controller.IO.VehicleIO;
import com.ezee.insurence.dto.AuthDTO;
import com.ezee.insurence.dto.ClaimDTO;
import com.ezee.insurence.dto.CustomerDTO;
import com.ezee.insurence.dto.IncidentDTO;
import com.ezee.insurence.dto.PaymentDTO;
import com.ezee.insurence.dto.PolicyDTO;
import com.ezee.insurence.dto.ReciptDTO;
import com.ezee.insurence.dto.RenewalDTO;
import com.ezee.insurence.dto.VehicleDTO;
import com.ezee.insurence.service.AuthService;
import com.ezee.insurence.service.CustomerService;
import com.ezee.insurence.util.StringUtil;

@RestController
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
	AuthService authService;

	@Autowired
	CustomerService customerService;

	@PostMapping("/add")
	public ResponseIO<String> addCustomer(@RequestHeader("authtoken") String authToken,
			@RequestBody CustomerIO customerIO) {
		AuthDTO authDTO = authService.verification(authToken);
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setName(customerIO.getName());
		customerDTO.setCustomerDOB(StringUtil.dateFormate(customerIO.getCustomerDOB()));
		customerDTO.setCustomerGender(customerIO.getCustomerGender());
		customerDTO.setCustomerAddress(customerIO.getCustomerAddress());
		customerDTO.setCustomerNumber(customerIO.getCustomerNumber());
		customerDTO.setCustomerEmail(customerIO.getCustomerEmail());
		customerDTO.setCustomerLicenseNum(customerIO.getCustomerLicenseNum());
		customerService.addCustomer(authDTO, customerDTO);
		return ResponseIO.success("Insert the customer records");
	}

	@PostMapping("/update/{code}")
	public ResponseIO<String> updateCustomer(@RequestHeader("authtoken") String authToken, @PathVariable String code,
			@RequestBody CustomerIO customerIO) {
		AuthDTO authDTO = authService.verification(authToken);
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setName(customerIO.getName());
		customerDTO.setCode(code);
		customerDTO.setCustomerDOB(customerIO.getCustomerDOB());
		customerDTO.setCustomerGender(customerIO.getCustomerGender());
		customerDTO.setCustomerAddress(customerIO.getCustomerAddress());
		customerDTO.setCustomerNumber(customerIO.getCustomerNumber());
		customerDTO.setCustomerEmail(customerIO.getCustomerEmail());
		customerDTO.setCustomerLicenseNum(customerIO.getCustomerLicenseNum());
		customerService.updateCustomer(authDTO, customerDTO);
		return ResponseIO.success("Update the customer records");
	}

	@GetMapping("/{code}")
	public ResponseIO<CustomerIO> getCustomer(@RequestHeader("authtoken") String authToken, @PathVariable String code) {
		authService.verification(authToken);
		CustomerDTO customerDTO = customerService.getCustomerByCode(code);
		CustomerIO customerIO = new CustomerIO();
		customerIO.setName(customerDTO.getName());
		customerIO.setCode(customerDTO.getCode());
		customerIO.setCustomerDOB(customerDTO.getCustomerDOB());
		customerIO.setCustomerGender(customerDTO.getCustomerGender());
		customerIO.setCustomerAddress(customerDTO.getCustomerAddress());
		customerIO.setCustomerNumber(customerDTO.getCustomerNumber());
		customerIO.setCustomerEmail(customerDTO.getCustomerEmail());
		customerIO.setCustomerLicenseNum(customerDTO.getCustomerLicenseNum());
		return ResponseIO.success(customerIO);
	}

	@GetMapping("/")
	public ResponseIO<List<CustomerIO>> getAllCustomer(@RequestHeader("authtoken") String authToken) {
		authService.verification(authToken);
		List<CustomerIO> listIO = new ArrayList<CustomerIO>();
		List<CustomerDTO> listDTO = customerService.getAllCustomer();
		for (CustomerDTO customerDTO : listDTO) {
			CustomerIO customerIO = new CustomerIO();
			customerIO.setName(customerDTO.getName());
			customerIO.setCode(customerDTO.getCode());
			customerIO.setCustomerDOB(customerDTO.getCustomerDOB());
			customerIO.setCustomerGender(customerDTO.getCustomerGender());
			customerIO.setCustomerAddress(customerDTO.getCustomerAddress());
			customerIO.setCustomerNumber(customerDTO.getCustomerNumber());
			customerIO.setCustomerEmail(customerDTO.getCustomerEmail());
			customerIO.setCustomerLicenseNum(customerDTO.getCustomerLicenseNum());
			listIO.add(customerIO);
		}
		return ResponseIO.success(listIO);
	}

	@GetMapping("/getvehiclebycustomer/{code}")
	public ResponseIO<List<CustomerIO>> getVehicleByCustomer(@RequestHeader("authtoken") String authToken,
			@PathVariable String code) {
		authService.verification(authToken);
		List<CustomerIO> listIO = new ArrayList<CustomerIO>();

		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setCode(code);
		customerDTO = customerService.getVehicleByCustomer(customerDTO);

		CustomerIO customerIO = new CustomerIO();
		customerIO.setName(customerDTO.getName());
		customerIO.setCode(customerDTO.getCode());
		customerIO.setCustomerDOB(customerDTO.getCustomerDOB());
		customerIO.setCustomerGender(customerDTO.getCustomerGender());
		customerIO.setCustomerAddress(customerDTO.getCustomerAddress());
		customerIO.setCustomerNumber(customerDTO.getCustomerNumber());
		customerIO.setCustomerEmail(customerDTO.getCustomerEmail());
		customerIO.setCustomerLicenseNum(customerDTO.getCustomerLicenseNum());
		List<VehicleIO> listVehicleIO = new ArrayList<>();
		for (VehicleDTO vehicleDTO : customerDTO.getVehicleDTO()) {
			VehicleIO vehicleIO = new VehicleIO();
			vehicleIO.setCode(vehicleDTO.getCode());
			vehicleIO.setVehiclePlateNum(vehicleDTO.getVehiclePlateNum());
			vehicleIO.setVehicleType(vehicleDTO.getVehicleType());
			vehicleIO.setVehicleEnginNum(vehicleDTO.getVehicleEnginNum());
			vehicleIO.setVehicleChasisNum(vehicleDTO.getVehicleChasisNum());
			vehicleIO.setVehicleNumber(vehicleDTO.getVehicleNumber());
			vehicleIO.setVehicleModelNum(vehicleDTO.getVehicleModelNum());
			listVehicleIO.add(vehicleIO);
		}
		customerIO.setVehicle(listVehicleIO);
		listIO.add(customerIO);
		return ResponseIO.success(listIO);
	}

	@GetMapping("/getpolicybycustomer/{code}")
	public ResponseIO<List<CustomerIO>> getPolicyByCustomer(@RequestHeader("authtoken") String authToken,
			@PathVariable String code) {
		authService.verification(authToken);
		List<CustomerIO> listIO = new ArrayList<CustomerIO>();

		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setCode(code);
		customerDTO = customerService.getPolicyByCustomer(customerDTO);

		CustomerIO customerIO = new CustomerIO();
		customerIO.setName(customerDTO.getName());
		customerIO.setCode(customerDTO.getCode());
		customerIO.setCustomerDOB(customerDTO.getCustomerDOB());
		customerIO.setCustomerGender(customerDTO.getCustomerGender());
		customerIO.setCustomerAddress(customerDTO.getCustomerAddress());
		customerIO.setCustomerNumber(customerDTO.getCustomerNumber());
		customerIO.setCustomerEmail(customerDTO.getCustomerEmail());
		customerIO.setCustomerLicenseNum(customerDTO.getCustomerLicenseNum());
		List<PolicyIO> listPolicyIO = new ArrayList<>();
		for (PolicyDTO policyDTO : customerDTO.getPolicyDTO()) {

			PolicyIO policyIO = new PolicyIO();

			policyIO.setCode(policyDTO.getCode());

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
			listPolicyIO.add(policyIO);

		}
		customerIO.setPolicy(listPolicyIO);
		listIO.add(customerIO);
		return ResponseIO.success(listIO);

	}

	@GetMapping("/getincidentbycustomer/{code}")
	public ResponseIO<List<CustomerIO>> getIncidentByCustomer(@RequestHeader("authtoken") String authToken,
			@PathVariable String code) {
		authService.verification(authToken);
		List<CustomerIO> listIO = new ArrayList<CustomerIO>();

		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setCode(code);
		customerDTO = customerService.getIncidentByCustomer(customerDTO);

		CustomerIO customerIO = new CustomerIO();
		customerIO.setName(customerDTO.getName());
		customerIO.setCode(customerDTO.getCode());
		customerIO.setCustomerDOB(customerDTO.getCustomerDOB());
		customerIO.setCustomerGender(customerDTO.getCustomerGender());
		customerIO.setCustomerAddress(customerDTO.getCustomerAddress());
		customerIO.setCustomerNumber(customerDTO.getCustomerNumber());
		customerIO.setCustomerEmail(customerDTO.getCustomerEmail());
		customerIO.setCustomerLicenseNum(customerDTO.getCustomerLicenseNum());
		List<IncidentIO> incidentList = new ArrayList<>();
		for (IncidentDTO incidentDTO : customerDTO.getIncidentDTO()) {

			IncidentIO incidentIO = new IncidentIO();

			incidentIO.setCode(incidentDTO.getCode());

			incidentIO.setIncidentDate(incidentDTO.getIncidentDate());
			incidentIO.setIncidentType(incidentDTO.getIncidentType());
			incidentIO.setIncidentInspector(incidentDTO.getIncidentInspector());
			incidentIO.setIncidentCost(incidentDTO.getIncidentCost());
			incidentIO.setIncidentDescription(incidentDTO.getIncidentDescription());
			incidentIO.setIncidentStatus(incidentDTO.getIncidentStatus());

			incidentList.add(incidentIO);
		}
		customerIO.setIncident(incidentList);
		listIO.add(customerIO);

		return ResponseIO.success(listIO);
	}

	@GetMapping("/getpaymentbycustomer/{code}")
	public ResponseIO<List<CustomerIO>> getPaymentByCustomer(@RequestHeader("authtoken") String authToken,
			@PathVariable String code) {
		authService.verification(authToken);
		List<CustomerIO> listIO = new ArrayList<CustomerIO>();

		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setCode(code);
		customerDTO = customerService.getPaymentByCustomer(customerDTO);

		CustomerIO customerIO = new CustomerIO();
		customerIO.setName(customerDTO.getName());
		customerIO.setCode(customerDTO.getCode());
		customerIO.setCustomerDOB(customerDTO.getCustomerDOB());
		customerIO.setCustomerGender(customerDTO.getCustomerGender());
		customerIO.setCustomerAddress(customerDTO.getCustomerAddress());
		customerIO.setCustomerNumber(customerDTO.getCustomerNumber());
		customerIO.setCustomerEmail(customerDTO.getCustomerEmail());
		customerIO.setCustomerLicenseNum(customerDTO.getCustomerLicenseNum());

		List<PaymentIO> paymentList = new ArrayList<>();

		for (PaymentDTO paymentDTO : customerDTO.getPaymentDTO()) {
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

			paymentIO.setPaymentDate(paymentDTO.getPaymentDate());
			paymentIO.setPaymentAmount(paymentDTO.getPaymentAmount());
			paymentIO.setPaymentMode(paymentDTO.getPaymentMode());
			paymentList.add(paymentIO);

		}
		customerIO.setPayment(paymentList);
		listIO.add(customerIO);
		return ResponseIO.success(listIO);
	}

	@GetMapping("/getreciptbycustomer/{code}")
	public ResponseIO<List<CustomerIO>> getReciptByCustomer(@RequestHeader("authtoken") String authToken,
			@PathVariable String code) {
		authService.verification(authToken);
		List<CustomerIO> listIO = new ArrayList<CustomerIO>();

		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setCode(code);
		customerDTO = customerService.getReciptByCustomer(customerDTO);

		CustomerIO customerIO = new CustomerIO();
		customerIO.setName(customerDTO.getName());
		customerIO.setCode(customerDTO.getCode());
		customerIO.setCustomerDOB(customerDTO.getCustomerDOB());
		customerIO.setCustomerGender(customerDTO.getCustomerGender());
		customerIO.setCustomerAddress(customerDTO.getCustomerAddress());
		customerIO.setCustomerNumber(customerDTO.getCustomerNumber());
		customerIO.setCustomerEmail(customerDTO.getCustomerEmail());
		customerIO.setCustomerLicenseNum(customerDTO.getCustomerLicenseNum());

		List<ReciptIO> reciptList = new ArrayList<>();

		for (ReciptDTO reciptDTO : customerDTO.getReciptDTO()) {

			ReciptIO reciptIO = new ReciptIO();
			reciptIO.setCode(reciptDTO.getCode());

			if (reciptDTO.getPolicyDTO() != null) {
				PolicyDTO policyDTO = reciptDTO.getPolicyDTO();

				PolicyIO policyIO = new PolicyIO();
				policyIO.setCode(policyDTO.getCode());

				VehicleDTO vehicleDTO = policyDTO.getVehicleDTO();
				VehicleIO vehicleIO = new VehicleIO();
				vehicleDTO.setCode(vehicleDTO.getCode());
				vehicleIO.setVehiclePlateNum(vehicleDTO.getVehiclePlateNum());
				vehicleIO.setVehicleType(vehicleDTO.getVehicleType());
				vehicleIO.setVehicleEnginNum(vehicleDTO.getVehicleEnginNum());
				vehicleIO.setVehicleChasisNum(vehicleDTO.getVehicleChasisNum());
				vehicleIO.setVehicleNumber(vehicleDTO.getVehicleNumber());
				vehicleIO.setVehicleModelNum(vehicleDTO.getVehicleModelNum());

				policyIO.setVehicle(vehicleIO);
				policyIO.setPolicyNumber(policyDTO.getPolicyNumber());
				policyIO.setStartDate(policyDTO.getStartDate());
				policyIO.setExpriyDate(policyDTO.getExpriyDate());
				policyIO.setPremiumAmount(policyDTO.getPremiumAmount());
				policyIO.setPaymentSchedule(policyDTO.getPaymentSchedule());
				policyIO.setTotalAmount(policyDTO.getTotalAmount());
				policyIO.setPolicyStatus(policyDTO.getPolicyStatus());
				policyIO.setPolicyDescription(policyDTO.getPolicyDescription());
				reciptIO.setPolicy(policyIO);

			} else {

				RenewalDTO renewalDTO = reciptDTO.getRenewalDTO();

				RenewalIO renewalIO = new RenewalIO();
				renewalIO.setCode(renewalDTO.getCode());

				PolicyDTO policyDTO = renewalDTO.getPolicyDTO();
				PolicyIO policyIO = new PolicyIO();
				policyIO.setCode(policyDTO.getCode());

				VehicleDTO vehicleDTO = policyDTO.getVehicleDTO();
				VehicleIO vehicleIO = new VehicleIO();
				vehicleDTO.setCode(vehicleDTO.getCode());
				vehicleIO.setVehiclePlateNum(vehicleDTO.getVehiclePlateNum());
				vehicleIO.setVehicleType(vehicleDTO.getVehicleType());
				vehicleIO.setVehicleEnginNum(vehicleDTO.getVehicleEnginNum());
				vehicleIO.setVehicleChasisNum(vehicleDTO.getVehicleChasisNum());
				vehicleIO.setVehicleNumber(vehicleDTO.getVehicleNumber());
				vehicleIO.setVehicleModelNum(vehicleDTO.getVehicleModelNum());

				policyIO.setVehicle(vehicleIO);
				policyIO.setPolicyNumber(policyDTO.getPolicyNumber());
				policyIO.setStartDate(policyDTO.getStartDate());
				policyIO.setExpriyDate(policyDTO.getExpriyDate());
				policyIO.setPremiumAmount(policyDTO.getPremiumAmount());
				policyIO.setPaymentSchedule(policyDTO.getPaymentSchedule());
				policyIO.setTotalAmount(policyDTO.getTotalAmount());
				policyIO.setPolicyStatus(policyDTO.getPolicyStatus());
				policyIO.setPolicyDescription(policyDTO.getPolicyDescription());

				renewalIO.setPolicy(policyIO);
				renewalIO.setRenewalDate(renewalDTO.getRenewalDate());
				renewalIO.setNewExpriyDate(renewalDTO.getNewExpriyDate());
				renewalIO.setRenewalAmount(renewalDTO.getRenewalAmount());
				renewalIO.setRenewalStatus(renewalDTO.getRenewalStatus());
				reciptIO.setRenewal(renewalIO);
			}
			reciptIO.setReciptAmount(reciptDTO.getReciptAmount());
			reciptIO.setPenaltyAmount(reciptDTO.getPenaltyAmount());
			reciptIO.setReciptDate(reciptDTO.getReciptDate());
			reciptIO.setDueDate(reciptDTO.getDueDate());
			reciptIO.setReciptTotalAmount(reciptDTO.getReciptTotalAmount());
			reciptIO.setReciptStatus(reciptDTO.getReciptStatus());
			reciptList.add(reciptIO);
		}
		customerIO.setRecipt(reciptList);
		listIO.add(customerIO);
		return ResponseIO.success(listIO);
	}

	@GetMapping("/getclaimbycustomer/{code}")
	public ResponseIO<List<CustomerIO>> getClaimByCustomer(@RequestHeader("authtoken") String authToken,
			@PathVariable String code) {
		authService.verification(authToken);
		List<CustomerIO> listIO = new ArrayList<CustomerIO>();

		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setCode(code);
		customerDTO = customerService.getClaimByCustomer(customerDTO);

		CustomerIO customerIO = new CustomerIO();
		customerIO.setName(customerDTO.getName());
		customerIO.setCode(customerDTO.getCode());
		customerIO.setCustomerDOB(customerDTO.getCustomerDOB());
		customerIO.setCustomerGender(customerDTO.getCustomerGender());
		customerIO.setCustomerAddress(customerDTO.getCustomerAddress());
		customerIO.setCustomerNumber(customerDTO.getCustomerNumber());
		customerIO.setCustomerEmail(customerDTO.getCustomerEmail());
		customerIO.setCustomerLicenseNum(customerDTO.getCustomerLicenseNum());

		List<ClaimIO> claimIOList = new ArrayList<>();

		for (ClaimDTO claimDTO : customerDTO.getClaimDTO()) {
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
			claimIOList.add(claimIO);
		}
		customerIO.setClaim(claimIOList);
		listIO.add(customerIO);

		return ResponseIO.success(listIO);

	}

	@GetMapping("/getactivepolicybycustomer/{code}")
	public ResponseIO<List<CustomerIO>> getActivePolicyByCustomer(@RequestHeader("authtoken") String authToken,
			@PathVariable String code, @RequestParam String policyStatus) {
		authService.verification(authToken);
		List<CustomerIO> listIO = new ArrayList<CustomerIO>();

		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setCode(code);
		PolicyDTO policyDTO = new PolicyDTO();
		policyDTO.setPolicyStatus(policyStatus.toUpperCase());
		customerDTO = customerService.getActivePolicyByCustomer(customerDTO, policyDTO);

		CustomerIO customerIO = new CustomerIO();
		customerIO.setName(customerDTO.getName());
		customerIO.setCode(customerDTO.getCode());
		customerIO.setCustomerDOB(customerDTO.getCustomerDOB());
		customerIO.setCustomerGender(customerDTO.getCustomerGender());
		customerIO.setCustomerAddress(customerDTO.getCustomerAddress());
		customerIO.setCustomerNumber(customerDTO.getCustomerNumber());
		customerIO.setCustomerEmail(customerDTO.getCustomerEmail());
		customerIO.setCustomerLicenseNum(customerDTO.getCustomerLicenseNum());
		List<PolicyIO> policyIOList = new ArrayList<PolicyIO>();
		for (PolicyDTO pDTO : customerDTO.getPolicyDTO()) {
			PolicyIO policyIO = new PolicyIO();

			policyIO.setCode(pDTO.getCode());

			VehicleDTO vehicle = pDTO.getVehicleDTO();
			VehicleIO vehicleIO = new VehicleIO();
			vehicleIO.setCode(vehicle.getCode());
			vehicleIO.setVehiclePlateNum(vehicle.getVehiclePlateNum());
			vehicleIO.setVehicleType(vehicle.getVehicleType());
			vehicleIO.setVehicleEnginNum(vehicle.getVehicleEnginNum());
			vehicleIO.setVehicleChasisNum(vehicle.getVehicleChasisNum());
			vehicleIO.setVehicleNumber(vehicle.getVehicleNumber());
			vehicleIO.setVehicleModelNum(vehicle.getVehicleModelNum());
			policyIO.setVehicle(vehicleIO);

			policyIO.setPolicyNumber(pDTO.getPolicyNumber());
			policyIO.setStartDate(pDTO.getStartDate());
			policyIO.setExpriyDate(pDTO.getExpriyDate());
			policyIO.setPremiumAmount(pDTO.getPremiumAmount());
			policyIO.setPaymentSchedule(pDTO.getPaymentSchedule());
			policyIO.setTotalAmount(pDTO.getTotalAmount());
			policyIO.setPolicyStatus(pDTO.getPolicyStatus());
			policyIO.setPolicyDescription(pDTO.getPolicyDescription());
			policyIOList.add(policyIO);
		}
		customerIO.setPolicy(policyIOList);
		listIO.add(customerIO);
		return ResponseIO.success(listIO);
	}

}
