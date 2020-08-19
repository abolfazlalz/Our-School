<?php

namespace MySqlConnection;

use mysqli;
use mysqli_result;

class Connection
{
    /**
     * @var string
     */
    private $charset;

    /**
     * @var Server
     */
    private $server;
    /**
     * @var string
     */
    private $database;
    /**
     * @var mysqli
     */
    private $last_connection;

    /**
     * Connection constructor.
     * @param Server $server
     * @param string $database
     */
    public function __construct($server, $database)
    {
        $this->server = $server;
        $this->database = $database;
        $this->charset = 'utf8';
    }

    /**
     * new Connection class without Server object
     * @param string $host
     * @param string $username
     * @param string $password
     * @param string $dbName
     * @return Connection
     */
    public static function create_connection($host, $username, $password, $dbName)
    {
        $server = new Server($host, $username, $password);
        return new Connection($server, $dbName);
    }

    /**
     * set charset to access database
     * @param $charset
     */
    public function set_charset($charset)
    {
        $this->charset = $charset;
    }

    /**
     * return database charset (in default is UTF8)
     * @return string
     */
    public function get_charset()
    {
        return $this->charset;
    }

    /**
     * create new mysql connection
     * @return mysqli
     */
    private function get_connection()
    {
        $mysqli = new mysqli($this->server->getServer(), $this->server->getUsername(), $this->server->getPassword(), $this->database);
        $mysqli->set_charset($this->get_charset());
        return $mysqli;
    }

    /**
     * get a string if your connection has error
     * @return string
     */
    public function get_connect_error()
    {
        return $this->get_connection()->connect_error;
    }

    /**
     * a string description of the last error like your queries error
     * @return string
     */
    public function get_last_error()
    {
        if ($this->last_connection != null)
            return mysqli_error($this->last_connection);
    }

    /**
     * Run and get result from Mysql Query
     * @param string $query
     * @return bool|mysqli_result
     */
    public function run_query($query)
    {
        if ($this->last_connection != null)
            $this->last_connection->close();
        $conn = $this->get_connection();
        $this->last_connection = $conn;
        if ($conn->connect_error) {
            return false;
        }

        return $conn->query($query);
    }

    /**
     * Run your query and get connection used for this run query
     * @param string $query
     * @param mysqli $conn
     * @return bool|mysqli_result
     */
    public function run_query_connection($query, &$conn)
    {
        $conn = $this->get_connection();
        $this->last_connection = $conn;
        if ($conn->connect_error) {
            return false;
        }

        return $conn->query($query);
    }

    /**
     * get result from mysql query have result like queries contain 'SELECT'
     * @param string $query
     * @return array
     */
    public function run_select_query($query)
    {
        $selectValues = array();

        $result = $this->run_query($query);
        if ($result != false && $result->num_rows > 0) {
            while ($row = $result->fetch_assoc()) {
                $select = array();

                foreach (array_keys($row) as $item) {
                    $select[$item] = $row[$item];
                }

                $selectValues[] = $select;
            }
        }

        return $selectValues;
    }

    public function close_last_connection()
    {
        $this->last_connection->close();
    }
}
