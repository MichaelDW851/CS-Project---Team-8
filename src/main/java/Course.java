import java.io.Serializable;

public class Course implements Serializable {
    //instance variables
    String year;
    String semester;
    String courseCode;
    String courseName;
    double creditHours;
    double earnedCreditHours;
    String grade;
    String season;

    //Constructor for Coures, will have all the information needed for course calculations and printing
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

    //Setter and getter methods
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

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

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
     //   this.semester = semester;
        System.out.println(semester);
        //Split the input string into two parts at the - character
        String[] semesterParse = semester.split("-");

        //Extracts the last two characters of the first part of the split string as the year
        //the year IS the last two characters of the string
        String semesterYear = semesterParse[0].substring(semesterParse[0].length()-2);
        //Extract second part
        String semesterCode = semesterParse[1];

        System.out.println(semesterYear);
        System.out.println(semesterCode);
        //Determines the season based on semester code and set the semester as needed
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

    //This method is used to print in the terminal to assure we are getting ALL the right information
    @Override
    public String toString() {
        return "Course: " + courseCode + " - " + courseName + ", " + "Year: " + year + ", " + "Semester: " + semester + ", " + "Credit Hours: " + creditHours + ", " + "Earned Credit Hours: " + earnedCreditHours + ", " + "Grade: " + grade;
    }

    //converts GPA to letter
    public static String getLetterGradeFromValue(double gradeValue) {
        if (gradeValue >= 4.0) {
            return "A";
        } else if (gradeValue >= 3.67) {
            return "A-";
        } else if (gradeValue >= 3.33) {
            return "B+";
        } else if (gradeValue >= 3.0) {
            return "B";
        } else if (gradeValue >= 2.67) {
            return "B-";
        } else if (gradeValue >= 2.33) {
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

    //converts letter grade to grade value
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