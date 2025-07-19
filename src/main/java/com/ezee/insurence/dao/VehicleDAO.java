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
import com.ezee.insurence.dto.CustomerDTO;
import com.ezee.insurence.dto.PolicyDTO;
import com.ezee.insurence.dto.VehicleDTO;
import com.ezee.insurence.dto.VehicleServiceDTO;
import com.ezee.insurence.exception.ServiceException;

import lombok.Cleanup;

@Repository
public class VehicleDAO {
	@Autowired
	private DataSource dataSource;

	private static final Logger logger = LogManager.getLogger("com.ezee.insurence.dao");

	public int vehicleUID(AuthDTO authDTO, VehicleDTO vehicleDTO) {
		int affected = 0;
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "{call sp_vehicle_uid(?,?,?,?,?,?,?,?,?,?,?)}";
			@Cleanup
			CallableStatement statement = connection.prepareCall(query);

			statement.setString(1, vehicleDTO.getCode());
			statement.setString(2, vehicleDTO.getCustomerDTO().getCode());
			statement.setString(3, vehicleDTO.getVehiclePlateNum());
			statement.setString(4, vehicleDTO.getVehicleType().toUpperCase());
			statement.setString(5, vehicleDTO.getVehicleEnginNum());
			statement.setString(6, vehicleDTO.getVehicleChasisNum());
			statement.setString(7, vehicleDTO.getVehicleNumber());
			statement.setString(8, vehicleDTO.getVehicleModelNum());
			statement.setString(9, authDTO.getEmlployeeDTO().getCode());
			statement.setInt(10, vehicleDTO.getActiveFlag());
			statement.registerOutParameter(11, Types.INTEGER);

			statement.execute();
			affected = statement.getInt(11);

			logger.info("Inserted/Updated vehicle with code");

		} catch (Exception e) {
			logger.error("Error in vehicle record: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

		return affected;
	}

	public VehicleDTO getVehicleByCode(VehicleDTO vehicleDTO) {
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			PreparedStatement statement = null;

			if (vehicleDTO.getCode() != null) {
				String query = "select id ,code ,customer_id, vehicle_plate_num, vehicle_type, vehicle_engin_num, vehicle_chasis_num, vehicle_number, vehicle_model_num, active_flag from vehicle where code = ? and active_flag = 1";
				statement = connection.prepareStatement(query);
				statement.setString(1, vehicleDTO.getCode());
			} else if (vehicleDTO.getId() != 0) {
				String query = "select id ,code ,customer_id, vehicle_plate_num, vehicle_type, vehicle_engin_num, vehicle_chasis_num, vehicle_number, vehicle_model_num, active_flag from vehicle where id = ? and active_flag = 1";
				statement = connection.prepareStatement(query);
				statement.setInt(1, vehicleDTO.getId());
			}

			@Cleanup
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				vehicleDTO.setId(rs.getInt("id"));
				vehicleDTO.setCode(rs.getString("code"));
				CustomerDTO customerDTO = new CustomerDTO();
				customerDTO.setId(rs.getInt("customer_id"));
				vehicleDTO.setCustomerDTO(customerDTO);
				vehicleDTO.setVehiclePlateNum(rs.getString("vehicle_plate_num"));
				vehicleDTO.setVehicleType(rs.getString("vehicle_type"));
				vehicleDTO.setVehicleEnginNum(rs.getString("vehicle_engin_num"));
				vehicleDTO.setVehicleChasisNum(rs.getString("vehicle_chasis_num"));
				vehicleDTO.setVehicleNumber(rs.getString("vehicle_number"));
				vehicleDTO.setVehicleModelNum(rs.getString("vehicle_model_num"));
				vehicleDTO.setActiveFlag(rs.getInt("active_flag"));
			}

			logger.info("Fetched vehicle by code: {}", vehicleDTO.getCode());

		} catch (Exception e) {
			logger.error("Error fetching vehicle: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

		return vehicleDTO;
	}

	public List<VehicleDTO> getAllVehicle() {
		List<VehicleDTO> list = new ArrayList<>();
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "select id ,code ,customer_id, vehicle_plate_num, vehicle_type, vehicle_engin_num, vehicle_chasis_num, vehicle_number, vehicle_model_num, active_flag from vehicle where active_flag = 1";
			PreparedStatement statement = connection.prepareStatement(query);

			@Cleanup
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				VehicleDTO dto = new VehicleDTO();
				dto.setId(rs.getInt("id"));
				dto.setCode(rs.getString("code"));
				CustomerDTO customerDTO = new CustomerDTO();
				customerDTO.setId(rs.getInt("customer_id"));
				dto.setCustomerDTO(customerDTO);
				dto.setVehiclePlateNum(rs.getString("vehicle_plate_num"));
				dto.setVehicleType(rs.getString("vehicle_type"));
				dto.setVehicleEnginNum(rs.getString("vehicle_engin_num"));
				dto.setVehicleChasisNum(rs.getString("vehicle_chasis_num"));
				dto.setVehicleNumber(rs.getString("vehicle_number"));
				dto.setVehicleModelNum(rs.getString("vehicle_model_num"));
				dto.setActiveFlag(rs.getInt("active_flag"));
				list.add(dto);
			}

			logger.info("Fetched all active vehicles");

		} catch (Exception e) {
			logger.error("Error fetching vehicles: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return list;
	}

	public VehicleDTO getPolicyByVehicle(VehicleDTO vehicleDTO) {
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String vehicleQuery = "select id ,code ,customer_id, vehicle_plate_num, vehicle_type, vehicle_engin_num, vehicle_chasis_num, vehicle_number, vehicle_model_num, active_flag from vehicle where code = ? and active_flag = 1";
			@Cleanup
			PreparedStatement vehiclePS = connection.prepareStatement(vehicleQuery);
			vehiclePS.setString(1, vehicleDTO.getCode());

			@Cleanup
			ResultSet vehicleRS = vehiclePS.executeQuery();

			if (vehicleRS.next()) {
				vehicleDTO.setId(vehicleRS.getInt("id"));
				vehicleDTO.setCode(vehicleRS.getString("code"));

				CustomerDTO customerDTO = new CustomerDTO();
				customerDTO.setId(vehicleRS.getInt("customer_id"));
				vehicleDTO.setCustomerDTO(customerDTO);
				
				vehicleDTO.setVehiclePlateNum(vehicleRS.getString("vehicle_plate_num"));
				vehicleDTO.setVehicleType(vehicleRS.getString("vehicle_type"));
				vehicleDTO.setVehicleEnginNum(vehicleRS.getString("vehicle_engin_num"));
				vehicleDTO.setVehicleChasisNum(vehicleRS.getString("vehicle_chasis_num"));
				vehicleDTO.setVehicleNumber(vehicleRS.getString("vehicle_number"));
				vehicleDTO.setVehicleModelNum(vehicleRS.getString("vehicle_model_num"));
				vehicleDTO.setActiveFlag(vehicleRS.getInt("active_flag"));
			}

			String policyQuery = "select id, code, customer_id, policy_number, start_date, expiry_date, premium_amount, payment_schedule, total_policy_amount, policy_status, policy_description, active_flag from policy where vehicle_id = ? and active_flag = 1";
			@Cleanup
			PreparedStatement policyPS = connection.prepareStatement(policyQuery);
			policyPS.setInt(1, vehicleDTO.getId());

			@Cleanup
			ResultSet policyRS = policyPS.executeQuery();

			List<PolicyDTO> policyList = new ArrayList<>();
			while (policyRS.next()) {
				PolicyDTO policyDTO = new PolicyDTO();
				policyDTO.setId(policyRS.getInt("id"));
				policyDTO.setCode(policyRS.getString("code"));

				CustomerDTO customerDTO = new CustomerDTO();
				customerDTO.setId(policyRS.getInt("customer_id"));
				policyDTO.setCustomerDTO(customerDTO);

				policyDTO.setPolicyNumber(policyRS.getString("policy_number"));
				policyDTO.setStartDate(policyRS.getString("start_date"));
				policyDTO.setExpriyDate(policyRS.getString("expiry_date"));
				policyDTO.setPremiumAmount(policyRS.getBigDecimal("premium_amount"));
				policyDTO.setPaymentSchedule(policyRS.getString("payment_schedule"));
				policyDTO.setTotalAmount(policyRS.getBigDecimal("total_policy_amount"));
				policyDTO.setPolicyStatus(policyRS.getString("policy_status"));
				policyDTO.setPolicyDescription(policyRS.getString("policy_description"));
				policyDTO.setActiveFlag(policyRS.getInt("active_flag"));
				policyList.add(policyDTO);
			}
			vehicleDTO.setPolicyDTO(policyList);
			logger.info("Fetched policies for vehicle code: {}", vehicleDTO.getCode());
		} catch (Exception e) {
			logger.error("Error fetching policies for vehicle code: {}", e.getMessage());
			throw new ServiceException(e.getMessage());
		}
		return vehicleDTO;
	}

	public VehicleDTO getVehicleServiceByVehicle(VehicleDTO vehicleDTO) {
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String vehicleQuery = "select id, code, customer_id, vehicle_plate_num, vehicle_type, vehicle_engin_num, vehicle_chasis_num, vehicle_number, vehicle_model_num, active_flag from vehicle where code = ? and active_flag = 1";
			@Cleanup
			PreparedStatement vehiclePS = connection.prepareStatement(vehicleQuery);
			vehiclePS.setString(1, vehicleDTO.getCode());

			@Cleanup
			ResultSet vehicleRS = vehiclePS.executeQuery();

			if (vehicleRS.next()) {
				vehicleDTO.setId(vehicleRS.getInt("id"));
				vehicleDTO.setCode(vehicleRS.getString("code"));

				CustomerDTO customerDTO = new CustomerDTO();
				customerDTO.setId(vehicleRS.getInt("customer_id"));
				vehicleDTO.setCustomerDTO(customerDTO);

				vehicleDTO.setVehiclePlateNum(vehicleRS.getString("vehicle_plate_num"));
				vehicleDTO.setVehicleType(vehicleRS.getString("vehicle_type"));
				vehicleDTO.setVehicleEnginNum(vehicleRS.getString("vehicle_engin_num"));
				vehicleDTO.setVehicleChasisNum(vehicleRS.getString("vehicle_chasis_num"));
				vehicleDTO.setVehicleNumber(vehicleRS.getString("vehicle_number"));
				vehicleDTO.setVehicleModelNum(vehicleRS.getString("vehicle_model_num"));
				vehicleDTO.setActiveFlag(vehicleRS.getInt("active_flag"));
			}

			String serviceQuery = "select id, code, claim_id, service_date, service_description, service_cost, service_status, active_flag from vehicleservice where vehicle_id = ? and active_flag = 1";
			@Cleanup
			PreparedStatement servicePS = connection.prepareStatement(serviceQuery);
			servicePS.setInt(1, vehicleDTO.getId());

			@Cleanup
			ResultSet serviceRS = servicePS.executeQuery();

			List<VehicleServiceDTO> serviceList = new ArrayList<>();
			while (serviceRS.next()) {
				VehicleServiceDTO serviceDTO = new VehicleServiceDTO();
				serviceDTO.setId(serviceRS.getInt("id"));
				serviceDTO.setCode(serviceRS.getString("code"));

				ClaimDTO claimDTO = new ClaimDTO();
				claimDTO.setId(serviceRS.getInt("claim_id"));
				serviceDTO.setClaimDTO(claimDTO);

				serviceDTO.setServiceDate(serviceRS.getString("service_date"));
				serviceDTO.setServiceDescription(serviceRS.getString("service_description"));
				serviceDTO.setServiceCost(serviceRS.getBigDecimal("service_cost"));
				serviceDTO.setServiceStatus(serviceRS.getString("service_status"));
				serviceDTO.setActiveFlag(serviceRS.getInt("active_flag"));

				serviceList.add(serviceDTO);
			}
			vehicleDTO.setVehicleServcieDTO(serviceList);

			logger.info("Fetched vehicle service records by vehicle code: {}", vehicleDTO.getCode());

		} catch (Exception e) {
			logger.error("Error fetching vehicle service by code {}: {}", vehicleDTO.getCode(), e.getMessage());
			throw new ServiceException(e.getMessage());
		}

		return vehicleDTO;
	}

}
