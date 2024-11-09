package com.example.finalproject;

public class UserModel {

    private String fullName;
    private String userEmail;
    private boolean isUser;  // Use boolean instead of String
    private boolean isAdmin; // Use boolean instead of String

    public UserModel() {
        // Default constructor required for Firestore
    }

    public UserModel(String fullName, String userEmail, boolean isUser, boolean isAdmin) {
        this.fullName = fullName;
        this.userEmail = userEmail;
        this.isUser = isUser;
        this.isAdmin = isAdmin;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public boolean getIsUser() {
        return isUser;
    }

    public void setIsUser(boolean isUser) {
        this.isUser = isUser;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
