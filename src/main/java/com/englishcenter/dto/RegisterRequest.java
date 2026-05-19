package com.englishcenter.dto;

public class RegisterRequest {
    private String fullName;
    private String email;
    private String password;

    // Getter và Setter cho fullName
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    // Getter và Setter cho email
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // Getter và Setter cho password
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}