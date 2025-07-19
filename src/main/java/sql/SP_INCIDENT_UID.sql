DROP PROCEDURE IF EXISTS SP_INCIDENT_UID;
DELIMITER $$

CREATE PROCEDURE SP_INCIDENT_UID( IN pcrCode VARCHAR(20),IN pcrCustomerCode VARCHAR(20), IN pcrIncidentDate VARCHAR(20), IN pcrIncidentType VARCHAR(50), IN pcrIncidentInspector VARCHAR(20), IN pcrIncidentCost INT, IN pcrIncidentDescription TEXT,IN pcrIncidentStatus VARCHAR(20) ,IN pcrUpdateBy VARCHAR(20), IN pitActiveFlag TINYINT, OUT pitRowCount INT )
/*
* Procedure Name		:	SP_INCIDENT_UID
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
* Tables           : INCIDENT
* Functions        : EB_FN_ISNOTNULL,NOW
*
* Procedures       : SP_INCIDENT_IUD
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
    DECLARE litIncidentId INT DEFAULT 0;
    DECLARE litCustomerId INT DEFAULT 0;
/*
*-----------------------------------------------------------------------------------------------------
*  Variable Initialized
*------------------------------------------------------------------------------------------------------
*/
    SET pitRowCount = 0;
    SELECT id INTO litIncidentId FROM incident WHERE CODE = pcrCode;
    SELECT id INTO litCustomerId FROM customer WHERE CODE = pcrCustomerCode;
/*
*-----------------------------------------------------------------------------------------------------
*  Update/Insert Employee
*------------------------------------------------------------------------------------------------------
*/
    IF (pitActiveFlag = 1 AND litIncidentId != 0) THEN
        UPDATE incident SET customer_id = litCustomerId, incident_date = pcrIncidentDate, incident_type = pcrIncidentType, incident_inspector = pcrIncidentInspector, incident_cost = pcrIncidentCost, incident_description = pcrIncidentDescription,incident_status = pcrIncidentStatus , update_by = pcrUpdateBy, update_at = NOW(), active_flag = pitActiveFlag WHERE CODE = pcrCode;
        SELECT ROW_COUNT() INTO pitRowCount;
        
    ELSEIF (pitActiveFlag = 1 AND litIncidentId = 0) THEN
        INSERT INTO incident (CODE, customer_id, incident_date, incident_type, incident_inspector, incident_cost, incident_description, incident_status, update_by, update_at, active_flag )
        VALUES ( pcrCode, litCustomerId, pcrIncidentDate, pcrIncidentType, pcrIncidentInspector, pcrIncidentCost, pcrIncidentDescription, pcrIncidentStatus, pcrUpdateBy, NOW(), 1);
        SELECT ROW_COUNT() INTO pitRowCount;
        
    ELSEIF (pitActiveFlag != 1 AND litIncidentId = 0) THEN
        UPDATE incident SET active_flag = pitActiveFlag, update_by = pcrUpdateBy, update_at = NOW() WHERE CODE = pcrCode;
        SELECT ROW_COUNT() INTO pitRowCount;
    END IF;
END $$

DELIMITER ;
