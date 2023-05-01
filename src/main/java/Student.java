import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Student implements Serializable {
    String name;
    String studentID;
    String major;
    String semesterAdmittedToProgram;

    private String track;
    private boolean isFastTrack;
    private boolean isThesisMasters;
    List<Course> courses;
    private List<Course> coreCourses = new ArrayList<>();
    private List<Course> electiveCourses = new ArrayList<>();
    private List<Course> prerequisites = new ArrayList<>();
    private List<Course> levelingCourses = new ArrayList<>();
    public Student(String name, String studentID, String major, String semesterAdmittedToProgram, boolean isFastTrack, boolean isThesisMasters) {
        this.name = name;
        this.studentID = studentID;
        this.major = major;
        this.semesterAdmittedToProgram = semesterAdmittedToProgram;
        this.courses = new ArrayList<>();
        this.isFastTrack = isFastTrack;
        this.isThesisMasters = isThesisMasters;

    }

    void setFastTrack() {
        isFastTrack = true;
    }

    void setThesisMasters() {
        isThesisMasters = true;
    }
    public boolean getFastTrackCheck(){
        if (isFastTrack) return true;
        return false;
    }
    public boolean getThesisMastersCheck(){
        if (isThesisMasters) return true;
        return false;
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
        // Dont add course if its 5117
        if (!course.getCourseCode().equals("CSC5177")) {
            courses.add(course);
        }
       // System.out.println(course.courseCode + " " + course.getSemester());
    }

    public void addPrereq(Course prereqs) {
        // Dont add course if its 5117
        if (!prereqs.getCourseCode().equals("CSC5177")) {
            courses.add(prereqs);
            System.out.println("adding " + prereqs.courseName + " to prereqs");
        }
        System.out.println(prerequisites);
    }

    public void addPrereq(String prereq) {
        String[] courseInfo = prereq.split(" ");
        String courseCode = courseInfo[0] + " " + courseInfo[1];
        String courseName = "";
        for (int i = 2;i < courseInfo.length;i++) {
            courseName += courseInfo[i] + " ";
        }
        Course preReq = new Course(courseCode,courseName);
        addPrereq(preReq);

    }
    public void applyAdditionalRules() {
        removeCsIppAssignment();
        handleElectiveCourses();
    }
    public List<Course> getCourseList() {
        return courses;
    }

    public List<Course> getPrerequisites(){
        System.out.println("prereqs- " + prerequisites);
        return prerequisites;
    }


    public void categorizeCoursesByTrack(String track,List<String> prerequisites) {
        List<String> coreCourses = new ArrayList<>();
        List<String> trackElectives = new ArrayList<>();
        List<String> levelingCourses = new ArrayList<>();
        this.track = track;
        switch (track) {

            case "Data Science":
                coreCourses = Arrays.asList("CS6313", "CS6350", "CS6363", "CS6375", "CS6360"); // Add core courses for this track
                trackElectives = Arrays.asList("CS6301", "CS6320", "CS6327", "CS6347", "CS6360");
                levelingCourses = Arrays.asList("CS3341","CS5333","CS5343","CS5303","CS5348","CS5330");
                break;
            // Add cases for other tracks and their respective core and elective courses
            case "Systems":
;               coreCourses = Arrays.asList("CS6304","CS6363","CS6378","CS6396");
                trackElectives = Arrays.asList("CS6349","CS6376","CS6380","CS6397","CS6399");
                levelingCourses = Arrays.asList("CS5303","CS5330","CS5333","CS5343","CS5348","CS5390");
                break;
            case "Interactive Computing":
                coreCourses = Arrays.asList("CS6326","CS6363");
                trackElectives = Arrays.asList("CS6323","CS6328","CS6331","CS6334","CS6366");
                levelingCourses = Arrays.asList("CS5303","CS5330","CS5333","CS5343","CS5348");
                break;
            case "Cyber Security":
                coreCourses = Arrays.asList("CS6324","CS6363","CS6378");
                trackElectives = Arrays.asList("CS6332","CS6348","CS6349","CS6377");
                levelingCourses = Arrays.asList("CS5303","CS5330","CS5333","CS5343","CS5348","CS5390");
                break;
            case "Intelligent Systems":
                coreCourses = Arrays.asList("CS6320","CS6363","CS6364","CS6375");
                trackElectives = Arrays.asList("CS6360","CS6378");
                levelingCourses = Arrays.asList("CS5303","CS5330","CS5343","CS5333","CS5348");
                break;

            case "Networks and Telecommunications":
                coreCourses = Arrays.asList("CS6352","CS6363","CS6378","CS6385","CS6390");
                levelingCourses = Arrays.asList("CS5303","CS5330","CS5343","CS5348","CS5390","CS3341","CS5333");
                break;
            case "Traditional Computer Science":
                coreCourses = Arrays.asList("CS6363","CS6378","CS6390");
                trackElectives = Arrays.asList("CS6353","CS6360","CS6371");
                levelingCourses = Arrays.asList("CS5303","CS5330","CS5333","CS5343","CS5348","CS5349","CS5390");
        }


        for (Course course : this.courses) {
            System.out.println(course.getCourseCode());
            int trackElectiveCounter = 0;
            if (coreCourses.contains(course.getCourseCode())) {
                this.coreCourses.add(course);
                System.out.println("Adding " + course.getCourseCode() + " to cores");
            } else if (trackElectives.contains(course.getCourseCode())) {
                //need to add in electives
                this.coreCourses.add(course);
                System.out.println("Adding " + course.getCourseCode() + " to cores");
            } else if (levelingCourses.contains(course.getCourseCode()) || prerequisites.contains(course.getCourseCode())) {
                if (!this.levelingCourses.contains(course)) {
                    this.levelingCourses.add(course);
                }
            }
        }
    }
//may be final if not, idk just know I changed it to final

    public String getTrack() {
        return track;
    }


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
    public void removeCsIppAssignment() {
        courses.removeIf(course -> course.getCourseCode().equals("5177") && course.getCourseName().equals("CS IPP ASSIGNMENT"));
    }

    public double calculateCoreGPA() {
        double totalGradePoints = 0;
        double totalCreditHours = 0;
        int coreCounter = 0;
        for (Course course : coreCourses) {
            if(coreCounter >= 5) {
                break;
            }
            String grade = course.getGrade();
            if (!grade.equals("N/A")) {
                double gradeValue = course.getGradeValue();
                double creditHours = course.getCreditHours();
                totalGradePoints += gradeValue * creditHours;
                totalCreditHours += creditHours;
            }
            coreCounter++;
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
