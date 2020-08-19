package com.futech.our_school.request.school;

public class SchoolApiData {

    Data data;

    public Data getData() {
        return data;
    }

    class Data {
        private String msg;
        private boolean status;
        private int status_code;
        private SchoolData[] schools;

        public String getMsg() {
            return msg;
        }

        public boolean isStatus() {
            return status;
        }

        public int getStatus_code() {
            return status_code;
        }

        public SchoolData[] getSchools() {
            return schools;
        }
    }

}
