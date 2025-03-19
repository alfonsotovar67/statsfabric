ALTER TABLE `aeromexico`.`cierresprint`
ADD COLUMN `sprintactivo` VARCHAR(45) NULL DEFAULT NULL AFTER `issuecve`;

ALTER TABLE `aeromexico`.`opensprint`
ADD COLUMN `sprintactivo` VARCHAR(45) NULL DEFAULT NULL AFTER `issuecve`;