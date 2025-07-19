package com.ezee.insurence.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezee.insurence.cache.ClaimCacheService;
import com.ezee.insurence.cache.PolicyCacheService;
import com.ezee.insurence.cache.VehicleCacheService;
import com.ezee.insurence.dao.CustomerDAO;
import com.ezee.insurence.dao.IncidentDAO;
import com.ezee.insurence.dao.PaymentDAO;
import com.ezee.insurence.dao.ReciptDAO;
import com.ezee.insurence.dao.RenewalDAO;
import com.ezee.insurence.dto.AuthDTO;
import com.ezee.insurence.dto.ClaimDTO;
import com.ezee.insurence.dto.CustomerDTO;
import com.ezee.insurence.dto.IncidentDTO;
import com.ezee.insurence.dto.PaymentDTO;
import com.ezee.insurence.dto.PolicyDTO;
import com.ezee.insurence.dto.ReciptDTO;
import com.ezee.insurence.dto.RenewalDTO;
import com.ezee.insurence.dto.VehicleDTO;
import com.ezee.insurence.exception.ErrorCode;
import com.ezee.insurence.exception.ServiceException;
import com.ezee.insurence.service.CustomerService;

import com.ezee.insurence.util.TokenGenerator;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private VehicleCacheService vehicleCacheService;

	@Autowired
	private PolicyCacheService policyCacheService;

	@Autowired
	private IncidentDAO incidentDAO;

	@Autowired
	private PaymentDAO paymentDAO;

	@Autowired
	private ReciptDAO reciptDAO;

	@Autowired
	private ClaimCacheService claimCacheService;

	@Autowired
	private RenewalDAO renewalDAO;

	public static final Logger logger = LogManager.getLogger("com.ezee.insurence.service.impl");

	@Override
	public void addCustomer(AuthDTO authDTO, CustomerDTO customerDTO) {

		try {
			String code = TokenGenerator.generateCode("CUS", 12);
			customerDTO.setCode(code);
			int affected = customerDAO.customerUID(authDTO, customerDTO);
			if (affected == 0) {
				throw new ServiceException(ErrorCode.CUSTOMER_NOT_CREATED);
			}
			logger.info("Insert the customer record");
		} catch (ServiceException e) {
			logger.error("Service exception : Error while Inserting the recored {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Error while Inserting the recored {} ", e.getMessage());
			throw e;
		}

	}

	@Override
	public void updateCustomer(AuthDTO authDTO, CustomerDTO customerDTO) {
		try {
			int affected = customerDAO.customerUID(authDTO, customerDTO);
			if (affected == 0) {
				throw new ServiceException(ErrorCode.CUSTOMER_NOT_CREATED);
			}
			logger.info("Update the customer record");
		} catch (ServiceException e) {
			logger.error("Service exception : Error while updateing the customer recored {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Error while updateing the customer recored {} ", e.getMessage());
			throw e;
		}
	}

	@Override
	public CustomerDTO getCustomerByCode(String code) {
		CustomerDTO customerDTO = new CustomerDTO();
		try {
			customerDTO.setCode(code);
			customerDTO = customerDAO.getCustomerByCode(customerDTO);
			if (customerDTO.getId() == 0) {
				throw new ServiceException(ErrorCode.CUSTOMER_NOT_FOUND);
			}
			logger.info("Fetch specific customer record {} ", customerDTO.getCode());
		} catch (ServiceException e) {
			logger.error("Service exception : Error while specific customer record {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Error while specific customer record {} ", e.getMessage());
			throw e;
		}
		return customerDTO;
	}

	@Override
	public List<CustomerDTO> getAllCustomer() {
		List<CustomerDTO> listDTO = new ArrayList<>();
		try {

			listDTO = customerDAO.getAllCustomer();
			if (listDTO == null || listDTO.isEmpty()) {
				throw new ServiceException(ErrorCode.CUSTOMER_NOT_FOUND);
			}
			logger.info("Fetch all customer record ");
		} catch (ServiceException e) {
			logger.error("Service exception : Error while all customer record {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Error while all customer record {} ", e.getMessage());
			throw e;
		}
		return listDTO;
	}

	@Override
	public CustomerDTO getVehicleByCustomer(CustomerDTO customerDTO) {
		CustomerDTO dto = new CustomerDTO();
		try {
			dto = customerDAO.getVehicleByCustomer(customerDTO);
			if (dto.getId() == 0) {
				throw new ServiceException(ErrorCode.CUSTOMER_NOT_FOUND);
			}
			List<VehicleDTO> VehicleList = new ArrayList<>();
			for (VehicleDTO vehicleDTO : dto.getVehicleDTO()) {
				vehicleDTO = vehicleCacheService.getVehicleCache(vehicleDTO);
				VehicleList.add(vehicleDTO);
			}
			dto.setVehicleDTO(VehicleList);
			logger.info("Fetch the policy record from by Customer {} ", customerDTO.getCode());
		} catch (ServiceException e) {
			logger.error("Service exception : Error while fetch the vehicle record by the customer {} ",
					e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Error while fetch the vehicle record by the customer {} ", e.getMessage());
			throw e;
		}
		return dto;
	}

	@Override
	public CustomerDTO getPolicyByCustomer(CustomerDTO customerDTO) {
		CustomerDTO dto = new CustomerDTO();
		try {

			dto = customerDAO.getPolicyByCustomer(customerDTO);
			if (dto.getId() == 0) {
				throw new ServiceException(ErrorCode.CUSTOMER_NOT_FOUND);
			}
			List<PolicyDTO> policyList = new ArrayList<>();
			for (PolicyDTO policyDTO : dto.getPolicyDTO()) {
				policyDTO = policyCacheService.getPolicyCache(policyDTO);
				VehicleDTO vehicleDTO = vehicleCacheService.getVehicleCache(policyDTO.getVehicleDTO());
				policyDTO.setVehicleDTO(vehicleDTO);
				policyList.add(policyDTO);
			}
			dto.setPolicyDTO(policyList);
			logger.info("Fetch the policy record from by Customer {} ", customerDTO.getCode());
		} catch (ServiceException e) {
			logger.error("Service exception : Error while fetch the policy record by the customer {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Error while fetch the policy record by the customer {} ", e.getMessage());
			throw e;
		}
		return dto;
	}

	@Override
	public CustomerDTO getIncidentByCustomer(CustomerDTO customerDTO) {
		CustomerDTO dto = new CustomerDTO();
		try {

			dto = customerDAO.getIncidentByCustomer(customerDTO);
			if (dto.getId() == 0) {
				throw new ServiceException(ErrorCode.CUSTOMER_NOT_FOUND);
			}
			List<IncidentDTO> incidentList = new ArrayList<>();
			for (IncidentDTO incidentDTO : dto.getIncidentDTO()) {
				incidentDTO = incidentDAO.getIncidentByCode(incidentDTO);
				if (incidentDTO.getId() == 0) {
					throw new ServiceException(ErrorCode.INCIDENT_NOT_FOUND);
				}
				incidentList.add(incidentDTO);
			}
			dto.setIncidentDTO(incidentList);
			logger.info("Fetch the incident record from by Customer {} ", customerDTO.getCode());
		} catch (ServiceException e) {
			logger.error("Service exception : Error while fetch the incident record by the customer {} ",
					e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Error while fetch the incident record by the customer {} ", e.getMessage());
			throw e;
		}
		return dto;
	}

	@Override
	public CustomerDTO getPaymentByCustomer(CustomerDTO customerDTO) {
		CustomerDTO dto = new CustomerDTO();
		try {

			dto = customerDAO.getPaymentByCustomer(customerDTO);
			if (dto.getId() == 0) {
				throw new ServiceException(ErrorCode.CUSTOMER_NOT_FOUND);
			}
			List<PaymentDTO> paymentList = new ArrayList<>();
			for (PaymentDTO paymentDTO : dto.getPaymentDTO()) {
				paymentDTO = paymentDAO.getPaymentByCode(paymentDTO);
				if (paymentDTO.getId() == 0) {
					throw new ServiceException(ErrorCode.PAYMENT_NOT_FOUND);
				}
				ReciptDTO reciptDTO = reciptDAO.getReciptByCode(paymentDTO.getReciptDTO());

				if (reciptDTO.getPolicyDTO() != null) {
					PolicyDTO policyDTO = policyCacheService.getPolicyCache(reciptDTO.getPolicyDTO());
					VehicleDTO vehicleDTO = vehicleCacheService.getVehicleCache(policyDTO.getVehicleDTO());
					policyDTO.setVehicleDTO(vehicleDTO);
					reciptDTO.setPolicyDTO(policyDTO);
				} else {
					reciptDTO.setPolicyDTO(null);
				}
				if (reciptDTO.getRenewalDTO() != null) {
					RenewalDTO renewalDTO = renewalDAO.getRenewalByCode(reciptDTO.getRenewalDTO());
					PolicyDTO policyDTO = policyCacheService.getPolicyCache(renewalDTO.getPolicyDTO());
					VehicleDTO vehicleDTO = vehicleCacheService
							.getVehicleCache(renewalDTO.getPolicyDTO().getVehicleDTO());
					policyDTO.setVehicleDTO(vehicleDTO);
					reciptDTO.setRenewalDTO(renewalDTO);
				} else {
					reciptDTO.setRenewalDTO(null);
				}
				paymentDTO.setReciptDTO(reciptDTO);
				paymentList.add(paymentDTO);
			}
			dto.setPaymentDTO(paymentList);
			logger.info("Fetch the payment record from by Customer {} ", customerDTO.getCode());
		} catch (ServiceException e) {
			logger.error("Service exception : Error while fetch the payment record by the customer {} ",
					e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Error while fetch the payment record by the customer {} ", e.getMessage());
			throw e;
		}
		return dto;
	}

	@Override
	public CustomerDTO getReciptByCustomer(CustomerDTO customerDTO) {
		CustomerDTO dto = new CustomerDTO();
		try {

			dto = customerDAO.getReciptByCustomer(customerDTO);
			if (dto.getId() == 0) {
				throw new ServiceException(ErrorCode.CUSTOMER_NOT_FOUND);
			}
			List<ReciptDTO> reciptList = new ArrayList<>();
			for (ReciptDTO reciptDTO : dto.getReciptDTO()) {
				reciptDTO = reciptDAO.getReciptByCode(reciptDTO);
				if (reciptDTO.getId() == 0) {
					throw new ServiceException(ErrorCode.PAYMENT_NOT_FOUND);
				}

				if (reciptDTO.getPolicyDTO() != null) {
					PolicyDTO policyDTO = policyCacheService.getPolicyCache(reciptDTO.getPolicyDTO());
					VehicleDTO vehicleDTO = vehicleCacheService.getVehicleCache(policyDTO.getVehicleDTO());
					policyDTO.setVehicleDTO(vehicleDTO);
					reciptDTO.setPolicyDTO(policyDTO);
				} else {
					reciptDTO.setPolicyDTO(null);
				}
				if (reciptDTO.getRenewalDTO() != null) {
					RenewalDTO renewalDTO = renewalDAO.getRenewalByCode(reciptDTO.getRenewalDTO());
					PolicyDTO policyDTO = policyCacheService.getPolicyCache(renewalDTO.getPolicyDTO());
					VehicleDTO vehicleDTO = vehicleCacheService
							.getVehicleCache(renewalDTO.getPolicyDTO().getVehicleDTO());
					policyDTO.setVehicleDTO(vehicleDTO);
					reciptDTO.setRenewalDTO(renewalDTO);
				} else {
					reciptDTO.setRenewalDTO(null);
				}

				reciptList.add(reciptDTO);
			}
			dto.setReciptDTO(reciptList);
			logger.info("Fetch the recipt record from by Customer {} ", customerDTO.getCode());
		} catch (ServiceException e) {
			logger.error("Service exception : Error while fetch the recipt record by the customer {} ",
					e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Error while fetch the recipt record by the customer {} ", e.getMessage());
			throw e;
		}
		return dto;
	}

	@Override
	public CustomerDTO getClaimByCustomer(CustomerDTO customerDTO) {
		CustomerDTO dto = new CustomerDTO();
		try {

			dto = customerDAO.getClaimByCustomer(customerDTO);
			if (dto.getId() == 0) {
				throw new ServiceException(ErrorCode.CUSTOMER_NOT_FOUND);
			}
			List<ClaimDTO> claimList = new ArrayList<>();
			for (ClaimDTO claimDTO : dto.getClaimDTO()) {
				claimDTO = claimCacheService.getClaimCache(claimDTO);
				PolicyDTO policyDTO = policyCacheService.getPolicyCache(claimDTO.getPolicyDTO());
				VehicleDTO vehicleDTO = vehicleCacheService.getVehicleCache(policyDTO.getVehicleDTO());
				policyDTO.setVehicleDTO(vehicleDTO);
				claimDTO.setPolicyDTO(policyDTO);
				IncidentDTO incidentDTO = incidentDAO.getIncidentByCode(claimDTO.getIncidentDTO());
				if (incidentDTO.getId() == 0) {
					throw new ServiceException(ErrorCode.INCIDENT_NOT_FOUND);
				}
				claimDTO.setIncidentDTO(incidentDTO);
				claimList.add(claimDTO);
			}
			dto.setClaimDTO(claimList);
			logger.info("Fetch the payment record from by Customer {} ", customerDTO.getCode());
		} catch (ServiceException e) {
			logger.error("Service exception : Error while fetch the payment record by the customer {} ",
					e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Error while fetch the payment record by the customer {} ", e.getMessage());
			throw e;
		}
		return dto;
	}

	@Override
	public CustomerDTO getActivePolicyByCustomer(CustomerDTO customerDTO, PolicyDTO policyDTO) {

		CustomerDTO dto = new CustomerDTO();
		try {

			dto = customerDAO.getActivePolicyByCustomer(customerDTO, policyDTO);
			if (dto.getId() == 0) {
				throw new ServiceException(ErrorCode.CUSTOMER_NOT_FOUND);
			}
			List<PolicyDTO> policyList = new ArrayList<>();
			for (PolicyDTO pDTO : dto.getPolicyDTO()) {
				pDTO = policyCacheService.getPolicyCache(pDTO);
				VehicleDTO vehicleDTO = vehicleCacheService.getVehicleCache(pDTO.getVehicleDTO());
				pDTO.setVehicleDTO(vehicleDTO);
				policyList.add(pDTO);
			}
			dto.setPolicyDTO(policyList);
			logger.info("Fetch the policy record from by Customer {} ", customerDTO.getCode());
		} catch (ServiceException e) {
			logger.error("Service exception : Error while fetch the policy record by the customer {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Error while fetch the policy record by the customer {} ", e.getMessage());
			throw e;
		}
		return dto;

	}

}
