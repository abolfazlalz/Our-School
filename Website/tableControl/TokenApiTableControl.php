<?php

/**
 * Copyright (c) 2020.
 * All this code was written by Abolfazl Alizadeh.
 */

/*
 * در کلاس توکین API مدیریت می شود
 * in this class, the API is managing
 * such Add token, remove token, disable token and ... (Blah Blah Blah (-; )
 * */

class TokenApiTableControl
{
    const FULL_ACCESS = 'full';
    const MIDDLE_ACCESS = 'middle';
    const WEAK_ACCESS = "weak";

    private $permissions = ['full' => 2, 'middle' => 1, 'weak' => 0];

    private $token;
    private $tableCtrl;

    /**
     * TokenApiTableControl constructor. prepare $token and $tableControl
     * @param string $token your API token, you want to manage
     * @author Abolfazl Alizadeh
     * @version 1.0.0.0
     */
    public function __construct($token)
    {
        $this->token = $token;
        $this->tableCtrl = DatabaseControl::get_api_table();
    }

    /**
     * Check entered token is valid or not, its check the entered token is exists in table
     * @return bool is token valid or not
     * @throws DatabaseException
     * @author Abolfazl Alizadeh
     * @version 1.0.0.0
     * @see \MySqlConnection\TableControl::value_exist()
     */
    public function is_valid_token()
    {
        $value_exist = $this->tableCtrl->value_exist('token', $this->token);
        DatabaseControl::check_table_result($this->tableCtrl);
        return $value_exist;
    }

    /**
     * get information  of token user
     * if token not valid, function was return false.
     *
     * @return bool|array
     * @author Abolfazl Alizadeh <mrabolfazlalz@gmail.com>
     */
    public function get_token_user()
    {
        if ($this->is_valid_token()) {
            $result = $this->tableCtrl->select_with_condition(['token' => $this->token])[0];
            DatabaseControl::check_table_result($this->tableCtrl);
            return (new AccountTableControl())->get_account_by_uid($result['uid']);
        }
        return false;
    }

    
    
    /**
     * get permission of token
     *
     * @return string
     * @author Abolfazl Alizadeh <mrabolfazlalz@gmail.com>
     */
    public function get_token_permission()
    {
        if ($this->is_valid_token()) {
            $select = $this->tableCtrl->select_with_condition(['token' => $this->token])[0];
            DatabaseControl::check_table_result($this->tableCtrl);
            return $select['permission'];
        }
    }

    /**
     * Check Permission for api Token
     * @param string $minPermission
     * @return bool
     * @throws TokenApiException
     * @throws DatabaseException
     */
    public function check_minimum_permission($minPermission)
    {
        if ($this->is_valid_token()) {
            $select = $this->tableCtrl->select_with_condition(['token' => $this->token])[0];
            DatabaseControl::check_table_result($this->tableCtrl);
            $minTarget = $this->permissions[$minPermission];
            $min = $this->permissions[$select['permission']];
            return $minTarget <= $min;
        } else
            throw new TokenApiException();
    }
}
