package com.example.b07demosummer2024.homepage;

import com.example.b07demosummer2024.auth.LoginContract;
import com.example.b07demosummer2024.user.SessionManager;

public class HomePresenter {
    private LoginContract.View view;
    private SessionManager sessionManager;

    public HomePresenter() {
        this.sessionManager = SessionManager.getInstance();
    }
}
