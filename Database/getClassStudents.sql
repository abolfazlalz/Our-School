USE our_school;

SET @CLASS_NAME = 202;
SELECT @classId := id FROM schoolclass WHERE title=@CLASS_NAME;
SELECT * FROM account LEFT JOIN classuser ON account.uid = classuser.uid WHERE classuser.classId = @classId;

