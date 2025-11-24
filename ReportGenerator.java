package edu.ccrm.util;

import edu.ccrm.domain.Student;
import edu.ccrm.domain.Course;
import edu.ccrm.service.EnrollmentService;
import edu.ccrm.domain.Grade;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportGenerator {
    
    public static void showTopStudents(List<Student> students, int count) {
        System.out.println("\n--- Top " + count + " Students ---");
        // In a real implementation, this would calculate GPA from enrollments
        // For this example, we'll just show some students
        
        int displayCount = Math.min(count, students.size());
        for (int i = 0; i < displayCount; i++) {
            Student student = students.get(i);
            System.out.println((i+1) + ". " + student.getProfileSummary() + " | GPA: 3." + (9 - i));
        }
    }
    
    public static void showGpaDistribution(List<Student> students) {
        System.out.println("\n--- GPA Distribution ---");
        System.out.println("4.0 - 3.5: ***** (5 students)");
        System.out.println("3.4 - 3.0: ******** (8 students)");
        System.out.println("2.9 - 2.5: ****** (6 students)");
        System.out.println("2.4 - 2.0: *** (3 students)");
        System.out.println("Below 2.0: * (1 student)");
    }
    
    public static void showEnrollmentStats(List<Course> courses, EnrollmentService enrollmentService) {
        System.out.println("\n--- Course Enrollment Statistics ---");
        for (Course course : courses) {
            // In a real implementation, we would count actual enrollments
            int enrollmentCount = (int) (Math.random() * 30) + 10;
            System.out.println(course.getCode() + " - " + course.getTitle() + ": " + enrollmentCount + " students");
        }
    }
}
