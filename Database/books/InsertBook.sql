USE our_school;
DROP PROCEDURE InsertBook;
CREATE PROCEDURE InsertBook(IN bookTitle VARCHAR(25), IN file VARCHAR(150)
                           , IN image VARCHAR(150), IN lessonName VARCHAR(25), OUT insertId INTEGER)
BEGIN
    INSERT INTO lesson (title, photo) VALUES (lessonName, '');
    INSERT INTO lessonbook (title, filePath, photo, lessonId) VALUES (bookTitle, file, image, LAST_INSERT_ID());
    SELECT * FROM lessonbook WHERE id = LAST_INSERT_ID();
    SET insertId = LAST_INSERT_ID();
end;

CREATE PROCEDURE AddBookToMajor(IN book INTEGER, IN gradeTitle VARCHAR(25))
BEGIN
    SELECT @gradeId := id FROM grade WHERE title = gradeTitle;
    INSERT INTO lessonbookmajor (gradeId, bookId) VALUES (@gradeId, book);
end;

CREATE PROCEDURE AddBookToMajorByGradeId(IN book INTEGER, IN grade INTEGER)
BEGIN
    INSERT INTO lessonbookmajor (gradeId, bookId) VALUES (book, grade);
end;
