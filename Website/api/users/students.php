<?php

/**
 * Copyright (c) 2020.
 * All this code was written by Abolfazl Alizadeh.
 */

include '../api.php';

//Api Documentation Address


// create_api_request($requestHandler, $apiDocAddress);


class StudentApi extends ApiHelper
{

    public function __construct()
    {
        parent::__construct();
        $this->set_api_doc_address(Address::getAddress() . '/documentation/users.html' . "#students");
    }

    /**
     * @param $request
     * @throws DatabaseException
     * @throws RequestException
     * @throws TokenApiException
     * @throws ValidationException
     */
    public function request_handler($request)
    {
        $apiDocAddress = $this->apiDocAddress;
        $student = new StudentsTableControl();
        //any time switch go to default case, isValidRequest get false value
        $this->result['data']['status'] = true;
        switch ($request) {
            // return the class in day
            case 'get-class':
                $column = ApiHelper::uid_or_username_is_entered();
                if ($column == '') return;
                $uid = $_GET[$column];
                if (!is_numeric($uid))
                    $uid = (new AccountTableControl())->get_uid_by_username($uid);
                $this->result['data']['items'] = $student->get_today_classes($uid);
                break;
            // get student information such as name and last name and more
            case 'get-student-information':
                $column = ApiHelper::uid_or_username_is_entered();
                if ($column == '') return;
                $uid = $_GET[$column];
                if (is_numeric($uid))
                    $this->result['data']['information'] = $student->get_information($uid);
                else
                    $this->result['data']['information'] = $student->get_information_by_username($uid);
                break;
            // get all student's classes in week
            case 'get-weekly-classes':
                ApiHelper::uid_is_entered();
                $uid = $_GET['uid'];
                $page = 0;
                $count = 5;
                if (array_key_exists('count', $_GET))
                    $count = $_GET['count'];
                if (array_key_exists('page', $_GET))
                    //page is zero base
                    $page = $_GET['page'] - 1;
                $weekly_classes = $student->get_weekly_classes($uid, $page, $count);
                $this->result['data']['length'] = (new ClassTableControl())->get_weekly_classes_length($uid);
                $this->result['data']['items'] = $weekly_classes;
                break;
            // create new student account
            case 'create-account':
                $fields = ['phone_number', 'username', 'password', 'name', 'last_name', 'class_id', 'birthday', 'device_name', 'ip', 'gender'];
                $photo = '';
                if (array_key_exists('photo', $_POST))
                    $photo = $_POST['photo'];

                //check are all filed is complete
                $incompleteFields = [];
                foreach ($fields as $field) {
                    if (!array_key_exists($field, $_POST))
                        //field is not complete and entered
                        array_push($incompleteFields, $field);
                }

                if (count($incompleteFields) == 0) {
                    //all fields is is completed, so done it !
                    $username = $_POST['username'];
                    $phoneNumber = $_POST['phone_number'];
                    $password = $_POST['password'];
                    $name = $_POST['name'];
                    $last_name = $_POST['last_name'];
                    $classId = $_POST['class_id'];
                    $birthday = $_POST['birthday'];
                    $gender = $_POST['gender'];

                    $insert = $student->create_student_account($phoneNumber, $username, $password, $name, $last_name, $birthday, $classId, $gender, $photo);
                    if ($insert > 0) {
                        http_response_code(201);
                        $this->result['data']['uid'] = $insert;
                        $accountTableCtrl = new AccountTableControl();
                        $deviceName = $_POST['device_name'];
                        $ip = $_POST['ip'];
                        $login = $accountTableCtrl->log_in($username, $password, $deviceName, $ip);
                        $this->set_data('information', $login);
                        $this->set_status(true);
                    } else {
                        $this->result['data']['msg'] = 'username are taken';
                        http_response_code(406);
                    }
                    //                $accountCtrl->create_account
                } else {
                    //one or more fields are not complete and entered
                    $this->result['data']['msg'] = 'some fields are not completed, at first enter there fields and then try again.\n' . join(', ', $incompleteFields);
                    http_response_code();
                }
                break;
            case 'delete_account':
                ApiHelper::uid_is_entered();
                $uid = $_GET['uid'];
                (new TokenApiTableControl($_GET['token']))->check_minimum_permission(TokenApiTableControl::FULL_ACCESS);
                (new AccountTableControl())->delete_account($uid);
                break;
            case 'get-students':
                self::school_class_id_is_entered();
                $page = 1;
                $count = 10;
                $sort = StudentsTableControl::SORT_LAST_NAME;
                if (array_key_exists('page', $_GET))
                    $page = $_GET['page'];
                if (array_key_exists('count', $_GET))
                    $page = $_GET['count'];

                $id = $_GET['class_id'];
                $this->result['data']['students'] = $student->get_students_in_class($id, $sort, $page, $count);
                $this->result['data']['count'] = $student->get_count_of_students_in_class($id);
                $this->result['data']['class'] = (new SchoolClassTableControl())->getSchoolClassById($id)[0];
                break;
            case 'get-school':
                ApiHelper::school_id_is_entered();
                $page = array_key_exists('page', $_GET) && is_numeric($_GET['page']) ? $_GET['page'] : 1;
                $count = array_key_exists('count', $_GET) && is_numeric($_GET['count']) ? $_GET['count'] : 10;
                $sort = StudentsTableControl::SORT_LAST_NAME;
                $schoolId = $_GET['school_id'];
                $this->result['data']['students'] = $student->get_all_student_in_school($schoolId, $sort, $page, $count);
                $this->result['data']['count'] = $student->student_in_school_count($schoolId);
                break;
            case 'get-books':
                ApiHelper::uid_is_entered();
                $lessonTable = new LessonBookTableControl();
                $this->set_data('books', $lessonTable->select_student_books($_GET['uid']));
                $this->set_status(true);
                break;
            case 'add-study-time':
                $studyTable = new StudyDurationTableControl();
                $required = ['book-id', 'start-time', 'end-time'];
                self::uid_is_entered();
                foreach ($required as $require) {
                    if (!array_key_exists($require, $_POST)) {
                        throw RequestException::newInstanceForArgumentsCount($require);
                    }
                }
                $uid = $_GET['uid'];
                $bookId = $_POST['book-id'];
                $startTime = $_POST['start-time'];
                $endTime = $_POST['end-time'];

                $insert = $studyTable->add_study_duration($uid, $bookId, $startTime, $endTime);
                if ($insert > 0) {
                    $this->set_status(true);
                }

                break;
            case 'select-study-history':
                $studyTable = new StudyDurationTableControl();
                self::uid_is_entered();
                $uid = $_GET['uid'];
                $value = $studyTable->select_study_duration($uid);
                if ($value == false)
                    $this->set_data('items', []);
                else
                    $this->set_data('items', $value);
                $this->set_status(true);
                break;
            case 'delete-study-time':
                self::uid_is_entered();
                self::parameters_are_entered(['id']);
                $studyTable = new StudyDurationTableControl();
                $studyTable->delete_row_by_id($_GET['id']);
                $this->set_status(true);
                break;
            default:
                $this->result['data']['status'] = false;
                http_response_code(400);
                $this->result['data']['msg'] = "invalid student request, if you don't know about requests, you can visit documentation $apiDocAddress";
                break;
        }
    }
}

(new StudentApi())->create_api_request();
