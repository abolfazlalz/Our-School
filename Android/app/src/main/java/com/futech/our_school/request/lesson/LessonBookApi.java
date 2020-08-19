package com.futech.our_school.request.lesson;

import com.futech.our_school.objects.LessonBookData;
import com.futech.our_school.request.ApiTemplate;

import java.util.List;

public class LessonBookApi extends ApiTemplate {

    public static class Data extends ApiTemplate.Data {
        private List<LessonBookData> books;

        public List<LessonBookData> getBooks() {
            return books;
        }
    }

    private Data data;
    public Data getData() {
        return data;
    }

}
