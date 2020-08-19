<?php
/**
 * Copyright (c) 2020.
 * All this code was written by Abolfazl Alizadeh.
 */

class TokenApiException extends Exception
{
    const INVALID_API_TOKEN_CODE = 0;
    const NO_PERMISSION_CODE = 1;

    public function __construct($code = 0)
    {
        switch ($code) {
            case self::INVALID_API_TOKEN_CODE:
                $message = 'not exist any token with this token code';
                break;
            case self::NO_PERMISSION_CODE:
                $message = "you don't have enough permission";
                break;
            default:
                $message = 'invalid permission exception';
                break;
        }
        parent::__construct($message, $code);
    }

}