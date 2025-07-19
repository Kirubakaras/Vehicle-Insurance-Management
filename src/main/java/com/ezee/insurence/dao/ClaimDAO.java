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
import com.ezee.insurence.exception.ServiceException;

import lombok.Cleanup;

@Repository
public class ClaimDAO {
	@Autowired
	private DataSource dataSource;

	private static final Logger logger = LogManager.getLogger("com.ezee.insurence.dao");

	public int claimUID(AuthDTO authDTO, ClaimDTO claimDTO) {
		int affected = 0;
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "{call sp_claim_uid(?,?,?,?,?,?,?,?,?,?,?,?)}";
			@Cleanup
			CallableStatement stmt = connection.prepareCall(query);

			stmt.setString(1, claimDTO.getCode());
			stmt.setString(2, claimDTO.getPolicyDTO().getCode());
			stmt.setString(3, claimDTO.getCustomerDTO().getCode());
			stmt.setString(4, claimDTO.getIncidentDTO().getCode());
			stmt.setString(5, claimDTO.getClaimType());
			stmt.setString(6, claimDTO.getClaimDate());
			stmt.setString(7, claimDTO.getClaimDescription());
			stmt.setBigDecimal(8, claimDTO.getClaimAmount());
			stmt.setString(9, claimDTO.getClaimStatus().toUpperCase());
			stmt.setString(10, authDTO.getEmlployeeDTO().getCode());
			stmt.setInt(11, claimDTO.getActiveFlag());
			stmt.registerOutParameter(12, Types.INTEGER);

			stmt.execute();
			affected = stmt.getInt(12);

			logger.info("Inserted/Updated claim with code: {}", claimDTO.getCode());

		} catch (Exception e) {
			logger.error("Error processing claim record: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

		return affected;
	}

	public ClaimDTO getClaimByCode(ClaimDTO claimDTO) {
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			PreparedStatement stmt = null;

			if (claimDTO.getCode() != null) {
				stmt = connection.prepareStatement(
						"select id, code, policy_id, customer_id, incident_id,claim_type, claim_date, claim_description, claim_amount, claim_status, update_by, update_at, active_flag from claim where code = ? and active_flag = 1");
				stmt.setString(1, claimDTO.getCode());
			} else if (claimDTO.getId() != 0) {
				stmt = connection.prepareStatement(
						"select id, code , policy_id, customer_id, incident_id,claim_type, claim_date, claim_description, claim_amount, claim_status, update_by, update_at, active_flag from claim where id = ? and active_flag = 1");
				stmt.setInt(1, claimDTO.getId());
			}

			@Cleanup
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				claimDTO.setId(rs.getInt("id"));
				claimDTO.setCode(rs.getString("code"));

				PolicyDTO policyDTO = new PolicyDTO();
				policyDTO.setId(rs.getInt("policy_id"));
				claimDTO.setPolicyDTO(policyDTO);

				CustomerDTO customerDTO = new CustomerDTO();
				customerDTO.setId(rs.getInt("customer_id"));
				claimDTO.setCustomerDTO(customerDTO);

				IncidentDTO incidentDTO = new IncidentDTO();
				incidentDTO.setId(rs.getInt("incident_id"));
				claimDTO.setIncidentDTO(incidentDTO);

				claimDTO.setClaimType(rs.getString("claim_type"));
				claimDTO.setClaimDate(rs.getString("claim_date"));
				claimDTO.setClaimDescription(rs.getString("claim_description"));
				claimDTO.setClaimAmount(rs.getBigDecimal("claim_amount"));
				claimDTO.setClaimStatus(rs.getString("claim_status"));
				claimDTO.setActiveFlag(rs.getInt("active_flag"));
			}

			logger.info("Fetched claim by code: {}", claimDTO.getCode());

		} catch (Exception e) {
			logger.error("Error fetching claim: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

		return claimDTO;
	}

	public List<ClaimDTO> getAllClaim() {
		List<ClaimDTO> list = new ArrayList<>();
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "select id, code, policy_id, customer_id, incident_id,claim_type, claim_date, claim_description, claim_amount, claim_status, update_by, update_at, active_flag from claim where active_flag = 1";
			@Cleanup
			PreparedStatement stmt = connection.prepareStatement(query);
			@Cleanup
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				ClaimDTO dto = new ClaimDTO();
				dto.setId(rs.getInt("id"));
				dto.setCode(rs.getString("code"));

				PolicyDTO policyDTO = new PolicyDTO();
				policyDTO.setId(rs.getInt("policy_id"));
				dto.setPolicyDTO(policyDTO);

				CustomerDTO customerDTO = new CustomerDTO();
				customerDTO.setId(rs.getInt("customer_id"));
				dto.setCustomerDTO(customerDTO);

				IncidentDTO incidentDTO = new IncidentDTO();
				incidentDTO.setId(rs.getInt("incident_id"));
				dto.setIncidentDTO(incidentDTO);

				dto.setClaimType(rs.getString("claim_type"));
				dto.setClaimDate(rs.getString("claim_date"));
				dto.setClaimDescription(rs.getString("claim_description"));
				dto.setClaimAmount(rs.getBigDecimal("claim_amount"));
				dto.setClaimStatus(rs.getString("claim_status"));
				dto.setActiveFlag(rs.getInt("active_flag"));

				list.add(dto);
			}

			logger.info("Fetched all active claims");

		} catch (Exception e) {
			logger.error("Error fetching all claims: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

		return list;
	}

	public List<ClaimDTO> getActiveClaim(ClaimDTO claimDTO) {
		List<ClaimDTO> list = new ArrayList<>();
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "select id, code, policy_id, customer_id, incident_id,claim_type, claim_date, claim_description, claim_amount, claim_status, update_by, update_at, active_flag from claim where claim_status = ? and  active_flag = 1";

			@Cleanup
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setString(1, claimDTO.getClaimStatus());

			@Cleanup
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				ClaimDTO dto = new ClaimDTO();
				dto.setId(rs.getInt("id"));
				dto.setCode(rs.getString("code"));

				PolicyDTO policyDTO = new PolicyDTO();
				policyDTO.setId(rs.getInt("policy_id"));
				dto.setPolicyDTO(policyDTO);

				CustomerDTO customerDTO = new CustomerDTO();
				customerDTO.setId(rs.getInt("customer_id"));
				dto.setCustomerDTO(customerDTO);

				IncidentDTO incidentDTO = new IncidentDTO();
				incidentDTO.setId(rs.getInt("incident_id"));
				dto.setIncidentDTO(incidentDTO);

				dto.setClaimType(rs.getString("claim_type"));
				dto.setClaimDate(rs.getString("claim_date"));
				dto.setClaimDescription(rs.getString("claim_description"));
				dto.setClaimAmount(rs.getBigDecimal("claim_amount"));
				dto.setClaimStatus(rs.getString("claim_status"));
				dto.setActiveFlag(rs.getInt("active_flag"));

				list.add(dto);
			}

			logger.info("Fetched all active claims");

		} catch (Exception e) {
			logger.error("Error fetching all claims: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

		return list;
	}

	public List<ClaimDTO> getClaimsByPolicyId(PolicyDTO policy) {
		List<ClaimDTO> claimList = new ArrayList<>();

		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "select id, code, policy_id, customer_id, incident_id, claim_type, claim_date, claim_description, claim_amount, claim_status, active_flag "
					+ "from claim where policy_id = ? and claim_status in('approved','settled')  and active_flag = 1";

			@Cleanup
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setInt(1, policy.getId());

			@Cleanup
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				ClaimDTO claimDTO = new ClaimDTO();
				claimDTO.setId(rs.getInt("id"));
				claimDTO.setCode(rs.getString("code"));

				PolicyDTO policyDTO = new PolicyDTO();
				policyDTO.setId(rs.getInt("policy_id"));
				claimDTO.setPolicyDTO(policyDTO);

				CustomerDTO customerDTO = new CustomerDTO();
				customerDTO.setId(rs.getInt("customer_id"));
				claimDTO.setCustomerDTO(customerDTO);

				IncidentDTO incidentDTO = new IncidentDTO();
				incidentDTO.setId(rs.getInt("incident_id"));
				claimDTO.setIncidentDTO(incidentDTO);

				claimDTO.setClaimType(rs.getString("claim_type"));
				claimDTO.setClaimDate(rs.getString("claim_date"));
				claimDTO.setClaimDescription(rs.getString("claim_description"));
				claimDTO.setClaimAmount(rs.getBigDecimal("claim_amount"));
				claimDTO.setClaimStatus(rs.getString("claim_status"));
				claimDTO.setActiveFlag(rs.getInt("active_flag"));

				claimList.add(claimDTO);
			}

			logger.info("Fetched claim records by policy");

		} catch (Exception e) {
			logger.error("Error fetching claim by policy: {}", e.getMessage());
			throw new ServiceException(e.getMessage());
		}

		return claimList;
	}

}
