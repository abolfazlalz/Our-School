<?php

/**
 * Copyright (c) 2020.
 * All this code was written by Abolfazl Alizadeh.
 */

use MySqlConnection\SelectQueryCreator;
use MySqlConnection\TableControl;

/**
 * Class StudentsTableControl
 * @author Abolfazl Alizadeh
 */
class StudentsTableControl extends TableControlHelper
{

    const SORT_LAST_NAME = 0;
    const SORT_NAME = 1;
    const SORT_AVERAGE = 2;

    /**
     * @var AccountTableControl
     */
    private $accountCtrl;

    /**
     * StudentsTableControl constructor.
     * @author Abolfazl Alizadeh
     */
    public function __construct()
    {
        parent::__construct(DatabaseControl::get_students_table());
        $this->accountCtrl = new AccountTableControl();
    }

    /**
     * Private functions and methods
     */

    /**
     * create a get students query template
     *
     * @param integer $classId
     * @return SelectQueryCreator
     */
    private function create_get_students_in_class($classId)
    {
        $selectCreator = new SelectQueryCreator('account');
        $selectCreator->join_database('classuser', ['uid' => 'uid'], SelectQueryCreator::JOIN_TYPE_LEFT);
        $selectCreator->set_condition(['role' => 2, 'classId' => $classId]);
        return $selectCreator;
    }

    /**
     * get all of students in a school
     *
     * @param integer $schoolId
     * @return array
     * @throws DatabaseException
     */
    private function get_all_student_in_school_command($schoolId)
    {
        $connection = DatabaseControl::get_connection();
        $students = $connection->run_select_query("CALL selectStudentFromSchool($schoolId)");
        TableControlHelper::connection_error_handler($connection);
        return $students;
    }

    /**
     * Public functions and methods
     */

    /**
     * this function return today a student classes
     * @param integer $uid the student you want to get classes
     * @return bool|array
     * @throws DatabaseException
     */
    public function get_today_classes($uid)
    {
        return (new ClassTableControl())->get_today_classes($uid);
    }

    /**
     * this function return all student classes in week
     * @param integer $uid
     * @param integer $page
     * @param integer $count
     * @return array
     * @throws DatabaseException
     */
    public function get_weekly_classes($uid, $page, $count)
    {
        return (new ClassTableControl())->get_weekly_classes($uid, $page, $count);
    }

    /**
     * get student information by uid
     * @param integer $uid
     * @return array
     * @throws DatabaseException
     */
    public function get_information($uid)
    {
        $result = (new AccountTableControl())->select_rows_by_condition(['uid' => $uid]);

        if (count($result) < 1)
            return array();
        $result = $result[0];
        $result['class'] = (new SchoolClassTableControl())->getSchoolClassByUid($uid);
        $result['study'] = [];
        $result['study']['total'] = (new StudyDurationTableControl())->get_total_student_study_time($uid);
        $result['study']['this_month'] = (new StudyDurationTableControl())->get_this_month_study_time($uid);
        return $result;
    }

    /**
     * @param $username
     * @return array
     * @throws DatabaseException
     * @throws RequestException
     */
    public function get_information_by_username($username)
    {
        $uid = (new AccountTableControl())->get_uid_by_username($username);
        if ($uid == false) {
            throw RequestException::newInstanceForNotDefineArgument(['username'], 'no any account exist by this username ! ');
        }
        return $this->get_information($uid);
    }

    /**
     * get the students in a class
     *
     * @param integer $classId
     * @param integer set order type 
     * @param integer set the page
     * @param integer set the count
     * @return array
     * @throws DatabaseException
     */
    public function get_students_in_class($classId, $sort = 0, $page = 1, $count = 10)
    {
        $page -= 1;
        $selectCreator = $this->create_get_students_in_class($classId);
        $selectCreator->set_limit_max($page * $count, $count);
        switch ($sort) {
            case StudentsTableControl::SORT_LAST_NAME:
                $selectCreator->set_order_by('last_name', SelectQueryCreator::ORDER_BY_OPTION_ASC);
                break;
            case StudentsTableControl::SORT_NAME:
                $selectCreator->set_order_by('name', SelectQueryCreator::ORDER_BY_OPTION_ASC);
                break;
        }
        $selectCreator->select_columns('account.uid');
        $idList = array();
        foreach ($this->tableControl->select_query($selectCreator) as $idKey => $id)
            array_push($idList, $id['uid']);
        $users = array();
        foreach ($idList as $id) {
            $user = $this->accountCtrl->get_account_by_uid($id);
            unset($user['role']);
            array_push($users, $user);
        }
        TableControlHelper::error_handler($this->tableControl);
        return ($users);
    }

    /**
     * @param $classId
     * @return mixed
     * @throws DatabaseException
     */
    public function get_count_of_students_in_class($classId)
    {
        $selectCreator = $this->create_get_students_in_class($classId);
        $selectCreator->select_columns('COUNT(*) as count');
        $result = $this->tableControl->select_query($selectCreator);
        TableControlHelper::error_handler($this->tableControl);
        return $result[0]['count'];
    }

    /**
     * get all of students in a schools classes
     *
     * @param integer $schoolId -> the school id
     * @param integer $sort -> order type
     * @param integer $page -> the page
     * @param integer $count -> count of students name in a page
     * @return array
     * @throws DatabaseException
     */
    public function get_all_student_in_school($schoolId, $sort = 0, $page = 1, $count = 10)
    {
        $page -= 1;
        $users = $this->get_all_student_in_school_command($schoolId);
        $column = array_column($users, 'last_name');
        if ($sort == self::SORT_NAME) {
            $column = array_column($users, 'name');
        } else if ($sort == self::SORT_AVERAGE) {
            $column = array_column($users, 'last_name');
        }
        array_multisort($column, SORT_ASC, $users);
        $users = array_slice($users, $page * $count, $count);
        return $this->fix_format($users);
    }

    /**
     * @param $schoolId
     * @return int
     * @throws DatabaseException
     */
    public function student_in_school_count($schoolId)
    {
        return count($this->get_all_student_in_school_command($schoolId));
    }

    /**
     * @param String $phoneNumber
     * @param string $username
     * @param string $password
     * @param string $name
     * @param string $last_name
     * @param $birthday
     * @param integer $classId
     * @param $gender
     * @param string $photo
     * @return bool|int
     * @throws DatabaseException
     * @throws ValidationException
     * @autor Abolfazl Alizadeh
     */
    public function create_student_account($phoneNumber, $username, $password, $name, $last_name, $birthday, $classId, $gender, $photo = '')
    {
        $accountCtrl = new AccountTableControl();
        $insertResult = $accountCtrl->create_account($phoneNumber, $username, $password, $name, $last_name, $birthday, 2, $gender, $photo);
        if ($insertResult < 1) {
            return -1;
        }
        $classUser = DatabaseControl::get_class_user_table();
        $insert = $classUser->insert_if_exist_update(['uid' => $insertResult, 'role' => 2, 'classId' => $classId], ['uid' => $insertResult]);
        if ($insert < 1) {
            $accountCtrl->delete_account($insertResult);
            return -1;
        }
        TableControlHelper::error_handler($classUser);
        return $insertResult;
    }

    /**
     * just override super class method
     *
     * @param array $array
     * @return array
     * @throws DatabaseException
     */
    public function fix_format($array)
    {
        return $this->accountCtrl->fix_format($array);
    }
}
