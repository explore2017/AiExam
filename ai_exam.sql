/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80013
 Source Host           : localhost:3306
 Source Schema         : ai_exam

 Target Server Type    : MySQL
 Target Server Version : 80013
 File Encoding         : 65001

 Date: 20/02/2019 14:53:19
*/


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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for batch
-- ----------------------------
DROP TABLE IF EXISTS `batch`;
CREATE TABLE `batch`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `exam_id` int(11) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批次名称',
  `describe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批次描述',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of batch
-- ----------------------------
INSERT INTO `batch` VALUES (1, 1, 'Java批次一', '描述', '2019-02-16 19:13:22', '2019-02-16 19:13:25');
INSERT INTO `batch` VALUES (2, 1, 'Java批次二', '描述', '2019-02-16 19:13:44', '2019-02-16 19:13:47');
INSERT INTO `batch` VALUES (3, 2, 'Linux批次一', '批次描述', '2019-02-17 17:44:02', '2019-02-17 17:44:04');
INSERT INTO `batch` VALUES (4, 3, '考试批次一', '批次描述', '2019-02-17 17:44:32', '2019-02-17 17:44:35');

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
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '考试开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '考试终止时间',
  `creator_id` int(11) NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

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
  `start_time` datetime(0) NULL DEFAULT NULL,
  `end_time` datetime(0) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `score` double(255, 0) NULL DEFAULT NULL,
  PRIMARY KEY (`exam_id`, `student_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of exam_student
-- ----------------------------
INSERT INTO `exam_student` VALUES (1, 1, 2, 0, NULL, NULL, '2019-02-18 13:52:53', 0);
INSERT INTO `exam_student` VALUES (2, 1, 3, 0, NULL, NULL, '2019-02-18 15:53:32', NULL);

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
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

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
  `exam_id` int(11) NULL DEFAULT NULL COMMENT '考试id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '试卷名称',
  `describe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '试卷描述',
  `status` int(11) UNSIGNED NULL DEFAULT 0 COMMENT '试卷状态',
  `paper_type` int(11) NULL DEFAULT NULL COMMENT '试卷类型 ',
  `is_subjective` int(11) NULL DEFAULT NULL COMMENT '0-客观题,1-主观题',
  `difficulty` int(11) NULL DEFAULT NULL COMMENT '试卷难度',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `part_num` int(11) NULL DEFAULT NULL COMMENT '大题数',
  `pass_score` double(255, 0) NULL DEFAULT NULL COMMENT '及格分数',
  `total_score` double(255, 0) NULL DEFAULT NULL COMMENT '总分',
  `need_time` int(11) NULL DEFAULT NULL COMMENT '考试时长（分钟）',
  `answer` varchar(3000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '试卷答案',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of paper
-- ----------------------------
INSERT INTO `paper` VALUES (1, 1, 'java试卷A', '描述', 1, 1, 1, NULL, '2019-02-14 20:56:21', '2019-02-14 20:56:24', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `paper` VALUES (2, 1, 'java试卷B', '描述', 1, 1, 1, NULL, '2019-02-14 21:49:05', '2019-02-14 21:49:07', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `paper` VALUES (3, 2, 'Linux试卷A', '描述', 1, 1, 1, NULL, '2019-02-14 21:49:43', '2019-02-14 21:49:45', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `paper` VALUES (8, 1, '试卷1', '试卷描述', 0, 1, 0, 1, '2019-02-16 17:26:01', '2019-02-16 17:26:01', NULL, 60, 100, 120, 'A B C D');

-- ----------------------------
-- Table structure for paper_compose
-- ----------------------------
DROP TABLE IF EXISTS `paper_compose`;
CREATE TABLE `paper_compose`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `paper_id` int(11) NULL DEFAULT NULL COMMENT '试卷id',
  `question_type_id` int(11) NULL DEFAULT NULL COMMENT '大题id',
  `question_num` int(11) NULL DEFAULT NULL COMMENT '该大题下对应小题数',
  `sequence` int(255) NULL DEFAULT NULL COMMENT '排序',
  `single_score` double(255, 0) NULL DEFAULT NULL COMMENT '每小题分数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

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
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `is_subjective` int(255) NULL DEFAULT NULL COMMENT '1-主观题，0-客观题',
  `difficulty` int(255) NULL DEFAULT NULL COMMENT '题目难度',
  `subject_id` int(11) NULL DEFAULT NULL COMMENT '题目对应的科目',
  `status` int(255) NULL DEFAULT 1 COMMENT '题目状态1-默认开启  0-关闭',
  `key_point` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '知识点',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of question
-- ----------------------------
INSERT INTO `question` VALUES (1, 'java八种数据结构', 'A int B double C char D hello', 1, 'D', '2019-02-14 22:18:09', '2019-02-14 22:18:11', 0, 1, 1, 1, '');
INSERT INTO `question` VALUES (2, 'java八种数据结构', 'A int B double C char D hello', 1, 'A', '2019-02-14 22:18:30', '2019-02-14 22:18:32', 0, 1, 1, 1, NULL);
INSERT INTO `question` VALUES (3, '公共题目', 'A int B double C char D hello', 1, 'A', '2019-02-15 12:40:24', '2019-02-15 12:40:26', 1, 1, 1, 1, NULL);
INSERT INTO `question` VALUES (4, 'Linux题目', 'A ls B cd C ping D dir', 1, 'D', '2019-02-15 12:54:40', '2019-02-15 12:54:42', 0, 1, 1, 1, NULL);
INSERT INTO `question` VALUES (5, '试题名称', '试题内容', 1, 'A', '2019-02-16 17:40:56', '2019-02-16 17:40:56', 0, 1, 1, 1, NULL);
INSERT INTO `question` VALUES (6, '试题名称', '试题内容', 1, 'A', '2019-02-16 17:41:44', '2019-02-16 17:41:44', 0, 1, 1, 1, '知识点');

-- ----------------------------
-- Table structure for question_paper
-- ----------------------------
DROP TABLE IF EXISTS `question_paper`;
CREATE TABLE `question_paper`  (
  `paper_id` int(11) NOT NULL,
  `question_id` int(255) NOT NULL,
  `sequence` int(255) NULL DEFAULT NULL COMMENT '试题在试卷中的序号',
  PRIMARY KEY (`paper_id`, `question_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of question_type
-- ----------------------------
INSERT INTO `question_type` VALUES (1, '选择题', NULL);
INSERT INTO `question_type` VALUES (2, '多选题', NULL);
INSERT INTO `question_type` VALUES (3, '填空题', NULL);
INSERT INTO `question_type` VALUES (4, '判断题', NULL);
INSERT INTO `question_type` VALUES (5, '简答题', NULL);
INSERT INTO `question_type` VALUES (6, '分析题', NULL);

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sno` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `grade` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `emial` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES (1, '1600300818', '123456', '2016', '10086', '794409767@qq.com', NULL, NULL);

-- ----------------------------
-- Table structure for subject
-- ----------------------------
DROP TABLE IF EXISTS `subject`;
CREATE TABLE `subject`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '课程名称',
  `subject_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '课程号(预留)',
  `subject_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '课程代码(预留)',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of subject
-- ----------------------------
INSERT INTO `subject` VALUES (1, 'java', '123', 'EXE123', '2019-02-15 15:38:34', '2019-02-15 15:38:37', NULL);

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
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES (1, 'teacher', '123456', '老师', '10086', '10086@qq.com', NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
