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
import com.ezee.insurence.dto.ClaimDTO;
import com.ezee.insurence.dto.VehicleDTO;
import com.ezee.insurence.dto.VehicleServiceDTO;
import com.ezee.insurence.exception.ServiceException;

import lombok.Cleanup;

@Repository
public class VehicleServiceDAO {

	@Autowired
	private DataSource dataSource;

	private static final Logger logger = LogManager.getLogger("com.ezee.insurence.dao");

	public int vehicleServiceUID(AuthDTO authDTO, VehicleServiceDTO serviceDTO) {
		int affected = 0;
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "{call sp_vehicleservice_uid(?,?,?,?,?,?,?,?,?,?)}";
			@Cleanup
			CallableStatement stmt = connection.prepareCall(query);

			stmt.setString(1, serviceDTO.getCode());
			stmt.setString(2, serviceDTO.getVehicleDTO().getCode());
			stmt.setString(3, serviceDTO.getClaimDTO().getCode());
			stmt.setString(4, serviceDTO.getServiceDate());
			stmt.setString(5, serviceDTO.getServiceDescription());
			stmt.setBigDecimal(6, serviceDTO.getServiceCost());
			stmt.setString(7, serviceDTO.getServiceStatus().toUpperCase());
			stmt.setString(8, authDTO.getEmlployeeDTO().getCode());
			stmt.setInt(9, serviceDTO.getActiveFlag());
			stmt.registerOutParameter(10, Types.INTEGER);

			stmt.execute();
			affected = stmt.getInt(10);

			logger.info("Inserted/Updated vehicle service with code: {}", serviceDTO.getCode());

		} catch (Exception e) {
			logger.error("Error in vehicle service record: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

		return affected;
	}

	public VehicleServiceDTO getVehicleServiceDTO(VehicleServiceDTO serviceDTO) {
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			PreparedStatement stmt = null;

			if (serviceDTO.getCode() != null) {
				stmt = connection.prepareStatement(
						"select id, code, vehicle_id, claim_id, service_date, service_description, service_cost, service_status, update_by, update_at, active_flag from vehicleservice where code = ? and active_flag = 1");
				stmt.setString(1, serviceDTO.getCode());
			} else if (serviceDTO.getId() != 0) {
				stmt = connection.prepareStatement(
						"select id, code, vehicle_id, claim_id, service_date, service_description, service_cost, service_status, update_by, update_at, active_flag from vehicleservice where id = ? and active_flag = 1");
				stmt.setInt(1, serviceDTO.getId());
			}

			@Cleanup
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				serviceDTO.setId(rs.getInt("id"));
				serviceDTO.setCode(rs.getString("code"));

				VehicleDTO vehicleDTO = new VehicleDTO();
				vehicleDTO.setId(rs.getInt("vehicle_id"));
				serviceDTO.setVehicleDTO(vehicleDTO);

				ClaimDTO claimDTO = new ClaimDTO();
				claimDTO.setId(rs.getInt("claim_id"));
				serviceDTO.setClaimDTO(claimDTO);

				serviceDTO.setServiceDate(rs.getDate("service_date").toString());
				serviceDTO.setServiceDescription(rs.getString("service_description"));
				serviceDTO.setServiceCost(rs.getBigDecimal("service_cost"));
				serviceDTO.setServiceStatus(rs.getString("service_status"));
				serviceDTO.setActiveFlag(rs.getInt("active_flag"));
			}

			logger.info("Fetched vehicle service by code: {}", serviceDTO.getCode());

		} catch (Exception e) {
			logger.error("Error fetching vehicle service: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

		return serviceDTO;
	}

	public List<VehicleServiceDTO> getAllVehicleService() {
		List<VehicleServiceDTO> list = new ArrayList<>();
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			String query = "select id, code, vehicle_id, claim_id, service_date, service_description, service_cost, service_status, update_by, update_at, active_flag FROM vehicleservice WHERE active_flag = 1";

			@Cleanup
			PreparedStatement stmt = connection.prepareStatement(query);
			@Cleanup
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				VehicleServiceDTO dto = new VehicleServiceDTO();
				dto.setId(rs.getInt("id"));
				dto.setCode(rs.getString("code"));

				VehicleDTO vehicleDTO = new VehicleDTO();
				vehicleDTO.setId(rs.getInt("vehicle_id"));
				dto.setVehicleDTO(vehicleDTO);

				ClaimDTO claimDTO = new ClaimDTO();
				claimDTO.setId(rs.getInt("claim_id"));
				dto.setClaimDTO(claimDTO);

				dto.setServiceDate(rs.getDate("service_date").toString());
				dto.setServiceDescription(rs.getString("service_description"));
				dto.setServiceCost(rs.getBigDecimal("service_cost"));
				dto.setServiceStatus(rs.getString("service_status"));
				dto.setActiveFlag(rs.getInt("active_flag"));

				list.add(dto);
			}

			logger.info("Fetched all active vehicle service records");

		} catch (Exception e) {
			logger.error("Error fetching vehicle service list: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

		return list;
	}

}
