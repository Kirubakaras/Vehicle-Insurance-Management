DROP PROCEDURE IF EXISTS SP_EMPLOYEE_UID;
DELIMITER $$
CREATE PROCEDURE SP_EMPLOYEE_UID(IN pcrCode VARCHAR(20),IN pcrName VARCHAR(20),IN pcrUsername VARCHAR(20),IN pcrEmployeeEmail VARCHAR(30),IN pcrEmployeeMobile VARCHAR(15),IN pcrPassword VARCHAR(20),IN ptxEmployeeAddress TEXT, IN pcrEmployeeRole VARCHAR(15), IN pcrUpdateBy VARCHAR(20), IN pitActiveFlag TINYINT, OUT pitRowCount INT)
/*
* Procedure Name		:	SP_EMPLOYEE_UID
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
* Tables           : complaint
*
* Functions        : EB_FN_ISNOTNULL,NOW
*
* Procedures       : SP_EMPLOYEE_IUD
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
DECLARE litEmployeeId INT DEFAULT 0;
DECLARE litEmployeeEmail VARCHAR(30) DEFAULT NULL;
/*
*-----------------------------------------------------------------------------------------------------
*  Variable Initialized
*------------------------------------------------------------------------------------------------------
*/
SET	pitRowCount  = 0;
SELECT id INTO litEmployeeId  FROM employee WHERE CODE = pcrCode ;
SELECT employee_email INTO litEmployeeEmail FROM employee WHERE employee_email = pcrEmployeeEmail;
/*
*-----------------------------------------------------------------------------------------------------
*  Update/Insert Employee
*------------------------------------------------------------------------------------------------------
*/	
	IF ( pitActiveFlag = 1 AND litEmployeeId != 0 ) THEN
		UPDATE employee SET employee_name = pcrName, username = pcrUsername, employee_email = pcrEmployeeEmail, employee_mobile = pcrEmployeeMobile, PASSWORD = pcrPassword, employee_address = ptxEmployeeAddress , employee_role = pcrEmployeeRole, update_at = NOW(), update_by = pcrUpdateBy, active_flag = pitActiveFlag WHERE CODE = pcrCode ;
		SELECT ROW_COUNT() INTO pitRowCount;
		
	 ELSEIF(pitActiveFlag = 1 AND litEmployeeId = 0 AND litEmployeeEmail IS NULL) THEN
		INSERT INTO employee (CODE, employee_name, username, employee_email, employee_mobile,PASSWORD, employee_address, employee_role, update_at, update_by,active_flag)
		VALUES(pcrCode, pcrName, pcrUsername, pcrEmployeeEmail, pcrEmployeeMobile, pcrPassword, ptxEmployeeAddress, pcrEmployeeRole, NOW(), pcrUpdateBy, 1) ;
		SELECT ROW_COUNT() INTO pitRowCount; 
		
	ELSEIF(pitActiveFlag != 1 AND IN_FN_ISNOTNULL(pcrCode)) THEN
	  UPDATE employee SET active_flag = pitActiveFlag, update_by = pcrUpdateBy, update_at = NOW() WHERE CODE = pcrCode;
	  SELECT ROW_COUNT() INTO pitRowCount;
	END IF;
END $$

DELIMITER ;