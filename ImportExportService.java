package edu.ccrm.io;

import edu.ccrm.domain.Student;
import edu.ccrm.domain.Course;
import edu.ccrm.domain.Semester;
import edu.ccrm.service.StudentService;
import edu.ccrm.service.CourseService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class ImportExportService {
    private final StudentService studentService;
    private final CourseService courseService;
    
    public ImportExportService() {
        this.studentService = new StudentService();
        this.courseService = new CourseService();
    }
    
    public int importStudents(String filename) throws IOException {
        Path path = Paths.get(filename);
        List<String> lines = Files.readAllLines(path);
        int count = 0;
        
        for (String line : lines) {
            if (line.startsWith("regNo")) continue; // Skip header
            
            String[] parts = line.split(",");
            if (parts.length >= 3) {
                String regNo = parts[0].trim();
                String fullName = parts[1].trim();
                String email = parts[2].trim();
                
                Student student = new Student(regNo, fullName, email);
                studentService.addStudent(student);
                count++;
            }
        }
        
        return count;
    }
    
    public int importCourses(String filename) throws IOException {
        Path path = Paths.get(filename);
        List<String> lines = Files.readAllLines(path);
        int count = 0;
        
        for (String line : lines) {
            if (line.startsWith("code")) continue; // Skip header
            
            String[] parts = line.split(",");
            if (parts.length >= 6) {
                String code = parts[0].trim();
                String title = parts[1].trim();
                int credits = Integer.parseInt(parts[2].trim());
                String instructor = parts[3].trim();
                Semester semester = Semester.valueOf(parts[4].trim().toUpperCase());
                String department = parts[5].trim();
                
                Course course = new Course(code, title, credits, instructor, semester, department);
                courseService.addCourse(course);
                count++;
            }
        }
        
        return count;
    }
    
    public int exportStudents(String filename) throws IOException {
        List<Student> students = studentService.getAllStudents();
        if (students.isEmpty()) {
            return 0;
        }
        
        StringBuilder content = new StringBuilder();
        content.append("regNo,fullName,email,active\n");
        
        for (Student student : students) {
            content.append(student.getRegNo()).append(",")
                   .append(student.getFullName()).append(",")
                   .append(student.getEmail()).append(",")
                   .append(student.isActive()).append("\n");
        }
        
        Path path = Paths.get(filename);
        Files.writeString(path, content.toString(), StandardOpenOption.CREATE, 
                         StandardOpenOption.TRUNCATE_EXISTING);
        
        return students.size();
    }
    
    public int exportCourses(String filename) throws IOException {
        List<Course> courses = courseService.getAllCourses();
        if (courses.isEmpty()) {
            return 0;
        }
        
        StringBuilder content = new StringBuilder();
        content.append("code,title,credits,instructor,semester,department,active\n");
        
        for (Course course : courses) {
            content.append(course.getCode()).append(",")
                   .append(course.getTitle()).append(",")
                   .append(course.getCredits()).append(",")
                   .append(course.getInstructor()).append(",")
                   .append(course.getSemester()).append(",")
                   .append(course.getDepartment()).append(",")
                   .append(course.isActive()).append("\n");
        }
        
        Path path = Paths.get(filename);
        Files.writeString(path, content.toString(), StandardOpenOption.CREATE, 
                         StandardOpenOption.TRUNCATE_EXISTING);
        
        return courses.size();
    }
}
