package com.example.b07demosummer2024.auth;

import android.os.Bundle;

import com.example.b07demosummer2024.base.BaseContract;

public interface LoginContract {

    interface View extends BaseContract.View {
        void navigateToRegister();
        void navigateToHome(Bundle args);
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void handleLogin(String email, String password);
        void handleRegisterClick();
    }
}
