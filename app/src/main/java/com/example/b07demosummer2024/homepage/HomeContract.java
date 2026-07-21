package com.example.b07demosummer2024.homepage;

public interface HomeContract {

    interface View {
        void displayToastMessage(String message);
        // void navigateToLogin();
    }

    interface Presenter {
        void attachView(HomeContract.View view);
        void detachView();
        void onDestroy();
    }
}
