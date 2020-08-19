<?php
/**
 * Copyright (c) 2020.
 * All this code was written by Abolfazl Alizadeh.
 */

class LessonTableControl extends TableControlHelper
{
    private $tableCtrl;

    public function __construct()
    {
        parent::__construct(DatabaseControl::get_lesson_table());
    }
}