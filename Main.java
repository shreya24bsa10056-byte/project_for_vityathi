package edu.ccrm.cli;

import edu.ccrm.config.AppConfig;
import edu.ccrm.service.CourseService;
import edu.ccrm.service.StudentService;
import edu.ccrm.service.EnrollmentService;
import edu.ccrm.io.ImportExportService;
import edu.ccrm.io.BackupService;
import edu.ccrm.util.ReportGenerator;
import edu.ccrm.domain.Student;
import edu.ccrm.domain.Course;
import edu.ccrm.domain.Semester;
import edu.ccrm.domain.Grade;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final StudentService studentService = new StudentService();
    private static final CourseService courseService = new CourseService();
    private static final EnrollmentService enrollmentService = new EnrollmentService();
    private static final ImportExportService importExportService = new ImportExportService();
    private static final BackupService backupService = new BackupService();

    public static void main(String[] args) {
        AppConfig config = AppConfig.getInstance();
        System.out.println("Welcome to Campus Course & Records Manager");
        System.out.println("Data directory: " + config.getDataDirectory());

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> manageStudents();
                case 2 -> manageCourses();
                case 3 -> manageEnrollments();
                case 4 -> manageGrades();
                case 5 -> importExportData();
                case 6 -> backupOperations();
                case 7 -> generateReports();
                case 8 -> {
                    running = false;
                    System.out.println("Goodbye!");
                }
                case 9 -> printJavaPlatformInfo();
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void printMainMenu() {
        System.out.println("\n===== MAIN MENU =====");
        System.out.println("1. Manage Students");
        System.out.println("2. Manage Courses");
        System.out.println("3. Manage Enrollments");
        System.out.println("4. Manage Grades");
        System.out.println("5. Import/Export Data");
        System.out.println("6. Backup Operations");
        System.out.println("7. Generate Reports");
        System.out.println("8. Exit");
        System.out.println("9. Java Platform Info");
    }

    private static void manageStudents() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===== STUDENT MANAGEMENT =====");
            System.out.println("1. Add Student");
            System.out.println("2. List Students");
            System.out.println("3. Update Student");
            System.out.println("4. Deactivate Student");
            System.out.println("5. View Student Profile");
            System.out.println("6. Back to Main Menu");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> listStudents();
                case 3 -> updateStudent();
                case 4 -> deactivateStudent();
                case 5 -> viewStudentProfile();
                case 6 -> back = true;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void addStudent() {
        System.out.println("\n--- Add New Student ---");
        String regNo = getStringInput("Registration Number: ");
        String fullName = getStringInput("Full Name: ");
        String email = getStringInput("Email: ");

        Student student = new Student(regNo, fullName, email);
        studentService.addStudent(student);
        System.out.println("Student added successfully!");
    }

    private static void listStudents() {
        List<Student> students = studentService.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        System.out.println("\n--- All Students ---");
        for (Student student : students) {
            System.out.println(student.getProfileSummary());
        }
    }

    private static void updateStudent() {
        String regNo = getStringInput("Enter student registration number to update: ");
        Student student = studentService.findStudentByRegNo(regNo);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        String newEmail = getStringInput("Enter new email (current: " + student.getEmail() + "): ");
        student.setEmail(newEmail);
        System.out.println("Student updated successfully!");
    }

    private static void deactivateStudent() {
        String regNo = getStringInput("Enter student registration number to deactivate: ");
        boolean success = studentService.deactivateStudent(regNo);

        if (success) {
            System.out.println("Student deactivated successfully!");
        } else {
            System.out.println("Student not found or already deactivated.");
        }
    }

    private static void viewStudentProfile() {
        String regNo = getStringInput("Enter student registration number: ");
        Student student = studentService.findStudentByRegNo(regNo);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.println("\n--- Student Profile ---");
        System.out.println(student);
    }

    private static void manageCourses() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===== COURSE MANAGEMENT =====");
            System.out.println("1. Add Course");
            System.out.println("2. List Courses");
            System.out.println("3. Update Course");
            System.out.println("4. Search Courses");
            System.out.println("5. Back to Main Menu");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> addCourse();
                case 2 -> listCourses();
                case 3 -> updateCourse();
                case 4 -> searchCourses();
                case 5 -> back = true;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void addCourse() {
        System.out.println("\n--- Add New Course ---");
        String code = getStringInput("Course Code: ");
        String title = getStringInput("Course Title: ");
        int credits = getIntInput("Credits: ");
        String instructor = getStringInput("Instructor: ");
        String department = getStringInput("Department: ");

        // Show available semesters
        System.out.println("Available semesters:");
        for (Semester semester : Semester.values()) {
            System.out.println((semester.ordinal() + 1) + ". " + semester);
        }
        int semesterChoice = getIntInput("Select semester: ") - 1;

        if (semesterChoice < 0 || semesterChoice >= Semester.values().length) {
            System.out.println("Invalid semester selection.");
            return;
        }

        Semester semester = Semester.values()[semesterChoice];
        Course course = new Course(code, title, credits, instructor, semester, department);
        courseService.addCourse(course);
        System.out.println("Course added successfully!");
    }

    private static void listCourses() {
        List<Course> courses = courseService.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }

        System.out.println("\n--- All Courses ---");
        for (Course course : courses) {
            System.out.println(course);
        }
    }

    private static void updateCourse() {
        String code = getStringInput("Enter course code to update: ");
        Course course = courseService.findCourseByCode(code);

        if (course == null) {
            System.out.println("Course not found.");
            return;
        }

        String newInstructor = getStringInput("Enter new instructor (current: " + course.getInstructor() + "): ");
        course.setInstructor(newInstructor);
        System.out.println("Course updated successfully!");
    }

    private static void searchCourses() {
        System.out.println("\n--- Search Courses ---");
        System.out.println("1. Search by Instructor");
        System.out.println("2. Search by Department");
        System.out.println("3. Search by Semester");

        int choice = getIntInput("Enter your choice: ");

        switch (choice) {
            case 1 -> {
                String instructor = getStringInput("Enter instructor name: ");
                List<Course> courses = courseService.findCoursesByInstructor(instructor);
                displaySearchResults(courses, "Courses by Instructor: " + instructor);
            }
            case 2 -> {
                String department = getStringInput("Enter department: ");
                List<Course> courses = courseService.findCoursesByDepartment(department);
                displaySearchResults(courses, "Courses in Department: " + department);
            }
            case 3 -> {
                System.out.println("Available semesters:");
                for (Semester semester : Semester.values()) {
                    System.out.println((semester.ordinal() + 1) + ". " + semester);
                }
                int semesterChoice = getIntInput("Select semester: ") - 1;

                if (semesterChoice < 0 || semesterChoice >= Semester.values().length) {
                    System.out.println("Invalid semester selection.");
                    return;
                }

                Semester semester = Semester.values()[semesterChoice];
                List<Course> courses = courseService.findCoursesBySemester(semester);
                displaySearchResults(courses, "Courses in Semester: " + semester);
            }
            default -> System.out.println("Invalid choice.");
        }
    }

    private static void displaySearchResults(List<Course> courses, String title) {
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }

        System.out.println("\n--- " + title + " ---");
        for (Course course : courses) {
            System.out.println(course);
        }
    }

    private static void manageEnrollments() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===== ENROLLMENT MANAGEMENT =====");
            System.out.println("1. Enroll Student in Course");
            System.out.println("2. Unenroll Student from Course");
            System.out.println("3. View Student Enrollments");
            System.out.println("4. Back to Main Menu");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> enrollStudent();
                case 2 -> unenrollStudent();
                case 3 -> viewStudentEnrollments();
                case 4 -> back = true;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void enrollStudent() {
        System.out.println("\n--- Enroll Student ---");
        String regNo = getStringInput("Student Registration Number: ");
        String courseCode = getStringInput("Course Code: ");

        try {
            enrollmentService.enrollStudent(regNo, courseCode);
            System.out.println("Student enrolled successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void unenrollStudent() {
        System.out.println("\n--- Unenroll Student ---");
        String regNo = getStringInput("Student Registration Number: ");
        String courseCode = getStringInput("Course Code: ");

        boolean success = enrollmentService.unenrollStudent(regNo, courseCode);
        if (success) {
            System.out.println("Student unenrolled successfully!");
        } else {
            System.out.println("Enrollment not found or already removed.");
        }
    }

    private static void viewStudentEnrollments() {
        String regNo = getStringInput("Enter student registration number: ");
        List<Course> enrollments = enrollmentService.getStudentEnrollments(regNo);

        if (enrollments.isEmpty()) {
            System.out.println("No enrollments found for this student.");
            return;
        }

        System.out.println("\n--- Student Enrollments ---");
        for (Course course : enrollments) {
            System.out.println(course);
        }
    }

    private static void manageGrades() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===== GRADE MANAGEMENT =====");
            System.out.println("1. Record Grade");
            System.out.println("2. View Student Transcript");
            System.out.println("3. Back to Main Menu");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> recordGrade();
                case 2 -> viewTranscript();
                case 3 -> back = true;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void recordGrade() {
        System.out.println("\n--- Record Grade ---");
        String regNo = getStringInput("Student Registration Number: ");
        String courseCode = getStringInput("Course Code: ");
        double marks = getDoubleInput("Marks Obtained: ");

        try {
            enrollmentService.recordGrade(regNo, courseCode, marks);
            System.out.println("Grade recorded successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewTranscript() {
        String regNo = getStringInput("Enter student registration number: ");
        String transcript = enrollmentService.generateTranscript(regNo);

        if (transcript == null) {
            System.out.println("Student not found or no enrollments.");
            return;
        }

        System.out.println("\n--- Student Transcript ---");
        System.out.println(transcript);
    }

    private static void importExportData() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===== IMPORT/EXPORT DATA =====");
            System.out.println("1. Import Students from CSV");
            System.out.println("2. Import Courses from CSV");
            System.out.println("3. Export Students to CSV");
            System.out.println("4. Export Courses to CSV");
            System.out.println("5. Back to Main Menu");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> importStudents();
                case 2 -> importCourses();
                case 3 -> exportStudents();
                case 4 -> exportCourses();
                case 5 -> back = true;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void importStudents() {
        String filename = getStringInput("Enter CSV filename: ");
        try {
            int count = importExportService.importStudents(filename);
            System.out.println("Imported " + count + " students successfully!");
        } catch (Exception e) {
            System.out.println("Error importing students: " + e.getMessage());
        }
    }

    private static void importCourses() {
        String filename = getStringInput("Enter CSV filename: ");
        try {
            int count = importExportService.importCourses(filename);
            System.out.println("Imported " + count + " courses successfully!");
        } catch (Exception e) {
            System.out.println("Error importing courses: " + e.getMessage());
        }
    }

    private static void exportStudents() {
        String filename = getStringInput("Enter CSV filename: ");
        try {
            int count = importExportService.exportStudents(filename);
            System.out.println("Exported " + count + " students successfully!");
        } catch (Exception e) {
            System.out.println("Error exporting students: " + e.getMessage());
        }
    }

    private static void exportCourses() {
        String filename = getStringInput("Enter CSV filename: ");
        try {
            int count = importExportService.exportCourses(filename);
            System.out.println("Exported " + count + " courses successfully!");
        } catch (Exception e) {
            System.out.println("Error exporting courses: " + e.getMessage());
        }
    }

    private static void backupOperations() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===== BACKUP OPERATIONS =====");
            System.out.println("1. Create Backup");
            System.out.println("2. Show Backup Size");
            System.out.println("3. List Backup Files");
            System.out.println("4. Back to Main Menu");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> createBackup();
                case 2 -> showBackupSize();
                case 3 -> listBackupFiles();
                case 4 -> back = true;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void createBackup() {
        try {
            String backupPath = backupService.createBackup();
            System.out.println("Backup created successfully at: " + backupPath);
        } catch (Exception e) {
            System.out.println("Error creating backup: " + e.getMessage());
        }
    }

    private static void showBackupSize() {
        try {
            long size = backupService.calculateBackupSize();
            System.out.println("Total backup size: " + size + " bytes");
        } catch (Exception e) {
            System.out.println("Error calculating backup size: " + e.getMessage());
        }
    }

    private static void listBackupFiles() {
        try {
            backupService.listBackupFiles();
        } catch (Exception e) {
            System.out.println("Error listing backup files: " + e.getMessage());
        }
    }

    private static void generateReports() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===== REPORTS =====");
            System.out.println("1. Top Students by GPA");
            System.out.println("2. GPA Distribution");
            System.out.println("3. Course Enrollment Statistics");
            System.out.println("4. Back to Main Menu");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> showTopStudents();
                case 2 -> showGpaDistribution();
                case 3 -> showEnrollmentStats();
                case 4 -> back = true;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void showTopStudents() {
        int count = getIntInput("How many top students to show? ");
        ReportGenerator.showTopStudents(studentService.getAllStudents(), count);
    }

    private static void showGpaDistribution() {
        ReportGenerator.showGpaDistribution(studentService.getAllStudents());
    }

    private static void showEnrollmentStats() {
        ReportGenerator.showEnrollmentStats(courseService.getAllCourses(), enrollmentService);
    }

    private static void printJavaPlatformInfo() {
        System.out.println("\n=== Java Platform Information ===");
        System.out.println("Java SE (Standard Edition): Core Java platform for desktop and server applications");
        System.out.println("Java ME (Micro Edition): For embedded systems and mobile devices");
        System.out.println("Java EE (Enterprise Edition): Now Jakarta EE, for large-scale enterprise applications");
        System.out.println("\nThis application is built on Java SE, which provides the core libraries and JVM.");
    }

    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid number.");
            scanner.next();
            System.out.print(prompt);
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        return value;
    }

    private static double getDoubleInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.println("Please enter a valid number.");
            scanner.next();
            System.out.print(prompt);
        }
        double value = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        return value;
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}
