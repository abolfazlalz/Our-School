<?php

use MySqlConnection\SelectQueryCreator;

/**
 * Copyright (c) 2020.
 * All this code was written by Abolfazl Alizadeh.
 */
class ClassTableControl extends TableControlHelper
{

    /**
     * @param integer $uid
     * @return SelectQueryCreator
     */
    private function get_weekly_classes_query_creator($uid)
    {
        $selectQuery = new SelectQueryCreator("class");
        $selectQuery->set_condition("classId = (SELECT classId FROM classuser WHERE uid = $uid)");
        return $selectQuery;
    }


    public function __construct()
    {
        parent::__construct(DatabaseControl::get_class_table());
    }

    /**
     * @param $uid
     * @return array|bool
     * @throws DatabaseException
     */
    public function get_today_classes($uid)
    {
        $dayOnWeek = date('w') + 1;
        if ($dayOnWeek == 7)
            $dayOnWeek = 0;
        else if ($dayOnWeek == 8)
            $dayOnWeek = 1;

        $result = $this->tableControl->get_connection()->run_select_query("CALL SelectStudentClass($uid, $dayOnWeek)");
        if (count($result) < 1)
            return array();
        TableControlHelper::error_handler($this->tableControl);

        return $this->fix_result($result);
    }

    /**
     * @param integer $uid
     * @param $page
     * @param $count
     * @return array
     * @throws DatabaseException
     */
    public function get_weekly_classes($uid, $page = 0, $count = 5)
    {
        $selectQuery = $this->get_weekly_classes_query_creator($uid);
        $selectQuery->set_limit_max($page * $count, $count);
        $result = $this->tableControl->select_query($selectQuery);
        TableControlHelper::error_handler($this->tableControl);
        if (count($result) < 1)
            return array();
        return $this->fix_result($result);
    }

    /**
     * fix some variable value, for example if `schoolId` have numeric value this function set a school value by that id
     * @param array $array
     * @return array
     * @throws DatabaseException
     */
    public function fix_result($array)
    {
        $result = array();
        foreach ($array as $item) {
            $lessonId = $item['lessonId'];
            $schoolId = $item['schoolId'];
            $classId = $item['classId'];
            $teacherId = $item['teacherId'];
            unset($item['schoolId']);
            unset($item['lessonId']);
            unset($item['classId']);
            unset($item['teacherId']);
            if (is_numeric($schoolId))
                $item['school'] = (new SchoolTableControl())->get_school_by_id($schoolId);
            if (is_numeric($classId))
                $item['schoolClass'] = (new SchoolClassTableControl())->getSchoolClassById($classId)[0];
            if (is_numeric($lessonId))
                $item['lesson'] = (new LessonBookTableControl())->select_row_by_id($lessonId);
            if (is_numeric($teacherId))
                $item['teacher'] = (new TeacherTableControl())->get_teacher_by_uid($teacherId);

            array_push($result, $item);
        }
        return $result;
    }

    /**
     * @param $uid
     * @return integer
     * @throws DatabaseException
     */
    public function get_weekly_classes_length($uid)
    {
        $selectQuery = $this->get_weekly_classes_query_creator($uid);
        $selectQuery->select_columns('COUNT(*)');
        $result = $this->tableControl->select_query($selectQuery);
        $this->check_query_run_result();
        if (count($result) < 1)
            return 0;
        return $result[0]['COUNT(*)'];
    }

}