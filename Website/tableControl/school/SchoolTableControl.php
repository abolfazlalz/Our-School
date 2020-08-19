<?php

use MySqlConnection\SelectQueryCreator;

/**
 * Copyright (c) 2020.
 * All this code was written by Abolfazl Alizadeh.
 * @author Abolfazl Alizadeh <mr.abolfazl.alz@gmail.com>
 */

class SchoolTableControl extends TableControlHelper
{
    /**
     * SchoolTableControl constructor.
     */
    public function __construct()
    {
        parent::__construct(DatabaseControl::get_school_table());
    }

    /**
     * get school by id from database
     * @param integer $id
     * @return bool|array
     * @throws DatabaseException
     */
    public function get_school_by_id($id)
    {
        $select = $this->tableControl->select_with_condition(['id' => $id]);
        $this->check_query_run_result();
        if (count($select) < 1)
            return false;
        return $select[0];
    }

    /**
     * check an integer id is a school id or not
     *
     * @param integer $id
     * @return boolean
     * @throws DatabaseException
     */
    public function is_school_id($id)
    {
        $isExists = $this->tableControl->value_exist('id', $id);
        $this->check_query_run_result();
        return $isExists;
    }

    /**
     * @param string|array $condition
     * @param int $page
     * @param int $count
     * @return bool|array
     * @throws DatabaseException
     */
    public function get_school_by_condition($condition, $page = 1, $count = 15)
    {
        $page -= 1;
        $select = new SelectQueryCreator('school');
        $select->set_limit_max($page * $count, $count);
        $select->set_condition($condition);
        $select = $this->tableControl->select_query($select);
        $this->check_query_run_result();
        if (count($select) < 1)
            return array();
        return $select;
    }

}