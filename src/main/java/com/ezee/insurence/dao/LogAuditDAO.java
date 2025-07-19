package com.ezee.insurence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ezee.insurence.dto.AuthDTO;
import com.ezee.insurence.exception.ServiceException;

import lombok.Cleanup;

@Repository
public class LogAuditDAO {
	@Autowired
	private DataSource dataSource;

	private static final Logger logger = LogManager.getLogger("com.tn.electricity.dao");

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public boolean loginEntry(AuthDTO authDTO) {
		try {

			@Cleanup
			Connection connection = dataSource.getConnection();
			String query = "insert into log_audit (employee_id, login, update_at,active_flag) values (?, ?, now(),1) ";
			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, authDTO.getEmlployeeDTO().getId());
			statement.setString(2, LocalDateTime.now().toString());
			statement.execute();
			logger.info("login has been enterd");
		} catch (Exception e) {
			logger.info("error our during the exception {} ", e.getMessage());
			throw new ServiceException(e.getMessage());
		}
		return true;
	}

	public int logoutUpdate(AuthDTO authDTO) {
		int logout = 0;
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			String query = "update log_audit set logout = ? where employee_id = ?";
			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, LocalDateTime.now().format(formatter));
			statement.setInt(2, authDTO.getEmlployeeDTO().getId());
			logout = statement.executeUpdate();

			logger.info("Sucessfully logout");

		} catch (Exception e) {
			logger.info("error our during the exception {} ", e.getMessage());
			throw new ServiceException(e.getMessage());

		}
		return logout;
	}
}
