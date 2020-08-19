USE our_school;

CREATE TABLE IF NOT EXISTS school
(
    id        INTEGER AUTO_INCREMENT,
    name      VARCHAR(50),
    address   TEXT,
    latitude  FLOAT,
    longitude FLOAT,
    image     VARCHAR(150),
    cityName  VARCHAR(50),

    PRIMARY KEY (id)
) ENGINE = INNODB;

ALTER TABLE school
    ADD COLUMN cityName VARCHAR(50);

CREATE TABLE IF NOT EXISTS role
(
    id    INTEGER AUTO_INCREMENT,
    title VARCHAR(25),
    image VARCHAR(150) NULL,
    PRIMARY KEY (id)
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS account
(
    uid         INTEGER AUTO_INCREMENT,
    username    VARCHAR(50),
    phoneNumber VARCHAR(15),
    `password`  VARCHAR(250),
    name        VARCHAR(25),
    last_name   varchar(25),
    birthday    DATE null,
    gender      ENUM('male', 'female'),
    roleId      INTEGER,
    photo       VARCHAR(150),
    PRIMARY KEY (uid),
    UNIQUE (phoneNumber),

    FOREIGN KEY (roleId)
        REFERENCES role (id)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE = INNODB;

ALTER TABLE account ADD COLUMN gender ENUM('male', 'female');

CREATE TABLE IF NOT EXISTS schoolRole
(
    id       INTEGER AUTO_INCREMENT,
    uid      INTEGER,
    roleId   INTEGER,
    schoolId INTEGER,
    PRIMARY KEY (id),
    FOREIGN KEY (roleId)
        REFERENCES role (id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (uid)
        REFERENCES account (uid)
        ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (schoolId)
        REFERENCES school (id)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE = INNODB;

ALTER TABLE lessonbookmajor
    ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS major
(
    id    INTEGER AUTO_INCREMENT,
    title NVARCHAR(25),
    PRIMARY KEY (id)
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS grade
(
    id      INTEGER AUTO_INCREMENT,
    title   VARCHAR(25),
    `index` INTEGER,
    majorId INTEGER,
    PRIMARY KEY (id),
    FOREIGN KEY (majorId)
        REFERENCES major (id)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS lesson
(
    id    INTEGER AUTO_INCREMENT,
    title VARCHAR(25),
    photo VARCHAR(25),

    PRIMARY KEY (id)
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS lessonBook
(
    id       INTEGER AUTO_INCREMENT,
    title    VARCHAR(25),
    filePath varchar(150),
    photo    VARCHAR(150),
    lessonId INTEGER,

    PRIMARY KEY (id),
    FOREIGN KEY (lessonId)
        REFERENCES lesson (id)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS lessonBookMajor
(
    id      INTEGER AUTO_INCREMENT,
    gradeId INTEGER,
    bookId  INTEGER,
    PRIMARY KEY (id),
    FOREIGN KEY (gradeId)
        REFERENCES grade (id)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS schoolClass
(
    id        INTEGER AUTO_INCREMENT,
    title     VARCHAR(25),
    gradeId   INTEGER,
    schoolId  INTEGER,
    teacherId INTEGER,

    PRIMARY KEY (id),
    FOREIGN KEY (gradeId)
        REFERENCES grade (id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (schoolId)
        REFERENCES school (id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (teacherId)
        REFERENCES account (uid)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS class
(
    id          INTEGER AUTO_INCREMENT,
    schoolId    INTEGER,
    startTime   DATETIME,
    endTime     DATETIME,
    description VARCHAR(150),
    lessonId    INTEGER,
    PRIMARY KEY (id),

    FOREIGN KEY (schoolId)
        REFERENCES school (id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (lessonId)
        REFERENCES lessonBook (id)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS classUser
(
    id      INTEGER AUTO_INCREMENT,
    uid     INTEGER,
    role    INTEGER,
    classId INTEGER,
    PRIMARY KEY (ID),
    FOREIGN KEY (classId)
        REFERENCES class (id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (role)
        REFERENCES role (id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (uid)
        REFERENCES account (uid)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    UNIQUE (uid)
) ENGINE = INNODB;

DROP TABLE IF EXISTS teachers;
CREATE TABLE IF NOT EXISTS teachers
(
    id       INTEGER AUTO_INCREMENT,
    uid      INTEGER COMMENT 'the teacher is in account',
    lessonId INTEGER COMMENT 'the lesson, teacher can teaching',
    PRIMARY KEY (id),
    FOREIGN KEY (lessonId)
        REFERENCES account (uid)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE = INNODB;

ALTER TABLE apitoken
    ENGINE = INNODB;

DROP TABLE IF EXISTS apiToken;
CREATE TABLE IF NOT EXISTS apiToken
(
    id         INTEGER AUTO_INCREMENT,
    uid        INTEGER COMMENT 'the user want to use it',
    permission ENUM ('full', 'middle', 'weak'),
    token      VARCHAR(100),
    PRIMARY KEY (id),
    UNIQUE KEY (permission),
    FOREIGN KEY (uid)
        REFERENCES account (uid)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE = INNODB;

DROP TABLE IF EXISTS confirmCode;

CREATE TABLE IF NOT EXISTS confirmCode
(
    id          INTEGER AUTO_INCREMENT,
    phoneNumber VARCHAR(15),
    dateCreated DATETIME DEFAULT current_time,
    timeout     INTEGER  DEFAULT 180,
    code        INTEGER,

    PRIMARY KEY (id),
    UNIQUE (phoneNumber)
) ENGINE = INNODB;


CREATE TABLE IF NOT EXISTS loginUser
(
    id         INTEGER AUTO_INCREMENT,
    uid        INTEGER,
    timeLogin  DATETIME DEFAULT CURRENT_TIMESTAMP,
    lastSeen   DATETIME DEFAULT CURRENT_TIMESTAMP,
    ip         VARCHAR(25),
    deviceName VARCHAR(25),

    PRIMARY KEY (id),
    FOREIGN KEY (uid)
        REFERENCES account (uid)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS studyDuration
(
    id        INTEGER AUTO_INCREMENT,
    uid       INTEGER,
    bookId    INTEGER,
    startTime DATETIME,
    endTime   DATETIME,

    PRIMARY KEY (id),
    FOREIGN KEY (uid)
        REFERENCES account (uid)
        ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (bookId)
        REFERENCES lessonBook (id)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS media_collection
(
    id   INTEGER AUTO_INCREMENT,
    name VARCHAR(25),
    PRIMARY KEY (id)
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS media
(
    id           INTEGER AUTO_INCREMENT,
    address      VARCHAR(150),
    collectionId INTEGER,
    type         ENUM ('Image', 'Video'),
    dateCreated  DATETIME DEFAULT CURRENT_TIMESTAMP(),
    dateModified DATETIME DEFAULT CURRENT_TIMESTAMP(),

    PRIMARY KEY (ID),
    FOREIGN KEY (collectionId)
        REFERENCES media_collection (id)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS question_forums
(
    id          INTEGER AUTO_INCREMENT,
    title       VARCHAR(50),
    writerId    INTEGER, #uid of the user ask question
    body        TEXT,
    dateCreated DATETIME DEFAULT CURRENT_TIMESTAMP(),
    dateModified DATETIME DEFAULT CURRENT_TIMESTAMP(),
    lessonId INTEGER,
    mediaListId INTEGER,

    PRIMARY KEY (id),
    UNIQUE KEY (title),
    FOREIGN KEY (writerId)
        REFERENCES account(uid)
        ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (lessonId)
        REFERENCES lessonbook(id)
        ON UPDATE CASCADE ON DELETE CASCADE ,
    FOREIGN KEY (mediaListId)
        REFERENCES media_collection(id)
        ON UPDATE CASCADE ON DELETE CASCADE

) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS answer_forums
(
    id  INTEGER AUTO_INCREMENT,
    answer TEXT,
    writerId INTEGER,
    dateCreate DATETIME DEFAULT CURRENT_TIMESTAMP(),
    dateModified DATETIME DEFAULT CURRENT_TIMESTAMP(),
    mediaListId INTEGER,
    questionId INTEGER,

    PRIMARY KEY (id),
    FOREIGN KEY (writerId)
        REFERENCES account(uid)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (mediaListId)
        REFERENCES media_collection(id)
        ON DELETE CASCADE ON UPDATE CASCADE ,
    FOREIGN KEY (questionId)
        REFERENCES question_forums(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = INNODB;

# TODO: USER SETTING
# CREATE TABLE IF NOT EXISTS userSetting (
#     id INTEGER AUTO_INCREMENT,
#     uid INTEGER COMMENT 'the user have dedicated settings',
#     language VARCHAR(3),
#     theme VARCHAR(15)
# )