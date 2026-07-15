package com.example.b07demosummer2024.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.databinding.FragmentLoginBinding;

/**
 * A simple {@link Fragment} subclass.
 * Handles user login input and validation
 */
public class LoginFragment extends Fragment implements LoginContract.View {

    private FragmentLoginBinding binding;
    private LoginContract.Presenter presenter;

    public LoginFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate fragment layout using ViewBinding
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the presenter
        presenter = new LoginPresenter(this);

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
        // Create RegisterFragment
        RegisterFragment registerFragment = new RegisterFragment();

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, registerFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void navigateToHome() {
        // TODO: Actually navigate to the homepage
        displayToastMessage("navigating to home...");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clean up binding to prevent memory leaks
        binding = null;
        if (presenter != null) {
            presenter.onDestroy();
            presenter = null;
        }
    }
}