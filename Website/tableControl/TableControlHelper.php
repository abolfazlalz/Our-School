<?php

use MySqlConnection\SelectQueryCreator;
use MySqlConnection\TableControl;
use MySqlConnection\Connection;

/**
 * Copyright (c) 2020.
 * All this code was written by Abolfazl Alizadeh.
 */
class TableControlHelper
{
    /**
     * Select rows from table from their id
     * @param integer $id
     * @param TableControl $tableControl
     * @param callable $fixer
     * @return array
     * @throws DatabaseException
     * @author Abolfazl Alizadeh
     */
    public static function select_by_id($id, $tableControl, $fixer = null)
    {
        $select = $tableControl->select_with_condition(['id' => $id]);
        self::error_handler($tableControl);
        if (count($select) < 1) {
            return array();
        }
        if ($fixer != null)
            $select = $fixer($select);
        return $select;
    }

    /**
     * @param TableControl $tableControl
     * @throws DatabaseException
     */
    public static function error_handler($tableControl)
    {
        if (!empty($tableControl->get_connection()->get_connect_error()))
            throw new DatabaseException($tableControl->get_connection()->get_connect_error(), DatabaseException::CONNECTION_EXCEPTION);
        else if (!empty($tableControl->get_connection()->get_last_error()))
            throw new DatabaseException($tableControl->get_connection()->get_last_error(), DatabaseException::QUERY_EXCEPTION);
    }

    /**
     * handle and check connections for error
     *
     * @param /Connection $connection
     * @return void
     * @throws DatabaseException
     * @throws Exception
     * @see Connection
     */
    public static function connection_error_handler($connection)
    {
        if ($connection == null)
            throw new Exception('متغیر خام');
        else if (!empty($connection->get_connect_error()))
            throw new DatabaseException($connection, DatabaseException::CONNECTION_EXCEPTION);
        else if (!empty($connection->get_last_error()))
            throw new DatabaseException($connection->get_last_error(), DatabaseException::QUERY_EXCEPTION);
    }


    /**
     * @var TableControl
     */
    protected $tableControl;

    /**
     * TableControlHelper constructor.
     * @param $tableControl
     * @author Abolfazl Alizadeh
     */
    public function __construct($tableControl)
    {
        $this->tableControl = $tableControl;
    }

    /**
     * @param SelectQueryCreator $selectQuery
     * @return array
     * @throws DatabaseException
     */
    public function select_rows($selectQuery)
    {
        $array = $this->tableControl->select_query($selectQuery);
        self::error_handler($this->tableControl);
        return $this->fix_format($array);
    }

    /**
     * @param array|string $condition
     * @param int $page
     * @param int $count
     * @return array
     * @throws DatabaseException
     */
    public function select_rows_by_condition($condition, $page = 1, $count = 10)
    {
        $page -= 1;
        $selectQuery = $this->tableControl->get_select_query_creator();
        $selectQuery->set_limit_max($page * $count, $count);
        $selectQuery->set_condition($condition);
        self::error_handler($this->tableControl);
        $select = $this->tableControl->select_query($selectQuery);
        return $this->fix_format($select);
    }

    /**
     * @param integer $id
     * @return array
     * @throws DatabaseException
     */
    public function select_row_by_id($id)
    {
        $row = $this->select_rows_by_condition(['id' => $id]);
        if (count($row) < 1)
            return array();
        return $row[0];
    }

    /**
     * @throws DatabaseException
     */
    public function check_query_run_result()
    {
        self::error_handler($this->tableControl);
    }

    /**
     * @param integer $id
     * @return boolean
     * @throws DatabaseException
     */
    public function delete_row_by_id($id) {
        $delete = $this->tableControl->delete_query(['id' => $id]);
        $this->check_query_run_result();
        return $delete;
    }

    public function fix_format($array)
    {
        return $array;
    }
}
