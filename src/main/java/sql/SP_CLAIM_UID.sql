DROP PROCEDURE IF EXISTS SP_CLAIM_UID;
DELIMITER $$

CREATE PROCEDURE SP_CLAIM_UID(IN pcrCode VARCHAR(20), IN pcrPolicyCode VARCHAR(20), IN pcrCustomerCode VARCHAR(20), IN pcrIncidentCode VARCHAR(20), IN pcrClaimType VARCHAR(20), IN pcrClaimDate VARCHAR(20), IN ptxClaimDescription TEXT, IN pdcClaimAmount DECIMAL(10, 2), IN pcrClaimStatus VARCHAR(20), IN pcrUpdateBy VARCHAR(20), IN pitActiveFlag TINYINT, OUT pitRowCount INT)
/*
* Procedure Name		:	SP_CLAIM_UID
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
* Tables           :  claim
* Functions        : EB_FN_ISNOTNULL,NOW
*
* Procedures       : SP_CLAIM_UID
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
    DECLARE litClaimId INT DEFAULT 0;
    DECLARE litPolicyId INT DEFAULT 0;
    DECLARE litCustomerId INT DEFAULT 0;
    DECLARE litIncidentId INT DEFAULT 0;
/*
*-----------------------------------------------------------------------------------------------------
*  Variable Initialized
*------------------------------------------------------------------------------------------------------
*/
    SET pitRowCount = 0;

    SELECT id INTO litClaimId FROM claim WHERE CODE = pcrCode;
    SELECT id INTO litPolicyId FROM policy WHERE CODE = pcrPolicyCode;
    SELECT id INTO litCustomerId FROM customer WHERE CODE = pcrCustomerCode;
    SELECT id INTO litIncidentId FROM incident WHERE CODE = pcrIncidentCode;
/*
*-----------------------------------------------------------------------------------------------------
*  Update/Insert Employee
*------------------------------------------------------------------------------------------------------
*/

    IF (pitActiveFlag = 1 AND litClaimId != 0) THEN
        UPDATE claim SET claim_type = pcrClaimType, claim_date = pcrClaimDate, claim_description = ptxClaimDescription, claim_amount = pdcClaimAmount, claim_status = pcrClaimStatus, update_by = pcrUpdateBy, update_at = NOW(), active_flag = pitActiveFlag
        WHERE CODE = pcrCode;
        SELECT ROW_COUNT() INTO pitRowCount;
        UPDATE incident SET incident_status = pcrClaimStatus  WHERE id = litIncidentId;

    ELSEIF (pitActiveFlag = 1 AND litClaimId = 0) THEN
        INSERT INTO claim (CODE, policy_id, customer_id, incident_id,claim_type, claim_date, claim_description, claim_amount, claim_status, update_by, update_at, active_flag)
        VALUES (pcrCode, litPolicyId, litCustomerId , litIncidentId, pcrClaimType, pcrClaimDate, ptxClaimDescription, pdcClaimAmount, pcrClaimStatus, pcrUpdateBy, NOW(), pitActiveFlag );
        SELECT ROW_COUNT() INTO pitRowCount;

        UPDATE incident SET incident_status = pcrClaimStatus  WHERE id = litIncidentId;
         
    ELSEIF (pitActiveFlag != 1 AND litClaimId != 0) THEN
        UPDATE claim SET active_flag = pitActiveFlag, update_by = pcrUpdateBy, update_at = NOW() WHERE CODE = pcrCode;
        SELECT ROW_COUNT() INTO pitRowCount;
 
    END IF;
END $$

DELIMITER ;
