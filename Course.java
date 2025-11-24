package edu.ccrm.domain;

public class Course {
    private final String code;
    private final String title;
    private final int credits;
    private String instructor;
    private final Semester semester;
    private final String department;
    private boolean active;
    
    public Course(String code, String title, int credits, String instructor, 
                 Semester semester, String department) {
        this.code = code;
        this.title = title;
        this.credits = credits;
        this.instructor = instructor;
        this.semester = semester;
        this.department = department;
        this.active = true;
    }
    
    public String getCode() { return code; }
    public String getTitle() { return title; }
    public int getCredits() { return credits; }
    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }
    public Semester getSemester() { return semester; }
    public String getDepartment() { return department; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    
    @Override
    public String toString() {
        return String.format("Course: %s - %s (%d credits)\n" +
                "  Instructor: %s, Department: %s\n" +
                "  Semester: %s, Status: %s",
                code, title, credits, instructor, department,
                semester, active ? "Active" : "Inactive");
    }
}
