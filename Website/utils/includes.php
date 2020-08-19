<?php
/**
 * Copyright (c) 2020.
 * All this code was written by Abolfazl Alizadeh.
 */


/*
 * i wrote 2 method for automate including files
 * 1. using spl_autoload_register function
 * 2. using include all files on one time
 * */

date_default_timezone_set('Asia/Tehran');

/**
 * set full path file
 * @param string $file
 * @return string
 * @author Abolfazl Alizadeh
 */
function setFullPathFile($file)
{
    $dir = '../';
    while (!file_exists($dir . $file)) {
        $dir .= '../';
    }
    return $dir . $file;
}

/**
 * Method 2: Include all files in one time
 */
function loadFromDirectories()
{
    $directoriesToIncluding = ['tableControl', 'exception', 'utils'];
    foreach ($directoriesToIncluding as $targetDirectory) {
        $targetDirectory = setFullPathFile($targetDirectory);
        foreach (scandir($targetDirectory) as $directory) {
            $directory = $targetDirectory . '/' . $directory;
            if (is_file($directory) && (pathinfo($directory, PATHINFO_EXTENSION)) == '.php') {
                try {
                    include_once $directory;
                    continue;
                } catch (Exception $ex) {
                    echo $ex->getMessage();
                }
            }
            $pattern = $directory . '/*.php';
            foreach (glob($pattern) as $file) {
                try {
                    include_once $file;
                } catch (Exception $ex) {
                    echo $ex->getMessage();
                }
            }
        }
    }
}

loadFromDirectories();