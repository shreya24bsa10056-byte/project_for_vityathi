package edu.ccrm.service;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.Semester;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CourseService {
    private final List<Course> courses;
    
    public CourseService() {
        this.courses = new ArrayList<>();
    }
    
    public void addCourse(Course course) {
        courses.add(course);
    }
    
    public List<Course> getAllCourses() {
        return new ArrayList<>(courses);
    }
    
    public Course findCourseByCode(String code) {
        return courses.stream()
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
    
    public List<Course> findCoursesByInstructor(String instructor) {
        return courses.stream()
                .filter(c -> c.getInstructor().equalsIgnoreCase(instructor))
                .collect(Collectors.toList());
    }
    
    public List<Course> findCoursesByDepartment(String department) {
        return courses.stream()
                .filter(c -> c.getDepartment().equalsIgnoreCase(department))
                .collect(Collectors.toList());
    }
    
    public List<Course> findCoursesBySemester(Semester semester) {
        return courses.stream()
                .filter(c -> c.getSemester() == semester)
                .collect(Collectors.toList());
    }
}
