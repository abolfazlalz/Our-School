<?php

use utils\MessageControl;

/**
 * Copyright (c) 2020.
 * All this code was written by Abolfazl Alizadeh.
 */
class ConfirmCodeTableControl extends TableControlHelper
{

    const OUT_OF_DATE_CODE = 0;
    const INVALID_CODE = 1;
    const VALID = 2;

    public function __construct()
    {
        parent::__construct(DatabaseControl::get_confirm_code());
    }

    /**
     * send confirm code to user for confirm phone number
     * @param string $phoneNumber
     * @throws DatabaseException
     */
    function add_phone_to_confirm($phoneNumber)
    {
        if ($phoneNumber[0] == '0') {
            $phoneNumber = substr($phoneNumber, 1, strlen($phoneNumber));
        }
        $randomCode = rand(100000, 999999);
        $values = ['phoneNumber' => "$phoneNumber", 'code' => $randomCode];
        $this->tableControl->delete_query(['phoneNumber' => "$phoneNumber"]);
        $this->check_query_run_result();
        $this->tableControl->insert_query($values);
        $this->check_query_run_result();

        $message = 'کد تایید شماره همراه شما برای نرم افزار مدرسه ما ' . $randomCode;
//        $message = 'کد مدرسه ما ' . $randomCode;
        MessageControl::sendMessage($phoneNumber, $message);
    }

    /**
     * check the confirm code is correct or not
     * @param string $phoneNumber
     * @param int $code
     * @return integer
     * @throws DatabaseException
     * @throws Exception
     */
    public function check_confirm_code($phoneNumber, $code)
    {
        if ($phoneNumber[0] == '0') {
            $phoneNumber = substr($phoneNumber, 1, strlen($phoneNumber));
        }
        $values = ['phoneNumber' => "$phoneNumber", 'code' => $code];
        $result = $this->tableControl->select_with_condition($values);
        $this->check_query_run_result();
        if (count($result) == 0)
            return self::INVALID_CODE;
        $result = $result[0];
        $date1 = new DateTime();
        $date2 = new DateTime($result['dateCreated']);

        $timeDiff = date_diff($date1, $date2);
        $this->tableControl->delete_query(['phoneNumber' => "$phoneNumber"]);
        if ($timeDiff->i * 60 + $timeDiff->s < $result['timeout']) {
            return self::VALID;
        }

        return self::OUT_OF_DATE_CODE;
    }

}
