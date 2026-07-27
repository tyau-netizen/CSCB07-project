package com.example.b07demosummer2024.auth;

import android.os.Bundle;

import com.example.b07demosummer2024.base.BasePresenter;
import com.example.b07demosummer2024.homepage.HomeFragment;
import com.example.b07demosummer2024.user.SessionManager;
import com.example.b07demosummer2024.user.User;

public class LoginPresenter extends BasePresenter<LoginContract.View>
        implements LoginContract.Presenter {

    private final SessionManager sessionManager;
    private final AuthRepository repository;

    public LoginPresenter() {
        this.repository = AuthRepository.getInstance();
        this.sessionManager = SessionManager.getInstance();
    }

    public void handleLogin(String email, String password) {
        if (view == null) return;

        if (email.isEmpty() || password.isEmpty()) {
            view.displayToastMessage("Please fill out all the fields");
            return;
        }

        // Attempt to sign in
        repository.signIn(email, password, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(String email) {
                // Start a user session
                sessionManager.startSession(new SessionManager.SessionCallback() {
                    @Override
                    public void onSuccess(User user) {
                        if (view != null) {
                            Bundle args = HomeFragment.packWelcomeBundle(false);
                            view.navigateToHome(args);
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        view.displayToastMessage("Failed to load user profile: " + e.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                if (view != null) {
                    view.displayToastMessage(errorMessage);
                }
            }
        });
    }

    public void handleRegisterClick() {
        if (view != null) {
            view.navigateToRegister();
        }
    }
}
