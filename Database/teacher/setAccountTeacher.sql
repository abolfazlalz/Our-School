USE our_school;
SELECT * FROM account LEFT JOIN `teachers` on account.uid=teachers.uid WHERE account.uid='16'