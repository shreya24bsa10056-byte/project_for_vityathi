package edu.ccrm.service;

import edu.ccrm.domain.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentService {
    private final List<Student> students;
    
    public StudentService() {
        this.students = new ArrayList<>();
    }
    
    public void addStudent(Student student) {
        students.add(student);
    }
    
    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }
    
    public Student findStudentByRegNo(String regNo) {
        Optional<Student> result = students.stream()
                .filter(s -> s.getRegNo().equals(regNo))
                .findFirst();
        return result.orElse(null);
    }
    
    public boolean deactivateStudent(String regNo) {
        Student student = findStudentByRegNo(regNo);
        if (student != null && student.isActive()) {
            student.setActive(false);
            return true;
        }
        return false;
    }
}
