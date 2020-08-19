<?php

use MySqlConnection\SelectQueryCreator;

/**
 * Copyright (c) 2020.
 * All this code was written by Abolfazl Alizadeh.
 */

class AnswerForumsTable extends TableControlHelper
{
    public function __construct()
    {
        parent::__construct(DatabaseControl::get_forums_answer());
    }

    /**
     * @param int $writerId
     * @param string $answer
     * @param integer $questionId
     * @return int
     * @throws DatabaseException
     */
    public function new_answer($writerId, $answer, $questionId)
    {
        $data = ['writerId' => $writerId, 'answer' => $answer, 'mediaListId' => 0, 'questionId' => $questionId];
        $insert = $this->tableControl->insert_query($data);
        $this->check_query_run_result();
        return $insert;
    }

    public function get_count($questionId) {
        return $this->tableControl->select_count(['questionId' => $questionId]);
    }

    public function select_answers($questionId)
    {
        $condition = ['questionId' => $questionId];
        $select = $this->tableControl->get_select_query_creator();
        $select->set_condition($condition);
        $select->set_order_by('dateCreate', SelectQueryCreator::ORDER_BY_OPTION_DESC);
        return $this->tableControl->select_query($select);
    }

}