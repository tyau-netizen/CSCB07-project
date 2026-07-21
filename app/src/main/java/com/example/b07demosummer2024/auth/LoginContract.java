package com.example.b07demosummer2024.auth;

import com.example.b07demosummer2024.base.BaseContract;

public interface LoginContract {

    interface View extends BaseContract.View {
        void navigateToRegister();
        void navigateToHome();
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void handleLogin(String email, String password);
        void handleRegisterClick();
    }
}
