package edu.ccrm.domain;

public enum Grade {
    S(10.0, "Outstanding"),
    A(9.0, "Excellent"),
    B(8.0, "Very Good"),
    C(7.0, "Good"),
    D(6.0, "Average"),
    E(5.0, "Pass"),
    F(0.0, "Fail");
    
    private final double points;
    private final String description;
    
    Grade(double points, String description) {
        this.points = points;
        this.description = description;
    }
    
    public double getPoints() {
        return points;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static Grade fromMarks(double marks) {
        if (marks >= 90) return S;
        if (marks >= 80) return A;
        if (marks >= 70) return B;
        if (marks >= 60) return C;
        if (marks >= 50) return D;
        if (marks >= 40) return E;
        return F;
    }
}
