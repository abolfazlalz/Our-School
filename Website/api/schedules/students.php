<?php
include '../api.php';


class StudentSchedulesApi extends ApiHelper
{
    public function __construct()
    {
        parent::__construct();
    }

    /**
     * @param string $request
     * @return void
     *
     * @throws RequestException
     * @throws DatabaseException
     */
    public function request_handler($request)
    {
        $table = new StudentScheduleTable();
        $this->set_status(true);
        $apiDocAddress = $this->apiDocAddress;
        switch (strtolower($request)) {
            case 'add_all_day':
            case 'add_schedule':
                parent::uid_is_entered($_POST);
                $required = ['title', 'description', 'is_study'];
                if ($request == 'add_schedule') :
                    array_push($required, 'start_date');
                    array_push($required, 'end_date');
                else :
                    array_push($required, 'date');
                endif;

                $lessonId = 0;
                $doesRepeat = false;
                $repeatMethod = StudentScheduleTable::DAY_REPEAT_METHOD;
                foreach ($required as $require) {
                    if (array_key_exists($require, $_POST)) continue;
                    // throw RequestException::newInstanceForNotDefineArgument([$require], 'please set all of required arguments');
                    throw RequestException::newInstanceForArgumentsCount([$require]);
                }
                $uid = $_POST['uid'];
                $title = $_POST['title'];
                $description = $_POST['description'];
                $isStudy = $_POST['is_study'];

                if (array_key_exists('lesson_id', $_POST))
                    $lessonId = $_POST['lesson_id'];
                else if (array_key_exists('does_repeat', $_POST))
                    $doesRepeat = $_POST['does_repeat'];
                else if (array_key_exists('request_method', $_POST))
                    $repeatMethod = $_POST['request_method'];
                $insert = 0;
                if ($request == 'add_schedule') :
                    $startDate = $_POST['start_date'];
                    $endDate = $_POST['end_date'];
                    $insert = $table->add_work_schedule($uid, $title, $description, $startDate, $endDate, $isStudy, $lessonId, $doesRepeat, $repeatMethod);
                else :
                    $date = $_POST['date'];
                    $insert = $table->add_all_day_work_schedule($uid, $title, $description, $date, $isStudy, $lessonId, $doesRepeat, $repeatMethod);
                endif;
                if ($insert > 0) {
                    $this->set_data('id', $insert);
                } else {
                    $this->set_status(false);
                    $this->set_message('something wrong in adding new data');
                }

                break;
            case 'select_schedule':
                $page = 1;
                $count = 15;
                if (array_key_exists('page', $_GET)) $page = $_GET['page'];
                if (array_key_exists('count', $_GET)) $count = $_GET['count'];
                $cols = ['token', 'request'];
                foreach ($cols as $col) {
                    unset($_GET[$col]);
                }
                $this->set_data('schedules', $table->select_schedules($_GET['uid'], $_GET, $page, $count));
                break;

            default:
                $this->set_status(false);
                http_response_code(400);
                $this->set_message("invalid schedule request, if you don't know about requests, you can visit documentation $apiDocAddress");
                break;
        }
    }
}


(new StudentSchedulesApi())->create_api_request();
