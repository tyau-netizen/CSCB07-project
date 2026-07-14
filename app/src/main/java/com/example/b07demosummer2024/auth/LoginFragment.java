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
public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

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

        // Click listeners
        binding.loginButton.setOnClickListener(v -> {
            // Get username and password from their text fields
            String username = binding.loginEmailInput.getText().toString().trim();
            String password = binding.loginPasswordInput.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all the fields",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Login stuff will happen",
                        Toast.LENGTH_SHORT).show();
            }
        });

        binding.registerLink.setOnClickListener(v -> {
            // Create RegisterFragment
            RegisterFragment registerFragment = new RegisterFragment();

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, registerFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clean up binding to prevent memory leaks
        binding = null;
    }
}