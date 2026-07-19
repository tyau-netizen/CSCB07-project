package com.example.b07demosummer2024.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthRepository {

    private static AuthRepository instance;
    private final FirebaseAuth auth;

    private AuthRepository() {
        this.auth = FirebaseAuth.getInstance();
    }

    // AuthRepository is a singleton - call getInstance() to instantiate
    public static synchronized AuthRepository getInstance() {
        if (instance == null) {
            instance = new AuthRepository();
        }
        return instance;
    }

    // Callback interface for presenter
    public interface AuthCallback {
        void onSuccess(String email);
        void onFailure(String errorMessage);
    }

    // Attempt to sign in a user with email and password, return result through callback
    public void signIn(String email, String password, AuthCallback callback) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && auth.getCurrentUser() != null) {
                        // Return successfully logged-in user email
                        callback.onSuccess(auth.getCurrentUser().getEmail());
                    } else {
                        // Return error message from Firebase
                        String error = task.getException() != null ?
                                task.getException().getMessage() : "Sign in failed.";
                        callback.onFailure(error);
                    }
                });
    }

    // Return true if there is a user logged in
    public boolean isLoggedIn() {
        return auth.getCurrentUser() != null;
    }

    // Return UID string for current user, if none then return null
    public String getUID() {
        if (isLoggedIn()) {
            return auth.getCurrentUser().getUid();
        }
        return null;
    }

    // Sign out user
    public void signOut() {
        auth.signOut();
    }

}
