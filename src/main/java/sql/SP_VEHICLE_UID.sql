DROP PROCEDURE IF EXISTS SP_VEHICLE_UID;
DELIMITER $$

CREATE PROCEDURE SP_VEHICLE_UID(IN pcrCode VARCHAR(20),IN pcrCustomerCode VARCHAR(20),IN pcrPlateNumber VARCHAR(20), IN pcrVehicleType VARCHAR(20), IN pcrEngineNum VARCHAR(20),  IN pcrChasisNum  VARCHAR(20), IN pcrVehicleNumber VARCHAR(20), IN pcrModelNum VARCHAR(20), IN pcrUpdateBy VARCHAR(20), IN pitActiveFlag TINYINT, OUT pitRowCount INT )
/*
* Procedure Name		:	SP_VEHICLE_UID
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
* Tables           : VEHICLE
*
* Functions        : EB_FN_ISNOTNULL,NOW
*
* Procedures       : SP_VEHICLE_IUD
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
    DECLARE litVehicleId INT DEFAULT 0;
    DECLARE litCustomerId INT DEFAULT 0;
    DECLARE lcrPlateNumber VARCHAR(20) DEFAULT NULL;
    
/*
*-----------------------------------------------------------------------------------------------------
*  Variable Initialized
*------------------------------------------------------------------------------------------------------
*/
    SET pitRowCount = 0;
    SELECT id INTO litVehicleId FROM vehicle WHERE CODE = pcrCode;
    SELECT id INTO litCustomerId  FROM customer WHERE CODE = pcrCustomerCode;
    SELECT vehicle_plate_num INTO lcrPlateNumber FROM vehicle WHERE vehicle_plate_num = pcrPlateNumber;
/*
*-----------------------------------------------------------------------------------------------------
*  Update/Insert Employee
*------------------------------------------------------------------------------------------------------
*/
    
    IF (pitActiveFlag = 1 AND litVehicleId != 0) THEN
        UPDATE vehicle SET customer_id = litCustomerId, vehicle_plate_num = pcrPlateNumber, vehicle_type = pcrVehicleType, vehicle_engin_num = pcrEngineNum, vehicle_chasis_num = pcrChasisNum, vehicle_number = pcrVehicleNumber, vehicle_model_num = pcrModelNum, update_by = pcrUpdateBy,  update_at = NOW(), active_flag = pitActiveFlag
        WHERE CODE = pcrCode;
        SELECT ROW_COUNT() INTO pitRowCount;

    ELSEIF (pitActiveFlag = 1 AND litVehicleId = 0 AND lcrPlateNumber IS NULL ) THEN
        INSERT INTO vehicle (CODE, customer_id, vehicle_plate_num, vehicle_type, vehicle_engin_num, vehicle_chasis_num, vehicle_number, vehicle_model_num,update_by, update_at, active_flag)
        VALUES (pcrCode, litCustomerId, pcrPlateNumber, pcrVehicleType, pcrEngineNum, pcrChasisNum,  pcrVehicleNumber, pcrModelNum, pcrUpdateBy,NOW(),1 );
        SELECT ROW_COUNT() INTO pitRowCount;

    ELSEIF (pitActiveFlag != 1 AND litVehicleId != 0) THEN
        UPDATE vehicle SET active_flag = pitActiveFlag, update_by = pcrUpdateBy, update_at = NOW() WHERE CODE = pcrCode;
        SELECT ROW_COUNT() INTO pitRowCount;
    END IF;
END $$
DELIMITER ;
