package com.futech.our_school.objects;

public class GradeData {

    private int id;
    private String title;
    private int index;
    private MajorData major;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getIndex() {
        return index;
    }

    public MajorData getMajor() {
        return major;
    }

    public static class MajorData {
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
