package com.ezee.insurence.service;

import java.util.List;

import com.ezee.insurence.dto.AuthDTO;
import com.ezee.insurence.dto.EmployeeDTO;

public interface EmployeeService {
	public void addEmployee( AuthDTO authDTO,EmployeeDTO employeeDTO);

	public void updateEmployee(AuthDTO authDTO, EmployeeDTO employeeDTO);

	public EmployeeDTO getEmployeeByCode(String code);

	public List<EmployeeDTO> getAllEmployee();
}
