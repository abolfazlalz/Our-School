USE our_school;

SET @saturday = 0;
SET @sunday = 1;
SET @monday = 2;
SET @tuesday = 3;
SET @wednesday = 4;
SET @thursday = 5;
SET @friday = 6;

SELECT @uid := uid FROM account WHERE CONCAT(name, ' ', last_name) = 'مسعود لطفیان';
SELECT @classId := classId FROM classuser WHERE uid = @uid;
SELECT * FROM class WHERE classId = @classId && dayInWeek = @monday;