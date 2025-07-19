package com.ezee.insurence.service;

import java.util.List;

import com.ezee.insurence.dto.AuthDTO;
import com.ezee.insurence.dto.RenewalDTO;

public interface RenewalService {
	public void addRenewal(AuthDTO authDTO, RenewalDTO renewalDTO);

	public void updateRenewal(AuthDTO authDTO, RenewalDTO renewalDTO);

	public RenewalDTO getRenewalByCode(String code);

	public List<RenewalDTO> getAllRenewal();

	public RenewalDTO getReciptByRenewalCode(RenewalDTO renewalDTO);

}
