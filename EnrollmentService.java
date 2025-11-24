package edu.ccrm.service;

import edu.ccrm.domain.Grade;
import edu.ccrm.domain.Student;
import edu.ccrm.domain.Course;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EnrollmentService {
    private final Map<String, Map<String, Double>> enrollments; 
    private final StudentService studentService;
    private final CourseService courseService;
    
    public EnrollmentService() {
        this.enrollments = new HashMap<>();
        this.studentService = new StudentService();
        this.courseService = new CourseService();
    }
    
    public void enrollStudent(String regNo, String courseCode) {
        Student student = studentService.findStudentByRegNo(regNo);
        Course course = courseService.findCourseByCode(courseCode);
        
        if (student == null) {
            throw new IllegalArgumentException("Student not found: " + regNo);
        }
        if (course == null) {
            throw new IllegalArgumentException("Course not found: " + courseCode);
        }
        if (!student.isActive()) {
            throw new IllegalArgumentException("Student is not active: " + regNo);
        }
        if (!course.isActive()) {
            throw new IllegalArgumentException("Course is not active: " + courseCode);
        }
        
        enrollments.putIfAbsent(regNo, new HashMap<>());
        Map<String, Double> studentEnrollments = enrollments.get(regNo);
        
        if (studentEnrollments.containsKey(courseCode)) {
            throw new IllegalArgumentException("Student already enrolled in this course");
        }
        
        studentEnrollments.put(courseCode, null);
        student.enrollInCourse(courseCode);
    }
    
    public boolean unenrollStudent(String regNo, String courseCode) {
        if (enrollments.containsKey(regNo)) {
            Map<String, Double> studentEnrollments = enrollments.get(regNo);
            if (studentEnrollments.containsKey(courseCode)) {
                studentEnrollments.remove(courseCode);
                
                Student student = studentService.findStudentByRegNo(regNo);
                if (student != null) {
                    student.unenrollFromCourse(courseCode);
                }
                
                return true;
            }
        }
        return false;
    }
    
    public void recordGrade(String regNo, String courseCode, double marks) {
        if (!enrollments.containsKey(regNo)) {
            throw new IllegalArgumentException("Student not enrolled in any courses: " + regNo);
        }
        
        Map<String, Double> studentEnrollments = enrollments.get(regNo);
        if (!studentEnrollments.containsKey(courseCode)) {
            throw new IllegalArgumentException("Student not enrolled in this course: " + courseCode);
        }
        
        studentEnrollments.put(courseCode, marks);
    }
    
    public List<Course> getStudentEnrollments(String regNo) {
        if (!enrollments.containsKey(regNo)) {
            return List.of();
        }
        
        Map<String, Double> studentEnrollments = enrollments.get(regNo);
        return studentEnrollments.keySet().stream()
                .map(courseService::findCourseByCode)
                .collect(Collectors.toList());
    }
    
    public String generateTranscript(String regNo) {
        Student student = studentService.findStudentByRegNo(regNo);
        if (student == null || !enrollments.containsKey(regNo)) {
            return null;
        }
        
        Map<String, Double> studentEnrollments = enrollments.get(regNo);
        if (studentEnrollments.isEmpty()) {
            return "No enrollments found for student: " + regNo;
        }
        
        StringBuilder transcript = new StringBuilder();
        transcript.append("Transcript for: ").append(student.getFullName()).append(" (").append(regNo).append(")\n");
        transcript.append("=".repeat(50)).append("\n");
        
        double totalPoints = 0;
        int totalCredits = 0;
        
        for (Map.Entry<String, Double> entry : studentEnrollments.entrySet()) {
            String courseCode = entry.getKey();
            Double marks = entry.getValue();
            Course course = courseService.findCourseByCode(courseCode);
            
            if (course != null && marks != null) {
                Grade grade = Grade.fromMarks(marks);
                double gradePoints = grade.getPoints() * course.getCredits();
                
                transcript.append(String.format("%-10s %-20s %-5d %-5.2f %-5s %-5.2f\n", 
                        courseCode, course.getTitle(), course.getCredits(), 
                        marks, grade, gradePoints));
                
                totalPoints += gradePoints;
                totalCredits += course.getCredits();
            }
        }
        
        double gpa = totalCredits > 0 ? totalPoints / totalCredits : 0;
        transcript.append("=".repeat(50)).append("\n");
        transcript.append(String.format("Overall GPA: %.2f\n", gpa));
        
        return transcript.toString();
    }
    
    public void setStudentService(StudentService studentService) {
    }
    
    public void setCourseService(CourseService courseService) {
    }
}
