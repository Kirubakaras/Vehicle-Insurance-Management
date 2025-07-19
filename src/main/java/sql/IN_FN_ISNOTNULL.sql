DROP FUNCTION IF EXISTS IN_FN_ISNOTNULL;

CREATE FUNCTION IN_FN_ISNOTNULL(pcrString TEXT)	RETURNS BOOLEAN
DETERMINISTIC
NO SQL
/*
* Procedure Name       : IN_FN_ISNOTNULL
* 
* Purpose              : Compress the Numberic Char To String. 
*
* Input                : Nemeric String
*
* Output               : Status
* 						 Sequence Code
*
* Returns              : 0 - Successful
*                        1 - Unsuccessful
*
* Dependencies
*
*     Tables           : none
*
*     Functions        : None
*
*     Procedures       : none
*
* Revision History:
*
*     1.0 - 2014/01/30	      Ezee Info 
*     EzeeTech                 Original Code
*
*/

RETURN	IF(ISNULL(pcrString) OR TRIM(pcrString) = '' OR TRIM(pcrString) = 'null'  OR TRIM(pcrString) = 'NULL'  OR TRIM(pcrString) = 'NA' ,FALSE,TRUE);