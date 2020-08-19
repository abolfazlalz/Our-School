<?php

/**
 * Copyright (c) 2020.
 * All this code was written by Abolfazl Alizadeh.
 */

class SchoolClassTableControl
{
    private $tableCtrl;

    public function __construct()
    {
        $this->tableCtrl = DatabaseControl::get_school_class_table();
    }

    /**
     * @param integer $id
     * @return array
     * @throws DatabaseException
     * @author Abolfazl Alizadeh
     */
    public function getSchoolClassById($id)
    {
        $select = $this->tableCtrl->select_with_condition(['id' => $id]);
        if (!empty($this->tableCtrl->get_connection()->get_connect_error()))
            throw new DatabaseException($this->tableCtrl->get_connection()->get_connect_error(), DatabaseException::CONNECTION_EXCEPTION);
        else if (!empty($this->tableCtrl->get_connection()->get_last_error()))
            throw new DatabaseException($this->tableCtrl->get_connection()->get_last_error(), DatabaseException::QUERY_EXCEPTION);

        if (count($select) < 1) {
            return array();
        }

        $callable = function ($items) {
            return $this->fixSelectItems($items);
        };

        return TableControlHelper::select_by_id($id, $this->tableCtrl, $callable);
    }

    /**
     * @param string $schoolName
     * @return array
     * @throws DatabaseException
     */
    public function getClassInSchool($schoolName)
    {
        $select = $this->tableCtrl->get_connection()->run_select_query("SELECT * FROM schoolclass WHERE schoolId = (SELECT id FROM school WHERE school.name = '$schoolName')");
        return $this->fixSelectItems($select);
    }

    /**
     * @param $uid
     * @return array
     * @throws DatabaseException
     */
    public function getSchoolClassByUid($uid)
    {
        $class = (new ClassUserTableControl())->get_user_class($uid);
        if ($class == false)
            return array();

        return $this->getSchoolClassById($class['classId']);
    }

    /**
     * check the integer id is a class id or not
     *
     * @param integer $id the class id you want to check is exist or not
     * @return boolean
     *
     * @author Abolfazl Alizadeh <mr.abolfazl.alz@gmail.com>
     */
    public function isSchoolClassId($id)
    {
        return $this->tableCtrl->value_exist('id', $id);
    }

    /**
     * @param $items
     * @return array
     * @throws DatabaseException
     */
    private function fixSelectItems($items)
    {
        $result = array();
        foreach ($items as $item) {
            $school_id = $item['schoolId'];
            $grade_id = $item['gradeId'];
            unset($item['schoolId']);
            unset($item['gradeId']);
            $item['grade'] = (new GradeTableControl())->get_grade_by_id($grade_id);
            $item['school'] = (new SchoolTableControl())->get_school_by_id($school_id);
            array_push($result, $item);
        }
        return $result;
    }
}
