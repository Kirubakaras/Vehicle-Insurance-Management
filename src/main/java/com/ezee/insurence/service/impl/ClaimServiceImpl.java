package com.ezee.insurence.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezee.insurence.cache.CustomerCacheService;
import com.ezee.insurence.cache.PolicyCacheService;
import com.ezee.insurence.cache.VehicleCacheService;
import com.ezee.insurence.dao.ClaimDAO;
import com.ezee.insurence.dao.IncidentDAO;
import com.ezee.insurence.dto.AuthDTO;
import com.ezee.insurence.dto.ClaimDTO;
import com.ezee.insurence.dto.IncidentDTO;
import com.ezee.insurence.dto.PolicyDTO;
import com.ezee.insurence.exception.ErrorCode;
import com.ezee.insurence.exception.ServiceException;
import com.ezee.insurence.service.ClaimService;
import com.ezee.insurence.util.TokenGenerator;

@Service
public class ClaimServiceImpl implements ClaimService {

	@Autowired
	private ClaimDAO claimDAO;

	@Autowired
	private CustomerCacheService customerCache;

	@Autowired
	private IncidentDAO incidentDAO;

	@Autowired
	private PolicyCacheService policyCache;

	@Autowired
	private VehicleCacheService vehicleCache;

	private static final Logger logger = LogManager.getLogger("com.ezee.insurence.service.impl");

	@Override
	public void addClaim(AuthDTO authDTO, ClaimDTO claimDTO) {

		try {
			String code = TokenGenerator.generateCode("CLM", 12);
			claimDTO.setCode(code);

			int affected = claimDAO.claimUID(authDTO, claimDTO);
			if (affected == 0) {
				throw new ServiceException(ErrorCode.ClAIM_NOT_CREATE);
			}
			logger.info("Inserted the claim record ");
		} catch (ServiceException e) {
			logger.error("ServiceException : Error while Insert the Claim record {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Error while Insert the Claim record {} ", e.getMessage());
			throw e;
		}
	}

	@Override
	public void updateClaim(AuthDTO authDTO, ClaimDTO claimDTO) {
		try {
			int affected = claimDAO.claimUID(authDTO, claimDTO);
			
			if (affected == 0) {
				throw new ServiceException(ErrorCode.ClAIM_NOT_CREATE);
			}
			logger.info("Inserted the claim record ");
		} catch (ServiceException e) {
			logger.error("ServiceException : Error while Update the Claim record {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Error while update the Claim record {} ", e.getMessage());
			throw e;
		}
	}

	@Override
	public ClaimDTO getClaimByCode(String code) {
		ClaimDTO claimDTO = new ClaimDTO();
		try {
			claimDTO.setCode(code);
			claimDTO = claimDAO.getClaimByCode(claimDTO);
			if (claimDTO != null) {
				PolicyDTO policyDTO = policyCache.getPolicyCache(claimDTO.getPolicyDTO());
				policyDTO.setVehicleDTO(vehicleCache.getVehicleCache(policyDTO.getVehicleDTO()));
				claimDTO.setPolicyDTO(policyDTO);
				claimDTO.setCustomerDTO(customerCache.getCustomerCache(claimDTO.getCustomerDTO()));
				IncidentDTO incidentDTO = incidentDAO.getIncidentByCode(claimDTO.getIncidentDTO());
				if (incidentDTO != null) {
					claimDTO.setIncidentDTO(incidentDTO);
				} else {
					throw new ServiceException(ErrorCode.INCIDENT_NOT_FOUND);
				}
			} else {
				throw new ServiceException(ErrorCode.CLAIM_NOT_FOUND);
			}

		} catch (ServiceException e) {
			logger.error("ServcieException: Error while fetch the claim record {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception: Error while fetch the claim record {} ", e.getMessage());
			throw e;

		}
		return claimDTO;
	}

	@Override
	public List<ClaimDTO> getAllClaim() {
		List<ClaimDTO> listDTO = new ArrayList<>();
		try {
			listDTO = claimDAO.getAllClaim();
			if (listDTO == null) {
				throw new ServiceException(ErrorCode.CLAIM_NOT_FOUND);
			}
			for (ClaimDTO claimDTO : listDTO) {
				PolicyDTO policyDTO = policyCache.getPolicyCache(claimDTO.getPolicyDTO());
				policyDTO.setVehicleDTO(vehicleCache.getVehicleCache(policyDTO.getVehicleDTO()));
				claimDTO.setPolicyDTO(policyDTO);
				claimDTO.setCustomerDTO(customerCache.getCustomerCache(claimDTO.getCustomerDTO()));
				IncidentDTO incidentDTO = incidentDAO.getIncidentByCode(claimDTO.getIncidentDTO());
				if (incidentDTO != null) {
					claimDTO.setIncidentDTO(incidentDTO);
				} else {
					throw new ServiceException(ErrorCode.INCIDENT_NOT_FOUND);
				}
			}
			logger.info("Fetch the all claim record ");
		} catch (ServiceException e) {
			logger.error("ServcieException: Error while fetch the claim record {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception: Error while fetch the claim record {} ", e.getMessage());
			throw e;

		}
		return listDTO;
	}

	@Override
	public List<ClaimDTO> getActiveClaim(ClaimDTO claimDTO) {
		List<ClaimDTO> listDTO = new ArrayList<>();
		try {
			listDTO = claimDAO.getActiveClaim(claimDTO);
			if (listDTO == null) {
				throw new ServiceException(ErrorCode.CLAIM_NOT_FOUND);
			}
			for (ClaimDTO cDTO : listDTO) {
				PolicyDTO policyDTO = policyCache.getPolicyCache(cDTO.getPolicyDTO());
				policyDTO.setVehicleDTO(vehicleCache.getVehicleCache(policyDTO.getVehicleDTO()));
				cDTO.setPolicyDTO(policyDTO);
				cDTO.setCustomerDTO(customerCache.getCustomerCache(cDTO.getCustomerDTO()));
				IncidentDTO incidentDTO = incidentDAO.getIncidentByCode(cDTO.getIncidentDTO());
				if (incidentDTO != null) {
					cDTO.setIncidentDTO(incidentDTO);
				} else {
					throw new ServiceException(ErrorCode.INCIDENT_NOT_FOUND);
				}
			}
		} catch (ServiceException e) {
			logger.error("ServcieException: Error while fetch the claim record {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception: Error while fetch the claim record {} ", e.getMessage());
			throw e;

		}
		return listDTO;
	}
}
