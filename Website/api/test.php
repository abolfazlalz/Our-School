<?php
/**
 * Copyright (c) 2020.
 * All this code was written by Abolfazl Alizadeh.
 */


use MySqlConnection\Server;
use MySqlConnection\TableControl;

$server = new Server('localhost', 'root', '');
$connection = new \MySqlConnection\Connection($server, 'my_db');

$select = new \MySqlConnection\SelectQueryCreator('test');
$select->set_condition(['id' => 'hello']);
$selectItems = $connection->run_select_query($select);