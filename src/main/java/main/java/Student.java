package main.java;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Student implements Serializable {
    String name;
    String studentID;
    String major;
    String semesterAdmittedToProgram;
    List<Course> courses;
    private List<Course> coreCourses = new ArrayList<>();
    private List<Course> electiveCourses = new ArrayList<>();
    private List<String> prerequisites = new ArrayList<>();
    private List<Course> levelingCourses = new ArrayList<>();
    public Student(String name, String studentID, String major, String semesterAdmittedToProgram) {
        this.name = name;
        this.studentID = studentID;
        this.major = major;
        this.semesterAdmittedToProgram = semesterAdmittedToProgram;
        this.courses = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getSemesterAdmittedToProgram() {
        return semesterAdmittedToProgram;
    }

    public void setSemesterAdmittedToProgram(String semesterAdmittedToProgram) {
        this.semesterAdmittedToProgram = semesterAdmittedToProgram;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public void addCourse(Course course) {
        courses.add(course);
    }
    public void applyAdditionalRules() {
        removeCsIppAssignment();
        handleElectiveCourses();
    }

    public void categorizeCoursesByTrack(String track,List<String> prerequisites) {
        List<String> coreCourses = new ArrayList<>();
        List<String> electiveCourses = new ArrayList<>();
        List<String> levelingCourses = new ArrayList<>();
        switch (track) {
            case "Data Science":
                coreCourses = Arrays.asList("CS6313", "CS6350", "CS6363", "CS6375", "CS6360"); // Add core courses for this track
                electiveCourses = Arrays.asList("CS5343", "CS6301", "CS6314", "CS6323", "CS6334", "CS6385");
                levelingCourses = Arrays.asList("CS3341","CS2340","CS5333","CS5343","CS5303","CS5348");
                break;
            // Add cases for other tracks and their respective core and elective courses
        }


        for (Course course : this.courses) {
            String fullCourseCode = "CS " + course.getCourseCode();
            if (coreCourses.contains(course.getCourseCode())) {
                this.coreCourses.add(course);
            } else if (electiveCourses.contains(course.getCourseCode())) {
                this.electiveCourses.add(course);
            } else if (levelingCourses.contains(course.getCourseCode()) || prerequisites.contains(course.getCourseCode())) {
                if (!this.levelingCourses.contains(course)) {
                    this.levelingCourses.add(course);
                }
            }
        }
    }
//may be final if not, idk just know I changed it to final


    public List<Course> getCoreCourses() {
        return coreCourses;
    }

    public List<Course> getElectiveCourses() {
        return electiveCourses;
    }
    public List<Course> getLevelingCourses() {
        return levelingCourses;
    }
    public void addLevelingCourse(Course course) {
        levelingCourses.add(course);
    }
    private void removeCsIppAssignment() {
        courses.removeIf(course -> course.getCourseCode().equals("5177") && course.getCourseName().equals("CS IPP ASSIGNMENT"));
    }

    public double calculateCoreGPA() {
        double totalGradePoints = 0;
        double totalCreditHours = 0;
        for (Course course : coreCourses) {
            String grade = course.getGrade();
            if (!grade.equals("N/A")) {
                double gradeValue = course.getGradeValue();
                double creditHours = course.getCreditHours();
                totalGradePoints += gradeValue * creditHours;
                totalCreditHours += creditHours;
            }
        }
        return totalCreditHours > 0 ? totalGradePoints / totalCreditHours : 0;
    }

    public double calculateElectiveGPA() {
        double totalGradePoints = 0;
        double totalCreditHours = 0;
        for (Course course : electiveCourses) {
            String grade = course.getGrade();
            if (!grade.equals("N/A")) {
                double gradeValue = course.getGradeValue();
                double creditHours = course.getCreditHours();
                totalGradePoints += gradeValue * creditHours;
                totalCreditHours += creditHours;
            }
        }
        return totalCreditHours > 0 ? totalGradePoints / totalCreditHours : 0;
    }
    private void handleElectiveCourses() {
        Course electiveCourse = null;
        List<Course> coursesToRemove = new ArrayList<>();

        for (Course course : courses) {
            if (course.getCourseCode().equals("5333") || course.getCourseCode().equals("5343") || course.getCourseCode().equals("5348")) {
                if (electiveCourse == null) {
                    electiveCourse = course;
                } else if (course.getGradeValue() > electiveCourse.getGradeValue()) {
                    coursesToRemove.add(electiveCourse);
                    electiveCourse = course;
                } else if (course.getGradeValue() == electiveCourse.getGradeValue()) {
                    if (course.getCourseCode().equals("5343")) {
                        coursesToRemove.add(electiveCourse);
                        electiveCourse = course;
                    } else if (course.getCourseCode().equals("5348") && !electiveCourse.getCourseCode().equals("5343")) {
                        coursesToRemove.add(electiveCourse);
                        electiveCourse = course;
                    } else {
                        coursesToRemove.add(course);
                    }
                } else {
                    coursesToRemove.add(course);
                }
            }
        }

        courses.removeAll(coursesToRemove);
    }

    public void saveToFile(String filename) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(filename + ".ser");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(this);
        out.close();
        fileOut.close();
    }

    public static Student loadFromFile(String filename) throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream(filename + ".ser");
        ObjectInputStream in = new ObjectInputStream(fileIn);
        Student student = (Student) in.readObject();
        in.close();
        fileIn.close();
        return student;
    }

}

