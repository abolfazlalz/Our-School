package com.futech.our_school.request.schedules;

import com.futech.our_school.objects.ClassData;
import com.futech.our_school.request.ApiTemplate;

import java.util.List;

public class ClassApiData extends ApiTemplate {

    private Data data;
    public Data getData() {
        return data;
    }

    public class Data extends ApiTemplate.Data {
        private List<ClassData> items;

        public List<ClassData> getItems() {
            return items;
        }
    }

}
