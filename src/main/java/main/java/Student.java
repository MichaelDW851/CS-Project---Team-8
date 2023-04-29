package main.java;
import java.io.*;
import java.util.*;


public class Student implements Serializable {
    String name;
    String studentID;
    String major;
    String semesterAdmittedToProgram;

    boolean isFastTrack;
    boolean isThesisMasters;
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
    public double getNeededGPAInRemainingCourses(double currentGPA, int totalCourses, List<Course> remainingCourses) {
        double desiredTotalGradePoints = 3.0 * totalCourses;
        double currentTotalGradePoints = currentGPA * (totalCourses - remainingCourses.size());

        double neededTotalGradePointsInRemainingCourses = desiredTotalGradePoints - currentTotalGradePoints;
        double neededGPAInRemainingCourses = neededTotalGradePointsInRemainingCourses / remainingCourses.size();

        return neededGPAInRemainingCourses;
    }

    public List<Course> getRemainingCourses() {
        List<Course> remainingCourses = new ArrayList<>();
        for (Course course : courses) {
            if (course.getGrade().equalsIgnoreCase("N/A")) {
                remainingCourses.add(course);
            }
        }
        return remainingCourses;
    }
    public String getRemainingOverallGPAMessage(double desiredOverallGPA) {
        List<Course> remainingCourses = new ArrayList<>();
        remainingCourses.addAll(getRemainingCoreCourses());
        remainingCourses.addAll(getRemainingElectiveCourses());

        StringBuilder message = new StringBuilder();

        if (!remainingCourses.isEmpty()) {
            double currentOverallGPA = calculateOverallGPA();
            int totalCourses = coreCourses.size() + electiveCourses.size();
            int remainingCoursesCount = remainingCourses.size();
            double requiredRemainingGPA = (desiredOverallGPA * (totalCourses + remainingCoursesCount) - currentOverallGPA * totalCourses) / remainingCoursesCount;

            if (requiredRemainingGPA >= 2.00) {
                message.append(String.format("To maintain an overall GPA of %.2f, the student needs a GPA >= %.2f in the remaining courses:\n", desiredOverallGPA, requiredRemainingGPA));

                appendCourseList(remainingCourses, message);
            } else {
                message.append(String.format("To maintain an overall GPA of %.2f, the student must pass the remaining courses:\n", desiredOverallGPA));
                appendCourseList(remainingCourses, message);
            }
        } else {
            message.append(String.format("All courses complete. Current overall GPA: %.2f\n", calculateOverallGPA()));
        }

        return message.toString();
    }

    public String getRemainingCoursesMessage() {
        List<Course> remainingCoreCourses = getRemainingCoreCourses();
        List<Course> remainingElectiveCourses = getRemainingElectiveCourses();

        StringBuilder message = new StringBuilder();

        if (!remainingCoreCourses.isEmpty()) {
            double remainingCoreGPA = getRemainingGPA(remainingCoreCourses, 3.19);
            // Rest of the code for core courses remains the same
            if (remainingCoreGPA >= 2.00) {
                if (remainingCoreCourses.size() == 1) {
                    Course course = remainingCoreCourses.get(0);
                    String requiredGrade = getRequiredGrade(remainingCoreGPA, course);
                    message.append(String.format("The student needs >= %s in %s\n", requiredGrade, course.getCourseCode()));
                } else {
                    message.append(String.format("The student needs a GPA >= %.2f in:", remainingCoreGPA));
                    appendCourseList(remainingCoreCourses, message);
                }
            } else {
                message.append("The student must pass");
                appendCourseList(remainingCoreCourses, message);
            }
        } else {
            message.append("Core complete.\n");
        }

        if (!remainingElectiveCourses.isEmpty()) {
            message.append("\nTo maintain a 3.0 elective GPA:");
            double remainingElectiveGPA = getRemainingGPA(remainingElectiveCourses, 3.00);
            double requiredGPA = (3.0 * (electiveCourses.size() + remainingElectiveCourses.size()) - calculateElectiveGPA() * electiveCourses.size()) / remainingElectiveCourses.size();

            if (requiredGPA >= 2.00) {
                if (remainingElectiveCourses.size() == 1) {
                    Course course = remainingElectiveCourses.get(0);
                    String requiredGrade = getRequiredGrade(requiredGPA, course);
                    message.append(String.format("The student needs >= %s in %s\n", requiredGrade, course.getCourseCode()));
                } else {
                    message.append(String.format("The student needs a GPA >= %.2f in:", requiredGPA));
                    appendCourseList(remainingElectiveCourses, message);
                }
            } else {
                message.append("The student must pass");
                appendCourseList(remainingElectiveCourses, message);
            }
        }

        return message.toString();
    }

//    private void generateCourseMessage(List<Course> remainingCourses, double remainingGPA, StringBuilder message) {
//        if (remainingGPA >= 2.00) {
//            if (remainingCourses.size() == 1) {
//                Course course = remainingCourses.get(0);
//                String requiredGrade = getRequiredGrade(remainingGPA, course);
//                message.append(String.format("The student needs >= %s in %s\n", requiredGrade, course.getCourseCode()));
//            } else {
//                message.append(String.format("The student needs a GPA >= %.3f in:", remainingGPA));
//                appendCourseList(remainingCourses, message);
//            }
//        } else {
//            message.append("The student must pass");
//            appendCourseList(remainingCourses, message);
//        }
//    }


    private void appendCourseList(List<Course> courses, StringBuilder message) {
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            message.append(String.format(" %s%s", course.getCourseCode(), (i == courses.size() - 1) ? ".\n" : ","));
        }
    }

    private String getRequiredGrade(double remainingGPA, Course course) {
        double requiredGradeValue = remainingGPA * course.getCreditHours();
        return Course.getLetterGradeFromValue(requiredGradeValue);
    }


    private double getRemainingGPA(List<Course> remainingCourses, double targetGPA) {
        double totalCreditHours = 0;
        double totalEarnedCreditHours = 0;

        for (Course course : remainingCourses) {
            if (!course.getGrade().equalsIgnoreCase("N/A")) {
                totalEarnedCreditHours += course.getEarnedCreditHours();
            }
            totalCreditHours += course.getCreditHours();
        }

        double remainingGPA = (totalCreditHours * targetGPA - totalEarnedCreditHours) / totalCreditHours;
        return remainingGPA;
    }
    public void addCourse(Course course) {
        // Dont add course if its 5117
        if (!course.getCourseCode().equals("CSC5177")) {
            courses.add(course);
        }
    }
    public List<Course> getRemainingCoreCourses() {
        List<Course> remainingCoreCourses = new ArrayList<>();
        for (Course course : coreCourses) {
            if (course.getGrade().equalsIgnoreCase("N/A")) {
                remainingCoreCourses.add(course);
            }
        }
        return remainingCoreCourses;
    }

    public List<Course> getRemainingElectiveCourses() {
        List<Course> remainingElectiveCourses = new ArrayList<>();
        for (Course course : electiveCourses) {
            if (course.getGrade().equalsIgnoreCase("N/A")) {
                remainingElectiveCourses.add(course);
            }
        }
        return remainingElectiveCourses;
    }

    //    public void addPrereq(Course prereqs) {
//        // Dont add course if its 5117
//        if (!prereqs.getCourseCode().equals("CSC5177")) {
//            courses.add(prereqs);
//        }
//    }
    public void applyAdditionalRules() {
        removeCsIppAssignment();
        handleElectiveCourses();
    }
    public List<Course> getCourseList() {
        return courses;
    }

//    public List<Course> getPrerequisites(){
//        return prerequisites;
//    }



    //may be final if not, idk just know I changed it to final
    public void categorizeCoursesByTrack(String track, List<String> prerequisites) {
        List<String> coreCourses = new ArrayList<>();
        List<String> levelingCourses = new ArrayList<>();
        List<Course> specialElectiveCourses = new ArrayList<>();
        Course highestGradeSpecialElective = null;
        Set<String> uniqueLevelingCourses = new HashSet<>();

        switch (track) {
            case "Data Science":
                coreCourses = Arrays.asList("CS6313", "CS6350", "CS6363", "CS6375", "CS6360");
                levelingCourses = Arrays.asList("CS3341", "CS2340",  "CS5303","CS5333","CS5343","CS5348");
                break;
            // Add cases for other tracks and their respective core and elective courses
        }

        for (Course course : this.courses) {
            String fullCourseCode = course.getCourseCode();
            String courseCodeWithoutWhitespace = course.getCourseCode().replaceAll("\\s+", "");
            if (coreCourses.contains(courseCodeWithoutWhitespace)) {
                this.coreCourses.add(course);
            } else if (levelingCourses.contains(courseCodeWithoutWhitespace) || prerequisites.contains(courseCodeWithoutWhitespace)){
                if (course.getCourseCode().equals("CS5333") || course.getCourseCode().equals("CS5343") || course.getCourseCode().equals("CS5348")) {
                    specialElectiveCourses.add(course);
                } else if (uniqueLevelingCourses.add(fullCourseCode)) {
                    this.levelingCourses.add(course);
                }
            } else if (course.getCourseCode().startsWith("CS6") || course.getCourseCode().startsWith("CS7")) {
                this.electiveCourses.add(course);
            }
        }

        if (!specialElectiveCourses.isEmpty()) {
            highestGradeSpecialElective = specialElectiveCourses.get(0);
            for (Course course : specialElectiveCourses) {
                if (course.getGradeValue() > highestGradeSpecialElective.getGradeValue()) {
                    highestGradeSpecialElective = course;
                }
            }
            this.electiveCourses.add(highestGradeSpecialElective);
            this.levelingCourses.remove(highestGradeSpecialElective); // Remove the highest grade course from levelingCourses
        }
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

    public double calculateOverallGPA() {

        double totalPoints = 0;
        double totalEarnedCreditHours = 0;

        for (Course course : this.courses) {
            String courseCode = course.getCourseCode();
            if (course.getGrade() != null && !course.getGrade().equals("NA") && !course.getGrade().equals("P") && !course.getGrade().equals("F") && Integer.parseInt(courseCode.substring(2, 4)) >= 50) {
                totalPoints += course.getGradeValue() * course.getEarnedCreditHours();
                totalEarnedCreditHours += course.getEarnedCreditHours();
            }
        }

        return totalEarnedCreditHours > 0 ? totalPoints / totalEarnedCreditHours : 0;
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


