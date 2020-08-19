<?php
/**
 * Copyright (c) 2020.
 * All this code was written by Abolfazl Alizadeh.
 */

namespace utils;


class MessageControl
{
    public static function sendMessage($phoneNumber, $message) {

        $apikey = '185A57E9-D0A7-45EA-B2C4-DFE698927359';
        $smsNumber = '10002000080000';
        $queryData = [
            'from' => $smsNumber,
            'to' => '0' . $phoneNumber,
            'message' => $message,
            'signature' => $apikey
        ];
        $query = http_build_query($queryData);
        $url  = 'http://sms.parsgreen.ir/UrlService/sendSMS.ashx?' . $query;

        $ch = curl_init($url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 0);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, 0);
        $result = curl_exec($ch);
        curl_close($ch);
    }

    function se() {

    }
}