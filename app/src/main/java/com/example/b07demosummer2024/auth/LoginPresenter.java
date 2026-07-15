package com.example.b07demosummer2024.auth;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View view;
    private AuthRepository repository;

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
        this.repository = new AuthRepository();
    }

    @Override
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

    @Override
    public void handleRegisterClick() {
        if (view != null) {
            view.navigateToRegister();
        }
    }

    @Override
    public void onDestroy() {
        this.view = null;
    }
}
