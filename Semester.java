package edu.ccrm.domain;

public enum Semester {
    SPRING("Spring Semester"),
    SUMMER("Summer Semester"),
    FALL("Fall Semester");
    
    private final String displayName;
    
    Semester(String displayName) {
        this.displayName = displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}
