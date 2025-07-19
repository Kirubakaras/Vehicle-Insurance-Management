package com.ezee.insurence.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.stereotype.Service;

import com.ezee.insurence.cache.dto.CustomerCacheDTO;
import com.ezee.insurence.dao.CustomerDAO;
import com.ezee.insurence.dto.CustomerDTO;
import com.ezee.insurence.exception.ErrorCode;
import com.ezee.insurence.exception.ServiceException;

@Service
public class CustomerCacheService {

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private CustomerDAO customerDAO;

	private static final String CACHE_NAME = "CustomerCache";

	public CustomerDTO getCustomerCache(CustomerDTO customerDTO) {
		if (customerDTO == null) {
			throw new ServiceException(ErrorCode.CUSTOMER_NOT_FOUND);
		}
		CustomerDTO response = null;
		Cache cache = cacheManager.getCache(CACHE_NAME);

		if (cache != null && customerDTO.getId() != 0) {
			String cacheKey = "CUS_" + customerDTO.getId();
			ValueWrapper cacheValue = cache.get(cacheKey);
			if (cacheValue != null) {
				CustomerCacheDTO customerCacheDTO = (CustomerCacheDTO) cacheValue.get();
				response = copyCacheToDTO(customerDTO, customerCacheDTO);
			}

		}
		if (response == null) {
			response = customerDAO.getCustomerByCode(customerDTO);
			if (response.getId() != 0 || response.getCode() != null) {
				String cacheKey = "CUS_" + response.getId();
				CustomerCacheDTO customerCacheDTO = copyDTOToCache(customerDTO);
				cacheManager.getCache(CACHE_NAME).put(cacheKey, customerCacheDTO);
			}
		}
		return response;
	}

	public void putEmployeeCache(CustomerDTO customerDTO) {
		Cache cache = cacheManager.getCache(CACHE_NAME);
		if (cache != null) {
			String cacheKey = "CUS_" + customerDTO.getId();
			CustomerCacheDTO customerCacheDTO = copyDTOToCache(customerDTO);
			cache.put(cacheKey, customerCacheDTO);
		}

	}

	public CustomerCacheDTO copyDTOToCache(CustomerDTO customerDTO) {
		CustomerCacheDTO cacheDTO = new CustomerCacheDTO();
		cacheDTO.setId(customerDTO.getId());
		cacheDTO.setCode(customerDTO.getCode());
		cacheDTO.setName(customerDTO.getName());
		cacheDTO.setCustomerDOB(customerDTO.getCustomerDOB());
		cacheDTO.setCustomerGender(customerDTO.getCustomerGender());
		cacheDTO.setCustomerAddress(customerDTO.getCustomerAddress());
		cacheDTO.setCustomerNumber(customerDTO.getCustomerNumber());
		cacheDTO.setCustomerEmail(customerDTO.getCustomerEmail());
		cacheDTO.setCustomerLicenseNum(customerDTO.getCustomerLicenseNum());
		cacheDTO.setActiveFlag(customerDTO.getActiveFlag());
		return cacheDTO;
	}

	public CustomerDTO copyCacheToDTO(CustomerDTO customerDTO, CustomerCacheDTO cacheDTO) {
		customerDTO.setId(cacheDTO.getId());
		customerDTO.setCode(cacheDTO.getCode());
		customerDTO.setName(cacheDTO.getName());
		customerDTO.setCustomerDOB(cacheDTO.getCustomerDOB());
		customerDTO.setCustomerGender(cacheDTO.getCustomerGender());
		customerDTO.setCustomerAddress(cacheDTO.getCustomerAddress());
		customerDTO.setCustomerNumber(cacheDTO.getCustomerNumber());
		customerDTO.setCustomerEmail(cacheDTO.getCustomerEmail());
		customerDTO.setCustomerLicenseNum(cacheDTO.getCustomerLicenseNum());
		customerDTO.setActiveFlag(cacheDTO.getActiveFlag());
		return customerDTO;
	}

}
