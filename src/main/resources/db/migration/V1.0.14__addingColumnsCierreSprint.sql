ALTER TABLE `aeromexico`.`cierresprint`
ADD COLUMN `businessdata` VARCHAR(45) NULL AFTER `pod`,
ADD COLUMN `metrics` VARCHAR(255) NULL AFTER `businessdata`,
ADD COLUMN `functionalrequirements` VARCHAR(45) NULL AFTER `metrics`,
ADD COLUMN `errorexpectedbehavior` VARCHAR(45) NULL AFTER `functionalrequirements`,
ADD COLUMN `uxdesignfigma` VARCHAR(45) NULL AFTER `errorexpectedbehavior`,
ADD COLUMN `criteriaofacceptance` VARCHAR(45) NULL AFTER `uxdesignfigma`,
ADD COLUMN `componentsdor` VARCHAR(45) NULL AFTER `criteriaofacceptance`,
ADD COLUMN `nonfunctionalrequirements` VARCHAR(45) NULL AFTER `componentsdor`,
ADD COLUMN `errorhandling` VARCHAR(45) NULL AFTER `nonfunctionalrequirements`,
ADD COLUMN `mitigateddependency` VARCHAR(45) NULL AFTER `errorhandling`,
ADD COLUMN `microservicescontract` VARCHAR(45) NULL AFTER `mitigateddependency`,
ADD COLUMN `testingstrategy` VARCHAR(45) NULL AFTER `microservicescontract`,
ADD COLUMN `specialpnrgeneration` VARCHAR(45) NULL AFTER `testingstrategy`;
