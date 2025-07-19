package com.ezee.insurence.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.stereotype.Service;

import com.ezee.insurence.cache.dto.EmployeeCacheDTO;
import com.ezee.insurence.dao.EmployeeDAO;
import com.ezee.insurence.dto.EmployeeDTO;
import com.ezee.insurence.exception.ErrorCode;
import com.ezee.insurence.exception.ServiceException;

@Service
public class EmployeeCacheService {
	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private EmployeeDAO employeeDAO;

	private static final String CACHE_NAME = "EmployeeCache";

	public EmployeeDTO getEmployeeCache(EmployeeDTO employeeDTO) {
		if (employeeDTO == null) {
			throw new ServiceException(ErrorCode.USER_NOT_FOUND);
		}
		EmployeeDTO response = null;
		Cache cache = cacheManager.getCache(CACHE_NAME);
		String cacheKey = "EMP_" + employeeDTO.getId();
		if (cache != null) {
			ValueWrapper cacheValue = cache.get(cacheKey);
			if (cacheValue != null) {
				EmployeeCacheDTO employeeCacheDTO = (EmployeeCacheDTO) cacheValue.get();
				response = copyEmployeeFromCache(employeeCacheDTO, employeeDTO);
			}

		}
		if (response == null) {
			response = employeeDAO.getEmployeeByCode(employeeDTO);
			if (response.getId() != 0 || response.getCode() != null) {
				EmployeeCacheDTO employeeCacheDTO = copyEmployeeToCache(response);
				cacheManager.getCache(CACHE_NAME).put(cacheKey, employeeCacheDTO);
			}
		}
		return response;
	}

	public void putEmployeeCache(EmployeeDTO employeeDTO) {
		Cache cache = cacheManager.getCache(CACHE_NAME);
		if (cache != null) {
			String cacheKey = "EMP_" + employeeDTO.getId();
			EmployeeCacheDTO employeeCacheDTO = copyEmployeeToCache(employeeDTO);
			cache.put(cacheKey, employeeCacheDTO);
		}

	}

	public EmployeeCacheDTO copyEmployeeToCache(EmployeeDTO employeeDTO) {
		EmployeeCacheDTO cacheDTO = new EmployeeCacheDTO();
		cacheDTO.setId(employeeDTO.getId());
		cacheDTO.setCode(employeeDTO.getCode());
		cacheDTO.setName(employeeDTO.getName());
		cacheDTO.setUsername(employeeDTO.getUsername());
		cacheDTO.setEmployeeEmail(employeeDTO.getEmployeeEmail());
		cacheDTO.setEmployeeMobile(employeeDTO.getEmployeeMobile());
		cacheDTO.setPassword(employeeDTO.getPassword());
		cacheDTO.setEmployeeAddress(employeeDTO.getEmployeeAddress());
		cacheDTO.setEmployeeRole(employeeDTO.getEmployeeRole());
		cacheDTO.setActiveFlag(employeeDTO.getActiveFlag());
		return cacheDTO;
	}

	public EmployeeDTO copyEmployeeFromCache(EmployeeCacheDTO cacheDTO, EmployeeDTO employeeDTO) {
		employeeDTO.setId(cacheDTO.getId());
		employeeDTO.setCode(cacheDTO.getCode());
		employeeDTO.setName(cacheDTO.getName());
		employeeDTO.setUsername(cacheDTO.getUsername());
		employeeDTO.setEmployeeEmail(cacheDTO.getEmployeeEmail());
		employeeDTO.setEmployeeMobile(cacheDTO.getEmployeeMobile());
		employeeDTO.setPassword(cacheDTO.getPassword());
		employeeDTO.setEmployeeAddress(cacheDTO.getEmployeeAddress());
		employeeDTO.setEmployeeRole(cacheDTO.getEmployeeRole());
		employeeDTO.setActiveFlag(cacheDTO.getActiveFlag());
		return employeeDTO;
	}

}
