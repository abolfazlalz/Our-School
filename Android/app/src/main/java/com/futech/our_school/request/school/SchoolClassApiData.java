package com.futech.our_school.request.school;

import com.futech.our_school.request.ApiTemplate;

public class SchoolClassApiData extends ApiTemplate {

    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data extends ApiTemplate.Data {
        private SchoolClassData[] school_class;
        SchoolClassData[] getSchoolClass() {
            return school_class;
        }
    }

}
