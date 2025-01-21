CREATE TABLE `courses` (
`id` BIGINT AUTO_INCREMENT,
`name` VARCHAR (100) NOT NULL,
`category` VARCHAR (100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
  );
