<?php

/**
 * Copyright (c) 2020.
 * All this code was written by Abolfazl Alizadeh.
 */

use MySqlConnection\TableControl;
use utils\MessageControl;

/**
 * Class AccountTableControl
 * @author Abolfazl Alizadeh
 */
class AccountTableControl extends TableControlHelper
{
    public function __construct()
    {
        parent::__construct(DatabaseControl::get_account_table());
    }

    /**
     * @param string $key
     * @return bool|array
     * @throws DatabaseException
     */
    public static function get_uid_by_key($key)
    {
        $tbl = DatabaseControl::get_login_user();
        $select = $tbl->select_with_condition(['md5(id)' => $key]);
        TableControlHelper::error_handler($tbl);
        if (count($select) == 0)
            return false;
        return $select[0]['uid'];
    }

    /**
     * get the account you want by uid
     * @param integer $uid
     * @return array
     * @throws DatabaseException
     * @author Abolfazl Alizadeh
     */
    public function get_account_by_uid($uid)
    {
        $result = $this->select_rows_by_condition(['uid' => $uid]);
        if (count($result) < 1)
            return array();
        return $result[0];
    }

    /**
     * @param $uid
     * @throws DatabaseException
     */
    public function get_information($uid) {
        $result = $this->get_account_by_uid($uid);
        if (array_key_exists('role', $result) && array_key_exists('id', $result['role']) && $result['role']['id'] == 2) {
            return (new StudentsTableControl())->get_information($uid);
        } else {
            return $result;
        }
    }

    /**
     * get uid of user from his/her username
     * if username is not exist function will return false
     *
     * @param string $username
     * @return integer|false
     */
    public function get_uid_by_username($username)
    {
        $select = $this->tableControl->select_with_condition(['username' => $username]);
        if (count($select) < 1)
            return false;
        return $select[0]['uid'];
    }

    /**
     * just override, i don't have anything to say :)
     * @param $array
     * @return mixed
     * @throws DatabaseException
     */
    public function fix_format($array)
    {
        $result = [];
        foreach ($array as $item) {
            if (is_numeric($item['roleId']))
                $item['role'] = (new TableControlHelper(DatabaseControl::get_role_table()))->select_row_by_id($item['roleId']);
            unset($item['roleId']);
            unset($item['password']);
            array_push($result, $item);
        }
        return $result;
    }

    /**
     * @param string $phoneNumber
     * @param string $username
     * @param string $password
     * @param string $name
     * @param string $last_name
     * @param $birthday
     * @param integer $roleId
     * @param string $gender
     * @param string $photo
     * @return bool
     * @throws DatabaseException
     * @throws ValidationException
     */
    public function create_account($phoneNumber, $username, $password, $name, $last_name, $birthday, $roleId, $gender, $photo = '')
    {
        ValidationException::check_username_with_throw($username);
        $dataList = ['phoneNumber' => $phoneNumber, 'username' => $username, 'name' => $name, 'last_name' => $last_name, 'birthday' => $birthday, 'roleId' => $roleId, 'photo' => $photo, 'password' => "password('$password')"];
//        $dataList = ['phoneNumber' => $phoneNumber, 'username' => $username, 'name' => $name, 'last_name' => $last_name, 'birthday' => $birthday, 'roleId' => $roleId, 'photo' => $photo, 'password' => "password('$password')", 'gender' => $gender];
        $insert = $this->tableControl->insert_if_not_exist($dataList, ['username' => $username]);
        $this->check_query_run_result();
        return $insert;
    }

    /**
     * login and check username and password
     *
     * @param string $username
     * @param string $password
     * @param string $ip
     * @param string $deviceName
     * @return false|array
     * @throws DatabaseException
     */
    public function log_in($username, $password, $ip, $deviceName)
    {
        $result = $this->select_rows_by_condition("username='$username' && `password`=password('$password')");
        if (count($result) > 0) {
            $result = $result[0];
            //TODO:CHECK USER WANT TO RECEIVE MESSAGE OR NOT
            $tableCtrl = DatabaseControl::get_login_user();

            $key = $tableCtrl->insert_query(['uid' => $result['uid'], 'ip' => $ip, 'deviceName' => $deviceName]);
            $result['key'] = md5($key);
            MessageControl::sendMessage($result['phoneNumber'], 'یک ورود جدید به حساب کاربری شما');
            return $result;
        }
        return false;
    }

    /**
     * @param string $key
     * @throws DatabaseException
     */
    public function signOut($key) {
        $tableCtrl = DatabaseControl::get_login_user();
        $tableCtrl->delete_query(['md5(`uid`)' => $key]);
        $t = new TableControlHelper($tableCtrl);
        $t->check_query_run_result();
    }

    /**
     * check the uid is for an account or not
     *
     * @param integer $uid the uid
     * @return boolean
     */
    public function is_uid_exists($uid)
    {
        return $this->tableControl->value_exist('uid', $uid);
    }

    /**
     * @param integer $uid
     * @return bool
     * @throws DatabaseException
     */
    public function delete_account($uid)
    {
        $result = $this->tableControl->delete_query(['`account`.uid' => $uid]);
        $this->check_query_run_result();
        return $result > 0;
    }

    /**
     * check mobile number are taken or is new
     * @param $phone_number
     * @return bool if phone number are exists return true else return false
     */
    public function is_phone_number_taken($phone_number)
    {
        return $this->tableControl->value_exist('phoneNumber', $phone_number);
    }
}
