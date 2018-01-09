package com.coursetable.android.model;

/**
 * Created by H on 2018/1/8.
 */

public class Score {
    /**
     * 学年
     * 学期
     * 课程代码
     * 课程名称
     * 学分
     * 绩点
     * 成绩
     */
    String Years;
    String Semester;
    String courseCode;
    String courseName;
    String Credit;
    String performancePoints;
    String Score;

    public Score(String years, String semester, String courseCode, String courseName, String credit, String performancePoints, String score) {
        Years = years;
        Semester = semester;
        this.courseCode = courseCode;
        this.courseName = courseName;
        Credit = credit;
        this.performancePoints = performancePoints;
        Score = score;
    }

    public String getYears() {
        return Years;
    }

    public void setYears(String years) {
        Years = years;
    }

    public String getSemester() {
        return Semester;
    }

    public void setSemester(String semester) {
        Semester = semester;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCredit() {
        return Credit;
    }

    public void setCredit(String credit) {
        Credit = credit;
    }

    public String getPerformancePoints() {
        return performancePoints;
    }

    public void setPerformancePoints(String performancePoints) {
        this.performancePoints = performancePoints;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }

    @Override
    public String toString() {
        return "学年:" + getYears() + "   学期:" + getSemester() + "    课程代码:" + getCourseCode() + "    课程名称:" + getCourseName() + "    学分:" + getCredit() + "  绩点:" + getPerformancePoints() + "   成绩:" + getScore();
    }
}
