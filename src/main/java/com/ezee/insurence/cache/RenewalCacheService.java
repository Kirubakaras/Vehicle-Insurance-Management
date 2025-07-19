package com.ezee.insurence.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.ezee.insurence.dao.RenewalDAO;
import com.ezee.insurence.dto.RenewalDTO;
import com.ezee.insurence.cache.dto.RenewalCacheDTO;
import com.ezee.insurence.exception.ErrorCode;
import com.ezee.insurence.exception.ServiceException;

@Service
public class RenewalCacheService {

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private RenewalDAO renewalDAO;

	private static final String CACHE_NAME = "RenewalCache";

	public RenewalDTO getRenewalCache(RenewalDTO renewalDTO) {
		if (renewalDTO == null) {
			throw new ServiceException(ErrorCode.RENEWAL_NOT_FOUND);
		}

		RenewalDTO response = null;

		Cache cache = cacheManager.getCache(CACHE_NAME);

		if (cache != null && renewalDTO.getId() != 0) {
			String cacheKey = "RNW_" + renewalDTO.getId();
			ValueWrapper cacheValue = cache.get(cacheKey);
			if (cacheValue != null) {
				RenewalCacheDTO renewalCacheDTO = (RenewalCacheDTO) cacheValue.get();
				response = copyCacheToDTO(renewalDTO, renewalCacheDTO);
			}
		}

		if (response == null) {

			response = renewalDAO.getRenewalByCode(renewalDTO);
			if (response != null && response.getId() != 0) {
				String cacheKey = "RNW_" + response.getId();
				RenewalCacheDTO cacheDTO = copyDTOToCache(response);
				cache.put(cacheKey, cacheDTO);
			}
		}

		return response;
	}

	public void putRenewalCache(RenewalDTO renewalDTO) {
		Cache cache = cacheManager.getCache(CACHE_NAME);
		if (cache != null) {
			String cacheKey = "RNW_" + renewalDTO.getId();
			RenewalCacheDTO cacheDTO = copyDTOToCache(renewalDTO);
			cache.put(cacheKey, cacheDTO);
		}
	}

	private RenewalCacheDTO copyDTOToCache(RenewalDTO renewalDTO) {
		RenewalCacheDTO cacheDTO = new RenewalCacheDTO();
		cacheDTO.setId(renewalDTO.getId());
		cacheDTO.setCode(renewalDTO.getCode());
		cacheDTO.setPolicyDTO(renewalDTO.getPolicyDTO());
		cacheDTO.setRenewalDate(renewalDTO.getRenewalDate());
		cacheDTO.setNewExpriyDate(renewalDTO.getNewExpriyDate());
		cacheDTO.setRenewalAmount(renewalDTO.getRenewalAmount());
		cacheDTO.setRenewalStatus(renewalDTO.getRenewalStatus());
		cacheDTO.setActiveFlag(renewalDTO.getActiveFlag());
		return cacheDTO;
	}

	private RenewalDTO copyCacheToDTO(RenewalDTO renewalDTO, RenewalCacheDTO cacheDTO) {
		renewalDTO.setId(cacheDTO.getId());
		renewalDTO.setCode(cacheDTO.getCode());
		renewalDTO.setPolicyDTO(cacheDTO.getPolicyDTO());
		renewalDTO.setRenewalDate(cacheDTO.getRenewalDate());
		renewalDTO.setNewExpriyDate(cacheDTO.getNewExpriyDate());
		renewalDTO.setRenewalAmount(cacheDTO.getRenewalAmount());
		renewalDTO.setRenewalStatus(cacheDTO.getRenewalStatus());
		renewalDTO.setActiveFlag(cacheDTO.getActiveFlag());
		return renewalDTO;
	}

}
