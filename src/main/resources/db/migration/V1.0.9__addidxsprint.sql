ALTER TABLE `aeromexico`.`cierresprint`
ADD INDEX `isx_cierreactivo` (`sprintactivo` ASC) VISIBLE;
;
ALTER TABLE `aeromexico`.`opensprint`
ADD INDEX `isx_openactivo` (`sprintactivo` ASC) VISIBLE;
;