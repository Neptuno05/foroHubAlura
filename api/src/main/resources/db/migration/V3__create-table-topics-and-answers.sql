CREATE TABLE `topics` (
`id` BIGINT AUTO_INCREMENT,
`title` VARCHAR (200) NOT NULL,
`message` TEXT NOT NULL,
`created_date` DATETIME NOT NULL,
`status` VARCHAR (50) NOT NULL,
`user_id` BIGINT NOT NULL,
`course_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
KEY `user_id_idx` (`user_id`), KEY `course_id_idx` (`course_id`),
CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `user_profile` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT `course_id` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
);


CREATE TABLE `answers` (
`id` BIGINT AUTO_INCREMENT,
`message` TEXT NOT NULL,
`created_date` DATETIME NOT NULL,
`solution` BOOLEAN DEFAULT FALSE,
`topic_id` BIGINT NOT NULL,
`an_user_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
KEY `topic_id_idx` (`topic_id`),
KEY `an_user_id_idx` (`an_user_id`),
CONSTRAINT `topic_id` FOREIGN KEY (`topic_id`) REFERENCES `topics` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT `an_user_id` FOREIGN KEY (`an_user_id`) REFERENCES `user_profile` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
);