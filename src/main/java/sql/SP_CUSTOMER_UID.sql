DROP PROCEDURE IF EXISTS SP_CUSTOMER_UID;
DELIMITER $$

CREATE PROCEDURE SP_CUSTOMER_UID( IN pcrCode VARCHAR(20), IN pcrName VARCHAR(50), IN pcrCustomerDOB VARCHAR(20), IN pcrCustomerGender VARCHAR(10), IN ptxCustomerAddress TEXT, IN pcrCustomerNumber VARCHAR(15), IN pcrCustomerEmail VARCHAR(50), IN pcrCustomerLicenseNum VARCHAR(50),  IN pcrUpdateBy VARCHAR(20), IN pitActiveFlag TINYINT, OUT pitRowCount INT)
/*
* Procedure Name		:	SP_CUSTOMER_UID
* 
* Purpose			:	Insert/ Update/ Delete complaint table
* 
* Input				:	None
* 
* Output			:	Code 
*   					    Affected Rows
* 
* Returns           		:	None
* 
* Dependencies
*
* Tables           : customer
*
* Functions        : EB_FN_ISNOTNULL,NOW
*
* Procedures       : SP_COSTOMER_IUD
* 
* Revision History:
*
*	1.0 - 2016/03/18	Ezee Info 
*	Java              	Original Code
*
*/
BEGIN

/*
*----------------------------------------------------------------------------------------------------
* Variable Declare
*-----------------------------------------------------------------------------------------------------
*/
    DECLARE litCustomerId INT DEFAULT 0;
    DECLARE lcrCustomerLicense VARCHAR(20) DEFAULT NULL;
    SET pitRowCount = 0;
    
/*
*-----------------------------------------------------------------------------------------------------
*  Variable Initialized
*------------------------------------------------------------------------------------------------------
*/
    SELECT id INTO litCustomerId  FROM customer WHERE CODE = pcrCode;
    SELECT customer_licensenum INTO lcrCustomerLicense FROM customer WHERE customer_licensenum = pcrCustomerLicenseNum;
    
/*
*-----------------------------------------------------------------------------------------------------
*  Update/Insert Employee
*------------------------------------------------------------------------------------------------------
*/
    IF (pitActiveFlag = 1 AND litCustomerId != 0 ) THEN
        UPDATE customer SET customer_Name = pcrName, customer_dob = pcrCustomerDOB, customer_gender = pcrCustomerGender, customer_address = ptxCustomerAddress, customer_number = pcrCustomerNumber, customer_email = pcrCustomerEmail, customer_licensenum = pcrCustomerLicenseNum, update_by = pcrUpdateBy, update_at = NOW(), active_flag = pitActiveFlag
        WHERE CODE = pcrCode;
        SELECT ROW_COUNT() INTO pitRowCount;
    ELSEIF (pitActiveFlag = 1 AND litCustomerId = 0 AND lcrCustomerLicense IS NULL ) THEN
        INSERT INTO customer ( CODE, customer_Name, customer_dob, customer_gender, customer_address, customer_number, customer_email, customer_licensenum, update_by, update_at, active_flag )
        VALUES (pcrCode, pcrName, pcrCustomerDOB, pcrCustomerGender, ptxCustomerAddress, pcrCustomerNumber, pcrCustomerEmail, pcrCustomerLicenseNum, pcrUpdateBy, NOW(), 1 );
        SELECT ROW_COUNT() INTO pitRowCount;
    ELSEIF (pitActiveFlag != 1 AND litCustomerId != 0 ) THEN
        UPDATE customer
        SET active_flag = pitActiveFlag, update_by = pcrUpdateBy, update_at = NOW() WHERE CODE = pcrCode;
        SELECT ROW_COUNT() INTO pitRowCount;
    END IF;
    
END $$

DELIMITER ;