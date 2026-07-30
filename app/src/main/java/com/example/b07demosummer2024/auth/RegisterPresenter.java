package com.example.b07demosummer2024.auth;

import com.example.b07demosummer2024.base.BasePresenter;
import com.example.b07demosummer2024.user.SessionManager;
import com.example.b07demosummer2024.user.User;
import com.example.b07demosummer2024.user.UserRepository;

public class RegisterPresenter extends BasePresenter<RegisterContract.View>
        implements RegisterContract.Presenter {

    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final SessionManager sessionManager;

    public RegisterPresenter() {
        this.authRepository = AuthRepository.getInstance();
        this.userRepository = new UserRepository();
        this.sessionManager = SessionManager.getInstance();
    }

    // Test constructor for injecting mocks
    public RegisterPresenter(AuthRepository authRepository, UserRepository userRepository, SessionManager sessionManager) {
        this.authRepository = authRepository;
        this.userRepository = userRepository;
        this.sessionManager = sessionManager;
    }

    @Override
    public void handleRegister(String username, String email, String password) {
        if (view == null) return;

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            view.displayToastMessage("Please fill in all the fields");
            return;
        }

        authRepository.signUp(email, password, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess() {
                String uid = authRepository.getUID();
                if (uid == null) {
                    if (view != null) {
                        view.displayToastMessage("Registration failed: Missing user ID.");
                    }
                    return;
                }

                User newUser = new User(uid, username);
                userRepository.saveNewUserProfile(newUser, new UserRepository.UserSaveCallback() {
                    @Override
                    public void onSuccess() {
                        sessionManager.startSession(new SessionManager.SessionCallback() {
                            @Override
                            public void onSuccess() {
                                if (view != null) {
                                    view.navigateToHome(false);
                                }
                            }

                            @Override
                            public void onFailure(Exception e) {
                                if (view != null) {
                                    view.displayToastMessage("Failed to start session: " + e.getMessage());
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(Exception e) {
                        if (view != null) {
                            view.displayToastMessage("Failed to save user: " + e.getMessage());
                        }
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

    @Override
    public void handleLoginClick() {
        if (view != null) {
            view.navigateToLogin();
        }
    }
}