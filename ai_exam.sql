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

 Date: 26/03/2019 22:18:50
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
  `score` double(255, 0) NULL DEFAULT NULL COMMENT '得分',
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
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of batch
-- ----------------------------
INSERT INTO `batch` VALUES (1, 1, 'Java批次一', '描述', '2019-02-16 19:13:22', '2019-02-16 19:13:25');
INSERT INTO `batch` VALUES (2, 1, 'Java批次二', '描述', '2019-02-16 19:13:44', '2019-02-16 19:13:47');
INSERT INTO `batch` VALUES (3, 2, 'Linux批次一', '批次描述', '2019-02-17 17:44:02', '2019-02-17 17:44:04');
INSERT INTO `batch` VALUES (4, 3, '考试批次一', '批次描述', '2019-02-17 17:44:32', '2019-02-17 17:44:35');

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
) ENGINE = MyISAM AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of class
-- ----------------------------
INSERT INTO `class` VALUES (1, '11122', '2', 1, 1, '6556', '555', 2, NULL, '2019-03-25 13:36:43');
INSERT INTO `class` VALUES (3, 'demo555', '58787', 2, 1, '4554', 'java', 0, '2019-03-19 22:36:36', NULL);
INSERT INTO `class` VALUES (4, 'demo', NULL, 45, 1, '5445', 'java', 0, '2019-03-22 14:23:25', NULL);

-- ----------------------------
-- Table structure for exam
-- ----------------------------
DROP TABLE IF EXISTS `exam`;
CREATE TABLE `exam`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '考试名称',
  `subject_id` int(11) NULL DEFAULT NULL COMMENT '对应科目id',
  `exam_type_id` int(255) NULL DEFAULT NULL COMMENT '类型',
  `subscribe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `start_time` datetime NULL DEFAULT NULL COMMENT '考试开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '考试终止时间',
  `creator_id` int(11) NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of exam
-- ----------------------------
INSERT INTO `exam` VALUES (1, 'java考试', 1, 1, '其中考试', '2019-02-14 18:07:52', '2019-02-14 18:07:54', 1, '2019-02-14 18:07:57', '2019-02-17 17:34:50');
INSERT INTO `exam` VALUES (2, 'linux考试', 2, 1, '期末考试', '2019-02-14 19:47:31', '2019-02-14 19:47:28', 1, '2019-02-14 19:47:25', '2019-02-17 17:34:52');
INSERT INTO `exam` VALUES (3, '考试名称', 3, 1, '描述', '2019-02-15 15:39:11', '2019-02-15 15:39:13', 2, '2019-02-15 13:32:03', '2019-02-17 17:34:55');

-- ----------------------------
-- Table structure for exam_student
-- ----------------------------
DROP TABLE IF EXISTS `exam_student`;
CREATE TABLE `exam_student`  (
  `exam_id` int(11) NOT NULL,
  `student_id` int(11) NOT NULL,
  `batch_id` int(11) NULL DEFAULT NULL,
  `status` int(255) NULL DEFAULT NULL,
  `start_time` datetime NULL DEFAULT NULL,
  `end_time` datetime NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `score` double(255, 0) NULL DEFAULT NULL,
  PRIMARY KEY (`exam_id`, `student_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of exam_student
-- ----------------------------
INSERT INTO `exam_student` VALUES (1, 1, 2, 0, NULL, NULL, '2019-02-18 13:52:53', 0);
INSERT INTO `exam_student` VALUES (2, 1, 3, 3, NULL, NULL, '2019-02-18 15:53:32', NULL);

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
  `describe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '试卷描述',
  `status` int(11) UNSIGNED NULL DEFAULT 0 COMMENT '试卷状态',
  `difficulty` int(11) NULL DEFAULT NULL COMMENT '试卷难度',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `pass_score` double(255, 0) NULL DEFAULT 0 COMMENT '及格分数',
  `total_score` double(255, 0) NULL DEFAULT 0 COMMENT '总分',
  `need_time` int(11) NULL DEFAULT NULL COMMENT '考试时长（分钟）',
  `answer` varchar(3000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '试卷答案',
  `subject_id` int(11) NULL DEFAULT NULL COMMENT '科目id',
  `subject_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出题人',
  `usufruct` int(11) NULL DEFAULT NULL COMMENT '使用权',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of paper
-- ----------------------------
INSERT INTO `paper` VALUES (8, '试卷1', '试卷描述', 0, 1, '2019-02-16 17:26:01', '2019-02-16 17:26:01', 60, 100, 120, 'A B C D', 8787, '4554', NULL, NULL);
INSERT INTO `paper` VALUES (10, '64554', '55dasjinksasdaaaaaaaarewqeqeqweqwe qwdwsdasdased asd as fsd rferqwrwetwe tew trwerwer wr wer ewdadas asd asdwerqweqwknnnnnnnnnknknknknknknnonnnnnn uajsf asjd nhiuaosjdinou saiou dasi hdsahouiojk ehnaes doason djasojn daosuij', NULL, NULL, NULL, '2019-02-27 16:52:12', NULL, NULL, NULL, NULL, 87, '545', NULL, NULL);
INSERT INTO `paper` VALUES (11, '545', '4554', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 8787, '4', NULL, NULL);
INSERT INTO `paper` VALUES (12, '54', '5454', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 787, '5', NULL, NULL);
INSERT INTO `paper` VALUES (13, '4554', '4545', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 8778, '45', NULL, NULL);
INSERT INTO `paper` VALUES (16, '5', '545', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 87, '54', NULL, NULL);
INSERT INTO `paper` VALUES (17, '45545', '54', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 8787, '54', NULL, NULL);
INSERT INTO `paper` VALUES (18, '545', '4', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 78, '454', NULL, NULL);
INSERT INTO `paper` VALUES (19, '45', '545', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 87, '45', NULL, NULL);
INSERT INTO `paper` VALUES (20, '454', '545', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 8787, '54', NULL, NULL);
INSERT INTO `paper` VALUES (21, '4554', '454', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 8787, '45', NULL, NULL);
INSERT INTO `paper` VALUES (22, '5454', '5454', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 78787, '4545', NULL, NULL);
INSERT INTO `paper` VALUES (24, '1', '454554', NULL, 0, NULL, '2019-03-25 13:44:57', 5502, 1002111, 545, NULL, NULL, NULL, NULL, 0);
INSERT INTO `paper` VALUES (25, '打啥', '啊实打实', 0, NULL, NULL, NULL, 0, 0, NULL, NULL, NULL, '4545', NULL, NULL);
INSERT INTO `paper` VALUES (26, '54/8778', '44554', 0, 0, '2019-03-25 13:45:24', '2019-03-25 13:45:24', 60, 100, 8787, NULL, 1, NULL, '123', 0);
INSERT INTO `paper` VALUES (27, '1222', '44554455454111', 0, 1, '2019-03-25 13:46:22', '2019-03-25 18:45:23', 55034, 1002111, 87871, NULL, 1, NULL, '123', 0);

-- ----------------------------
-- Table structure for paper_compose
-- ----------------------------
DROP TABLE IF EXISTS `paper_compose`;
CREATE TABLE `paper_compose`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `paper_id` int(11) NULL DEFAULT NULL COMMENT '试卷id',
  `question_id` int(11) NULL DEFAULT NULL COMMENT '试题id',
  `question_type_id` int(11) NULL DEFAULT NULL COMMENT '大题id',
  `question_num` int(11) NULL DEFAULT NULL COMMENT '该大题下对应小题数',
  `sequence` int(255) NULL DEFAULT NULL COMMENT '排序',
  `single_score` double(255, 0) NULL DEFAULT NULL COMMENT '每小题分数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of paper_compose
-- ----------------------------
INSERT INTO `paper_compose` VALUES (1, 5, 6, NULL, NULL, 0, NULL);
INSERT INTO `paper_compose` VALUES (7, 105, 5, NULL, NULL, 1, 555);
INSERT INTO `paper_compose` VALUES (10, 105, 2, NULL, NULL, 2, NULL);
INSERT INTO `paper_compose` VALUES (12, 10, 30, NULL, NULL, 4, NULL);
INSERT INTO `paper_compose` VALUES (13, 10, 31, 1, NULL, 5, 3);
INSERT INTO `paper_compose` VALUES (14, 10, 28, 1, NULL, 1, 12);
INSERT INTO `paper_compose` VALUES (15, 10, 27, 1, NULL, 2, 3);
INSERT INTO `paper_compose` VALUES (30, 10, 29, 1, NULL, 3, 3);

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
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of question
-- ----------------------------
INSERT INTO `question` VALUES (2, '5555', '5454', NULL, NULL, NULL, NULL, '2019-02-21 23:13:38', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `question` VALUES (3, '公共题目', 'A int B double C char D hello', 1, 'A', NULL, '2019-02-15 12:40:24', '2019-02-15 12:40:26', 1, 1, 1, 1, NULL, NULL);
INSERT INTO `question` VALUES (4, '6', '545454', 1, NULL, NULL, NULL, '2019-02-27 17:28:10', NULL, NULL, 1, NULL, NULL, '999');
INSERT INTO `question` VALUES (5, '试题名称', '试题内容', 1, 'A', NULL, '2019-02-16 17:40:56', '2019-02-16 17:40:56', 0, 1, 1, 1, NULL, NULL);
INSERT INTO `question` VALUES (6, '试题名称', '试题内容', 1, 'A', NULL, '2019-02-16 17:41:44', '2019-02-16 17:41:44', 0, 1, 1, 1, '知识点', NULL);
INSERT INTO `question` VALUES (7, '6', '54', NULL, NULL, NULL, NULL, '2019-02-27 17:26:38', NULL, NULL, NULL, NULL, NULL, '999');
INSERT INTO `question` VALUES (8, NULL, '455454', NULL, NULL, NULL, '2019-03-10 18:49:03', '2019-03-10 18:49:03', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `question` VALUES (9, NULL, NULL, NULL, NULL, NULL, '2019-03-10 18:59:36', '2019-03-10 18:59:36', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `question` VALUES (10, NULL, NULL, NULL, NULL, NULL, '2019-03-10 18:59:38', '2019-03-10 18:59:38', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `question` VALUES (11, NULL, NULL, NULL, NULL, NULL, '2019-03-10 19:07:16', '2019-03-10 19:07:16', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `question` VALUES (12, NULL, NULL, NULL, NULL, NULL, '2019-03-10 19:14:10', '2019-03-10 19:14:10', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `question` VALUES (13, NULL, '???', 0, 'B', NULL, '2019-03-10 19:25:07', '2019-03-10 19:25:07', 1, 0, 1, NULL, '????', '3');
INSERT INTO `question` VALUES (14, NULL, '??', 0, 'C', NULL, '2019-03-10 19:27:23', '2019-03-10 19:27:23', 1, 0, 1, NULL, '?', '3');
INSERT INTO `question` VALUES (15, NULL, '???q', 0, 'D', NULL, '2019-03-10 19:28:34', '2019-03-10 19:28:34', 1, 1, 1, NULL, '44', '3');
INSERT INTO `question` VALUES (16, NULL, 'undefined', 0, 'C', NULL, '2019-03-13 19:51:53', '2019-03-13 19:51:53', 1, 1, 1, NULL, 'wqeq', '3');
INSERT INTO `question` VALUES (17, NULL, 'undefined', 1, '1', NULL, '2019-03-13 20:36:47', '2019-03-13 20:36:47', 1, 1, 2, NULL, 'wqeq', '3');
INSERT INTO `question` VALUES (18, NULL, '857887', 1, '1', NULL, '2019-03-13 20:37:06', '2019-03-13 20:37:06', 1, 1, 2, NULL, '21231', '3');
INSERT INTO `question` VALUES (19, NULL, '??', 4, 'undefined', NULL, '2019-03-13 20:37:48', '2019-03-13 20:37:48', 0, 1, 2, NULL, 'da\'d', '3');
INSERT INTO `question` VALUES (20, NULL, '6566565', 5, 'undefined', NULL, '2019-03-13 20:46:48', '2019-03-13 20:46:48', 0, 0, 2, NULL, '44', '3');
INSERT INTO `question` VALUES (21, NULL, '??', 3, 'undefined', NULL, '2019-03-13 20:50:17', '2019-03-13 20:50:17', 0, 1, 2, NULL, 'wqeq', '3');
INSERT INTO `question` VALUES (22, NULL, '???', 2, 'A,B', NULL, '2019-03-13 20:57:21', '2019-03-13 20:57:21', 1, 2, 1, NULL, '21231', '3');
INSERT INTO `question` VALUES (23, NULL, NULL, NULL, NULL, NULL, '2019-03-14 19:16:24', '2019-03-14 19:16:24', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `question` VALUES (24, NULL, NULL, NULL, NULL, NULL, '2019-03-14 19:21:08', '2019-03-14 19:21:08', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `question` VALUES (25, NULL, 'popo', 0, 'B', NULL, '2019-03-17 20:45:23', '2019-03-17 20:45:23', 1, 2, 2, NULL, '21231', '3');
INSERT INTO `question` VALUES (26, NULL, '4545545', 2, 'A,B', 'undefined&&&undefined&&&undefined&&&undefined', '2019-03-22 18:01:58', '2019-03-22 18:01:58', 1, 0, 2, NULL, '5454', '3');
INSERT INTO `question` VALUES (27, '???', '544545', 0, 'D', '444&&&111&&&111&&&555', '2019-03-26 10:18:35', '2019-03-26 10:18:35', 1, 0, 1, NULL, '5445', '3');
INSERT INTO `question` VALUES (28, '???', '455545', 0, 'D', '4554&&&7877&&&888&&&88878787&&&54545&&&87887&&&87778', '2019-03-26 10:37:53', '2019-03-26 10:37:53', 1, 1, 2, NULL, '44', '');
INSERT INTO `question` VALUES (29, '???', '1212121', 1, '0', NULL, '2019-03-26 11:35:55', '2019-03-26 11:35:55', 1, 1, 2, NULL, '21231', '3');
INSERT INTO `question` VALUES (30, '???', '545454', 2, 'D,E', '455&&&545&&&545&&&545&&&455454', '2019-03-26 11:51:17', '2019-03-26 11:51:17', 1, 0, 2, NULL, 'wqeq5', '3');
INSERT INTO `question` VALUES (31, '???', '45545454445', 3, NULL, NULL, '2019-03-26 19:39:28', '2019-03-26 19:39:28', 0, 1, 2, NULL, '21231', '3');

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
) ENGINE = MyISAM AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student_class
-- ----------------------------
INSERT INTO `student_class` VALUES (5, 22, 3, NULL);
INSERT INTO `student_class` VALUES (12, 1, 3, 'demo555');
INSERT INTO `student_class` VALUES (4, 23, 1, '455454');
INSERT INTO `student_class` VALUES (7, 6, 1, NULL);
INSERT INTO `student_class` VALUES (8, 9, 1, NULL);
INSERT INTO `student_class` VALUES (13, 11, 3, 'demo555');
INSERT INTO `student_class` VALUES (10, 2, 1, '111');

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
INSERT INTO `teacher` VALUES (6, NULL, '123456', '?', NULL, NULL, '2019-03-14 19:26:02', '2019-03-17 14:50:09', 'java,前端');
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
) ENGINE = MyISAM AUTO_INCREMENT = 49 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teacher_subject
-- ----------------------------
INSERT INTO `teacher_subject` VALUES (22, 55, 2, NULL);
INSERT INTO `teacher_subject` VALUES (21, 55, 1, '55555');
INSERT INTO `teacher_subject` VALUES (23, 6, 1, '5555555');
INSERT INTO `teacher_subject` VALUES (24, 6, 2, NULL);
INSERT INTO `teacher_subject` VALUES (42, 56, 2, NULL);
INSERT INTO `teacher_subject` VALUES (32, 44, 1, NULL);
INSERT INTO `teacher_subject` VALUES (33, 44, 2, NULL);
INSERT INTO `teacher_subject` VALUES (36, 46, 1, NULL);
INSERT INTO `teacher_subject` VALUES (37, 46, 2, NULL);
INSERT INTO `teacher_subject` VALUES (40, 45, 1, NULL);
INSERT INTO `teacher_subject` VALUES (43, 56, 1, NULL);
INSERT INTO `teacher_subject` VALUES (47, 57, 2, NULL);
INSERT INTO `teacher_subject` VALUES (48, 58, 1, NULL);

SET FOREIGN_KEY_CHECKS = 1;
