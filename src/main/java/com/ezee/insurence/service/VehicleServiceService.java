package com.ezee.insurence.service;

import java.util.List;

import com.ezee.insurence.dto.AuthDTO;
import com.ezee.insurence.dto.VehicleServiceDTO;

public interface VehicleServiceService {

	public void addVehicleService(AuthDTO authDTO, VehicleServiceDTO vehicleService);

	public void updateVehicleService(AuthDTO authDTO, VehicleServiceDTO vehicleService);

	public VehicleServiceDTO getVehicleServiceDTO(String code);

	public List<VehicleServiceDTO> getAllVehicleService();

}
