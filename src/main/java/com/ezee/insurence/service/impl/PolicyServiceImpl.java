package com.ezee.insurence.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezee.insurence.cache.ClaimCacheService;
import com.ezee.insurence.cache.CustomerCacheService;
import com.ezee.insurence.cache.RenewalCacheService;
import com.ezee.insurence.cache.VehicleCacheService;
import com.ezee.insurence.dao.IncidentDAO;
import com.ezee.insurence.dao.PolicyDAO;
import com.ezee.insurence.dao.ReciptDAO;
import com.ezee.insurence.dto.AuthDTO;
import com.ezee.insurence.dto.ClaimDTO;
import com.ezee.insurence.dto.CustomerDTO;
import com.ezee.insurence.dto.IncidentDTO;
import com.ezee.insurence.dto.PolicyDTO;
import com.ezee.insurence.dto.ReciptDTO;
import com.ezee.insurence.dto.RenewalDTO;
import com.ezee.insurence.dto.VehicleDTO;
import com.ezee.insurence.exception.ErrorCode;
import com.ezee.insurence.exception.ServiceException;
import com.ezee.insurence.service.PolicyService;
import com.ezee.insurence.util.TokenGenerator;

@Service
public class PolicyServiceImpl implements PolicyService {

	@Autowired
	private PolicyDAO policyDAO;

	@Autowired
	private CustomerCacheService customerCache;

	@Autowired
	private VehicleCacheService vehicleCache;

	@Autowired
	private ClaimCacheService claimCache;

	@Autowired
	private RenewalCacheService renewalCache;

	@Autowired
	private IncidentDAO incidentDAO;

	@Autowired
	private ReciptDAO reciptDAO;

	private static final Logger logger = LogManager.getLogger("com.ezee.insurence.service.impl");

	@Override
	public void addPolicy(AuthDTO authDTO, PolicyDTO policyDTO) {
		try {

			String code = TokenGenerator.generateCode("POL", 12);
			policyDTO.setCode(code);

			String policyNum = TokenGenerator.generateNum("PRE", 12);
			policyDTO.setPolicyNumber(policyNum);

			if (policyDTO.getPaymentSchedule().equalsIgnoreCase("YEARLY")) {
				BigDecimal premiumAmount = calculatYearPremium(policyDTO.getVehicleDTO(), policyDTO.getTotalAmount());
				policyDTO.setPremiumAmount(premiumAmount);
			} else if (policyDTO.getPaymentSchedule().equalsIgnoreCase("MONTHLY")) {
				BigDecimal premiumAmount = calculateMonthPremium(policyDTO.getVehicleDTO(), policyDTO.getTotalAmount());
				policyDTO.setPremiumAmount(premiumAmount);
			}

			int affected = policyDAO.policyUID(authDTO, policyDTO);

			if (affected == 0) {
				throw new ServiceException(ErrorCode.POLICY_NOT_FOUND);
			}
			logger.info("Insert the receipt record");

		} catch (ServiceException e) {
			logger.error("Service Exception : Error while Inserting the policy record {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Error while Inserting the policy record {}", e.getMessage());
			throw e;
		}
	}

	@Override
	public void updatePolicy(AuthDTO authDTO, PolicyDTO policyDTO) {
		try {

			if (policyDTO.getPaymentSchedule().equalsIgnoreCase("YEARLY")) {
				BigDecimal premiumAmount = calculatYearPremium(policyDTO.getVehicleDTO(), policyDTO.getTotalAmount());
				policyDTO.setPremiumAmount(premiumAmount);
			} else if (policyDTO.getPaymentSchedule().equalsIgnoreCase("MONTHLY")) {
				BigDecimal premiumAmount = calculateMonthPremium(policyDTO.getVehicleDTO(), policyDTO.getTotalAmount());
				policyDTO.setPremiumAmount(premiumAmount);
			}

			int affected = policyDAO.policyUID(authDTO, policyDTO);

			if (affected == 0) {
				throw new ServiceException(ErrorCode.POLICY_NOT_FOUND);
			}
			logger.info("Update the receipt record");

		} catch (ServiceException e) {
			logger.error("Service Exception : Error while Updateing the policy record {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Error while Updateing the policy record {}", e.getMessage());
			throw e;
		}

	}

	@Override
	public PolicyDTO getPolicyByCode(String code) {
		PolicyDTO policyDTO = new PolicyDTO();
		try {
			policyDTO.setCode(code);
			policyDTO = policyDAO.getPolicyByCode(policyDTO);
			if (policyDTO.getId() != 0) {
				policyDTO.setCustomerDTO(customerCache.getCustomerCache(policyDTO.getCustomerDTO()));
				policyDTO.setVehicleDTO(vehicleCache.getVehicleCache(policyDTO.getVehicleDTO()));
			} else {
				throw new ServiceException(ErrorCode.POLICY_NOT_FOUND);
			}
			logger.info("Fetch the policy detail {} ", policyDTO.getCode());
		} catch (ServiceException e) {
			logger.error("Service Exception : Error while fetch the policy record {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Error while fetch the policy record {} ", e.getMessage());
			throw e;
		}
		return policyDTO;
	}

	@Override
	public List<PolicyDTO> getAllPolicy() {
		List<PolicyDTO> listDTO = new ArrayList<>();
		try {
			listDTO = policyDAO.getAllPolicy();
			if (listDTO != null) {
				for (PolicyDTO policyDTO : listDTO) {
					policyDTO.setCustomerDTO(customerCache.getCustomerCache(policyDTO.getCustomerDTO()));
					policyDTO.setVehicleDTO(vehicleCache.getVehicleCache(policyDTO.getVehicleDTO()));
				}
			} else {
				throw new ServiceException(ErrorCode.POLICY_NOT_FOUND);
			}
			logger.info("Fetch the policy detail");
		} catch (ServiceException e) {
			logger.error("Service Exception : Error while fetch the policy record {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Error while fetch the policy record {} ", e.getMessage());
			throw e;
		}
		return listDTO;
	}

	@Override
	public PolicyDTO getClaimByPolicy(PolicyDTO policyDTO) {
		try {
			policyDTO = policyDAO.getClaimByPolicy(policyDTO);
			if (policyDTO.getId() != 0) {
				policyDTO.setCustomerDTO(customerCache.getCustomerCache(policyDTO.getCustomerDTO()));
				policyDTO.setVehicleDTO(vehicleCache.getVehicleCache(policyDTO.getVehicleDTO()));
				List<ClaimDTO> listClaim = new ArrayList<>();
				for (ClaimDTO claimDTO : policyDTO.getClaimDTO()) {
					ClaimDTO dto = claimCache.getClaimCache(claimDTO);
					IncidentDTO incidentDTO = incidentDAO.getIncidentByCode(dto.getIncidentDTO());
					if (incidentDTO != null) {
						dto.setIncidentDTO(incidentDTO);
					} else {
						throw new ServiceException(ErrorCode.INCIDENT_NOT_FOUND);
					}
					listClaim.add(dto);
				}
				policyDTO.setClaimDTO(listClaim);
			} else {
				throw new ServiceException(ErrorCode.POLICY_NOT_FOUND);
			}
			logger.info("Fetch the claim detail by the policy code {} ", policyDTO.getCode());

		} catch (ServiceException e) {
			logger.error("Service Exception : Error while Fetch the claim detail by the policy {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Error while Fetch the claim detail by the policy {} ", e.getMessage());
			throw e;
		}
		return policyDTO;
	}

	@Override
	public PolicyDTO getClaimByPolicyNum(PolicyDTO policyDTO) {
		try {
			policyDTO = policyDAO.getClaimByPolicyNum(policyDTO);
			if (policyDTO.getId() != 0) {
				policyDTO.setCustomerDTO(customerCache.getCustomerCache(policyDTO.getCustomerDTO()));
				policyDTO.setVehicleDTO(vehicleCache.getVehicleCache(policyDTO.getVehicleDTO()));
				List<ClaimDTO> listClaim = new ArrayList<>();
				for (ClaimDTO claimDTO : policyDTO.getClaimDTO()) {
					ClaimDTO dto = claimCache.getClaimCache(claimDTO);
					IncidentDTO incidentDTO = incidentDAO.getIncidentByCode(dto.getIncidentDTO());
					if (incidentDTO != null) {
						dto.setIncidentDTO(incidentDTO);
					} else {
						throw new ServiceException(ErrorCode.INCIDENT_NOT_FOUND);
					}
					listClaim.add(dto);
				}
				policyDTO.setClaimDTO(listClaim);
			} else {
				throw new ServiceException(ErrorCode.POLICY_NOT_FOUND);
			}
			logger.info("Fetch the claim detail by the policy number {} ", policyDTO.getPolicyNumber());

		} catch (ServiceException e) {
			logger.error("Service Exception : Error while Fetch the claim detail by the policy {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Error while Fetch the claim detail by the policy {} ", e.getMessage());
			throw e;
		}
		return policyDTO;
	}

	@Override
	public PolicyDTO getReciptByPolicyNum(PolicyDTO policyDTO) {
		try {
			policyDTO = policyDAO.getReciptByPolicyNum(policyDTO);
			if (policyDTO.getId() != 0) {
				policyDTO.setCustomerDTO(customerCache.getCustomerCache(policyDTO.getCustomerDTO()));
				policyDTO.setVehicleDTO(vehicleCache.getVehicleCache(policyDTO.getVehicleDTO()));
				List<ReciptDTO> listRecipt = new ArrayList<>();
				for (ReciptDTO reciptDTO : policyDTO.getReciptDTO()) {
					ReciptDTO dto = reciptDAO.getReciptByCode(reciptDTO);
					if (dto.getId() == 0) {
						throw new ServiceException(ErrorCode.RENEWAL_NOT_FOUND);
					}
					listRecipt.add(dto);
				}
				policyDTO.setReciptDTO(listRecipt);
			} else {
				throw new ServiceException(ErrorCode.POLICY_NOT_FOUND);
			}
			logger.info("Fetch the recipt detail by the policy number {} ", policyDTO.getPolicyNumber());

		} catch (

		ServiceException e) {
			logger.error("Service Exception : Error while Fetch the recipt detail by the policy number {} ",
					e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Error while Fetch the recipt detail by the policy number {} ", e.getMessage());
			throw e;
		}
		return policyDTO;
	}

	@Override
	public PolicyDTO getRenewalByPolicyNumber(PolicyDTO policyDTO) {
		try {

			policyDTO = policyDAO.getRenewalByPolicyNumber(policyDTO);
			if (policyDTO.getId() != 0) {
				policyDTO.setCustomerDTO(customerCache.getCustomerCache(policyDTO.getCustomerDTO()));
				policyDTO.setVehicleDTO(vehicleCache.getVehicleCache(policyDTO.getVehicleDTO()));

				List<RenewalDTO> listRenewal = new ArrayList<>();
				for (RenewalDTO renewalDTO : policyDTO.getRenewalDTO()) {
					RenewalDTO dto = renewalCache.getRenewalCache(renewalDTO);
					listRenewal.add(dto);
				}
				policyDTO.setRenewalDTO(listRenewal);
			} else {
				throw new ServiceException(ErrorCode.POLICY_NOT_FOUND);
			}
			logger.info("Fetch the renewal detail by policy code {} ", policyDTO.getPolicyNumber());
		} catch (ServiceException e) {
			logger.error("Service Exception : Error while Fetch the renewal detail by the policy number {} ",
					e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Error while Fetch the renewal detail by the policy number {} ", e.getMessage());
			throw e;
		}
		return policyDTO;
	}

	// calculate the premium amount yearly

	public BigDecimal calculatYearPremium(VehicleDTO vechileDTO, BigDecimal totalAmount) {

		BigDecimal baseRate = totalAmount.multiply(new BigDecimal("0.03"));
		BigDecimal riskMultiplier = BigDecimal.ONE;

		VehicleDTO vehicle = vehicleCache.getVehicleCache(vechileDTO);

		CustomerDTO customer = customerCache.getCustomerCache(vehicle.getCustomerDTO());

		switch (vehicle.getVehicleType()) {
		case "CAR":
			riskMultiplier = new BigDecimal("1.2");
			break;
		case "BIKE":
			riskMultiplier = new BigDecimal("1.0");
			break;
		case "TRUCK":
			riskMultiplier = new BigDecimal("1.5");
			break;
		}

		int age = getAge(customer.getCustomerDOB());
		if (age < 25) {
			riskMultiplier = riskMultiplier.multiply(new BigDecimal("1.3"));
		}

		return baseRate.multiply(riskMultiplier);
	}

	// calculate the premium amount monthly

	public BigDecimal calculateMonthPremium(VehicleDTO vechileDTO, BigDecimal totalAmount) {

		BigDecimal baseRate = totalAmount.multiply(new BigDecimal("0.03"));
		BigDecimal riskMultiplier = BigDecimal.ONE;

		VehicleDTO vehicle = vehicleCache.getVehicleCache(vechileDTO);

		CustomerDTO customer = customerCache.getCustomerCache(vehicle.getCustomerDTO());

		switch (vehicle.getVehicleType()) {
		case "CAR":
			riskMultiplier = new BigDecimal("1.2");
			break;
		case "BIKE":
			riskMultiplier = new BigDecimal("1.0");
			break;
		case "TRUCK":
			riskMultiplier = new BigDecimal("1.5");
			break;
		}

		int age = getAge(customer.getCustomerDOB());
		if (age < 25) {
			riskMultiplier = riskMultiplier.multiply(new BigDecimal("1.3"));
		}
		return baseRate.multiply(riskMultiplier).divide(new BigDecimal("12"));
	}

	// calculate the age for the premium amount

	public int getAge(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate dateType = LocalDate.parse(date, formatter);

		LocalDate now = LocalDate.now();

		Period age = Period.between(dateType, now);

		return age.getYears();
	}

}
