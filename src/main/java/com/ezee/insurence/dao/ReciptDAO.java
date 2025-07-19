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
public class ReciptDAO {
	@Autowired
	private DataSource dataSource;

	private static final Logger logger = LogManager.getLogger("com.ezee.insurence.dao");

	public int reciptUID(AuthDTO authDTO, ReciptDTO reciptDTO) {
		int affected = 0;
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "{call sp_recipt_uid(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
			@Cleanup
			CallableStatement stmt = connection.prepareCall(query);

			stmt.setString(1, reciptDTO.getCode());
			stmt.setString(2, reciptDTO.getPolicyDTO() != null ? reciptDTO.getPolicyDTO().getCode() : null);
			stmt.setString(3, reciptDTO.getCustomerDTO().getCode());
			stmt.setString(4, reciptDTO.getRenewalDTO() != null ? reciptDTO.getRenewalDTO().getCode() : null);
			stmt.setBigDecimal(5, reciptDTO.getReciptAmount());
			stmt.setBigDecimal(6, reciptDTO.getPenaltyAmount());
			stmt.setString(7, reciptDTO.getReciptDate());
			stmt.setString(8, reciptDTO.getDueDate());
			stmt.setBigDecimal(9, reciptDTO.getReciptTotalAmount());
			stmt.setString(10, reciptDTO.getReciptStatus());
			stmt.setString(11, authDTO.getEmlployeeDTO().getCode());
			stmt.setInt(12, reciptDTO.getActiveFlag());
			stmt.registerOutParameter(13, Types.INTEGER);

			stmt.execute();
			affected = stmt.getInt(13);

			logger.info("Inserted/Updated receipt with code: {}", reciptDTO.getCode());

		} catch (Exception e) {
			logger.error("Error in reciptUID: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

		return affected;
	}

	public ReciptDTO getReciptByCode(ReciptDTO reciptDTO) {
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			PreparedStatement stmt = null;

			if (reciptDTO.getCode() != null) {
				stmt = connection.prepareStatement(
						"select id, code, policy_id, customer_id, renewal_id, recipt_amount, penalty_amount, recipt_date, due_date, recipt_total_amount, recipt_status, update_by, update_at, active_flag from recipt where code = ? and active_flag = 1");
				stmt.setString(1, reciptDTO.getCode());
			} else if (reciptDTO.getId() != 0) {
				stmt = connection.prepareStatement(
						"select id, code, policy_id, customer_id, renewal_id, recipt_amount, penalty_amount, recipt_date, due_date, recipt_total_amount, recipt_status, update_by, update_at, active_flag from recipt where id = ? and active_flag = 1");
				stmt.setInt(1, reciptDTO.getId());
			}

			@Cleanup
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				reciptDTO.setId(rs.getInt("id"));
				reciptDTO.setCode(rs.getString("code"));

				CustomerDTO customer = new CustomerDTO();
				customer.setId(rs.getInt("customer_id"));
				reciptDTO.setCustomerDTO(customer);
				if (rs.getInt("policy_id") != 0) {
					PolicyDTO policy = new PolicyDTO();
					policy.setId(rs.getInt("policy_id"));
					reciptDTO.setPolicyDTO(policy);
				}
				if (rs.getInt("renewal_id") != 0) {
					RenewalDTO renewal = new RenewalDTO();
					renewal.setId(rs.getInt("renewal_id"));
					reciptDTO.setRenewalDTO(renewal);
				}

				reciptDTO.setReciptAmount(rs.getBigDecimal("recipt_amount"));
				reciptDTO.setPenaltyAmount(rs.getBigDecimal("penalty_amount"));
				reciptDTO.setReciptDate(rs.getString("recipt_date"));
				reciptDTO.setDueDate(rs.getString("due_date"));
				reciptDTO.setReciptTotalAmount(rs.getBigDecimal("recipt_total_amount"));
				reciptDTO.setReciptStatus(rs.getString("recipt_status"));
				reciptDTO.setActiveFlag(rs.getInt("active_flag"));
			}

			logger.info("Fetched receipt with code: {}", reciptDTO.getCode());

		} catch (Exception e) {
			logger.error("Error fetching receipt by code: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

		return reciptDTO;
	}

	public List<ReciptDTO> getAllRecipt() {
		List<ReciptDTO> list = new ArrayList<>();
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "select id, code, policy_id, customer_id, renewal_id, recipt_amount, penalty_amount, recipt_date, due_date, recipt_total_amount, recipt_status, update_by, update_at, active_flag from recipt where active_flag = 1";
			PreparedStatement stmt = connection.prepareStatement(query);
			@Cleanup
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				ReciptDTO dto = new ReciptDTO();
				dto.setId(rs.getInt("id"));
				dto.setCode(rs.getString("code"));

				CustomerDTO customer = new CustomerDTO();
				customer.setId(rs.getInt("customer_id"));
				dto.setCustomerDTO(customer);

				if (rs.getInt("policy_id") != 0) {
					PolicyDTO policy = new PolicyDTO();
					policy.setId(rs.getInt("policy_id"));
					dto.setPolicyDTO(policy);
				}
				if (rs.getInt("renewal_id") != 0) {
					RenewalDTO renewal = new RenewalDTO();
					renewal.setId(rs.getInt("renewal_id"));
					dto.setRenewalDTO(renewal);
				}

				dto.setReciptAmount(rs.getBigDecimal("recipt_amount"));
				dto.setPenaltyAmount(rs.getBigDecimal("penalty_amount"));
				dto.setReciptDate(rs.getString("recipt_date"));
				dto.setDueDate(rs.getString("due_date"));
				dto.setReciptTotalAmount(rs.getBigDecimal("recipt_total_amount"));
				dto.setReciptStatus(rs.getString("recipt_status"));
				dto.setActiveFlag(rs.getInt("active_flag"));

				list.add(dto);
			}

			logger.info("Fetched all active receipts");

		} catch (Exception e) {
			logger.error("Error fetching receipts: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

		return list;
	}

	public int countReceiptsByPolicy(PolicyDTO policyDTO) {
		int count = 0;
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "select count(id) as receipt_count from recipt where policy_id = ? and active_flag = 1";
			@Cleanup
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, policyDTO.getId());

			@Cleanup
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				count = rs.getInt("receipt_count");
			}

			logger.info("Fetched receipt count for policy");

		} catch (Exception e) {
			logger.error("Error counting receipts for policy {}", e.getMessage());
			throw new ServiceException(e.getMessage());
		}

		return count;
	}
}
