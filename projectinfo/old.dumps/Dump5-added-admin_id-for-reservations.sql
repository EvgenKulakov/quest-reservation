CREATE DATABASE  IF NOT EXISTS `quest_reservations` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `quest_reservations`;
-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: localhost    Database: quest_reservations
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admins`
--

DROP TABLE IF EXISTS `admins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admins` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `mail` varchar(45) NOT NULL,
  `phone` char(12) DEFAULT NULL,
  `password_crypt` varchar(100) NOT NULL,
  `money` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admins`
--

LOCK TABLES `admins` WRITE;
/*!40000 ALTER TABLE `admins` DISABLE KEYS */;
INSERT INTO `admins` VALUES (1,'test','test@test.com','+79995200000','test',1000000),(5,'second','yui@yy.com','+79997776655','test',10000);
/*!40000 ALTER TABLE `admins` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blacklist`
--

DROP TABLE IF EXISTS `blacklist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blacklist` (
  `id` int NOT NULL AUTO_INCREMENT,
  `phone` char(12) NOT NULL,
  `messages` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `admin_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `blacklist_admins_id_fk` (`admin_id`),
  CONSTRAINT `blacklist_admins_id_fk` FOREIGN KEY (`admin_id`) REFERENCES `admins` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blacklist`
--

LOCK TABLES `blacklist` WRITE;
/*!40000 ALTER TABLE `blacklist` DISABLE KEYS */;
/*!40000 ALTER TABLE `blacklist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clients`
--

DROP TABLE IF EXISTS `clients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clients` (
  `id` int NOT NULL AUTO_INCREMENT,
  `admin_id` int NOT NULL,
  `first_name` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `last_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `phone` char(12) NOT NULL,
  `email` varchar(45) DEFAULT NULL,
  `blacklist_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `admin_id` (`admin_id`),
  KEY `black_list_id` (`blacklist_id`),
  CONSTRAINT `clients_ibfk_1` FOREIGN KEY (`admin_id`) REFERENCES `admins` (`id`),
  CONSTRAINT `clients_ibfk_2` FOREIGN KEY (`blacklist_id`) REFERENCES `blacklist` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=137 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clients`
--

LOCK TABLES `clients` WRITE;
/*!40000 ALTER TABLE `clients` DISABLE KEYS */;
/*!40000 ALTER TABLE `clients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quests`
--

DROP TABLE IF EXISTS `quests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quests` (
  `id` int NOT NULL AUTO_INCREMENT,
  `quest_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `slot_list` varchar(3000) NOT NULL,
  `min_persons` tinyint NOT NULL,
  `max_persons` tinyint NOT NULL,
  `auto_block` time DEFAULT NULL,
  `sms` varchar(70) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `admin_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `admin_id` (`admin_id`),
  CONSTRAINT `quests_ibfk_1` FOREIGN KEY (`admin_id`) REFERENCES `admins` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quests`
--

LOCK TABLES `quests` WRITE;
/*!40000 ALTER TABLE `quests` DISABLE KEYS */;
INSERT INTO `quests` VALUES (15,'QuestTest','{\r\n  \"monday\" : {\r\n    \"10:30\" : 2800,\r\n    \"12:00\" : 3200,\r\n    \"13:30\" : 3200,\r\n    \"15:00\" : 3600,\r\n    \"16:30\" : 3600,\r\n    \"18:00\" : 3900,\r\n    \"19:30\" : 3900,\r\n    \"21:00\" : 4300,\r\n    \"22:30\" : 4300\r\n  },\r\n  \"tuesday\" : {\r\n    \"10:30\" : 2800,\r\n    \"12:00\" : 3200,\r\n    \"13:30\" : 3200,\r\n    \"15:00\" : 3600,\r\n    \"16:30\" : 3600,\r\n    \"18:00\" : 3900,\r\n    \"19:30\" : 3900,\r\n    \"21:00\" : 4300,\r\n    \"22:30\" : 4300\r\n  },\r\n  \"wednesday\" : {\r\n    \"10:30\" : 2800,\r\n    \"12:00\" : 3200,\r\n    \"13:30\" : 3200,\r\n    \"15:00\" : 3600,\r\n    \"16:30\" : 3600,\r\n    \"18:00\" : 3900,\r\n    \"19:30\" : 3900,\r\n    \"21:00\" : 4300,\r\n    \"22:30\" : 4300\r\n  },\r\n  \"thursday\" : {\r\n    \"10:30\" : 2800,\r\n    \"12:00\" : 3200,\r\n    \"13:30\" : 3200,\r\n    \"15:00\" : 3600,\r\n    \"16:30\" : 3600,\r\n    \"18:00\" : 3900,\r\n    \"19:30\" : 3900,\r\n    \"21:00\" : 4300,\r\n    \"22:30\" : 4300\r\n  },\r\n  \"friday\" : {\r\n    \"10:30\" : 2800,\r\n    \"12:00\" : 3200,\r\n    \"13:30\" : 3200,\r\n    \"15:00\" : 3600,\r\n    \"16:30\" : 3600,\r\n    \"18:00\" : 3900,\r\n    \"19:30\" : 3900,\r\n    \"21:00\" : 4300,\r\n    \"22:30\" : 4300\r\n  },\r\n  \"saturday\" : {\r\n    \"10:30\" : 3200,\r\n    \"12:00\" : 3600,\r\n    \"13:30\" : 3600,\r\n    \"15:00\" : 3900,\r\n    \"16:30\" : 3900,\r\n    \"18:00\" : 4300,\r\n    \"19:30\" : 4300,\r\n    \"21:00\" : 4600,\r\n    \"22:30\" : 4600\r\n  },\r\n  \"sunday\" : {\r\n    \"10:30\" : 3200,\r\n    \"12:00\" : 3600,\r\n    \"13:30\" : 3600,\r\n    \"15:00\" : 3900,\r\n    \"16:30\" : 3900,\r\n    \"18:00\" : 4300,\r\n    \"19:30\" : 4300,\r\n    \"21:00\" : 4600,\r\n    \"22:30\" : 4600\r\n  }\r\n}',2,8,'00:00:00',NULL,1),(16,'YamiQuest','{\r\n  \"monday\" : {\r\n    \"10:30\" : 2800,\r\n    \"12:00\" : 3200,\r\n    \"13:30\" : 3200,\r\n    \"15:00\" : 3600,\r\n    \"16:30\" : 3600,\r\n    \"18:00\" : 3900,\r\n    \"19:30\" : 3900,\r\n    \"21:00\" : 4300,\r\n    \"22:30\" : 4300\r\n  },\r\n  \"tuesday\" : {\r\n    \"10:30\" : 2800,\r\n    \"12:00\" : 3200,\r\n    \"13:30\" : 3200,\r\n    \"15:00\" : 3600,\r\n    \"16:30\" : 3600,\r\n    \"18:00\" : 3900,\r\n    \"19:30\" : 3900,\r\n    \"21:00\" : 4300,\r\n    \"22:30\" : 4300\r\n  },\r\n  \"wednesday\" : {\r\n    \"10:30\" : 2800,\r\n    \"12:00\" : 3200,\r\n    \"13:30\" : 3200,\r\n    \"15:00\" : 3600,\r\n    \"16:30\" : 3600,\r\n    \"18:00\" : 3900,\r\n    \"19:30\" : 3900,\r\n    \"21:00\" : 4300,\r\n    \"22:30\" : 4300\r\n  },\r\n  \"thursday\" : {\r\n    \"10:30\" : 2800,\r\n    \"12:00\" : 3200,\r\n    \"13:30\" : 3200,\r\n    \"15:00\" : 3600,\r\n    \"16:30\" : 3600,\r\n    \"18:00\" : 3900,\r\n    \"19:30\" : 3900,\r\n    \"21:00\" : 4300,\r\n    \"22:30\" : 4300\r\n  },\r\n  \"friday\" : {\r\n    \"10:30\" : 2800,\r\n    \"12:00\" : 3200,\r\n    \"13:30\" : 3200,\r\n    \"15:00\" : 3600,\r\n    \"16:30\" : 3600,\r\n    \"18:00\" : 3900,\r\n    \"19:30\" : 3900,\r\n    \"21:00\" : 4300,\r\n    \"22:30\" : 4300\r\n  },\r\n  \"saturday\" : {\r\n    \"10:30\" : 3200,\r\n    \"12:00\" : 3600,\r\n    \"13:30\" : 3600,\r\n    \"15:00\" : 3900,\r\n    \"16:30\" : 3900,\r\n    \"18:00\" : 4300,\r\n    \"19:30\" : 4300,\r\n    \"21:00\" : 4600,\r\n    \"22:30\" : 4600\r\n  },\r\n  \"sunday\" : {\r\n    \"10:30\" : 3200,\r\n    \"12:00\" : 3600,\r\n    \"13:30\" : 3600,\r\n    \"15:00\" : 3900,\r\n    \"16:30\" : 3900,\r\n    \"18:00\" : 4300,\r\n    \"19:30\" : 4300,\r\n    \"21:00\" : 4600,\r\n    \"22:30\" : 4600\r\n  }\r\n}',2,8,'00:00:00',NULL,1);
/*!40000 ALTER TABLE `quests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservations`
--

DROP TABLE IF EXISTS `reservations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservations` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date_reserve` date NOT NULL,
  `time_reserve` time NOT NULL,
  `time_created` datetime DEFAULT NULL,
  `time_last_change` datetime DEFAULT NULL,
  `changed_slot_time` time DEFAULT NULL,
  `auto_block` time DEFAULT NULL,
  `quest_id` int NOT NULL,
  `status_type` varchar(20) NOT NULL,
  `source_reserve` varchar(45) NOT NULL,
  `changed_price` int DEFAULT NULL,
  `client_id` int DEFAULT NULL,
  `count_persons` tinyint DEFAULT NULL,
  `admin_comment` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `client_comment` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `history_messages` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `admin_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `client_id` (`client_id`),
  KEY `reservations_quests_id_fk` (`quest_id`),
  KEY `reservations_admins_id_fk` (`admin_id`),
  CONSTRAINT `reservations_admins_id_fk` FOREIGN KEY (`admin_id`) REFERENCES `admins` (`id`),
  CONSTRAINT `reservations_ibfk_1` FOREIGN KEY (`client_id`) REFERENCES `clients` (`id`),
  CONSTRAINT `reservations_quests_id_fk` FOREIGN KEY (`quest_id`) REFERENCES `quests` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=123 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservations`
--

LOCK TABLES `reservations` WRITE;
/*!40000 ALTER TABLE `reservations` DISABLE KEYS */;
/*!40000 ALTER TABLE `reservations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `status_quest`
--

DROP TABLE IF EXISTS `status_quest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `status_quest` (
  `status_id` int NOT NULL,
  `quest_id` int NOT NULL,
  PRIMARY KEY (`status_id`,`quest_id`),
  KEY `quest_id` (`quest_id`),
  CONSTRAINT `status_quest_ibfk_1` FOREIGN KEY (`status_id`) REFERENCES `statuses` (`id`),
  CONSTRAINT `status_quest_ibfk_2` FOREIGN KEY (`quest_id`) REFERENCES `quests` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status_quest`
--

LOCK TABLES `status_quest` WRITE;
/*!40000 ALTER TABLE `status_quest` DISABLE KEYS */;
INSERT INTO `status_quest` VALUES (1,15),(2,15),(3,15),(4,15),(1,16),(2,16),(3,16),(4,16);
/*!40000 ALTER TABLE `status_quest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `statuses`
--

DROP TABLE IF EXISTS `statuses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `statuses` (
  `id` int NOT NULL AUTO_INCREMENT,
  `status_type` enum('BLOCK','MODIFIED','NEW_RESERVE','CANCEL','CONFIRMED','COMPLETED') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `statuses`
--

LOCK TABLES `statuses` WRITE;
/*!40000 ALTER TABLE `statuses` DISABLE KEYS */;
INSERT INTO `statuses` VALUES (1,'NEW_RESERVE'),(2,'CANCEL'),(3,'CONFIRMED'),(4,'COMPLETED');
/*!40000 ALTER TABLE `statuses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `synchronized_quests`
--

DROP TABLE IF EXISTS `synchronized_quests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `synchronized_quests` (
  `id_first_quest` int NOT NULL,
  `id_second_quest` int NOT NULL,
  PRIMARY KEY (`id_first_quest`,`id_second_quest`),
  KEY `id_second_quest` (`id_second_quest`),
  CONSTRAINT `synchronized_quests_ibfk_1` FOREIGN KEY (`id_first_quest`) REFERENCES `quests` (`id`),
  CONSTRAINT `synchronized_quests_ibfk_2` FOREIGN KEY (`id_second_quest`) REFERENCES `quests` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `synchronized_quests`
--

LOCK TABLES `synchronized_quests` WRITE;
/*!40000 ALTER TABLE `synchronized_quests` DISABLE KEYS */;
/*!40000 ALTER TABLE `synchronized_quests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_quest`
--

DROP TABLE IF EXISTS `user_quest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_quest` (
  `user_id` int NOT NULL,
  `quest_id` int NOT NULL,
  PRIMARY KEY (`user_id`,`quest_id`),
  KEY `quest_id` (`quest_id`),
  CONSTRAINT `user_quest_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `user_quest_ibfk_2` FOREIGN KEY (`quest_id`) REFERENCES `quests` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_quest`
--

LOCK TABLES `user_quest` WRITE;
/*!40000 ALTER TABLE `user_quest` DISABLE KEYS */;
INSERT INTO `user_quest` VALUES (22,15);
/*!40000 ALTER TABLE `user_quest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password_crypt` varchar(100) NOT NULL,
  `mail` varchar(45) DEFAULT NULL,
  `admin_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `admin_id` (`admin_id`),
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`admin_id`) REFERENCES `admins` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (22,'AdminTest','test',NULL,1),(24,'secondUser','qwe',NULL,1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'quest_reservations'
--

--
-- Dumping routines for database 'quest_reservations'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-09-01 14:34:59
