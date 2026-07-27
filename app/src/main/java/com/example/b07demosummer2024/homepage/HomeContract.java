package com.example.b07demosummer2024.homepage;

import android.os.Bundle;

import com.example.b07demosummer2024.base.BaseContract;

public interface HomeContract {

    interface View extends BaseContract.View {
        void navigateToLogin();
        void showWelcomeMessage(String username);
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void handleInitialArguments(Bundle arguments);
    }
}
