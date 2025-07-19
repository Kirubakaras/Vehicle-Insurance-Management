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
import com.ezee.insurence.dto.CustomerDTO;
import com.ezee.insurence.dto.IncidentDTO;
import com.ezee.insurence.exception.ServiceException;

import lombok.Cleanup;

@Repository
public class IncidentDAO {

	@Autowired
	private DataSource dataSource;

	private static final Logger logger = LogManager.getLogger("com.ezee.insurence.dao");

	public int incidentUID(AuthDTO authDTO, IncidentDTO incidentDTO) {
		int affected = 0;
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "{call sp_incident_uid(?,?,?,?,?,?,?,?,?,?,?)}";

			@Cleanup
			CallableStatement statement = connection.prepareCall(query);

			statement.setString(1, incidentDTO.getCode());
			statement.setString(2, incidentDTO.getCustomerDTO().getCode());
			statement.setString(3, incidentDTO.getIncidentDate());
			statement.setString(4, incidentDTO.getIncidentType().toUpperCase());
			statement.setString(5, incidentDTO.getIncidentInspector());
			statement.setBigDecimal(6, incidentDTO.getIncidentCost());
			statement.setString(7, incidentDTO.getIncidentDescription());
			statement.setString(8, incidentDTO.getIncidentStatus().toUpperCase());
			statement.setString(9, authDTO.getEmlployeeDTO().getCode());
			statement.setInt(10, incidentDTO.getActiveFlag());
			statement.registerOutParameter(11, Types.INTEGER);

			statement.execute();
			affected = statement.getInt(11);

			logger.info("Inserted/Updated incident with code: {}", incidentDTO.getCode());

		} catch (Exception e) {
			logger.error("Error in incident record: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

		return affected;
	}

	public IncidentDTO getIncidentByCode(IncidentDTO incidentDTO) {
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			PreparedStatement statement = null;

			if (incidentDTO.getCode() != null) {
				String query = "select id, code, customer_id, incident_date, incident_type, incident_inspector, incident_cost, incident_description, incident_status, active_flag  from incident where code = ? and active_flag = 1";
				statement = connection.prepareStatement(query);
				statement.setString(1, incidentDTO.getCode());
			} else if (incidentDTO.getId() != 0) {
				String query = "select id, code, customer_id, incident_date, incident_type, incident_inspector, incident_cost, incident_description, incident_status, active_flag  from incident where id = ? and active_flag = 1";
				statement = connection.prepareStatement(query);
				statement.setInt(1, incidentDTO.getId());
			}

			@Cleanup
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				incidentDTO.setId(rs.getInt("id"));
				incidentDTO.setCode(rs.getString("code"));
				CustomerDTO customerDTO = new CustomerDTO();
				customerDTO.setId(rs.getInt("customer_id"));
				incidentDTO.setCustomerDTO(customerDTO);
				incidentDTO.setIncidentDate(rs.getString("incident_date"));
				incidentDTO.setIncidentType(rs.getString("incident_type"));
				incidentDTO.setIncidentInspector(rs.getString("incident_inspector"));
				incidentDTO.setIncidentCost(rs.getBigDecimal("incident_cost"));
				incidentDTO.setIncidentDescription(rs.getString("incident_description"));
				incidentDTO.setIncidentStatus(rs.getString("incident_status"));
				incidentDTO.setActiveFlag(rs.getInt("active_flag"));
			}

			logger.info("Fetched incident by code: {}", incidentDTO.getCode());

		} catch (Exception e) {
			logger.error("Error fetching incident: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

		return incidentDTO;
	}

	public List<IncidentDTO> getAllIncident() {
		List<IncidentDTO> list = new ArrayList<>();
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "select id, code, customer_id, incident_date, incident_type, incident_inspector, incident_cost, incident_description, incident_status, active_flag  from incident where active_flag = 1";
			PreparedStatement statement = connection.prepareStatement(query);

			@Cleanup
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				IncidentDTO dto = new IncidentDTO();
				dto.setId(rs.getInt("id"));
				dto.setCode(rs.getString("code"));
				CustomerDTO customerDTO = new CustomerDTO();
				customerDTO.setId(rs.getInt("customer_id"));
				dto.setCustomerDTO(customerDTO);
				dto.setIncidentDate(rs.getString("incident_date"));
				dto.setIncidentType(rs.getString("incident_type"));
				dto.setIncidentInspector(rs.getString("incident_inspector"));
				dto.setIncidentCost(rs.getBigDecimal("incident_cost"));
				dto.setIncidentDescription(rs.getString("incident_description"));
				dto.setIncidentStatus(rs.getString("incident_status"));
				dto.setActiveFlag(rs.getInt("active_flag"));
				list.add(dto);
			}

			logger.info("Fetched all active incidents");

		} catch (Exception e) {
			logger.error("Error fetching all incidents: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

		return list;
	}

}
