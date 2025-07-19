DROP PROCEDURE IF EXISTS SP_POLICY_UID;
DELIMITER $$

CREATE PROCEDURE SP_POLICY_UID(IN pcrCode VARCHAR(20), IN pcrCustomerCode VARCHAR(20), IN pcrVehicleCode VARCHAR(20),IN pcrPolicyNumber VARCHAR(20), IN pcrStartDate VARCHAR(20), IN pcrExpiryDate VARCHAR(20), IN pdcPremiumAmount DECIMAL(10, 2), IN pcrPaymentSchedule VARCHAR(20), IN pdcTotalAmount DECIMAL(10, 2), IN pcrPolicyStatus VARCHAR(20), IN ptxPolicyDescription TEXT, IN pcrUpdateBy VARCHAR(20),IN pitActiveFlag TINYINT, OUT pitRowCount INT)
/*
* Procedure Name		:	SP_POLICY_UID
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
* Tables           : policy
* Functions        : EB_FN_ISNOTNULL,NOW
*
* Procedures       : SP_POLICY_UID
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
    DECLARE litPolicyId INT DEFAULT 0;
    DECLARE litCustomerId INT DEFAULT 0;
    DECLARE litVehicleId INT DEFAULT 0;
    DECLARE litCount INT DEFAULT 0;

/*
*-----------------------------------------------------------------------------------------------------
*  Variable Initialized
*------------------------------------------------------------------------------------------------------
*/
    SET pitRowCount = 0;
    SELECT id INTO litPolicyId FROM policy WHERE CODE = pcrCode;
    SELECT id INTO litCustomerId FROM customer WHERE CODE = pcrCustomerCode;
    SELECT id INTO litVehicleId FROM vehicle WHERE CODE = pcrVehicleCode;
    SELECT COUNT(vehicle_id) INTO litCount FROM  policy WHERE vehicle_id = litVehicleId ;
/*
*-----------------------------------------------------------------------------------------------------
*  Update/Insert Employee
*------------------------------------------------------------------------------------------------------
*/
    IF (pitActiveFlag = 1 AND litPolicyId != 0) THEN
        UPDATE policy SET customer_id = litCustomerId, vehicle_id = litVehicleId, start_date = pcrStartDate,expiry_date = pcrExpiryDate, premium_amount = pdcPremiumAmount, payment_schedule = pcrPaymentSchedule, total_policy_amount = pdcTotalAmount, policy_status = pcrPolicyStatus, policy_description = ptxPolicyDescription, update_by = pcrUpdateBy, update_at = NOW(), active_flag = pitActiveFlag WHERE CODE = pcrCode;
        SELECT ROW_COUNT() INTO pitRowCount;

    ELSEIF (pitActiveFlag = 1 AND litPolicyId = 0 AND litCount = 0 ) THEN
        INSERT INTO policy ( CODE, customer_id ,vehicle_id , policy_number, start_date, expiry_date, premium_amount, payment_schedule, total_policy_amount, policy_status, policy_description ,update_by, update_at , active_flag) 
        VALUES ( pcrCode, litCustomerId, litVehicleId, pcrPolicyNumber, pcrStartDate, pcrExpiryDate, pdcPremiumAmount, pcrPaymentSchedule, pdcTotalAmount, pcrPolicyStatus, ptxPolicyDescription, pcrUpdateBy, NOW(), 1);
        SELECT ROW_COUNT() INTO pitRowCount;
    ELSEIF (pitActiveFlag != 1 AND litPolicyId != 0) THEN
        UPDATE policy SET active_flag = pitActiveFlag, update_by = pcrUpdateBy, update_at = NOW() WHERE CODE = pcrCode;
        SELECT ROW_COUNT() INTO pitRowCount;
    END IF;
END $$

DELIMITER ;
