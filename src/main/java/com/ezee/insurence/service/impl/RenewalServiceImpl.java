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

import com.ezee.insurence.cache.CustomerCacheService;
import com.ezee.insurence.cache.PolicyCacheService;
import com.ezee.insurence.cache.VehicleCacheService;
import com.ezee.insurence.dao.ClaimDAO;
import com.ezee.insurence.dao.ReciptDAO;
import com.ezee.insurence.dao.RenewalDAO;
import com.ezee.insurence.dto.AuthDTO;
import com.ezee.insurence.dto.ClaimDTO;
import com.ezee.insurence.dto.CustomerDTO;
import com.ezee.insurence.dto.PolicyDTO;
import com.ezee.insurence.dto.ReciptDTO;
import com.ezee.insurence.dto.RenewalDTO;
import com.ezee.insurence.dto.VehicleDTO;
import com.ezee.insurence.exception.ErrorCode;
import com.ezee.insurence.exception.ServiceException;
import com.ezee.insurence.service.RenewalService;
import com.ezee.insurence.util.TokenGenerator;

@Service
public class RenewalServiceImpl implements RenewalService {
	@Autowired
	private RenewalDAO renewalDAO;

	@Autowired
	private PolicyCacheService policyCache;

	@Autowired
	private CustomerCacheService customerCache;

	@Autowired
	private VehicleCacheService vehicleCache;

	@Autowired
	private ReciptDAO reciptDAO;

	@Autowired
	private ClaimDAO claimDAO;

	private static final Logger logger = LogManager.getLogger("com.ezee.insurence.service.impl");

	@Override
	public void addRenewal(AuthDTO authDTO, RenewalDTO renewalDTO) {
		try {
			String code = TokenGenerator.generateCode("REN", 12);
			renewalDTO.setCode(code);

			BigDecimal renewalAmount = calculateRenewalAmount(renewalDTO.getPolicyDTO());
			renewalDTO.setRenewalAmount(renewalAmount);

			int affected = renewalDAO.renewalUID(authDTO, renewalDTO);
			if (affected == 0) {
				throw new ServiceException(ErrorCode.RENEWAL_NOT_FOUND);
			}
			logger.info("Insert the renewal record ");
		} catch (ServiceException e) {
			logger.error("ServiceException : Insert the renewl record {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Insert the renewl record {} ", e.getMessage());
			throw e;
		}
	}

	@Override
	public void updateRenewal(AuthDTO authDTO, RenewalDTO renewalDTO) {
		try {

			BigDecimal renewalAmount = calculateRenewalAmount(renewalDTO.getPolicyDTO());
			renewalDTO.setRenewalAmount(renewalAmount);

			int affected = renewalDAO.renewalUID(authDTO, renewalDTO);
			if (affected == 0) {
				throw new ServiceException(ErrorCode.RENEWAL_NOT_FOUND);
			}
			logger.info("Update the renewal record ");
		} catch (ServiceException e) {
			logger.error("ServiceException : update the renewl record {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : update the renewl record {} ", e.getMessage());
			throw e;
		}
	}

	@Override
	public RenewalDTO getRenewalByCode(String code) {
		RenewalDTO renewalDTO = new RenewalDTO();
		try {
			renewalDTO.setCode(code);
			renewalDTO = renewalDAO.getRenewalByCode(renewalDTO);
			if (renewalDTO != null) {
				PolicyDTO policyDTO = policyCache.getPolicyCache(renewalDTO.getPolicyDTO());
				VehicleDTO vehicleDTO = vehicleCache.getVehicleCache(policyDTO.getVehicleDTO());
				policyDTO.setVehicleDTO(vehicleDTO);
				CustomerDTO customerDTO = customerCache.getCustomerCache(vehicleDTO.getCustomerDTO());
				policyDTO.setCustomerDTO(customerDTO);
				renewalDTO.setPolicyDTO(policyDTO);
			} else {
				throw new ServiceException(ErrorCode.RENEWAL_NOT_FOUND);
			}

		} catch (ServiceException e) {
			logger.error("ServiceException : update the renewl record {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("ServiceException : update the renewl record {} ", e.getMessage());
			throw e;
		}

		return renewalDTO;
	}

	@Override
	public List<RenewalDTO> getAllRenewal() {
		List<RenewalDTO> listDTO = new ArrayList<>();
		try {
			listDTO = renewalDAO.getAllRenewal();
			if (listDTO != null) {
				for (RenewalDTO renewalDTO : listDTO) {
					PolicyDTO policyDTO = policyCache.getPolicyCache(renewalDTO.getPolicyDTO());
					VehicleDTO vehicleDTO = vehicleCache.getVehicleCache(policyDTO.getVehicleDTO());
					policyDTO.setVehicleDTO(vehicleDTO);
					CustomerDTO customerDTO = customerCache.getCustomerCache(vehicleDTO.getCustomerDTO());
					policyDTO.setCustomerDTO(customerDTO);
					renewalDTO.setPolicyDTO(policyDTO);
				}
			} else {
				throw new ServiceException(ErrorCode.RENEWAL_NOT_FOUND);
			}

		} catch (ServiceException e) {
			logger.error("ServiceException : update the renewl record {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("ServiceException : update the renewl record {} ", e.getMessage());
			throw e;
		}
		return listDTO;
	}

	@Override
	public RenewalDTO getReciptByRenewalCode(RenewalDTO renewalDTO) {
		try {
			renewalDTO = renewalDAO.getReciptByRenewalCode(renewalDTO);
			if (renewalDTO != null) {
				PolicyDTO policyDTO = policyCache.getPolicyCache(renewalDTO.getPolicyDTO());
				VehicleDTO vehicleDTO = vehicleCache.getVehicleCache(policyDTO.getVehicleDTO());
				policyDTO.setVehicleDTO(vehicleDTO);
				CustomerDTO customerDTO = customerCache.getCustomerCache(vehicleDTO.getCustomerDTO());
				policyDTO.setCustomerDTO(customerDTO);
				renewalDTO.setPolicyDTO(policyDTO);
				List<ReciptDTO> listRecipt = new ArrayList<>();
				for (ReciptDTO reciptDTO : renewalDTO.getReciptDTO()) {
					reciptDTO = reciptDAO.getReciptByCode(reciptDTO);
					listRecipt.add(reciptDTO);
				}
				renewalDTO.setReciptDTO(listRecipt);

			} else {
				throw new ServiceException(ErrorCode.RENEWAL_NOT_FOUND);
			}

		} catch (ServiceException e) {
			logger.error("ServiceException : update the renewl record {} ", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("ServiceException : update the renewl record {} ", e.getMessage());
			throw e;
		}

		return renewalDTO;
	}

	// calculate the renewal amount

	public BigDecimal calculateRenewalAmount(PolicyDTO policy) {

		PolicyDTO policyDTO = policyCache.getPolicyCache(policy);

		BigDecimal basePremium = policyDTO.getPremiumAmount();
		BigDecimal renewalAmount = basePremium;

		List<ClaimDTO> listClaimDTO = claimDAO.getClaimsByPolicyId(policyDTO);
		int count = listClaimDTO.size();

		int noClaimYears = 0;
		if (count == 0) {
			noClaimYears = getPolicyYear(policyDTO.getStartDate(), policy.getExpriyDate());
		}

		if (noClaimYears > 0) {
			BigDecimal ncbDiscount = new BigDecimal("0.10").multiply(new BigDecimal(noClaimYears)); // 10% per year

			if (ncbDiscount.compareTo(new BigDecimal("0.50")) > 0) {
				ncbDiscount = new BigDecimal("0.50");
			}
			renewalAmount = renewalAmount.subtract(basePremium.multiply(ncbDiscount));
		}

		BigDecimal tax = renewalAmount.multiply(new BigDecimal("0.18"));
		renewalAmount = renewalAmount.add(tax);

		return renewalAmount;
	}

	// calculate the duration of the year

	public int getPolicyYear(String startdate, String expiryDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		LocalDate startType = LocalDate.parse(startdate, formatter);
		LocalDate expiryType = LocalDate.parse(expiryDate, formatter);

		Period duration = Period.between(startType, expiryType);

		return duration.getYears();
	}

}
