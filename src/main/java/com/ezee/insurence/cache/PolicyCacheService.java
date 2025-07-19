package com.ezee.insurence.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.ezee.insurence.cache.dto.PolicyCacheDTO;

import com.ezee.insurence.dao.PolicyDAO;
import com.ezee.insurence.dto.PolicyDTO;
import com.ezee.insurence.exception.ErrorCode;
import com.ezee.insurence.exception.ServiceException;

@Service
public class PolicyCacheService {
	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private PolicyDAO policyDAO;

	private static final String CACHE_NAME = "PolicyCache";

	public PolicyDTO getPolicyCache(PolicyDTO policyDTO) {

		if (policyDTO == null) {
			throw new ServiceException(ErrorCode.POLICY_NOT_FOUND);
		}

		PolicyDTO response = null;
		Cache cache = cacheManager.getCache(CACHE_NAME);
		if (cache != null && policyDTO.getId() != 0) {
			String cacheKey = "PLY_" + policyDTO.getId();
			ValueWrapper cacheValue = cache.get(cacheKey);
			if (cacheValue != null) {
				PolicyCacheDTO policyCacheDTO = (PolicyCacheDTO) cacheValue.get();
				response = copyCacheToDTO(policyDTO, policyCacheDTO);
			}
		}

		if (response == null) {
			response = policyDAO.getPolicyByCode(policyDTO);
			if (response != null && response.getId() != 0) {
				String cacheKey = "PLY_" + response.getId();
				PolicyCacheDTO cacheDTO = copyDTOToCache(response);
				cache.put(cacheKey, cacheDTO);
			}
		}

		return response;
	}

	public void putPolicyCache(PolicyDTO policyDTO) {
		Cache cache = cacheManager.getCache(CACHE_NAME);
		if (cache != null) {
			String cacheKey = "PLY_" + policyDTO.getId();
			PolicyCacheDTO cacheDTO = copyDTOToCache(policyDTO);
			cache.put(cacheKey, cacheDTO);
		}
	}

	public PolicyCacheDTO copyDTOToCache(PolicyDTO policyDTO) {
		PolicyCacheDTO cacheDTO = new PolicyCacheDTO();
		cacheDTO.setId(policyDTO.getId());
		cacheDTO.setCode(policyDTO.getCode());
		cacheDTO.setCustomerDTO(policyDTO.getCustomerDTO());
		cacheDTO.setVehicleDTO(policyDTO.getVehicleDTO());
		cacheDTO.setPolicyNumber(policyDTO.getPolicyNumber());
		cacheDTO.setStartDate(policyDTO.getStartDate());
		cacheDTO.setExpriyDate(policyDTO.getExpriyDate());
		cacheDTO.setPerimunAmount(policyDTO.getPremiumAmount());
		cacheDTO.setPaymentSchedule(policyDTO.getPaymentSchedule());
		cacheDTO.setTotalAmount(policyDTO.getTotalAmount());
		cacheDTO.setPolicyStatus(policyDTO.getPolicyStatus());
		cacheDTO.setPolicyDescription(policyDTO.getPolicyDescription());
		cacheDTO.setActiveFlag(policyDTO.getActiveFlag());
		return cacheDTO;
	}

	public PolicyDTO copyCacheToDTO(PolicyDTO policyDTO, PolicyCacheDTO cacheDTO) {
		policyDTO.setId(cacheDTO.getId());
		policyDTO.setCode(cacheDTO.getCode());
		policyDTO.setCustomerDTO(cacheDTO.getCustomerDTO());
		policyDTO.setVehicleDTO(cacheDTO.getVehicleDTO());
		policyDTO.setPolicyNumber(cacheDTO.getPolicyNumber());
		policyDTO.setStartDate(cacheDTO.getStartDate());
		policyDTO.setExpriyDate(cacheDTO.getExpriyDate());
		policyDTO.setPremiumAmount(cacheDTO.getPerimunAmount());
		policyDTO.setPaymentSchedule(cacheDTO.getPaymentSchedule());
		policyDTO.setTotalAmount(cacheDTO.getTotalAmount());
		policyDTO.setPolicyStatus(cacheDTO.getPolicyStatus());
		policyDTO.setPolicyDescription(cacheDTO.getPolicyDescription());
		policyDTO.setActiveFlag(cacheDTO.getActiveFlag());
		return policyDTO;
	}
}
