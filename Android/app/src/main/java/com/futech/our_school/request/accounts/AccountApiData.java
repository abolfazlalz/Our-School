package com.futech.our_school.request.accounts;

import com.futech.our_school.request.ApiTemplate;

public class AccountApiData extends ApiTemplate {

    public static class Data extends ApiTemplate.Data {

        private AccountData information;

        public AccountData getInformation() {
            return information;
        }
    }

    private Data data;
    public Data getData() {
        return data;
    }

}
