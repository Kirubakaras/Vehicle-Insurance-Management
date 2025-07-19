package com.ezee.insurence.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezee.insurence.cache.CustomerCacheService;
import com.ezee.insurence.cache.PolicyCacheService;
import com.ezee.insurence.cache.VehicleCacheService;
import com.ezee.insurence.dao.PaymentDAO;
import com.ezee.insurence.dao.ReciptDAO;
import com.ezee.insurence.dao.RenewalDAO;
import com.ezee.insurence.dto.AuthDTO;
import com.ezee.insurence.dto.CustomerDTO;
import com.ezee.insurence.dto.PaymentDTO;
import com.ezee.insurence.dto.PolicyDTO;
import com.ezee.insurence.dto.ReciptDTO;
import com.ezee.insurence.dto.RenewalDTO;
import com.ezee.insurence.dto.VehicleDTO;
import com.ezee.insurence.exception.ErrorCode;
import com.ezee.insurence.exception.ServiceException;
import com.ezee.insurence.service.PaymentService;
import com.ezee.insurence.util.TokenGenerator;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private PaymentDAO paymentDAO;

	@Autowired
	private ReciptDAO reciptDAO;

	@Autowired
	private CustomerCacheService customerCache;

	@Autowired
	private VehicleCacheService vehicleCache;

	@Autowired
	private PolicyCacheService policyCache;

	@Autowired
	private RenewalDAO renewalDAO;
	

	private static final Logger logger = LogManager.getLogger("com.ezee.insurence.service.impl");

	@Override
	public PaymentDTO addPayment(AuthDTO authDTO, PaymentDTO paymentDTO) {
		PaymentDTO dto = new PaymentDTO();
		try {
			String code = TokenGenerator.generateCode("PAY", 12);
			paymentDTO.setCode(code);
			
			ReciptDTO reciptDTO = reciptDAO.getReciptByCode(paymentDTO.getReciptDTO());
			
			String paymentDate = reciptDTO.getReciptDate();
			paymentDTO.setPaymentDate(paymentDate);
			
			BigDecimal amount  = reciptDTO.getReciptTotalAmount();
			paymentDTO.setPaymentAmount(amount);
			
			dto = paymentDAO.paymentUID(authDTO, paymentDTO);

			dto = getDTOtoIO(dto);

			logger.info("Inserted payment record with code: {}", paymentDTO.getCode());
		} catch (ServiceException e) {
			logger.error("ServiceException while inserting payment: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception while inserting payment: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return dto;
	}

	@Override
	public PaymentDTO updatePayment(AuthDTO authDTO, PaymentDTO paymentDTO) {
		PaymentDTO dto = new PaymentDTO();
		try {
			dto = paymentDAO.paymentUID(authDTO, paymentDTO);

			dto = getDTOtoIO(dto);
			logger.info("Updated payment record with code: {}", paymentDTO.getCode());
		} catch (ServiceException e) {
			logger.error("ServiceException while Updateing payment: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception while updateing payment: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return dto;
	}

	@Override
	public PaymentDTO getPaymentByCode(String code) {
		PaymentDTO paymentDTO = new PaymentDTO();
		try {
			paymentDTO.setCode(code);
			paymentDTO = paymentDAO.getPaymentByCode(paymentDTO);
			if (paymentDTO.getId() == 0) {
				throw new ServiceException(ErrorCode.PAYMENT_NOT_FOUND);
			}
			ReciptDTO reciptDTO = reciptDAO.getReciptByCode(paymentDTO.getReciptDTO());
			if (reciptDTO.getPolicyDTO() != null) {
				PolicyDTO policyDTO = policyCache.getPolicyCache(reciptDTO.getPolicyDTO());
				VehicleDTO vehicleDTO = vehicleCache.getVehicleCache(policyDTO.getVehicleDTO());
				policyDTO.setVehicleDTO(vehicleDTO);
				reciptDTO.setPolicyDTO(policyDTO);
			} else {
				reciptDTO.setPolicyDTO(null);
			}

			if (reciptDTO.getRenewalDTO() != null) {
				RenewalDTO renewalDTO = renewalDAO.getRenewalByCode(reciptDTO.getRenewalDTO());
				PolicyDTO policyDTO = policyCache.getPolicyCache(renewalDTO.getPolicyDTO());
				VehicleDTO vehicleDTO = vehicleCache.getVehicleCache(renewalDTO.getPolicyDTO().getVehicleDTO());
				policyDTO.setVehicleDTO(vehicleDTO);
				reciptDTO.setRenewalDTO(renewalDTO);
			} else {
				reciptDTO.setRenewalDTO(null);
			}
			CustomerDTO customerDTO = customerCache.getCustomerCache(paymentDTO.getCustomerDTO());
			paymentDTO.setCustomerDTO(customerDTO);

			logger.info("Fetched payment record by code: {}", code);
		} catch (ServiceException e) {
			logger.error("ServiceException while fetching payment: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception while fetching payment: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return paymentDTO;
	}

	@Override
	public List<PaymentDTO> getAllPayments() {
		List<PaymentDTO> paymentList = new ArrayList<>();
		try {
			paymentList = paymentDAO.getAllPayments();
			if (paymentList.isEmpty()) {
				throw new ServiceException(ErrorCode.PAYMENT_NOT_FOUND);
			} else {
				for (PaymentDTO paymentDTO : paymentList) {
					ReciptDTO reciptDTO = reciptDAO.getReciptByCode(paymentDTO.getReciptDTO());
					if (reciptDTO.getPolicyDTO() != null) {
						PolicyDTO policyDTO = policyCache.getPolicyCache(reciptDTO.getPolicyDTO());
						VehicleDTO vehicleDTO = vehicleCache.getVehicleCache(policyDTO.getVehicleDTO());
						policyDTO.setVehicleDTO(vehicleDTO);
						reciptDTO.setPolicyDTO(policyDTO);
					} else {
						reciptDTO.setPolicyDTO(null);
					}

					if (reciptDTO.getRenewalDTO() != null) {
						RenewalDTO renewalDTO = renewalDAO.getRenewalByCode(reciptDTO.getRenewalDTO());
						PolicyDTO policyDTO = policyCache.getPolicyCache(renewalDTO.getPolicyDTO());
						VehicleDTO vehicleDTO = vehicleCache.getVehicleCache(renewalDTO.getPolicyDTO().getVehicleDTO());
						policyDTO.setVehicleDTO(vehicleDTO);
						reciptDTO.setRenewalDTO(renewalDTO);
					} else {
						reciptDTO.setRenewalDTO(null);
					}
					CustomerDTO customerDTO = customerCache.getCustomerCache(paymentDTO.getCustomerDTO());
					paymentDTO.setCustomerDTO(customerDTO);
				}
				logger.info("Fetched all active payments");
			}
		} catch (ServiceException e) {
			logger.error("ServiceException while fetching payments: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception while fetching payments: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return paymentList;

	}

	public PaymentDTO getDTOtoIO(PaymentDTO paymentDTO) {
		ReciptDTO reciptDTO = reciptDAO.getReciptByCode(paymentDTO.getReciptDTO());
		if (reciptDTO.getPolicyDTO() != null) {
			PolicyDTO policyDTO = policyCache.getPolicyCache(reciptDTO.getPolicyDTO());
			VehicleDTO vehicleDTO = vehicleCache.getVehicleCache(policyDTO.getVehicleDTO());
			policyDTO.setVehicleDTO(vehicleDTO);
			reciptDTO.setPolicyDTO(policyDTO);
		} else {
			reciptDTO.setPolicyDTO(null);
		}
		if (reciptDTO.getRenewalDTO() != null) {
			RenewalDTO renewalDTO = renewalDAO.getRenewalByCode(reciptDTO.getRenewalDTO());
			PolicyDTO policyDTO = policyCache.getPolicyCache(renewalDTO.getPolicyDTO());
			VehicleDTO vehicleDTO = vehicleCache.getVehicleCache(renewalDTO.getPolicyDTO().getVehicleDTO());
			policyDTO.setVehicleDTO(vehicleDTO);
			reciptDTO.setRenewalDTO(renewalDTO);
		} else {
			reciptDTO.setRenewalDTO(null);
		}
		CustomerDTO customerDTO = customerCache.getCustomerCache(paymentDTO.getCustomerDTO());
		paymentDTO.setCustomerDTO(customerDTO);

		return paymentDTO;
	}

}
