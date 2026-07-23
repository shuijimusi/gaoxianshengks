-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: gybbysj
-- ------------------------------------------------------
-- Server version	8.0.34

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `teacher_id` bigint NOT NULL COMMENT '老师id',
  `course_code` varchar(50) NOT NULL COMMENT '课程编码',
  `course_name` varchar(100) NOT NULL COMMENT '课程名称',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '课程状态：1正常 0禁用',
  `student_num` int NOT NULL DEFAULT '0' COMMENT '选课人数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_course_code` (`course_code`) COMMENT '课程编码唯一不重复',
  KEY `idx_teacher_id` (`teacher_id`) COMMENT '根据老师id查询索引'
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='课程表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES (1,4,'80ce0b0e-2805-4ff5-a6d8-96091e705cfa','{\"name\":\"数据库原理\"}','2026-07-05 22:49:42','2026-07-05 23:18:42',0,0),(2,4,'437cf2f2-bc9d-4493-86cb-654d9ca68993','{\"name\":\"尝试1\"}','2026-07-05 22:52:24','2026-07-05 23:18:42',0,0),(3,4,'da2a9e7f-4310-49a6-9688-65032412003c','{\"name\":\"111\"}','2026-07-05 22:59:06','2026-07-05 23:19:00',0,0),(4,4,'1600c9e2-3d78-45ec-8a7e-5459f17f9cf0','{\"name\":\"222\"}','2026-07-05 23:02:45','2026-07-05 23:19:00',0,0),(5,4,'0c358f85-0692-451f-9091-f3b461f06fb9','222333','2026-07-05 23:11:56','2026-07-20 15:57:01',1,2),(6,4,'16d57c73-e3d8-4a23-b01e-b641b70145e1','333','2026-07-05 23:13:50','2026-07-20 09:00:16',1,1),(7,4,'dbd8bb66-ec68-45c6-8fcd-d475e4b524cf','22333','2026-07-06 23:01:07','2026-07-06 23:01:31',1,1);
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exam_course`
--

DROP TABLE IF EXISTS `exam_course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `exam_course` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `teacher_id` bigint NOT NULL COMMENT '发起考试的老师ID',
  `class_id` bigint NOT NULL COMMENT '班级ID',
  `paper_id` bigint NOT NULL COMMENT '试卷ID，关联exam_paper',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '考试状态：0-未开始，1-考试中，2-已结束',
  `start_time` datetime DEFAULT NULL COMMENT '考试开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '考试截止时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_paper_id` (`paper_id`),
  KEY `idx_class_id` (`class_id`),
  KEY `idx_teacher_id` (`teacher_id`),
  KEY `idx_status_endtime` (`status`,`end_time`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='班级考试表（一场分配给班级的考试）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exam_course`
--

LOCK TABLES `exam_course` WRITE;
/*!40000 ALTER TABLE `exam_course` DISABLE KEYS */;
INSERT INTO `exam_course` VALUES (1,4,6,1,2,'2026-07-16 13:36:34','2026-07-16 13:37:34','2026-07-16 13:36:33','2026-07-16 13:39:11'),(2,4,5,4,2,'2026-07-16 14:20:44','2026-07-16 15:50:44','2026-07-16 14:20:43','2026-07-16 15:54:24'),(3,4,5,4,2,'2026-07-16 16:06:06','2026-07-16 17:36:06','2026-07-16 16:06:05','2026-07-17 10:58:20'),(4,4,5,4,2,'2026-07-17 13:28:40','2026-07-17 14:58:40','2026-07-17 13:28:40','2026-07-17 14:58:53'),(5,4,5,5,2,'2026-07-17 14:55:50','2026-07-17 16:25:50','2026-07-17 14:55:49','2026-07-20 08:51:36'),(6,4,6,5,2,'2026-07-20 08:53:08','2026-07-20 10:23:08','2026-07-20 08:53:07','2026-07-20 10:23:34'),(7,4,5,5,2,'2026-07-20 09:02:58','2026-07-20 10:32:58','2026-07-20 09:02:57','2026-07-20 10:45:27'),(8,4,5,5,2,'2026-07-20 10:45:47','2026-07-20 12:15:47','2026-07-20 10:45:46','2026-07-20 13:12:39'),(9,4,5,5,2,'2026-07-20 13:12:55','2026-07-20 14:42:55','2026-07-20 13:12:55','2026-07-20 14:46:10'),(10,4,5,5,2,'2026-07-20 14:47:14','2026-07-20 16:17:14','2026-07-20 14:47:14','2026-07-21 08:25:49'),(11,4,5,5,2,'2026-07-20 14:56:36','2026-07-20 16:26:36','2026-07-20 14:56:35','2026-07-21 08:25:49'),(12,4,7,5,2,'2026-07-21 08:26:25','2026-07-21 09:56:25','2026-07-21 08:26:24','2026-07-21 09:56:36'),(13,4,6,3,2,'2026-07-21 09:01:03','2026-07-21 09:02:03','2026-07-21 09:01:02','2026-07-21 09:27:30');
/*!40000 ALTER TABLE `exam_course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exam_paper`
--

DROP TABLE IF EXISTS `exam_paper`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `exam_paper` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '试卷ID',
  `title` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '试卷标题',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT '试卷描述',
  `total_score` decimal(8,2) NOT NULL COMMENT '总分',
  `duration` int NOT NULL COMMENT '考试时长(分钟)',
  `pass_score` decimal(8,2) DEFAULT NULL COMMENT '及格线',
  `question_count` int NOT NULL COMMENT '题目数量',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `creator_id` bigint NOT NULL COMMENT '创建者ID',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exam_paper`
--

LOCK TABLES `exam_paper` WRITE;
/*!40000 ALTER TABLE `exam_paper` DISABLE KEYS */;
INSERT INTO `exam_paper` VALUES (1,'2312','213123',20.00,1,12.00,10,'2026-07-16 11:11:11','2026-07-22 08:24:25',4,1),(2,'213','21312',20.00,1213,12.00,10,'2026-07-16 11:12:08','2026-07-22 08:24:30',4,1),(3,'123','213',20.00,1,12.00,10,'2026-07-16 11:14:22','2026-07-22 08:24:32',4,1),(4,'第一章原理测试','第一章',10.00,90,6.00,5,'2026-07-16 14:20:36','2026-07-16 14:20:36',4,0),(5,'第一章综合测试','全部题型都有的考试',12.00,90,7.00,6,'2026-07-17 14:55:45','2026-07-17 14:55:45',4,0);
/*!40000 ALTER TABLE `exam_paper` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exam_paper_question`
--

DROP TABLE IF EXISTS `exam_paper_question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `exam_paper_question` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `paper_id` bigint NOT NULL COMMENT '试卷ID',
  `question_id` bigint NOT NULL COMMENT '题目ID',
  `question_type` int NOT NULL COMMENT '题型',
  `score` decimal(5,2) NOT NULL COMMENT '本题分值',
  `sort_order` int NOT NULL DEFAULT '0' COMMENT '排序',
  PRIMARY KEY (`id`),
  KEY `paper_id` (`paper_id`),
  KEY `question_id` (`question_id`),
  CONSTRAINT `exam_paper_question_ibfk_1` FOREIGN KEY (`paper_id`) REFERENCES `exam_paper` (`id`),
  CONSTRAINT `exam_paper_question_ibfk_2` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exam_paper_question`
--

LOCK TABLES `exam_paper_question` WRITE;
/*!40000 ALTER TABLE `exam_paper_question` DISABLE KEYS */;
INSERT INTO `exam_paper_question` VALUES (1,1,1,1,2.00,1),(2,1,2,1,2.00,2),(3,1,3,1,2.00,3),(4,1,4,1,2.00,4),(5,1,5,1,2.00,5),(6,1,31,1,2.00,6),(7,1,32,1,2.00,7),(8,1,33,1,2.00,8),(9,1,34,1,2.00,9),(10,1,35,1,2.00,10),(11,2,1,1,2.00,1),(12,2,2,1,2.00,2),(13,2,3,1,2.00,3),(14,2,4,1,2.00,4),(15,2,5,1,2.00,5),(16,2,31,1,2.00,6),(17,2,32,1,2.00,7),(18,2,33,1,2.00,8),(19,2,34,1,2.00,9),(20,2,35,1,2.00,10),(21,3,1,1,2.00,1),(22,3,2,1,2.00,2),(23,3,3,1,2.00,3),(24,3,4,1,2.00,4),(25,3,5,1,2.00,5),(26,3,31,1,2.00,6),(27,3,32,1,2.00,7),(28,3,33,1,2.00,8),(29,3,34,1,2.00,9),(30,3,35,1,2.00,10),(31,4,1,1,2.00,1),(32,4,2,1,2.00,2),(33,4,3,1,2.00,3),(34,4,4,1,2.00,4),(35,4,5,1,2.00,5),(36,5,1,1,2.00,1),(37,5,7,2,2.00,2),(38,5,11,3,2.00,3),(39,5,16,4,2.00,4),(40,5,21,5,2.00,5),(41,5,26,6,2.00,6);
/*!40000 ALTER TABLE `exam_paper_question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exam_student`
--

DROP TABLE IF EXISTS `exam_student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `exam_student` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `exam_course_id` bigint NOT NULL COMMENT '考试场次id（对应exam_course，也就是你之前的exam_id）',
  `student_id` bigint NOT NULL COMMENT '学生用户id',
  `stu_status` tinyint NOT NULL DEFAULT '0' COMMENT '考试状态：0-未参加 1-正在参加 2-已完成考试',
  `start_time` datetime DEFAULT NULL COMMENT '学生进入考试时间',
  `submit_time` datetime DEFAULT NULL COMMENT '试卷提交时间',
  `total_score` decimal(5,1) DEFAULT NULL COMMENT '本次考试总分',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_exam_student` (`exam_course_id`,`student_id`) COMMENT '一个学生一场考试仅允许一条记录'
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='学生考试主记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exam_student`
--

LOCK TABLES `exam_student` WRITE;
/*!40000 ALTER TABLE `exam_student` DISABLE KEYS */;
INSERT INTO `exam_student` VALUES (1,7,10,1,'2026-07-20 09:11:30',NULL,NULL,'2026-07-20 09:11:30','2026-07-20 09:11:30'),(2,8,10,1,'2026-07-20 10:46:34',NULL,NULL,'2026-07-20 10:46:33','2026-07-20 10:46:33'),(3,9,10,2,'2026-07-20 13:13:06','2026-07-20 14:45:51',NULL,'2026-07-20 13:13:06','2026-07-20 14:45:51'),(4,10,10,2,'2026-07-20 14:47:22','2026-07-20 14:47:34',NULL,'2026-07-20 14:47:22','2026-07-20 14:47:34'),(5,11,10,2,'2026-07-20 14:56:57','2026-07-20 15:52:50',NULL,'2026-07-20 14:56:57','2026-07-20 15:52:50'),(6,11,11,1,'2026-07-20 15:57:15',NULL,NULL,'2026-07-20 15:57:15','2026-07-20 15:57:15'),(7,12,10,1,'2026-07-21 08:26:46',NULL,NULL,'2026-07-21 08:26:46','2026-07-21 08:26:46');
/*!40000 ALTER TABLE `exam_student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exam_student_answer`
--

DROP TABLE IF EXISTS `exam_student_answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `exam_student_answer` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `exam_student_id` bigint NOT NULL COMMENT '关联exam_student主表id',
  `question_id` bigint NOT NULL COMMENT '题目id',
  `student_answer` text COMMENT '学生作答答案（选择题存选项，主观题存文本）',
  `student_draft` text COMMENT '草稿缓存（实时保存草稿使用）',
  `question_score` decimal(5,1) DEFAULT NULL COMMENT '本题得分',
  `is_correct` tinyint DEFAULT NULL COMMENT '是否答对 0错 1对，主观题可null等待批改',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_exam_question` (`exam_student_id`,`question_id`) COMMENT '同一场考试同一题目只能一条答题记录',
  KEY `idx_exam_student_id` (`exam_student_id`)
) ENGINE=InnoDB AUTO_INCREMENT=784 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='学生每题答题明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exam_student_answer`
--

LOCK TABLES `exam_student_answer` WRITE;
/*!40000 ALTER TABLE `exam_student_answer` DISABLE KEYS */;
INSERT INTO `exam_student_answer` VALUES (1,10,36,'111','C',0.0,0,'2026-07-20 10:06:59','2026-07-21 08:38:04'),(2,10,38,NULL,'true',NULL,NULL,'2026-07-20 10:06:59','2026-07-20 10:07:21'),(3,10,39,NULL,'123',NULL,NULL,'2026-07-20 10:16:52','2026-07-20 10:16:52'),(13,10,37,NULL,'A',NULL,NULL,'2026-07-20 13:45:04','2026-07-20 13:45:04'),(20,3,36,NULL,'A',NULL,NULL,'2026-07-20 14:29:39','2026-07-20 14:29:39'),(21,3,37,NULL,'A',NULL,NULL,'2026-07-20 14:29:39','2026-07-20 14:29:39'),(26,3,38,NULL,'true',NULL,NULL,'2026-07-20 14:31:33','2026-07-20 14:31:33'),(27,3,39,NULL,'sdsdds',NULL,NULL,'2026-07-20 14:31:33','2026-07-20 14:31:33'),(28,3,40,NULL,'sadsada',NULL,NULL,'2026-07-20 14:31:33','2026-07-20 14:31:33'),(29,3,41,NULL,'sdasdada',NULL,NULL,'2026-07-20 14:31:33','2026-07-20 14:31:33'),(180,4,36,NULL,'A',NULL,NULL,'2026-07-20 14:47:31','2026-07-20 14:47:31'),(181,4,37,NULL,'B',NULL,NULL,'2026-07-20 14:47:31','2026-07-20 14:47:31'),(182,4,38,NULL,'false',NULL,NULL,'2026-07-20 14:47:31','2026-07-20 14:47:31'),(183,4,39,NULL,'111',NULL,NULL,'2026-07-20 14:47:31','2026-07-20 14:47:31'),(184,4,40,NULL,'11',NULL,NULL,'2026-07-20 14:47:31','2026-07-20 14:47:31'),(185,4,41,NULL,'1',NULL,NULL,'2026-07-20 14:47:31','2026-07-20 14:47:31'),(192,5,36,NULL,'A',NULL,NULL,'2026-07-20 14:57:00','2026-07-20 14:57:00'),(193,5,37,NULL,'C',NULL,NULL,'2026-07-20 14:57:00','2026-07-20 14:57:00'),(214,5,38,'true',NULL,NULL,NULL,'2026-07-20 15:52:50','2026-07-20 15:52:50'),(215,5,39,'11',NULL,NULL,NULL,'2026-07-20 15:52:50','2026-07-20 15:52:50'),(216,5,40,' ds',NULL,NULL,NULL,'2026-07-20 15:52:50','2026-07-20 15:52:50'),(217,5,41,'ssad',NULL,NULL,NULL,'2026-07-20 15:52:50','2026-07-20 15:52:50'),(218,6,36,'A',NULL,0.0,0,'2026-07-20 16:00:44','2026-07-20 16:00:44'),(219,6,37,'B',NULL,0.0,0,'2026-07-20 16:00:44','2026-07-20 16:00:44'),(220,6,38,'false',NULL,0.0,0,'2026-07-20 16:00:44','2026-07-20 16:00:44'),(221,6,39,'adas',NULL,0.0,0,'2026-07-20 16:00:44','2026-07-20 16:00:44'),(222,6,40,'csad',NULL,0.0,0,'2026-07-20 16:00:44','2026-07-20 16:00:44'),(223,6,41,'sdsa',NULL,0.0,0,'2026-07-20 16:00:44','2026-07-20 16:00:44'),(386,7,36,'D',NULL,0.0,0,'2026-07-21 08:26:59','2026-07-21 08:39:51'),(387,7,37,'A',NULL,0.0,0,'2026-07-21 08:26:59','2026-07-21 08:26:59'),(388,7,41,'111',NULL,0.0,0,'2026-07-21 08:26:59','2026-07-21 08:26:59'),(390,7,38,'true',NULL,0.0,0,'2026-07-21 08:27:15','2026-07-21 08:27:15'),(493,7,1,'D',NULL,2.0,1,'2026-07-21 08:48:00','2026-07-21 09:20:08'),(494,7,11,'对',NULL,2.0,1,'2026-07-21 08:48:00','2026-07-21 09:23:15'),(627,7,7,'ABCD',NULL,2.0,1,'2026-07-21 09:21:59','2026-07-21 09:30:19'),(643,7,16,'有穷性',NULL,2.0,1,'2026-07-21 09:22:49','2026-07-21 09:22:49');
/*!40000 ALTER TABLE `exam_student_answer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '题目ID',
  `type` int NOT NULL COMMENT '题型(1-单选, 2-多选, 3-判断, 4-填空, 5-简答, 6-编程)',
  `chapter` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '所属章节',
  `difficulty` int NOT NULL COMMENT '难度(1-简单, 2-中等, 3-困难)',
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '题目内容',
  `options` text COLLATE utf8mb4_unicode_ci COMMENT '选项(JSON格式)',
  `answer` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '答案',
  `analysis` text COLLATE utf8mb4_unicode_ci COMMENT '答案解析',
  `score` decimal(5,2) NOT NULL COMMENT '分值',
  `tags` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标签(逗号分隔)',
  `creator_id` bigint NOT NULL COMMENT '创建者ID',
  `status` int NOT NULL DEFAULT '1' COMMENT '状态(0-禁用, 1-启用)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `creator_id` (`creator_id`),
  CONSTRAINT `question_ibfk_1` FOREIGN KEY (`creator_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
INSERT INTO `question` VALUES (1,1,'第一章 绪论',1,'数据结构研究的对象不包括？','[\"数据逻辑结构\",\"存储结构\",\"数据运算\",\"计算机硬件\"]','D','',2.00,'基础',4,0,'2026-07-06 01:00:00','2026-07-06 01:01:00'),(2,1,'第一章 绪论',1,'算法时间复杂度衡量标准是？','[\"代码行数\",\"执行指令数量\",\"运行秒数\",\"内存占用\"]','B','',2.00,'基础',4,0,'2026-07-06 01:02:00','2026-07-06 01:03:00'),(3,1,'第一章 绪论',2,'以下属于非线性结构的是？','[\"顺序表\",\"栈\",\"二叉树\",\"队列\"]','C','',2.00,'基础',4,0,'2026-07-06 01:04:00','2026-07-06 01:05:00'),(4,1,'第一章 绪论',2,'O(n²)代表什么复杂度？','[\"常数阶\",\"线性阶\",\"平方阶\",\"对数阶\"]','C','',2.00,'基础',4,0,'2026-07-06 01:06:00','2026-07-06 01:07:00'),(5,1,'第一章 绪论',3,'算法不具备的特性是？','[\"有穷性\",\"确定性\",\"无限性\",\"输出\"]','C','',2.00,'基础',4,0,'2026-07-06 01:08:00','2026-07-06 01:09:00'),(6,2,'第一章 绪论',1,'算法五大特性包含？','[\"输入\",\"输出\",\"有穷性\",\"随机性\"]','ABC','',2.00,'基础',4,0,'2026-07-06 01:10:00','2026-07-06 01:11:00'),(7,2,'第一章 绪论',2,'数据逻辑结构分为？','[\"集合\",\"线性\",\"树形\",\"图形\"]','ABCD','',2.00,'基础',4,0,'2026-07-06 01:12:00','2026-07-06 01:13:00'),(8,2,'第一章 绪论',2,'衡量算法优劣指标有？','[\"时间复杂度\",\"空间复杂度\",\"可读性\",\"代码长度\"]','ABC','',2.00,'基础',4,0,'2026-07-06 01:14:00','2026-07-06 01:15:00'),(9,2,'第一章 绪论',3,'属于线性结构的有？','[\"链表\",\"栈\",\"图\",\"串\"]','ABD','',2.00,'基础',4,0,'2026-07-06 01:16:00','2026-07-06 01:17:00'),(10,2,'第一章 绪论',3,'存储结构包含？','[\"顺序存储\",\"链式存储\",\"索引存储\",\"散列存储\"]','ABCD','',2.00,'基础',4,0,'2026-07-06 01:18:00','2026-07-06 01:19:00'),(11,3,'第一章 绪论',1,'数据元素是数据最小处理单位。','[\"对\",\"错\"]','对','',2.00,'基础',4,0,'2026-07-06 01:20:00','2026-07-06 01:21:00'),(12,3,'第一章 绪论',1,'递归算法会额外占用栈空间。','[\"对\",\"错\"]','对','',2.00,'基础',4,0,'2026-07-06 01:22:00','2026-07-06 01:23:00'),(13,3,'第一章 绪论',2,'O(1)代表线性时间复杂度。','[\"对\",\"错\"]','错','',2.00,'基础',4,0,'2026-07-06 01:24:00','2026-07-06 01:25:00'),(14,3,'第一章 绪论',2,'树属于线性数据结构。','[\"对\",\"错\"]','错','',2.00,'基础',4,0,'2026-07-06 01:26:00','2026-07-06 01:27:00'),(15,3,'第一章 绪论',3,'同一逻辑结构可采用多种存储结构。','[\"对\",\"错\"]','对','',2.00,'基础',4,0,'2026-07-06 01:28:00','2026-07-06 01:29:00'),(16,4,'第一章 绪论',1,'算法的____指执行步骤有限，不能无限循环。','[]','有穷性','',2.00,'基础',4,0,'2026-07-06 01:30:00','2026-07-06 01:31:00'),(17,4,'第一章 绪论',1,'数据结构两大核心：逻辑结构与____。','[]','存储结构','',2.00,'基础',4,0,'2026-07-06 01:32:00','2026-07-06 01:33:00'),(18,4,'第一章 绪论',2,'复杂度O(logn)称为____阶复杂度。','[]','对数','',2.00,'基础',4,0,'2026-07-06 01:34:00','2026-07-06 01:35:00'),(19,4,'第一章 绪论',2,'图、树属于____数据结构。','[]','非线性','',2.00,'基础',4,0,'2026-07-06 01:36:00','2026-07-06 01:37:00'),(20,4,'第一章 绪论',3,'散列存储依靠____映射数据位置。','[]','散列函数','',2.00,'基础',4,0,'2026-07-06 01:38:00','2026-07-06 01:39:00'),(21,5,'第一章 绪论',1,'简述算法五大特性。','[]','有穷性、确定性、输入、输出、可行性','',2.00,'基础',4,0,'2026-07-06 01:40:00','2026-07-06 01:41:00'),(22,5,'第一章 绪论',1,'简述逻辑结构四大分类。','[]','集合、线性、树形、图形','',2.00,'基础',4,0,'2026-07-06 01:42:00','2026-07-06 01:43:00'),(23,5,'第一章 绪论',2,'时间复杂度与空间复杂度区别？','[]','时间衡量运行指令，空间衡量内存占用','',2.00,'基础',4,0,'2026-07-06 01:44:00','2026-07-06 01:45:00'),(24,5,'第一章 绪论',2,'顺序存储和链式存储优缺点？','[]','顺序随机访问快、扩容开销大；链式插入删除快、无法随机访问','',2.00,'基础',4,0,'2026-07-06 01:46:00','2026-07-06 01:47:00'),(25,5,'第一章 绪论',3,'如何化简算法时间复杂度？','[]','只保留最高阶项，忽略常数与低次项','',2.00,'基础',4,0,'2026-07-06 01:48:00','2026-07-06 01:49:00'),(26,6,'第一章 绪论',1,'编写代码输出1~100所有整数。','[]','循环遍历打印','',2.00,'基础',4,0,'2026-07-06 01:50:00','2026-07-06 01:51:00'),(27,6,'第一章 绪论',1,'递归实现计算1到n累加和。','[]','递归终止n=0返回0，否则n+sum(n-1)','',2.00,'基础',4,0,'2026-07-06 01:52:00','2026-07-06 01:53:00'),(28,6,'第一章 绪论',2,'判断输入数字是否为素数。','[]','遍历2~sqrt(n)取模判断','',2.00,'基础',4,0,'2026-07-06 01:54:00','2026-07-06 01:55:00'),(29,6,'第一章 绪论',2,'统计数组中偶数个数。','[]','遍历数组判断取模2等于0计数','',2.00,'基础',4,0,'2026-07-06 01:56:00','2026-07-06 01:57:00'),(30,6,'第一章 绪论',3,'冒泡排序实现一维数组升序。','[]','两层循环相邻元素交换','',2.00,'基础',4,0,'2026-07-06 01:58:00','2026-07-06 01:59:00'),(31,1,'第二章 线性表',1,'顺序表随机访问时间复杂度为？','[\"O(1)\",\"O(n)\",\"O(logn)\",\"O(n²)\"]','A','',2.00,'基础',4,0,'2026-07-06 02:00:00','2026-07-06 02:01:00'),(32,1,'第二章 线性表',1,'单链表查找指定元素复杂度？','[\"O(1)\",\"O(n)\",\"O(logn)\",\"O(n²)\"]','B','',2.00,'基础',4,0,'2026-07-06 02:02:00','2026-07-06 02:03:00'),(33,1,'第二章 线性表',2,'适合频繁插入删除的存储结构？','[\"顺序表\",\"单链表\",\"数组\",\"静态数组\"]','B','',2.00,'基础',4,0,'2026-07-06 02:04:00','2026-07-06 02:05:00'),(34,1,'第二章 线性表',2,'双向链表相比单链表新增指针是？','[\"后继指针\",\"前驱指针\",\"头指针\",\"尾指针\"]','B','',2.00,'基础',4,0,'2026-07-06 02:06:00','2026-07-06 02:07:00'),(35,1,'第二章 线性表',3,'静态链表依靠什么模拟指针？','[\"内存地址\",\"游标下标\",\"索引值\",\"哈希值\"]','B','',2.00,'基础',4,0,'2026-07-06 02:08:00','2026-07-06 02:09:00'),(36,2,'第二章 线性表',1,'顺序表缺点包含？','[\"扩容拷贝开销大\",\"随机访问慢\",\"中间插入慢\",\"连续内存占用\"]','ACD','',2.00,'基础',4,0,'2026-07-06 02:10:00','2026-07-06 02:11:00'),(37,2,'第二章 线性表',2,'链表优势有？','[\"无需连续内存\",\"插入删除O(1)\",\"支持随机访问\",\"动态扩容\"]','ABD','',2.00,'基础',4,0,'2026-07-06 02:12:00','2026-07-06 02:13:00'),(38,2,'第二章 线性表',2,'循环链表特点？','[\"尾节点指向头节点\",\"可从尾直接访问头\",\"无空指针\",\"随机访问O(1)\"]','ABC','',2.00,'基础',4,0,'2026-07-06 02:14:00','2026-07-06 02:15:00'),(39,2,'第二章 线性表',3,'线性表存储结构分为？','[\"顺序存储\",\"链式存储\",\"索引存储\",\"树形存储\"]','ABC','',2.00,'基础',4,0,'2026-07-06 02:16:00','2026-07-06 02:17:00'),(40,2,'第二章 线性表',3,'带表头结点链表好处？','[\"统一空表非空表操作\",\"简化边界判断\",\"减少判空代码\",\"提升查找速度\"]','ABC','',2.00,'基础',4,0,'2026-07-06 02:18:00','2026-07-06 02:19:00'),(41,3,'第二章 线性表',1,'顺序表中间插入元素需要后移数据。','[\"对\",\"错\"]','对','',2.00,'基础',4,0,'2026-07-06 02:20:00','2026-07-06 02:21:00'),(42,3,'第二章 线性表',1,'单链表可以直接逆向遍历。','[\"对\",\"错\"]','错','',2.00,'基础',4,0,'2026-07-06 02:22:00','2026-07-06 02:23:00'),(43,3,'第二章 线性表',2,'循环链表尾指针可以快速获取表头。','[\"对\",\"错\"]','对','',2.00,'基础',4,0,'2026-07-06 02:24:00','2026-07-06 02:25:00'),(44,3,'第二章 线性表',2,'静态链表使用真实内存地址存储指针。','[\"对\",\"错\"]','错','',2.00,'基础',4,0,'2026-07-06 02:26:00','2026-07-06 02:27:00'),(45,3,'第二章 线性表',3,'双向链表删除节点不需要额外前驱遍历。','[\"对\",\"错\"]','对','',2.00,'基础',4,0,'2026-07-06 02:28:00','2026-07-06 02:29:00'),(46,4,'第二章 线性表',1,'顺序表依靠____内存空间存储数据。','[]','连续','',2.00,'基础',4,0,'2026-07-06 02:30:00','2026-07-06 02:31:00'),(47,4,'第二章 线性表',1,'单链表每个节点包含数据域和____域。','[]','指针','',2.00,'基础',4,0,'2026-07-06 02:32:00','2026-07-06 02:33:00'),(48,4,'第二章 线性表',2,'____链表尾节点next指向头结点。','[]','循环','',2.00,'基础',4,0,'2026-07-06 02:34:00','2026-07-06 02:35:00'),(49,4,'第二章 线性表',2,'双向链表节点拥有前驱指针与____指针。','[]','后继','',2.00,'基础',4,0,'2026-07-06 02:36:00','2026-07-06 02:37:00'),(50,4,'第二章 线性表',3,'静态链表使用____代替真实指针。','[]','游标','',2.00,'基础',4,0,'2026-07-06 02:38:00','2026-07-06 02:39:00'),(51,5,'第二章 线性表',1,'顺序表与单链表插入删除效率差异？','[]','顺序表中间插入需移动元素O(n)；链表找到节点后插入O(1)','',2.00,'基础',4,0,'2026-07-06 02:40:00','2026-07-06 02:41:00'),(52,5,'第二章 线性表',1,'简述头结点作用。','[]','统一空表与非空表操作，省去表头边界判断','',2.00,'基础',4,0,'2026-07-06 02:42:00','2026-07-06 02:43:00'),(53,5,'第二章 线性表',2,'循环链表尾指针优势？','[]','尾指针可一步访问表头与表尾，头尾操作更高效','',2.00,'基础',4,0,'2026-07-06 02:44:00','2026-07-06 02:45:00'),(54,5,'第二章 线性表',2,'双向链表相比单链表优缺点？','[]','可双向遍历删除简单；多存一份指针，内存开销更大','',2.00,'基础',4,0,'2026-07-06 02:46:00','2026-07-06 02:47:00'),(55,5,'第二章 线性表',3,'静态链表实现原理？','[]','数组存储节点，游标下标模拟指针，下标-1代表空指针','',2.00,'基础',4,0,'2026-07-06 02:48:00','2026-07-06 02:49:00'),(56,6,'第二章 线性表',1,'实现顺序表尾部插入函数。','[]','数组长度自增，新元素赋值到末尾下标','',2.00,'基础',4,0,'2026-07-06 02:50:00','2026-07-06 02:51:00'),(57,6,'第二章 线性表',1,'单链表头插法插入节点代码。','[]','新节点指向原头，更新头指针为新节点','',2.00,'基础',4,0,'2026-07-06 02:52:00','2026-07-06 02:53:00'),(58,6,'第二章 线性表',2,'遍历单链表输出全部元素。','[]','循环p=p.next直到p为空，打印每个节点数据','',2.00,'基础',4,0,'2026-07-06 02:54:00','2026-07-06 02:55:00'),(59,6,'第二章 线性表',2,'合并两个有序单链表。','[]','双指针比较，小节点接入结果链表','',2.00,'基础',4,0,'2026-07-06 02:56:00','2026-07-06 02:57:00'),(60,6,'第二章 线性表',3,'双向链表指定位置删除节点。','[]','修改前驱next与后继prev指针，释放当前节点','',2.00,'基础',4,0,'2026-07-06 02:58:00','2026-07-06 02:59:00');
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student_course`
--

DROP TABLE IF EXISTS `student_course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student_course` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `student_id` bigint NOT NULL COMMENT '学生ID',
  `course_id` bigint NOT NULL COMMENT '课程主表ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '选课时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_student_course` (`student_id`,`course_id`),
  KEY `idx_student_id` (`student_id`),
  KEY `idx_course_id` (`course_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='学生选课关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_course`
--

LOCK TABLES `student_course` WRITE;
/*!40000 ALTER TABLE `student_course` DISABLE KEYS */;
INSERT INTO `student_course` VALUES (1,10,5,'2026-07-06 01:46:20'),(2,10,7,'2026-07-06 23:01:31'),(3,10,6,'2026-07-20 09:00:16'),(4,11,5,'2026-07-20 15:57:01');
/*!40000 ALTER TABLE `student_course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码(BCrypt加密)',
  `real_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '真实姓名',
  `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `role` int NOT NULL DEFAULT '1' COMMENT '角色(0-管理员, 1-教师, 2-学生)',
  `status` int NOT NULL DEFAULT '1' COMMENT '状态(0-禁用, 1-启用)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin','2005','gyb','3531037807@qq.com','19700957099',0,1,'2026-07-03 20:24:20','2026-07-03 20:24:20'),(2,'111','123456',NULL,'123@ex',NULL,0,1,'2026-07-04 19:47:09','2026-07-04 19:47:09'),(3,'cj','2005',NULL,'cj@qq.com',NULL,0,1,'2026-07-04 20:06:17','2026-07-04 20:06:17'),(4,'gyb','2005',NULL,'gyb@qq',NULL,1,1,'2026-07-04 20:12:17','2026-07-04 20:12:17'),(5,'student1','student1',NULL,'student1@qq.com',NULL,2,1,'2026-07-05 23:20:02','2026-07-05 23:22:59'),(6,'student2','student2',NULL,'student2@qq.com',NULL,1,1,'2026-07-05 23:21:37','2026-07-05 23:21:37'),(7,'student3','student3',NULL,'student3@qq.com',NULL,0,1,'2026-07-05 23:23:34','2026-07-05 23:23:34'),(8,'student4','student4',NULL,'student4@qq',NULL,2,1,'2026-07-05 23:24:05','2026-07-05 23:24:05'),(9,'student31','student3',NULL,'student31@qq.com',NULL,0,1,'2026-07-05 23:25:32','2026-07-05 23:25:32'),(10,'gyb1','gyb1',NULL,'gyb1@qq',NULL,2,1,'2026-07-05 23:27:30','2026-07-05 23:27:30'),(11,'gyb2','2005',NULL,'35310378071@qq.com',NULL,2,1,'2026-07-20 15:56:36','2026-07-20 15:56:36');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-22 14:34:52
