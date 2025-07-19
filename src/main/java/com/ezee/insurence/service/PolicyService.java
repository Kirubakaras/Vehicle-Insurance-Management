package com.ezee.insurence.service;

import java.util.List;

import com.ezee.insurence.dto.AuthDTO;
import com.ezee.insurence.dto.PolicyDTO;

public interface PolicyService {
	public void addPolicy(AuthDTO authDTO, PolicyDTO policyDTO);

	public void updatePolicy(AuthDTO authDTO, PolicyDTO policyDTO);

	public PolicyDTO getPolicyByCode(String code);

	public List<PolicyDTO> getAllPolicy();

	public PolicyDTO getClaimByPolicy(PolicyDTO policyDTO);

	public PolicyDTO getClaimByPolicyNum(PolicyDTO policyDTO);

	public PolicyDTO getReciptByPolicyNum(PolicyDTO policyDTO);

	public PolicyDTO getRenewalByPolicyNumber(PolicyDTO policyDTO);

}
