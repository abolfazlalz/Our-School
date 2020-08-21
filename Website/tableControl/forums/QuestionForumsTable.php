<?php

/**
 * Copyright (c) 2020.
 * All this code was written by Abolfazl Alizadeh.
 */

class QuestionForumsTable extends TableControlHelper
{
    const ORDER_ASC = 0;
    const ORDER_DESK = 1;

    public function __construct()
    {
        parent::__construct(DatabaseControl::get_forums_question());
    }

    /**
     * @param integer $writerId
     * @param string $title
     * @param string $body
     * @param integer $lessonId
     * @param integer $mediaListId
     * @return int
     * @throws DatabaseException
     */
    public function add_question($writerId, $title, $body, $lessonId, $mediaListId)
    {
        $data = ['writerId' => $writerId, 'title' => $title, 'body' => $body, 'lessonId' => $lessonId, 'mediaListId' => $mediaListId];
        $insert = $this->tableControl->insert_query($data);
        $this->check_query_run_result();
        return $insert;
    }

    /**
     * @param $questionId
     * @return bool|mysqli_result
     * @throws DatabaseException
     */
    public function delete_question($questionId)
    {
        $delete = $this->tableControl->delete_query(['id' => $questionId]);
        $this->check_query_run_result();
        return $delete;
    }

    /**
     * @param int $uid
     * @param int $page
     * @param int $count
     * @param int $id
     * @param string $orderType
     * @param string $orderBy
     * @return array
     * @throws DatabaseException
     */
    public function select_questions($uid, $page = 1, $count = 10, $id = -1, $orderType = 'DESC', $orderBy = 'dateCreated')
    {
        $page -= 1;
        $table = DatabaseControl::get_forums_question();
        $selectQuery = $table->get_select_query_creator();
        $selectQuery->set_limit_max($page * $count, $count);

        $condition = [];

        $register = new AccountTableControl();
        $account = $register->get_account_by_uid($uid);
        if (count($account) == 0)
            return [];
        $schoolClass = new SchoolClassTableControl();
        $sc = $schoolClass->getSchoolClassByUid($uid);
        $selectQuery->set_inner_join('lessonbookmajor lm', ['lm.bookId' => 'lb.id']);
        $selectQuery->set_left_join('lessonbook lb', ['lb.id' => 'question_forums.lessonId']);

        $condition['lm.gradeId'] = $sc[0]['grade']['id'];
        $selectQuery->select_columns('question_forums.*');

        if ($id > -1) {
            $condition['question_forums.id'] = $id;
        }

        $selectQuery->set_condition($condition);
        $selectQuery->set_order_by($orderBy, $orderType);

        $select = $table->select_query($selectQuery);

        $this->check_query_run_result();
        return $this->fix_format($select);
    }

    /**
     * @param array $array
     * @return array
     * @throws DatabaseException
     */
    public function fix_format($array)
    {
        $arrayList = [];

        foreach ($array as $item) {
            if (array_key_exists('writerId', $item)) {
                $item['writer'] = (new AccountTableControl())->get_account_by_uid($item['writerId']);

                unset($item['writerId']);
            }
            if (array_key_exists('lessonId', $item)) {
                $item['lesson'] = (new LessonBookTableControl())->select_row_by_id((int)$item['lessonId']);
                unset($item['lessonId']);
            }
            $item['answers-count'] = (new AnswerForumsTable())->get_count($item['id']);

            if (count($array) == 1) {
                $item['answers'] = (new AnswerForumsTable())->select_answers($item['id']);
                $item['mediaList'] = (new MediaCollectionControl())->get_collection_by_id($item['mediaListId']);
                unset($item['mediaListId']);

                for ($i = 0; $i < count($item['answers']); $i++) {
                    $item['answers'][$i]['writer'] = (new AccountTableControl())->get_information($item['answers'][$i]['writerId']);
                    unset($item['answers'][$i]['writerId']);
                }
            }
            array_push($arrayList, $item);
        }

        return $arrayList;
    }
}


