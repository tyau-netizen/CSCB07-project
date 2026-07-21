package com.example.b07demosummer2024.homepage;

import android.os.Bundle;

import com.example.b07demosummer2024.auth.LoginContract;
import com.example.b07demosummer2024.base.BasePresenter;
import com.example.b07demosummer2024.user.SessionManager;
import com.example.b07demosummer2024.user.User;

public class HomePresenter extends BasePresenter<HomeContract.View>
        implements HomeContract.Presenter {
    private final SessionManager sessionManager;

    public HomePresenter() {
        this.sessionManager = SessionManager.getInstance();
    }

    @Override
    public void handleInitialArguments(Bundle args) {
        if (args == null) {
            return;
        } else if (args.containsKey(HomeFragment.KEY_WELCOME_USER)) {
            unpackWelcomeBundle(args.getBundle(HomeFragment.KEY_WELCOME_USER));
        }
    }

    private void unpackWelcomeBundle(Bundle bundle) {
        boolean isGuest = bundle.getBoolean(HomeFragment.KEY_IS_GUEST, false);
        String username;

        if (isGuest) {
            username = "Guest";
        } else {
            username = sessionManager.getCurrentUser().getUsername();
        }
        view.showWelcomeMessage(username);
    }
}
