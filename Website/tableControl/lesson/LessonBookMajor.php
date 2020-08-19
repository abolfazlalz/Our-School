<?php
/**
 * Copyright (c) 2020.
 * All this code was written by Abolfazl Alizadeh.
 */

class LessonBookMajor extends TableControlHelper
{
    public function __construct()
    {
        parent::__construct(DatabaseControl::get_lesson_book_major_table());
    }

    /**
     * @param $array
     * @return array
     * @throws DatabaseException
     */
    public function fix_format($array)
    {
        $result = [];
        foreach ($array as $item) {
            $gradeId = $item['gradeId'];
            unset($item['gradeId']);
            $item['grade'] = (new GradeTableControl())->get_grade_by_id($gradeId);
            array_push($result, $item);
        }

        return $result;
    }

}