CREATE DATABASE IF NOT EXISTS our_school;

USE our_school;

DROP TABLE IF EXISTS schedule;

/*ER_PARSE_ERROR: You have an error in your SQL syntax; check the manual that corresponds to your MariaDB server version for the right syntax to use near 'CASCADE,
    FOREIGN KEY (uid)
        REFERENCES our_school.account(uid)
   ' at line 10*/
CREATE TABLE IF NOT EXISTS schedule (
    id INTEGER AUTO_INCREMENT,
    title VARCHAR(25),
    `description` VARCHAR(150),
    `startDate` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `endDate` DATETIME DEFAULT CURRENT_TIMESTAMP,
    isAllDay TINYINT(1),
    isStudy TINYINT(1),
    lessonId INTEGER(11),
    doesRepeat TINYINT(1),
    repeatMethod ENUM('day', 'week', 'month', 'year'),
    `uid` INTEGER(11),

    PRIMARY KEY (id),
    FOREIGN KEY (lessonId) 
        REFERENCES our_school.lesson(id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (`uid`) 
        REFERENCES our_school.account(`uid`)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS LessonStudyTime (
    id INTEGER AUTO_INCREMENT,
    lessonId INTEGER,
    `uid` INTEGER,
    durationStudy INTEGER,

    PRIMARY KEY (id),
    FOREIGN KEY (lessonId)
        REFERENCES our_school.lesson(id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (uid)
        REFERENCES our_school.account(uid)
        ON UPDATE CASCADE ON DELETE CASCADE

) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS repeat_week (
    id INTEGER AUTO_INCREMENT,
    dayInWeek INTEGER,
    scheduleId INTEGER,

    PRIMARY KEY (id),
    FOREIGN KEY (scheduleId)
        REFERENCES schedule(id)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS repeat_month (
    id INTEGER AUTO_INCREMENT,
    dayInWeek INTEGER DEFAULT -1,
    weekInMonth INTEGER DEFAULT -1,
    scheduleId INTEGER,
    day INTEGER DEFAULT -1,

    PRIMARY KEY (id),
    FOREIGN KEY (scheduleId)
        REFERENCES schedule(id)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE = INNODB;

