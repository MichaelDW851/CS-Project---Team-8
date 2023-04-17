class Course {
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
        return "Course: " + courseCode + " - " + courseName + ", " + "Year: " + year + ", " + "Season: " + semester + ", " + "Credit Hours: " + creditHours + ", " + "Earned Credit Hours: " + earnedCreditHours + ", " + "Grade: " + grade;
    }
    // Add any other necessary methods
}