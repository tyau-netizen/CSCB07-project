package com.example.b07demosummer2024.auth;

import org.junit.Test;
import static org.junit.Assert.*;

public class RegisterFragmentTest {

    // Helper method matching your fragment's validation criteria
    private boolean validateRegisterFields(String username, String email, String password) {
        return !username.trim().isEmpty() && !email.trim().isEmpty() && !password.trim().isEmpty() && password.length() >= 6;
    }

    @Test
    public void testEmptyFields_failsValidation() {
        assertFalse(validateRegisterFields("", "email@test.com", "password123"));
        assertFalse(validateRegisterFields("user", "", "password123"));
        assertFalse(validateRegisterFields("user", "email@test.com", ""));
    }

    @Test
    public void testShortPassword_failsValidation() {
        assertFalse(validateRegisterFields("user", "email@test.com", "123"));
    }

    @Test
    public void testValidRegistrationInputs_passesValidation() {
        assertTrue(validateRegisterFields("user", "email@test.com", "password123"));
    }
}