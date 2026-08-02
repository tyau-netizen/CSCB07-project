package com.example.b07demosummer2024.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.base.BaseFragment;
import com.example.b07demosummer2024.databinding.FragmentRegisterBinding;
import com.example.b07demosummer2024.homepage.HomeFragment;

public class RegisterFragment extends BaseFragment<FragmentRegisterBinding, RegisterContract.View,
        RegisterContract.Presenter> implements RegisterContract.View {

    public RegisterFragment() {}

    @NonNull
    @Override
    protected FragmentRegisterBinding inflateBinding(@NonNull LayoutInflater inflater,
                                                     @Nullable ViewGroup container) {
        return FragmentRegisterBinding.inflate(inflater, container, false);
    }

    @NonNull
    @Override
    protected RegisterPresenter createPresenter() {
        return new RegisterPresenter();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.registerButton.setOnClickListener(v -> {
            String username = binding.registerUsernameInput.getText().toString().trim();
            String email = binding.loginEmailInput.getText().toString().trim();
            String password = binding.loginPasswordInput.getText().toString();

            presenter.handleRegister(username, email, password);
        });

        binding.textViewLogin.setOnClickListener(v -> {
            presenter.handleLoginClick();
        });
    }

    @Override
    public void navigateToLogin() {
        Navigation.findNavController(requireView()).navigate(
                R.id.action_registerFragment_to_loginFragment);
    }

    @Override
    public void navigateToHome(boolean isGuest) {
        Bundle args = HomeFragment.packWelcomeBundle(isGuest);
        Navigation.findNavController(requireView()).navigate(
                R.id.action_registerFragment_to_recyclerViewFragment, args);
    }
}