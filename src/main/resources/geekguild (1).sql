SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

SET NAMES utf8mb4;

DROP DATABASE IF EXISTS `geekguild`;
CREATE DATABASE `geekguild` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `geekguild`;

DROP TABLE IF EXISTS `comments`;
CREATE TABLE `comments` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `text` varchar(255) NOT NULL,
    `post_id` bigint(20) DEFAULT NULL,
    `creator_id` bigint(20) DEFAULT NULL,
    `snippet` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKbqnvawwwv4gtlctsi3o7vs131` (`post_id`),
    KEY `FKt7f0j94mbyal8bamvf1friujw` (`creator_id`),
    CONSTRAINT `FKbqnvawwwv4gtlctsi3o7vs131` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`),
    CONSTRAINT `FKt7f0j94mbyal8bamvf1friujw` FOREIGN KEY (`creator_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `comment_reactions`;
CREATE TABLE `comment_reactions` (
    `reaction_id` bigint(20) NOT NULL,
    `comment_id` bigint(20) NOT NULL,
    KEY `FKfb7jmhiih0qcj4sykg2pcip35` (`comment_id`),
    KEY `FKi74dcdqajkaycymcaf3l9an47` (`reaction_id`),
    CONSTRAINT `FKfb7jmhiih0qcj4sykg2pcip35` FOREIGN KEY (`comment_id`) REFERENCES `comments` (`id`),
    CONSTRAINT `FKi74dcdqajkaycymcaf3l9an47` FOREIGN KEY (`reaction_id`) REFERENCES `reaction` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `friend_request`;
CREATE TABLE `friend_request` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `status` varchar(255) DEFAULT NULL,
    `receiver_id` bigint(20) DEFAULT NULL,
    `sender_id` bigint(20) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK2j9x9icn4n27jgwx9daltsi9a` (`receiver_id`),
    KEY `FK5rji2dcs4fmykw6ovpsyv5ssw` (`sender_id`),
    CONSTRAINT `FK2j9x9icn4n27jgwx9daltsi9a` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`id`),
    CONSTRAINT `FK5rji2dcs4fmykw6ovpsyv5ssw` FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `groups`;
CREATE TABLE `groups` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `banner` varchar(255) DEFAULT NULL,
    `description` varchar(255) DEFAULT NULL,
    `groupname` varchar(255) NOT NULL,
    `image` varchar(255) DEFAULT NULL,
    `admin_id` bigint(20) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `groupusers`;
CREATE TABLE `groupusers` (
    `user_id` bigint(20) NOT NULL,
    `group_id` bigint(20) NOT NULL,
    KEY `FKpv04nf5ei8yc67jfqfkpy6tak` (`group_id`),
    KEY `FKft4ds04ndrquosa71sbb7ghhv` (`user_id`),
    CONSTRAINT `FKft4ds04ndrquosa71sbb7ghhv` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `FKpv04nf5ei8yc67jfqfkpy6tak` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `language`;
CREATE TABLE `language` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `language_name` varchar(255) NOT NULL,
    `logo` varchar(255) DEFAULT NULL,
    `user_id` bigint(20) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKsp8nh9fmid45e5nex598wdnm9` (`user_id`),
    CONSTRAINT `FKsp8nh9fmid45e5nex598wdnm9` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `portfolio`;
CREATE TABLE `portfolio` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `about` varchar(255) DEFAULT NULL,
    `facebook` varchar(255) DEFAULT NULL,
    `github` varchar(255) DEFAULT NULL,
    `headline` varchar(255) DEFAULT NULL,
    `linkedin` varchar(255) DEFAULT NULL,
    `misclink` varchar(255) DEFAULT NULL,
    `proj1` varchar(255) DEFAULT NULL,
    `proj2` varchar(255) DEFAULT NULL,
    `proj3` varchar(255) DEFAULT NULL,
    `user_id` bigint(20) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_c7fv45hgmubvas83vp5ikuo0i` (`user_id`),
    CONSTRAINT `FK76ws6sj6wg26k7lcx6a5mtqi4` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `post`;
CREATE TABLE `post` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `body` varchar(255) NOT NULL,
    `image` varchar(255) DEFAULT NULL,
    `group_id` bigint(20) DEFAULT NULL,
    `user_id` bigint(20) DEFAULT NULL,
    `snippet` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKpme8kb381ax97lhyydx4cu8ne` (`group_id`),
    KEY `FK7ky67sgi7k0ayf22652f7763r` (`user_id`),
    CONSTRAINT `FK7ky67sgi7k0ayf22652f7763r` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `FKpme8kb381ax97lhyydx4cu8ne` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `post_reactions`;
CREATE TABLE `post_reactions` (
    `reaction_id` bigint(20) NOT NULL,
    `post_id` bigint(20) NOT NULL,
    KEY `FK7aa8im6vplpp1ndineboq9jyg` (`post_id`),
    KEY `FK4awo0m3pa5w6t4arpxmo6jddr` (`reaction_id`),
    CONSTRAINT `FK4awo0m3pa5w6t4arpxmo6jddr` FOREIGN KEY (`reaction_id`) REFERENCES `reaction` (`id`),
    CONSTRAINT `FK7aa8im6vplpp1ndineboq9jyg` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `reaction`;
CREATE TABLE `reaction` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `reaction` varchar(255) NOT NULL,
    `user_id` bigint(20) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK52m11aguxqx0uje12knj056jw` (`user_id`),
    CONSTRAINT `FK52m11aguxqx0uje12knj056jw` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `banner` varchar(255) DEFAULT NULL,
    `email` varchar(255) NOT NULL,
    `firstname` varchar(255) DEFAULT NULL,
    `image` varchar(255) DEFAULT NULL,
    `lastname` varchar(255) DEFAULT NULL,
    `password` varchar(255) NOT NULL,
    `username` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`),
    UNIQUE KEY `UK_r43af9ap4edm43mmtq01oddj6` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `user_languages`;
CREATE TABLE `user_languages` (
    `user_id` bigint(20) NOT NULL,
    `language_id` bigint(20) NOT NULL,
    KEY `FK85eswkit45d5iy2oeuajp6ms5` (`language_id`),
    KEY `FKt3sjkb7b30p03i378qdcr2s9k` (`user_id`),
    CONSTRAINT `FK85eswkit45d5iy2oeuajp6ms5` FOREIGN KEY (`language_id`) REFERENCES `language` (`id`),
    CONSTRAINT `FKt3sjkb7b30p03i378qdcr2s9k` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `work`;
CREATE TABLE `work` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `ask` varchar(255) DEFAULT NULL,
    `fact` varchar(255) DEFAULT NULL,
    `help` varchar(255) DEFAULT NULL,
    `learn` varchar(255) DEFAULT NULL,
    `working` varchar(255) DEFAULT NULL,
    `user_id` bigint(20) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_3x3693qx6alerxkwb2e527jyk` (`user_id`),
    CONSTRAINT `FKdvq5s0o0fqpnbyeffg1nf9831` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
