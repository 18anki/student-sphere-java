package com.studentsphere.auth.dto;

public class SignUpRequest {
    private String fullName;
    private String email;
    private String password;
    private String confirmPassword;
    private String studentCollegeEmail;
    private Long collegeId;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getStudentCollegeEmail() {
        return studentCollegeEmail;
    }

    public void setStudentCollegeEmail(String studentCollegeEmail) {
        this.studentCollegeEmail = studentCollegeEmail;
    }

    public Long getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(Long collegeId) {
        this.collegeId = collegeId;
    }
}
