package com.example.b07demosummer2024.user;

public final class SessionManager {
    private SessionListener listener;
    private User currentUser;

    private SessionManager() {}

    private static class Holder {
        private static final SessionManager INSTANCE = new SessionManager();
    }

    public static SessionManager getInstance() {
        return Holder.INSTANCE;
    }

    public void startSession(User user) {
        this.currentUser = user;
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

    public void setSessionListener(SessionListener listener) {
        this.listener = listener;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public interface SessionListener {
        void onSessionEnded();
    }
}
