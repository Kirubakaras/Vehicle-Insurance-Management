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
import com.ezee.insurence.dto.PaymentDTO;
import com.ezee.insurence.dto.ReciptDTO;
import com.ezee.insurence.exception.ErrorCode;
import com.ezee.insurence.exception.ServiceException;

import lombok.Cleanup;

@Repository
public class PaymentDAO {

	@Autowired
	private DataSource dataSource;

	private static final Logger logger = LogManager.getLogger("com.ezee.insurence.dao");

	public PaymentDTO paymentUID(AuthDTO authDTO, PaymentDTO paymentDTO) {
		
		try {
			@Cleanup
			Connection conn = dataSource.getConnection();

			String query = "{call sp_payment_uid(?,?,?,?,?,?,?,?,?)}";
			@Cleanup
			CallableStatement stmt = conn.prepareCall(query);

			stmt.setString(1, paymentDTO.getCode());
			stmt.setString(2, paymentDTO.getReciptDTO().getCode());
			stmt.setString(3, paymentDTO.getCustomerDTO().getCode());
			stmt.setString(4, paymentDTO.getPaymentDate());
			stmt.setBigDecimal(5, paymentDTO.getPaymentAmount());
			stmt.setString(6, paymentDTO.getPaymentMode().toUpperCase());
			stmt.setString(7, authDTO.getEmlployeeDTO().getCode());
			stmt.setInt(8, paymentDTO.getActiveFlag());
			stmt.registerOutParameter(9, Types.INTEGER);

			stmt.execute();
			int affected = stmt.getInt(9);
			
			if(affected == 0) {
				throw new ServiceException(ErrorCode.PAYMENT_NOT_CREATE);
			}
			logger.info("Inserted/Updated payment ");

		} catch (Exception e) {
			logger.error("Error while insert a paymentUID: {}", e.getMessage());
			throw new ServiceException(e.getMessage());
		}
		return paymentDTO;
	}

	public PaymentDTO getPaymentByCode(PaymentDTO paymentDTO) {
		try {
			@Cleanup
			Connection conn = dataSource.getConnection();
			PreparedStatement stmt = null;

			if (paymentDTO.getCode() != null) {
				stmt = conn.prepareStatement(
						"select id, code, recipt_id, customer_id, payment_date, payment_amount, payment_mode, update_by, update_at, active_flag  from payment where code = ? and active_flag = 1");
				stmt.setString(1, paymentDTO.getCode());
			} else if (paymentDTO.getId() != 0) {
				stmt = conn.prepareStatement(
						"select id, code, recipt_id, customer_id, payment_date, payment_amount, payment_mode, update_by, update_at, active_flag  from payment where id = ? and active_flag = 1");
				stmt.setInt(1, paymentDTO.getId());
			}

			@Cleanup
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				paymentDTO.setId(rs.getInt("id"));
				paymentDTO.setCode(rs.getString("code"));

				ReciptDTO recipt = new ReciptDTO();
				recipt.setId(rs.getInt("recipt_id"));
				paymentDTO.setReciptDTO(recipt);

				CustomerDTO customer = new CustomerDTO();
				customer.setId(rs.getInt("customer_id"));
				paymentDTO.setCustomerDTO(customer);

				paymentDTO.setPaymentDate(rs.getDate("payment_date").toString());
				paymentDTO.setPaymentAmount(rs.getBigDecimal("payment_amount"));
				paymentDTO.setPaymentMode(rs.getString("payment_mode"));
				paymentDTO.setActiveFlag(rs.getInt("active_flag"));
			}

			logger.info("Fetched payment with code: {}", paymentDTO.getCode());

		} catch (Exception e) {
			logger.error("Error while fetching  the payment: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

		return paymentDTO;
	}

	public List<PaymentDTO> getAllPayments() {
		List<PaymentDTO> list = new ArrayList<>();
		try {
			@Cleanup
			Connection conn = dataSource.getConnection();
			String query = "select id, code, recipt_id, customer_id, payment_date, payment_amount, payment_mode, update_by, update_at, active_flag from payment where active_flag = 1";
			@Cleanup
			PreparedStatement stmt = conn.prepareStatement(query);
			@Cleanup
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				PaymentDTO dto = new PaymentDTO();
				dto.setId(rs.getInt("id"));
				dto.setCode(rs.getString("code"));

				ReciptDTO recipt = new ReciptDTO();
				recipt.setId(rs.getInt("recipt_id"));
				dto.setReciptDTO(recipt);

				CustomerDTO customer = new CustomerDTO();
				customer.setId(rs.getInt("customer_id"));
				dto.setCustomerDTO(customer);

				dto.setPaymentDate(rs.getDate("payment_date").toString());
				dto.setPaymentAmount(rs.getBigDecimal("payment_amount"));
				dto.setPaymentMode(rs.getString("payment_mode"));
				dto.setActiveFlag(rs.getInt("active_flag"));

				list.add(dto);
			}

			logger.info("Fetched all active payment");

		} catch (Exception e) {
			logger.error("Error fetching all payment list: {}", e.getMessage());
			throw new ServiceException(e.getMessage());
		}

		return list;
	}

}
