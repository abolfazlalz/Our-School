package com.futech.our_school.request;

import java.util.Date;

public abstract class ApiTemplate {

    private About about;

    public About getAbout() {
        return about;
    }

    public static class Data {

        private boolean status;
        private int status_code;
        private String msg;

        public String getMessage() {
            return msg;
        }

        public boolean isSuccess() {
            return status;
        }

        public int getStatusCode() {
            return status_code;
        }
    }

    public static class About {
        public static String JSON_FORMAT = "json";
        public static String XML_FORMAT = "xml";

        private Date date;
        private String format;

        public Date getDate() {
            return date;
        }

        public String getFormat() {
            return format;
        }
    }

}
