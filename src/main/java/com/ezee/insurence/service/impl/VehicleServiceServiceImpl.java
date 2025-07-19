package com.ezee.insurence.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezee.insurence.cache.ClaimCacheService;
import com.ezee.insurence.cache.CustomerCacheService;
import com.ezee.insurence.cache.PolicyCacheService;
import com.ezee.insurence.cache.VehicleCacheService;
import com.ezee.insurence.dao.IncidentDAO;
import com.ezee.insurence.dao.VehicleServiceDAO;
import com.ezee.insurence.dto.AuthDTO;
import com.ezee.insurence.dto.ClaimDTO;
import com.ezee.insurence.dto.IncidentDTO;
import com.ezee.insurence.dto.PolicyDTO;
import com.ezee.insurence.dto.VehicleServiceDTO;
import com.ezee.insurence.exception.ErrorCode;
import com.ezee.insurence.exception.ServiceException;
import com.ezee.insurence.service.VehicleServiceService;
import com.ezee.insurence.util.TokenGenerator;

@Service
public class VehicleServiceServiceImpl implements VehicleServiceService {

	@Autowired
	private VehicleServiceDAO vehicleServiceDAO;

	@Autowired
	private VehicleCacheService vehicleCache;

	@Autowired
	private ClaimCacheService claimCache;

	@Autowired
	private PolicyCacheService policyCache;

	@Autowired
	private IncidentDAO incidentDAO;

	@Autowired
	private CustomerCacheService customerCache;

	private static final Logger logger = LogManager.getLogger("com.ezee.insurence.service.impl");

	@Override
	public void addVehicleService(AuthDTO authDTO, VehicleServiceDTO vehicleServiceDTO) {
		try {
			String code = TokenGenerator.generateCode("VSER", 12);
			vehicleServiceDTO.setCode(code);
			int affetched = vehicleServiceDAO.vehicleServiceUID(authDTO, vehicleServiceDTO);
			if (affetched == 0) {
				throw new ServiceException(ErrorCode.SERVICE_NOT_CREATED);
			}
			logger.info("Inserted the vehicleService record ");
		} catch (ServiceException e) {
			logger.error("ServiceException : Error while Insert the vehicle Service {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Error while Insert the vehicle Service {} ", e.getMessage());
			throw e;
		}

	}

	@Override
	public void updateVehicleService(AuthDTO authDTO, VehicleServiceDTO vehicleServiceDTO) {
		try {
			int affetched = vehicleServiceDAO.vehicleServiceUID(authDTO, vehicleServiceDTO);
			if (affetched == 0) {
				throw new ServiceException(ErrorCode.SERVICE_NOT_FOUND);
			}
			logger.info("Update the vehicleService record ");
		} catch (ServiceException e) {
			logger.error("ServiceException : Error while Update the vehicle Service {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Error while Update the vehicle Service {} ", e.getMessage());
			throw e;
		}
	}

	@Override
	public List<VehicleServiceDTO> getAllVehicleService() {
		List<VehicleServiceDTO> listDTO = new ArrayList<>();
		try {

			listDTO = vehicleServiceDAO.getAllVehicleService();
			if (listDTO != null) {
				for (VehicleServiceDTO serviceDTO : listDTO) {
					serviceDTO.setVehicleDTO(vehicleCache.getVehicleCache(serviceDTO.getVehicleDTO()));
					ClaimDTO claimDTO = claimCache.getClaimCache(serviceDTO.getClaimDTO());
					PolicyDTO policyDTO = policyCache.getPolicyCache(claimDTO.getPolicyDTO());
					claimDTO.setPolicyDTO(policyDTO);
					claimDTO.setCustomerDTO(customerCache.getCustomerCache(claimDTO.getCustomerDTO()));
					IncidentDTO incidentDTO = incidentDAO.getIncidentByCode(claimDTO.getIncidentDTO());
					claimDTO.setIncidentDTO(incidentDTO);
				}
			} else {
				throw new ServiceException(ErrorCode.VEHICLE_NOT_FOUND);
			}
			logger.info("Fetch the all vehicleServcie ");
		} catch (ServiceException e) {
			logger.error("ServiceException : Error while Update the vehicle Service {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Error while Update the vehicle Service {} ", e.getMessage());
			throw e;
		}
		return listDTO;
	}

	@Override
	public VehicleServiceDTO getVehicleServiceDTO(String code) {
		VehicleServiceDTO serviceDTO = new VehicleServiceDTO();
		try {
			serviceDTO.setCode(code);
			serviceDTO = vehicleServiceDAO.getVehicleServiceDTO(serviceDTO);
			if (serviceDTO != null) {
				serviceDTO.setVehicleDTO(vehicleCache.getVehicleCache(serviceDTO.getVehicleDTO()));
				ClaimDTO claimDTO = claimCache.getClaimCache(serviceDTO.getClaimDTO());
				PolicyDTO policyDTO = policyCache.getPolicyCache(claimDTO.getPolicyDTO());
				claimDTO.setPolicyDTO(policyDTO);
				claimDTO.setCustomerDTO(customerCache.getCustomerCache(claimDTO.getCustomerDTO()));
				IncidentDTO incidentDTO = incidentDAO.getIncidentByCode(claimDTO.getIncidentDTO());
				claimDTO.setIncidentDTO(incidentDTO);
			} else {
				throw new ServiceException(ErrorCode.VEHICLE_NOT_FOUND);
			}
			logger.info("Fetch the vehicleServcie by code: {} ", serviceDTO.getCode());
		} catch (ServiceException e) {
			logger.error("ServiceException : Error while Update the vehicle Service {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Error while Update the vehicle Service {} ", e.getMessage());
			throw e;
		}
		return serviceDTO;

	}

}
