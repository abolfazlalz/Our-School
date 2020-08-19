<?php

/**
 * Copyright (c) 2020.
 * All this code was written by Abolfazl Alizadeh.
 */

// all of method and properties this class are "static method"

use MySqlConnection\Connection;
use MySqlConnection\Server;
use MySqlConnection\TableControl;

include_once 'MySqlConnection/MySqlConnection.php';
include_once 'config.php';

class DatabaseControl
{
    /*  PRIVATE METHODS  */

    /**
     * prepare a server by information in config.php file
     * @return Server
     * @author Abolfazl Alizadeh
     * @version 1.0.0
     * @see Server
     */
    private static function get_server()
    {
        return new Server(DB_HOST, DB_USERNAME, DB_PASSWORD);
    }

    /**
     * prepare Database connection
     * @return Connection
     * @author Abolfazl Alizadeh
     * @version 1.0.0
     * @see Connection
     */
    public static function get_connection()
    {
        return new Connection(self::get_server(), DB_NAME);
    }

    public static function get_schedules_connection()
    {
        return new Connection(self::get_server(), DB_SCHEDULE_DB_NAME);
    }

    /**
     * get and prepare connection for new table from Database
     * @param string $tableName The name of table in database you want to access
     * @return TableControl
     * @author Abolfazl Alizadeh
     * @version 1.0.0
     * @example $myTable = create_table_connection('my_table_name');
     */
    private static function create_table_connection($tableName)
    {
        return new TableControl(self::get_connection(), $tableName);
    }


    /*  PUBLIC METHODS  */

    /*      All of below code return table class value      */

    public static function get_account_table()
    {
        return self::create_table_connection('account');
    }

    public static function get_teacher_table()
    {
        return self::create_table_connection('teacher');
    }

    public static function get_students_table()
    {
        return self::create_table_connection('students');
    }

    public static function get_role_table()
    {
        return self::create_table_connection('role');
    }

    public static function get_grade_table()
    {
        return self::create_table_connection('grade');
    }

    public static function get_school_table()
    {
        return self::create_table_connection('school');
    }

    public static function get_school_class_table()
    {
        return self::create_table_connection('schoolclass');
    }

    public static function get_school_role()
    {
        return self::create_table_connection('school_role');
    }

    public static function get_class_table()
    {
        return self::create_table_connection('class');
    }

    public static function get_class_user_table()
    {
        return self::create_table_connection('classuser');
    }

    public static function get_lesson_table()
    {
        return self::create_table_connection('lesson');
    }

    public static function get_lesson_book_table()
    {
        return self::create_table_connection('lessonbook');
    }

    public static function get_lesson_book_major_table()
    {
        return self::create_table_connection('lessonbookmajor');
    }

    public static function get_major_table()
    {
        return self::create_table_connection('major');
    }

    public static function get_api_table()
    {
        return self::create_table_connection('apitoken');
    }

    /**             Schedule table              **/

    public static function get_schedules_table()
    {
        return self::create_table_connection('schedule');
    }

    public static function get_lesson_study_time_table()
    {
        return self::create_table_connection('lessonstudytime');
    }

    public static function get_repeat_month()
    {
        return self::create_table_connection('repeat_month');
    }

    public static function get_repeat_week()
    {
        return self::create_table_connection('repeat_week');
    }

    public static function get_confirm_code()
    {
        return self::create_table_connection('confirmcode');
    }

    public static function get_study_duration()
    {
        return self::create_table_connection('studyduration');
    }

    public static function get_login_user()
    {
        return self::create_table_connection('loginuser');
    }

    public static function get_media_control()
    {
        return self::create_table_connection('media_collection');
    }

    public static function get_media()
    {
        return self::create_table_connection('media');
    }

    public static function get_forums_question()
    {
        return self::create_table_connection('question_forums');
    }

    public static function get_forums_answer()
    {
        return self::create_table_connection('answer_forums');
    }

    /*  *   *   *   *   *   *   *   *   *   *   *   *   *   *   */

    /**
     * @param TableControl $table
     * @throws DatabaseException
     */
    public static function check_table_result($table)
    {
        if ($table->get_connection()->get_last_error() != '') {
            throw new DatabaseException($table->get_connection()->get_last_error(), DatabaseException::QUERY_EXCEPTION);
        } elseif ($table->get_connection()->get_connect_error() != '') {
            throw new DatabaseException($table->get_connection()->get_connect_error(), DatabaseException::CONNECTION_EXCEPTION);
        }
    }
}
