  CREATE TABLE `profiles` (
`id` BIGINT AUTO_INCREMENT,
`name` VARCHAR (100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
);

CREATE TABLE `user_profile` (
`id` BIGINT AUTO_INCREMENT,
`name` VARCHAR (100) NOT NULL,
`email` VARCHAR (150) NOT NULL,
`clave` VARCHAR (300) NOT NULL,
`profile_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
KEY `profile_id_idx` (`profile_id`),
CONSTRAINT `profile_id` FOREIGN KEY (`profile_id`) REFERENCES `profiles` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
);