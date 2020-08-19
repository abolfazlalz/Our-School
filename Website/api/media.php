<?php
/**
 * Copyright (c) 2020.
 * All this code was written by Abolfazl Alizadeh.
 */

include_once 'api.php';

class MediaApi extends ApiHelper
{
    public function __construct()
    {
        parent::__construct();
    }

    /**
     * @param string $request
     * @throws DatabaseException|RequestException|UploadFileException
     * @throws Exception
     */
    public function request_handler($request)
    {
        switch ($request) {
            case 'upload-image':
                $media = new MediaTableControl();
                self::parameters_are_entered(['file'], $_FILES);
                $collectionId = 0;
                if (array_key_exists('collection', $_POST)) {
                    $mediaList = new MediaCollectionControl();
                    $collection = $_POST['collection'];
                    $list = $mediaList->get_collection_by_title($collection);
                    if (count($list) == 0)
                        throw new Exception('no any collection exists by this title: ' . $collection);
                    $collectionId = $list['id'];
                }
                $this->set_status(true);
                $this->set_data('upload_id', $media->add_new_media($_FILES['file'], $collectionId, MediaTableControl::TYPE_IMAGE));
                break;
            case 'upload-new-list':
                self::parameters_are_entered(['file'], $_FILES);
                self::parameters_are_entered(['title'], $_POST);
                $mediaList = new MediaCollectionControl();
                $media = new MediaTableControl();
                $listId = $mediaList->create_new_collection($_POST['title']);
                $uploadId = $media->add_new_media($_FILES['file'], $listId, MediaTableControl::TYPE_IMAGE);
                $this->set_data('upload_id', $uploadId);
                $this->set_data('list_id', $listId);
                break;
        }
    }


}
$m = new MediaApi();
$m->create_api_request();

