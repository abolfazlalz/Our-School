<?php

use MySqlConnection\SelectQueryCreator;

/**
 * Copyright (c) 2020.
 * All this code was written by Abolfazl Alizadeh.
 */

class ClassUserTableControl extends TableControlHelper
{
    public function __construct()
    {
        parent::__construct(DatabaseControl::get_class_user_table());
    }

    /**
     * get user class information
     * if uid doesn't exist in database function will return false
     *
     * @param int $uid
     * @return array|false
     * @throws DatabaseException
     * @author Abolfazl Alizadeh <mrabolfazlalz@gmail.com>
     */
    public function get_user_class($uid)
    {
        $select = $this->select_rows_by_condition(['uid' => $uid]);
        if (count($select) < 1)
            return false;

        return $select[0];
    }

    /**
     * get user class information
     * if class id doesn't exist in database, function will return false
     *
     * @param int $classId
     * @return array|boolean
     * @throws DatabaseException
     */
    public function get_user_class_by_class_id($classId)
    {
        $select = $this->select_rows_by_condition(['classId' => $classId]);
        if (count($select) < 1)
            return false;
        return $select[0];
    }
}
