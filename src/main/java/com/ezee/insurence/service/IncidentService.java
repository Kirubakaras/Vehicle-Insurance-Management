package com.ezee.insurence.service;

import java.util.List;

import com.ezee.insurence.dto.AuthDTO;
import com.ezee.insurence.dto.IncidentDTO;

public interface IncidentService {
	public void addIncident(AuthDTO authDTO, IncidentDTO incidentDTO);

	public void updateIncident(AuthDTO authDTO, IncidentDTO incidentDTO);

	public IncidentDTO getIncidentByCode(String code);

	public List<IncidentDTO> getIAllncident();

}
