<?php

use MySqlConnection\SelectQueryCreator;

/**
 * Copyright (c) 2020.
 * All this code was written by Abolfazl Alizadeh.
 */
class StudyDurationTableControl extends TableControlHelper
{

    private $studentTable;

    /**
     * StudyDurationTableControl constructor.
     */
    public function __construct()
    {
        parent::__construct(DatabaseControl::get_study_duration());
        $this->studentTable = new StudentsTableControl();
    }

    /**
     * @param int $uid
     * @param int $bookId
     * @param DateTime $startTime
     * @param DateTime $endTime
     * @return int
     * @throws DatabaseException
     */
    public function add_study_duration($uid, $bookId, $startTime, $endTime)
    {
        $values = ['uid' => $uid, 'bookId' => $bookId, 'startTime' => $startTime, 'endTime' => $endTime];
        $insert = $this->tableControl->insert_query($values);
        $this->check_query_run_result();
        return $insert;
    }

    /**
     * Select the user study history
     * @param integer $uid
     * @param int $page
     * @param int $count
     * @return array|boolean
     * @throws DatabaseException
     */
    public function select_study_duration($uid, $page = 1, $count = 10)
    {
        $page -= 1;

        $select = $this->tableControl->get_select_query_creator();
        $select->set_limit_max($page * $count, $count);
        $select->set_condition(['uid' => $uid]);
        $select->set_order_by('startTime', SelectQueryCreator::ORDER_BY_OPTION_DESC);
        $array = $this->tableControl->select_query($select);
        if (count($array) > 0) {
            return $this->fix_format($array);
        } else {
            return false;
        }
    }

    /**
     * @param $uid
     * @param array $condition
     * @return int|mixed
     * @throws DatabaseException
     */
    public function get_student_study_time($uid, $condition)
    {
        $time = 'SEC_TO_TIME( SUM( TIME_TO_SEC( TIMEDIFF(`endTime`, `startTime`) ) ) )';
        $select = $this->tableControl->get_select_query_creator();
        $select->select_columns(["HOUR($time) as hour",
            "MINUTE($time) as minute",
            "SECOND($time) as second"]);
        $condition['uid'] = $uid;
        $select->set_condition($condition);
        $result = $this->tableControl->select_query($select);
        $this->check_query_run_result();
        if (count($result) > 0) {
            return $result[0];
        }
        return ['minute' => 0, 'second' => 0, 'hour' => 0];
    }

    /**
     * @param $uid
     * @return int|mixed
     * @throws DatabaseException
     */
    public function get_total_student_study_time($uid)
    {
        $condition = array();
        return $this->get_student_study_time($uid, $condition);
    }

    /**
     * @param $uid
     * @return int|mixed
     * @throws DatabaseException
     */
    public function get_this_month_study_time($uid)
    {
        $condition = [];
        $condition['MONTH(`startTime`)'] = (int)date('m');
        return $this->get_student_study_time($uid, $condition);
    }

    /**
     * @param $array
     * @return mixed
     * @throws DatabaseException
     */
    public function fix_format($array)
    {
        $result = [];
        $bookCtrl = new LessonBookTableControl();
        foreach ($array as $item) {
            $bookId = $item['bookId'];
            unset($item['bookId']);
            unset($item['uid']);
            $item['book'] = $bookCtrl->select_row_by_id($bookId);
            array_push($result, $item);
        }
        return $result;
    }


}