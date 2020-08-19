<?php

/**
 * Copyright (c) 2020.
 * All this code was written by Abolfazl Alizadeh.
 */

include_once '../../utils/includes.php';

include '../api.php';

class TeachersApi extends ApiHelper
{
    public function __construct()
    {
        parent::__construct();
        $this->set_api_doc_address(Address::getAddress() . '/documentation/users.html' . "#teacher");
    }

    /**
     * @param $request
     * @throws DatabaseException
     * @throws RequestException
     */
    public function request_handler($request)
    {
        $accountTableCtrl = new AccountTableControl();
        $teachersTableCtrl = new TeacherTableControl();
        $apiDocAddress = $this->apiDocAddress;
        //any time switch go to default case, isValidRequest get false value
        $this->set_status(true);
        switch ($request) {
            case 'get_teacher':
                self::uid_or_username_is_entered();

                if (array_key_exists('uid', $_GET))
                    $uid = $_GET['uid'];
                elseif (array_key_exists('username', $_GET))
                    $uid = $accountTableCtrl->get_uid_by_username($_GET['username']);

                if (!$teachersTableCtrl->is_teacher_id($uid)) {
                    $this->set_status(false);
                    $this->set_message("this account isn't a Teacher account");
                    break;
                }
                $this->set_data('information', $teachersTableCtrl->get_teacher_by_uid($uid));

                break;
            default:
                $this->set_status(false);
                http_response_code(400);
                $this->set_message("invalid teachers request, if you don't know about requests, you can visit documentation $apiDocAddress");
                break;
        }
    }
}

(new TeachersApi())->create_api_request();
