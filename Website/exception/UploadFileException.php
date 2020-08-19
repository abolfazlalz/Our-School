<?php
/**
 * Copyright (c) 2020.
 * All this code was written by Abolfazl Alizadeh.
 */

class UploadFileException extends Exception
{
    const FILE_NOT_VALID = 0;
    const FILE_EXISTS = 1;
    const FILE_IS_LARGE = 2;
    const FILE_FORMAT = 3;
    const FILE_INVALID_ERROR = 4;



    public function __construct($code)
    {
        $message = '';
        switch ($code) {
            case self::FILE_NOT_VALID:
                $message = 'the file format are not valid';
                break;
            case self::FILE_EXISTS:
                $message = 'file already exists';
                break;
            case self::FILE_IS_LARGE:
                $message = 'file is to large for uploading';
                break;
            case self::FILE_FORMAT:
                $message = 'invalid file format';
                break;
            case self::FILE_INVALID_ERROR:
                $message = "invalid error in uploading file";
                break;
        }
        parent::__construct($message, ExceptionCode::FILE_UPLOAD_CODE);
    }

    // public static function
}