package com.futech.our_school.request.upload;

import com.futech.our_school.request.ApiTemplate;

public class UploadRequestData extends ApiTemplate {

    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data extends ApiTemplate.Data {
        private int upload_id = -1;
        private int list_id = -1;

        public int getUploadId() {
            return upload_id;
        }

        public int getListId() {
            return list_id;
        }
    }

}
