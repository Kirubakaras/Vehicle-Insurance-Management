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
import com.ezee.insurence.dto.PolicyDTO;
import com.ezee.insurence.dto.ReciptDTO;
import com.ezee.insurence.dto.RenewalDTO;
import com.ezee.insurence.exception.ServiceException;

import lombok.Cleanup;

@Repository
public class RenewalDAO {

	@Autowired
	private DataSource dataSource;

	private static final Logger logger = LogManager.getLogger("com.ezee.insurence.dao");

	public int renewalUID(AuthDTO authDTO, RenewalDTO renewalDTO) {
		int affected = 0;
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "{call sp_renewal_uid(?,?,?,?,?,?,?,?,?)}";
			@Cleanup
			CallableStatement stmt = connection.prepareCall(query);

			stmt.setString(1, renewalDTO.getCode());
			stmt.setString(2, renewalDTO.getPolicyDTO().getCode());
			stmt.setString(3, renewalDTO.getRenewalDate());
			stmt.setString(4, renewalDTO.getNewExpriyDate());
			stmt.setBigDecimal(5, renewalDTO.getRenewalAmount());
			stmt.setString(6, renewalDTO.getRenewalStatus().toUpperCase());
			stmt.setString(7, authDTO.getEmlployeeDTO().getCode());
			stmt.setInt(8, renewalDTO.getActiveFlag());
			stmt.registerOutParameter(9, Types.INTEGER);

			stmt.execute();
			affected = stmt.getInt(9);

			logger.info("Inserted/Updated renewal with code: {}", renewalDTO.getCode());

		} catch (Exception e) {
			logger.error("Error in renewalUID: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

		return affected;
	}

	public RenewalDTO getRenewalByCode(RenewalDTO renewalDTO) {
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			PreparedStatement stmt = null;

			if (renewalDTO.getCode() != null) {
				stmt = connection.prepareStatement(
						"select id, code, policy_id, renewal_date, newexpriy_date, renewal_amount, renewal_status, active_flag from renewal where code = ? and active_flag = 1");
				stmt.setString(1, renewalDTO.getCode());
			} else if (renewalDTO.getId() != 0) {
				stmt = connection.prepareStatement(
						"select id, code, policy_id, renewal_date, newexpriy_date, renewal_amount, renewal_status, active_flag from renewal where id = ? and active_flag = 1");
				stmt.setInt(1, renewalDTO.getId());
			}

			@Cleanup
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				renewalDTO.setId(rs.getInt("id"));
				renewalDTO.setCode(rs.getString("code"));

				PolicyDTO policyDTO = new PolicyDTO();
				policyDTO.setId(rs.getInt("policy_id"));
				renewalDTO.setPolicyDTO(policyDTO);

				renewalDTO.setRenewalDate(rs.getDate("renewal_date").toString());
				renewalDTO.setNewExpriyDate(rs.getDate("newexpriy_date").toString());
				renewalDTO.setRenewalAmount(rs.getBigDecimal("renewal_amount"));
				renewalDTO.setRenewalStatus(rs.getString("renewal_status"));
				renewalDTO.setActiveFlag(rs.getInt("active_flag"));
			}

			logger.info("Fetched renewal by code: {}", renewalDTO.getCode());

		} catch (Exception e) {
			logger.error("Error fetching renewal by code: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

		return renewalDTO;
	}

	public List<RenewalDTO> getAllRenewal() {
		List<RenewalDTO> list = new ArrayList<>();
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			String query = "select id, code, policy_id, renewal_date, newexpriy_date, renewal_amount, renewal_status, active_flag from renewal where active_flag = 1";

			@Cleanup
			PreparedStatement stmt = connection.prepareStatement(query);
			@Cleanup
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				RenewalDTO dto = new RenewalDTO();
				dto.setId(rs.getInt("id"));
				dto.setCode(rs.getString("code"));

				PolicyDTO policyDTO = new PolicyDTO();
				policyDTO.setId(rs.getInt("policy_id"));
				dto.setPolicyDTO(policyDTO);

				dto.setRenewalDate(rs.getString("renewal_date"));
				dto.setNewExpriyDate(rs.getString("newexpriy_date"));
				dto.setRenewalAmount(rs.getBigDecimal("renewal_amount"));
				dto.setRenewalStatus(rs.getString("renewal_status"));
				dto.setActiveFlag(rs.getInt("active_flag"));

				list.add(dto);
			}

			logger.info("Fetched all active renewals");

		} catch (Exception e) {
			logger.error("Error fetching renewal list: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

		return list;
	}

	public RenewalDTO getReciptByRenewalCode(RenewalDTO renewalDTO) {
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "select id, code, policy_id, customer_id, recipt_amount, penalty_amount, recipt_date, due_date, recipt_status, active_flag from recipt where renewal_id = ? and active_flag = 1";
			@Cleanup
			PreparedStatement reciptPS = connection.prepareStatement(query);

			String renewalQuery = "select id, code, policy_id, renewal_date, newexpriy_date, renewal_amount, renewal_status, active_flag from renewal where code = ? and active_flag = 1";
			@Cleanup
			PreparedStatement renewalPS = connection.prepareStatement(renewalQuery);
			renewalPS.setString(1, renewalDTO.getCode());

			@Cleanup
			ResultSet renewalRS = renewalPS.executeQuery();

			if (renewalRS.next()) {
				renewalDTO.setId(renewalRS.getInt("id"));
				renewalDTO.setCode(renewalRS.getString("code"));

				PolicyDTO policyDTO = new PolicyDTO();
				policyDTO.setId(renewalRS.getInt("policy_id"));
				renewalDTO.setPolicyDTO(policyDTO);

				renewalDTO.setRenewalDate(renewalRS.getString("renewal_date"));
				renewalDTO.setNewExpriyDate(renewalRS.getString("newexpriy_date"));
				renewalDTO.setRenewalAmount(renewalRS.getBigDecimal("renewal_amount"));
				renewalDTO.setRenewalStatus(renewalRS.getString("renewal_status"));
				renewalDTO.setActiveFlag(renewalRS.getInt("active_flag"));
			} 

			reciptPS.setInt(1, renewalDTO.getId());

			@Cleanup
			ResultSet reciptRS = reciptPS.executeQuery();

			List<ReciptDTO> reciptList = new ArrayList<>();
			while (reciptRS.next()) {
				ReciptDTO reciptDTO = new ReciptDTO();
				reciptDTO.setId(reciptRS.getInt("id"));
				reciptDTO.setCode(reciptRS.getString("code"));

				PolicyDTO policyDTO = new PolicyDTO();
				policyDTO.setId(reciptRS.getInt("policy_id"));
				reciptDTO.setPolicyDTO(policyDTO);

				CustomerDTO customerDTO = new CustomerDTO();
				customerDTO.setId(reciptRS.getInt("customer_id"));
				reciptDTO.setCustomerDTO(customerDTO);

				reciptDTO.setReciptAmount(reciptRS.getBigDecimal("recipt_amount"));
				reciptDTO.setPenaltyAmount(reciptRS.getBigDecimal("penalty_amount"));
				reciptDTO.setReciptDate(reciptRS.getString("recipt_date"));
				reciptDTO.setDueDate(reciptRS.getString("due_date"));
				reciptDTO.setReciptStatus(reciptRS.getString("recipt_status"));
				reciptDTO.setActiveFlag(reciptRS.getInt("active_flag"));

				reciptList.add(reciptDTO);
			}
			renewalDTO.setReciptDTO(reciptList);
			logger.info("Fetched receipts by renewal code: {}", renewalDTO.getCode());

		} catch (Exception e) {
			logger.error("Error fetching receipts for renewal {}", e.getMessage());
			throw new ServiceException(e.getMessage());
		}

		return renewalDTO;
	}

}
