package com.ezee.insurence.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.ezee.insurence.cache.dto.ClaimCacheDTO;
import com.ezee.insurence.dao.ClaimDAO;
import com.ezee.insurence.dto.ClaimDTO;
import com.ezee.insurence.exception.ErrorCode;
import com.ezee.insurence.exception.ServiceException;

@Service
public class ClaimCacheService {

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private ClaimDAO claimDAO;

	private static final String CACHE_NAME = "ClaimCache";

	public ClaimDTO getClaimCache(ClaimDTO claimDTO) {
		if (claimDTO == null) {
			throw new ServiceException(ErrorCode.CLAIM_NOT_FOUND);
		}

		ClaimDTO response = null;
		String cacheKey = "CLAIM_" + claimDTO.getId();
		Cache cache = cacheManager.getCache(CACHE_NAME);

		if (cache != null) {
			ValueWrapper cacheValue = cache.get(cacheKey);
			if (cacheValue != null) {
				ClaimCacheDTO cacheDTO = (ClaimCacheDTO) cacheValue.get();
				response = copyCacheToDTO(claimDTO, cacheDTO);
			}
		}

		if (response == null) {
			response = claimDAO.getClaimByCode(claimDTO);
			if (response != null && response.getId() != 0) {
				ClaimCacheDTO cacheDTO = copyDTOToCache(response);
				cache.put(cacheKey, cacheDTO);
			}
		}

		return response;
	}

	public void putClaimCache(ClaimDTO claimDTO) {
		Cache cache = cacheManager.getCache(CACHE_NAME);
		if (cache != null) {
			String cacheKey = "CLAIM_" + claimDTO.getId();
			ClaimCacheDTO cacheDTO = copyDTOToCache(claimDTO);
			cache.put(cacheKey, cacheDTO);
		}
	}

	private ClaimCacheDTO copyDTOToCache(ClaimDTO claimDTO) {
		ClaimCacheDTO cacheDTO = new ClaimCacheDTO();
		cacheDTO.setId(claimDTO.getId());
		cacheDTO.setCode(claimDTO.getCode());
		cacheDTO.setPolicyDTO(claimDTO.getPolicyDTO());
		cacheDTO.setCustomerDTO(claimDTO.getCustomerDTO());
		cacheDTO.setIncidentDTO(claimDTO.getIncidentDTO());
		cacheDTO.setClaimType(claimDTO.getClaimType());
		cacheDTO.setClaimDate(claimDTO.getClaimDate());
		cacheDTO.setClaimDescription(claimDTO.getClaimDescription());
		cacheDTO.setClaimAmount(claimDTO.getClaimAmount());
		cacheDTO.setClaimStatus(claimDTO.getClaimStatus());
		cacheDTO.setActiveflag(String.valueOf(claimDTO.getActiveFlag()));
		return cacheDTO;
	}

	private ClaimDTO copyCacheToDTO(ClaimDTO claimDTO, ClaimCacheDTO cacheDTO) {
		claimDTO.setId(cacheDTO.getId());
		claimDTO.setCode(cacheDTO.getCode());
		claimDTO.setPolicyDTO(cacheDTO.getPolicyDTO());
		claimDTO.setCustomerDTO(cacheDTO.getCustomerDTO());
		claimDTO.setIncidentDTO(cacheDTO.getIncidentDTO());
		claimDTO.setClaimType(cacheDTO.getClaimType());
		claimDTO.setClaimDate(cacheDTO.getClaimDate());
		claimDTO.setClaimDescription(cacheDTO.getClaimDescription());
		claimDTO.setClaimAmount(cacheDTO.getClaimAmount());
		claimDTO.setClaimStatus(cacheDTO.getClaimStatus());
		claimDTO.setActiveFlag(Integer.parseInt(cacheDTO.getActiveflag()));
		return claimDTO;
	}

}
