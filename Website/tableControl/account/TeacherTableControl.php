<?php

/**
 * Copyright (c) 2020.
 * All this code was written by Abolfazl Alizadeh.
 */

use MySqlConnection\SelectQueryCreator;
use MySqlConnection\TableControl;

/**
 * Class TeacherTableControl
 * @author Abolfazl Alizadeh
 */
class TeacherTableControl extends TableControlHelper
{

    /**
     * @var TableControl
     */
    private $accountTable;

    public function __construct()
    {
        parent::__construct(DatabaseControl::get_teacher_table());
        $this->accountTable = DatabaseControl::get_account_table();
    }

    /**
     * @param integer $uid
     * @return array
     * @throws DatabaseException
     */
    public function get_teacher_by_uid($uid)
    {
        $selectCreator = new SelectQueryCreator('account');
        $selectCreator->join_database('teachers', ['uid' => 'uid'], SelectQueryCreator::JOIN_TYPE_LEFT);
        $selectCreator->set_condition(['account.uid' => $uid]);
        $result = $this->tableControl->select_query($selectCreator);
        $this->check_query_run_result();
        if (count($result) < 1)
            return array();
        $result = $this->fix_format($result);
        return $result[0];
    }

    public function is_teacher_id($uid)
    {
        $result = $this->accountTable->values_exist(['uid' => $uid, 'roleId' => 1]);
        self::error_handler($this->accountTable);
        return $result;
    }

    /**
     * @param $array
     * @return array|mixed
     * @throws DatabaseException
     */
    public function fix_format($array)
    {
        $array = (new AccountTableControl())->fix_format($array);
        $result = [];
        foreach ($array as $item) {
            $lessonId = $item['lessonId'];
            if (is_numeric($lessonId)) {
                unset($item['lessonId']);
                $item['lesson'] = (new LessonTableControl())->select_row_by_id($lessonId);
            }
            array_push($result, $item);
        }
        return $result;
    }
}
