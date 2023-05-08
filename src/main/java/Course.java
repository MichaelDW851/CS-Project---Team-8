
public class Course {
    String year;
    String semester;
    String courseCode;
    String courseName;
    double creditHours;
    double earnedCreditHours;
    String grade;

    String season;
    public Course(String year, String semester, String courseCode, String courseName, double creditHours, double earnedCreditHours, String grade) {
        this.year = year;
        setSemester(semester);
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.creditHours = creditHours;
        this.earnedCreditHours = earnedCreditHours;
        this.grade = grade;
    }

    //temporary way to add pre-reqs via strings
    public Course(String courseCode, String courseName) {
        this.courseCode = courseCode;
        this.courseName = courseName;
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
     //   this.semester = semester;
        System.out.println(semester);

        String[] semesterParse = semester.split("-");
        String semesterYear = semesterParse[0].substring(semesterParse[0].length()-2);
        String semesterCode = semesterParse[1];
        System.out.println(semesterYear);
        System.out.println(semesterCode);
        if(semesterCode.equals("01")) {
            this.semester = semesterYear + "S";
            System.out.println("Spring");
        }
        else if(semesterCode.equals("02")) {
            this.semester = semesterYear + "U";
            System.out.println("Summer");
        }
        else if(semesterCode.equals("03")) {
            this.semester = semesterYear + "F";
            System.out.println("Fall");
        }
        System.out.println(this.semester);
    }
//comment 62
    public String getCourseCode() {
        return courseCode;
    }


    public String getCourseName() {
        return courseName;
    }



    public double getCreditHours() {
        return creditHours;
    }



    public double getEarnedCreditHours() {
        return earnedCreditHours;
    }



    public String getGrade() {
        return grade;
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
                return 4.000;
            case "A-":
                return 3.670;
            case "B+":
                return 3.330;
            case "B":
                return 3.000;
            case "B-":
                return 2.670;
            case "C+":
                return 2.330;
            case "C":
                return 2.000;
            case "C-":
                return 1.670;
            case "D+":
                return 1.330;
            case "D":
                return 1.000;
            case "D-":
                return 0.670;
            case "F":
                return 0.000;
            default:
                return 0.000;
        }
    }


}