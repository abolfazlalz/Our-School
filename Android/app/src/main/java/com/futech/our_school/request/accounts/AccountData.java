package com.futech.our_school.request.accounts;

public class AccountData {

    private int uid;
    private String phoneNumber;
    private String username;
    private String password;
    private String name;
    private String last_name;
    private String photo;
    private String key;
    private RoleData role;
    private Study study;

    public int getUid() {
        return uid;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return last_name;
    }

    public String getPhoto() {
        return photo;
    }

    public String getKey() {
        return key;
    }

    public RoleData getRole() {
        return role;
    }

    public String getFullName() {
        return String.format("%s %s", name, last_name);
    }

    public Study getStudy() {
        return this.study;
    }

    public static class RoleData {
        public static int TEACHER_Role_ID = 1;
        public static int STUDENT_Role_ID = 2;
        public static int _MAJOR_ID = 3;

        private int id;
        private String title;
        private String image;

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getImage() {
            return image;
        }
    }

    public static class Study {
        private TimeData total;
        private TimeData this_month;

        public static class TimeData {
            private int hour;
            private int minute;
            private int second;

            public int getHour() {
                return hour;
            }

            public int getMinute() {
                return minute;
            }

            public int getSecond() {
                return second;
            }
        }

        public TimeData getTotal() {
            return total;
        }

        public TimeData getThisMonth() {
            return this_month;
        }
    }

}
