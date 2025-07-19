package com.ezee.insurence.cache;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.ezee.insurence.cache.dto.AuthCacheDTO;
import com.ezee.insurence.dto.AuthDTO;
import com.ezee.insurence.exception.ErrorCode;
import com.ezee.insurence.exception.ServiceException;

import org.springframework.cache.Cache.ValueWrapper;

@Service
public class AuthCacheServcie {

	@Autowired
	private CacheManager cacheManager;

	private static final Logger logger = LogManager.getLogger("com.ezee.insurence.cache");

	private static final String CACHE_NAME = "AuthCache";
	private static final String CACHE_USERNAME = "UsernameCache";

	public AuthDTO getAuthDTO(String authToken) {
		AuthDTO authDTO = null;
		ValueWrapper cache = cacheManager.getCache(CACHE_NAME).get(authToken);
		if (cache != null && cache.get() != null) {
			AuthCacheDTO authCacheDTO = (AuthCacheDTO) cache.get();
			authDTO = bindCacheObjectfromAuth(authCacheDTO);
			logger.info("Validate the record from the cache");

		} else {
			logger.error("Invalid validation access ");
			throw new ServiceException(ErrorCode.INVALID_CREDENTIALS);
		}
		return authDTO;
	}

	public AuthDTO getAuthDTOByUsername(String username) {
		AuthDTO authDTO = null;
		ValueWrapper cache = cacheManager.getCache(CACHE_USERNAME).get(username);
		if (cache != null && cache.get() != null) {
			AuthCacheDTO authCacheDTO = (AuthCacheDTO) cache.get();
			authDTO = bindCacheObjectfromAuth(authCacheDTO);
			logger.info("Validate the record from the cache");
		}
		return authDTO;
	}

	public void putAuthDTO(AuthDTO authDTO) {
		String cacheKey = authDTO.getAuthToken();
		String cacheName = authDTO.getEmlployeeDTO().getUsername();
		AuthCacheDTO authCacheDTO = bindAuthToCahcheObject(authDTO);
		cacheManager.getCache(CACHE_NAME).put(cacheKey, authCacheDTO);
		cacheManager.getCache(CACHE_USERNAME).put(cacheName, authCacheDTO);
		logger.info("Put the auth record to the cache");
	}

	public void clearAuthDTO(String authToken) {
		cacheManager.getCache(CACHE_NAME).evict(authToken);
	}

	public void clearAuthDTO(AuthDTO authDTO) {
		String cacheKey = authDTO.getAuthToken();
		String cacheName = authDTO.getEmlployeeDTO().getUsername();
		cacheManager.getCache(CACHE_NAME).evict(cacheKey);
		cacheManager.getCache(CACHE_USERNAME).evict(cacheName);
	}

	protected AuthCacheDTO bindAuthToCahcheObject(AuthDTO authDTO) {
		AuthCacheDTO cacheDTO = new AuthCacheDTO();
		cacheDTO.setAuthToken(authDTO.getAuthToken());
		cacheDTO.setEmployeeDTO(authDTO.getEmlployeeDTO());
		return cacheDTO;
	}

	protected AuthDTO bindCacheObjectfromAuth(AuthCacheDTO cacheDTO) {
		AuthDTO authDTO = new AuthDTO();
		authDTO.setAuthToken(cacheDTO.getAuthToken());
		authDTO.setEmlployeeDTO(cacheDTO.getEmployeeDTO());
		return authDTO;
	}
}
