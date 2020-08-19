<?php
/**
 * Copyright (c) 2020.
 * All this code was written by Abolfazl Alizadeh.
 */

class MediaTableControl extends TableControlHelper
{


    const TYPE_IMAGE = 'image';
    const TYPE_VIDEO = 'video';

    public function __construct()
    {
        parent::__construct(DatabaseControl::get_media());
    }

    /**
     * @param $file
     * @param string|integer $collection
     * @param $type
     * @return int
     * @throws DatabaseException|UploadFileException
     */
    public function add_new_media($file, $collection, $type)
    {
        $type = $this->check_entered_type($type);

        if (is_string($collection)) {
            $mediaCollection = new MediaCollectionControl();
            $collection = $mediaCollection->create_new_collection($collection);
        }

        $link = '/uploads/' . date('Y') . '/' . date('m') . '/images/';
        $address = $_SERVER['DOCUMENT_ROOT'] . $link;
        if (is_dir($address) === false)
            mkdir($address, 0777, true);

        $basename = basename($file['name']);
        $imageFileType = strtolower(pathinfo($basename, PATHINFO_EXTENSION));
        if ($imageFileType == '')
            $imageFileType = 'png';
        $address .= date('h-i-s') . '.' . $imageFileType;

        $resultUpload = UploadFileHelper::uploadFile($file, $address, 10000000);
        if ($resultUpload) {
            $data = ['address' => $link, 'collectionId' => $collection, 'type' => $type];
            $insert = $this->tableControl->insert_query($data);
            $this->check_query_run_result();
            return $insert;
        }
        return -1;
    }

    private function check_entered_type($type)
    {
        if ($type != self::TYPE_IMAGE && $type != self::TYPE_VIDEO) return self::TYPE_IMAGE;
        return $type;
    }

}