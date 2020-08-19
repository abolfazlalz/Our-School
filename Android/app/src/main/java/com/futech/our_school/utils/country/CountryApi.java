package com.futech.our_school.utils.country;

public class CountryApi {

    Data data;

    Data getData() {
        return data;
    }

    class Data {
        private boolean status;
        private int status_code;
        private CityData[] cities;
        private StateData[] states;
        private int page;
        private int total_page;

        public boolean isStatus() {
            return status;
        }

        public int getStatus_code() {
            return status_code;
        }

        public CityData[] getCities() {
            return cities;
        }

        public int getPage() {
            return page;
        }

        public int getTotal_page() {
            return total_page;
        }

        public StateData[] getStates() {
            return states;
        }
    }

}
