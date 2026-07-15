package com.example.b07demosummer2024.auth;

public interface LoginContract {

    interface View {
        void displayToastMessage(String message);
        void navigateToRegister();
        void navigateToHome();
    }

    interface Presenter {
        void handleLogin(String email, String password);
        void handleRegisterClick();
        void onDestroy();
    }
}
