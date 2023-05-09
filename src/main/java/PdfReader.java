import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PdfReader {
    public static Student processTranscript(String pdfFilePath, String track, List<String> prerequisites, boolean isFastTrack, boolean isThesisMasters) throws IOException {
        // Load the PDF document
        PDDocument document = PDDocument.load(new File(pdfFilePath));

        // Create a PDFTextStripper object to extract text from the PDF document
        PDFTextStripper pdfStripper = new PDFTextStripper();

        // Extract text from the PDF document
        String text = pdfStripper.getText(document);

        String name = null;
        String id = null;
        String major = null;
        String record = null;

        // Extract the name using regex - .*, matches any character zero or more times, () to express that it should be put into a group
        Pattern namePattern = Pattern.compile("Name:(.*)");
        Matcher nameMatcher = namePattern.matcher(text);
        if (nameMatcher.find()) {
            //if the pattern is found , trim the whitespace from matched group and assign it the 'name' variable
            name = nameMatcher.group(1).trim();
            System.out.println("Name: " + name);
        } else {
            System.out.println("Name not found");
        }

        // Extract the student ID using regex, same idea as name
        Pattern idPattern = Pattern.compile("Student ID:(.*)");
        Matcher idMatcher = idPattern.matcher(text);
        if (idMatcher.find()) {
            id = idMatcher.group(1).trim();
            System.out.println("Student ID: " + id);
        } else {
            System.out.println("Student ID not found");
        }

        // Extract the major, Honestly I just assumed Master Program every time
        Pattern programPattern = Pattern.compile("Program: Master");
        Matcher programMatcher = programPattern.matcher(text);
        if (programMatcher.find()) {
            // If "Program: Master" is found, search for the major
            Pattern majorPattern = Pattern.compile("(Computer Science|Software Engineering) Major");
            Matcher majorMatcher = majorPattern.matcher(text);
            if (majorMatcher.find()) {
                major = majorMatcher.group(1).trim();
                System.out.println("Major: " + major);
            } else {
                System.out.println("Major not found");
            }
        } else {
            System.out.println("Program not found");
        }

        // Extract the semester admitted to program for degree plan
        // \\s+  matches one or more whitespace characters, \\d{4} matches exaclty four digits,
        // \\s+ matches one or more whitespace characters, \\w mathces one or more letters,digits,and underscores
        // string with four digit number, followed by one or more whitespace, and ends with one or more word characters
        Pattern recordPattern = Pattern.compile("Beginning of Graduate Record\\s+(\\d{4}\\s+\\w+)");
        Matcher recordMatcher = recordPattern.matcher(text);
        if (recordMatcher.find()) {
            record = recordMatcher.group(1).trim();
            System.out.println("Semester Admitted to Program: " + record);
        } else {
            System.out.println("Semester Admitted to Program for degree plan not found");
        }

        //Constructs a student object that gets information read from the PDf
        Student student = new Student(name, id, major, record, isFastTrack, isThesisMasters);

        //(\\d{4}) matches four digits in a row, \\s+ matches one or more whitespaces, followed by Fall, Semester, or Spring
        Pattern yearPattern = Pattern.compile("(\\d{4})\\s+(Fall|Summer|Spring)");

        //((CS|SE|CSC|SEC) )? matches a prefix followed by a space
        //([5-9]\d{3})- This group matches a four digit course code,starting with a 5-9
        // \\s+ matches one or more whitespace characters
        //(.*?) matches any combo of characters using ? to indicate to keep it short as possible
        // (\d+.\d+) matches the credit hours for the course, (a decimal number) and we do the same for earned credit hours
        // \s* zero or more whitespace
        // ([A-Z][+-]?\s*) matches an option letter grade for a course, followed by a plus or minus, and optional white space
        Pattern coursePattern = Pattern.compile("((CS|SE|CSC|SEC) )?([5-9]\\d{3})\\s+(.*?)\\s+(\\d+\\.\\d+)\\s+(\\d+\\.\\d+)\\s*([A-Z][+-]?\\s*)?");

        // Initialize a TreeMap to store the courses grouped by year and semester
        TreeMap<String, StringBuilder> coursesBySemester = new TreeMap<>();

        // Loop through the lines of text and extract course information
        //Split the text by new line characters
        String[] lines = text.split("\\r?\\n");
        String currentSemester = null;
        //Loop through each line in text
        for (String line : lines) {
            //Check if line matches year pattern
            Matcher yearMatcher = yearPattern.matcher(line);
            if (yearMatcher.find()) {
                // Extract year and season from matched text
                String year = yearMatcher.group(1);
                String season = yearMatcher.group(2);

             //Combine year and season to create semesterKey
                String semesterKey = year + "-" + (season.equals("Spring") ? "01" : season.equals("Summer") ? "02" : "03");
                currentSemester = semesterKey;
                //Add a new stringBuilder to the map for the current semester
                coursesBySemester.put(currentSemester, new StringBuilder());
                //Check if the line matches the course pattern
            } else {
                Matcher courseMatcher = coursePattern.matcher(line);
                if (courseMatcher.find() && currentSemester != null) {
                    // Get stringbuilder for current semester
                    StringBuilder courses = coursesBySemester.get(currentSemester);
                    courses.append(line).append("\n");
                }
            }
        }

        //Loop through all semesters found in the document
        for (String semester : coursesBySemester.keySet()) {
            //EXtract the year and season from the semester key
            String year = semester.substring(0, 4);
            String season = semester.substring(5).equals("01") ? "Spring" : semester.substring(5).equals("02") ? "Summer" : "Fall";
            //Split the course lines for the current semester into an array
            String[] courseLines = coursesBySemester.get(semester).toString().split("\\n");
            //Loop through each course in current semester
            for (String courseLine : courseLines) {
                Matcher courseMatcher = coursePattern.matcher(courseLine);

                if (courseMatcher.find()) {
                    //Extract the course prefix and code,name,credit hours, and  grade from the matched line
                    String coursePrefix = courseMatcher.group(2) != null ? courseMatcher.group(2) : ""; // Extract course prefix
                    String courseCode = coursePrefix + courseMatcher.group(3); // Prepend course prefix to the course code
                    String courseName = courseMatcher.group(4);
                    double creditHours = Double.parseDouble(courseMatcher.group(5));
                    double earnedCreditHours = Double.parseDouble(courseMatcher.group(6));
                    String grade = courseMatcher.group(7);

                    //If grade is empty or null set it to N/A
                    if (grade != null && !grade.trim().isEmpty()) {
                        grade = grade.trim();
                    } else {
                        grade = "N/A";
                    }
                    //Dispay the extracted course information in the console
                    String displaySeason = season.equals("01") ? "Spring" : season.equals("02") ? "Summer" : "Fall";
                    //Create a course object with the extracted course information
                    Course course = new Course(year, semester, courseCode, courseName, creditHours, earnedCreditHours, grade);
                    System.out.printf("Course: %s - %s, Year: %s, Season: %s, Credit Hours: %.1f, Earned Credit Hours: %.1f, Grade: %s%n", courseCode, courseName, year, displaySeason, creditHours, earnedCreditHours, grade);
                    //add the course to the students course list.
                    student.addCourse(course);


                }
            }
        }


        // Close the PDF document
        document.close();
        student.applyAdditionalRules();
        return student;
    }
}