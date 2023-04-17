package main.java;
import java.io.Serializable;
import java.util.List;

public class Course implements Serializable {
    String year;
    String semester;
    String courseCode;
    String courseName;
    double creditHours;
    double earnedCreditHours;
    String grade;

    public Course(String year, String semester, String courseCode, String courseName, double creditHours, double earnedCreditHours, String grade) {
        this.year = year;
        this.semester = semester;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.creditHours = creditHours;
        this.earnedCreditHours = earnedCreditHours;
        this.grade = grade;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
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

    public double getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(double creditHours) {
        this.creditHours = creditHours;
    }

    public double getEarnedCreditHours() {
        return earnedCreditHours;
    }

    public void setEarnedCreditHours(double earnedCreditHours) {
        this.earnedCreditHours = earnedCreditHours;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }


//    @Override
//    public String toString() {
//        return "Course: " + courseCode + " - " + courseName + ", " + "Year: " + year + ", " + "Semester: " + semester + ", " + "Credit Hours: " + creditHours + ", " + "Earned Credit Hours: " + earnedCreditHours + ", " + "Grade: " + grade;
//    }

    public String toString() {
        return courseCode + " - " + courseName;
    }
    private String getSeasonString() {
        if (semester != null && !semester.isEmpty()) {
            int month = Integer.parseInt(semester.substring(5, 7));
            if (month == 1) {
                return "Spring";
            } else if (month == 5) {
                return "Summer";
            } else if (month == 8) {
                return "Fall";
            }
        }
        return "";
    }

    public double getGradeValue() {
        switch (grade) {
            case "A":
                return 4.0;
            case "A-":
                return 3.670;
            case "B+":
                return 3.330;
            case "B":
                return 3.0;
            case "B-":
                return 2.670;
            case "C+":
                return 2.330;
            case "C":
                return 2.0;
            case "F":
                return 0;
            default:
                return 0;

        }
    }
}