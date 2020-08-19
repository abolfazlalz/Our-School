package com.futech.our_school.utils.request.values;

public class ConfirmCodeApi {

    private Data data;
    public Data getData() {
        return data;
    }

    public static class Data
    {
        private String msg;
        private boolean status;
        private int code;

        public String getMsg() {
            return msg;
        }

        public boolean isStatus() {
            return status;
        }

        public int getCode() {
            return code;
        }
    }

}
