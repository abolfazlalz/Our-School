use our_school;
SET @insertId = 0;
CALL InsertBook('محیط زیست',
                '',
                '',
                'محیط زیست', @insertId);

CALL AddBookToMajorByGradeId(@insertId, 4);
CALL AddBookToMajorByGradeId(@insertId, 5);
CALL AddBookToMajorByGradeId(@insertId, 6);