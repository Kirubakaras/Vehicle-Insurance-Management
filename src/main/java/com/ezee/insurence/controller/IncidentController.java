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
import com.ezee.insurence.controller.IO.IncidentIO;
import com.ezee.insurence.controller.IO.ResponseIO;
import com.ezee.insurence.dto.AuthDTO;
import com.ezee.insurence.dto.CustomerDTO;
import com.ezee.insurence.dto.IncidentDTO;
import com.ezee.insurence.service.AuthService;
import com.ezee.insurence.service.IncidentService;

@RestController
@RequestMapping("/incident")
public class IncidentController {
	@Autowired
	AuthService authService;

	@Autowired
	IncidentService incidentService;

	@PostMapping("/add")
	public ResponseIO<String> addIncident(@RequestHeader("authToken") String authToken,
			@RequestBody IncidentIO incidentIO) {
		AuthDTO authDTO = authService.verification(authToken);

		IncidentDTO incidentDTO = new IncidentDTO();

		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setCode(incidentIO.getCustomer().getCode());
		incidentDTO.setCustomerDTO(customerDTO);

		incidentDTO.setIncidentDate(incidentIO.getIncidentDate());
		incidentDTO.setIncidentType(incidentIO.getIncidentType());
		incidentDTO.setIncidentInspector(incidentIO.getIncidentInspector());
		incidentDTO.setIncidentCost(incidentIO.getIncidentCost());
		incidentDTO.setIncidentDescription(incidentIO.getIncidentDescription());
		incidentDTO.setIncidentStatus(incidentIO.getIncidentStatus());

		incidentService.addIncident(authDTO, incidentDTO);
		return ResponseIO.success("Incident record inserted successfully");
	}

	@PostMapping("/update/{code}")
	public ResponseIO<String> updateIncident(@RequestHeader("authToken") String authToken, @PathVariable String code,
			@RequestBody IncidentIO incidentIO) {
		AuthDTO authDTO = authService.verification(authToken);

		IncidentDTO incidentDTO = new IncidentDTO();
		incidentDTO.setCode(code);

		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setCode(incidentIO.getCustomer().getCode());
		incidentDTO.setCustomerDTO(customerDTO);

		incidentDTO.setIncidentDate(incidentIO.getIncidentDate());
		incidentDTO.setIncidentType(incidentIO.getIncidentType());
		incidentDTO.setIncidentInspector(incidentIO.getIncidentInspector());
		incidentDTO.setIncidentCost(incidentIO.getIncidentCost());
		incidentDTO.setIncidentDescription(incidentIO.getIncidentDescription());
		incidentDTO.setIncidentStatus(incidentIO.getIncidentStatus());
		incidentDTO.setActiveFlag(incidentIO.getActiveFlag());

		incidentService.updateIncident(authDTO, incidentDTO);
		return ResponseIO.success("Incident record updated successfully");
	}

	@GetMapping("/{code}")
	public ResponseIO<IncidentIO> getIncident(@RequestHeader("authToken") String authToken, @PathVariable String code) {
		authService.verification(authToken);

		IncidentDTO incidentDTO = incidentService.getIncidentByCode(code);
		IncidentIO incidentIO = new IncidentIO();

		incidentIO.setCode(incidentDTO.getCode());

		CustomerIO customerIO = new CustomerIO();
		customerIO.setCode(incidentDTO.getCustomerDTO().getCode());
		customerIO.setName(incidentDTO.getCustomerDTO().getName());
		customerIO.setCustomerDOB(incidentDTO.getCustomerDTO().getCustomerDOB());
		customerIO.setCustomerGender(incidentDTO.getCustomerDTO().getCustomerGender());
		customerIO.setCustomerAddress(incidentDTO.getCustomerDTO().getCustomerAddress());
		customerIO.setCustomerNumber(incidentDTO.getCustomerDTO().getCustomerNumber());
		customerIO.setCustomerEmail(incidentDTO.getCustomerDTO().getCustomerEmail());
		customerIO.setCustomerLicenseNum(incidentDTO.getCustomerDTO().getCustomerLicenseNum());
		incidentIO.setCustomer(customerIO);

		incidentIO.setIncidentDate(incidentDTO.getIncidentDate());
		incidentIO.setIncidentType(incidentDTO.getIncidentType());
		incidentIO.setIncidentInspector(incidentDTO.getIncidentInspector());
		incidentIO.setIncidentCost(incidentDTO.getIncidentCost());
		incidentIO.setIncidentDescription(incidentDTO.getIncidentDescription());
		incidentIO.setIncidentStatus(incidentDTO.getIncidentStatus());

		return ResponseIO.success(incidentIO);
	}

	@GetMapping("/")
	public ResponseIO<List<IncidentIO>> getAllIncidents(@RequestHeader("authToken") String authToken) {
		authService.verification(authToken);

		List<IncidentDTO> listDTO = incidentService.getIAllncident();
		List<IncidentIO> listIO = new ArrayList<>();

		for (IncidentDTO incidentDTO : listDTO) {
			IncidentIO incidentIO = new IncidentIO();

			incidentIO.setCode(incidentDTO.getCode());

			CustomerIO customerIO = new CustomerIO();
			customerIO.setCode(incidentDTO.getCustomerDTO().getCode());
			customerIO.setName(incidentDTO.getCustomerDTO().getName());
			customerIO.setCustomerDOB(incidentDTO.getCustomerDTO().getCustomerDOB());
			customerIO.setCustomerGender(incidentDTO.getCustomerDTO().getCustomerGender());
			customerIO.setCustomerAddress(incidentDTO.getCustomerDTO().getCustomerAddress());
			customerIO.setCustomerNumber(incidentDTO.getCustomerDTO().getCustomerNumber());
			customerIO.setCustomerEmail(incidentDTO.getCustomerDTO().getCustomerEmail());
			customerIO.setCustomerLicenseNum(incidentDTO.getCustomerDTO().getCustomerLicenseNum());
			incidentIO.setCustomer(customerIO);

			incidentIO.setIncidentDate(incidentDTO.getIncidentDate());
			incidentIO.setIncidentType(incidentDTO.getIncidentType());
			incidentIO.setIncidentInspector(incidentDTO.getIncidentInspector());
			incidentIO.setIncidentCost(incidentDTO.getIncidentCost());
			incidentIO.setIncidentDescription(incidentDTO.getIncidentDescription());
			incidentIO.setIncidentStatus(incidentDTO.getIncidentStatus());

			listIO.add(incidentIO);
		}
		return ResponseIO.success(listIO);
	}

}
