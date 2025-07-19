package com.ezee.insurence.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezee.insurence.cache.CustomerCacheService;
import com.ezee.insurence.dao.IncidentDAO;
import com.ezee.insurence.dto.AuthDTO;
import com.ezee.insurence.dto.IncidentDTO;
import com.ezee.insurence.exception.ErrorCode;
import com.ezee.insurence.exception.ServiceException;
import com.ezee.insurence.service.IncidentService;
import com.ezee.insurence.util.TokenGenerator;

@Service
public class IncidentServiceImpl implements IncidentService {

	@Autowired
	private IncidentDAO incidentDAO;

	@Autowired
	private CustomerCacheService customerCache;

	private static final Logger logger = LogManager.getLogger("com.ezee.insurence.service.impl");

	@Override
	public void addIncident(AuthDTO authDTO, IncidentDTO incidentDTO) {
		try {
			String code = TokenGenerator.generateCode("INC", 12);
			incidentDTO.setCode(code);
			int affected = incidentDAO.incidentUID(authDTO, incidentDTO);
			if (affected == 0) {
				throw new ServiceException(ErrorCode.INCIDENT_NOT_CREATED);
			}
			logger.info("Inserted the incident record ");
		} catch (ServiceException e) {
			logger.error("ServiceException : Error while insert the incident record {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Error while insert the incident record {} ", e.getMessage());
			throw e;
		}

	}

	@Override
	public void updateIncident(AuthDTO authDTO, IncidentDTO incidentDTO) {
		try {
			int affected = incidentDAO.incidentUID(authDTO, incidentDTO);
			if (affected == 0) {
				throw new ServiceException(ErrorCode.INCIDENT_NOT_CREATED);
			}
			logger.info("Update the incident record ");
		} catch (ServiceException e) {
			logger.error("ServiceException : Error while insert the incident record {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Error while insert the incident record {} ", e.getMessage());
			throw e;
		}

	}

	@Override
	public IncidentDTO getIncidentByCode(String code) {
		IncidentDTO incidentDTO = new IncidentDTO();
		try {
			incidentDTO.setCode(code);
			incidentDTO = incidentDAO.getIncidentByCode(incidentDTO);
			if (incidentDTO != null) {
				incidentDTO.setCustomerDTO(customerCache.getCustomerCache(incidentDTO.getCustomerDTO()));
			} else {
				throw new ServiceException(ErrorCode.INCIDENT_NOT_FOUND);
			}
			logger.info("Fetch the incident record {} ", incidentDTO.getCode());
		} catch (ServiceException e) {
			logger.error("ServiceException : Error while insert the incident record {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Error while insert the incident record {} ", e.getMessage());
			throw e;
		}
		return incidentDTO;
	}

	@Override
	public List<IncidentDTO> getIAllncident() {
		List<IncidentDTO> listIncident = new ArrayList<>();
		try {
			listIncident = incidentDAO.getAllIncident();
			if (listIncident != null) {
				for (IncidentDTO incidentDTO : listIncident) {
					incidentDTO.setCustomerDTO(customerCache.getCustomerCache(incidentDTO.getCustomerDTO()));
				}
			} else {
				throw new ServiceException(ErrorCode.INCIDENT_NOT_FOUND);
			}
			logger.info("Fetch the all incident record ");
		} catch (ServiceException e) {
			logger.error("ServiceException : Error while insert the incident record {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Error while insert the incident record {} ", e.getMessage());
			throw e;
		}
		return listIncident;
	}

}
