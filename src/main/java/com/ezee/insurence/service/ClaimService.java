package com.ezee.insurence.service;

import java.util.List;

import com.ezee.insurence.dto.AuthDTO;
import com.ezee.insurence.dto.ClaimDTO;

public interface ClaimService {

	public void addClaim(AuthDTO authDTO, ClaimDTO claimDTO);

	public void updateClaim(AuthDTO authDTO, ClaimDTO claimDTO);

	public ClaimDTO getClaimByCode(String code);

	public List<ClaimDTO> getAllClaim();
	
	public List<ClaimDTO> getActiveClaim(ClaimDTO claimDTO);

}
