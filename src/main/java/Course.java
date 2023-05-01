
public class Course {
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

    @Override
    public String toString() {
        return "Course: " + courseCode + " - " + courseName + ", " + "Year: " + year + ", " + "Semester: " + semester + ", " + "Credit Hours: " + creditHours + ", " + "Earned Credit Hours: " + earnedCreditHours + ", " + "Grade: " + grade;
    }
    public static String getLetterGradeFromValue(double gradeValue) {
        if (gradeValue >= 4.0) {
            return "A";
        } else if (gradeValue >= 3.7) {
            return "A-";
        } else if (gradeValue >= 3.3) {
            return "B+";
        } else if (gradeValue >= 3.0) {
            return "B";
        } else if (gradeValue >= 2.7) {
            return "B-";
        } else if (gradeValue >= 2.3) {
            return "C+";
        } else if (gradeValue >= 2.0) {
            return "C";
        } else if (gradeValue >= 1.7) {
            return "C-";
        } else if (gradeValue >= 1.3) {
            return "D+";
        } else if (gradeValue >= 1.0) {
            return "D";
        } else if (gradeValue >= 0.7) {
            return "D-";
        } else {
            return "F";
        }
    }
    public double getGradeValue() {
        switch (grade) {
            case "A":
                return 4.0;
            case "A-":
                return 3.7;
            case "B+":
                return 3.3;
            case "B":
                return 3.0;
            case "B-":
                return 2.7;
            case "C+":
                return 2.3;
            case "C":
                return 2.0;
            case "C-":
                return 1.7;
            case "D+":
                return 1.3;
            case "D":
                return 1.0;
            case "D-":
                return 0.7;
            case "F":
                return 0.0;
            default:
                return 0.0;
        }
    }


}