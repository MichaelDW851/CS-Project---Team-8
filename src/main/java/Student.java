import java.io.*;
import java.util.*;

public class Student implements Serializable {
//Instance Variables
    String name;
    String studentID;
    String major;
    String semesterAdmittedToProgram;
    private String track;
    boolean isFastTrack;
    boolean isThesisMasters;

// Lists that will be used for printing and calculating GPA
    List<Course> courses;
    private List<Course> coreCourses = new ArrayList<>();
    private List<Course> electiveCourses = new ArrayList<>();
    private List<Course> levelingCourses = new ArrayList<>();

//Constructor for Student
    public Student(String name, String studentID, String major, String semesterAdmittedToProgram, boolean isFastTrack, boolean isThesisMasters) {
        this.name = name;
        this.studentID = studentID;
        this.major = major;
        this.semesterAdmittedToProgram = semesterAdmittedToProgram;
        this.courses = new ArrayList<>();
        this.isFastTrack = isFastTrack;
        this.isThesisMasters = isThesisMasters;


    }
//Set and getter methods will be used for Audit report (GPA calculations and printing classes)
    void setFastTrack() {
        isFastTrack = true;
    }

    public void setThesisMasters() {
        isThesisMasters = true;
    }

    public boolean getFastTrackCheck() {
        if (isFastTrack) return true;
        return false;
    }

    public boolean getThesisMastersCheck() {
        if (isThesisMasters) return true;
        return false;
    }

    public String getName() {
        return name;
    }

    public String getStudentID() {
        return studentID;
    }

    public String getSemesterAdmittedToProgram() {
        return semesterAdmittedToProgram;
    }
    public String getMajor() {
        return major;
    }

    public List<Course> getCourses() {
        return courses;
    }



//Method to print Outstanding requirements in the Audit report
    public String getRemainingOverallGPAMessage(double desiredOverallGPA) {
        List<Course> remainingCourses = new ArrayList<>();
        remainingCourses.addAll(getRemainingCoreCourses());
        remainingCourses.addAll(getRemainingElectiveCourses());

        StringBuilder message = new StringBuilder();
        //
        if (!remainingCourses.isEmpty()) {
            double currentOverallGPA = calculateOverallGPA();
            int totalCourses = countCoursesWithValidGrades();
            int remainingCoursesCount = remainingCourses.size();
            //totalCoures are GRADED courses, remainingCoures count is NON-graded coures, desiredoverall GPA is either 3.0 or 3.19
            // Subtract grade points earned so far then divide by remaining courses.
            double requiredRemainingGPA = (desiredOverallGPA * (totalCourses + remainingCoursesCount) - currentOverallGPA * totalCourses) / remainingCoursesCount;

            //If we need a GPA greater than 2.00 we must print either grade or GPA, depends on if its one class or two
            // Getting errors...ask group for help, recheck logic?
            if (requiredRemainingGPA >= 2.00) {
                if (remainingCoursesCount == 1) {
                    //gets course with N/A grade
                    Course course = remainingCourses.get(0);
                    String requiredGrade = getRequiredGrade(requiredRemainingGPA, course,currentOverallGPA,totalCourses);
                    message.append(String.format("To maintain a 3.0 overall GPA, The student needs >= %s in %s\n", requiredGrade, course.getCourseCode()));
                } else {
                    message.append(String.format("To maintain a 3.0 overall GPA, The student needs a GPA >= %.3f in:\n", requiredRemainingGPA));
                    appendCourseList(remainingCourses, message);
                }
                //if GPA needed is less than 2.00 just say pass.
            } else {
                message.append("The student must pass");
                appendCourseList(remainingCourses, message);
            }
            //else, you have completed all your classes in your transcript so far.
        } else {
            message.append(String.format("All courses complete. Current overall GPA: %.3f\n", calculateOverallGPA()));
        }
        //as method runs, it adds to message and prints it out when invoked in createOutputDOCX method
        return message.toString();
    }

    //Method to count number of courses with VALID grades
    public int countCoursesWithValidGrades() {
        int count = 0;
        List<Course> allCourses = new ArrayList<>();
        allCourses.addAll(coreCourses); // Add all coures to list
        allCourses.addAll(electiveCourses);//Add all elective courses to the list

        //Iterate through courses
        for (Course course : allCourses) {
            String grade = course.getGrade();
            //Check if grade is not NA, W, P, or F
            if (!grade.equals("N/A") && !grade.equals("W") && !grade.equals("P") && !grade.equals("F")) {
                count++; //If valid INCREMENT!
            }
        }
        return count; //returns number of courses with valid grades
    }

//Method to generate the message containing remaining courses requirements and required GPA for a student.
    public String getRemainingCoursesMessage() {
        List<Course> remainingCoreCourses = getRemainingCoreCourses(); //Gets remaining core courses and stores it
        List<Course> remainingElectiveCourses = getRemainingElectiveCourses(); // Gets remaining elective courses and stores it

        double currentCoreCreditHours = getCurrentCoreCreditHours(); // Get the current core credit hours -> will be used for calculations same with below
        double currentCoreTotalGradePoints = getCurrentCoreTotalGradePoints(); //Get the current core total grade points and stores it
        double remainingCoreCreditHours = getRemainingCoreCreditHours(remainingCoreCourses); //Get the remaining core credit hours and stores it

        double currentElectiveCreditHours = getCurrentElectiveCreditHours(); //Gets current elective credit hours
        double currentElectiveTotalGradePoints = getCurrentElectiveTotalGradePoints(); // Gets the current elective total grade points
        double remainingElectiveCreditHours = getRemainingElectiveCreditHours(remainingElectiveCourses); // Get the remaining elective credit hours

        StringBuilder message = new StringBuilder(); // Initialize String Buidlder to build the message

        //Checks for remaining core courses (classes with N/A)
        if (!remainingCoreCourses.isEmpty()) {
            double remainingCoreGPA = getRemainingGPA(remainingCoreCourses, 3.19); //Calculate the remaning core GPA
            if (remainingCoreGPA > 0.00 && remainingCoreGPA >= 2.00) {
                double neededGPA = (3.19 * (currentCoreCreditHours + remainingCoreCreditHours) - currentCoreTotalGradePoints) / remainingCoreCreditHours;
               //Different messages if one class or two classes, One class -> Letter, Two Classes -> GPA to three decimal places
                if (remainingCoreCourses.size() == 1) {
                    Course course = remainingCoreCourses.get(0);
                    String requiredGrade = getRequiredGrade(remainingCoreGPA, course, calculateOverallGPA(), countCoursesWithValidGrades());

                    message.append(String.format("To maintain a 3.19 core GPA,The student needs >= %s in %s\n", requiredGrade, course.getCourseCode()));
                } else {
                    message.append(String.format("To maintain a 3.19 core GPA,The student needs a GPA >= %.3f in\n", neededGPA));
                    appendCourseList(remainingCoreCourses, message); //append N/A cores to message
                }
            } else {
                message.append("The student must pass");
                appendCourseList(remainingCoreCourses, message); //append NA cors to message
            }
        } else {
            message.append("Core complete.");
        }

        //Electives, everything is the same but GPA min is 3.00 instead of 3.19 like cores, message change too
        if (!remainingElectiveCourses.isEmpty()) {
            double remainingElectiveGPA = getRemainingGPA(remainingElectiveCourses, 3.00);
            if (remainingElectiveGPA > 0.00 && remainingElectiveGPA >= 2.00) {
                double neededGPA = (3.00 * (currentElectiveCreditHours + remainingElectiveCreditHours) - currentElectiveTotalGradePoints) / remainingElectiveCreditHours;
                if (remainingElectiveCourses.size() == 1) {
                    Course course = remainingElectiveCourses.get(0);
                    String requiredGrade = getRequiredGrade(remainingElectiveGPA, course, calculateOverallGPA(), countCoursesWithValidGrades());
                   // String requiredGrade = getRequiredGrade(remainingElectiveGPA, course);
                    message.append(String.format("\nTo maintain a 3.0 elective GPA, the student needs >= %s in %s\n", requiredGrade, course.getCourseCode()));
                } else {
                    message.append(String.format("\nTo maintain a 3.0 elective GPA, the student needs a GPA >= %.3f in:", neededGPA));
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



//Remeber we are getting these to divide total grade points by total credit hours
    public double getCurrentCoreCreditHours() {
        //Initialize to zero as to not mess up future calculations
        double creditHours = 0;
        //Loop through all core courses
        for (Course course : coreCourses) {
            //Grade has to be a valid grade!...Kept getting wrong GPA,checked Transcript and forgot there were W's :/.
            String grade = course.getGrade();
            if (!grade.equals("N/A") && !grade.equals("W") && !grade.equals("P") && !grade.equals("F")) {
                //Keep adding credit hours
                creditHours += course.getCreditHours();
            }
        }
        return creditHours; //total core credit hours
    }
//Same as above but to find current core Grade.
    public double getCurrentCoreTotalGradePoints() {
        //Initialize grade points to 0
        double totalGradePoints = 0;
        for (Course course : coreCourses) {
            String grade = course.getGrade();
            if (!grade.equals("N/A") && !grade.equals("W") && !grade.equals("P") && !grade.equals("F")) {
                totalGradePoints += course.getGradeValue() * course.getCreditHours(); //changes if a class is worth 3 or 4 credit hours
            }
        }
        return totalGradePoints;
    }
//Next two methods are the same as above, but for electives instead
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


    //Do the same but with credit hours for core and electives, we cannot find totalGradePoints because we havent even got a grade yet
    //That was confusing me for so long
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



    private void appendCourseList(List<Course> courses, StringBuilder message) {
        //iterate over courses in the list
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            // Remove the colon from this line:
            //add the course to the message, comma if its not last course put a comma after,  i ==courses.size-1 -> checks to see if last course.
            message.append(String.format(" %s%s", course.getCourseCode(), (i == courses.size() - 1) ? "." : ","));
        }
        // Add a newline character after the course list
        message.append("\n");
    }

    private String getRequiredGrade(double remainingGPA, Course course, double currentOverallGPA, int totalCourses) {
        //calculate the required to achieve the remaining GPA for the give course
        double requiredGradePoints = (remainingGPA * (totalCourses + 1) - currentOverallGPA * totalCourses) * course.getCreditHours();
        //Convert to letter grade, so that it prints a letter grade when there is only one Course
        return Course.getLetterGradeFromValue(requiredGradePoints);
    }

    //calculate remaining GPA need to achieve the target GPA for the remaining courses (core,elective, and all)
//calculations used to get the Remaining GPA for cores,electives, and all
    private double getRemainingGPA(List<Course> remainingCourses, double targetGPA) {
        double totalCreditHours = 0;
        double totalEarnedGradePoints = getCurrentCoreTotalGradePoints() + getCurrentElectiveTotalGradePoints();
//iterate over the remaining coures
        for (Course course : remainingCourses) {
            //add the credit hours to the course to the total credit hours
            totalCreditHours += course.getCreditHours();
        }
    double currentCreditHours = getCurrentCoreCreditHours() + getCurrentElectiveCreditHours();
    double requiredTotalGradePoints = (currentCreditHours + totalCreditHours) * targetGPA;
    double remainingGradePoints = requiredTotalGradePoints - totalEarnedGradePoints;
    double remainingGPA = remainingGradePoints / totalCreditHours;

    //This determines what message output we get,
    return remainingGPA;
}


//add a course to the list of courses, or replace if a higher grade is earned
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

        // If the course is not in the list, we add it
        if (!courseFound) {
            courses.add(course);
        }
    }


//Adds cores with no grade to a list
    public List<Course> getRemainingCoreCourses() {
        List<Course> remainingCoreCourses = new ArrayList<>(); // create a list to hold remainingCoreCourses
        for (Course course : coreCourses) { //loop through all core courses
            if (course.getGrade().equalsIgnoreCase("N/A")) {
                remainingCoreCourses.add(course);//if the course has not been graded yet (NA) add to remainingCourses.
            }
        }
        return remainingCoreCourses;
    }

    //Adds electives with no grade to a list, exactly like above
    public List<Course> getRemainingElectiveCourses() {
        List<Course> remainingElectiveCourses = new ArrayList<>();
        for (Course course : electiveCourses) {
            if (course.getGrade().equalsIgnoreCase("N/A")) {
                remainingElectiveCourses.add(course);
            }
        }
        return remainingElectiveCourses;
    }

    //this
    public void applyAdditionalRules() {
        removeCsIppAssignment();
        handleElectiveCourses();
    }

    public List<Course> getCourseList() {
        return courses;
    }


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
                levelingCourses = Arrays.asList("CS3341", "CS2340", "CS5303", "CS5333", "CS5343", "CS5348");
                break;
            // Add cases for other tracks and their respective core and elective courses
            case "Cyber Security":
                coreCourses = Arrays.asList("CS6324", "CS6363", "CS6378", "CS6332", "CS6348", "CS6349", "CS6377");
                levelingCourses = Arrays.asList("CS2340", "CS5303", "CS5333", "CS5343", "CS5348", "CS5390");
                break;
            case "Interactive Computing":
                coreCourses = Arrays.asList("CS6326", "CS6363", "CS6323", "CS6328", "CS6331", "CS6334", "CS6366");
                levelingCourses = Arrays.asList("CS2340", "CS5303", "CS5333", "CS5343", "CS5348", "CS5390");
                break;
            case "Networks and Telecommunications":
                coreCourses = Arrays.asList("CS6320", "CS6363", "CS6364", "CS6375", "CS6360", "CS6378");
                levelingCourses = Arrays.asList("CS2340", "CS5303", "CS5333", "CS5343", "CS5348", "CS5390", "CS3341");
                break;
            case "Systems":
                coreCourses = Arrays.asList("CS6304", "CS6363", "CS6396", "CS6349", "CS6376", "CS6378", "CS6380", "CS6397");
                levelingCourses = Arrays.asList("CS2340", "CS5303", "CS5333", "CS5343", "CS5348");
                break;
            case "Traditional Computer Science":
                coreCourses = Arrays.asList("CS6390", "CS6363", "CS6353", "CS6371", "CS6360", "CS6378");
                levelingCourses = Arrays.asList("CS2340", "CS5303", "CS5333", "CS5343", "CS5348", "CS5349", "CS5390");
                break;
            case "Intelligent Systems":
                coreCourses = Arrays.asList("CS6320", "CS6363", "CS6364", "CS6375", "CS6360", "CS6378");
                levelingCourses = Arrays.asList("CS2340", "CS5303", "CS5333", "CS5343", "CS5348");
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
            } else if (levelingCourses.contains(courseCodeWithoutWhitespace) || prerequisites.contains(courseCodeWithoutWhitespace)) {
                if (course.getCourseCode().equals("CS5333") || course.getCourseCode().equals("CS5343") || course.getCourseCode().equals("CS5348")) {
                    specialElectiveCourses.add(course);
                } else if (uniqueLevelingCourses.add(fullCourseCode)) {
                    this.levelingCourses.add(course);
                }
            } else if (course.getCourseCode().startsWith("CS6") || course.getCourseCode().startsWith("CS7")||course.getCourseCode().startsWith("SE6") || course.getCourseCode().startsWith("SE7"))  {
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

    //This handles these notes in specs
//    If the student earned the same score in 2 or more of CS 5333, CS 5343, and CS 5348, use one with those with the
//    higher score by choosing CS 5343 over CS 5333 and CS 5348 as the elective, choosing CS 5348 over CS 5333
//    as the elective. And only one can be used as elective.
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


