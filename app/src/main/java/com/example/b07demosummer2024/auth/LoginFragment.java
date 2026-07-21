package com.example.b07demosummer2024.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.base.BaseFragment;
import com.example.b07demosummer2024.databinding.FragmentLoginBinding;

/**
 * A simple {@link Fragment} subclass.
 * Handles user login input and validation
 */
public class LoginFragment extends BaseFragment<FragmentLoginBinding, LoginContract.View,
        LoginContract.Presenter> implements LoginContract.View {

    public LoginFragment() {}

    @NonNull
    @Override
    protected FragmentLoginBinding inflateBinding(@NonNull LayoutInflater inflater,
                                                  @Nullable ViewGroup container) {
        return FragmentLoginBinding.inflate(inflater, container, false);
    }

    @NonNull
    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Click listeners
        binding.loginButton.setOnClickListener(v -> {
            // Get email and password from their text fields
            String email = binding.loginEmailInput.getText().toString().trim();
            String password = binding.loginPasswordInput.getText().toString();

            presenter.handleLogin(email, password);
        });

        binding.registerLink.setOnClickListener(v -> {
            presenter.handleRegisterClick();
        });
    }

    @Override
    public void displayToastMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToRegister() {
        Navigation.findNavController(requireView()).navigate(
                R.id.action_loginFragment_to_registerFragment);
    }

    @Override
    public void navigateToHome() {
        Navigation.findNavController(requireView()).navigate(
                R.id.action_loginFragment_to_homeFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clean up binding to prevent memory leaks
        binding = null;
        // Detach presenter to avoid it calling methods on a null binding
        presenter.detachView();
    }
}