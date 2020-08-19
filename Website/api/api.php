<?php

include_once $_SERVER['DOCUMENT_ROOT'] . '/utils/includes.php';
//include_once '../../utils/includes.php';


/**
 * Copyright (c) 2020.
 * All this code was written by Abolfazl Alizadeh.
 */

/**
 * ApiHelper can extends to other class and make a standard 'Our School' format api response
 * @author Abolfazl Alizadeh <mr.abolfazl.alz@gmail.com>
 */
class ApiHelper
{

    /**
     * if sometime user put a wrong request, api suggest this address to read
     *
     * @var string
     */

    protected $apiDocAddress;
    protected $result;

    protected function set_api_doc_address($address)
    {
        $this->apiDocAddress = $address;
    }

    public function __construct()
    {
        $this->result = ['data' => ['status' => false, 'status_code' => 200], 'about' => ['time' => date(Format::getApiDateFormat())]];
        $this->apiDocAddress = Address::getAddress() . '/documentation/index.php';
    }

    public function request_handler($request)
    {
        $this->result['data']['msg'] = 'not define any request for this method';
    }

    /**
     * @author Abolfazl Alizadeh <mr.abolfazl.alz@gmail.com>
     * @noinspection PhpRedundantCatchClauseInspection
     */
    public function create_api_request()
    {
        http_response_code(200);

        $format = 'json';
        if (array_key_exists('format', $_GET)) {
            switch (strtolower($_GET['format'])) {
                case 'xml':
                    $format = 'xml';
                    break;
                case 'json':
                    $format = 'json';
                    break;
                default:
                    $this->result['about']['format_msg'] = 'format is incorrect, format is json';
                    break;
            }
        }
        $this->result['about']['format'] = $format;


        //at first is better checking token is entered by user or not
        if (isset($_GET['token'])) {
            try {
                $tokenManager = new TokenApiTableControl($_GET['token']);
                //and then checking token is valid or not
                if ($tokenManager->is_valid_token()) {

                    if (array_key_exists('register-key', $_GET)) {
                        $_GET['uid'] = AccountTableControl::get_uid_by_key($_GET['register-key']);
                        if ($_GET['uid'] == false) {
                            throw new ValidationException('invalid register-key');
                        }
                    }
                    $this->result['about']['token']['user'] = $tokenManager->get_token_user();
                    $this->result['about']['token']['permission'] = $tokenManager->get_token_permission();
                    if (isset($_GET['request'])) {
                        $this->request_handler($_GET['request']);
                    } else {
                        $apiDocAddress = $this->apiDocAddress;
                        $this->result['data']['msg'] = "Enter your request command as POST http request, you can visit '$apiDocAddress'";
                    }
                } else {
                    http_response_code(401);
                    $this->result['data']['msg'] = 'your token api entered is not valid, please check it and try again';
                }
            } catch (DatabaseException $dbEx) {
                $this->result['data']['status'] = false;
                $this->result['data']['msg'] = $dbEx->getMessage();
                $this->result['data']['code'] = $dbEx->getCode();
                http_response_code(500);
            } catch (ArgumentCountError $e) {
                $this->result['data']['status'] = false;
                $this->result['data']['msg'] = 'Few argument for method please check server code - ' . $e->getMessage();
                $this->result['data']['code'] = $e->getCode();
                http_response_code(500);
            } catch (RequestException $requestException) {
                $this->result['data']['status'] = false;
                $this->result['data']['msg'] = $requestException->getMessage();
                $this->result['data']['code'] = $requestException->getCode();
                http_response_code(400);
            } catch (ValidationException $validationException) {
                $this->result['data']['status'] = false;
                $this->result['data']['msg'] = $validationException->getMessage();
                $this->result['data']['code'] = $validationException->getCode();
                http_response_code(406);
            } catch (TokenApiException $apiException) {
                $this->result['data']['status'] = false;
                $this->result['data']['msg'] = "you don't have permission for this request";
                http_response_code(405);
            } catch (Exception $ex) {
                $this->result['data']['status'] = false;
                $this->result['data']['msg'] = "it's an invalid exception, please let us know";
                $this->result['data']['msg_info'] = $ex->getMessage();
                $this->result['data']['code'] = $ex->getCode();
                http_response_code(500);
            }
        } else {
            $this->result['data']['msg'] = 'Enter your API token';
        }
        $this->result['data']['status_code'] = http_response_code();

        if (!isset($_GET['token_info']) || $_GET['token_info'] != true)
            unset($this->result['about']['token']);

        http_response_code(200);

        if ($format == 'json')
            echo json_encode($this->result);
        elseif ($format == 'xml')
            echo xmlrpc_encode($this->result);
    }

    /**
     * check class_id is enter from user or not, by GET request
     *
     * @return boolean
     *
     * @throws RequestException
     */
    protected static function school_class_id_is_entered()
    {
        if (array_key_exists('class_id', $_GET)) {
            $classId = $_GET['class_id'];
            if ((new SchoolClassTableControl())->isSchoolClassId($classId))
                return true;
            else
                throw RequestException::newInstanceForNotDefineArgument(['class_id'], 'no any school class exists by this id');
        }
        throw RequestException::newInstanceForArgumentsCount(['class_id']);
    }

    /**
     * set message for api response
     *
     * @param string $message
     * @return void
     */
    protected function set_message($message)
    {
        $this->set_data('msg', $message);
    }

    /**
     * set status of api response
     *
     * @param boolean $status
     * @return void
     */
    protected function set_status($status)
    {
        $this->set_data('status', $status);
    }

    /**
     * set data key for api response
     *
     * @param string $key
     * @param $value
     * @return void
     */
    protected function set_data($key, $value)
    {
        $this->result['data'][$key] = $value;
    }

    /**
     * @return bool
     * @throws DatabaseException
     * @throws RequestException
     */
    protected static function school_id_is_entered()
    {
        if (!array_key_exists('school_id', $_GET))
            throw RequestException::newInstanceForArgumentsCount('school_id');
        if ((new SchoolTableControl())->is_school_id($_GET['school_id']) == true)
            return true;
        else
            throw RequestException::newInstanceForNotDefineArgument(['school_id'], 'no any school exists by this id');
    }

    /**
     * sometime we need uid or username of user, so we have to check it, and if exist something exist of them return the name of them
     * @return string the column is exist
     * @throws RequestException
     */
    protected static function uid_or_username_is_entered()
    {
        $registerTbl = DatabaseControl::get_account_table();
        if (!array_key_exists('uid', $_GET) && !array_key_exists('username', $_GET)) {
            throw RequestException::newInstanceForArgumentsCount(['uid', 'username']);
        } elseif (array_key_exists('uid', $_GET) && !$registerTbl->value_exist('uid', $_GET['uid'])) {
            throw RequestException::newInstanceForNotDefineArgument(['uid'], 'no any account exist by this uid ! ');
        } elseif (array_key_exists('username', $_GET) && !$registerTbl->value_exist('username', $_GET['username'])) {
            throw RequestException::newInstanceForNotDefineArgument(['username'], 'no any account exist by this username ! ');
        } else {
            if (array_key_exists('uid', $_GET))
                return 'uid';
            else
                return 'username';
        }
    }

    /**
     * sometime we need uid user, so we have to check it
     * @param null|array $request
     * @throws RequestException
     */
    protected static function uid_is_entered($request = null)
    {
        if ($request == null)
            $request = $_GET;
        self::check_account_parameter_entered('uid', 'no any account exist by this uid ! ', $request);
    }

    /**
     * @param null $requests
     * @throws RequestException
     */
    protected static function username_is_entered($requests = null)
    {
        self::check_account_parameter_entered('username', 'no any account exist by this username !', $requests);
    }

    /**
     * @param array $parameters
     * @param null|array $request
     * @throws RequestException
     */
    protected static function parameters_are_entered($parameters, $request = null)
    {
        if ($request == null)
            $request = $_GET;
        foreach ($parameters as $parameter) {
            if (!array_key_exists($parameter, $request))
                throw RequestException::newInstanceForArgumentsCount($parameter);
        }
    }

    /**
     * check a key is exists in account and GET parameter
     *
     * @param string $key
     * @param string $message
     * @param null|array $requests
     * @return boolean
     * @throws RequestException
     */
    protected static function check_account_parameter_entered($key, $message = '', $requests = null)
    {
        if ($requests == null)
            $requests = $_GET;

        $registerTbl = DatabaseControl::get_account_table();
        if (!array_key_exists($key, $requests)) {
            throw RequestException::newInstanceForArgumentsCount([$key]);
        } elseif (!$registerTbl->value_exist($key, $requests[$key])) {
            if ($message == '')
                $message = 'no any account exist by this information';
            throw RequestException::newInstanceForNotDefineArgument([$key], $message);
        } else {
            return true;
        }
    }
}
