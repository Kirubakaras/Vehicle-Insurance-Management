DROP PROCEDURE IF EXISTS SP_PAYMENT_UID;
DELIMITER $$

CREATE PROCEDURE SP_PAYMENT_UID( IN pcrCode VARCHAR(20), IN pcrReciptCode VARCHAR(20), IN pcrCustomerCode VARCHAR(20), IN pcrPaymentDate VARCHAR(20), IN pdcPaymentAmount DECIMAL(10,2), IN pcrPaymentMode VARCHAR(20), IN pcrUpdateBy VARCHAR(20), IN pitActiveFlag TINYINT, OUT pitRowCount INT )
/*
* Procedure Name		:	SP_PAYMENT_UID
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
* Tables           : payment
* Functions        : EB_FN_ISNOTNULL,NOW
*
* Procedures       :SP_PAYMENT_UID
* 
* Revision History:
*
*	1.0 - 2016/03/18	Ezee Info 
*	Java              	Original Code
*
*/
BEGIN

/*
*-----------------------------------------------------------------------------------------------------
*  Variable Declare
*------------------------------------------------------------------------------------------------------
*/
    DECLARE litPaymentId INT DEFAULT 0;
    DECLARE litReciptId INT DEFAULT 0;
    DECLARE litCustomerId INT DEFAULT 0;
/*
*-----------------------------------------------------------------------------------------------------
*  Variable Initialized
*------------------------------------------------------------------------------------------------------
*/
    SET pitRowCount = 0;
    SELECT id INTO litPaymentId FROM payment WHERE CODE = pcrCode;
    SELECT id INTO litReciptId FROM recipt WHERE CODE = pcrReciptCode;
    SELECT id INTO litCustomerId FROM customer WHERE CODE = pcrCustomerCode;

    IF (pitActiveFlag = 1 AND litPaymentId != 0) THEN
        UPDATE payment SET recipt_id = litReciptId, customer_id = litCustomerId, payment_date = pcrPaymentDate, payment_amount = pdcPaymentAmount, payment_mode = pcrPaymentMode, update_by = pcrUpdateBy, update_at = NOW(), active_flag = pitActiveFlag  WHERE CODE = pcrCode;
        SELECT ROW_COUNT() INTO pitRowCount;
        
        UPDATE recipt SET recipt_status = 'PAID' WHERE id = litReciptId;
        
    ELSEIF (pitActiveFlag = 1 AND litPaymentId = 0) THEN
        INSERT INTO payment ( CODE, recipt_id, customer_id, payment_date, payment_amount, payment_mode, update_by, update_at, active_flag )
        VALUES ( pcrCode, litReciptId, litCustomerId, pcrPaymentDate, pdcPaymentAmount, pcrPaymentMode, pcrUpdateBy, NOW(), pitActiveFlag );
        SELECT ROW_COUNT() INTO pitRowCount;
        
        UPDATE recipt SET recipt_status = 'PAID' WHERE id = litReciptId;

    ELSEIF (pitActiveFlag != 1 AND litPaymentId != 0) THEN
        UPDATE payment SET active_flag = pitActiveFlag, update_by = pcrUpdateBy, update_at = NOW() WHERE CODE = pcrCode;
        SELECT ROW_COUNT() INTO pitRowCount;
        
    END IF;
END $$
DELIMITER ;
