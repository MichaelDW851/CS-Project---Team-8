import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.TreeMap;

public class PdfReader {
    public static void main(String[] args) throws IOException {
        // Load the PDF document
        PDDocument document = PDDocument.load(new File("C:/Users/adria/Downloads/Krab, Krusty-SSR_TSRPT-1.pdf"));

        // Create a PDFTextStripper object to extract text from the PDF document
        PDFTextStripper pdfStripper = new PDFTextStripper();

        // Extract text from the PDF document
        String text = pdfStripper.getText(document);

        String name = null;
        String id = null;
        String major = null;
        String record = null;

        // Extract the name
        Pattern namePattern = Pattern.compile("Name:(.*)");
        Matcher nameMatcher = namePattern.matcher(text);
        if (nameMatcher.find()) {
            name = nameMatcher.group(1).trim();
            System.out.println("Name: " + name);
        } else {
            System.out.println("Name not found");
        }

        // Extract the student ID
        Pattern idPattern = Pattern.compile("Student ID:(.*)");
        Matcher idMatcher = idPattern.matcher(text);
        if (idMatcher.find()) {
            id = idMatcher.group(1).trim();
            System.out.println("Student ID: " + id);
        } else {
            System.out.println("Student ID not found");
        }

        // Extract the major
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
        Pattern recordPattern = Pattern.compile("Beginning of Graduate Record\\s+(\\d{4}\\s+\\w+)");
        Matcher recordMatcher = recordPattern.matcher(text);
        if (recordMatcher.find()) {
            record = recordMatcher.group(1).trim();
            System.out.println("Semester Admitted to Program: " + record);
        } else {
            System.out.println("Semester Admitted to Program for degree plan not found");
        }

        Student student = new Student(name, id, major, record);

        Pattern yearPattern = Pattern.compile("(\\d{4})\\s+(Fall|Summer|Spring)");
        Pattern coursePattern = Pattern.compile("([5-9]\\d{3})\\s+(.*?)\\s+(\\d+\\.\\d+)\\s+(\\d+\\.\\d+)\\s*([A-Z][+-]?\\s*)?");

        // Initialize a TreeMap to store the courses grouped by year and semester
        TreeMap<String, StringBuilder> coursesBySemester = new TreeMap<>();

        // Loop through the lines of text and extract course information
        String[] lines = text.split("\\r?\\n");
        String currentSemester = null;
        for (String line : lines) {
            Matcher yearMatcher = yearPattern.matcher(line);
            if (yearMatcher.find()) {
                // Found a new semester
                String year = yearMatcher.group(1);
                String season = yearMatcher.group(2);
                String semesterKey = year + "-" + (season.equals("Spring") ? "01" : season.equals("Summer") ? "02" : "03");
                currentSemester = semesterKey;
                coursesBySemester.put(currentSemester, new StringBuilder());
            } else {
                Matcher courseMatcher = coursePattern.matcher(line);
                if (courseMatcher.find() && currentSemester != null) {
                    // Found a course for the current semester
                    StringBuilder courses = coursesBySemester.get(currentSemester);
                    courses.append(line).append("\n");
                }
            }
        }

        // Print the courses grouped by year and semester
        for (String semester : coursesBySemester.keySet()) {
            String year = semester.substring(0, 4);
            String season = semester.substring(5).equals("01") ? "Spring" : semester.substring(5).equals("02") ? "Summer" : "Fall";
            String[] courseLines = coursesBySemester.get(semester).toString().split("\\n");
            for (String courseLine : courseLines) {
                Matcher courseMatcher = coursePattern.matcher(courseLine);
                if (courseMatcher.find()) {
                    String courseCode = courseMatcher.group(1);
                    String courseName = courseMatcher.group(2);
                    double creditHours = Double.parseDouble(courseMatcher.group(3));
                    double earnedCreditHours = Double.parseDouble(courseMatcher.group(4));
                    String grade = courseMatcher.group(5);
                    if (grade != null) {
                        grade = grade.trim();
                    } else {
                        grade = "N/A";
                    }
                    String displaySeason = season.equals("01") ? "Spring" : season.equals("02") ? "Summer" : "Fall";
                    Course course = new Course(year, semester, courseCode, courseName, creditHours, earnedCreditHours, grade);
                    System.out.printf("Course: %s - %s, Year: %s, Season: %s, Credit Hours: %.1f, Earned Credit Hours: %.1f, Grade: %s%n", courseCode, courseName, year, displaySeason, creditHours, earnedCreditHours, grade);
                    student.addCourse(course);
                }
            }
        }

        // Close the PDF document
        document.close();
    }
}
