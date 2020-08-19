USE our_school;

DROP PROCEDURE selectBooksByMajorIndex;
CREATE PROCEDURE selectBooksByMajorIndex(IN `index` INTEGER)
begin
    SELECT l.id, l.title, lessonId
    FROM lessonbookmajor
             LEFT JOIN grade g on lessonbookmajor.gradeId = g.id
             RIGHT JOIN lessonbook l on lessonbookmajor.bookId = l.id
    WHERE g.`index` = `index`;
end;

SELECT lessonbook.* FROM lessonbook LEFT JOIN lessonbookmajor l on lessonbook.id = l.bookId RIGHT JOIN grade g on l.gradeId = g.id WHERE g.title = 'یازدهم ریاضی';

CALL selectBooksByMajorIndex(11);