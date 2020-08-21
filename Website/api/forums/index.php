<?php
/**
 * Copyright (c) 2020.
 * All this code was written by Abolfazl Alizadeh.
 */

include_once '../api.php';

class ForumsApi extends ApiHelper
{
    public function __construct()
    {
        parent::__construct();
    }

    /**
     * @param $request
     * @throws DatabaseException
     * @throws RequestException
     */
    public function request_handler($request)
    {
        $questionTable = new QuestionForumsTable();
        $answerTable = new AnswerForumsTable();
        switch ($request) {
            case 'new-question':
                self::uid_is_entered();
                $mediaListId = -1;
                self::parameters_are_entered(['title', 'text', 'lesson-id'], $_POST);
                if (array_key_exists('upload-id', $_POST)) {
                    $mediaListId = $_POST['upload-id'];
                }
                $insert = $questionTable->add_question($_GET['uid'], $_POST['title'], $_POST['text'], $_POST['lesson-id'], $mediaListId);
                if ($insert > 0)
                    $this->set_status(true);
                $this->set_data('id', $insert);
                break;
            case 'new-answer':
                self::uid_is_entered();
                self::parameters_are_entered(['answer', 'question-id'], $_POST);
                $insert = $answerTable->new_answer($_GET['uid'], $_POST['answer'], $_POST['question-id']);
                if ($insert > 0)
                    $this->set_status(true);
                $this->set_data('id', $insert);
                break;
            case 'delete-question':
                self::parameters_are_entered(['id'], $_POST);
                if ($questionTable->delete_question($_POST['id'])) {
                    $this->set_status(true);
                }
                break;
            case 'select-question':
                self::uid_is_entered();

                $page = 1;
                $count = 10;
                $id = -1;

                if (array_key_exists('page', $_GET))
                    $page = $_GET['page'];
                if (array_key_exists('count', $_GET))
                    $count = $_GET['count'];
                if (array_key_exists('id', $_GET))
                    $id = $_GET['id'];

                $select = $questionTable->select_questions($_GET['uid'], $page, $count, $id);
                $this->set_status(true);
                $this->set_data('questions', $select);

                break;
        }
    }

}

$forums = new ForumsApi();
$forums->create_api_request();

