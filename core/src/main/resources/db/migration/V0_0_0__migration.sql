USE pa;
CREATE TABLE `pa_userProfile` (
  `id` varchar(255) NOT NULL,
  `type` varchar(15) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_c6q6g27wvlld8ilurfc81yegr` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `pa_user` (
                           `id`        varchar(255) NOT NULL,
                           `email`     varchar(255) NOT NULL,
                           `firstName` varchar(255) NOT NULL,
                           `lastName`  varchar(255) NOT NULL,
                           `password`  varchar(255) NOT NULL,
                           `state`     varchar(255) NOT NULL,
                           `username`  varchar(255) NOT NULL,
                           `enabled`   bit(1) DEFAULT NULL,
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

CREATE TABLE `pa_project` (
  `id` varchar(255) NOT NULL,
  `counter` int(11) DEFAULT NULL,
  `creationDate` datetime NOT NULL,
  `description` varchar(255) NOT NULL,
  `evaluationEnd` datetime NOT NULL,
  `evaluationStart` datetime NOT NULL,
  `ideal` int(11) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `state` int(11) NOT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `guidelineAxis` varchar(255) DEFAULT NULL,
  `sponsor` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfwl4a1663jijkvnfuh9q6bkth` (`creator`),
  KEY `FK90metvsh2je2gjhe6hsvujv7c` (`guidelineAxis`),
  KEY `FKk4eaehycnnqfpe0ulj2m65yo8` (`sponsor`),
  CONSTRAINT `FK90metvsh2je2gjhe6hsvujv7c` FOREIGN KEY (`guidelineAxis`) REFERENCES `pa_axis` (`id`),
  CONSTRAINT `FKfwl4a1663jijkvnfuh9q6bkth` FOREIGN KEY (`creator`) REFERENCES `pa_user` (`id`),
  CONSTRAINT `FKk4eaehycnnqfpe0ulj2m65yo8` FOREIGN KEY (`sponsor`) REFERENCES `pa_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `pa_project2specialists`
(
    `projectId` varchar(255) NOT NULL,
    `userId`    varchar(255) NOT NULL,
    PRIMARY KEY (`projectId`, `userId`),
    KEY `FKe2mrilbmeo2em11yudk9tx51x` (`userId`),
    CONSTRAINT `FK71i4emr4d6s95yqec9yioph5s` FOREIGN KEY (`projectId`) REFERENCES `pa_project` (`id`),
    CONSTRAINT `FKe2mrilbmeo2em11yudk9tx51x` FOREIGN KEY (`userId`) REFERENCES `pa_user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

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

INSERT INTO `pa_user`
VALUES (UUID(), 'hello@pa.ie', 'MasterUser', 'DefaultUser',
        '$2a$10$cTGkYBhRSUBDwbvZFdRtZesRNu0WcMAPyQ94ev4uug4ZCgZaCDK0u', 'user.status-active', 'master', 1);

INSERT INTO `pa_user2userProfile` (`userId`,`userProfileId`)VALUES ((SELECT `id` FROM `pa`.`pa_user` WHERE `username` LIKE 'master'),(SELECT `id` from `pa`.`pa_userProfile` WHERE `type` LIKE 'USER'));
INSERT INTO `pa_user2userProfile` (`userId`,`userProfileId`)VALUES ((SELECT `id` FROM `pa`.`pa_user` WHERE `username` LIKE 'master'),(SELECT `id` from `pa`.`pa_userProfile` WHERE `type` LIKE 'SPECIALIST'));
INSERT INTO `pa_user2userProfile` (`userId`,`userProfileId`)VALUES ((SELECT `id` FROM `pa`.`pa_user` WHERE `username` LIKE 'master'),(SELECT `id` from `pa`.`pa_userProfile` WHERE `type` LIKE 'SPONSOR'));
INSERT INTO `pa_user2userProfile` (`userId`,`userProfileId`)VALUES ((SELECT `id` FROM `pa`.`pa_user` WHERE `username` LIKE 'master'),(SELECT `id` from `pa`.`pa_userProfile` WHERE `type` LIKE 'ADMIN'));

