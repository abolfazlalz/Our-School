package com.futech.our_school.request.school;

import androidx.annotation.NonNull;

import com.futech.our_school.objects.GradeData;

public class SchoolClassData {

    private int id;
    private String title;
    private GradeData grade;
    private SchoolData school;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public GradeData getGrade() {
        return grade;
    }

    public SchoolData getSchool() {
        return school;
    }

    @NonNull
    @Override
    public String toString() {
        return title;
    }
}
