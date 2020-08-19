package com.futech.our_school.objects;

import com.futech.our_school.request.school.SchoolClassData;
import com.futech.our_school.request.school.SchoolData;
import com.futech.our_school.utils.request.TimeData;

import java.time.DateTimeException;
import java.util.Date;

public class ClassData {

    private int id;
    private String description;
    private String startTime;
    private String endTime;
    private int dayInWeek;
    private int repeat;
    private SchoolData school;
    private SchoolClassData schoolClass;

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TimeData getStartTime() {
        return new TimeData(startTime);
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public TimeData getEndTime() {
        return new TimeData(endTime);
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getDayInWeek() {
        return dayInWeek;
    }

    public void setDayInWeek(int dayInWeek) {
        this.dayInWeek = dayInWeek;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public SchoolData getSchool() {
        return school;
    }

    public void setSchool(SchoolData school) {
        this.school = school;
    }

    public SchoolClassData getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClassData schoolClass) {
        this.schoolClass = schoolClass;
    }
}
