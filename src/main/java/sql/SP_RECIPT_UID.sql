DROP PROCEDURE IF EXISTS SP_RECIPT_UID;
DELIMITER $$

CREATE PROCEDURE SP_RECIPT_UID( IN pcrCode VARCHAR(20),IN pcrPolicyCode VARCHAR(20),IN pcrCustomerCode VARCHAR(20), IN pcrRenewalCode VARCHAR(20), IN pdlReciptAmount DECIMAL(10,2) ,IN pdlPenaltyAmount DECIMAL(10,2), IN pcrReciptDate VARCHAR(20), IN pcrDueDate VARCHAR(20),IN pdlRecpitTotalAmount DECIMAL(10,2) ,IN pcrReciptStatus VARCHAR(10), IN pcrUpdateBy VARCHAR(20), IN pitActiveFlag TINYINT,OUT pitRowCount INT)
/*
* Procedure Name		:	SP_RECIPT_UID
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
* Tables           :  RECIPIT
* Functions        : EB_FN_ISNOTNULL,NOW
*
* Procedures       :SP_RECIPT_UID
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
    DECLARE litReciptId INT DEFAULT 0;
    DECLARE litPolicyId INT DEFAULT 0;
    DECLARE litCustomerId INT DEFAULT 0;
    DECLARE litRenewalId INT DEFAULT 0;
/*
*-----------------------------------------------------------------------------------------------------
*  Variable Initialized
*------------------------------------------------------------------------------------------------------
*/
    SET pitRowCount = 0;

    SELECT id INTO litReciptId FROM recipt WHERE CODE = pcrCode;
    SELECT id INTO litPolicyId FROM policy WHERE CODE = pcrPolicyCode;
    SELECT id INTO litCustomerId FROM customer WHERE CODE = pcrCustomerCode;
    SELECT id INTO litRenewalId FROM renewal WHERE CODE = pcrRenewalCode;
    
    IF( litPolicyId != 0)THEN
	    IF (pitActiveFlag = 1 AND litReciptId != 0) THEN
		UPDATE recipt SET policy_id = litPolicyId, customer_id = litCustomerId, recipt_amount = pdlReciptAmount, penalty_amount = pdlPenaltyAmount, recipt_date = pcrReciptDate, due_date = pcrDueDate, recipt_total_amount = pdlRecpitTotalAmount, recipt_status = pcrReciptStatus, update_by = pcrUpdateBy, update_at = NOW(), active_flag = pitActiveFlag WHERE CODE = pcrCode;
		SELECT ROW_COUNT() INTO pitRowCount;
	    ELSEIF (pitActiveFlag = 1 AND litReciptId = 0) THEN
		INSERT INTO recipt ( CODE, policy_id, customer_id, recipt_amount, penalty_amount, recipt_date, due_date, recipt_total_amount ,recipt_status, update_by, update_at, active_flag) 
		VALUES ( pcrCode, litPolicyId, litCustomerId, pdlReciptAmount, pdlPenaltyAmount, pcrReciptDate, pcrDueDate, pdlRecpitTotalAmount ,pcrReciptStatus, pcrUpdateBy, NOW(), pitActiveFlag );
		SELECT ROW_COUNT() INTO pitRowCount;
	    END IF;
    ELSEIF(litRenewalId != 0)THEN 
	    IF (pitActiveFlag = 1 AND litReciptId != 0) THEN
		UPDATE recipt SET customer_id = litCustomerId, renewal_id = litRenewalId, recipt_amount = pdlReciptAmount, penalty_amount = pdlPenaltyAmount, recipt_date = pcrReciptDate, due_date = pcrDueDate, recipt_total_amount = pdlRecpitTotalAmount ,recipt_status = pcrReciptStatus, update_by = pcrUpdateBy, update_at = NOW(), active_flag = pitActiveFlag WHERE CODE = pcrCode;
		SELECT ROW_COUNT() INTO pitRowCount;
		UPDATE 	renewal SET renewal_status = pcrReciptStatus WHERE  id = litRenewalId;
	    ELSEIF (pitActiveFlag = 1 AND litReciptId = 0) THEN
		INSERT INTO recipt ( CODE, customer_id, renewal_id, recipt_amount, penalty_amount, recipt_date, due_date, recipt_total_amount,recipt_status, update_by, update_at, active_flag) 
		VALUES ( pcrCode, litCustomerId, litRenewalId, pdlReciptAmount, pdlPenaltyAmount, pcrReciptDate, pcrDueDate, pdlRecpitTotalAmount, pcrReciptStatus, pcrUpdateBy, NOW(), pitActiveFlag );
		SELECT ROW_COUNT() INTO pitRowCount;
		UPDATE 	renewal SET renewal_status = pcrReciptStatus WHERE  id = litRenewalId;
		
	    END IF;
	    
    ELSEIF (pitActiveFlag != 1 AND litReciptId != 0) THEN
        UPDATE recipt SET active_flag = pitActiveFlag, update_by = pcrUpdateBy, update_at = NOW() WHERE CODE = pcrCode;
        SELECT ROW_COUNT() INTO pitRowCount;
    END IF;
END $$
DELIMITER ;
