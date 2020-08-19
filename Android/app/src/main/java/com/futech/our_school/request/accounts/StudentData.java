package com.futech.our_school.request.accounts;

import com.futech.our_school.objects.GradeData;
import com.futech.our_school.request.accounts.AccountData;

public class StudentData extends AccountData {

    private GradeData.MajorData major;

    public static class Major {
        private int id;
        private String title;

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }
    }

}
