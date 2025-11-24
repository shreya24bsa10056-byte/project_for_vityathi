package edu.ccrm.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Student 
{
    private static int nextId = 1;
    
    private final int id;
    private final String regNo;
    private String fullName;
    private String email;
    private boolean active;
    private final LocalDate createdDate;
    private LocalDate updatedDate;
    private final List<String> enrolledCourses;
    
    public Student(String regNo, String fullName, String email) 
    {
        this.id = nextId++;
        this.regNo = regNo;
        this.fullName = fullName;
        this.email = email;
        this.active = true;
        this.createdDate = LocalDate.now();
        this.updatedDate = LocalDate.now();
        this.enrolledCourses = new ArrayList<>();
    }
    
    // Getters and setters
    public int getId() { return id; }
    public String getRegNo() { return regNo; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { 
        this.fullName = fullName; 
        this.updatedDate = LocalDate.now();
    }
    public String getEmail() { return email; }
    public void setEmail(String email) { 
        this.email = email; 
        this.updatedDate = LocalDate.now();
    }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { 
        this.active = active; 
        this.updatedDate = LocalDate.now();
    }
    public LocalDate getCreatedDate() { return createdDate; }
    public LocalDate getUpdatedDate() { return updatedDate; }
    public List<String> getEnrolledCourses() { return enrolledCourses; }
    
    public void enrollInCourse(String courseCode) {
        if (!enrolledCourses.contains(courseCode)) {
            enrolledCourses.add(courseCode);
            this.updatedDate = LocalDate.now();
        }
    }
    
    public void unenrollFromCourse(String courseCode) {
        enrolledCourses.remove(courseCode);
        this.updatedDate = LocalDate.now();
    }
    
    public String getProfileSummary() {
        return String.format("ID: %d, RegNo: %s, Name: %s, Email: %s, Active: %s", 
                id, regNo, fullName, email, active ? "Yes" : "No");
    }
    
    @Override
    public String toString() {
        return String.format("Student Profile:\n" +
                "  ID: %d\n" +
                "  Registration No: %s\n" +
                "  Name: %s\n" +
                "  Email: %s\n" +
                "  Status: %s\n" +
                "  Created: %s\n" +
                "  Last Updated: %s\n" +
                "  Enrolled Courses: %d",
                id, regNo, fullName, email, 
                active ? "Active" : "Inactive",
                createdDate, updatedDate, enrolledCourses.size());
    }
}
