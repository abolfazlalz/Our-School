<?php
include_once '../api.php';

class AccountApi extends ApiHelper
{
    public function __construct()
    {
        parent::__construct();
        $this->set_api_doc_address(Address::getAddress() . '/documentation/users.html' . "#accounts");
    }

    /**
     * @param string $request
     * @throws DatabaseException
     * @throws RequestException
     */
    public function request_handler($request)
    {
        $accountTableCtrl = new AccountTableControl();
        $apiDocAddress = $this->apiDocAddress;

        switch ($request) {

            case 'login':
                self::username_is_entered($_POST);
                if (!array_key_exists('password', $_POST)) {
                    throw RequestException::newInstanceForNotDefineArgument('password', 'Enter user password');
                }
                $username = $_POST['username'];
                $password = $_POST['password'];
                $deviceName = $_POST['device_name'];
                $ip = $_POST['ip'];
                $login = $accountTableCtrl->log_in($username, $password, $deviceName, $ip);
                if ($login !== false) {
                    $this->set_data('information', $login);
                    $this->set_status(true);
                } else {
                    $this->set_message('incorrect password or username');
                }

                break;

            case 'sign-out':
                self::parameters_are_entered(['key'], $_POST);
                $accountTableCtrl->signOut($_POST['key']);
                $this->set_status(true);
                break;

            case 'check_phone_number':
                define('ALREADY_EXISTS', 0);
                define('CODE', 1);
                if (!array_key_exists('phone_number', $_POST)) {
                    throw RequestException::newInstanceForNotDefineArgument('phone_number', 'enter some mobile number to check');
                }

                if ($accountTableCtrl->is_phone_number_taken($_POST['phone_number'])) {
                    $this->set_message('the phone number are taken');
                    $this->set_data('code', ALREADY_EXISTS);
                    break;
                }

                $confirmTable = new ConfirmCodeTableControl();
                $confirmTable->add_phone_to_confirm($_POST['phone_number']);
                $this->set_message('the confirmation code was sent');
                $this->set_data('code', CODE);
                $this->set_status(true);

                break;
            case 'confirm_phone_number':

                if (!array_key_exists('phone_number', $_POST)) {
                    throw RequestException::newInstanceForNotDefineArgument('phone_number', 'enter some mobile number to check');
                }

                if (!array_key_exists('code', $_POST)) {
                    throw RequestException::newInstanceForNotDefineArgument('code', 'enter the validation code');
                }
                $confirm = new ConfirmCodeTableControl();
                $resultCode = $confirm->check_confirm_code($_POST['phone_number'], $_POST['code']);
                $this->set_data('code', $resultCode);
                if ($resultCode == ConfirmCodeTableControl::VALID)
                    $this->set_status(true);

                break;
            case 'get-information':
                self::uid_is_entered();
                $register = new AccountTableControl();
                $register = $register->get_information($_GET['uid']);
                $this->set_data('information', $register);
                $this->set_status(true);
                break;
            default:
                $this->set_status(false);
                http_response_code(400);
                $this->set_message("invalid accounts request, if you don't know about requests, you can visit documentation $apiDocAddress");
                break;
        }
    }
}

(new AccountApi())->create_api_request();
