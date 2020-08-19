CREATE PROCEDURE selectStudentFromSchool(IN `school` INTEGER) BEGIN
SELECT account.uid,
    account.username,
    account.name,
    account.last_name,
    account.roleId,
    account.photo
FROM account
    INNER JOIN classuser ON account.uid = classUser.uid
    INNER JOIN schoolclass ON schoolclass.id = classuser.classId
WHERE schoolclass.schoolId = `school`;
end;