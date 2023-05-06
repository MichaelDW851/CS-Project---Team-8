import java.io.*;
import java.util.*;
public class Student implements Serializable {
    String name;
    String studentID;
    String major;
    String semesterAdmittedToProgram;

    private String track;
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
    void setFastTrack() {
        isFastTrack = true;
    }
    public void setThesisMasters() {
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
            int totalCourses = countCoursesWithValidGrades();
            int remainingCoursesCount = remainingCourses.size();
            double requiredRemainingGPA = (desiredOverallGPA * (totalCourses + remainingCoursesCount) - currentOverallGPA * totalCourses) / remainingCoursesCount;

            if (requiredRemainingGPA >= 2.00) {
                if (remainingCoursesCount == 1) {
                    Course course = remainingCourses.get(0);
                    String requiredGrade = getRequiredGrade(requiredRemainingGPA, course);
                    message.append(String.format("To maintain a 3.0 overall GPA, The student needs >= %s in %s\n", requiredGrade, course.getCourseCode()));
                } else {
                    message.append(String.format("To maintain a 3.0 overall GPA, The student needs a GPA >= %.3f in:\n", requiredRemainingGPA));
                    appendCourseList(remainingCourses, message);
                }
            } else {
                message.append("The student must pass");
                appendCourseList(remainingCourses, message);
            }
        } else {
            message.append(String.format("All courses complete. Current overall GPA: %.3f\n", calculateOverallGPA()));
        }

        return message.toString();
    }


    public int countCoursesWithValidGrades() {
        int count = 0;
        List<Course> allCourses = new ArrayList<>();
        allCourses.addAll(coreCourses);
        allCourses.addAll(electiveCourses);

        for (Course course : allCourses) {
            String grade = course.getGrade();
            if (!grade.equals("N/A") && !grade.equals("W") && !grade.equals("P") && !grade.equals("F")) {
                count++;
            }
        }
        return count;
    }


    public double getCurrentCoreCreditHours() {
        double creditHours = 0;
        for (Course course : coreCourses) {
            String grade = course.getGrade();
            if (!grade.equals("N/A") && !grade.equals("W") && !grade.equals("P") && !grade.equals("F")) {
                creditHours += course.getCreditHours();
            }
        }
        return creditHours;
    }

    public double getCurrentCoreTotalGradePoints() {
        double totalGradePoints = 0;
        for (Course course : coreCourses) {
            String grade = course.getGrade();
            if (!grade.equals("N/A") && !grade.equals("W") && !grade.equals("P") && !grade.equals("F")) {
                totalGradePoints += course.getGradeValue() * course.getCreditHours();
            }
        }
        return totalGradePoints;
    }

    public double getCurrentElectiveCreditHours() {
        double creditHours = 0;
        for (Course course : electiveCourses) {
            String grade = course.getGrade();
            if (!grade.equals("N/A") && !grade.equals("W") && !grade.equals("P") && !grade.equals("F")) {
                creditHours += course.getCreditHours();
            }
        }
        return creditHours;
    }

    public double getCurrentElectiveTotalGradePoints() {
        double totalGradePoints = 0;
        for (Course course : electiveCourses) {
            String grade = course.getGrade();
            if (!grade.equals("N/A") && !grade.equals("W") && !grade.equals("P") && !grade.equals("F")) {
                totalGradePoints += course.getGradeValue() * course.getCreditHours();
            }
        }
        return totalGradePoints;
    }

    public double getRemainingCoreCreditHours(List<Course> remainingCoreCourses) {
        double creditHours = 0;
        for (Course course : remainingCoreCourses) {
            creditHours += course.getCreditHours();
        }
        return creditHours;
    }

    public double getRemainingElectiveCreditHours(List<Course> remainingElectiveCourses) {
        double creditHours = 0;
        for (Course course : remainingElectiveCourses) {
            creditHours += course.getCreditHours();
        }
        return creditHours;
    }
    public String getRemainingCoursesMessage() {
        List<Course> remainingCoreCourses = getRemainingCoreCourses();
        List<Course> remainingElectiveCourses = getRemainingElectiveCourses();

        double currentCoreCreditHours = getCurrentCoreCreditHours();
        double currentCoreTotalGradePoints = getCurrentCoreTotalGradePoints();
        double remainingCoreCreditHours = getRemainingCoreCreditHours(remainingCoreCourses);

        double currentElectiveCreditHours = getCurrentElectiveCreditHours();
        double currentElectiveTotalGradePoints = getCurrentElectiveTotalGradePoints();
        double remainingElectiveCreditHours = getRemainingElectiveCreditHours(remainingElectiveCourses);

        StringBuilder message = new StringBuilder();

        if (!remainingCoreCourses.isEmpty()) {
            double remainingCoreGPA = getRemainingGPA(remainingCoreCourses, 3.19);
            if (remainingCoreGPA >= 2.00) {
                double neededGPA = (3.19 * (currentCoreCreditHours + remainingCoreCreditHours) - currentCoreTotalGradePoints) / remainingCoreCreditHours;
                if (remainingCoreCourses.size() == 1) {
                    Course course = remainingCoreCourses.get(0);
                    String requiredGrade = getRequiredGrade(remainingCoreGPA, course);
                    message.append(String.format("To maintain a 3.19 core GPA,The student needs >= %s in %s\n", requiredGrade, course.getCourseCode()));
                } else {
                    message.append(String.format("To maintain a 3.0 core GPA,The student needs a GPA >= %.3f in\n", neededGPA));
                    appendCourseList(remainingCoreCourses, message);
                }
            } else {
                message.append("The student must pass");
                appendCourseList(remainingCoreCourses, message);
            }
        } else {
            message.append("Core complete.");
        }

        if (!remainingElectiveCourses.isEmpty()) {
            double remainingElectiveGPA = getRemainingGPA(remainingElectiveCourses, 3.00);
            if (remainingElectiveGPA >= 2.00) {
                double neededGPA = (3.00 * (currentElectiveCreditHours + remainingElectiveCreditHours) - currentElectiveTotalGradePoints) / remainingElectiveCreditHours;
                if (remainingElectiveCourses.size() == 1) {
                    Course course = remainingElectiveCourses.get(0);
                    String requiredGrade = getRequiredGrade(remainingElectiveGPA, course);
                    message.append(String.format("To maintain a 3.0 elective GPA, the student needs >= %s in %s\n", requiredGrade, course.getCourseCode()));
                } else {
                    message.append(String.format("To maintain a 3.0 elective GPA, the student needs a GPA >= %.3f in:", neededGPA));
                    appendCourseList(remainingElectiveCourses, message);
                }
            } else {
                message.append("The student must pass");
                appendCourseList(remainingElectiveCourses, message);
            }
        } else {
            message.append("Electives complete.\n");
        }

        return message.toString();
    }


    private void appendCourseList(List<Course> courses, StringBuilder message) {
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            // Remove the colon from this line:
            message.append(String.format(" %s%s", course.getCourseCode(), (i == courses.size() - 1) ? "." : ","));
        }
        // Add a newline character after the course list
        message.append("\n");
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
    //    public void addCourse(Course course) {
//        // Dont add course if its 5117
//        if (!course.getCourseCode().equals("CSC5177")) {
//            courses.add(course);
//        }
//    }
    public void addCourse(Course course) {
        String courseCode = course.getCourseCode();

        // Ignore the course if it is CSC5177
        if (courseCode.equals("CSC5177")) {
            return;
        }

        // Find if the course already exists in the list
        boolean courseFound = false;
        for (int i = 0; i < courses.size(); i++) {
            Course existingCourse = courses.get(i);
            if (existingCourse.getCourseCode().equals(courseCode)) {
                // If the new course has a higher grade, replace the existing course
                if (course.getGradeValue() > existingCourse.getGradeValue()) {
                    courses.set(i, course);
                }
                courseFound = true;
                break;
            }
        }

        // If the course is not in the list, add it
        if (!courseFound) {
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
        this.track = track;

        switch (track) {
            case "Data Science":
                coreCourses = Arrays.asList("CS6313", "CS6350", "CS6363", "CS6375", "CS6360");
                levelingCourses = Arrays.asList("CS3341", "CS2340",  "CS5303","CS5333","CS5343","CS5348");
                break;
            // Add cases for other tracks and their respective core and elective courses
            case "Cyber Security":
                coreCourses = Arrays.asList("CS6324", "CS6363", "CS6378", "CS6332", "CS6348", "CS6349", "CS6377");
                levelingCourses = Arrays.asList( "CS2340",  "CS5303","CS5333","CS5343","CS5348", "CS5390");
                break;
            case "Interactive Computing":
                coreCourses = Arrays.asList("CS6326", "CS6363", "CS6323", "CS6328", "CS6331", "CS6334", "CS6366");
                levelingCourses = Arrays.asList( "CS2340",  "CS5303","CS5333","CS5343","CS5348", "CS5390");
                break;
            case "Networks and Telecommunications":
                coreCourses = Arrays.asList("CS6320", "CS6363", "CS6364", "CS6375", "CS6360", "CS6378");
                levelingCourses = Arrays.asList( "CS2340",  "CS5303","CS5333","CS5343","CS5348", "CS5390", "CS3341");
                break;
            case "Systems":
                coreCourses = Arrays.asList("CS6304", "CS6363", "CS6396", "CS6349", "CS6376", "CS6378", "CS6380", "CS6397");
                levelingCourses = Arrays.asList( "CS2340",  "CS5303","CS5333","CS5343","CS5348");
                break;
            case "Traditional Computer Science":
                coreCourses = Arrays.asList("CS6390", "CS6363", "CS6353", "CS6371", "CS6360", "CS6378");
                levelingCourses = Arrays.asList( "CS2340",  "CS5303","CS5333","CS5343","CS5348", "CS5349", "CS5390");
                break;
            case "Intelligent Systems":
                coreCourses = Arrays.asList("CS6320", "CS6363", "CS6364", "CS6375", "CS6360", "CS6378");
                levelingCourses = Arrays.asList( "CS2340",  "CS5303","CS5333","CS5343","CS5348");
                break;
            case " ":
                coreCourses = Arrays.asList("SE6329", "SE6361","CS6361", "SE6362", "SE6367", "SE6387", "SE6378");
                levelingCourses = Arrays.asList( "CS2340",  "CS5303","CS5333","CS5343","CS5348");
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
