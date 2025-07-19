package com.ezee.insurence.exception;


public enum ErrorCode {
	USER_CODE_NOT_CREATED("101", "User Code is not created"), 
	USER_INVALID_AUTH_TOKEN("102", "Invalid Auth Token"),
	USERNAME_SHOULD_NOT_NULL("103", "Username Should not be null.."), 
	USER_INVALID_PASSWORD("104", "Invalid Password"),
	USER_INVALID_EMAIL("105", "Email ID not exists"), 
	USER_INVALID_MOBILE("106", "Mobile Number not exists"),
	USER_INVALID_SERVICE_NUMBER("107", "Service number not exists"),
	USER_NOT_FOUND("108","User is not found"),
	USER_NOT_CREATED("109","User is not created"),
	CUSTOMER_NOT_FOUND("109","Customer is not found"),
	CUSTOMER_NOT_CREATED("110","Customer is not created"),
	VEHICLE_NOT_FOUND("111","Vehicle is not found"),
	VEHICLE_NOT_CREATED("112","Vehicle is not created"),
	POLICY_NOT_FOUND("113","policy is not found"),
	POLICY_NOT_CREATED("114","policy is not created"),
	POLICY_NUMBER_NOT_FOUND("115","Policy number is not found"),
	RENEWAL_NOT_FOUND("116","Renewal is not found"),
	RENEWAL_NOT_CREATE("117","Renewal is not created"),
	CLAIM_NOT_FOUND("118","Claim is not found"),
	ClAIM_NOT_CREATE("119","Claim is not created"),
	INCIDENT_NOT_FOUND("120","Incident is not found"),
	INCIDENT_NOT_CREATED("121","Incident is not created"),
	PAYMENT_NOT_FOUND("122","payment is not found"),
	PAYMENT_NOT_CREATE("123","payment is not done"),
	RECIPT_NOT_FOUND("124","Recipt is not found"),
	RECIPT_NOT_CREATE("125","Recipt is not created"),
	SERVICE_NOT_CREATED("126","VehicleService not created"),
	SERVICE_NOT_FOUND("127","VehicleService not found"),
	UPDATE_FAIL("134", "Update fail"),
	UNAUTHORIZED("135", "Unauthorized access"),
	UNDEFINE_EXCEPTION("500", "Un Known Exception"),
	INVALID_CREDENTIALS("PG15", "Invalid credentials"),
	DATE_FORMATE("D11", "Enter date in the correct formate"),
	DATE_TIME_FORMATE("D12", "Enter date time in the correct formate");

	private final String code;
	private final String message;

	private ErrorCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public String getCode() {
		return code;
	}

	public Integer getIntCode() {
		return Integer.valueOf(code);
	}

	public String toString() {
		return code + ": " + message;
	}

	public static ErrorCode getErrorCode(String value) {
		ErrorCode[] values = values();
		for (ErrorCode errorCode : values) {
			if (errorCode.getCode().equalsIgnoreCase(value)) {
				return errorCode;
			}
		}
		return UNDEFINE_EXCEPTION;
	}
}
