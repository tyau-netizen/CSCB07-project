package com.example.b07demosummer2024.base;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;

public abstract class BasePresenter<V extends BaseContract.View>
        implements BaseContract.Presenter<V> {
    protected V view;

    @Override
    @CallSuper
    public void attachView(V view) {
        this.view = view;
    }

    @Override
    @CallSuper
    public void detachView() {
        this.view = null;
    }

    @Override
    @CallSuper
    public void onDestroy() {
        detachView();
    }
}
