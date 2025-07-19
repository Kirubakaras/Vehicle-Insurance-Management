package com.ezee.insurence.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezee.insurence.cache.CustomerCacheService;
import com.ezee.insurence.cache.PolicyCacheService;
import com.ezee.insurence.cache.VehicleCacheService;
import com.ezee.insurence.dao.ReciptDAO;
import com.ezee.insurence.dao.RenewalDAO;
import com.ezee.insurence.dto.AuthDTO;
import com.ezee.insurence.dto.CustomerDTO;
import com.ezee.insurence.dto.PolicyDTO;
import com.ezee.insurence.dto.ReciptDTO;
import com.ezee.insurence.dto.RenewalDTO;
import com.ezee.insurence.dto.VehicleDTO;
import com.ezee.insurence.exception.ErrorCode;
import com.ezee.insurence.exception.ServiceException;
import com.ezee.insurence.service.ReciptService;
import com.ezee.insurence.util.StringUtil;
import com.ezee.insurence.util.TokenGenerator;

@Service
public class ReciptServiceImpl implements ReciptService {
	@Autowired
	private ReciptDAO reciptDAO;

	@Autowired
	private PolicyCacheService policyCache;

	@Autowired
	private CustomerCacheService customerCache;

	@Autowired
	private VehicleCacheService vehicleCache;

	@Autowired
	private RenewalDAO renewalDAO;

	private static final Logger logger = LogManager.getLogger("com.ezee.insurence.service.impl");

	@Override
	public void addRecipt(AuthDTO authDTO, ReciptDTO reciptDTO) {
		try {
			String code = TokenGenerator.generateCode("REC", 12);
			reciptDTO.setCode(code);

			if (reciptDTO.getPolicyDTO().getCode().startsWith("POL")) {
				PolicyDTO policyDTO = policyCache.getPolicyCache(reciptDTO.getPolicyDTO());

				if (policyDTO.getPaymentSchedule().equalsIgnoreCase("MONTHLY")) {
					String startDate = policyDTO.getStartDate();
					String schedule = policyDTO.getPaymentSchedule();
					int existingReceiptCount = reciptDAO.countReceiptsByPolicy(policyDTO);
					String dueDate = calculateNextDueDate(startDate, existingReceiptCount, schedule);
					reciptDTO.setDueDate(dueDate);
				} else if (policyDTO.getPaymentSchedule().equalsIgnoreCase("YEARLY")) {
					String startDate = policyDTO.getStartDate();
					String schedule = policyDTO.getPaymentSchedule();
					int existingReceiptCount = reciptDAO.countReceiptsByPolicy(policyDTO);
					String dueDate = calculateNextDueDate(startDate, existingReceiptCount, schedule);
					reciptDTO.setDueDate(dueDate);
				}

				BigDecimal penaltyAmount = calculatePenalty(reciptDTO.getDueDate(), reciptDTO.getReciptDate());
				reciptDTO.setPenaltyAmount(penaltyAmount);

				BigDecimal policyAmount = reciptDTO.getReciptAmount();

				BigDecimal totalAmount = policyAmount.add(penaltyAmount);
				reciptDTO.setReciptTotalAmount(totalAmount);
			} else if (reciptDTO.getRenewalDTO().getCode().startsWith("REN")) {
				BigDecimal policyAmount = reciptDTO.getReciptAmount();
				reciptDTO.setReciptTotalAmount(policyAmount);
			}

			int affected = reciptDAO.reciptUID(authDTO, reciptDTO);
			if (affected == 0) {
				throw new ServiceException(ErrorCode.RECIPT_NOT_CREATE);
			}
			logger.info("Insert the receipt record");
		} catch (ServiceException e) {
			logger.error("ServiceException : Insert the receipt record {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Insert the receipt record {}", e.getMessage());
			throw e;
		}
	}

	@Override
	public void updateRecipt(AuthDTO authDTO, ReciptDTO reciptDTO) {
		try {
			if (reciptDTO.getPolicyDTO().getCode().startsWith("POL")) {
				PolicyDTO policyDTO = policyCache.getPolicyCache(reciptDTO.getPolicyDTO());

				if (policyDTO.getPaymentSchedule().equalsIgnoreCase("MONTHLY")) {
					String startDate = policyDTO.getStartDate();
					String schedule = policyDTO.getPaymentSchedule();
					int existingReceiptCount = reciptDAO.countReceiptsByPolicy(policyDTO);
					String dueDate = calculateNextDueDate(startDate, existingReceiptCount, schedule);
					reciptDTO.setDueDate(dueDate);
				} else if (policyDTO.getPaymentSchedule().equalsIgnoreCase("YEARLY")) {
					String startDate = policyDTO.getStartDate();
					String schedule = policyDTO.getPaymentSchedule();
					int existingReceiptCount = reciptDAO.countReceiptsByPolicy(policyDTO);
					String dueDate = calculateNextDueDate(startDate, existingReceiptCount, schedule);
					reciptDTO.setDueDate(dueDate);
				}

				BigDecimal penaltyAmount = calculatePenalty(reciptDTO.getDueDate(), reciptDTO.getReciptDate());
				reciptDTO.setPenaltyAmount(penaltyAmount);

				BigDecimal policyAmount = reciptDTO.getReciptAmount();

				BigDecimal totalAmount = policyAmount.add(penaltyAmount);
				reciptDTO.setReciptTotalAmount(totalAmount);
			} else if (reciptDTO.getRenewalDTO().getCode().startsWith("REN")) {

				BigDecimal policyAmount = reciptDTO.getReciptAmount();
				reciptDTO.setReciptTotalAmount(policyAmount);
			}

			int affected = reciptDAO.reciptUID(authDTO, reciptDTO);
			if (affected == 0) {
				throw new ServiceException(ErrorCode.RECIPT_NOT_CREATE);
			}
			logger.info("Update the receipt record");
		} catch (ServiceException e) {
			logger.error("ServiceException : Insert the receipt record {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Insert the receipt record {}", e.getMessage());
			throw e;
		}

	}

	@Override
	public ReciptDTO getReciptByCode(String code) {
		ReciptDTO reciptDTO = new ReciptDTO();
		try {
			reciptDTO.setCode(code);
			reciptDTO = reciptDAO.getReciptByCode(reciptDTO);
			if (reciptDTO != null) {
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
				CustomerDTO customerDTO = customerCache.getCustomerCache(reciptDTO.getCustomerDTO());
				reciptDTO.setCustomerDTO(customerDTO);
			} else {
				throw new ServiceException(ErrorCode.RECIPT_NOT_FOUND);
			}
			logger.info("fetch the receipt record by code {} ", reciptDTO.getCode());
		} catch (ServiceException e) {
			logger.error("ServiceException : Get receipt by code {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Get receipt by code {}", e.getMessage());
			throw e;
		}
		return reciptDTO;
	}

	@Override
	public List<ReciptDTO> getAllRecipt() {
		List<ReciptDTO> list = new ArrayList<>();
		try {
			list = reciptDAO.getAllRecipt();
			if (list == null) {
				throw new ServiceException(ErrorCode.RECIPT_NOT_FOUND);
			}
			for (ReciptDTO reciptDTO : list) {
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
				CustomerDTO customerDTO = customerCache.getCustomerCache(reciptDTO.getCustomerDTO());
				reciptDTO.setCustomerDTO(customerDTO);
			}
		} catch (ServiceException e) {
			logger.error("Service exception : Fetch all receipts {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Exception : Fetch all receipts {}", e.getMessage());
			throw new ServiceException(e.getMessage());
		}
		return list;
	}

	private BigDecimal calculatePenalty(String dueDateStr, String receiptDateStr) {
		LocalDate expiryDate = StringUtil.getDateformate(dueDateStr);
		LocalDate receiptDate = StringUtil.getDateformate(receiptDateStr);

		long delayDays = ChronoUnit.DAYS.between(expiryDate, receiptDate);
		BigDecimal amount = null;
		if (delayDays <= 0) {
			amount = BigDecimal.ZERO;
		} else {
			amount = BigDecimal.valueOf(delayDays * 10);
		}

		return amount;
	}

	public String calculateNextDueDate(String startDateStr, int paymentIndex, String schedule) {

		LocalDate startDate = StringUtil.getDateformate(startDateStr);
		LocalDate dueDate;

		if (schedule.equalsIgnoreCase("MONTHLY")) {
			dueDate = startDate.plusMonths(paymentIndex);
		} else if (schedule.equalsIgnoreCase("YEARLY")) {
			dueDate = startDate.plusYears(paymentIndex);
		} else {
			dueDate = startDate.plusYears(paymentIndex);
		}
		return dueDate.toString();
	}

}
