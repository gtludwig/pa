USE pa;
CREATE TABLE `pa_userProfile` (
  `id` varchar(255) NOT NULL,
  `type` varchar(15) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_c6q6g27wvlld8ilurfc81yegr` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE `pa_user` (
  `id` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `firstName` varchar(255) NOT NULL,
  `lastName` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `state` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_fupbh7oy8lskxu8htqy1q1bqo` (`email`),
  UNIQUE KEY `UK_cf8y2a09g8gk0ysqqtyyx70c2` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `pa_user2userProfile` (
  `userId` varchar(255) NOT NULL,
  `userProfileId` varchar(255) NOT NULL,
  PRIMARY KEY (`userId`,`userProfileId`),
  KEY `FKo7bnr6wfcewt6i6cjw9s1abn` (`userProfileId`),
  CONSTRAINT `FKo7bnr6wfcewt6i6cjw9s1abn` FOREIGN KEY (`userProfileId`) REFERENCES `pa_userProfile` (`id`),
  CONSTRAINT `FKtcx6u47odtc55oin546ugtaw8` FOREIGN KEY (`userId`) REFERENCES `pa_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `pa_guideline` (
  `id` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `ordering` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `pa_project` (
  `id` varchar(255) NOT NULL,
  `counter` int(11) DEFAULT NULL,
  `creationDate` datetime NOT NULL,
  `creator` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `evaluationEnd` datetime NOT NULL,
  `evaluationStart` datetime NOT NULL,
  `ideal` int(11) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `sponsor` varchar(255) NOT NULL,
  `state` int(11) NOT NULL,
  `guideline` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FKe9vip34l0fnnx1lajpaeyeiwq` FOREIGN KEY (`guideline`) REFERENCES `pa_guideline` (`id`),
  CONSTRAINT `FKfwl4a1663jijkvnfuh9q6bkth` FOREIGN KEY (`creator`) REFERENCES `pa_user` (`id`),
  CONSTRAINT `FKk4eaehycnnqfpe0ulj2m65yo8` FOREIGN KEY (`sponsor`) REFERENCES `pa_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `pa_axis` (
  `id` varchar(255) NOT NULL,
  `active` bit(1) NOT NULL,
  `description` varchar(255) NOT NULL,
  `ordering` int(11) NOT NULL,
  `projectId` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjdvdvjf0js0cu1vj31kkk1opq` (`projectId`),
  CONSTRAINT `FKjdvdvjf0js0cu1vj31kkk1opq` FOREIGN KEY (`projectId`) REFERENCES `pa_project` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `pa_rule` (
  `id` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `ordering` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `pa_guideline2rule` (
  `guidelineId` varchar(255) NOT NULL,
  `ruleId` varchar(255) NOT NULL,
  PRIMARY KEY (`guidelineId`,`ruleId`),
  KEY `FKdf8a3tyfmgqh38si6y1skcjno` (`ruleId`),
  CONSTRAINT `FKdf8a3tyfmgqh38si6y1skcjno` FOREIGN KEY (`ruleId`) REFERENCES `pa_rule` (`id`),
  CONSTRAINT `FKob5uxc7qk7d5o6dvb6dd3qiwm` FOREIGN KEY (`guidelineId`) REFERENCES `pa_guideline` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `pa_axis2rule` (
  `axisId` varchar(255) NOT NULL,
  `ruleId` varchar(255) NOT NULL,
  PRIMARY KEY (`axisId`,`ruleId`),
  KEY `FKni60qdicnpq0wmp16231ic1oa` (`ruleId`),
  CONSTRAINT `FKnb29pn45x47ybfm051h559ejs` FOREIGN KEY (`axisId`) REFERENCES `pa_axis` (`id`),
  CONSTRAINT `FKni60qdicnpq0wmp16231ic1oa` FOREIGN KEY (`ruleId`) REFERENCES `pa_rule` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE `pa_analysis` (
  `id` varchar(255) NOT NULL,
  `analyst` tinyblob NOT NULL,
  `evaluationDate` datetime DEFAULT NULL,
  `latest` bit(1) NOT NULL,
  `result` float NOT NULL,
  `projectId` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3gy3857xci5h0q5m769xy3ssa` (`projectId`),
  CONSTRAINT `FK3gy3857xci5h0q5m769xy3ssa` FOREIGN KEY (`projectId`) REFERENCES `pa_project` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT  INTO `pa`.`pa_userProfile` (`id`, `type`) VALUES (UUID(), 'USER');
INSERT  INTO `pa`.`pa_userProfile` (`id`, `type`) VALUES (UUID(), 'SPECIALIST');
INSERT  INTO `pa`.`pa_userProfile` (`id`, `type`) VALUES (UUID(), 'SPONSOR');
INSERT  INTO `pa`.`pa_userProfile` (`id`, `type`) VALUES (UUID(), 'ADMIN');

INSERT INTO `pa_user` VALUES (UUID(),'gustavo.ludwig@gmail.com','MasterUser','DefaultUser','$2a$10$cTGkYBhRSUBDwbvZFdRtZesRNu0WcMAPyQ94ev4uug4ZCgZaCDK0u','status.active','master');

INSERT INTO `pa_user2userProfile` (`userId`,`userProfileId`)VALUES ((SELECT `id` FROM `pa`.`pa_user` WHERE `username` LIKE 'master'),(SELECT `id` from `pa`.`pa_userProfile` WHERE `type` LIKE 'USER'));
INSERT INTO `pa_user2userProfile` (`userId`,`userProfileId`)VALUES ((SELECT `id` FROM `pa`.`pa_user` WHERE `username` LIKE 'master'),(SELECT `id` from `pa`.`pa_userProfile` WHERE `type` LIKE 'SPECIALIST'));
INSERT INTO `pa_user2userProfile` (`userId`,`userProfileId`)VALUES ((SELECT `id` FROM `pa`.`pa_user` WHERE `username` LIKE 'master'),(SELECT `id` from `pa`.`pa_userProfile` WHERE `type` LIKE 'SPONSOR'));
INSERT INTO `pa_user2userProfile` (`userId`,`userProfileId`)VALUES ((SELECT `id` FROM `pa`.`pa_user` WHERE `username` LIKE 'master'),(SELECT `id` from `pa`.`pa_userProfile` WHERE `type` LIKE 'ADMIN'));

INSERT INTO `pa`.`pa_project` (`id`, `counter`, `creationDate`, `creator`, `description`, `evaluationEnd`, `evaluationStart`, `ideal`, `name`, `sponsor`, `state`, `guideline`) VALUES (UUID(), 0, curdate(), (SELECT `id` from `pa`.`pa_user` WHERE `username` LIKE 'master'), 'Default Project description', curdate() + interval 7 day, curdate(), 5, 'Default Project name', (SELECT `id` from `pa`.`pa_user` WHERE `username` LIKE 'master'), 0, (SELECT `id` from `pa`.`pa_guideline` WHERE `description` LIKE 'Default Guideline description'));

INSERT INTO `pa`.`pa_guideline` (`id`, `description`, `name`, `ordering`) VALUES (UUID(), 'Default Guideline description', 'Default Guideline name', 0);

INSERT INTO `pa`.`pa_rule` (`id`, `description`, `ordering`) VALUES (UUID(), 'Default Guideline Rule 0', 0);
INSERT INTO `pa`.`pa_rule` (`id`, `description`, `ordering`) VALUES (UUID(), 'Default Guideline Rule 1', 1);
INSERT INTO `pa`.`pa_rule` (`id`, `description`, `ordering`) VALUES (UUID(), 'Default Guideline Rule 2', 2);

INSERT INTO `pa`.`pa_rule` (`id`, `description`, `ordering`) VALUES (UUID(), 'Default Axis 0 Rule 0', 0);
INSERT INTO `pa`.`pa_rule` (`id`, `description`, `ordering`) VALUES (UUID(), 'Default Axis 0 Rule 1', 1);
INSERT INTO `pa`.`pa_rule` (`id`, `description`, `ordering`) VALUES (UUID(), 'Default Axis 0 Rule 2', 2);
INSERT INTO `pa`.`pa_rule` (`id`, `description`, `ordering`) VALUES (UUID(), 'Default Axis 1 Rule 0', 0);
INSERT INTO `pa`.`pa_rule` (`id`, `description`, `ordering`) VALUES (UUID(), 'Default Axis 1 Rule 1', 1);
INSERT INTO `pa`.`pa_rule` (`id`, `description`, `ordering`) VALUES (UUID(), 'Default Axis 1 Rule 2', 2);
INSERT INTO `pa`.`pa_rule` (`id`, `description`, `ordering`) VALUES (UUID(), 'Default Axis 2 Rule 0', 0);
INSERT INTO `pa`.`pa_rule` (`id`, `description`, `ordering`) VALUES (UUID(), 'Default Axis 2 Rule 1', 1);
INSERT INTO `pa`.`pa_rule` (`id`, `description`, `ordering`) VALUES (UUID(), 'Default Axis 2 Rule 2', 2);

INSERT INTO `pa`.`pa_guideline2rule` (`guidelineId`, `ruleId`) VALUES ((SELECT `id` FROM `pa`.`pa_guideline` WHERE `description` LIKE 'Default Guideline description'), (SELECT `id` FROM `pa`.`pa_rule` WHERE `description` LIKE 'Default Guideline Rule 0'));
INSERT INTO `pa`.`pa_guideline2rule` (`guidelineId`, `ruleId`) VALUES ((SELECT `id` FROM `pa`.`pa_guideline` WHERE `description` LIKE 'Default Guideline description'), (SELECT `id` FROM `pa`.`pa_rule` WHERE `description` LIKE 'Default Guideline Rule 1'));
INSERT INTO `pa`.`pa_guideline2rule` (`guidelineId`, `ruleId`) VALUES ((SELECT `id` FROM `pa`.`pa_guideline` WHERE `description` LIKE 'Default Guideline description'), (SELECT `id` FROM `pa`.`pa_rule` WHERE `description` LIKE 'Default Guideline Rule 2'));

INSERT INTO `pa`.`pa_axis` (`id`, `active`, `description`, `ordering`, `projectId`) VALUES (UUID(), 1, 'Default Axis 0', 0, (SELECT `id` FROM `pa`.`pa_project` WHERE `name` LIKE 'Default Project name'));
INSERT INTO `pa`.`pa_axis` (`id`, `active`, `description`, `ordering`, `projectId`) VALUES (UUID(), 1, 'Default Axis 1', 1, (SELECT `id` FROM `pa`.`pa_project` WHERE `name` LIKE 'Default Project name'));
INSERT INTO `pa`.`pa_axis` (`id`, `active`, `description`, `ordering`, `projectId`) VALUES (UUID(), 1, 'Default Axis 2', 2, (SELECT `id` FROM `pa`.`pa_project` WHERE `name` LIKE 'Default Project name'));

INSERT INTO `pa`.`pa_axis2rule` (`axisId`, `ruleId`) VALUES ((SELECT `id` FROM `pa`.`pa_axis` WHERE `description` LIKE 'Default Axis 0'), (SELECT `id` FROM `pa`.`pa_rule` WHERE `description` LIKE 'Default Axis 0 Rule 0'));
INSERT INTO `pa`.`pa_axis2rule` (`axisId`, `ruleId`) VALUES ((SELECT `id` FROM `pa`.`pa_axis` WHERE `description` LIKE 'Default Axis 0'), (SELECT `id` FROM `pa`.`pa_rule` WHERE `description` LIKE 'Default Axis 0 Rule 1'));
INSERT INTO `pa`.`pa_axis2rule` (`axisId`, `ruleId`) VALUES ((SELECT `id` FROM `pa`.`pa_axis` WHERE `description` LIKE 'Default Axis 0'), (SELECT `id` FROM `pa`.`pa_rule` WHERE `description` LIKE 'Default Axis 0 Rule 2'));
INSERT INTO `pa`.`pa_axis2rule` (`axisId`, `ruleId`) VALUES ((SELECT `id` FROM `pa`.`pa_axis` WHERE `description` LIKE 'Default Axis 1'), (SELECT `id` FROM `pa`.`pa_rule` WHERE `description` LIKE 'Default Axis 1 Rule 0'));
INSERT INTO `pa`.`pa_axis2rule` (`axisId`, `ruleId`) VALUES ((SELECT `id` FROM `pa`.`pa_axis` WHERE `description` LIKE 'Default Axis 1'), (SELECT `id` FROM `pa`.`pa_rule` WHERE `description` LIKE 'Default Axis 1 Rule 1'));
INSERT INTO `pa`.`pa_axis2rule` (`axisId`, `ruleId`) VALUES ((SELECT `id` FROM `pa`.`pa_axis` WHERE `description` LIKE 'Default Axis 1'), (SELECT `id` FROM `pa`.`pa_rule` WHERE `description` LIKE 'Default Axis 1 Rule 2'));
INSERT INTO `pa`.`pa_axis2rule` (`axisId`, `ruleId`) VALUES ((SELECT `id` FROM `pa`.`pa_axis` WHERE `description` LIKE 'Default Axis 2'), (SELECT `id` FROM `pa`.`pa_rule` WHERE `description` LIKE 'Default Axis 2 Rule 0'));
INSERT INTO `pa`.`pa_axis2rule` (`axisId`, `ruleId`) VALUES ((SELECT `id` FROM `pa`.`pa_axis` WHERE `description` LIKE 'Default Axis 2'), (SELECT `id` FROM `pa`.`pa_rule` WHERE `description` LIKE 'Default Axis 2 Rule 1'));
INSERT INTO `pa`.`pa_axis2rule` (`axisId`, `ruleId`) VALUES ((SELECT `id` FROM `pa`.`pa_axis` WHERE `description` LIKE 'Default Axis 2'), (SELECT `id` FROM `pa`.`pa_rule` WHERE `description` LIKE 'Default Axis 2 Rule 2'));