<?php

use MySqlConnection\SelectQueryCreator;

/**
 * Copyright (c) 2020.
 * All this code was written by Abolfazl Alizadeh.
 */
class LessonBookTableControl extends TableControlHelper
{

    public function __construct()
    {
        parent::__construct(DatabaseControl::get_lesson_book_table());
    }

    /**
     * @param integer $gradeId
     * @return bool|mysqli_result
     * @throws DatabaseException
     */
    public function select_books_by_grade($gradeId)
    {
        $query = new SelectQueryCreator('lessonBook');
        $query->select_columns('lessonbook.*');
        $query->set_left_join('lessonbookmajor l', ['lessonbook.id' => 'l.bookId']);
        $query->set_right_join('grade g', ['l.gradeId' => 'g.id']);
        $query->set_condition(['g.id' => $gradeId]);
        return $this->run_lesson_book_query($query);
    }

    /**
     * @param integer|string $major
     * @param integer $index
     * @return array|bool
     * @throws DatabaseException
     */
    public function select_lesson_book($major, $index)
    {

        $query = new SelectQueryCreator('lessonbook');
        $query->select_columns('lessonbook.*');
        $query->set_left_join('lessonbookmajor l', ['lessonbook.id' => 'l.bookId']);
        $query->set_right_join('grade g', ['l.gradeId' => 'g.id']);

        if (is_numeric($major)) {
            $query->set_condition(['g.`index`' => $index, 'g.majorId' => $major]);
        } else {
            $query->set_inner_join('major m', ['g.majorId' => 'm.id']);
            $query->set_condition(['g.`index`' => $index, 'm.`title`' => $major]);
        }
        $query->set_order_by('lessonbook.title', SelectQueryCreator::ORDER_BY_OPTION_ASC);
        return $this->run_lesson_book_query($query);
    }

    /**
     * @param $uid
     * @return array|bool
     * @throws DatabaseException
     */
    public function select_student_books($uid)
    {
        $gradeTable = new GradeTableControl();
        $grade = $gradeTable->get_grade_by_uid($uid);
        if (count($grade) == 0) return false;
        $grade = $grade[0];
        return $this->select_lesson_book($grade['major']['title'], $grade['index']);
    }

    /**
     * @param $query
     * @return array|bool
     * @throws DatabaseException
     */
    private function run_lesson_book_query($query)
    {
        $result = $this->tableControl->get_connection()->run_select_query($query);
        $this->check_query_run_result();
        if ($result == null)
            return false;
        return $result;
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
            $lesson_id = $item['lessonId'];
            unset($item['lessonId']);
            $item['lesson'] = (new LessonTableControl())->select_row_by_id($lesson_id);
            array_push($result, $item);
        }

        return $result;
    }

}