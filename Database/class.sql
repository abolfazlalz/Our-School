USE our_school;

SET @saturday = 0;
SET @sunday = 1;
SET @monday = 2;
SET @tuesday = 3;
SET @wednesday = 4;
SET @thursday = 5;
SET @friday = 6;

SET @description = 'کلاس آمار و احتمال';
SET @lessonId = 12;
SET @startTime = '7:30';
SET @endTime = '9:10';
SET @dayInWeek = @wednesday;
SET @repeat = 7;
SET @teacherId = 15;
SET @classId = 13;

INSERT INTO class (schoolId, description, lessonId, startTime, endTime, dayInWeek, `repeat`, teacherId, classId)
VALUES (1, @description, @lessonId, @startTime, @endTime, @dayInWeek, @repeat, @teacherId, @classId);

SET @insert = LAST_INSERT_ID();

SELECT *
FROM class;

DROP PROCEDURE getTodayStudentsClass;
CREATE PROCEDURE getTodayStudentsClass(IN uid INTEGER)
BEGIN
    SET @day = DAYOFWEEK(CURRENT_DATE());
    IF @day = 1 THEN
        SET @day = 0;
    end if;

    CALL getTodayStudentsClass(uid, @day);

END;

