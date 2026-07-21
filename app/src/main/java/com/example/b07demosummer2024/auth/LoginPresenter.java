package com.example.b07demosummer2024.auth;

import com.example.b07demosummer2024.base.BasePresenter;

public class LoginPresenter extends BasePresenter<LoginContract.View>
        implements LoginContract.Presenter {
    private AuthRepository repository;

    public LoginPresenter() {
        this.repository = AuthRepository.getInstance();
    }

    public void handleLogin(String email, String password) {
        if (view == null) return;

        if (email.isEmpty() || password.isEmpty()) {
            view.displayToastMessage("Please fill out all the fields");
            return;
        }

        repository.signIn(email, password, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(String email) {
                if (view != null) {
                    view.displayToastMessage("Logged in " + email);
                    view.navigateToHome();
                }
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
