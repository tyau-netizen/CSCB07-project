package com.example.b07demosummer2024.user;

public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
        super("User does not exist in database");
    }
}
