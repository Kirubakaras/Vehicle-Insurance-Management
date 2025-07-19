DROP PROCEDURE IF EXISTS SP_VEHICLESERVICE_UID;
DELIMITER $

CREATE PROCEDURE SP_VEHICLESERVICE_UID(IN pcrCode VARCHAR(20),IN pcrVehicleCode VARCHAR(20), IN pcrClaimCode VARCHAR(20),IN pcrServiceDate VARCHAR(20), IN ptxServiceDescription TEXT, IN pdlServiceCost DECIMAL(10,2),IN pcrServiceStatus VARCHAR(20), IN pcrUpdateBy VARCHAR(20), IN pitActiveFlag TINYINT , OUT pitRowCount INT )

/*
* Procedure Name		:	SP_VEHICLESERVICE_UID
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
* Tables           :  VEHICLESERVICE
* Functions        : EB_FN_ISNOTNULL,NOW
*
* Procedures       : SP_VEHICLESERVICE_UID
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
 
	DECLARE litVehicleServiceId  INT DEFAULT 0;
	DECLARE litVehicleId INT DEFAULT 0;
	DECLARE litClaimId INT DEFAULT 0;
	DECLARE litCount INT DEFAULT 0;
/*
*-----------------------------------------------------------------------------------------------------
*  Variable Initialized
*------------------------------------------------------------------------------------------------------
*/
	SET pitRowCount = 0;
	SELECT id INTO litVehicleServiceId  FROM vehicleservice WHERE CODE = pcrCode;
	SELECT id INTO litVehicleId FROM vehicle WHERE CODE = pcrVehicleCode;
	SELECT id INTO litClaimId FROM claim WHERE CODE = pcrClaimCode;
	SELECT COUNT(claim_id) INTO litCount FROM vehicleservice WHERE claim_id = litClaimId;

/*
*-----------------------------------------------------------------------------------------------------
*  Update/Insert Employee
*------------------------------------------------------------------------------------------------------
*/
	IF(pitActiveFlag = 1 AND litVehicleServiceId != 0) THEN
		UPDATE vehicleservice  SET CODE = pcrCode ,vehicle_id = litVehicleId ,claim_id = litClaimId ,service_date = pcrServiceDate,service_description = ptxServiceDescription ,service_cost = pdlServiceCost ,service_status = pcrServiceStatus ,update_by = pcrUpdateBy ,update_at = NOW(),active_flag = pitActiveFlag 
		WHERE CODE = pcrCode;
		SELECT ROW_COUNT() INTO pitRowCount;
		
		IF( pcrServiceStatus = 'COMPLETED' )THEN 
			UPDATE claim SET claim_status = 'SETTLED' WHERE id  = litClaimId;
		END IF;	
		
	ELSEIF (pitActiveFlag = 1 AND litVehicleServiceId = 0 AND litCount = 0 )THEN 
		INSERT INTO vehicleservice (CODE,vehicle_id,claim_id,service_date,service_description, service_cost,service_status,update_by,update_at,active_flag)
		VALUES ( pcrCode, litVehicleId, litClaimId, pcrServiceDate, ptxServiceDescription, pdlServiceCost, pcrServiceStatus, pcrUpdateBy, NOW(),1);
		
		SELECT ROW_COUNT() INTO pitRowCount;
		
		IF( pcrServiceStatus = 'COMPLETED' ) THEN 
			UPDATE claim SET claim_status = 'SETTLED' WHERE id  = litClaimId;
		ELSEIF( pcrServiceStatus = 'INCOMPLETED' ) THEN 
			UPDATE claim SET claim_status = 'APPROVED' WHERE id  = litClaimId;
		END IF;
		
	ELSEIF (pitActiveFlag = 1 AND litVehicleServiceId != 0) THEN
		UPDATE vehicleservice SET active_flag = pitActiveFlag, update_by = pcrUpdateBy, update_at = NOW() WHERE CODE = pcrCode;
		SELECT ROW_COUNT() INTO pitRowCount;
	 END IF;
END $

DELIMITER ;

