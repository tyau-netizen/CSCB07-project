package com.example.b07demosummer2024.auth;

import com.example.b07demosummer2024.base.BaseContract;

public interface RegisterContract {

    interface View extends BaseContract.View {
        void navigateToLogin();
        void navigateToHome(boolean isGuest);
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void handleRegister(String username, String email, String password);
        void handleLoginClick();
    }
}