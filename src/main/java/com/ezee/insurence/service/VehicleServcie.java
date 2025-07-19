package com.ezee.insurence.service;

import java.util.List;

import com.ezee.insurence.dto.AuthDTO;
import com.ezee.insurence.dto.VehicleDTO;

public interface VehicleServcie {
	public void addVehicle(AuthDTO authDTO, VehicleDTO vehicleDTO);

	public void updateVehicle(AuthDTO authDTO, VehicleDTO vechicleDTO);

	public VehicleDTO getVehicleDTO(String code);

	public List<VehicleDTO> getAllvehicle();

	public VehicleDTO getPolicyByVehicle(VehicleDTO vehicleDTO);

	public VehicleDTO getVehicleServiceByVehicle(VehicleDTO vehicleDTO);
}
