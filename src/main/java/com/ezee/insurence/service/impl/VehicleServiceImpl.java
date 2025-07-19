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
import com.ezee.insurence.dao.IncidentDAO;
import com.ezee.insurence.dao.VehicleDAO;
import com.ezee.insurence.dao.VehicleServiceDAO;
import com.ezee.insurence.dto.AuthDTO;
import com.ezee.insurence.dto.ClaimDTO;
import com.ezee.insurence.dto.CustomerDTO;
import com.ezee.insurence.dto.IncidentDTO;
import com.ezee.insurence.dto.PolicyDTO;
import com.ezee.insurence.dto.VehicleDTO;
import com.ezee.insurence.dto.VehicleServiceDTO;
import com.ezee.insurence.exception.ErrorCode;
import com.ezee.insurence.exception.ServiceException;
import com.ezee.insurence.service.VehicleServcie;
import com.ezee.insurence.util.TokenGenerator;

@Service
public class VehicleServiceImpl implements VehicleServcie {

	@Autowired
	private VehicleDAO vehicleDAO;

	@Autowired
	private CustomerCacheService customerCache;

	@Autowired
	private PolicyCacheService policyCache;

	@Autowired
	private VehicleServiceDAO vehicleServiceDAO;

	@Autowired
	private ClaimCacheService claimCache;

	@Autowired
	private IncidentDAO incidentDAO;

	private static final Logger logger = LogManager.getLogger("com.ezee.insurence.service.impl");

	@Override
	public void addVehicle(AuthDTO authDTO, VehicleDTO vehicleDTO) {
		String code = TokenGenerator.generateCode("VEH", 12);
		vehicleDTO.setCode(code);
		try {
			int affected = vehicleDAO.vehicleUID(authDTO, vehicleDTO);
			if (affected == 0) {
				throw new ServiceException(ErrorCode.VEHICLE_NOT_FOUND);
			}
			logger.info("Insert the Vehicle record ");
		} catch (ServiceException e) {
			logger.error("ServiceException : Error while inserting the  Vehicle record {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Error while inserting the  Vehicle record {} ", e.getMessage());
			throw e;
		}

	}

	@Override
	public void updateVehicle(AuthDTO authDTO, VehicleDTO vehicleDTO) {
		try {
			int affected = vehicleDAO.vehicleUID(authDTO, vehicleDTO);
			if (affected == 0) {
				throw new ServiceException(ErrorCode.VEHICLE_NOT_FOUND);
			}
			logger.info("Update the Vehicle record ");
		} catch (ServiceException e) {
			logger.error("ServiceException : Error while Updateing the  Vehicle record {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Error while Updateing the Vehicle record {} ", e.getMessage());
			throw e;
		}

	}

	@Override
	public VehicleDTO getVehicleDTO(String code) {
		VehicleDTO vehicleDTO = new VehicleDTO();
		try {
			vehicleDTO.setCode(code);
			vehicleDTO = vehicleDAO.getVehicleByCode(vehicleDTO);
			if (vehicleDTO.getId() == 0) {
				throw new ServiceException(ErrorCode.VEHICLE_NOT_FOUND);
			}
			CustomerDTO customerDTO = customerCache.getCustomerCache(vehicleDTO.getCustomerDTO());
			vehicleDTO.setCustomerDTO(customerDTO);

			logger.info("Fetch specific the Vehicle record {} ", vehicleDTO.getCode());
		} catch (ServiceException e) {
			logger.error("ServiceException : Error while Fetch specific the Vehicle record {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Error while Fetch specific the Vehicle record {} ", e.getMessage());
			throw e;
		}
		return vehicleDTO;
	}

	@Override
	public List<VehicleDTO> getAllvehicle() {
		List<VehicleDTO> vehicleList = new ArrayList<>();
		try {

			vehicleList = vehicleDAO.getAllVehicle();
			if (vehicleList != null) {
				for (VehicleDTO vehicleDTO : vehicleList) {
					CustomerDTO customerDTO = customerCache.getCustomerCache(vehicleDTO.getCustomerDTO());
					vehicleDTO.setCustomerDTO(customerDTO);
				}
			} else {
				throw new ServiceException(ErrorCode.VEHICLE_NOT_FOUND);
			}
			logger.info("Fetch all the Vehicle record ");
		} catch (ServiceException e) {
			logger.error("ServiceException : Error while Fetch all the Vehicle record {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Error while Fetch all the Vehicle record {} ", e.getMessage());
			throw e;
		}
		return vehicleList;
	}

	@Override
	public VehicleDTO getPolicyByVehicle(VehicleDTO vehicleDTO) {
		try {
			vehicleDTO = vehicleDAO.getPolicyByVehicle(vehicleDTO);
			if (vehicleDTO.getId() == 0) {
				throw new ServiceException(ErrorCode.VEHICLE_NOT_FOUND);
			}
			CustomerDTO customerDTO = customerCache.getCustomerCache(vehicleDTO.getCustomerDTO());
			vehicleDTO.setCustomerDTO(customerDTO);
			List<PolicyDTO> policyList = new ArrayList<>();
			for (PolicyDTO policyDTO : vehicleDTO.getPolicyDTO()) {
				policyDTO = policyCache.getPolicyCache(policyDTO);
				policyList.add(policyDTO);
			}
			vehicleDTO.setPolicyDTO(policyList);
			logger.info("Fetch policy detail by Vehicle ");
		} catch (ServiceException e) {
			logger.error("ServiceException : Error while Fetch policy detail the Vehicle record {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Error while Fetch policy detail the Vehicle record {} ", e.getMessage());
			throw e;
		}
		return vehicleDTO;
	}

	@Override
	public VehicleDTO getVehicleServiceByVehicle(VehicleDTO vehicleDTO) {
		try {
			vehicleDTO = vehicleDAO.getVehicleServiceByVehicle(vehicleDTO);

			if (vehicleDTO.getId() == 0) {
				throw new ServiceException(ErrorCode.VEHICLE_NOT_FOUND);
			}
			CustomerDTO customerDTO = customerCache.getCustomerCache(vehicleDTO.getCustomerDTO());
			vehicleDTO.setCustomerDTO(customerDTO);

			List<VehicleServiceDTO> serviceList = new ArrayList<>();
			for (VehicleServiceDTO serviceDTO : vehicleDTO.getVehicleServcieDTO()) {
				VehicleServiceDTO dto = vehicleServiceDAO.getVehicleServiceDTO(serviceDTO);

				ClaimDTO claimDTO = claimCache.getClaimCache(dto.getClaimDTO());

				PolicyDTO policyDTO = policyCache.getPolicyCache(claimDTO.getPolicyDTO());
				claimDTO.setPolicyDTO(policyDTO);

				IncidentDTO incidentDTO = incidentDAO.getIncidentByCode(claimDTO.getIncidentDTO());
				claimDTO.setIncidentDTO(incidentDTO);

				dto.setClaimDTO(claimDTO);
				serviceList.add(dto);
			}
			vehicleDTO.setVehicleServcieDTO(serviceList);

			logger.info("Fetch vehicle service details by vehicle code: {}", vehicleDTO.getCode());

		} catch (ServiceException e) {
			logger.error("ServiceException while fetching vehicle service by code : {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception while fetching vehicle service by code: {}", e.getMessage());
			throw e;
		}
		return vehicleDTO;
	}

}
