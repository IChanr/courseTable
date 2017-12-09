package com.coursetable.android.model;

/**
 * Created by Caihan on 2017/12/9.
 */

public class Course {
    String courseName;
    String courseTeacher;
    String courseTime;
    String courseLocation;

    public Course(String courseName, String courseTeacher, String courseTime, String
            courseLocation) {
        this.courseName = courseName;
        this.courseTeacher = courseTeacher;
        this.courseTime = courseTime;
        this.courseLocation = courseLocation;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseTeacher() {
        return courseTeacher;
    }

    public void setCourseTeacher(String courseTeacher) {
        this.courseTeacher = courseTeacher;
    }

    public String getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
    }

    public String getCourseLocation() {
        return courseLocation;
    }

    public void setCourseLocation(String courseLocation) {
        this.courseLocation = courseLocation;
    }

    public int position(String str) {
        if (str.contains("周一第1,2节")) {
            return 0;
        }
        if (str.contains("周二第1,2节")) {
            return 1;
        }
        if (str.contains("周三第1,2节")) {
            return 2;
        }
        if (str.contains("周四第1,2节")) {
            return 3;
        }
        if (str.contains("周五第1,2节")) {
            return 4;
        }
        if (str.contains("周六第1,2节")) {
            return 5;
        }
        if (str.contains("周日第1,2节")) {
            return 6;
        }


        if (str.contains("周一第3,4节")) {
            return 7;
        }
        if (str.contains("周二第3,4节")) {
            return 8;
        }
        if (str.contains("周三第3,4节")) {
            return 9;
        }
        if (str.contains("周四第3,4节")) {
            return 10;
        }
        if (str.contains("周五第3,4节")) {
            return 11;
        }
        if (str.contains("周六第3,4节")) {
            return 12;
        }
        if (str.contains("周日第3,4节")) {
            return 13;
        }


        if (str.contains("周一第5,6节")) {
            return 14;
        }
        if (str.contains("周二第5,6节")) {
            return 15;
        }
        if (str.contains("周三第5,6节")) {
            return 16;
        }
        if (str.contains("周四第5,6节")) {
            return 17;
        }
        if (str.contains("周五第5,6节")) {
            return 18;
        }
        if (str.contains("周六第5,6节")) {
            return 19;
        }
        if (str.contains("周日第5,6节")) {
            return 20;
        }


        if (str.contains("周一第7,8节")) {
            return 21;
        }
        if (str.contains("周二第7,8节")) {
            return 22;
        }
        if (str.contains("周三第7,8节")) {
            return 23;
        }
        if (str.contains("周四第7,8节")) {
            return 24;
        }
        if (str.contains("周五第7,8节")) {
            return 25;
        }
        if (str.contains("周六第7,8节")) {
            return 26;
        }
        if (str.contains("周日第7,8节")) {
            return 27;
        }


        if (str.contains("周一第9,10节")) {
            return 28;
        }
        if (str.contains("周二第9,10节")) {
            return 29;
        }
        if (str.contains("周三第9,10节")) {
            return 30;
        }
        if (str.contains("周四第9,10节")) {
            return 31;
        }
        if (str.contains("周五第9,10节")) {
            return 32;
        }
        if (str.contains("周六第9,10节")) {
            return 33;
        }
        if (str.contains("周日第9,10节")) {
            return 34;
        }


        if (str.contains("周一第11,12节")) {
            return 35;
        }
        if (str.contains("周二第11,12节")) {
            return 36;
        }
        if (str.contains("周三第11,12节")) {
            return 37;
        }
        if (str.contains("周四第11,12节")) {
            return 38;
        }
        if (str.contains("周五第11,12节")) {
            return 39;
        }
        if (str.contains("周六第11,12节")) {
            return 40;
        }
        if (str.contains("周日第11,12节")) {
            return 41;
        }
        return 42;

    }

    @Override
    public String toString() {
        return getCourseName() + "@" + courseLocation;
    }
}
