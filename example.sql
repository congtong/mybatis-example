-- 感谢这个博主的例子 直接拿过来用的
-- 表名随意建 跟配置文件一样就行

CREATE TABLE `student` (
  `student_id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` VARCHAR(20) DEFAULT NULL COMMENT '姓名',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '电话',
  `email` VARCHAR(50) DEFAULT NULL COMMENT '邮箱',
  `sex` TINYINT(4) DEFAULT NULL COMMENT '性别',
  `locked` TINYINT(4) DEFAULT NULL COMMENT '状态(0:正常,1:锁定)',
  `gmt_created` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '存入数据库的时间',
  `gmt_modified` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改的时间',
  PRIMARY KEY (`student_id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='学生表';


INSERT INTO `student`(`name`, phone, email, sex, locked)
VALUES
('小明', 13821378270, 'xiaoming@mybatis.cn', 1, 0),
('小丽', 13821378271,  'xiaoli@mybatis.cn', 0, 0),
('小刚', 13821378272, 'xiaogang@mybatis.cn', 1, 0),
('小花', 13821378273, 'xiaohua@mybatis.cn', 0, 0),
('小强', 13821378274, 'xiaoqiang@mybatis.cn', 1, 0),
('小红', 13821378275, 'xiaohong@mybatis.cn', 0, 0);