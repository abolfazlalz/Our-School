<?php
/**
 * Copyright (c) 2020.
 * All this code was written by Abolfazl Alizadeh.
 */

class DatabaseException extends Exception
{
    const TABLE_EXCEPTION = 0;
    const CONNECTION_EXCEPTION = 1;
    const QUERY_EXCEPTION = 2;
    const DATA_NOT_EXISTS = 3;

    public function __construct($message, $code)
    {
        parent::__construct($message, $code);
    }

    // public static function 
}