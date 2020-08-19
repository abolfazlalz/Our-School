<?php


class UploadFileHelper
{
    /**
     * @param $file
     * @param $target_file
     * @param int $size
     * @param array $formats
     * @return bool
     * @throws UploadFileException
     */
    public static function uploadFile($file, $target_file, $size = -1, $formats = [])
    {
        $basename = basename($file['name']);
        $imageFileType = strtolower(pathinfo($basename, PATHINFO_EXTENSION));

        if (isset($_POST["submit"])) {
            $check = getimagesize($file["tmp_name"]);
            if ($check === false) {
                throw new UploadFileException(UploadFileException::FILE_NOT_VALID);
            }
        }
        if (file_exists($target_file)) {
            throw new UploadFileException(UploadFileException::FILE_EXISTS);
        }
        if ($size != -1 && $file["size"] > $size) {
            throw new UploadFileException(UploadFileException::FILE_IS_LARGE);
        }
        if (count($formats) > 0 && array_key_exists($imageFileType, $formats)) {
            throw new UploadFileException(UploadFileException::FILE_FORMAT);
        }

        if (move_uploaded_file($file["tmp_name"], $target_file)) {
            return true;
        } else {
            return false;
        }
    }
}