<?php
/**
 * Copyright (c) 2020.
 * All this code was written by Abolfazl Alizadeh.
 */

class ValidationException extends Exception
{
    const USERNAME = 0;

    public function __construct($message = "", $code = 0)
    {
        parent::__construct($message, $code);
    }

    public static function new_instance_username_exception()
    {
        return new ValidationException('The username must have 5 english characters or number', self::USERNAME);
    }

    /**
     * @param string $username
     * @return bool
     * @throws ValidationException
     */
    public static function check_username_with_throw($username)
    {
        if (ValidationInput::valid_username($username)) {
            return true;
        }
        throw self::new_instance_username_exception();
    }

}