package com.futech.our_school.request.schedules;

import com.futech.our_school.objects.StudyDurationData;
import com.futech.our_school.request.ApiTemplate;

import java.util.List;

public class StudyHistoryApiData extends ApiTemplate {

    private Data data;

    public Data getData() {
        return data;
    }

    public class Data extends ApiTemplate.Data {
        private List<StudyDurationData> items;

        public List<StudyDurationData> getItems() {
            return items;
        }
    }

}
