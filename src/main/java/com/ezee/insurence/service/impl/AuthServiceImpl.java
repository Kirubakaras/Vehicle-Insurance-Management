package com.ezee.insurence.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezee.insurence.cache.AuthCacheServcie;
import com.ezee.insurence.dao.EmployeeDAO;
import com.ezee.insurence.dao.LogAuditDAO;
import com.ezee.insurence.dto.AuthDTO;
import com.ezee.insurence.dto.EmployeeDTO;
import com.ezee.insurence.exception.ErrorCode;
import com.ezee.insurence.exception.ServiceException;
import com.ezee.insurence.service.AuthService;
import com.ezee.insurence.util.TokenGenerator;

@Service
public class AuthServiceImpl implements AuthService {
	@Autowired
	private AuthCacheServcie authCacheService;

	@Autowired
	private EmployeeDAO employeeDAO;

	@Autowired
	private LogAuditDAO logAuditDAO;

	private static final Logger logger = LogManager.getLogger("com.ezee.insurence.service.impl");

	@Override
	public AuthDTO getAuthendication(String username, String password) {
		AuthDTO authDTO = new AuthDTO();
		try {
			AuthDTO response = authCacheService.getAuthDTOByUsername(username);
			if (response != null) {
				authDTO = response;
			} else {
				EmployeeDTO employeeDTO = employeeDAO.getAuthendtication(username, password);
				if (employeeDTO.getId() == 0) {
					throw new ServiceException(ErrorCode.USER_NOT_FOUND);
				} else {
					String token = TokenGenerator.authTokenGenerator();
					authDTO.setAuthToken(token);
					authDTO.setEmlployeeDTO(employeeDTO);
					authCacheService.putAuthDTO(authDTO);
					logAuditDAO.loginEntry(authDTO);
				}
			}
			logger.info("Login successful for user {}", username);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			logger.error("Check the login username and password ");
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
		return authDTO;
	}

	public AuthDTO verification(String authtoken) {
		AuthDTO authDTO = new AuthDTO();
		authDTO = authCacheService.getAuthDTO(authtoken);
		return authDTO;
	}

	public int logout(AuthDTO authDTO) {
		int update = 0;
		try {
			update = logAuditDAO.logoutUpdate(authDTO);
			authCacheService.clearAuthDTO(authDTO);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
		return update;
	}

}
