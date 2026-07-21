package com.example.b07demosummer2024.base;

public interface BaseContract {

    interface View {
        void displayToastMessage(String message);
        // TODO: implement loading screen/animation
        // void showLoading();
        // void hideLoading();
    }

    interface Presenter<V extends View> {
        void attachView(V view);
        void detachView();
        void onDestroy();
    }
}
