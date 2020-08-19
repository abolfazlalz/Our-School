<?php

/**
 * Copyright (c) 2020.
 * All this code was written by Abolfazl Alizadeh.
 */


class RequestException extends Exception
{
    private $requestArgs;

    const NOT_VALID = 0;
    const ARGS_COUNT_ERROR = 1;
    const NOT_DEFINE_REQUEST = 2;

    /**
     * RequestException constructor.
     * @param array|string $requestArgs
     * @param string $message
     * @param integer $code
     */
    public function __construct($requestArgs, $message, $code)
    {
        parent::__construct($message, ExceptionCode::REQUEST_ERROR_CODE + $code);
        if (is_array($requestArgs))
            $this->requestArgs = $requestArgs;
        else if (is_string($requestArgs))
            $this->requestArgs = [$requestArgs];
    }

    public static function newInstanceWithMessage($requestArgs, $message)
    {
        return new RequestException($requestArgs, $message, self::NOT_VALID);
    }

    /**
     * create new instance of Arguments count exception
     *
     * @param array|string $requestArgs
     * @return RequestException
     * @author Abolfazl Alizadeh <mr.abolfazl.alz@gmail.com>
     */
    public static function newInstanceForArgumentsCount($requestArgs)
    {
        $requestArgsString = '';
        if (is_array($requestArgs)) {
            $requestArgsString = join($requestArgs, ', ');
        } else {
            $requestArgsString = $requestArgs;
        }
        return new
            RequestException(
                $requestArgs,
                'you didn\'t entered all of http request parameter such as ' . $requestArgsString,
                self::ARGS_COUNT_ERROR
            );
    }

    /**
     * create new exception for not defined argument
     *
     * @param string|array $requestArgs
     * @param string $message
     * @return RequestException
     * @author Abolfazl Alizadeh <mrabolfazlalz@gmail.com>
     */
    public static function newInstanceForNotDefineArgument($requestArgs, $message)
    {
        return new RequestException($requestArgs, $message, self::NOT_DEFINE_REQUEST);
    }

    public function getRequestArgs()
    {
        return $this->requestArgs;
    }

    public function __toString()
    {
        return 'request-exception';
    }
}
