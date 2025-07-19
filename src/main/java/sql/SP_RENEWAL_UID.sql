DROP PROCEDURE IF EXISTS SP_RENEWAL_UID;
DELIMITER $$

CREATE PROCEDURE SP_RENEWAL_UID( IN pcrCode VARCHAR(20), IN pcrPolicyCode VARCHAR(20), IN pcrRenewalDate VARCHAR(20), IN pcrNewExpiryDate VARCHAR(20), IN pdlRenewalAmount DECIMAL(10,2), IN pcrRenewalStatus VARCHAR(20), IN pcrUpdateBy VARCHAR(20),IN pitActiveFlag TINYINT,OUT pitRowCount INT)
/*
* Procedure Name		:	SP_RENEWAL_UID
* 
* Purpose			:	Insert/ Update/ Delete complaint table
* 
* Input				:	None
* 
* Output			:       Affected Rows
*
* Returns           		:	None
* 
* Dependencies
*
* Tables           :  RENEWAL
* Functions        : EB_FN_ISNOTNULL,NOW
*
* Procedures       :SP_RENEWAL_UID
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
    DECLARE litRenewalId INT DEFAULT 0;
    DECLARE litPolicyId INT DEFAULT 0;
    
/*
*-----------------------------------------------------------------------------------------------------
*  Variable Initialized
*------------------------------------------------------------------------------------------------------
*/

    SET pitRowCount = 0;
    SELECT id INTO litRenewalId FROM renewal WHERE CODE = pcrCode;
    SELECT id INTO litPolicyId FROM policy WHERE CODE = pcrPolicyCode;

/*
*-----------------------------------------------------------------------------------------------------
*  Update/Insert Employee
*------------------------------------------------------------------------------------------------------
*/
    IF (pitActiveFlag = 1 AND litRenewalId != 0) THEN
        UPDATE renewal  SET policy_id = litPolicyId, renewal_date = pcrRenewalDate, newexpriy_date = pcrNewExpiryDate, renewal_amount = pdlRenewalAmount, renewal_status = pcrRenewalStatus, update_by = pcrUpdateBy, update_at = NOW(), active_flag = pitActiveFlag
        WHERE CODE = pcrCode;
        SELECT ROW_COUNT() INTO pitRowCount;
   
        IF(pcrRenewalStatus = 'PAID') THEN 
		UPDATE  policy SET policy_status = 'ACTIVE' ,expiry_date = pcrNewExpiryDate WHERE id = litPolicyId;
	ELSEIF(pcrRenewalStatus = 'UNPAID')THEN
		UPDATE  policy SET policy_status = 'UNACTIVE' WHERE id = litPolicyId;
	END IF;
        
    ELSEIF (pitActiveFlag = 1 AND litRenewalId = 0) THEN
        INSERT INTO renewal ( CODE, policy_id, renewal_date, newexpriy_date, renewal_amount, renewal_status, update_by, update_at, active_flag)
        VALUES ( pcrCode, litPolicyId, pcrRenewalDate, pcrNewExpiryDate, pdlRenewalAmount, pcrRenewalStatus, pcrUpdateBy, NOW(), pitActiveFlag );
        SELECT ROW_COUNT() INTO pitRowCount;
        
        IF(pcrRenewalStatus = 'PAID') THEN 
		UPDATE  policy SET policy_status = 'ACTIVE' ,  expiry_date = pcrNewExpiryDate WHERE id = litPolicyId;
	ELSEIF(pcrRenewalStatus = 'PROCESSING')THEN
		UPDATE  policy SET policy_status = 'ACTIVE' WHERE id = litPolicyId;
	ELSEIF(pcrRenewalStatus = 'UNPAID')THEN
		UPDATE  policy SET policy_status = 'UNACTIVE' WHERE id = litPolicyId;
	END IF;
	
    ELSEIF (pitActiveFlag != 1 AND litRenewalId != 0) THEN
        UPDATE renewal SET active_flag = pitActiveFlag, update_by = pcrUpdateBy, update_at = NOW()
        WHERE CODE = pcrCode;
        SELECT ROW_COUNT() INTO pitRowCount;
        
    END IF;
END $$

DELIMITER ;
