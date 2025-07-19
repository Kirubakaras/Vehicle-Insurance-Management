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

import com.ezee.insurence.controller.IO.EmployeeIO;
import com.ezee.insurence.controller.IO.ResponseIO;
import com.ezee.insurence.dto.AuthDTO;
import com.ezee.insurence.dto.EmployeeDTO;
import com.ezee.insurence.service.AuthService;
import com.ezee.insurence.service.EmployeeService;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	AuthService authService;

	@Autowired
	EmployeeService employeeService;

	@PostMapping("/add")
	public ResponseIO<String> addEmployee(@RequestHeader("authtoken") String authtoken,
			@RequestBody EmployeeIO employeeIO) {
		AuthDTO authDTO = authService.verification(authtoken);
		EmployeeDTO employeeDTO = new EmployeeDTO();
		employeeDTO.setName(employeeIO.getName());
		employeeDTO.setUsername(employeeIO.getUsername());
		employeeDTO.setEmployeeEmail(employeeIO.getEmployeeEmail());
		employeeDTO.setEmployeeMobile(employeeIO.getEmployeeMobile());
		employeeDTO.setPassword(employeeIO.getPassword());
		employeeDTO.setEmployeeAddress(employeeIO.getEmployeeAddress());
		employeeDTO.setEmployeeRole(employeeIO.getEmployeeRole());
		employeeDTO.setActiveFlag(employeeIO.getActiveFlag());
		employeeService.addEmployee(authDTO, employeeDTO);

		return ResponseIO.success("Inserted a record sucessfully");
	}

	@PostMapping("/update")
	public ResponseIO<String> UpdateEmployee(@RequestHeader("authtoken") String authtoken,
			@RequestBody EmployeeIO employeeIO) {
		AuthDTO authDTO = authService.verification(authtoken);
		EmployeeDTO employeeDTO = new EmployeeDTO();
		employeeDTO.setName(employeeIO.getName());
		employeeDTO.setCode(employeeIO.getCode());
		employeeDTO.setUsername(employeeIO.getUsername());
		employeeDTO.setEmployeeEmail(employeeIO.getEmployeeEmail());
		employeeDTO.setEmployeeMobile(employeeIO.getEmployeeMobile());
		employeeDTO.setPassword(employeeIO.getPassword());
		employeeDTO.setEmployeeAddress(employeeIO.getEmployeeAddress());
		employeeDTO.setEmployeeRole(employeeIO.getEmployeeRole());
		employeeDTO.setActiveFlag(employeeIO.getActiveFlag());
		employeeService.updateEmployee(authDTO, employeeDTO);

		return ResponseIO.success("Updated a record sucessfully");
	}

	@GetMapping("/{code}")
	public ResponseIO<EmployeeIO> getEmployee(@RequestHeader("authtoken") String authtoken, @PathVariable String code) {
		authService.verification(authtoken);

		EmployeeDTO employeeDTO = employeeService.getEmployeeByCode(code);
		EmployeeIO employeeIO = new EmployeeIO();
		employeeIO.setCode(employeeDTO.getCode());
		employeeIO.setName(employeeDTO.getName());
		employeeIO.setUsername(employeeDTO.getUsername());
		employeeIO.setEmployeeEmail(employeeDTO.getEmployeeEmail());
		employeeIO.setEmployeeMobile(employeeDTO.getEmployeeMobile());
		employeeIO.setEmployeeAddress(employeeDTO.getEmployeeAddress());
		employeeIO.setEmployeeRole(employeeDTO.getEmployeeRole());
		employeeIO.setActiveFlag(employeeDTO.getActiveFlag());

		return ResponseIO.success(employeeIO);

	}

	@GetMapping("/")
	public ResponseIO<List<EmployeeIO>> getAllEmployee(@RequestHeader("authtoken") String authtoken) {
		authService.verification(authtoken);
		List<EmployeeIO> listIO = new ArrayList<EmployeeIO>();

		List<EmployeeDTO> listDTO = employeeService.getAllEmployee();
		for (EmployeeDTO employeeDTO : listDTO) {
			EmployeeIO employeeIO = new EmployeeIO();
			employeeIO.setCode(employeeDTO.getCode());
			employeeIO.setName(employeeDTO.getName());
			employeeIO.setUsername(employeeDTO.getUsername());
			employeeIO.setEmployeeEmail(employeeDTO.getEmployeeEmail());
			employeeIO.setEmployeeMobile(employeeDTO.getEmployeeMobile());
			employeeIO.setEmployeeAddress(employeeDTO.getEmployeeAddress());
			employeeIO.setEmployeeRole(employeeDTO.getEmployeeRole());
			employeeIO.setActiveFlag(employeeDTO.getActiveFlag());
			listIO.add(employeeIO);
		}

		return ResponseIO.success(listIO);
	}
}
