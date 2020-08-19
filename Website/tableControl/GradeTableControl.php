<?php
/**
 * Copyright (c) 2020.
 * All this code was written by Abolfazl Alizadeh.
 */

include_once 'TableControlHelper.php';

class GradeTableControl extends TableControlHelper
{

    /**
     * GradeTableControl constructor.
     */
    public function __construct()
    {
        parent::__construct(DatabaseControl::get_grade_table());
    }

    /**
     * get grade information by id
     * @param integer $id
     * @return array
     * @throws DatabaseException
     */
    public function get_grade_by_id($id)
    {
        return $this->select_row_by_id($id);
    }

    /**
     * @param $uid
     * @return bool|mysqli_result
     * @throws DatabaseException
     */
    public function get_grade_by_uid($uid)
    {
        $query = "SELECT g.* FROM account LEFT JOIN classuser c on account.uid = c.uid
    RIGHT JOIN schoolclass s on c.classId = s.id INNER JOIN grade g on s.gradeId = g.id WHERE account.uid = $uid";

        $result = $this->tableControl->get_connection()->run_query($query);
        $this->check_query_run_result();
        return $this->fix_format($result);
    }

    /**
     * @param $array
     * @return array|mixed
     * @throws DatabaseException
     */
    public function fix_format($array)
    {
        $result = [];
        foreach ($array as $value) {
            $majorId = $value['majorId'];
            $major = (new TableControlHelper(DatabaseControl::get_major_table()))->select_row_by_id($majorId);
            unset($value['majorId']);
            $value['major'] = $major;
            array_push($result, $value);
        }
        return $result;
    }


}