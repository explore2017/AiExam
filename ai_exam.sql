/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50553
 Source Host           : localhost:3306
 Source Schema         : ai_exam

 Target Server Type    : MySQL
 Target Server Version : 50553
 File Encoding         : 65001

 Date: 12/04/2019 00:28:33
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for answer_record
-- ----------------------------
DROP TABLE IF EXISTS `answer_record`;
CREATE TABLE `answer_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `paper_id` int(11) NULL DEFAULT NULL,
  `question_id` int(11) NULL DEFAULT NULL,
  `answer` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '答案',
  `score` double(3, 2) NULL DEFAULT NULL COMMENT '得分',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for batch
-- ----------------------------
DROP TABLE IF EXISTS `batch`;
CREATE TABLE `batch`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `exam_id` int(11) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批次名称',
  `describe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批次描述',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `max_number` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '批次最大人数',
  `paper_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of batch
-- ----------------------------
INSERT INTO `batch` VALUES (1, 1, 'Java批次一', '描述', '2019-02-16 19:13:22', '2019-02-16 19:13:25', 40, NULL);
INSERT INTO `batch` VALUES (2, 1, 'Java批次二', '描述', '2019-02-16 19:13:44', '2019-02-16 19:13:47', 30, NULL);
INSERT INTO `batch` VALUES (3, 2, 'Linux批次一', '批次描述', '2019-02-17 17:44:02', '2019-02-17 17:44:04', 50, NULL);
INSERT INTO `batch` VALUES (4, 3, '考试批次一', '批次描述', '2019-02-17 17:44:32', '2019-02-17 17:44:35', 40, NULL);
INSERT INTO `batch` VALUES (11, 2, NULL, NULL, '2019-04-11 00:22:00', '2019-04-14 00:22:00', 2, 2);
INSERT INTO `batch` VALUES (12, 2, NULL, NULL, '2019-04-10 00:26:00', '2019-04-11 00:26:00', 2, 2);

-- ----------------------------
-- Table structure for batch_student
-- ----------------------------
DROP TABLE IF EXISTS `batch_student`;
CREATE TABLE `batch_student`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `batch_id` int(11) NULL DEFAULT NULL,
  `student_id` int(11) NULL DEFAULT NULL,
  `score` double(3, 2) NULL DEFAULT NULL,
  `status` int(11) NULL DEFAULT NULL,
  `start_time` datetime NULL DEFAULT NULL,
  `submit_time` datetime NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for class
-- ----------------------------
DROP TABLE IF EXISTS `class`;
CREATE TABLE `class`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '班级名称',
  `class_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '班级号',
  `teacher_id` int(11) NULL DEFAULT NULL COMMENT '老师id',
  `subject_id` int(11) NULL DEFAULT NULL COMMENT '所属科目号',
  `teacher_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '老师名称',
  `subject_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目名称',
  `number` int(11) NULL DEFAULT NULL COMMENT '班级人数',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of class
-- ----------------------------
INSERT INTO `class` VALUES (1, '班级1', '111111', 6, 1, '6556', '555', 2, NULL, '2019-03-25 13:36:43');
INSERT INTO `class` VALUES (3, 'demo555', '58787', 2, 1, '4554', 'java', 0, '2019-03-19 22:36:36', '2019-04-02 11:49:24');
INSERT INTO `class` VALUES (4, 'demo', NULL, 45, 1, '5445', 'java', 0, '2019-03-22 14:23:25', NULL);

-- ----------------------------
-- Table structure for exam
-- ----------------------------
DROP TABLE IF EXISTS `exam`;
CREATE TABLE `exam`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '考试名称',
  `paper_id` int(11) NULL DEFAULT NULL COMMENT '对应试卷id',
  `exam_type_id` int(255) NULL DEFAULT NULL COMMENT '类型',
  `subscribe` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `start_time` datetime NULL DEFAULT NULL COMMENT '考试报名开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '考试报名终止时间',
  `creator_id` int(11) NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `class_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of exam
-- ----------------------------
INSERT INTO `exam` VALUES (1, 'java考试', 1, 1, '其中考试', '2019-02-14 18:07:52', '2019-02-14 18:07:54', 1, '2019-02-14 18:07:57', '2019-02-17 17:34:50', 1);
INSERT INTO `exam` VALUES (2, 'linux考试', 2, 1, '期末考试', '2019-02-14 19:47:31', '2019-02-14 19:47:28', 1, '2019-02-14 19:47:25', '2019-02-17 17:34:52', 1);
INSERT INTO `exam` VALUES (3, '考试名称', 3, 1, '描述', '2019-02-15 15:39:11', '2019-02-15 15:39:13', 2, '2019-02-15 13:32:03', '2019-02-17 17:34:55', 1);

-- ----------------------------
-- Table structure for manager
-- ----------------------------
DROP TABLE IF EXISTS `manager`;
CREATE TABLE `manager`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `nick` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '别称',
  `role` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of manager
-- ----------------------------
INSERT INTO `manager` VALUES (1, 'admin', '123456', '超级管理员', 'admin');

-- ----------------------------
-- Table structure for paper
-- ----------------------------
DROP TABLE IF EXISTS `paper`;
CREATE TABLE `paper`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '试卷名称',
  `describe` varchar(2550) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '试卷描述',
  `status` int(11) UNSIGNED NULL DEFAULT 0 COMMENT '试卷状态',
  `difficulty` int(11) NULL DEFAULT NULL COMMENT '试卷难度',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `pass_score` double(3, 2) NULL DEFAULT 0 COMMENT '及格分数',
  `total_score` double(3, 2) NULL DEFAULT 0 COMMENT '总分',
  `need_time` int(11) NULL DEFAULT NULL COMMENT '考试时长（分钟）',
  `score` double(3, 2) NULL DEFAULT NULL COMMENT '试卷答案',
  `subject_id` int(11) NULL DEFAULT NULL COMMENT '科目id',
  `subject_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出题人',
  `usufruct` int(11) NULL DEFAULT 0 COMMENT '使用权',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 48 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of paper
-- ----------------------------
INSERT INTO `paper` VALUES (10, '试卷1', '55dasjinksasdaaaaaaaarewqeqeqweqwe qwdwsdasdased asd as fsd rferqwrwetwe tew trwerwer wr wer ewdadas asd asdwerqweqwknnnnnnnnnknknknknknknnonnnnnn uajsf asjd nhiuaosjdinou saiou dasi hdsahouiojk ehnaes doason djasojn daosuij', NULL, 0, NULL, '2019-04-04 01:08:31', 4, 24, 5, 34, 1, '1', NULL, 0);
INSERT INTO `paper` VALUES (11, '试卷1', '{\"singeNumber\":0,\"singeKeyPoint\":\"\",\"judgeNumber\":0,\"judgeKeyPoint\":\"\",\"multipleNumber\":0,\"multipleKeyPoint\":\"\",\"completionNumber\":0,\"completionKeyPoint\":\"\",\"shortNumber\":0,\"shortKeyPoint\":\"\",\"analysisNumber\":0}', 2, NULL, NULL, '2019-04-01 19:26:43', NULL, 111, NULL, NULL, 1, '1', NULL, 0);
INSERT INTO `paper` VALUES (12, '54', '5454', 0, NULL, NULL, '2019-04-01 19:28:04', NULL, 100, NULL, NULL, 787, '5', NULL, 0);
INSERT INTO `paper` VALUES (13, '4554', '4545', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 8778, '45', NULL, 0);
INSERT INTO `paper` VALUES (16, '5', '545', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 87, '54', NULL, 0);
INSERT INTO `paper` VALUES (17, '45545', '5455dasjinksasdaaaaaaaarewqeqeqweqwe qwdwsdasdased asd as fsd rferqwrwetwe tew trwerwer wr wer ewdadas asd asdwerqweqwknnnnnnnnnknknknknknknnonnnnnn uajsf asjd nhiuaosjdinou saiou dasi hdsahouiojk ehnaes doason djasojndaosuijSSSSSSSSSSSSSSSSSSSSSSSSSSSSS5', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, '54', NULL, 0);
INSERT INTO `paper` VALUES (18, '545', '4', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 78, '454', NULL, 1);
INSERT INTO `paper` VALUES (19, '45', '545', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 87, '45', NULL, 1);
INSERT INTO `paper` VALUES (20, '454', '545', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 8787, '54', NULL, 1);
INSERT INTO `paper` VALUES (21, '4554', '454', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 8787, '45', NULL, 1);
INSERT INTO `paper` VALUES (22, '5454', '5454', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 78787, '4545', NULL, NULL);
INSERT INTO `paper` VALUES (24, '54', '454554', NULL, 0, NULL, '2019-04-01 19:28:25', 5502, 102, 545, NULL, NULL, NULL, NULL, 0);
INSERT INTO `paper` VALUES (25, '打啥', '啊实打实', 0, NULL, NULL, NULL, 0, 0, NULL, NULL, NULL, '4545', NULL, NULL);
INSERT INTO `paper` VALUES (26, '1222', '44554', 0, 0, '2019-03-25 13:45:24', '2019-04-02 11:24:57', 60, 10000, 8787, 0, 2, NULL, '123', 1);
INSERT INTO `paper` VALUES (27, '15555', '445544554541115511aaaaaaa', 0, 1, '2019-03-25 13:46:22', '2019-04-02 11:58:03', 55034, 106, 87871, 6, 1, NULL, '123', 0);
INSERT INTO `paper` VALUES (28, NULL, NULL, 0, NULL, '2019-04-06 21:35:12', NULL, 0, 0, NULL, NULL, NULL, NULL, NULL, -1);
INSERT INTO `paper` VALUES (29, NULL, NULL, 0, NULL, '2019-04-06 21:35:20', NULL, 0, 0, NULL, NULL, NULL, NULL, NULL, -1);
INSERT INTO `paper` VALUES (41, NULL, '{\"singeNumber\":10,\"singeKeyPoint\":\"\",\"judgeNumber\":0,\"judgeKeyPoint\":\"\",\"multipleNumber\":0,\"multipleKeyPoint\":\"\",\"completionNumber\":0,\"completionKeyPoint\":\"\",\"shortNumber\":0,\"shortKeyPoint\":\"\",\"analysisNumber\":0}', 2, 0, '2019-04-08 11:59:56', NULL, NULL, 100, NULL, NULL, 1, NULL, NULL, 0);
INSERT INTO `paper` VALUES (42, NULL, '{\"singeNumber\":10,\"singeKeyPoint\":\"\",\"singescore\":3,\"judgeNumber\":0,\"judgeKeyPoint\":\"\",\"judgescore\":0,\"multipleNumber\":0,\"multipleKeyPoint\":\"\",\"multiplescore\":0,\"completionNumber\":0,\"completionKeyPoint\":\"\",\"completionscore\":0,\"shortNumber\":0,\"shortKeyPoint\":\"\",\"shortscore\":0,\"analysisNumber\":0,\"analysisKeyPoint\":\"\",\"analysisscore\":0}', 2, 0, '2019-04-09 14:32:04', NULL, NULL, 30, NULL, NULL, 1, NULL, NULL, 0);
INSERT INTO `paper` VALUES (43, NULL, '{\"singeNumber\":20,\"singeKeyPoint\":\"\",\"singescore\":5,\"judgeNumber\":0,\"judgeKeyPoint\":\"\",\"judgescore\":0,\"multipleNumber\":0,\"multipleKeyPoint\":\"\",\"multiplescore\":0,\"completionNumber\":0,\"completionKeyPoint\":\"\",\"completionscore\":0,\"shortNumber\":0,\"shortKeyPoint\":\"\",\"shortscore\":0,\"analysisNumber\":0,\"analysisKeyPoint\":\"\",\"analysisscore\":0}', 2, 0, '2019-04-09 14:36:17', NULL, NULL, 100, NULL, NULL, 1, NULL, NULL, 0);
INSERT INTO `paper` VALUES (44, NULL, '{\"singeNumber\":5,\"singeKeyPoint\":\"\",\"singescore\":5,\"judgeNumber\":1,\"judgeKeyPoint\":\"\",\"judgescore\":5,\"multipleNumber\":0,\"multipleKeyPoint\":\"\",\"multiplescore\":0,\"completionNumber\":0,\"completionKeyPoint\":\"\",\"completionscore\":0,\"shortNumber\":0,\"shortKeyPoint\":\"\",\"shortscore\":0,\"analysisNumber\":0,\"analysisKeyPoint\":\"\",\"analysisscore\":0}', 2, 0, '2019-04-09 19:12:46', NULL, NULL, 30, NULL, NULL, 1, NULL, NULL, 0);
INSERT INTO `paper` VALUES (45, NULL, '{\"singeNumber\":10,\"singeKeyPoint\":\"\",\"singescore\":5,\"judgeNumber\":2,\"judgeKeyPoint\":\"\",\"judgescore\":5,\"multipleNumber\":1,\"multipleKeyPoint\":\"\",\"multiplescore\":15,\"completionNumber\":0,\"completionKeyPoint\":\"\",\"completionscore\":0,\"shortNumber\":0,\"shortKeyPoint\":\"\",\"shortscore\":0,\"analysisNumber\":0,\"analysisKeyPoint\":\"\",\"analysisscore\":0}', 2, 0, '2019-04-09 19:23:16', NULL, NULL, 75, NULL, NULL, 1, NULL, NULL, 0);
INSERT INTO `paper` VALUES (46, NULL, '{\"singeNumber\":10,\"singeKeyPoint\":\"\",\"singescore\":10,\"judgeNumber\":0,\"judgeKeyPoint\":\"\",\"judgescore\":0,\"multipleNumber\":0,\"multipleKeyPoint\":\"\",\"multiplescore\":0,\"completionNumber\":0,\"completionKeyPoint\":\"\",\"completionscore\":0,\"shortNumber\":0,\"shortKeyPoint\":\"\",\"shortscore\":0,\"analysisNumber\":0,\"analysisKeyPoint\":\"\",\"analysisscore\":0}', 2, 0, '2019-04-09 20:04:39', NULL, NULL, 100, NULL, NULL, 1, NULL, NULL, 0);
INSERT INTO `paper` VALUES (47, NULL, '{\"singeNumber\":0,\"singeKeyPoint\":\"\",\"singescore\":0,\"judgeNumber\":0,\"judgeKeyPoint\":\"\",\"judgescore\":0,\"multipleNumber\":0,\"multipleKeyPoint\":\"\",\"multiplescore\":0,\"completionNumber\":0,\"completionKeyPoint\":\"\",\"completionscore\":0,\"shortNumber\":0,\"shortKeyPoint\":\"\",\"shortscore\":0,\"analysisNumber\":0,\"analysisKeyPoint\":\"\",\"analysisscore\":0}', 2, 0, '2019-04-11 12:32:05', NULL, NULL, 0, NULL, NULL, 1, NULL, NULL, -1);

-- ----------------------------
-- Table structure for paper_compose
-- ----------------------------
DROP TABLE IF EXISTS `paper_compose`;
CREATE TABLE `paper_compose`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `paper_id` int(11) NULL DEFAULT NULL COMMENT '试卷id',
  `question_id` int(11) NULL DEFAULT NULL COMMENT '试题id',
  `question_type_id` int(11) NULL DEFAULT NULL COMMENT '大题id',
  `question_num` int(11) NULL DEFAULT NULL COMMENT '操作数',
  `sequence` int(255) NULL DEFAULT NULL COMMENT '排序',
  `single_score` double(255, 0) UNSIGNED NULL DEFAULT 0 COMMENT '每小题分数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 86 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of paper_compose
-- ----------------------------
INSERT INTO `paper_compose` VALUES (75, 10, 35, NULL, NULL, 2, 9);
INSERT INTO `paper_compose` VALUES (79, 10, 41, NULL, NULL, 0, 4);
INSERT INTO `paper_compose` VALUES (80, 10, 42, NULL, NULL, 1, 12);
INSERT INTO `paper_compose` VALUES (81, 10, 43, NULL, NULL, 3, 3);
INSERT INTO `paper_compose` VALUES (82, 10, 40, NULL, NULL, 4, 3);
INSERT INTO `paper_compose` VALUES (83, 27, 35, NULL, NULL, 0, 3);
INSERT INTO `paper_compose` VALUES (84, 27, 37, NULL, NULL, 1, 3);
INSERT INTO `paper_compose` VALUES (85, 10, 46, NULL, NULL, 5, 3);

-- ----------------------------
-- Table structure for question
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(3000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '题目标题',
  `content` varchar(3000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '题目内容',
  `question_type_id` int(255) NULL DEFAULT NULL COMMENT '题目类型',
  `answer` varchar(3000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '题目答案',
  `selects` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '选项',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `is_subjective` int(255) NULL DEFAULT NULL COMMENT '1-主观题，0-客观题',
  `difficulty` int(255) NULL DEFAULT NULL COMMENT '题目难度',
  `subject_id` int(11) NULL DEFAULT NULL COMMENT '题目对应的科目',
  `status` int(255) NULL DEFAULT 1 COMMENT '题目状态1-默认开启  0-关闭',
  `key_point` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '知识点',
  `default_score` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '默认分数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 56 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of question
-- ----------------------------
INSERT INTO `question` VALUES (35, '???', '??????????????????????????????????????????? ??????????????\n????????', 0, 'C', '???&&&????&&&???AAS&&&??? ???a', '2019-04-02 17:00:26', '2019-04-02 17:00:26', 1, 0, 1, NULL, 'wqeq', '3');
INSERT INTO `question` VALUES (36, '???', '??????????????????????????????????????????????? ??????????????as', 2, 'B,C,D', '??????das&&&?????d&&&????das&&&???? ?????', '2019-04-02 17:00:46', '2019-04-02 17:00:46', 1, 1, 2, NULL, '44', '3');
INSERT INTO `question` VALUES (37, '???', '?????????????????????????????? ???????????', 1, '0', NULL, '2019-04-02 17:01:03', '2019-04-02 17:01:03', 1, 0, 1, NULL, '44', '3');
INSERT INTO `question` VALUES (38, '???', '????????????????????????????????????', 3, NULL, NULL, '2019-04-02 17:01:09', '2019-04-02 17:01:09', 0, 1, 2, NULL, '21231', '3');
INSERT INTO `question` VALUES (39, '???', '??????????????????????????????????????????', 3, NULL, NULL, '2019-04-02 17:01:14', '2019-04-02 17:01:14', 0, 1, 1, NULL, '21231', '3');
INSERT INTO `question` VALUES (40, '???', '??????????????????????????????????????????', 3, NULL, NULL, '2019-04-02 17:01:14', '2019-04-02 17:01:14', 0, 1, 1, NULL, '21231', '3');
INSERT INTO `question` VALUES (41, '???', '???sad???????????sad???????????sad???????????sad???????????sad???????????sad???????????sad???????????sad???????????sad???????????sad???????????sad???????????sad???????????sad???????????sad????????', 4, NULL, NULL, '2019-04-02 17:01:22', '2019-04-02 17:01:22', 0, 1, 1, NULL, 'wqeq', '3');
INSERT INTO `question` VALUES (42, '???', 'i;mki;mki;mki;mki;mki;mki;mki;mk;mikmk???????as????????????????????i;mki;mki;mki;mki;mki;mki;mki;mk;mikmk???????as????????????????????i;mki;mki;mki;mki;mki;mki;mki;mk;mikmk???????as????????????????????i;mki;mki;mki;mki;mki;mki;mki;mk;mikmk???????as????????????????????i;mki;mki;mki;mki;mki;mki;mki;mk;mikmk???????as????????????????????', 5, NULL, NULL, '2019-04-02 17:01:37', '2019-04-02 17:01:37', 0, 0, 1, NULL, '44', '3');
INSERT INTO `question` VALUES (43, '???', '???', 5, NULL, NULL, '2019-04-02 17:42:27', '2019-04-02 17:42:27', 0, 2, 1, NULL, '44', '3');
INSERT INTO `question` VALUES (44, '???', '1111', 0, 'A', '1&&&1&&&1&&&1', '2019-04-08 11:52:09', '2019-04-08 11:52:09', 1, 0, 1, NULL, '44', '3');
INSERT INTO `question` VALUES (45, '???', '2222', 0, 'A', '1&&&1&&&1&&&1', '2019-04-08 11:52:11', '2019-04-08 11:52:11', 1, 0, 1, NULL, '44', '3');
INSERT INTO `question` VALUES (46, '???', '3333', 0, 'A', '1&&&1&&&1&&&1', '2019-04-08 11:52:14', '2019-04-08 11:52:14', 1, 0, 1, NULL, '44', '3');
INSERT INTO `question` VALUES (47, '???', '4444', 0, 'A', '1&&&1&&&1&&&1', '2019-04-08 11:52:18', '2019-04-08 11:52:18', 1, 0, 1, NULL, '44', '3');
INSERT INTO `question` VALUES (48, '???', '5555', 0, 'A', '1&&&1&&&1&&&1', '2019-04-08 11:52:22', '2019-04-08 11:52:22', 1, 1, 1, NULL, '44', '3');
INSERT INTO `question` VALUES (49, '???', '6666', 0, 'A', '1&&&1&&&1&&&1', '2019-04-08 11:52:28', '2019-04-08 11:52:28', 1, 1, 1, NULL, '44', '3');
INSERT INTO `question` VALUES (50, '???', '7777', 0, 'A', '1&&&1&&&1&&&1', '2019-04-08 11:52:32', '2019-04-08 11:52:32', 1, 1, 1, NULL, '44', '3');
INSERT INTO `question` VALUES (51, '???', '8888', 0, 'A', '1&&&1&&&1&&&1', '2019-04-08 11:52:35', '2019-04-08 11:52:35', 1, 1, 1, NULL, '44', '3');
INSERT INTO `question` VALUES (52, '???', '9999', 0, 'A', '1&&&1&&&1&&&1', '2019-04-08 11:52:39', '2019-04-08 11:52:39', 1, 2, 1, NULL, '44', '3');
INSERT INTO `question` VALUES (53, '???', '1010', 0, 'A', '1&&&1&&&1&&&1', '2019-04-08 11:52:43', '2019-04-08 11:52:43', 1, 2, 1, NULL, '44', '3');
INSERT INTO `question` VALUES (54, '???', '11115', 0, 'A', '1&&&1&&&1&&&1', '2019-04-08 11:52:46', '2019-04-08 11:52:46', 1, 2, 1, NULL, '44', '3');
INSERT INTO `question` VALUES (55, '???', '1212', 0, 'A', '1&&&1&&&1&&&1', '2019-04-08 11:52:49', '2019-04-08 11:52:49', 1, 2, 1, NULL, '44', '3');

-- ----------------------------
-- Table structure for question_paper
-- ----------------------------
DROP TABLE IF EXISTS `question_paper`;
CREATE TABLE `question_paper`  (
  `paper_id` int(11) NOT NULL,
  `question_id` int(255) NOT NULL,
  `sequence` int(255) NULL DEFAULT NULL COMMENT '试题在试卷中的序号',
  PRIMARY KEY (`paper_id`, `question_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of question_paper
-- ----------------------------
INSERT INTO `question_paper` VALUES (1, 1, NULL);
INSERT INTO `question_paper` VALUES (1, 2, NULL);
INSERT INTO `question_paper` VALUES (1, 3, NULL);
INSERT INTO `question_paper` VALUES (2, 1, NULL);
INSERT INTO `question_paper` VALUES (2, 3, NULL);
INSERT INTO `question_paper` VALUES (3, 3, NULL);
INSERT INTO `question_paper` VALUES (3, 4, NULL);

-- ----------------------------
-- Table structure for question_type
-- ----------------------------
DROP TABLE IF EXISTS `question_type`;
CREATE TABLE `question_type`  (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '试题类型',
  `is_subjective` int(255) NULL DEFAULT NULL COMMENT '0-非主观题 1-主观题',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of question_type
-- ----------------------------
INSERT INTO `question_type` VALUES (1, '选择题', 0);
INSERT INTO `question_type` VALUES (2, '多选题', 2);
INSERT INTO `question_type` VALUES (3, '填空题', 3);
INSERT INTO `question_type` VALUES (4, '判断题', 1);
INSERT INTO `question_type` VALUES (5, '简答题', 4);
INSERT INTO `question_type` VALUES (6, '分析题', 5);

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sno` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `classes` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES (1, '1600300818', '123456', '2016', '10086', '794409767@qq.com', NULL, '2019-03-25 13:44:07', '3');
INSERT INTO `student` VALUES (2, '123', '123', '222', '222', '222', NULL, NULL, '5554,web1712');
INSERT INTO `student` VALUES (11, '170030061611', '123456', '??', NULL, NULL, '2019-03-18 22:11:38', '2019-03-25 13:44:17', '3');
INSERT INTO `student` VALUES (12, '17003006161112', '123456', '??', NULL, NULL, '2019-03-18 22:14:43', NULL, 'java,前端');
INSERT INTO `student` VALUES (21, 'q1234561', '123456', 'demo', NULL, NULL, '2019-03-19 13:11:41', NULL, '2,1');
INSERT INTO `student` VALUES (22, 'q1234561111', '123456', 'demo', NULL, NULL, '2019-03-19 13:33:13', NULL, '2,1');
INSERT INTO `student` VALUES (23, '1700300616', '123456', '54545', NULL, NULL, '2019-03-19 13:48:09', NULL, '111');
INSERT INTO `student` VALUES (24, '', '', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (26, '', '', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (27, '', '', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (28, '', '', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (29, '', '', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (30, '', '', NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for student_class
-- ----------------------------
DROP TABLE IF EXISTS `student_class`;
CREATE TABLE `student_class`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `student_id` int(11) NOT NULL,
  `class_id` int(11) NOT NULL,
  `class_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of student_class
-- ----------------------------
INSERT INTO `student_class` VALUES (4, 1, 1, '455454');
INSERT INTO `student_class` VALUES (5, 22, 3, NULL);
INSERT INTO `student_class` VALUES (7, 1, 4, NULL);
INSERT INTO `student_class` VALUES (8, 1, 6, NULL);
INSERT INTO `student_class` VALUES (10, 2, 1, '111');
INSERT INTO `student_class` VALUES (12, 1, 3, 'demo555');
INSERT INTO `student_class` VALUES (13, 11, 3, 'demo555');

-- ----------------------------
-- Table structure for subject
-- ----------------------------
DROP TABLE IF EXISTS `subject`;
CREATE TABLE `subject`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '课程名称',
  `subject_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '课程号(预留)',
  `subject_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '课程代码(预留)',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of subject
-- ----------------------------
INSERT INTO `subject` VALUES (1, 'java', '123', 'EXE123', '2019-02-15 15:38:34', '2019-02-15 15:38:37', NULL);
INSERT INTO `subject` VALUES (2, '前端', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `subject` VALUES (3, '555555', NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `subject_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属科目(可以多个)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 59 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES (6, '111', '123456', '?', NULL, NULL, '2019-03-14 19:26:02', '2019-03-17 14:50:09', 'java,前端');
INSERT INTO `teacher` VALUES (13, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (14, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (16, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (17, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (19, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (20, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (21, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (22, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (23, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (24, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (25, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (26, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (27, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (28, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (29, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (30, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (31, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (32, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (37, '545455555555', '123456', '4554', NULL, NULL, '2019-03-15 19:28:30', NULL, NULL);
INSERT INTO `teacher` VALUES (38, 'admin655454', '123456', 'demo', NULL, NULL, '2019-03-15 21:04:37', NULL, '');
INSERT INTO `teacher` VALUES (39, 'admin6554546545654', '123456', 'demo', NULL, NULL, '2019-03-15 21:07:34', NULL, '');
INSERT INTO `teacher` VALUES (43, 'admin65545465456545454545', '123456', 'demo', NULL, NULL, '2019-03-15 21:16:22', NULL, '');
INSERT INTO `teacher` VALUES (44, 'tuanwei555', '123456', '5454', NULL, NULL, '2019-03-15 21:17:41', '2019-03-17 19:55:13', 'java,前端');
INSERT INTO `teacher` VALUES (45, 'admin54554', '123456', '5445', NULL, NULL, '2019-03-15 21:18:01', '2019-03-17 20:00:55', 'java');
INSERT INTO `teacher` VALUES (46, 'string', 'string', 'string', 'string', 'string', '2019-03-16 17:14:18', '2019-03-17 19:57:25', 'java,前端');
INSERT INTO `teacher` VALUES (47, NULL, NULL, NULL, NULL, 'string', '2019-03-16 17:14:34', NULL, NULL);
INSERT INTO `teacher` VALUES (48, NULL, NULL, NULL, NULL, 'string', '2019-03-16 17:16:09', NULL, NULL);
INSERT INTO `teacher` VALUES (49, '7888888888888', '123456', '8788888888', NULL, NULL, '2019-03-16 17:58:48', NULL, 'java,??');
INSERT INTO `teacher` VALUES (53, 'q123456??', '123456', '??', NULL, NULL, '2019-03-16 18:48:03', NULL, '');
INSERT INTO `teacher` VALUES (55, 'guest6+6+', '123456', 'demo26', NULL, NULL, '2019-03-17 14:47:09', '2019-03-17 14:49:10', 'java,前端');
INSERT INTO `teacher` VALUES (56, 'q12345666568', '123456', '6889', NULL, NULL, '2019-03-17 20:34:09', NULL, '前端,java');
INSERT INTO `teacher` VALUES (57, 'admin11111111', '123456', '4554', NULL, NULL, '2019-03-19 13:28:54', NULL, '2');
INSERT INTO `teacher` VALUES (58, 'q1234562222222', '123456', '22222', NULL, NULL, '2019-03-19 13:33:23', NULL, '1');

-- ----------------------------
-- Table structure for teacher_subject
-- ----------------------------
DROP TABLE IF EXISTS `teacher_subject`;
CREATE TABLE `teacher_subject`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `teacher_id` int(11) NOT NULL,
  `subject_id` int(11) NOT NULL,
  `subject_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 49 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of teacher_subject
-- ----------------------------
INSERT INTO `teacher_subject` VALUES (21, 55, 1, '55555');
INSERT INTO `teacher_subject` VALUES (22, 55, 2, NULL);
INSERT INTO `teacher_subject` VALUES (23, 6, 1, '5555555');
INSERT INTO `teacher_subject` VALUES (24, 6, 2, NULL);
INSERT INTO `teacher_subject` VALUES (32, 44, 1, NULL);
INSERT INTO `teacher_subject` VALUES (33, 44, 2, NULL);
INSERT INTO `teacher_subject` VALUES (36, 46, 1, NULL);
INSERT INTO `teacher_subject` VALUES (37, 46, 2, NULL);
INSERT INTO `teacher_subject` VALUES (40, 45, 1, NULL);
INSERT INTO `teacher_subject` VALUES (42, 56, 2, NULL);
INSERT INTO `teacher_subject` VALUES (43, 56, 1, NULL);
INSERT INTO `teacher_subject` VALUES (47, 57, 2, NULL);
INSERT INTO `teacher_subject` VALUES (48, 58, 1, NULL);

-- ----------------------------
-- Table structure for paper_record
-- ----------------------------
DROP TABLE IF EXISTS `paper_record`;
CREATE TABLE `paper_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `batch_id` int(11) NULL DEFAULT NULL,
  `student_id` int(11) NULL DEFAULT NULL,
  `sequence` int(11) NULL DEFAULT NULL,
  `single_score` double(3, 2) NULL DEFAULT NULL,
  `score` double(3, 2) NULL DEFAULT NULL COMMENT '分数',
  `question_id` int(11) NULL DEFAULT NULL,
  `reply` varchar(5000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '填写的答案',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
