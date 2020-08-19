<?php
/**
 * Copyright (c) 2020.
 * All this code was written by Abolfazl Alizadeh.
 */

include_once '../../utils/includes.php';
include_once '../api.php';

class SchoolClass extends ApiHelper
{

    public function __construct()
    {
        parent::__construct();
        $this->set_api_doc_address(Address::getAddress() . '/documentation/school.html');
    }

    /**
     * @param string $request
     * @throws DatabaseException
     */
    public function request_handler($request)
    {
        $table = new SchoolTableControl();

        switch ($request) {
            case 'get-schools':
                $condition = [];
                if (array_key_exists('city', $_GET))
                    $condition['cityName'] = $_GET['city'];
                $this->set_status(true);
                $this->set_data('schools', $table->get_school_by_condition($condition));

                break;
            case 'get-school-class':
                $schoolClassTable = new SchoolClassTableControl();
                if (!array_key_exists('name', $_GET))
                    throw new ArgumentCountError("name");
                $result = $schoolClassTable->getClassInSchool($_GET['name']);
                if (count($result) > 0) {
                    $this->set_data('school_class', $result);
                    $this->set_status(true);
                }

                break;
            default:
                $this->result['data']['status'] = false;
                http_response_code(400);
                $this->result['data']['msg'] = "invalid school request, if you don't know about requests, you can visit documentation " . $this->apiDocAddress;
                break;
        }
    }


}

(new SchoolClass())->create_api_request();