/*
 * Copyright (c) 2020.
 * All this code was written by Abolfazl Alizadeh.
 */


CREATE PROCEDURE selectQuestions(IN uid INTEGER)
BEGIN
SELECT question_forums.* FROM  question_forums
    LEFT JOIN lessonbook lb ON lb.id = question_forums.lessonId
    LEFT JOIN lesson l on lb.lessonId = l.id
    LEFT JOIN lessonbookmajor lm ON lb.id = lm.bookId
    LEFT JOIN grade g on lm.gradeId = g.id
    LEFT JOIN schoolclass sc ON g.id = sc.gradeId
    LEFT JOIN classuser c on sc.id = c.classId
    INNER JOIN account a on c.uid = a.uid
    WHERE a.uid = uid;
END;