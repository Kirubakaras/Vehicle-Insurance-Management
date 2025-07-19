package com.ezee.insurence.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.ezee.insurence.cache.dto.VehicleCacheDTO;
import com.ezee.insurence.dao.VehicleDAO;
import com.ezee.insurence.dto.VehicleDTO;
import com.ezee.insurence.exception.ErrorCode;
import com.ezee.insurence.exception.ServiceException;

@Service
public class VehicleCacheService {

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private VehicleDAO vehicleDAO;

	private static final String CACHE_NAME = "VehicleCache";

	public VehicleDTO getVehicleCache(VehicleDTO vehicleDTO) {
		if (vehicleDTO == null) {
			throw new ServiceException(ErrorCode.VEHICLE_NOT_FOUND);
		}
		VehicleDTO response = null;
		Cache cache = cacheManager.getCache(CACHE_NAME);

		if (cache != null && vehicleDTO.getId() != 0) {
			String cacheKey = "VEH_" + vehicleDTO.getId();
			ValueWrapper cacheValue = cache.get(cacheKey);
			if (cacheValue != null) {
				VehicleCacheDTO vehicleCacheDTO = (VehicleCacheDTO) cacheValue.get();
				response = copyCacheToDTO(vehicleDTO, vehicleCacheDTO);
			}
		}

		if (response == null) {
			response = vehicleDAO.getVehicleByCode(vehicleDTO);
			if (response != null && response.getId() != 0) {
				String cacheKey = "VEH_" + response.getId();
				VehicleCacheDTO cacheDTO = copyDTOToCache(response);
				cacheManager.getCache(CACHE_NAME).put(cacheKey, cacheDTO);
			}
		}

		return response;
	}

	public void putVehicleCache(VehicleDTO vehicleDTO) {
		Cache cache = cacheManager.getCache(CACHE_NAME);
		if (cache != null) {
			String cacheKey = "VEH_" + vehicleDTO.getId();
			VehicleCacheDTO cacheDTO = copyDTOToCache(vehicleDTO);
			cache.put(cacheKey, cacheDTO);
		}
	}

	public VehicleCacheDTO copyDTOToCache(VehicleDTO vehicleDTO) {
		VehicleCacheDTO cacheDTO = new VehicleCacheDTO();
		cacheDTO.setId(vehicleDTO.getId());
		cacheDTO.setCode(vehicleDTO.getCode());
		cacheDTO.setCustomerDTO(vehicleDTO.getCustomerDTO());
		cacheDTO.setVehiclePlateNum(vehicleDTO.getVehiclePlateNum());
		cacheDTO.setVehicleType(vehicleDTO.getVehicleType());
		cacheDTO.setVehicleEnginNum(vehicleDTO.getVehicleEnginNum());
		cacheDTO.setVehicleChasisNum(vehicleDTO.getVehicleChasisNum());
		cacheDTO.setVehicleNumber(vehicleDTO.getVehicleNumber());
		cacheDTO.setVehicleModelNum(vehicleDTO.getVehicleModelNum());
		cacheDTO.setActiveFlag(vehicleDTO.getActiveFlag());
		return cacheDTO;
	}

	public VehicleDTO copyCacheToDTO(VehicleDTO vehicleDTO, VehicleCacheDTO cacheDTO) {
		vehicleDTO.setId(cacheDTO.getId());
		vehicleDTO.setCode(cacheDTO.getCode());
		vehicleDTO.setCustomerDTO(cacheDTO.getCustomerDTO());
		vehicleDTO.setVehiclePlateNum(cacheDTO.getVehiclePlateNum());
		vehicleDTO.setVehicleType(cacheDTO.getVehicleType());
		vehicleDTO.setVehicleEnginNum(cacheDTO.getVehicleEnginNum());
		vehicleDTO.setVehicleChasisNum(cacheDTO.getVehicleChasisNum());
		vehicleDTO.setVehicleNumber(cacheDTO.getVehicleNumber());
		vehicleDTO.setVehicleModelNum(cacheDTO.getVehicleModelNum());
		vehicleDTO.setActiveFlag(cacheDTO.getActiveFlag());
		return vehicleDTO;
	}

}
