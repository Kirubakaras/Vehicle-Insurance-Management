package com.ezee.insurence.service;

import java.util.List;

import com.ezee.insurence.dto.AuthDTO;
import com.ezee.insurence.dto.CustomerDTO;
import com.ezee.insurence.dto.PolicyDTO;

public interface CustomerService {

	public void addCustomer(AuthDTO authDTO, CustomerDTO customerDTO);

	public void updateCustomer(AuthDTO authDTO, CustomerDTO customerDTO);

	public CustomerDTO getCustomerByCode(String code);

	public List<CustomerDTO> getAllCustomer();

	public CustomerDTO getVehicleByCustomer(CustomerDTO customerDTO);

	public CustomerDTO getPolicyByCustomer(CustomerDTO customerDTO);

	public CustomerDTO getIncidentByCustomer(CustomerDTO customerDTO);

	public CustomerDTO getPaymentByCustomer(CustomerDTO customerDTO);

	public CustomerDTO getReciptByCustomer(CustomerDTO customerDTO);

	public CustomerDTO getClaimByCustomer(CustomerDTO customerDTO);

	public CustomerDTO getActivePolicyByCustomer(CustomerDTO customerDTO, PolicyDTO policyDTO);
}
