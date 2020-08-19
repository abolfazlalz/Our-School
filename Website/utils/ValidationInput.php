<?php
/**
 * Copyright (c) 2020.
 * All this code was written by Abolfazl Alizadeh.
 */

class ValidationInput
{
    /**
     * Check username is valid or not
     * English Character, numbers and longer than or equals 5 characters
     * @param $username
     * @return false|int
     */
    public static function valid_username($username)
    {
        return preg_match('/^[a-zA-Z0-9]{5,}$/', $username);
    }
}
