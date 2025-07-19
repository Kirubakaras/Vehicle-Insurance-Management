package com.ezee.insurence.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ezee.insurence.dto.AuthDTO;
import com.ezee.insurence.dto.ClaimDTO;
import com.ezee.insurence.dto.CustomerDTO;
import com.ezee.insurence.dto.IncidentDTO;
import com.ezee.insurence.dto.PaymentDTO;
import com.ezee.insurence.dto.PolicyDTO;
import com.ezee.insurence.dto.ReciptDTO;
import com.ezee.insurence.dto.RenewalDTO;
import com.ezee.insurence.dto.VehicleDTO;
import com.ezee.insurence.exception.ServiceException;

import lombok.Cleanup;

@Repository
public class CustomerDAO {
	@Autowired
	private DataSource dataSource;

	private static final Logger logger = LogManager.getLogger("com.ezee.insurence.dao");

	public int customerUID(AuthDTO authDTO, CustomerDTO customerDTO) {
		int affected = 0;
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			String query = "{call sp_customer_uid(?,?,?,?,?,?,?,?,?,?,?)}";

			@Cleanup
			CallableStatement statement = connection.prepareCall(query);
			statement.setString(1, customerDTO.getCode());
			statement.setString(2, customerDTO.getName());
			statement.setString(3, customerDTO.getCustomerDOB());
			statement.setString(4, customerDTO.getCustomerGender().toUpperCase());
			statement.setString(5, customerDTO.getCustomerAddress());
			statement.setString(6, customerDTO.getCustomerNumber());
			statement.setString(7, customerDTO.getCustomerEmail());
			statement.setString(8, customerDTO.getCustomerLicenseNum());
			statement.setString(9, authDTO.getEmlployeeDTO().getCode());
			statement.setInt(10, customerDTO.getActiveFlag());
			statement.registerOutParameter(11, Types.INTEGER);
			statement.execute();
			affected = statement.getInt(11);
			logger.info("Inserted/Updated customer record with code: {}", customerDTO.getCode());

		} catch (Exception e) {
			logger.error("Error in customer record processing: {}", e.getMessage());
			throw new ServiceException(e.getMessage());
		}

		return affected;
	}

	public CustomerDTO getCustomerByCode(CustomerDTO customerDTO) {
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			PreparedStatement statement = null;

			if (customerDTO.getCode() != null) {
				String query = "select id, code, customer_name, customer_dob, customer_gender, customer_address, customer_number, customer_email, customer_licensenum , active_flag from customer where code = ? and active_flag = 1";
				statement = connection.prepareStatement(query);
				statement.setString(1, customerDTO.getCode());
			} else if (customerDTO.getId() != 0) {
				String query = "select id, code, customer_name, customer_dob, customer_gender, customer_address, customer_number, customer_email, customer_licensenum , active_flag from customer where id = ? and active_flag = 1";
				statement = connection.prepareStatement(query);
				statement.setInt(1, customerDTO.getId());
			}

			@Cleanup
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				customerDTO.setId(rs.getInt("id"));
				customerDTO.setCode(rs.getString("code"));
				customerDTO.setName(rs.getString("customer_name"));
				customerDTO.setCustomerDOB(rs.getString("customer_dob"));
				customerDTO.setCustomerGender(rs.getString("customer_gender"));
				customerDTO.setCustomerAddress(rs.getString("customer_address"));
				customerDTO.setCustomerNumber(rs.getString("customer_number"));
				customerDTO.setCustomerEmail(rs.getString("customer_email"));
				customerDTO.setCustomerLicenseNum(rs.getString("customer_licensenum"));
				customerDTO.setActiveFlag(rs.getInt("active_flag"));
			}

			logger.info("Fetched customer by code: {}", customerDTO.getCode());

		} catch (Exception e) {
			logger.error("Error fetching customer: {}", e.getMessage());
			throw new ServiceException(e.getMessage());
		}

		return customerDTO;
	}

	public List<CustomerDTO> getAllCustomer() {
		List<CustomerDTO> list = new ArrayList<>();
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "select id, code, customer_name, customer_dob, customer_gender, customer_address, customer_number, customer_email, customer_licensenum , active_flag from customer where active_flag = 1";
			PreparedStatement statement = connection.prepareStatement(query);

			@Cleanup
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				CustomerDTO dto = new CustomerDTO();
				dto.setId(rs.getInt("id"));
				dto.setCode(rs.getString("code"));
				dto.setName(rs.getString("customer_name"));
				dto.setCustomerDOB(rs.getString("customer_dob"));
				dto.setCustomerGender(rs.getString("customer_gender"));
				dto.setCustomerAddress(rs.getString("customer_address"));
				dto.setCustomerNumber(rs.getString("customer_number"));
				dto.setCustomerEmail(rs.getString("customer_email"));
				dto.setCustomerLicenseNum(rs.getString("customer_licensenum"));
				dto.setActiveFlag(rs.getInt("active_flag"));
				list.add(dto);
			}

			logger.info("Fetched all active customers.");

		} catch (Exception e) {
			logger.error("Error fetching all customers: {}", e.getMessage());
			throw new ServiceException(e.getMessage());
		}

		return list;
	}

	public CustomerDTO getVehicleByCustomer(CustomerDTO customerDTO) {
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query1 = "select id ,code , vehicle_plate_num, vehicle_type, vehicle_engin_num, vehicle_chasis_num, vehicle_number, vehicle_model_num, active_flag from vehicle where customer_id = ? and active_flag = 1";
			@Cleanup
			PreparedStatement vehiclePS = connection.prepareStatement(query1);

			String query2 = "select id, code, customer_name, customer_dob, customer_gender, customer_address, customer_number, customer_email, customer_licensenum, active_flag from customer where code = ? and active_flag = 1";
			@Cleanup
			PreparedStatement customerPS = connection.prepareStatement(query2);

			customerPS.setString(1, customerDTO.getCode());

			@Cleanup
			ResultSet customerRS = customerPS.executeQuery();

			if (customerRS.next()) {
				customerDTO.setId(customerRS.getInt("id"));
				customerDTO.setCode(customerRS.getString("code"));
				customerDTO.setName(customerRS.getString("customer_name"));
				customerDTO.setCustomerDOB(customerRS.getString("customer_dob"));
				customerDTO.setCustomerGender(customerRS.getString("customer_gender"));
				customerDTO.setCustomerAddress(customerRS.getString("customer_address"));
				customerDTO.setCustomerNumber(customerRS.getString("customer_number"));
				customerDTO.setCustomerEmail(customerRS.getString("customer_email"));
				customerDTO.setCustomerLicenseNum(customerRS.getString("customer_licensenum"));
				customerDTO.setActiveFlag(customerRS.getInt("active_flag"));
			}

			vehiclePS.setInt(1, customerDTO.getId());
			@Cleanup
			ResultSet vehicleRS = vehiclePS.executeQuery();

			List<VehicleDTO> vehicleList = new ArrayList<>();
			while (vehicleRS.next()) {
				VehicleDTO vehicleDTO = new VehicleDTO();
				vehicleDTO.setId(vehicleRS.getInt("id"));
				vehicleDTO.setCode(vehicleRS.getString("code"));
				vehicleDTO.setVehiclePlateNum(vehicleRS.getString("vehicle_plate_num"));
				vehicleDTO.setVehicleType(vehicleRS.getString("vehicle_type"));
				vehicleDTO.setVehicleEnginNum(vehicleRS.getString("vehicle_engin_num"));
				vehicleDTO.setVehicleChasisNum(vehicleRS.getString("vehicle_chasis_num"));
				vehicleDTO.setVehicleNumber(vehicleRS.getString("vehicle_number"));
				vehicleDTO.setVehicleModelNum(vehicleRS.getString("vehicle_model_num"));
				vehicleDTO.setActiveFlag(vehicleRS.getInt("active_flag"));
				vehicleList.add(vehicleDTO);
			}

			customerDTO.setVehicleDTO(vehicleList);

			logger.info("Fetched customer and vehicle details for customer code {}", customerDTO.getCode());

		} catch (Exception e) {
			logger.error("Error fetching customer with vehicles for code: {}", e.getMessage());
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
		return customerDTO;
	}

	public CustomerDTO getPolicyByCustomer(CustomerDTO customerDTO) {
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query1 = "select id, code,vehicle_id, start_date, expiry_date, premium_amount, payment_schedule, total_policy_amount, policy_status, policy_description, active_flag from policy where customer_id = ? and active_flag = 1";
			@Cleanup
			PreparedStatement policyPS = connection.prepareStatement(query1);

			String query = "select id, code, customer_name, customer_dob, customer_gender, customer_address, customer_number, customer_email, customer_licensenum, active_flag from customer where code = ? and active_flag = 1";
			@Cleanup
			PreparedStatement customerPS = connection.prepareStatement(query);
			customerPS.setString(1, customerDTO.getCode());

			@Cleanup
			ResultSet customerRS = customerPS.executeQuery();

			if (customerRS.next()) {
				customerDTO.setId(customerRS.getInt("id"));
				customerDTO.setCode(customerRS.getString("code"));
				customerDTO.setName(customerRS.getString("customer_name"));
				customerDTO.setCustomerDOB(customerRS.getString("customer_dob"));
				customerDTO.setCustomerGender(customerRS.getString("customer_gender"));
				customerDTO.setCustomerAddress(customerRS.getString("customer_address"));
				customerDTO.setCustomerNumber(customerRS.getString("customer_number"));
				customerDTO.setCustomerEmail(customerRS.getString("customer_email"));
				customerDTO.setCustomerLicenseNum(customerRS.getString("customer_licensenum"));
				customerDTO.setActiveFlag(customerRS.getInt("active_flag"));
			}

			policyPS.setInt(1, customerDTO.getId());

			@Cleanup
			ResultSet policyRS = policyPS.executeQuery();

			List<PolicyDTO> policyList = new ArrayList<>();
			while (policyRS.next()) {
				PolicyDTO policyDTO = new PolicyDTO();
				policyDTO.setId(policyRS.getInt("id"));
				policyDTO.setCode(policyRS.getString("code"));

				VehicleDTO vehicleDTO = new VehicleDTO();
				vehicleDTO.setId(policyRS.getInt("vehicle_id"));
				policyDTO.setVehicleDTO(vehicleDTO);

				policyDTO.setStartDate(policyRS.getString("start_date"));
				policyDTO.setExpriyDate(policyRS.getString("expiry_date"));
				policyDTO.setPremiumAmount(policyRS.getBigDecimal("premium_amount"));
				policyDTO.setPaymentSchedule(policyRS.getString("payment_schedule"));
				policyDTO.setTotalAmount(policyRS.getBigDecimal("total_policy_amount"));
				policyDTO.setPolicyStatus(policyRS.getString("policy_status"));
				policyDTO.setPolicyDescription(policyRS.getString("policy_description"));
				policyDTO.setActiveFlag(policyRS.getInt("active_flag"));
				policyList.add(policyDTO);
			}
			customerDTO.setPolicyDTO(policyList);

			logger.info("Fetched policies for customer code {}", customerDTO.getCode());

		} catch (Exception e) {
			logger.error("Error fetching policies for customer code: {}", e.getMessage());
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
		return customerDTO;
	}

	public CustomerDTO getIncidentByCustomer(CustomerDTO customerDTO) {
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query2 = "select id, code, incident_type, incident_inspector, incident_cost, incident_description, incident_status, active_flag from incident where customer_id = ? and active_flag = 1";
			@Cleanup
			PreparedStatement incidentPS = connection.prepareStatement(query2);

			String query1 = "select id, code, customer_name, customer_dob, customer_gender, customer_address,customer_number, customer_email, customer_licensenum, active_flag from customer where code = ? and active_flag = 1";
			@Cleanup
			PreparedStatement customerPS = connection.prepareStatement(query1);
			customerPS.setString(1, customerDTO.getCode());

			@Cleanup
			ResultSet customerRS = customerPS.executeQuery();

			if (customerRS.next()) {
				customerDTO.setId(customerRS.getInt("id"));
				customerDTO.setCode(customerRS.getString("code"));
				customerDTO.setName(customerRS.getString("customer_name"));
				customerDTO.setCustomerDOB(customerRS.getString("customer_dob"));
				customerDTO.setCustomerGender(customerRS.getString("customer_gender"));
				customerDTO.setCustomerAddress(customerRS.getString("customer_address"));
				customerDTO.setCustomerNumber(customerRS.getString("customer_number"));
				customerDTO.setCustomerEmail(customerRS.getString("customer_email"));
				customerDTO.setCustomerLicenseNum(customerRS.getString("customer_licensenum"));
				customerDTO.setActiveFlag(customerRS.getInt("active_flag"));
			}

			incidentPS.setInt(1, customerDTO.getId());

			@Cleanup
			ResultSet incidentRS = incidentPS.executeQuery();
			List<IncidentDTO> incidentList = new ArrayList<>();

			while (incidentRS.next()) {
				IncidentDTO incidentDTO = new IncidentDTO();
				incidentDTO.setId(incidentRS.getInt("id"));
				incidentDTO.setCode(incidentRS.getString("code"));
				incidentDTO.setIncidentType(incidentRS.getString("incident_type"));
				incidentDTO.setIncidentInspector(incidentRS.getString("incident_inspector"));
				incidentDTO.setIncidentCost(incidentRS.getBigDecimal("incident_cost"));
				incidentDTO.setIncidentDescription(incidentRS.getString("incident_description"));
				incidentDTO.setIncidentStatus(incidentRS.getString("incident_status"));
				incidentDTO.setActiveFlag(incidentRS.getInt("active_flag"));
				incidentList.add(incidentDTO);
			}

			customerDTO.setIncidentDTO(incidentList);

			logger.info("Fetched incident details for customer code {}", customerDTO.getCode());

		} catch (Exception e) {
			logger.error("Error fetching incidents for customer code: {}", e.getMessage());
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}

		return customerDTO;
	}

	public CustomerDTO getPaymentByCustomer(CustomerDTO customerDTO) {
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "select id, code, recipt_id, customer_id, payment_date, payment_amount, payment_mode, active_flag from payment where customer_id = ? and active_flag = 1 ";
			@Cleanup
			PreparedStatement paymentPS = connection.prepareStatement(query);

			String query1 = "select id, code, customer_name, customer_dob, customer_gender, customer_address,customer_number, customer_email, customer_licensenum, active_flag from customer where code = ? and active_flag = 1";
			@Cleanup
			PreparedStatement customerPS = connection.prepareStatement(query1);
			customerPS.setString(1, customerDTO.getCode());

			@Cleanup
			ResultSet customerRS = customerPS.executeQuery();

			if (customerRS.next()) {
				customerDTO.setId(customerRS.getInt("id"));
				customerDTO.setCode(customerRS.getString("code"));
				customerDTO.setName(customerRS.getString("customer_name"));
				customerDTO.setCustomerDOB(customerRS.getString("customer_dob"));
				customerDTO.setCustomerGender(customerRS.getString("customer_gender"));
				customerDTO.setCustomerAddress(customerRS.getString("customer_address"));
				customerDTO.setCustomerNumber(customerRS.getString("customer_number"));
				customerDTO.setCustomerEmail(customerRS.getString("customer_email"));
				customerDTO.setCustomerLicenseNum(customerRS.getString("customer_licensenum"));
				customerDTO.setActiveFlag(customerRS.getInt("active_flag"));
			}

			paymentPS.setInt(1, customerDTO.getId());
			ResultSet paymentRS = paymentPS.executeQuery();
			List<PaymentDTO> paymentList = new ArrayList<PaymentDTO>();
			while (paymentRS.next()) {
				PaymentDTO paymentDTO = new PaymentDTO();
				paymentDTO.setId(paymentRS.getInt("id"));
				paymentDTO.setCode(paymentRS.getString("code"));

				ReciptDTO reciptDTO = new ReciptDTO();
				reciptDTO.setId(paymentRS.getInt("recipt_id"));
				paymentDTO.setReciptDTO(reciptDTO);

				paymentDTO.setPaymentDate(paymentRS.getDate("payment_date").toString());
				paymentDTO.setPaymentAmount(paymentRS.getBigDecimal("payment_amount"));
				paymentDTO.setPaymentMode(paymentRS.getString("payment_mode"));
				paymentDTO.setActiveFlag(paymentRS.getInt("active_flag"));
				paymentList.add(paymentDTO);
			}
			customerDTO.setPaymentDTO(paymentList);
			logger.info("Fetch the payment by the customer code: {} ", customerDTO.getCode());
		} catch (Exception e) {
			logger.error("Error fetching payment for customer code: {}", e.getMessage());
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
		return customerDTO;
	}

	public CustomerDTO getReciptByCustomer(CustomerDTO customerDTO) {
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "select id, code, policy_id, renewal_id, recipt_amount, penalty_amount, recipt_date, due_date, recipt_total_amount, recipt_status, update_by, update_at, active_flag from recipt where customer_id = ? and active_flag = 1";
			@Cleanup
			PreparedStatement reciptPS = connection.prepareStatement(query);

			String query1 = "select id, code, customer_name, customer_dob, customer_gender, customer_address,customer_number, customer_email, customer_licensenum, active_flag from customer where code = ? and active_flag = 1";
			@Cleanup
			PreparedStatement customerPS = connection.prepareStatement(query1);
			customerPS.setString(1, customerDTO.getCode());

			@Cleanup
			ResultSet customerRS = customerPS.executeQuery();

			if (customerRS.next()) {
				customerDTO.setId(customerRS.getInt("id"));
				customerDTO.setCode(customerRS.getString("code"));
				customerDTO.setName(customerRS.getString("customer_name"));
				customerDTO.setCustomerDOB(customerRS.getString("customer_dob"));
				customerDTO.setCustomerGender(customerRS.getString("customer_gender"));
				customerDTO.setCustomerAddress(customerRS.getString("customer_address"));
				customerDTO.setCustomerNumber(customerRS.getString("customer_number"));
				customerDTO.setCustomerEmail(customerRS.getString("customer_email"));
				customerDTO.setCustomerLicenseNum(customerRS.getString("customer_licensenum"));
				customerDTO.setActiveFlag(customerRS.getInt("active_flag"));
			}

			reciptPS.setInt(1, customerDTO.getId());
			ResultSet reciptRS = reciptPS.executeQuery();
			List<ReciptDTO> reciptList = new ArrayList<>();
			while (reciptRS.next()) {
				ReciptDTO dto = new ReciptDTO();
				dto.setId(reciptRS.getInt("id"));
				dto.setCode(reciptRS.getString("code"));

				if (reciptRS.getInt("policy_id") != 0) {
					PolicyDTO policy = new PolicyDTO();
					policy.setId(reciptRS.getInt("policy_id"));
					dto.setPolicyDTO(policy);
				}
				if (reciptRS.getInt("renewal_id") != 0) {
					RenewalDTO renewal = new RenewalDTO();
					renewal.setId(reciptRS.getInt("renewal_id"));
					dto.setRenewalDTO(renewal);
				}

				dto.setReciptAmount(reciptRS.getBigDecimal("recipt_amount"));
				dto.setPenaltyAmount(reciptRS.getBigDecimal("penalty_amount"));
				dto.setReciptDate(reciptRS.getString("recipt_date"));
				dto.setDueDate(reciptRS.getString("due_date"));
				dto.setReciptTotalAmount(reciptRS.getBigDecimal("recipt_total_amount"));
				dto.setReciptStatus(reciptRS.getString("recipt_status"));
				dto.setActiveFlag(reciptRS.getInt("active_flag"));

				reciptList.add(dto);
			}
			customerDTO.setReciptDTO(reciptList);
			logger.info("Fetch the recipt by the customer code: {} ", customerDTO.getCode());
		} catch (Exception e) {
			logger.error("Error fetching recipt for customer code: {}", e.getMessage());
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
		return customerDTO;
	}

	public CustomerDTO getClaimByCustomer(CustomerDTO customerDTO) {
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "select id, code, policy_id, incident_id, claim_type, claim_date, claim_description, claim_amount, claim_status, active_flag from claim where customer_id = ? and active_flag = 1";
			@Cleanup
			PreparedStatement claimPS = connection.prepareStatement(query);

			String query1 = "select id, code, customer_name, customer_dob, customer_gender, customer_address, customer_number, customer_email, customer_licensenum, active_flag from customer where code = ? and active_flag = 1";
			@Cleanup
			PreparedStatement customerPS = connection.prepareStatement(query1);
			customerPS.setString(1, customerDTO.getCode());

			@Cleanup
			ResultSet customerRS = customerPS.executeQuery();

			if (customerRS.next()) {
				customerDTO.setId(customerRS.getInt("id"));
				customerDTO.setCode(customerRS.getString("code"));
				customerDTO.setName(customerRS.getString("customer_name"));
				customerDTO.setCustomerDOB(customerRS.getString("customer_dob"));
				customerDTO.setCustomerGender(customerRS.getString("customer_gender"));
				customerDTO.setCustomerAddress(customerRS.getString("customer_address"));
				customerDTO.setCustomerNumber(customerRS.getString("customer_number"));
				customerDTO.setCustomerEmail(customerRS.getString("customer_email"));
				customerDTO.setCustomerLicenseNum(customerRS.getString("customer_licensenum"));
				customerDTO.setActiveFlag(customerRS.getInt("active_flag"));
			}

			claimPS.setInt(1, customerDTO.getId());
			@Cleanup
			ResultSet claimRS = claimPS.executeQuery();

			List<ClaimDTO> claimList = new ArrayList<>();
			while (claimRS.next()) {
				ClaimDTO claimDTO = new ClaimDTO();
				claimDTO.setId(claimRS.getInt("id"));
				claimDTO.setCode(claimRS.getString("code"));

				PolicyDTO policy = new PolicyDTO();
				policy.setId(claimRS.getInt("policy_id"));
				claimDTO.setPolicyDTO(policy);

				IncidentDTO incident = new IncidentDTO();
				incident.setId(claimRS.getInt("incident_id"));
				claimDTO.setIncidentDTO(incident);

				claimDTO.setClaimType(claimRS.getString("claim_type"));
				claimDTO.setClaimDate(claimRS.getDate("claim_date").toString());
				claimDTO.setClaimDescription(claimRS.getString("claim_description"));
				claimDTO.setClaimAmount(claimRS.getBigDecimal("claim_amount"));
				claimDTO.setClaimStatus(claimRS.getString("claim_status"));
				claimDTO.setActiveFlag(claimRS.getInt("active_flag"));
				claimList.add(claimDTO);
			}
			customerDTO.setClaimDTO(claimList);
			logger.info("Fetch the Claim by the customer code: {} ", customerDTO.getCode());
		} catch (Exception e) {
			logger.error("Error fetching claim for customer  {}", e.getMessage());
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
		return customerDTO;
	}

	public CustomerDTO getActivePolicyByCustomer(CustomerDTO customerDTO, PolicyDTO policyDTO) {
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String policyQuery = "select id, code, vehicle_id, policy_number, start_date, expiry_date, premium_amount, payment_schedule, total_policy_amount, policy_status, policy_description, active_flag from policy where customer_id = ? and policy_status = ? and active_flag = 1";

			String query1 = "select id, code, customer_name, customer_dob, customer_gender, customer_address, customer_number, customer_email, customer_licensenum, active_flag from customer where code = ? and active_flag = 1";
			@Cleanup
			PreparedStatement customerPS = connection.prepareStatement(query1);
			customerPS.setString(1, customerDTO.getCode());

			@Cleanup
			ResultSet customerRS = customerPS.executeQuery();

			if (customerRS.next()) {
				customerDTO.setId(customerRS.getInt("id"));
				customerDTO.setCode(customerRS.getString("code"));
				customerDTO.setName(customerRS.getString("customer_name"));
				customerDTO.setCustomerDOB(customerRS.getString("customer_dob"));
				customerDTO.setCustomerGender(customerRS.getString("customer_gender"));
				customerDTO.setCustomerAddress(customerRS.getString("customer_address"));
				customerDTO.setCustomerNumber(customerRS.getString("customer_number"));
				customerDTO.setCustomerEmail(customerRS.getString("customer_email"));
				customerDTO.setCustomerLicenseNum(customerRS.getString("customer_licensenum"));
				customerDTO.setActiveFlag(customerRS.getInt("active_flag"));
			}

			@Cleanup
			PreparedStatement policyPS = connection.prepareStatement(policyQuery);

			policyPS.setInt(1, customerDTO.getId());
			policyPS.setString(2, policyDTO.getPolicyStatus());
			@Cleanup
			ResultSet policyRS = policyPS.executeQuery();
			List<PolicyDTO> policyList = new ArrayList<>();
			while (policyRS.next()) {
				PolicyDTO dto = new PolicyDTO();
				dto.setId(policyRS.getInt("id"));
				dto.setCode(policyRS.getString("code"));

				VehicleDTO vehicleDTO = new VehicleDTO();
				vehicleDTO.setId(policyRS.getInt("vehicle_id"));
				dto.setVehicleDTO(vehicleDTO);

				dto.setPolicyNumber(policyRS.getString("policy_number"));
				dto.setStartDate(policyRS.getString("start_date"));
				dto.setExpriyDate(policyRS.getString("expiry_date"));
				dto.setPremiumAmount(policyRS.getBigDecimal("premium_amount"));
				dto.setPaymentSchedule(policyRS.getString("payment_schedule"));
				dto.setTotalAmount(policyRS.getBigDecimal("total_policy_amount"));
				dto.setPolicyStatus(policyRS.getString("policy_status"));
				dto.setPolicyDescription(policyRS.getString("policy_description"));
				dto.setActiveFlag(policyRS.getInt("active_flag"));
				policyList.add(dto);
			}
			customerDTO.setPolicyDTO(policyList);
			logger.info("Fetch the policy by the customer code: {} ", customerDTO.getCode());
		} catch (Exception e) {
			logger.error("Error fetching policy for customerCode {}", e.getMessage());
			throw new ServiceException(e.getMessage());
		}

		return customerDTO;
	}

}
