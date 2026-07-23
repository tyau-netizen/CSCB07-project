package com.example.b07demosummer2024.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

public abstract class BaseFragment<VB extends ViewBinding, V extends BaseContract.View,
        P extends BaseContract.Presenter<V>>
        extends Fragment implements BaseContract.View {
    protected VB binding;
    protected P presenter;

    // Factory method for subclasses to inflate their ViewBinding
    @NonNull
    protected abstract VB inflateBinding(@NonNull LayoutInflater inflater,
                                         @Nullable ViewGroup container);

    // Factory method for subclasses to create their associated presenter
    @NonNull
    protected abstract P createPresenter();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate fragment layout using ViewBinding
        binding = inflateBinding(inflater, container);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Create and attach presenter
        if (presenter == null) {
            presenter = createPresenter();
        }
        presenter.attachView((V) this);
    }

    @Override
    public void onDestroyView() {
        // Clean up binding to prevent memory leaks
        binding = null;
        // Detach presenter to avoid it calling methods on a null binding
        presenter.detachView();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        presenter = null;
        super.onDestroy();
    }

    @Override
    public void displayToastMessage(String message) {
        if (getContext() != null && message != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
