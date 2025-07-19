package com.ezee.insurence.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ezee.insurence.dto.AuthDTO;
import com.ezee.insurence.dto.EmployeeDTO;
import com.ezee.insurence.exception.ServiceException;

import lombok.Cleanup;

@Repository
public class EmployeeDAO {
	@Autowired
	private DataSource dataSource;

	private static final Logger logger = LogManager.getLogger("com.ezee.insurence.dao");

	public int employeeUID(AuthDTO authDTO, EmployeeDTO employeeDTO) {
		int affected = 0;
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			String query = "{call sp_employee_uid(?,?,?,?,?,?,?,?,?,?,?)}";

			@Cleanup
			CallableStatement statement = connection.prepareCall(query);
			statement.setString(1, employeeDTO.getCode());
			statement.setString(2, employeeDTO.getName());
			statement.setString(3, employeeDTO.getUsername());
			statement.setString(4, employeeDTO.getEmployeeEmail());
			statement.setString(5, employeeDTO.getEmployeeMobile());
			statement.setString(6, employeeDTO.getPassword());
			statement.setString(7, employeeDTO.getEmployeeAddress());
			statement.setString(8, employeeDTO.getEmployeeRole());
			statement.setString(9, authDTO.getEmlployeeDTO().getCode());
			statement.setInt(10, employeeDTO.getActiveFlag());
			statement.registerOutParameter(11, Types.INTEGER);
			statement.execute();
			affected = statement.getInt("pitRowCount");
			logger.info("Inserted a employee records {} ", employeeDTO.getCode());
		} catch (Exception e) {
			logger.error("Error in the Employee record {}", e.getMessage());
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
		return affected;
	}

	public EmployeeDTO getEmployeeByCode(EmployeeDTO employeeDTO) {
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			PreparedStatement statement = null;
			if (employeeDTO.getCode() != null) {
				String query = "select id, code, employee_name, username, employee_email, employee_mobile, password, employee_address, employee_role, active_flag from employee where code = ? and active_flag = 1";
				statement = connection.prepareStatement(query);
				statement.setString(1, employeeDTO.getCode());
			} else if (employeeDTO.getId() != 0) {
				String query = "select id, code, employee_name, username, employee_email, employee_mobile, password, employee_address, employee_role, active_flag from employee where id = ? and active_flag = 1";
				statement = connection.prepareStatement(query);
				statement.setInt(1, employeeDTO.getId());
			}
			@Cleanup
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				employeeDTO.setId(resultSet.getInt("id"));
				employeeDTO.setCode(resultSet.getString("code"));
				employeeDTO.setName(resultSet.getString("employee_name"));
				employeeDTO.setUsername(resultSet.getString("username"));
				employeeDTO.setEmployeeEmail(resultSet.getString("employee_email"));
				employeeDTO.setEmployeeMobile(resultSet.getString("employee_mobile"));
				employeeDTO.setPassword(resultSet.getString("password"));
				employeeDTO.setEmployeeAddress(resultSet.getString("employee_address"));
				employeeDTO.setEmployeeRole(resultSet.getString("employee_role"));
				employeeDTO.setActiveFlag(resultSet.getInt("active_flag"));
			}
			logger.info("Get a specific employee by code {} ", employeeDTO.getCode());

		} catch (Exception e) {
			logger.error("Passing the invalid Employee record {}", e.getMessage());
			e.getStackTrace();
			throw new ServiceException(e.getMessage());
		}
		return employeeDTO;
	}

	public List<EmployeeDTO> getAllemployee() {
		List<EmployeeDTO> list = new ArrayList<>();
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "select id ,code, employee_name, username, employee_email, employee_mobile, password, employee_address, employee_role, active_flag from employee where active_flag = 1";
			PreparedStatement statement = connection.prepareStatement(query);
			@Cleanup
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				EmployeeDTO dto = new EmployeeDTO();
				dto.setId(resultSet.getInt("id"));
				dto.setCode(resultSet.getString("code"));
				dto.setName(resultSet.getString("employee_name"));
				dto.setUsername(resultSet.getString("username"));
				dto.setEmployeeEmail(resultSet.getString("employee_email"));
				dto.setEmployeeMobile(resultSet.getString("employee_mobile"));
				dto.setPassword(resultSet.getString("password"));
				dto.setEmployeeAddress(resultSet.getString("employee_address"));
				dto.setEmployeeRole(resultSet.getString("employee_role"));
				dto.setActiveFlag(resultSet.getInt("active_flag"));
				list.add(dto);
			}
			logger.info("Get all employee List ");
		} catch (Exception e) {
			logger.error("Passing the invalid Employee record {}", e.getMessage());
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
		return list;
	}

	public EmployeeDTO getAuthendtication(String username, String password) {
		EmployeeDTO dto = new EmployeeDTO();
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			String query = "select id ,code, employee_name, username, employee_email, employee_mobile, password, employee_address, employee_role, active_flag from employee where username = ? and password = ? and active_flag = 1 ";
			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, username);
			statement.setString(2, password);
			@Cleanup
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				dto.setId(resultSet.getInt("id"));
				dto.setCode(resultSet.getString("code"));
				dto.setName(resultSet.getString("employee_name"));
				dto.setUsername(username);
				dto.setEmployeeEmail(resultSet.getString("employee_email"));
				dto.setEmployeeMobile(resultSet.getString("employee_mobile"));
				dto.setPassword(password);
				dto.setEmployeeAddress(resultSet.getString("employee_address"));
				dto.setEmployeeRole(resultSet.getString("employee_role"));
				dto.setActiveFlag(resultSet.getInt("active_flag"));
			}
			logger.info("Login with the employee");
		} catch (Exception e) {
			logger.error("username and password is invalid {} ", e.getMessage());
			throw new ServiceException(e.getMessage());
		}
		return dto;
	}
}
