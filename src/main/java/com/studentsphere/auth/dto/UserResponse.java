package com.studentsphere.auth.dto;

public class UserResponse {
    private Long id;
    private String fullName;
    private String email;
    private String studentCollegeEmail;
    private Long collegeId;
    private String collegeName; // Added
    private String profilePictureUrl;

    public UserResponse() {}

    public UserResponse(Long id, String fullName, String email, String studentCollegeEmail, Long collegeId, String collegeName, String profilePictureUrl) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.studentCollegeEmail = studentCollegeEmail;
        this.collegeId = collegeId;
        this.collegeName = collegeName;
        this.profilePictureUrl = profilePictureUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
}
