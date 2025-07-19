package com.ezee.insurence.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezee.insurence.dao.EmployeeDAO;
import com.ezee.insurence.dto.AuthDTO;
import com.ezee.insurence.dto.EmployeeDTO;
import com.ezee.insurence.exception.ErrorCode;
import com.ezee.insurence.exception.ServiceException;
import com.ezee.insurence.service.EmployeeService;
import com.ezee.insurence.util.TokenGenerator;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	@Autowired
	private EmployeeDAO employeeDAO;

	public static final Logger logger = LogManager.getLogger("com.ezee.insurence.service.impl");

	public void addEmployee(AuthDTO authDTO, EmployeeDTO employeeDTO) {
		try {
			String code = TokenGenerator.generateCode("EMP", 12);
			employeeDTO.setCode(code);
			int affected = employeeDAO.employeeUID(authDTO, employeeDTO);
			if (affected == 0) {
				throw new ServiceException(ErrorCode.USER_NOT_CREATED);
			}
			logger.info("Insert the employee Record ");
		} catch (ServiceException e) {
			logger.error("While Inserting employee record service exception {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("While Inserting employee record exception {} ", e.getMessage());
			throw e;
		}
	}

	public void updateEmployee(AuthDTO authDTO, EmployeeDTO employeeDTO) {
		try {

			int affected = employeeDAO.employeeUID(authDTO, employeeDTO);
			if (affected == 0) {
				throw new ServiceException(ErrorCode.USER_NOT_CREATED);
			}
			logger.info("Insert the employee Record ");
		} catch (ServiceException e) {
			logger.error("While Inserting employee record service exception {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("While Inserting employee record exception {} ", e.getMessage());
			throw e;
		}
	}

	@Override
	public EmployeeDTO getEmployeeByCode(String Code) {
		EmployeeDTO employeeDTO = new EmployeeDTO();
		try {
			employeeDTO.setCode(Code);
			employeeDTO = employeeDAO.getEmployeeByCode(employeeDTO);
			if (employeeDTO.getId() == 0) {
				throw new ServiceException(ErrorCode.USER_NOT_FOUND);
			}
			logger.info("Fetch the specific Employee Record ");
		} catch (ServiceException e) {
			logger.error("While Fetch Employee Record in the Servcie Exception {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("While Fetch Employee Record in the Exception {} ", e.getMessage());
			throw e;
		}
		return employeeDTO;
	}

	@Override
	public List<EmployeeDTO> getAllEmployee() {
		List<EmployeeDTO> listDTO = new ArrayList<>();
		try {
			listDTO = employeeDAO.getAllemployee();
			if (listDTO == null || listDTO.isEmpty()) {
				throw new ServiceException(ErrorCode.USER_NOT_FOUND);
			}
			logger.info("Fetch all employee Record ");
		} catch (ServiceException e) {
			logger.error("While Fetch all Employee Record in the Servcie Exception {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("While Fetch all Employee Record in the Exception {} ", e.getMessage());
			throw e;
		}
		return listDTO;
	}

}
