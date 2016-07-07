CREATE SCHEMA `openflorian` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin ;
CREATE TABLE `of_operation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `buzzword` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `city` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `crossway` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `dispatchedAt` datetime DEFAULT NULL,
  `incurredAt` datetime DEFAULT NULL,
  `keyword` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `object` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `operationNr` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `positionLatitude` double DEFAULT NULL,
  `positionLongitude` double DEFAULT NULL,
  `priority` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `resourcesRaw` longtext COLLATE utf8_bin,
  `street` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `takenOverAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `of_operation_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `callName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `crew` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `licensePlate` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `type` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `of_operation_of_operation_resource` (
  `operation_id` bigint(20) NOT NULL,
  `operation_resource_id` bigint(20) NOT NULL,
  KEY `FK7jny2h7elb4kwnhh8rbc6ir6o` (`operation_resource_id`),
  KEY `FK49ap70vjapykhdbkxp8bub0ob` (`operation_id`),
  FOREIGN KEY (`operation_id`) REFERENCES `of_operation` (`id`),
  FOREIGN KEY (`operation_resource_id`) REFERENCES `of_operation_resource` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `of_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdAt` datetime DEFAULT NULL,
  `email` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `firstname` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `isActive` bit(1) DEFAULT NULL,
  `isDeleted` bit(1) DEFAULT NULL,
  `lastname` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `password` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `passwordSalt` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `user_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `permission` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6gp4hkp0olya0a4bksrgcqjsp` (`user_id`),
  FOREIGN KEY (`user_id`) REFERENCES `of_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;