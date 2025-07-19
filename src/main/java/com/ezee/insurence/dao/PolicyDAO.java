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
import com.ezee.insurence.dto.IncidentDTO;
import com.ezee.insurence.dto.PolicyDTO;
import com.ezee.insurence.dto.ReciptDTO;
import com.ezee.insurence.dto.RenewalDTO;
import com.ezee.insurence.dto.VehicleDTO;
import com.ezee.insurence.exception.ServiceException;

import lombok.Cleanup;

@Repository
public class PolicyDAO {

	@Autowired
	private DataSource dataSource;

	private static final Logger logger = LogManager.getLogger("com.ezee.insurence.dao");

	public int policyUID(AuthDTO authDTO, PolicyDTO policyDTO) {
		int affected = 0;
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "{call sp_policy_uid(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
			@Cleanup
			CallableStatement stmt = connection.prepareCall(query);

			stmt.setString(1, policyDTO.getCode());
			stmt.setString(2, policyDTO.getCustomerDTO().getCode());
			stmt.setString(3, policyDTO.getVehicleDTO().getCode());
			stmt.setString(4, policyDTO.getPolicyNumber());
			stmt.setString(5, policyDTO.getStartDate());
			stmt.setString(6, policyDTO.getExpriyDate());
			stmt.setBigDecimal(7, policyDTO.getPremiumAmount());
			stmt.setString(8, policyDTO.getPaymentSchedule());
			stmt.setBigDecimal(9, policyDTO.getTotalAmount());
			stmt.setString(10, policyDTO.getPolicyStatus().toUpperCase());
			stmt.setString(11, policyDTO.getPolicyDescription());
			stmt.setString(12, authDTO.getEmlployeeDTO().getCode());
			stmt.setInt(13, policyDTO.getActiveFlag());
			stmt.registerOutParameter(14, Types.INTEGER);

			stmt.execute();
			affected = stmt.getInt(14);

			logger.info("Inserted/Updated policy with code: {}", policyDTO.getCode());

		} catch (Exception e) {
			logger.error("Error processing policy record: {}", e.getMessage());
			throw new ServiceException(e.getMessage());
		}

		return affected;
	}

	public PolicyDTO getPolicyByCode(PolicyDTO policyDTO) {
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			PreparedStatement stmt = null;

			if (policyDTO.getCode() != null) {
				stmt = connection.prepareStatement(
						"select id, code, customer_id, vehicle_id, policy_number, start_date, expiry_date, premium_amount, payment_schedule, total_policy_amount, policy_status, policy_description, update_by, update_at, active_flag from policy where code = ? and active_flag = 1");
				stmt.setString(1, policyDTO.getCode());
			} else if (policyDTO.getId() != 0) {
				stmt = connection.prepareStatement(
						"select id, code, customer_id, vehicle_id, policy_number, start_date, expiry_date, premium_amount, payment_schedule, total_policy_amount, policy_status, policy_description, update_by, update_at, active_flag from policy where id = ? and active_flag = 1");
				stmt.setInt(1, policyDTO.getId());
			}

			@Cleanup
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				policyDTO.setId(rs.getInt("id"));
				policyDTO.setCode(rs.getString("code"));

				CustomerDTO customerDTO = new CustomerDTO();
				customerDTO.setId(rs.getInt("customer_id"));
				policyDTO.setCustomerDTO(customerDTO);

				VehicleDTO vehicleDTO = new VehicleDTO();
				vehicleDTO.setId(rs.getInt("vehicle_id"));
				policyDTO.setVehicleDTO(vehicleDTO);

				policyDTO.setPolicyNumber(rs.getString("policy_number"));
				policyDTO.setStartDate(rs.getDate("start_date").toString());
				policyDTO.setExpriyDate(rs.getDate("expiry_date").toString());
				policyDTO.setPremiumAmount(rs.getBigDecimal("premium_amount"));
				policyDTO.setPaymentSchedule(rs.getString("payment_schedule"));
				policyDTO.setTotalAmount(rs.getBigDecimal("total_policy_amount"));
				policyDTO.setPolicyStatus(rs.getString("policy_status"));
				policyDTO.setPolicyDescription(rs.getString("policy_description"));
				policyDTO.setActiveFlag(rs.getInt("active_flag"));
			}

			logger.info("Fetched policy by code: {}", policyDTO.getCode());

		} catch (Exception e) {
			logger.error("Error fetching policy: {}", e.getMessage());
			throw new ServiceException(e.getMessage());
		}

		return policyDTO;
	}

	public List<PolicyDTO> getAllPolicy() {
		List<PolicyDTO> list = new ArrayList<>();
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "select id, code, customer_id, vehicle_id, policy_number, start_date, expiry_date, premium_amount, payment_schedule, total_policy_amount, policy_status, policy_description, update_by, update_at, active_flag from policy where active_flag = 1";
			@Cleanup
			PreparedStatement stmt = connection.prepareStatement(query);
			@Cleanup
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				PolicyDTO dto = new PolicyDTO();
				dto.setId(rs.getInt("id"));
				dto.setCode(rs.getString("code"));

				CustomerDTO customerDTO = new CustomerDTO();
				customerDTO.setId(rs.getInt("customer_id"));
				dto.setCustomerDTO(customerDTO);

				VehicleDTO vehicleDTO = new VehicleDTO();
				vehicleDTO.setId(rs.getInt("vehicle_id"));
				dto.setVehicleDTO(vehicleDTO);

				dto.setPolicyNumber(rs.getString("policy_number"));
				dto.setStartDate(rs.getDate("start_date").toString());
				dto.setExpriyDate(rs.getDate("expiry_date").toString());
				dto.setPremiumAmount(rs.getBigDecimal("premium_amount"));
				dto.setPaymentSchedule(rs.getString("payment_schedule"));
				dto.setTotalAmount(rs.getBigDecimal("total_policy_amount"));
				dto.setPolicyStatus(rs.getString("policy_status"));
				dto.setPolicyDescription(rs.getString("policy_description"));
				dto.setActiveFlag(rs.getInt("active_flag"));
				list.add(dto);
			}

			logger.info("Fetched all active policies");

		} catch (Exception e) {
			logger.error("Error fetching all policies: {}", e.getMessage());
			throw new ServiceException(e.getMessage());
		}

		return list;
	}

	public PolicyDTO getClaimByPolicy(PolicyDTO policyDTO) {
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String policyQuery = "select id, code, customer_id, vehicle_id, policy_number, start_date, expiry_date, premium_amount, payment_schedule, total_policy_amount, policy_status, policy_description, active_flag from policy where code = ? and active_flag = 1";
			@Cleanup
			PreparedStatement policyPS = connection.prepareStatement(policyQuery);
			policyPS.setString(1, policyDTO.getCode());

			@Cleanup
			ResultSet policyRS = policyPS.executeQuery();

			if (policyRS.next()) {
				policyDTO.setId(policyRS.getInt("id"));
				policyDTO.setCode(policyRS.getString("code"));

				CustomerDTO customerDTO = new CustomerDTO();
				customerDTO.setId(policyRS.getInt("customer_id"));
				policyDTO.setCustomerDTO(customerDTO);

				VehicleDTO vehicleDTO = new VehicleDTO();
				vehicleDTO.setId(policyRS.getInt("vehicle_id"));
				policyDTO.setVehicleDTO(vehicleDTO);

				policyDTO.setPolicyNumber(policyRS.getString("policy_number"));
				policyDTO.setStartDate(policyRS.getString("start_date"));
				policyDTO.setExpriyDate(policyRS.getString("expiry_date"));
				policyDTO.setPremiumAmount(policyRS.getBigDecimal("premium_amount"));
				policyDTO.setPaymentSchedule(policyRS.getString("payment_schedule"));
				policyDTO.setTotalAmount(policyRS.getBigDecimal("total_policy_amount"));
				policyDTO.setPolicyStatus(policyRS.getString("policy_status"));
				policyDTO.setPolicyDescription(policyRS.getString("policy_description"));
				policyDTO.setActiveFlag(policyRS.getInt("active_flag"));
			}

			String claimQuery = "select id, code, customer_id, incident_id, claim_type, claim_date, claim_description, claim_amount, claim_status, active_flag from claim where policy_id = ? and active_flag = 1";
			@Cleanup
			PreparedStatement claimPS = connection.prepareStatement(claimQuery);
			claimPS.setInt(1, policyDTO.getId());

			@Cleanup
			ResultSet claimRS = claimPS.executeQuery();

			List<ClaimDTO> claimList = new ArrayList<>();
			while (claimRS.next()) {
				ClaimDTO claimDTO = new ClaimDTO();
				claimDTO.setId(claimRS.getInt("id"));
				claimDTO.setCode(claimRS.getString("code"));

				CustomerDTO customer = new CustomerDTO();
				customer.setId(claimRS.getInt("customer_id"));
				claimDTO.setCustomerDTO(customer);

				IncidentDTO incident = new IncidentDTO();
				incident.setId(claimRS.getInt("incident_id"));
				claimDTO.setIncidentDTO(incident);

				claimDTO.setClaimType(claimRS.getString("claim_type"));
				claimDTO.setClaimDate(claimRS.getDate("claim_date").toString());
				claimDTO.setClaimDescription(claimRS.getString("claim_description"));
				claimDTO.setClaimAmount(claimRS.getBigDecimal("claim_amount"));
				claimDTO.setClaimStatus(claimRS.getString("claim_status"));
				claimDTO.setActiveFlag(claimRS.getInt("active_flag"));

				claimList.add(claimDTO);
			}

			policyDTO.setClaimDTO(claimList);

			logger.info("Fetched claim records by policy code: {}", policyDTO.getCode());

		} catch (Exception e) {
			logger.error("Error fetching claims for policy {}", e.getMessage());
			throw new ServiceException(e.getMessage());
		}

		return policyDTO;
	}

	public PolicyDTO getClaimByPolicyNum(PolicyDTO policyDTO) {
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "select id, code, customer_id, incident_id, claim_type, claim_date, claim_description, claim_amount, claim_status, active_flag from claim where policy_id = ? and active_flag = 1";
			@Cleanup
			PreparedStatement claimPS = connection.prepareStatement(query);

			String policyQuery = "select id, code, customer_id, vehicle_id, policy_number, start_date, expiry_date, premium_amount, payment_schedule, total_policy_amount, policy_status, policy_description, active_flag from policy where policy_number = ? and active_flag = 1";
			@Cleanup
			PreparedStatement policyPS = connection.prepareStatement(policyQuery);
			policyPS.setString(1, policyDTO.getPolicyNumber());

			@Cleanup
			ResultSet policyRS = policyPS.executeQuery();

			if (policyRS.next()) {
				policyDTO.setId(policyRS.getInt("id"));
				policyDTO.setCode(policyRS.getString("code"));

				CustomerDTO customerDTO = new CustomerDTO();
				customerDTO.setId(policyRS.getInt("customer_id"));
				policyDTO.setCustomerDTO(customerDTO);

				VehicleDTO vehicleDTO = new VehicleDTO();
				vehicleDTO.setId(policyRS.getInt("vehicle_id"));
				policyDTO.setVehicleDTO(vehicleDTO);

				policyDTO.setPolicyNumber(policyRS.getString("policy_number"));
				policyDTO.setStartDate(policyRS.getString("start_date"));
				policyDTO.setExpriyDate(policyRS.getString("expiry_date"));
				policyDTO.setPremiumAmount(policyRS.getBigDecimal("premium_amount"));
				policyDTO.setPaymentSchedule(policyRS.getString("payment_schedule"));
				policyDTO.setTotalAmount(policyRS.getBigDecimal("total_policy_amount"));
				policyDTO.setPolicyStatus(policyRS.getString("policy_status"));
				policyDTO.setPolicyDescription(policyRS.getString("policy_description"));
				policyDTO.setActiveFlag(policyRS.getInt("active_flag"));
			}

			claimPS.setInt(1, policyDTO.getId());

			@Cleanup
			ResultSet claimRS = claimPS.executeQuery();

			List<ClaimDTO> claimList = new ArrayList<>();
			while (claimRS.next()) {
				ClaimDTO claimDTO = new ClaimDTO();
				claimDTO.setId(claimRS.getInt("id"));
				claimDTO.setCode(claimRS.getString("code"));

				CustomerDTO customer = new CustomerDTO();
				customer.setId(claimRS.getInt("customer_id"));
				claimDTO.setCustomerDTO(customer);

				IncidentDTO incident = new IncidentDTO();
				incident.setId(claimRS.getInt("incident_id"));
				claimDTO.setIncidentDTO(incident);

				claimDTO.setClaimType(claimRS.getString("claim_type"));
				claimDTO.setClaimDate(claimRS.getDate("claim_date").toString());
				claimDTO.setClaimDescription(claimRS.getString("claim_description"));
				claimDTO.setClaimAmount(claimRS.getBigDecimal("claim_amount"));
				claimDTO.setClaimStatus(claimRS.getString("claim_status"));
				claimDTO.setActiveFlag(claimRS.getInt("active_flag"));

				claimList.add(claimDTO);
			}

			policyDTO.setClaimDTO(claimList);

			logger.info("Fetched claim records by policy code: {}", policyDTO.getCode());

		} catch (Exception e) {
			logger.error("Error fetching claims for policy {}", e.getMessage());
			throw new ServiceException(e.getMessage());
		}

		return policyDTO;
	}

	public PolicyDTO getReciptByPolicyNum(PolicyDTO policyDTO) {
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query1 = "select id, code, customer_id, vehicle_id, policy_number, start_date, expiry_date, premium_amount, payment_schedule, total_policy_amount, policy_status, policy_description, active_flag from policy where policy_number = ? and active_flag = 1";
			@Cleanup
			PreparedStatement policyPS = connection.prepareStatement(query1);
			policyPS.setString(1, policyDTO.getPolicyNumber());

			@Cleanup
			ResultSet policyRS = policyPS.executeQuery();

			if (policyRS.next()) {
				policyDTO.setId(policyRS.getInt("id"));
				policyDTO.setCode(policyRS.getString("code"));

				CustomerDTO customerDTO = new CustomerDTO();
				customerDTO.setId(policyRS.getInt("customer_id"));
				policyDTO.setCustomerDTO(customerDTO);

				VehicleDTO vehicleDTO = new VehicleDTO();
				vehicleDTO.setId(policyRS.getInt("vehicle_id"));
				policyDTO.setVehicleDTO(vehicleDTO);

				policyDTO.setPolicyNumber(policyRS.getString("policy_number"));
				policyDTO.setStartDate(policyRS.getString("start_date"));
				policyDTO.setExpriyDate(policyRS.getString("expiry_date"));
				policyDTO.setPremiumAmount(policyRS.getBigDecimal("premium_amount"));
				policyDTO.setPaymentSchedule(policyRS.getString("payment_schedule"));
				policyDTO.setTotalAmount(policyRS.getBigDecimal("total_policy_amount"));
				policyDTO.setPolicyStatus(policyRS.getString("policy_status"));
				policyDTO.setPolicyDescription(policyRS.getString("policy_description"));
				policyDTO.setActiveFlag(policyRS.getInt("active_flag"));
			}


			String query = "select rc.id, rc.code, rc.customer_id, rc.renewal_id, rc.recipt_amount, rc.penalty_amount, rc.recipt_date, rc.due_date, rc.recipt_status, rc.active_flag from recipt rc  left join renewal re on rc.renewal_id = re.id where (rc.policy_id = ? or re.policy_id = ?) and rc.active_flag = 1";

			@Cleanup
			PreparedStatement reciptPS = connection.prepareStatement(query);

			reciptPS.setInt(1, policyDTO.getId());
			reciptPS.setInt(2, policyDTO.getId());

			@Cleanup
			ResultSet reciptRS = reciptPS.executeQuery();

			List<ReciptDTO> reciptList = new ArrayList<>();
			while (reciptRS.next()) {
				ReciptDTO reciptDTO = new ReciptDTO();
				reciptDTO.setId(reciptRS.getInt("id"));
				reciptDTO.setCode(reciptRS.getString("code"));

				CustomerDTO customer = new CustomerDTO();
				customer.setId(reciptRS.getInt("customer_id"));
				reciptDTO.setCustomerDTO(customer);

				RenewalDTO renewal = new RenewalDTO();
				renewal.setId(reciptRS.getInt("renewal_id"));
				reciptDTO.setRenewalDTO(renewal);

				reciptDTO.setReciptAmount(reciptRS.getBigDecimal("recipt_amount"));
				reciptDTO.setPenaltyAmount(reciptRS.getBigDecimal("penalty_amount"));
				reciptDTO.setReciptDate(reciptRS.getString("recipt_date"));
				reciptDTO.setDueDate(reciptRS.getString("due_date"));
				reciptDTO.setReciptStatus(reciptRS.getString("recipt_status"));
				reciptDTO.setActiveFlag(reciptRS.getInt("active_flag"));

				reciptList.add(reciptDTO);
			}

			policyDTO.setReciptDTO(reciptList);
			logger.info("Fetched receipt records by policy code: {}", policyDTO.getCode());

		} catch (Exception e) {
			logger.error("Error fetching receipt records for policy {}", e.getMessage());
			throw new ServiceException(e.getMessage());
		}

		return policyDTO;
	}

	public PolicyDTO getRenewalByPolicyNumber(PolicyDTO policyDTO) {
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "select id, code, renewal_date, newexpriy_date, renewal_amount, renewal_status, active_flag from renewal where policy_id = ? and active_flag = 1";

			String policyQuery = "select id, code, customer_id, vehicle_id, start_date, expiry_date, premium_amount, payment_schedule, total_policy_amount, policy_status, policy_description, active_flag from policy where policy_number = ? and active_flag = 1";
			@Cleanup
			PreparedStatement policyPS = connection.prepareStatement(policyQuery);
			policyPS.setString(1, policyDTO.getPolicyNumber());

			@Cleanup
			ResultSet policyRS = policyPS.executeQuery();

			if (policyRS.next()) {
				policyDTO.setId(policyRS.getInt("id"));
				policyDTO.setCode(policyRS.getString("code"));

				CustomerDTO customer = new CustomerDTO();
				customer.setId(policyRS.getInt("customer_id"));
				policyDTO.setCustomerDTO(customer);

				VehicleDTO vehicle = new VehicleDTO();
				vehicle.setId(policyRS.getInt("vehicle_id"));
				policyDTO.setVehicleDTO(vehicle);

				policyDTO.setStartDate(policyRS.getString("start_date"));
				policyDTO.setExpriyDate(policyRS.getString("expiry_date"));
				policyDTO.setPremiumAmount(policyRS.getBigDecimal("premium_amount"));
				policyDTO.setPaymentSchedule(policyRS.getString("payment_schedule"));
				policyDTO.setTotalAmount(policyRS.getBigDecimal("total_policy_amount"));
				policyDTO.setPolicyStatus(policyRS.getString("policy_status"));
				policyDTO.setPolicyDescription(policyRS.getString("policy_description"));
				policyDTO.setActiveFlag(policyRS.getInt("active_flag"));
			}

			@Cleanup
			PreparedStatement renewalPS = connection.prepareStatement(query);
			renewalPS.setInt(1, policyDTO.getId());

			@Cleanup
			ResultSet renewalRS = renewalPS.executeQuery();

			List<RenewalDTO> renewalList = new ArrayList<>();
			while (renewalRS.next()) {
				RenewalDTO renewalDTO = new RenewalDTO();
				renewalDTO.setId(renewalRS.getInt("id"));
				renewalDTO.setCode(renewalRS.getString("code"));
				renewalDTO.setRenewalDate(renewalRS.getString("renewal_date"));
				renewalDTO.setNewExpriyDate(renewalRS.getString("newexpriy_date"));
				renewalDTO.setRenewalAmount(renewalRS.getBigDecimal("renewal_amount"));
				renewalDTO.setRenewalStatus(renewalRS.getString("renewal_status"));
				renewalDTO.setActiveFlag(renewalRS.getInt("active_flag"));
				renewalList.add(renewalDTO);
			}

			policyDTO.setRenewalDTO(renewalList);
			logger.info("Fetched renewal records by policy number: {}", policyDTO.getPolicyNumber());

		} catch (Exception e) {
			logger.error("Error fetching renewal for policy: {}", e.getMessage());
			throw new ServiceException(e.getMessage());
		}

		return policyDTO;
	}

}
