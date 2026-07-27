package com.example.b07demosummer2024.user;

import com.example.b07demosummer2024.auth.AuthRepository;

public final class SessionManager {
    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private SessionListener listener;
    private User currentUser;

    private SessionManager() {
        this.userRepository = new UserRepository();
        this.authRepository = AuthRepository.getInstance();
    }

    private static class Holder {
        private static final SessionManager INSTANCE = new SessionManager();
    }

    public static SessionManager getInstance() {
        return Holder.INSTANCE;
    }

    public void startSession(SessionCallback callback) {
        String uid = authRepository.getUID();

        if (uid == null || uid.isEmpty()) {
            if (callback != null) {
                callback.onFailure(new IllegalStateException("No authenticated UID found."));
            }
            return;
        }

        userRepository.fetchUserProfile(uid, new UserRepository.UserFetchCallback() {
            @Override
            public void onSuccess(User user) {
                currentUser = user;
                if (callback != null) {
                    callback.onSuccess(user);
                }
            }

            @Override
            public void onFailure(Exception e) {
                // Defensive check: legacy users may not have user data yet
                if (e instanceof UserNotFoundException) {
                    User user = new User(uid, "noUsername");

                    userRepository.saveNewUserProfile(user, new UserRepository.UserSaveCallback() {
                        @Override
                        public void onSuccess() {
                            currentUser = user;
                            if (callback != null) {
                                callback.onSuccess(user);
                            }
                        }

                        @Override
                        public void onFailure(Exception e) {
                            if (callback != null) {
                                callback.onFailure(new Exception(
                                        "Failed to create user data: " + e.getMessage(), e));
                            }
                        }
                    });
                } else {
                    currentUser = null;
                    if (callback != null) {
                        callback.onFailure(e);
                    }
                }
            }
        });

    }

    public void endSession() {
        this.currentUser = null;
        if (listener != null) {
            listener.onSessionEnded();
        }
    }

    public boolean isLoggedIn() {
        return this.currentUser != null;
    }

    public boolean isAdminSession() {
        if (isLoggedIn()) {
            return this.currentUser.isAdmin();
        }
        return false;
    }

    public void setSessionListener(SessionListener listener) {
        this.listener = listener;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public interface SessionCallback {
        void onSuccess(User user);
        void onFailure(Exception e);
    }
    public interface SessionListener {
        void onSessionEnded();
    }
}
